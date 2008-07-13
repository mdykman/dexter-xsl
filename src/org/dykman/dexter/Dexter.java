/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.dykman.dexter.base.DexterEntityResolver;
import org.dykman.dexter.base.DocumentEditor;
import org.dykman.dexter.base.XSLTDocSequencer;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeDescriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Dexter
{
	static Set<File> outputFile = new HashSet<File>();

	boolean useScriptContext = true;

	protected Document inputDocument;

	protected Properties properties;

	String filename;

	String encoding;

	String indent = "no";

	String method = "html";

	String mediaType = "text/html";

	protected Map<Object, Object> userData = new HashMap<Object, Object>();

	public void setIndent(boolean b)
	{
		if (b)
		{
			indent = "yes";
		}
		else
		{
			indent = "no";
		}
	}

	public void setMethod(String method)
	{
		this.method = method;
	}
	public void setMediaType(String type)
	{
		mediaType = type;
	}

	// TODO..  thses methods are useless, aren't they?
	public void setUserData(Object key, Object value)
	{
		userData.put(key, value);
	}

	public Object getUserData(Object key)
	{
		return userData.get(key);
	}
	Map<String, Document> allDocs = new HashMap<String, Document>();
	
	public Map<String, Document> generateXSLT(String filename,Document document) throws Exception
	{
		this.filename = filename;
		Document clone = (Document)document.cloneNode(true);
		document = clone;
		document.normalize();

		scanDocument(document);
		Descriptor descriptor = Dexter.marshall(document, this);

		XSLTDocSequencer sequencer = new XSLTDocSequencer(filename, encoding);
		sequencer.setProperties(properties);
		sequencer.setIdNames(idNames);
		sequencer.runDescriptor(descriptor);
		return sequencer.getDocuments();
	}

	public Map<String, Document> getDocuments()
	{
		return allDocs;
	}
	protected List<String> idNames = new ArrayList<String>();

	private Map<String, String> descriptors = new HashMap<String, String>();

	private Map<String, String> editors = new HashMap<String, String>();

	private Map<String, String> blocks = new HashMap<String, String>();

	public Dexter(String encoding)
	{
		this(encoding, loadBuiltInProperties(Dexter.class));
	}

	public Dexter(String encoding, Properties properties)
	{
		this.properties = properties;
		this.encoding = encoding;
		init(properties);

	}

	private static Properties loadBuiltInProperties(Class k)
	{
		Properties p = new Properties();
		try
		{
			p.load(k.getResourceAsStream(DexterityConstants.SCAN_CFG));
		}
		catch (IOException e)
		{
			throw new DexterException("unable to load "
			      + DexterityConstants.SCAN_CFG);
		}

		return p;
	}

	public void init()
	{
		Properties p = new Properties();
		try
		{
			p.load(getClass().getResourceAsStream(DexterityConstants.SCAN_CFG));
		}
		catch (IOException e)
		{
			throw new DexterException("unable to load "
			      + DexterityConstants.SCAN_CFG);
		}
		init(p);
	}

	public void init(Properties p) // throws Exception
	{
		properties.putAll(p);

		String v = p.getProperty("dexter.node.id");
		String[] b = v.split(",");
		for (int i = 0; i < b.length; ++i)
		{
//			System.out.println("adding id attribute " + b[i]);
			idNames.add(b[i]);
		}

		String prefix = p.getProperty(DexterityConstants.PREFIX);
		v = p.getProperty("dexter.block");
		if (v != null)
		{
			String[] blks = v.split(",");
			for (int i = 0; i < blks.length; ++i)
			{
				String t = "dexter.block" + "." + blks[i];
				v = p.getProperty(t);
				if (v != null)
				{
					v = prefix + v;
				}
				blocks.put(prefix + blks[i], v);
			}
		}


		String seq = p.getProperty("dexter.descriptors");
		String[] tk = seq.split(",");
		for (int i = 0; i < tk.length; ++i)
		{
			String key = "dexter.a." + tk[i];
			String klassName = p.getProperty(key);
			String tag = prefix + tk[i];
			// System.out.println("loading attr " + tag);
			this.descriptors.put(tag, klassName);
		}
		v = p.getProperty("dexter.editors");
		tk = v.split(",");
		for (int i = 0; i < tk.length; ++i)
		{
			String key = "dexter.a." + tk[i];
			String klassName = p.getProperty(key);
			String tag = prefix + tk[i];
			this.editors.put(tag, klassName);
		}
	}

	public boolean isIdName(String name)
	{
		return idNames.contains(name);
	}

	public void blessTree(Node node)
	{
		bless(node);
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); ++i)
		{
			blessTree(list.item(i));
		}
	}

	public void bless(Node node)
	{
		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element) node;
			Iterator<String> it = idNames.iterator();
			while (it.hasNext())
			{
				String s = it.next();
				if (element.hasAttribute(s))
				{
					element.setIdAttribute(s, true);
				}
			}
		}
	}


	/**
	 * @throws Exception
	 */
	public void scanDocument() throws Exception
	{
		scanDocument(inputDocument);
	}

	public void scanDocument(Document document) throws Exception
	{
		Document oldDocument = inputDocument;
		this.inputDocument = document;
		Element root = inputDocument.getDocumentElement();
		blessTree(root); // make ids into 'id's
		
		// find editor attributes, remove them, and
		// run the associated objects on the document
		scanElementForEditors(root); // run the editors
		// find descriptor attributes, remove them and 
		// attach the associated objects 
		scan(document); // find descriptor attributes and associate
		inputDocument = oldDocument;
	}

	public void scan(Node node) throws Exception
	{
		scanNode(node);
		NodeList children = node.getChildNodes();
		if (children != null)
		{
			for (int i = 0; i < children.getLength(); ++i)
			{
				Node child = children.item(i);
				if (child == null)
				{
					System.out.println("INFO: child removed by earier process");
				}
				else if (child.getParentNode() == null)
				{
					System.out.println("INFO: child has no parent removed by earier process");
				}
				else
				{
					scan(child);
				}
			}
		}
	}

	protected void scanElementForEditors(Element element) throws Exception
	{
		// System.out.println("scanElementForEditors");
		scanForEditors(element);
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i)
		{
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				scanElementForEditors((Element) n);
			}
		}
		// Element top = inputDocument.getDocumentElement();
	}

	protected void scanForEditors(Element el) throws Exception
	{
		Iterator<String> it = editors.keySet().iterator();
		while (it.hasNext()) // for each editor in the list
		{
			String alabel = it.next();
			if (el.hasAttribute(alabel))
			{
				Class klass = Class.forName(editors.get(alabel));
				DocumentEditor editor = (DocumentEditor) klass.newInstance();
				editor.setProperties(properties);
				editor.setDexter(this);
				editor.setReference(el.getOwnerDocument(), el);
				editor.edit(alabel, el.getAttribute(alabel));
				el.removeAttribute(alabel);
			}
		}
	}

	public TransformSpecifier createSpecifier(Element element, String label,
	      Properties properties) throws Exception
	{
		TransformSpecifier td = null;
		String k = descriptors.get(label);
		Class cl = Class.forName(k);
		if (blocks.containsKey(label))
		{
			// System.out.println("processing block ");
			Node parent = element.getParentNode();

			String end = blocks.get(label);
			Element[] siblings = findContiguousSiblings(element, label, end, false);

			Element blockNode = inputDocument
			      .createElement(DexterityConstants.BLOCK);
			parent.replaceChild(blockNode, element);
			element = blockNode;
			// prepare the args
			String[] names = new String[siblings.length];
			String[] values = new String[siblings.length];

			for (int i = 0; i < siblings.length; ++i)
			{
				Element be = siblings[i];
				if (be.hasAttribute(label))
				{
					names[i] = label;
					values[i] = be.getAttribute(label);
					be.removeAttribute(label);
				}
				else if (end != null && be.hasAttribute(end))
				{
					names[i] = end;
					values[i] = be.getAttribute(end);
					be.removeAttribute(end);
				}
				scan(be);
				blockNode.appendChild(be);
			}
			BlockTransformSpecifier btd = new BlockTransformSpecifier(cl);
			btd.setArgs(siblings, names, values);
			td = btd;
		}
		else
		{
			// System.out.println("processing descrptor ");
			td = new TransformSpecifier(cl);
			td.setArg(element, label, element.getAttribute(label));
			element.removeAttribute(label);
		}
		td.setDexter(this);
		td.setProperties(properties);
		addSpecifier(element, td);
		return td;
	}

	protected void scanForDescriptors(Element el) throws Exception
	{
		Iterator<String> it = descriptors.keySet().iterator();
		while (it.hasNext()) // for each decriptor in the list
		{
			String alabel = it.next();
			if (el.hasAttribute(alabel))
			{
				TransformSpecifier td = createSpecifier(el, alabel, properties);
			}
		}
	}

	public static void addSpecifier(Element element, TransformSpecifier specifier)
	{
		List list = (List) element.getUserData(DexterityConstants.DEXTER_SPECIFIERS);
		if (list == null)
		{
			list = new ArrayList<TransformSpecifier>();
			element.setUserData(DexterityConstants.DEXTER_SPECIFIERS, list, null);
		}
		list.add(specifier);
	}

	public void scanNode(Node node) throws Exception
	{
		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element el = (Element) node;
			scanForDescriptors(el);
		}
	}

	public Element[] findContiguousSiblings(Element el, String keyAttr,
	      String endAttr, boolean remove) throws Exception
	{
		Node parent = el.getParentNode();
		NodeList children = parent.getChildNodes();
		int n = children.getLength();
		Element[] related = new Element[n];
		Node[] drop = new Node[n];
		int dc = 0;
		int c = 0;
		boolean start = false;
		for (int i = 0; i < n; ++i)
		{
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
			{
				Element ce = (Element) child;
				if (el.isSameNode(ce))
				{
					start = true;
					related[c++] = ce; // already being scanned
				}
				else if (start)
				{
					if (ce.hasAttribute(keyAttr))
					{
						related[c++] = ce;
						drop[dc++] = ce;
					}
					else if (endAttr != null && ce.hasAttribute(endAttr))
					{
						start = false;
						related[c++] = ce;
						drop[dc++] = ce;
						break; // end of the block
					}
					else
					{
						start = false;
						break; // end of the block
					}
				}
			}
			else if (start)
			{
				if (child.getNodeType() != Node.TEXT_NODE)
				{
					reportInternalError("dropping non-text nodes between siblings",null);
				}
				else if (child.getNodeValue().trim().length() != 0)
				{
					System.err.println(
							"WARNING: dropping non-empty text nodes between siblings" +
					      		"in block section...");
					System.err.print("-->> ");
					System.err.print(child.getNodeValue().trim());
					System.err.println(" <<--");
				}
				drop[dc++] = child;
			}
		}

		for (int i = 0; i < dc; ++i)
		{
			parent.removeChild(drop[i]);
		}
		// System.out.println("found of group of siblings: " + c);
		return Arrays.copyOfRange(related, 0, c);
	}

	private void putToDisk(String name, Document doc) throws Exception
	{
		File f  = new File(name);

		if (outputFile.contains(f))
		{
			throw new DexterException("duplicate output names: " + f.getPath());
		}
		else
		{
			outputFile.add(f);
		}

		Writer writer = new FileWriter(f);

		write(doc, writer, encoding);
		writer.close();
	}

	private TransformerFactory factory = TransformerFactory.newInstance();

	protected void write(Document document, Writer writer, String encoding)
	{
		try
		{

//			document.normalizeDocument();

			Transformer tranformer = factory.newTransformer();
//			tranformer.
			tranformer.setOutputProperty("indent", indent);
			tranformer.setOutputProperty("method", method);
			tranformer.setOutputProperty("media-type", mediaType);
			tranformer.setOutputProperty("encoding", encoding);

			// Writer writer = null;
			Result result = new javax.xml.transform.stream.StreamResult(writer);

			Source source = new javax.xml.transform.dom.DOMSource(document);
			tranformer.transform(source, result);
		}
		catch (Exception e)
		{
			throw new DexterException("error while rendering document",e);
			
		}
	}
