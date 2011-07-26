package org.dykman.dexter;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dykman.dexter.base.DexterEntityResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Main {
	private static Properties dexterProps = new Properties();
	private static String encoding = "UTF-8";
	private static String mediaType = "text/html";
	private static String method = "html";
	private static String indent = "no";
	private static String outputDirectory = null;
	private static File userProperties = null;
	private static boolean checkValidity = true;
	private static Set<File> outputFile = new HashSet<File>();
//	private static TransformerFactory transformerFactory = TransformerFactory
//			.newInstance();
	static {
		// transformerFactory.
	}
//	private static boolean preserveEntities = true;

	private static boolean propComments = true;
	private static String inputXSL = null;

	private static String idHash = null;

	public static void main(String[] args) {

		int argp = 0;
		LongOpt[] opts = new LongOpt[16];
		opts[0] = new LongOpt("mime-type", LongOpt.REQUIRED_ARGUMENT, null, 't');
		opts[1] = new LongOpt("method", LongOpt.REQUIRED_ARGUMENT, null, 'm');
		opts[2] = new LongOpt("properties", LongOpt.REQUIRED_ARGUMENT, null,
				'p');
		opts[3] = new LongOpt("encoding", LongOpt.REQUIRED_ARGUMENT, null, 'e');
		opts[4] = new LongOpt("directory", LongOpt.REQUIRED_ARGUMENT, null, 'o');
		opts[5] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
		opts[6] = new LongOpt("indent", LongOpt.REQUIRED_ARGUMENT, null, 'i');
		opts[7] = new LongOpt("define", LongOpt.REQUIRED_ARGUMENT, null, 'd');
		opts[8] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
		opts[9] = new LongOpt("resolve-entities", LongOpt.NO_ARGUMENT, null,
				'r');
		opts[10] = new LongOpt("suppress-comments", LongOpt.NO_ARGUMENT, null,
				'C');
		opts[11] = new LongOpt("transform", LongOpt.REQUIRED_ARGUMENT, null,
				'x');
		opts[12] = new LongOpt("skip-validation", LongOpt.NO_ARGUMENT, null,
				'V');
		opts[13] = new LongOpt("no-media-type", LongOpt.NO_ARGUMENT, null, 'T');
		opts[14] = new LongOpt("library", LongOpt.REQUIRED_ARGUMENT, null, 'L');
		opts[15] = new LongOpt("hash", LongOpt.REQUIRED_ARGUMENT, null, 'H');

		Getopt go = new Getopt("dexter", args,
				"m::L::o::p::e::i::t::d::x::H::hvrCVT", opts, false);
		int s;

		Set<String> libararySet = new HashSet<String>();

		while ((s = go.getopt()) != -1) {
			switch (s) {
			case 'H':
				idHash = go.getOptarg();

				break;
			case 'L':
				libararySet.add(go.getOptarg());
				break;
			case 'V':
				checkValidity = false;
				break;

			case 'x':
				inputXSL = go.getOptarg();
				break;
			case 'C':
				propComments = false;
				break;
			case 'r':
//				preserveEntities = false;
				break;

			case 'v':
				System.out.println(Dexter.DEXTER_VERSION);
				System.out.println(Dexter.DEXTER_COPYRIGHT);
				System.exit(0);
				break;
			case 'e':
				encoding = go.getOptarg();
				break;
			case 't':
				mediaType = go.getOptarg();
				break;
			case 'T':
				mediaType = null;
				break;
			case 'm':
				method = go.getOptarg();
				break;
			case 'i':
				indent = go.getOptarg();
				break;
			case 'p':
				try {
					File userProperties  = new File(
							go.getOptarg());
					if (!userProperties.canRead()) {
						throw new DexterHaltException(
								"unable to read properties file: "
										+ userProperties.getName());
					}
					dexterProps.load(new FileInputStream(userProperties));
				} catch (IOException e) {
					throw new DexterHaltException(
							"error reading properties file: "
									+ userProperties.getName(), e);
				}
				break;
			case 'd':
				String ps = go.getOptarg();
				if (ps.indexOf('=') != -1) {
					String b[] = ps.split("[=]", 2);
					String v = b.length > 1 ? b[1] : "";
					System.setProperty(b[0], v);
				} else {
					throw new DexterException("invalid property definition: "
							+ ps);
				}
				break;
			case 'o':
				outputDirectory = go.getOptarg();
				File ff = new File(outputDirectory);
				if (!ff.isDirectory() || ff.canWrite()) {
					throw new DexterException(outputDirectory
							+ " is not a writeable directory");
				}
				break;
			case '?':
			case 'h':
				showHelpFile();
				System.exit(0);
				break;
			default:
				System.out.println("invalid switch specified `" + (char) s
						+ "'");
				showHelpFile();
				System.exit(3);
			}
		}

		argp = go.getOptind();
		try {
			if (args.length <= argp) {
				showHelpFile();
				System.exit(1);
			}
           
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
//			dbf.setEntityResolver(new DexterEntityResolver(encoding));			
			dbf.setValidating(false);
			dbf.setExpandEntityReferences(false);
//			Schema schema = dbf.getSchema();
			// WHY AGAIN??
			dbf.setCoalescing(true);
			dbf.setIgnoringComments(false);
			DocumentBuilder builder = dbf.newDocumentBuilder();
System.out.println("builder = " + builder.getClass().getName());
			builder.setEntityResolver(new DexterEntityResolver(encoding));
//			HtmlDocumentBuilder builder= new HtmlDocumentBuilder();
//System.out.println("VALIDATING = " + builder.isValidating());


TransformerFactory transFact = TransformerFactory.newInstance();
			if (inputXSL != null) {
				Templates templates = transFact.newTemplates(new StreamSource(
						new File((inputXSL))));

				while (argp < args.length) {
					Transformer transformer = templates.newTransformer();
					Source source = new StreamSource(new File(args[argp]));
					source.setSystemId(args[argp]);
					transformer.transform(source, new StreamResult(System.out));
					++argp;
				}
				System.out.println();
				System.exit(0);
			}

			Dexter dexter = new Dexter(encoding, dexterProps, builder);
			dexter.setIdHash(idHash);

			dexter.loadLibraryTemplate(builder, libararySet);

			dexter.setPropigateComments(propComments);

			if (mediaType != null)
				dexter.setMediaType(mediaType);
			dexter.setMethod(method);
			dexter.setIndent(indent.equals("yes"));

			String fn;
			Map<String, Document> docs = null;
			while (argp < args.length) {
				fn = args[argp];
				try {
					Document impl = builder.parse(new FileInputStream(fn));
					// dexter.dump(impl);
					docs = dexter.generateXSLT(dexter.getHashName(fn), impl);
					Iterator<String> k = docs.keySet().iterator();
					while (k.hasNext()) {
						String name = k.next();
						if (!name.endsWith(".dispose.xsl")) {
							Document dd = docs.get(name);
							// System.out.println("SAVING");
							// dexter.dump(dd);
							putToDisk(name, dd, dexter);
						}
					}
					if (checkValidity) {
						k = docs.keySet().iterator();
						while (k.hasNext()) {
							String name = k.next();
							File f;
							if (outputDirectory == null)
								f = new File(name);
							else
								f = new File(outputDirectory, name);
							StreamSource source = new StreamSource(f);
							source.setSystemId(f);
							Transformer transformer = transFact
									.newTransformer(source);
							transFact.setURIResolver(new URIResolver() {

								@Override
								public Source resolve(String arg0, String arg1)
										throws TransformerException {
//									System.out.println("resoving " + arg0
//											+ "::" + arg1);
									// TODO Auto-generated method stub
									return null;
								}
							});
							transformer.hashCode();
						}
					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
					System.out.print("error while processing source file `"
							+ fn + "'");
					System.out.println(e.getMessage());
				}
				++argp;
			}

		} catch (DexterHaltException e) {
			// just end it quietly
		} catch (DexteritySyntaxException e) {
			System.err.println("Syntax exception in dexterity descriptor: "
					+ e.getMessage());
		} catch (DexterException e) {
			System.err.println("DexterException: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("unexpected exception");
			e.printStackTrace();
		}
	}

	protected static void showHelpFile() {
		try {
			InputStream in = Dexter.class.getResourceAsStream("help.txt");
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String line;
			System.out.print(Dexter.DEXTER_VERSION);
			System.out.print(" ");
			System.out.println(Dexter.DEXTER_COPYRIGHT);
			while ((line = read.readLine()) != null)
				System.out.println(line);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void collectEntities(Node n, Map<String,String> m, Dexter dexter) {
		if (n.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
			// System.out.print("entity reference ");
			// System.out.println(n.getNodeName() + " " +
			// dexter.getEntity(n.getNodeName()));
			m.put(n.getNodeName(), dexter.getEntity(n.getNodeName()));
		}
		NodeList nl = n.getChildNodes();
		for (int i = 0; i < nl.getLength(); ++i) {
			collectEntities(nl.item(i), m, dexter);
		}
	}

	private static void putToDisk(String name, Document doc, Dexter dexter)
			throws Exception {
		File f;

		if (outputDirectory == null)
			f = new File(name);
		else
			f = new File(outputDirectory, name);

		if (outputFile.contains(f))
			throw new DexterException("duplicate output names: " + f.getPath());
		else
			outputFile.add(f);

		Map<String, String> mm = new HashMap<String, String>();
		collectEntities(doc, mm, dexter);
		Writer writer = new FileWriter(f);
		write(doc, writer, encoding, mm);
		writer.close();
	}

	protected static void write(Document document, Writer writer,
			String encoding, Map<String, String> entities) throws IOException {

		try {
			DocumentSerializer serializer = new DocumentSerializer(encoding);
			serializer.setEntities(entities);
			serializer.serialize(document, writer);
/*
			// document

			// document.getDocumentElement().setAttributeNS(
			// "http://www.w3.org/1999/XSL/Transform", "xsl", "value");
			// document.createElementNS(
			// "http://www.w3.org/1999/XSL/Transform","xml");
			OutputFormat of = new OutputFormat("XML", "UTF-8", true);
			// of.setDoctype("xsl", "http://www.w3.org/1999/XSL/Transform");
			of.setOmitDocumentType(false);
			// of.setOmitDocumentType(false);
			// of.

			XMLSerializer serializer = new XMLSerializer(writer, of);
			serializer.startPreserving();
			serializer.startDocument();
			// serializer.
			for (Map.Entry<String, String> entry : entities.entrySet()) {
				serializer.unparsedEntityDecl("one", "two", "three", "four");
				// System.out.println("    " + entry.getKey() + "::" +
				// entry.getValue());
				// serializer.internalEntityDecl(entry.getKey(),
				// entry.getValue0());
			}
			// serializer.startDTD("Entity", "b", "c");
			// serializer.endDTD();
			serializer.serialize(document.getDocumentElement());

			serializer.endDocument();
	writer.write("\n");
	*/
		} catch (Exception e) {
			throw new DexterException("error while rendering document: "
					+ e.getMessage(), e);
		}
	}
}
