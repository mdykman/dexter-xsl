/**
 * dexter (c) 2007,-2009 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.DexterException;
import org.dykman.dexter.DexterHaltException;
import org.dykman.dexter.descriptor.CrossPathResolver;
import org.dykman.dexter.dexterity.DexteritySyntaxException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XSLTDocSequencer extends BaseTransformSequencer
{
	private String indent = "no";
	private String method = "html";
	private String mediaType = "text/html";

	private String levelCounter = "DexterDepthLevel";
	private List<String> dexterNamespaces;
	
	public static final String XSLTEXT = "xsl:text";
	public static final String XSLVALUEOF = "xsl:value-of";
	public static final String XSLTEMPLATE = "xsl:template";
	public static final String XSLFOREACH = "xsl:for-each";
	public static final String XSLVARIABLE = "xsl:variable";
	public static final String XSLELEMENT = "xsl:element";
	
	
	private DocumentBuilder builder;

	
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		
		dbf.setExpandEntityReferences(false);
		dbf.setCoalescing(true);
		dbf.setIgnoringComments(false);
//		builder = dbf.newDocumentBuilder();
	}

	private Map<String, Document> finished = new HashMap<String, Document>();

	private Map<String, Map<String, DocumentFragment>> valMap = new HashMap<String, Map<String, DocumentFragment>>();
	private Map<String, Map<String, List<Element>>> replacementMap = new HashMap<String, Map<String, List<Element>>>();

	private int pathc = 0;

	// output node stack..  a little hacky, a little arbitrary, but simple.
	private short[] nodeTypes = new short[8092];
	private int nodeLevel = 0;

	private List<String> idNames;

	private Stack<Document> docStack = new Stack<Document>();
	private Stack<String> nameStack = new Stack<String>();
	private Stack<Node> nodeStack = new Stack<Node>();
	private Stack<Element> stylesheetStack = new Stack<Element>();

	private Document currentDocument;
	private Node currentNode;
	private Node currentStylesheet;

	private String filename;
	private String encoding;

	public XSLTDocSequencer(Dexter dexter,
			DocumentBuilder builder, String name, String encoding) throws Exception
	{
		super(dexter);
		this.builder = builder;
		this.encoding = encoding;
		this.filename = name;
	}

	public void setIdNames(List ids)
	{
		idNames = ids;
	}

	Stack<String> iteratorStack = new Stack<String>();

	public void startIterator(CrossPathResolver resolver,String path)
	{
		StringBuffer sb = new StringBuffer();
		String[] pp = path.split("[|]");
		for(int i = 0; i < pp.length; ++i)
		{
			sb.append(translateXSLPath(resolver,pp[i]));
			if(i+1 < pp.length) sb.append("|");
		}
		path = sb.toString();
		iteratorStack.push(path);
		Element element = currentDocument.createElement(XSLFOREACH);
		element.setAttribute("select", path);
		currentNode.appendChild(element);
		
		Element var = currentDocument.createElement(XSLVARIABLE);
		var.setAttribute("name", levelCounter + iteratorStack.size());
		Element vo = currentDocument.createElement(XSLVALUEOF);
		vo.setAttribute("select", "position()");
		var.appendChild(vo);
		element.appendChild(var);

		pushNode(element);
	}

	public void endIterator()
	{
		popNode();
		iteratorStack.pop();
		--pathc;
	}

	public void mapNode(
			CrossPathResolver resolver, 
			String []path, 
			String def,
			boolean force,
			boolean disable_escape) {
		currentNode.appendChild(
				valueTemplate(resolver,path, def, "xsl:value-of",force,disable_escape));
	}

	public void copyNodes(CrossPathResolver resolver,String path, String def, boolean children)
	{
		String av = path;
		if(children) av = path + "/*";
		Element valueOf = callTemplateEvaluator(resolver, av, "xsl:copy-of",children);
		Element map;
		if (def != null)
		{
			Element choose = currentDocument.createElement("xsl:choose");
			Element when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", translateXSLPath(resolver,path));
			when.appendChild(valueOf);
			choose.appendChild(when);
			map = choose;
		}
		else
		{
			map = valueOf;
		}

		if (def != null)
		{
			Element otherwise = currentDocument.createElement("xsl:otherwise");
			otherwise.appendChild(currentDocument.createTextNode(def));
			map.appendChild(otherwise);
		}
		currentNode.appendChild(map);
	}
	
	public void mapAttribute(CrossPathResolver resolver,
			String name, String[] path, String def, boolean force, boolean disable_escape)
	{

		Element element = currentDocument.createElement("xsl:attribute");
		name = translateName(name);
		element.setAttribute("name", name);

		element.appendChild(valueTemplate(resolver,path, def,"xsl:value-of",force,disable_escape));

		currentNode.appendChild(element);
	}

	Pattern functiondesc = Pattern.compile("^([a-zA-Z][a-zA-Z0-9._-]+[(])(.*)");

	protected String getInnerExpresion(String path) {
		Matcher matcher = functiondesc.matcher(path);
		if(matcher.matches()) {
			String m = matcher.group(1);
			return getInnerExpresion(path.substring(m.length()));
		} else {
			int n = path.indexOf(')');
			if(n != -1) {
				return path.substring(0,n);
			} else {
				return path;
			}
		}
		
	}
	
	protected Element callTemplateEvaluator(
			CrossPathResolver resolver, String path, 
			String evalTag, boolean disableEscaping) {
		Matcher matcher = functiondesc.matcher(path);
		if(matcher.matches()) {
			String s = matcher.group(1);
			String nn = s.substring(0,s.length() - 1);
			
			Element caller = currentDocument.createElement("xsl:call-template");
			caller.setAttribute("name",nn);
			
			Element p1 = currentDocument.createElement("xsl:with-param");
			
			
			
			p1.setAttribute("name","param1");
			path = matcher.group(2);
			int n = path.indexOf(')');
			if(n != -1) {
				path = path.substring(0,n);
			}
			
			p1.setAttribute("select", translateXSLPath(resolver, path));
			dexter.loadTemplate((Element)currentStylesheet,nn);
//			p1.appendChild(callTemplateEvaluator(
//					resolver,matcher.group(2),evalTag, disableEscaping));
			caller.appendChild(p1);
			
			return caller;
		} else {
			Element valueOf = currentDocument.createElement(evalTag);
			int n = path.indexOf(')');
			if(n != -1) {
				path = path.substring(0,n);
			}
			valueOf.setAttribute("select", translateXSLPath(resolver, path));
			if(disableEscaping) {
				valueOf.setAttribute("disable-output-escaping", "yes");;
			}
			return valueOf;
		}
	}

	protected Element valueTemplate(
		CrossPathResolver resolver, String[] path, String def,
		String evalTag, boolean force, boolean disable_escape)
	{
		if (def != null || path.length > 1) {
			StringBuffer attrTest = new StringBuffer();
			boolean first = true;
			if (path.length == 1) {
				String p = getInnerExpresion(path[0]);
				p = translateXSLPath(resolver,p);
				if(force) p = "(" + "length(string(" + p + ")) > 0)";
				attrTest.append(p);
			} else for (int i = 0; i < path.length; ++i) {
				if (i % 2 != 0) {
					String p = getInnerExpresion(path[i]);
					 p = translateXSLPath(resolver,p);
					if (!first) attrTest.append(" and ");
					else first = false;
					
					if(force) p = "(" + "length(string(" + p + ")) > 0)";
					attrTest.append(p);
				}
			}

			Element choose = currentDocument.createElement("xsl:choose");
			Element when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", attrTest.toString());
			choose.appendChild(when);
			// if path.length > 1, then we alternating literals and paths
			if(path.length == 1) {
				Element valueOf = callTemplateEvaluator(
						resolver,path[0],evalTag,disable_escape);
				when.appendChild(valueOf);
			} 
			else for (int i = 0; i < path.length; ++i) {
				if (i % 2 == 0)  {
					if(path[i].length() > 0) {
						when.appendChild(textContainer(path[i]));
					}
				} else {
					Element valueOf = callTemplateEvaluator(
						resolver,path[i],evalTag,disable_escape);
					when.appendChild(valueOf);
				}
			}

			Element otherwise = currentDocument.createElement("xsl:otherwise");
			otherwise.appendChild(textContainer(def == null ? "" : def));
			choose.appendChild(otherwise);
			return choose;
		} else {
			Element valueOf = callTemplateEvaluator(
					resolver,path[0],evalTag,disable_escape);
			return valueOf;
		}
	}

	public void setAttribute(String key, String value)
	{
		key = translateName(key);
		if (idNames.contains(key))
		{
			setIdentityAttribute(key, value);
		}
		else
		{
			Element element = currentDocument.createElement("xsl:attribute");
			element.setAttribute("name", key);
			Text text = currentDocument.createTextNode(value);
			element.appendChild(text);
			currentNode.appendChild(element);
		}
	}
	
	public void setIdentityAttribute(String key, String value)
	{
		DocumentFragment fragment = processIdentityValueTemplate(key, value);
		Element element = currentDocument.createElement("xsl:attribute");
		element.setAttribute("name", key);
		element.appendChild(fragment);
		currentNode.appendChild(element);
	}

	public void startTest(CrossPathResolver resolver, String tests)
	{
		Element element = currentDocument.createElement("xsl:if");
		element.setAttribute("test", generateXSLTest(resolver,tests));
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endTest()
	{
		popNode();
	}

	public void startCaseBlock()
	{
		Element element = currentDocument.createElement("xsl:choose");
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endCaseBlock()
	{
		popNode();
	}

	public void startCase(CrossPathResolver resolver,String tests)
	{
		Element element = null;
		if (tests.length() > 0)
		{
			element = currentDocument.createElement("xsl:when");
			element.setAttribute("test", generateXSLTest(resolver,tests));
		}
		else
		{
			element = currentDocument.createElement("xsl:otherwise");
		}
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endCase()
	{
		popNode();
	}

	public void startSubdoc(CrossPathResolver resolver,String altDoc, String name, String match,
	      boolean keepSubDoc)
	{
		String fn = altDoc == null ? filename + '-' + name : altDoc;
		String tn = fn.replaceAll("[^a-zA-Z0-9]","-");

		Element element = currentDocument.createElement("xsl:include");
		element.setAttribute("href", fn + ".xsl");

		NodeList ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
		Node output = ch.item(0);
		currentStylesheet.insertBefore(element, output);
		currentNode.appendChild(createExternalTemplateCall(resolver,match,tn));

		Document document = createStub(resolver,match,tn);
		if (altDoc != null)	name = name + ".dispose";
		pushDoc(document, name);
		
// create the main entry point template to handle cases where it is invoked independently
		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("match", "/");
		template.appendChild(createExternalTemplateCall(resolver,match,tn));

		ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
		output = ch.item(0);
		currentStylesheet.insertBefore(template, output);
	}

	public void endSubdoc()
	{
		// createStub, called by startSubDoc does 2 pushes, so we do 2 pops
		popNode();
		popNode();
		popStylesheet();
		popDoc();
	}

	public void setDocType(DocumentType dt)
	{
		// this should always return the current output element
		Element element = (Element)currentStylesheet.getFirstChild();
		element.setAttribute("doctype-public",dt.getPublicId());
		if(dt.getSystemId() != null)
		{
			element.setAttribute("doctype-system",dt.getSystemId());
		}
	}
	
	protected void indentWithWhitespace() {
		if(! lastWasEntity) {
			int n = nodeStack.size();
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			for(int i = 0; i < n; ++i) {
				sb.append("\t");
			}
			currentNode.appendChild(currentDocument.createTextNode(sb.toString()));
		}
	}
	boolean lastWasEntity = false;
	public void startNode(String name, int type)
	{
		nodeTypes[nodeLevel++] = (short) type;
		switch (type)
		{
			case Node.DOCUMENT_NODE:
			{
				Document document = createStub(null,"/");
				pushDoc(document, filename);
//				pushNode(document);
				lastWasEntity = false;
			}
			break;
			case Node.ELEMENT_NODE:
			{
				indentWithWhitespace();
				Element el = currentDocument.createElement(XSLELEMENT);
				el.setAttribute("name", name);
				currentNode.appendChild(el);
				pushNode(el);
				lastWasEntity = false;
			}
			break;
			case Node.TEXT_NODE:
			{
				indentWithWhitespace();
				Element el = currentDocument.createElement(XSLTEXT);
				el.setTextContent(name);
				currentNode.appendChild(el);
				lastWasEntity = false;
			}
			break;
			case Node.CDATA_SECTION_NODE:
			{
				indentWithWhitespace();
				CDATASection cd = currentDocument.createCDATASection(name);
				currentNode.appendChild(cd);
				lastWasEntity = false;
			}
			break;
			case Node.ENTITY_NODE:
				// TODO: this is screwed up for this case, I am sure...
			case Node.ENTITY_REFERENCE_NODE:
			{
				Node n = translateEntityReference(name);
				currentNode.appendChild(n);
				lastWasEntity = true;
			}
			break;

			case Node.COMMENT_NODE:
			{
				indentWithWhitespace();
				if(dexter.isPropigateComments())
				{
					Comment comment = currentDocument.createComment(name);
					currentNode.appendChild(comment);
				}
				lastWasEntity = false;
			}
			break;
			default:
			{
				Dexter.reportInternalError("FATAL: encountered  an unhandle node type: " + type, null);
				throw new DexterHaltException("internal exception - unhandled node type: " + type);
			}
		}
	}

	public void endNode()
	{
		short type = nodeTypes[--nodeLevel];

		if(false)
		{
			throw new RuntimeException();
		}

		switch (type)
		{
			case Node.DOCUMENT_NODE:
				popStylesheet();
				popDoc();
			case Node.ELEMENT_NODE:
				popNode();
			break;
		}
	}

	protected Node translateEntityReference(String ref)
	{
		String val = dexter.getEntity(ref);
		if(val == null)
		{
			throw new DexterException("unrecognized entity reference used: " + ref);
		}
		val = val.trim();
		
		Map<String,String> ent = 
			(Map<String,String>)currentDocument.getUserData("entity-map");
		if(ent == null)
		{
			ent = new TreeMap<String,String>();
			currentDocument.setUserData("entity-map",ent,null);
		}
		ent.put(ref, val);
		return  currentDocument.createTextNode("&" + ref + ';');
	}

	protected Document createStub(CrossPathResolver resolver,String match) {
		return createStub(resolver,match, null);
	}

	protected Element createExternalTemplateCall(CrossPathResolver resolver,String match,String name) {
		
		String trmatch = translateXSLPath(resolver, match);
		Element choose = currentDocument.createElement("xsl:choose");
		Element when = currentDocument.createElement("xsl:when");
		when.setAttribute("test", trmatch);
		Element apply = currentDocument.createElement("xsl:apply-templates");
		apply.setAttribute("select", trmatch);
		apply.setAttribute("mode", name);
		when.appendChild(apply);
		choose.appendChild(when);

		Element otherwise = currentDocument.createElement("xsl:otherwise");
		Element call = currentDocument.createElement("xsl:call-template");
		call.setAttribute("name", name);
		otherwise.appendChild(call);
		choose.appendChild(otherwise);
		
		return choose;
	}
	
	protected Document createStub(CrossPathResolver resolver,String match,String name)
	{
		DOMImplementation impl = builder.getDOMImplementation();
		DocumentType dt = impl.createDocumentType("stylesheet", null, 
				"http://www.w3.org/1999/XSL/Transform");
		
		Document document = impl.createDocument(
				"http://www.w3.org/1999/XSL/Transform",
				"xsl:stylesheet", dt);

		Element style = document.getDocumentElement();
		style.setAttribute("version", "1.0");

		pushStylesheet(style);
		pushNode(style);

		Element output = document.createElement("xsl:output");
		output.setAttribute("encoding", encoding);
		output.setAttribute("indent", indent);
		if(mediaType != null) output.setAttribute("media-type", mediaType);
		output.setAttribute("method", method);

		style.appendChild(output);
		tagTemplate(output);

		Element template = document.createElement(XSLTEMPLATE);
		if(match != null) {
			template.setAttribute("match", match);
			style.appendChild(template);
		}
		if(name != null) {
			template.setAttribute("name", name);
			template.setAttribute("mode", name);
			style.appendChild(template);
		}
//		style.appendChild(document.createTextNode("\n"));

		pushNode(template);
		return document;
	}

	private void blockComment(Element element, String[] lines)
	{
		Document document = element.getOwnerDocument();
		for (int i = 0; i < lines.length; ++i)
		{
			element.appendChild(document.createTextNode("\n"));
			element.appendChild(document.createComment(lines[i]));
		}
		element.appendChild(document.createTextNode("\n"));
	}

	private void tagTemplate(Element element)
	{
		String[] TAG = new String[] {
				" generated by dexter from `" + filename + "'  ",
		 };
		blockComment(element, TAG);
	}
/*
	public Node getTextExpression(String key, String value)
	{
		Map<String, DocumentFragment> vt = valMap.get(key);
		if (vt != null && vt.containsKey(value))
		{
			Node n = vt.get(value);
			return n.cloneNode(true);
		}
		else
		{
			// send back a placeholder and file for auto-replacement
			Element tmp = currentDocument.createElement("DEXTERTEMP");
			Map<String, List<Element>> im = replacementMap.get(key);
			if (im == null)
			{
				im = new HashMap<String, List<Element>>();
				replacementMap.put(key, im);
			}
			List<Element> elst = im.get(value);
			if (elst == null)
			{
				elst = new ArrayList<Element>();
				im.put(value, elst);
			}
			elst.add(tmp);
			return tmp;
		}
	}
*/
	protected DocumentFragment processIdentityValueTemplate(String key,
	      String value)
	{
		DocumentFragment fragment = createIdentityValueExpression(currentDocument,
		      value);
		Map<String, List<Element>> im = replacementMap.get(key);
		if (im != null)
		{
			List<Element> els = im.get(value);
			if (els != null)
			{
				Iterator<Element> it = els.iterator();
				while (it.hasNext())
				{
					Element el = it.next();
					Node parent = el.getParentNode();
					Node repl = fragment.cloneNode(true);
					Document eldoc = el.getOwnerDocument();
					eldoc.adoptNode(repl);
					parent.replaceChild(repl, el);
				}
			}
		}
		Map<String, DocumentFragment> kk = valMap.get(key);
		if (kk == null)
		{
			kk = new HashMap<String, DocumentFragment>();
			valMap.put(key, kk);
		}
		kk.put(value, (DocumentFragment) fragment.cloneNode(true));
		return fragment;
	}

	public Element textContainer(String content)
	{
		return textContainer(currentDocument, content);
	}

	public Element textContainer(Document document, String content)
	{
		Element element = document.createElement(XSLTEXT);
		element.setTextContent(content);
		return element;
	}

	protected DocumentFragment createIdentityValueExpression(
			Document document, 
			String value) 
	{
		DocumentFragment fragment = document.createDocumentFragment();

		Element element = currentDocument.createElement(XSLTEXT);
		element.appendChild(currentDocument.createTextNode(value));
		fragment.appendChild(element);
		
		for (int i = 1; i <= iteratorStack.size(); ++i)
		{
			element = currentDocument.createElement(XSLTEXT);
			element.appendChild(currentDocument.createTextNode("-"));
			Element vo = currentDocument.createElement(XSLVALUEOF);
			vo.setAttribute("select", "$" + levelCounter + i);
			fragment.appendChild(element);
			fragment.appendChild(vo);
		}
		return fragment;
	}

	public Node getCurrentNode()
	{
		return currentNode;
	}


	private void popStylesheet()
	{
		stylesheetStack.pop();
		if (stylesheetStack.size() > 0)
			currentStylesheet = stylesheetStack.peek();
		else
			currentStylesheet = null;
	}

	private void pushStylesheet(Element t)
	{
		stylesheetStack.push(t);
		currentStylesheet = t;
	}

	private void pushDoc(Document document, String name)
	{
		currentDocument = document;
		docStack.push(document);
		if(nameStack.size() > 0)
			name = filename +"-" + name;
		nameStack.push(name + ".xsl");
	}

	private Document popDoc()
	{
		Document popped = docStack.pop();
		String name = nameStack.pop();
		finished.put(name, popped);

		if (docStack.size() > 0)
			currentDocument = docStack.peek();
		else
			currentDocument = null;
		return popped;
	}

	private void pushNode(Node node)
	{
		nodeStack.push(node);
		currentNode = node;
	}

	private Node popNode()
	{
		Node popped = nodeStack.pop();
		if (nodeStack.size() > 0)
			currentNode = nodeStack.peek();
		else
			currentNode = null;
		return popped;
	}

	public Map<String, Document> getDocuments()
	{
		return finished;
	}

	public void setIndent(String indent)
    {
    	this.indent = indent;
    }

	public void setMethod(String method)
    {
    	this.method = method;
    }

	public void setMediaType(String mediaType)
    {
    	this.mediaType = mediaType;
    }

	public void setDexterNamespaces(List<String> dexterNamespaces)
    {
    	this.dexterNamespaces = dexterNamespaces;
    }
	private String translateName(String name)
	{
		String result = name;
		if(name.indexOf(':') != -1)
		{
			String[] b = name.split("[:]");
			if(dexterNamespaces.contains(b[0]))
				throw new DexteritySyntaxException(
						"unrecognized attribute specified in dexter namespace: `" 
						+ name + "'");
		}
		return result;
	}
}
