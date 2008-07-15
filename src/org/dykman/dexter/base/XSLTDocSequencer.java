/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.DexterException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XSLTDocSequencer extends BaseTransformSequencer
{
	private String indent = "no";
	private String method = "html";
	private String mediaType = "text/html";

	private TransformerFactory factory;
	private DocumentBuilder builder;
	{
		factory = TransformerFactory.newInstance();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setExpandEntityReferences(false);
		dbf.setCoalescing(true);
		dbf.setIgnoringComments(false);
		builder = dbf.newDocumentBuilder();

	}

	private Map<String, Document> finished = new HashMap<String, Document>();

	private Map<String, Map<String, DocumentFragment>> valMap = new HashMap<String, Map<String, DocumentFragment>>();
	private Map<String, Map<String, List<Element>>> replacementMap = new HashMap<String, Map<String, List<Element>>>();

	private int pathc = 0;

	// output node stack..  a little hacky, a little arbitrary, but simple.
	private short[] nodeTypes = new short[8092];
	private int nodeLevel = 0;

	private List idNames;

	private Stack<Document> docStack = new Stack<Document>();
	private Stack<String> nameStack = new Stack<String>();
	private Stack<Node> nodeStack = new Stack<Node>();
	private Stack<Element> stylesheetStack = new Stack<Element>();
//	private Element scriptNode = null;

	private Document currentDocument;
	private Node currentNode;
	private Node currentStylesheet;

	private String filename;
	private String encoding;

	public XSLTDocSequencer(Dexter dexter,String name, String encoding) throws Exception
	{
		super(dexter);
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
		StringBuffer sb = new StringBuffer();
		String[] pp = path.split("[|]");
		for(int i = 0; i < pp.length; ++i)
		{
			sb.append(translateXSLPath(pp[i]));
			
			if(i+1 < pp.length) sb.append("|");
		}
		path = sb.toString();
		iteratorStack.push(path);
		Element element = currentDocument.createElement("xsl:for-each");
		element.setAttribute("select", path);
		currentNode.appendChild(element);

		pushNode(element);
	}

	public void endIterator()
	{
		popNode();
		iteratorStack.pop();
		--pathc;
	}

	private String translateName(String name)
	{
		String result = name;
		if(name.indexOf('!') != -1)
		{
			String[] b = name.split("[!]");
			result = b[0] + ':' + b[1];
		}
		return result;
	}
	public void mapAttribute(String name, String[] path, String def)
	{

		Element element = currentDocument.createElement("xsl:attribute");
		name = translateName(name);
		element.setAttribute("name", name);

		if (def != null || path.length > 1)
		{
			StringBuffer attrTest = new StringBuffer();
			boolean first = true;
			if (path.length == 1)
			{
				attrTest.append(translateXSLPath(path[0]));
			}
			else for (int i = 0; i < path.length; ++i)
			{
				if (i % 2 != 0)
				{
					String p = path[i] = translateXSLPath(path[i]);
					if (!first)
					{
						attrTest.append(" and ");
					}
					else
					{
						first = false;
					}
					attrTest.append(p);
				}
			}

			Element choose = currentDocument.createElement("xsl:choose");
			Element when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", attrTest.toString());
			choose.appendChild(when);
			for (int i = 0; i < path.length; ++i)
			{
				if (i % 2 == 0)
				{
					when.appendChild(textContainer(path[i]));
				}
				else
				{
					Element valueOf = currentDocument.createElement("xsl:value-of");
					valueOf.setAttribute("select", path[i]);
					when.appendChild(valueOf);
				}
			}
			Element otherwise = currentDocument.createElement("xsl:otherwise");
			otherwise.appendChild(textContainer(def == null ? "" : def));
			choose.appendChild(otherwise);
			element.appendChild(choose);
		}
		else
		{
			Element valueOf = currentDocument.createElement("xsl:value-of");
			valueOf.setAttribute("select", translateXSLPath(path[0]));
			element.appendChild(valueOf);
		}

		currentNode.appendChild(element);

	}

	public void copyChildren(String path, String def)
	{
		path = translateXSLPath(path);

		Element map;
		Element valueOf = currentDocument.createElement("xsl:copy-of");
		valueOf.setAttribute("select", path + "/*");

		if (def != null)
		{
			Element choose = currentDocument.createElement("xsl:choose");
			Element when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", path);
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

	public void mapNode(String path, String def,boolean disableEscaping)
	{
		path = translateXSLPath(path);

		Element map;
		Element valueOf = currentDocument.createElement("xsl:value-of");
		valueOf.setAttribute("select", path);
		if(disableEscaping)
		{
			valueOf.setAttribute("disable-output-escaping", "yes");
		}

		if (def != null)
		{
			Element choose = currentDocument.createElement("xsl:choose");
			Element when;
			
			when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", path + "/" + "text()");
			Element copyOf = currentDocument.createElement("xsl:copy-of");
			copyOf.setAttribute("select", path);
			when.appendChild(copyOf);
			choose.appendChild(when);

			when = currentDocument.createElement("xsl:when");
			when.setAttribute("test", path);
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

	public void startTest(String tests)
	{
		Element element = currentDocument.createElement("xsl:if");
		element.setAttribute("test", generateXSLTest(tests));
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

	public void startCase(String tests)
	{
		Element element = null;
		if (tests.length() > 0)
		{
			element = currentDocument.createElement("xsl:when");
			element.setAttribute("test", this.generateXSLTest(tests));
		}
		else
		{
			element = currentDocument.createElement("xsl:otherwise");
		}
		currentNode.appendChild(element);
		pushNode(element);
	}

	// public void endCase(String[] tests)

	public void endCase()
	{
		popNode();
	}

	public void startSubdoc(String altDoc, String name, String match,
	      boolean keepSubDoc)
	{
		String fn = altDoc == null ? filename + '-' + name : altDoc;
		match = this.translateXSLPath(match);
		Element element = currentDocument.createElement("xsl:include");
		element.setAttribute("href", fn + ".xsl");

		NodeList ch = currentDocument.getElementsByTagName("xsl:template");
		Node output = ch.item(0);
		currentStylesheet.insertBefore(element, output);

		Element apply = currentDocument.createElement("xsl:apply-templates");
		apply.setAttribute("select", match);
		currentNode.appendChild(apply);

		Document document = createStub(getLastToken(match));
		if (altDoc != null)
		{
			name = name + ".dispose";
		}
		pushDoc(document, name);

		Element template = currentDocument.createElement("xsl:template");
		template.setAttribute("match", "/");
//		tagTemplate(template);

		apply = currentDocument.createElement("xsl:apply-templates");
		apply.setAttribute("select", match);
		template.appendChild(apply);
		ch = currentDocument.getElementsByTagName("xsl:template");
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
	public void startNode(String name, int type)
	{
		nodeTypes[nodeLevel++] = (short) type;
		switch (type)
		{
			case Node.DOCUMENT_NODE:
			{
				Document document = createStub("/");
				pushDoc(document, filename);
//				pushNode(document);
			}
			break;
			case Node.ELEMENT_NODE:
			{
				Element el = currentDocument.createElement("xsl:element");
				el.setAttribute("name", name);
				currentNode.appendChild(el);
				pushNode(el);
			}
			break;
			case Node.DOCUMENT_TYPE_NODE:
System.out.println("DOCUMENT_TYPE_NODE seen");				
//currentDocument.cre				
				// do nothing
			break;
			case Node.TEXT_NODE:
			{
				Text text = currentDocument.createTextNode(name);
				currentNode.appendChild(text);
			}
			break;
			case Node.CDATA_SECTION_NODE:
			{
				CDATASection cd = currentDocument.createCDATASection(name);
				currentNode.appendChild(cd);
			}
			break;
			case Node.ENTITY_NODE:
				// TODO: this is screwed up for this case, I am sure...
			case Node.ENTITY_REFERENCE_NODE:
			{
//				
System.out.println("name = " + name);				
				Node n = translateEntityReference(name);
				EntityReference en = currentDocument.createEntityReference(name);
				currentNode.appendChild(en);
			}
			break;

			case Node.COMMENT_NODE:
			{
				Comment comment = currentDocument.createComment(name);
				currentNode.appendChild(comment);

			}
			break;
			default:
			{
				Dexter.reportInternalError("FATAL: encountered  an unhandler node type: " + type, null);
				throw new DexterException("internal exception - unhandled node type: " + type);
			}
		}
	}
	protected String lookupEnitityReference(String ref)
	{
		String key = "dexter.entities." + ref;
		String val = dexter.getProperty(key);
		if(val == null)
		{
			throw new DexterException("unrecognized entity reference used: " + ref);
		}
		return val.trim();
	}
	protected Node translateEntityReference(String ref)
	{
//		currentDocument.
		Node en = currentDocument.createEntityReference("nbsp");
//		Node en = currentDocument.createEntityReference(lookupEnitityReference(ref));
		Element el = currentDocument.createElement("xsl:text");
		el.setAttribute("disable-output-escaping", "yes");
		el.appendChild(currentDocument.createEntityReference(ref));
//		el.setNodeValue(lookupEnitityReference(ref));
//		el.setTextContent(lookupEnitityReference(ref));
		el.appendChild(en);
		return el;
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
//			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.DOCUMENT_NODE:
				popStylesheet();
				popDoc();
			case Node.ELEMENT_NODE:
				popNode();
			break;
		}
	}

	protected Document createStub(String match)
	{
	
		Document document = builder.newDocument();
//		builder.

		Element style = document.createElement("xsl:stylesheet");
		style.setAttribute("version", "1.0");
		style.setAttribute("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");
		document.appendChild(style);

		pushStylesheet(style);
		pushNode(style);
		// TODO: hacky, hacky
		Element output = document.createElement("xsl:output");
		output.setAttribute("encoding", encoding);
		output.setAttribute("indent", indent);
		output.setAttribute("media-type", mediaType);
		output.setAttribute("method", method);

		style.appendChild(output);
		tagTemplate(output);

		Element template = document.createElement("xsl:template");
		template.setAttribute("match", match);
//		tagTemplate(template);
		style.appendChild(template);

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

	public Node getIdentityValueTemplate(String key, String value)
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

	protected DocumentFragment processIdentityValueTemplate(String key,
	      String value)
	{
		DocumentFragment fragment = createIdentityValueTemplate(currentDocument,
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
		Element element = document.createElement("xsl:text");
		CDATASection cd = document.createCDATASection(content);
		element.appendChild(cd);
		return element;
	}

	protected DocumentFragment createIdentityValueTemplate(Document document,
	      String value)
	{
		DocumentFragment fragment = document.createDocumentFragment();

		Element element = textContainer(value);
		fragment.appendChild(element);

		for (int i = 0; i < iteratorStack.size(); ++i)
		{
			element = textContainer("-");
			fragment.appendChild(element);

			element = currentDocument.createElement("xsl:value-of");
			element.setAttribute("select", iteratorStack.get(i) + "/position()");
			fragment.appendChild(element);
		}
		return fragment;
	}

	public Node getCurrentNode()
	{
		return currentNode;
	}

	public void setIdentityAttribute(String key, String value)
	{
		DocumentFragment fragment = processIdentityValueTemplate(key, value);

		Element element = currentDocument.createElement("xsl:attribute");
		element.setAttribute("name", key);
		element.appendChild(fragment);
		currentNode.appendChild(element);
	}

	private void popStylesheet()
	{
		stylesheetStack.pop();
		if (stylesheetStack.size() > 0)
		{
			currentStylesheet = stylesheetStack.peek();
		}
		else
		{
			currentStylesheet = null;
		}
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
		{
			name = filename +"-" + name;
		}
		nameStack.push(name + ".xsl");
	}


	private Document popDoc()
	{
		Document popped = docStack.pop();
		String name = nameStack.pop();
		finished.put(name, popped);

		if (docStack.size() > 0)
		{
			currentDocument = docStack.peek();
		}
		else
		{
			currentDocument = null;
		}

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
		{
			currentNode = nodeStack.peek();
		}
		else
		{
			currentNode = null;
		}
		return popped;
	}

	protected void write(Document document, Writer writer, String encoding)
	{
		try
		{

			document.normalizeDocument();

			Transformer tranformer = factory.newTransformer();
			tranformer.setOutputProperty("indent", indent);
			tranformer.setOutputProperty("method", method);
			tranformer.setOutputProperty("media-type", mediaType);
			tranformer.setOutputProperty("encoding", encoding);

			Result result = new javax.xml.transform.stream.StreamResult(writer);

			Source source = new javax.xml.transform.dom.DOMSource(document);
			tranformer.transform(source, result);
		}
		catch (Exception e)
		{
			throw new DexterException("An error has occured while trying to render the document",e);
		}

	}

	public Map<String, Document> getDocuments()
	{
		return finished;
	}
}