//	public List<String> getIdNames()
//	{
//		return idNames;
//	}

	public static Descriptor marshallNode(Node node,Dexter dexter)
   {
   	Descriptor descriptor = new NodeDescriptor(node);
   	List<NodeSpecifier> list = (List<NodeSpecifier>) node
   	      .getUserData(DexterityConstants.DEXTER_SPECIFIERS);
   	if (list != null)
   	{
   		Iterator<NodeSpecifier> it = list.iterator();
   		TransformDescriptor td;
   		while (it.hasNext())
   		{
   			NodeSpecifier specifier = it.next();
   			descriptor = td = specifier.enclose(descriptor);
   			td.setDexter(dexter);
   		}
   	}
   	return descriptor;
   }

	public static Descriptor marshall(Node node,Dexter dexter)
   	{
   //System.out.println("marshalling " + element.getNodeName());
   		Descriptor parent = Dexter.marshallNode(node,dexter);
   		Descriptor c;
   		NodeList children = node.getChildNodes();
   		int nc = children.getLength();
   		for (int i = 0; i < nc; ++i)
   		{
   			Node child = children.item(i);
   			if(child != null &&	child.getParentNode() != null)
   			{
   				c = marshall(child,dexter);
   				parent.appendChild(c);
   			}
   		}
   		return parent;
   	}

	public static void reportInternalError(String msg, Exception ex)
	{
		PrintStream out = System.err;
		out.println("!!!! An internal error has occured: `" + msg + "' !!!!");
		out.println("    please send an error report to michael@dykman.org with the word ");
		out.println("    'DEXTER-INTERNAL' in the subject line including the following information: ");
		out.println("       * the source file which triggered the error");
		out.println("       * the version the JRE (output of '$ java -version')");
		out.println("       * the full contents of this message ");
		if(ex!=null)
		{
			ex.printStackTrace(out);
		}
		out.println("!!!! end of message !!!!");
	}
	public static void main(String[] args)
	{
		int argp = 0;
//		System.setProperty("jaxp.debug", "true");
		try
		{
			if (args.length == 0)
			{
				System.out.println("please specify an input file");
				System.exit(1);
			}
			String encoding = "UTF-8";

			Dexter dexter = new Dexter(encoding);
			dexter.setMediaType("text/html");
			dexter.setMethod("xml");
			dexter.setIndent(true);

			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setExpandEntityReferences(false);
			dbf.setValidating(false);
			
			DocumentBuilder builder = dbf.newDocumentBuilder();
			builder.setEntityResolver(new DexterEntityResolver(encoding));

			while(argp < args.length)
			{
				String fn = args[argp];
				Document impl = builder.parse(new FileInputStream(fn));
				Map<String, Document> docs = dexter.generateXSLT(fn,impl);
				++argp;
				Iterator<String> k = docs.keySet().iterator();
				while(k.hasNext())
				{
					String name = k.next();
	 				if(!name.endsWith(".dispose"))
					{
						dexter.putToDisk(name, docs.get(name));
					}
				}
			}
		}
		catch (DexterException e)
		{
			String msg = e.getMessage();
			System.err.println("DexterException: " + msg);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
