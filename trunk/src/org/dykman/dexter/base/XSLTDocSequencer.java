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
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.DexterException;
import org.dykman.dexter.DexterHaltException;
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
	
	public static final String XSLOUTPUT = "xsl:output";
	public static final String XSLINCLUDE = "xsl:include";
	public static final String XSLIMPORT = "xsl:import";
	public static final String XSLVARIABLE = "xsl:variable";

	public static final String XSLTEMPLATE = "xsl:template";
	public static final String XSLPARAM = "xsl:param";
	public static final String XSLCALLTEMPLATE = "xsl:call-template";
	public static final String XSLWITHPARAM = "xsl:with-param";
	public static final String XSLAPPLYTEMPLATES = "xsl:apply-templates";
	
	public static final String XSLTEXT = "xsl:text";
//public static final String XSLFOREACH = "xsl:for-each";
	public static final String XSLELEMENT = "xsl:element";
	public static final String XSLCOPYOF = "xsl:copy-of";
	public static final String XSLVALUEOF = "xsl:value-of";

	public static final String XSLATTRIBUTE = "xsl:attribute";
	
	public static final String XSLIF = "xsl:if";
	public static final String XSLCHOOSE = "xsl:choose";
	public static final String XSLWHEN = "xsl:when";
	public static final String XSLOTHERWISE = "xsl:otherwise";

	private DocumentBuilder builder;

	
	static {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		
		dbf.setExpandEntityReferences(false);
		dbf.setCoalescing(true);
		dbf.setIgnoringComments(false);
	}

	private Map<String, Document> finished = new HashMap<String, Document>();

	private Map<String, Map<String, DocumentFragment>> valMap = new HashMap<String, Map<String, DocumentFragment>>();
	private Map<String, Map<String, List<Element>>> replacementMap = new HashMap<String, Map<String, List<Element>>>();

	private short[] nodeTypes = new short[8092];
	private int nodeLevel = 0;

	private List<String> idNames;

	private Stack<Document> docStack = new Stack<Document>();
	private Stack<String> nameStack = new Stack<String>();
	private Stack<Node> nodeStack = new Stack<Node>();
	private Stack<Element> stylesheetStack = new Stack<Element>();

	private Document currentDocument;
	private Node currentNode;
	private Element currentStylesheet;

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
	

	public Element createTemplate(String name, String match, String mode) {
		Element template = currentDocument.createElement(XSLTEMPLATE);
		if(name != null) template.setAttribute("name", name);
		if(match != null) template.setAttribute("match", match);
		if(mode != null) template.setAttribute("mode", mode);
		return template;
	}

	public void setIdNames(List ids)
	{
		idNames = ids;
	}

	Stack<String> iteratorStack = new Stack<String>();

	public void startIterator(String path)
	{
		iteratorStack.push(path);
		startSelect(null, path, randMode());
		
//		Element var = currentDocument.createElement(XSLVARIABLE);
//		var.setAttribute("name", levelCounter + iteratorStack.size());
//		Element vo = currentDocument.createElement(XSLVALUEOF);
//		vo.setAttribute("select", "position()");
//		var.appendChild(vo);
//		currentNode.appendChild(var);
	}

	public void endIterator() {
		endSelect();
		iteratorStack.pop();
	}

	public void mapNode(
		String[] path, 
		String def,
		boolean force,
		boolean disable_escape) {
		
		Element v = valueTemplate(path, def,XSLVALUEOF,force,disable_escape);
		if(v != null) currentNode.appendChild(v);
	}

	public void copyNodes(String path, String def, boolean children)
	{
		String av = path;
		if(children) av = path + "/*";
		Element valueOf = callTemplateEvaluator(av, XSLCOPYOF,false);
		if(valueOf != null) {
			Element map;
			if (def != null)
			{
				Element choose = currentDocument.createElement(XSLCHOOSE);
				Element when = currentDocument.createElement(XSLWHEN);
				when.setAttribute("test", path);
				when.appendChild(valueOf);
				choose.appendChild(when);
				map = choose;
			}
			else {
				map = valueOf;
			}
	
			if (def != null)
			{
				Element otherwise = currentDocument.createElement(XSLOTHERWISE);
				otherwise.appendChild(currentDocument.createTextNode(def));
				map.appendChild(otherwise);
			}
			currentNode.appendChild(map);
		}
	}
	
	public void mapAttribute(
			String name, String[] path, String def, boolean force, boolean disable_escape)
	{

		Element element = currentDocument.createElement(XSLATTRIBUTE);
		name = translateName(name);
		element.setAttribute("name", name);

		Element v = valueTemplate(path, def,XSLVALUEOF,force,disable_escape);
		if(v != null) element.appendChild(v);

		currentNode.appendChild(element);
	}

	Pattern functiondesc = Pattern.compile("^([a-zA-Z][a-zA-Z0-9._-]+[(])(.*)");

	protected String getInnerExpresion(String path) {
		Matcher matcher = functiondesc.matcher(path);
		if(matcher.matches()) { 
	 		String m = matcher.group(1).substring(0, matcher.group(1).length() - 1);
			if(dexter.loadTemplate(currentStylesheet, m)) {
				return getInnerExpresion(path.substring(m.length()));
			}
		}
		return path;
		
	}
	
	protected Element callTemplateEvaluator(
			String path, 
			String evalTag, boolean disableEscaping) {
		Matcher matcher = functiondesc.matcher(path);
		if(matcher.matches()) {
			String s = matcher.group(1);
			String nn = s.substring(0,s.length() - 1);

			if(dexter.loadTemplate(currentStylesheet,nn)) {
				Element caller = currentDocument.createElement(XSLCALLTEMPLATE);
				caller.setAttribute("name",nn);
				
				Element p1 = currentDocument.createElement(XSLWITHPARAM);
				p1.setAttribute("name","param1");
				path = matcher.group(2);
				int n = path.indexOf(')');
				if(n != -1) {
					path = path.substring(0,n);
				}
				
				p1.setAttribute("select", path);
				caller.appendChild(p1);
				
				return caller;
			} 
		}
		Element valueOf = null;
		if(path.length() > 0) {
			valueOf = currentDocument.createElement(evalTag);
			valueOf.setAttribute("select", path);
			if(disableEscaping) valueOf.setAttribute("disable-output-escaping", "yes");;
		}
		return valueOf;
	}

	protected Element valueTemplate(
		String[] path, String def,
		String evalTag, boolean force, boolean disable_escape)
	{
		if (def != null || path.length > 1) {
			StringBuilder attrTest = new StringBuilder();
			boolean first = true;
			if (path.length == 1) {
				String p = getInnerExpresion(path[0]);
				if(force) p = "(" + "string-length(" + p + ") > 0)";
				attrTest.append(p);
			} else for (int i = 0; i < path.length; ++i) {
				if (i % 2 != 0) {
					String p = getInnerExpresion(path[i]);
					if (!first) attrTest.append(" and ");
					else first = false;
					
					if(force) p = "(" + "string-length(string(" + p + ")) > 0)";
					attrTest.append(p);
				}
			}

			Element choose = currentDocument.createElement(XSLCHOOSE);
			Element when = currentDocument.createElement(XSLWHEN);
			when.setAttribute("test", attrTest.toString());
			choose.appendChild(when);
			// if path.length > 1, then we alternating literals and paths
			if(path.length == 1) {
				Element valueOf = callTemplateEvaluator(
						path[0],evalTag,disable_escape);
				if(valueOf != null) {
					when.appendChild(valueOf);
				}
			} 
			else for (int i = 0; i < path.length; ++i) {
				if (i % 2 == 0)  {
					if(path[i].length() > 0) {
						when.appendChild(textContainer(path[i]));
					}
				} else {
					Element valueOf = callTemplateEvaluator(
						path[i],evalTag,disable_escape);
					if(valueOf != null) when.appendChild(valueOf);
				}
			}

			Element otherwise = currentDocument.createElement(XSLOTHERWISE);
			otherwise.appendChild(textContainer(def == null ? "" : def));
			choose.appendChild(otherwise);
			return choose;
		} else {
			return callTemplateEvaluator(
					path[0],evalTag,disable_escape);
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
			Element element = currentDocument.createElement(XSLATTRIBUTE);
			element.setAttribute("name", key);
			Text text = currentDocument.createTextNode(value);
			element.appendChild(text);
			currentNode.appendChild(element);
		}
	}
	
	public void setIdentityAttribute(String key, String value)
	{
		DocumentFragment fragment = processIdentityValueTemplate(key, value);
		Element element = currentDocument.createElement(XSLATTRIBUTE);
		element.setAttribute("name", key);
		element.appendChild(fragment);
		currentNode.appendChild(element);
	}

	public void startTest(String tests)
	{
		Element element = currentDocument.createElement(XSLIF);
		element.setAttribute("test", tests);
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endTest()
	{
		popNode();
	}

	public void startCaseBlock()
	{
		Element element = currentDocument.createElement(XSLCHOOSE);
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endCaseBlock() {
		popNode();
	}

	public void startSelect(String name, String match, String mode) {
		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("match", match);
		template.setAttribute("mode", mode);
		currentStylesheet.appendChild(template);
		currentStylesheet.appendChild(currentDocument.createTextNode("\n"));

		Element matcher = currentDocument.createElement(XSLAPPLYTEMPLATES);
		matcher.setAttribute("select", match);
		matcher.setAttribute("mode", mode);
		
		currentNode.appendChild(matcher);
		currentNode.appendChild(currentDocument.createTextNode("\n"));

		pushNode(template);
		
		
	}
	public void endSelect() {
		popNode();
	}
	public void startCase(String tests)
	{
		Element element = null;
		if (tests != null)
		{
			element = currentDocument.createElement("xsl:when");
			element.setAttribute("test", tests);
		}
		else
		{
			element = currentDocument.createElement(XSLOTHERWISE);
		}
		currentNode.appendChild(element);
		pushNode(element);
	}

	public void endCase()
	{
		popNode();
	}

	public void startSubdoc(String altDoc, String name, String match,
	      boolean keepSubDoc)
	{
		String fn = altDoc == null ? filename + '-' + name : altDoc;
		String tn = fn.replaceAll("[^a-zA-Z0-9]","-");

		Element element = currentDocument.createElement(XSLIMPORT);
		element.setAttribute("href", fn + ".xsl");

		NodeList ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
		Node output = ch.item(0);
		currentStylesheet.insertBefore(element, output);
		currentStylesheet.insertBefore(
				currentDocument.createTextNode("\n"), output);
		currentNode.appendChild(createExternalTemplateCall(match,tn));

		Document document = createStub(match,tn,tn);
		if (altDoc != null)	name = name + ".dispose";
		pushDoc(document, name);
		
// create the main entry point template to handle cases where it is invoked independently
		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("match", "/");
		template.appendChild(createExternalTemplateCall(match,tn));
		ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
		output = ch.item(0);
		currentStylesheet.insertBefore(template, output);
		currentStylesheet.insertBefore(currentDocument.createTextNode("\n"), output);
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
		indentWithWhitespace(0);
	}
		protected void indentWithWhitespace(int m) {
		if(! lastWasEntity) {
			int n = nodeStack.size() + m;
			StringBuilder sb = new StringBuilder();
//			sb.append("\n");
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
				Document document = createStub("/",null,null);
				pushDoc(document, filename);
				lastWasEntity = false;
			}
			break;
			case Node.ELEMENT_NODE:
			{
				indentWithWhitespace();
				Element el = currentDocument.createElement(XSLELEMENT);
				el.setAttribute("name", name);
				currentNode.appendChild(el);
				currentNode.appendChild(currentDocument.createTextNode("\n"));
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
				currentNode.appendChild(currentDocument.createTextNode("\n"));
				lastWasEntity = false;
			}
			break;
			case Node.CDATA_SECTION_NODE:
			{
				indentWithWhitespace();
				CDATASection cd = currentDocument.createCDATASection(name);
				currentNode.appendChild(cd);
				currentNode.appendChild(currentDocument.createTextNode("\n"));
				lastWasEntity = false;
			}
			break;
			case Node.ENTITY_NODE:
				// TODO: this is screwed up for this case, I am sure...
			case Node.ENTITY_REFERENCE_NODE:
			{

				indentWithWhitespace();
				Node n = translateEntityReference(name);
				Element el = currentDocument.createElement(XSLTEXT);
				el.appendChild(n);
				currentNode.appendChild(el);
				currentNode.appendChild(currentDocument.createTextNode("\n"));
				lastWasEntity = true;
			}
			break;

			case Node.COMMENT_NODE:
			{
				if(dexter.isPropigateComments())
				{
					indentWithWhitespace();
					Comment comment = currentDocument.createComment(name);
					currentNode.appendChild(comment);
					currentNode.appendChild(currentDocument.createTextNode("\n"));
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
				if(type!= Node.DOCUMENT_NODE) { 
					currentNode.appendChild(currentDocument.createTextNode("\n"));
				}
			default : 
				if(type!= Node.DOCUMENT_NODE) { 
					indentWithWhitespace(-1);
				}
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


	protected Element createExternalTemplateCall(String match,String name) {
		
//		String trmatch = translateXSLPath(match);
		Element choose = currentDocument.createElement(XSLCHOOSE);
		Element when = currentDocument.createElement(XSLWHEN);
		when.setAttribute("test", match);
		Element apply = currentDocument.createElement(XSLAPPLYTEMPLATES);
		apply.setAttribute("select", match);
		apply.setAttribute("mode", name);
		when.appendChild(apply);
		choose.appendChild(when);

		Element otherwise = currentDocument.createElement(XSLOTHERWISE);
		Element call = currentDocument.createElement(XSLCALLTEMPLATE);
		call.setAttribute("name", name);
		otherwise.appendChild(call);
		choose.appendChild(otherwise);
		
		return choose;
	}
	
	protected Document createStub(String match,String name, String mode)
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

		Element output = document.createElement(XSLOUTPUT);
		output.setAttribute("encoding", encoding);
		output.setAttribute("indent", indent);
		if(mediaType != null) output.setAttribute("media-type", mediaType);
		output.setAttribute("method", method);

		style.appendChild(output);
		style.appendChild(document.createTextNode("\n"));
		
		tagTemplate(output);

		Element template = document.createElement(XSLTEMPLATE);
		template.appendChild(document.createTextNode("\n"));

		if(match != null) template.setAttribute("match", match);
		if
		(name != null) template.setAttribute("name", name);
		if(mode != null) template.setAttribute("mode", mode);

		style.appendChild(template);
		style.appendChild(document.createTextNode("\n"));

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
				" generated by "  +  Dexter.DEXTER_VERSION 
					+ " (" + Dexter.DEXTER_COPYRIGHT+ ") from `" + filename + "'  ",
		 };
		blockComment(element, TAG);
	}

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
		element = currentDocument.createElement(XSLIF);
		element.setAttribute("test", "last() > 1");
		fragment.appendChild(element);
		
		element.appendChild(currentDocument.createTextNode("-"));
		
		Element eval  = currentDocument.createElement(XSLVALUEOF);
		eval.setAttribute("select", "generate-id()");
		element.appendChild(eval);
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
	public void appendText(String s) {
		appendText(s, false);
	}
	public void appendText(String s,boolean escape) {
		Element el = currentDocument.createElement(XSLTEXT);
		if(escape) el.setAttribute("disable-output-escaping", "yes");
		currentNode.appendChild(currentDocument.createTextNode(s));
	}


	Random rand = new Random();
	public String randMode()
    {
		return "a" + Long.toHexString(rand.nextLong());
    }

	



}
