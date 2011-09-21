package org.dykman.dexter;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
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


public class Main {
	private static Properties dexterProps = new Properties();
	private static String encoding = "UTF-8";
	private static String mediaType = "text/html";
	private static String method = "html";
	private static String indent = "no";
	private static String outputDirectory = null;
	private static File userProperties = null;
	private static boolean checkValidity = true;

	private static boolean propComments = true;
	private static boolean autohash = false;
	private static String inputXSL = null;

	private static String idHash = null;

	public static void main(String[] args) {
		int argp = 0;
		LongOpt[] opts = {
			new LongOpt("mime-type", LongOpt.REQUIRED_ARGUMENT, null, 't'),
			new LongOpt("method", LongOpt.REQUIRED_ARGUMENT, null, 'm'),
			new LongOpt("properties", LongOpt.REQUIRED_ARGUMENT, null, 'p'),
			new LongOpt("encoding", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
			new LongOpt("directory", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
			new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
			new LongOpt("indent", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
			new LongOpt("define", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
			new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v'),
			new LongOpt("resolve-entities", LongOpt.NO_ARGUMENT, null,'r'),
			new LongOpt("suppress-comments", LongOpt.NO_ARGUMENT, null,'C'),
			new LongOpt("transform", LongOpt.REQUIRED_ARGUMENT, null,'x'),
			new LongOpt("skip-validation", LongOpt.NO_ARGUMENT, null, 'V'),
			new LongOpt("no-media-type", LongOpt.NO_ARGUMENT, null, 'T'),
			new LongOpt("library", LongOpt.REQUIRED_ARGUMENT, null, 'L'),
			new LongOpt("hash", LongOpt.REQUIRED_ARGUMENT, null, 'H'),
			new LongOpt("autohash", LongOpt.NO_ARGUMENT, null, 'a')
		};
		
		Getopt go = new Getopt("dexter", args,
				"m::L::o::p::e::i::t::d::x::H::hvrCVT", opts, false);
		int s;

		Set<String> libararySet = new HashSet<String>();

		while ((s = go.getopt()) != -1) {
			switch (s) {
			case 'a' :
				autohash = true;
				break;
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
			
			dbf.setValidating(false);
			dbf.setExpandEntityReferences(false);
			dbf.setCoalescing(true);
			dbf.setIgnoringComments(false);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			builder.setEntityResolver(new DexterEntityResolver(encoding));


			if (inputXSL != null) {
				TransformerFactory transFact = TransformerFactory.newInstance();
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
			dexter.setAutohash(autohash);

			String fn;
			Map<String, Document> docs = null;
			while (argp < args.length) {
				fn = args[argp];
				try {
					File dexin = new File(fn);
					if(autohash) {
						long l = dexin.lastModified();
			
					}
					docs = dexter.generateXSLT(dexin);
					Iterator<String> k = docs.keySet().iterator();
					while (k.hasNext()) {
						String name = k.next();
						Document dd = docs.get(name);
						dexter.putToDisk(new File(name), dd);
					}
					if (checkValidity) {
						k = docs.keySet().iterator();
						TransformerFactory transFact = TransformerFactory.newInstance();
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


	protected static void write(Document document, Writer writer,
			String encoding, Map<String, String> entities) throws IOException {

		try {
			DocumentSerializer serializer = new DocumentSerializer(encoding);
			serializer.setEntities(entities);
			serializer.serialize(document, writer);
		} catch (Exception e) {
			throw new DexterException("error while rendering document: "
					+ e.getMessage(), e);
		}
	}
}
