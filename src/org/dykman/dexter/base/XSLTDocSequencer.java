/**
 * dexter (c) 2007,-2009 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

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
	

	public void setIdNames(List ids)
	{
		idNames = ids;
	}

	Stack<String> iteratorStack = new Stack<String>();

	public void startIterator(String path)
	{
		iteratorStack.push(path);
		startSelect(null, path);
//		startSelect(null, path, randMode());
	}

	public void endIterator() {
		endSelect();
		iteratorStack.pop();
	}

	public void copyNodes(PathEval pe, String def, boolean children)
	{
		String av = pe.path;
		if(children) av = pe.path + "/*";
		PathEval ev = new PathEval(pe,av);
		Element valueOf = callTemplateEvaluator(ev, XSLCOPYOF);

		Element map;
		if (def != null) {
			Element choose = currentDocument.createElement(XSLCHOOSE);
			Element when = currentDocument.createElement(XSLWHEN);
			when.setAttribute("test", ev.path);
			when.appendChild(valueOf);
			choose.appendChild(when);
			map = choose;
		}
		else {
			map = valueOf;
		}

		if (def != null) {
			Element otherwise = currentDocument.createElement(XSLOTHERWISE);
			otherwise.appendChild(currentDocument.createTextNode(def));
			map.appendChild(otherwise);
		}
		currentNode.appendChild(map);
	}
	
	public void mapNode(
			List<PathEval> path, 
			String def) {
			
			Element v = valueTemplate(path, def,XSLVALUEOF);
			if(v != null) currentNode.appendChild(v);
		}

	public void mapAttribute(
			String name, List<PathEval> path, String def)
	{

		Element element = currentDocument.createElement(XSLATTRIBUTE);
		name = translateName(name);
		element.setAttribute("name", name);

		Element v = valueTemplate(path, def,XSLVALUEOF);
		if(v != null) element.appendChild(v);

		currentNode.appendChild(element);
	}

//	Pattern functiondesc = Pattern.compile("^([a-zA-Z][a-zA-Z0-9._-]+[(].*)");
//	Pattern functiondesc = Pattern.compile("^([a-zA-Z][a-zA-Z0-9._-]+)[(](.*)[)]$");

	protected String getInnerExpresion(String path) {
		if(Dexter.isTemplateCall(path)) {
			String[] parts = Dexter.parseTemplateCall(path);
			String label = parts[0];
				if(Dexter.isXpathFunction(label)) {
					return path;
				} else if(dexter.loadTemplate(currentStylesheet, label)) {
					String val = parts[1];
					if(val.length() == 0) {
						val = ".";
					}
					return val;
				}
			} 
		return path;
		}
		

	Set<String> lookupTable = new HashSet<String>();
	
	protected Element callTemplateEvaluator(
			PathEval pp, 
			String evalTag) {
		Element cc= scallTemplateEvaluator(pp, evalTag);
		if(pp.getType() == PathEval.LOOKUP) {
			// ensure lookup is loaded
			dexter.loadTemplate(currentStylesheet,"lookup");
			// TODO: ensure lookup template in inserted
			// probably need to work harder to allow any legal XPATH
			String file = "dexter-lookup";
			String p = pp.path;
			int n;
			if((n = p.indexOf(":")) != -1) {
				if(n != p.indexOf("::")) {
					String ss[] = p.split("[:]");
					file = ss[0] ;
					p = ss[1];
				}
			}
			if(! lookupTable.contains(file)) {
				lookupTable.add(file);
				// now load a document as a variable immediately after the output
				Element output = getFirstChildElement(currentStylesheet);
				Element var = currentDocument.createElement(XSLVARIABLE);
				var.setAttribute("name",file + "data");
				var.setAttribute("select", "document('" + file + ".xml')");
				NodeList ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
				Node firstTemplate = ch.item(0);
				currentStylesheet.insertBefore(var, firstTemplate);
//				currentStylesheet.insertBefore(firstTemplate, currentDocument.createTextNode("\n\n"));
			}
			n = file.indexOf(".");
			Element lookup = currentDocument.createElement(XSLCALLTEMPLATE);
			lookup.setAttribute("name", "lookup");
			Element param = currentDocument.createElement(XSLWITHPARAM);
			param.setAttribute("name", "key");
			param.appendChild(cc);
			lookup.appendChild(param);

			
			// TODO ensure filedata variable is established
			param = currentDocument.createElement(XSLWITHPARAM);
			param.setAttribute("name", "data");
			param.setAttribute("select", "$" + file + "data");
			lookup.appendChild(param);
			
			return lookup;
		}
		return cc;
	}
	
	protected Element scallTemplateEvaluator(
			PathEval pp, 
			String evalTag) {
		String path = pp.path;
		if(Dexter.isTemplateCall(path)) {
			String[] parts = Dexter.parseTemplateCall(path);
			String label = parts[0];
			if(dexter.loadTemplate(currentStylesheet,label)) {
				Element caller = currentDocument.createElement(XSLCALLTEMPLATE);
				caller.setAttribute("name",label);
				
				Element p1 = currentDocument.createElement(XSLWITHPARAM);
				p1.setAttribute("name","param1");
				path = parts[1];
				
				p1.setAttribute("select", path);
				caller.appendChild(p1);
				
				return caller;
			} 
		}
		Element valueOf = null;
		if(path.length() > 0) {
			valueOf = currentDocument.createElement(evalTag);
			valueOf.setAttribute("select", path);
//			if(pp.disableEscape) valueOf.setAttribute("disable-output-escaping", "yes");;
		}
		return valueOf;
	}

	protected String enquote(String s) {
		StringBuilder sb = new StringBuilder();
		return sb.append('"').append(s).append('"').toString();
	}
	
	protected String lengthTest(Object s) {
		StringBuilder sb = new StringBuilder("string-length(");
		if(s instanceof PathEval) {
			sb.append(((PathEval) s).path);
		} else {
			sb.append(s.toString());
		}
		return sb.append(")").toString();
		
	}

	protected Element valueTemplateSingle(
			PathEval path, String def,
			String evalTag)
	{

		Element result = null;;
		if(def != null && def.length() > 0) {
			result = currentDocument.createElement(XSLIF);
//			StringBuilder attrTest = new StringBuilder();
			String p = getInnerExpresion(path.path);
//			if(path.path.startsWith("{{")) path.lookup = true;
			result.setAttribute("test" , "string-length(" + p + ")");
		}  else {
//System.out.println("I have NO default");
		}
		Element content = currentDocument.createElement(XSLVALUEOF);
		content.setAttribute("select", path.path);
		if(result == null) result = content;
		else result.appendChild(content);
		return result;
	}

	protected Element valueTemplate(
			List<PathEval> path, 
			String def,
		String evalTag)
	{
		StringBuilder attrTest = new StringBuilder();
		int n = path.size();
		boolean app = false;
		if(! pureLiteral(path)) for(PathEval pp : path) {
			if(pp.type != PathEval.LITERAL) {
				if(app) attrTest.append(" and ");
				String p = getInnerExpresion(pp.path);
				attrTest.append("string-length(" + p + ")");
				app = true;
			}  /*else {
				System.out.println("STRING: " + pp.toString());
			} */
		} else  {
			valueTemplateSingle(path.get(0), def, evalTag);
		}

		Element choose;
		if(!pureLiteral(path)) {
			Element when;
			if(def != null && def.length() > 0) {
				choose = currentDocument.createElement(XSLCHOOSE);
				when = currentDocument.createElement(XSLWHEN);
				choose.appendChild(when);
			} else {
				when = choose =  currentDocument.createElement(XSLIF);
			}
			
			
			when.setAttribute("test", attrTest.toString());
			for(PathEval pe : path) {
				if(pe.type != PathEval.LITERAL) {
						Element valueOf = callTemplateEvaluator(
								pe,evalTag);
						when.appendChild(valueOf);
	//				}
				} else {
					when.appendChild(textContainer(pe.path));
				}
			}
	
			if(def != null && def.length() > 0) {
				Element otherwise = currentDocument.createElement(XSLOTHERWISE);
				otherwise.appendChild(textContainer(def));
				choose.appendChild(otherwise);
			}
			
		} else {
			return callTemplateEvaluator(path.get(0),evalTag);
		}
		return choose;
		
	}
	
	private boolean pureLiteral(List<PathEval> list) {
		for(PathEval p : list) {
			if(p.type != PathEval.LITERAL) {
				return false;
			}
		}
		return true;
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

	public void callNamedTemplate(String name) {
		Element caller = currentDocument.createElement(XSLCALLTEMPLATE);
		caller.setAttribute("name", name);
		currentNode.appendChild(caller);

	}
	public void startNamedTemplate(String name) {
//		callNamedTemplate(name);

		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("name", name);
		currentStylesheet.appendChild(currentDocument.createTextNode("\n"));
		currentStylesheet.appendChild(template);
		currentStylesheet.appendChild(currentDocument.createTextNode("\n"));
		pushNode(template);
	}

	public void endNamedTemplate() {
		popNode();
	}
//	public void startSelect(String name, String match, String mode) {
	public void startSelect(String name, String match) {
			
		int n = match.lastIndexOf(".//");
		String select = match;
		match = makeMatch(select);
		
		String mode = this.randMode();

		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("match", match);
		template.setAttribute("mode", mode);
		currentStylesheet.appendChild(currentDocument.createTextNode("\n"));
		currentStylesheet.appendChild(template);
		currentStylesheet.appendChild(currentDocument.createTextNode("\n"));

		Element matcher = currentDocument.createElement(XSLAPPLYTEMPLATES);
		matcher.setAttribute("select", select);
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
			element = currentDocument.createElement(XSLWHEN);
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

//		NodeList ch = currentDocument.getElementsByTagName(XSLTEMPLATE);
		
		Element output = childElement(currentStylesheet, XSLOUTPUT);
		 
		currentStylesheet.insertBefore(element, output);
		currentStylesheet.insertBefore(
				currentDocument.createTextNode("\n"), output);
		
		String select = match;
		match = makeMatch(select);

		currentNode.appendChild(createExternalTemplateCall(select,tn));

		// create secondary document, set  the stack
		Document document = createStub(match,tn,tn);
		if (altDoc != null)	name = name + ".dispose";
		pushDoc(document, name);
		
// create the main entry point template to handle cases where it is invoked independently
		Element template = currentDocument.createElement(XSLTEMPLATE);
		template.setAttribute("match", "/");
		template.appendChild(createExternalTemplateCall(select,tn));
		output = childElement(currentStylesheet,XSLTEMPLATE);;
		currentStylesheet.insertBefore(template, output);
		currentStylesheet.insertBefore(currentDocument.createTextNode("\n"), output);
		currentStylesheet.insertBefore(currentDocument.createTextNode("\n"), template);
	}

	public void endSubdoc()
	{
		// createStub, called by startSubDoc does 2 pushes, so we do 2 pops
		popNode();
		popNode();
		popStylesheet();
		popDoc();
	}

	protected Element getFirstChildElement(Element parent) {
		NodeList nl = parent.getChildNodes();
		for(int i = 0; i < nl.getLength(); ++i) {
			if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				return (Element) nl.item(i);
			}
		}
		
		return null;
	}
	public void setDocType(DocumentType dt)
	{
		// this should always return the current output element
		Element element = getFirstChildElement(currentStylesheet);
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

	public Element childElement(Node parent,String name) {
		NodeList nl = parent.getChildNodes();
		parent.getChildNodes();
		for(int i = 0; i < nl.getLength(); ++i) {
			Node nn = nl.item(i);
			if(nn.getNodeType() == Node.ELEMENT_NODE && name.equalsIgnoreCase(
				nn.getNodeName())) {
				return (Element) nn;
			}
		}
		return null;
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
				NodeList nl = currentStylesheet.getElementsByTagName("xsl:template");
				for(int i = 0; i < nl.getLength(); ++i) {
					Element nn = (Element) nl.item(i);
					String match = nn.getAttribute("match");
					if("/".equals(match)) {
						Element tn = currentDocument.createElement(XSLTEXT);
						tn.appendChild(currentDocument.createTextNode("\n"));
						nn.appendChild(tn);
						break;
					}
				}
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


	protected String makeMatch(String select) {
		String match = select;
		int n = select.lastIndexOf('/');
		if(n > -1) match = select.substring(n+1);
			
		return match;
	}
	
	protected Element createExternalTemplateCall(String select,String name) {
		Element choose = currentDocument.createElement(XSLCHOOSE);
		Element when = currentDocument.createElement(XSLWHEN);
		when.setAttribute("test", select);
		Element apply = currentDocument.createElement(XSLAPPLYTEMPLATES);
		apply.setAttribute("select", select);
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

		style.appendChild(document.createTextNode("\n"));
		style.appendChild(output);
		style.appendChild(document.createTextNode("\n"));
		
		tagTemplate(output);

		Element template = document.createElement(XSLTEMPLATE);
		template.appendChild(document.createTextNode("\n"));

		if(match != null) template.setAttribute("match", match);
		if(name != null) template.setAttribute("name", name);
		if(mode != null) template.setAttribute("mode", mode);

		style.appendChild(document.createTextNode("\n"));
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
		
		popped.getDocumentElement().appendChild(popped.createTextNode("\n"));
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
