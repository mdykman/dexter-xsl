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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dykman.dexter.base.DexterEntityResolver;
import org.dykman.dexter.dexterity.DexteritySyntaxException;
import org.w3c.dom.Document;

public class Main
{
	private static Properties dexterProps = new Properties();
	private static String encoding = "UTF-8";
	private static String mediaType = "text/html";
	private static String method="html";
	private static String indent="no";
	private static String outputDirectory = null;
	private static File userProperties = null;
	private static boolean checkValidity = true;
	private static Set<File> outputFile = new HashSet<File>();
	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static boolean preserveEntities = true;

	private static boolean propComments = true;
	private static String inputXSL = null; 
	private static boolean displayMacros = false;
	
	public static void main(String[] args)
	{
		
		int argp = 0;
		LongOpt[] opts = new LongOpt[14];
		opts[0] = new LongOpt("mime-type",LongOpt.REQUIRED_ARGUMENT,null,'t');
		opts[1] = new LongOpt("method",LongOpt.REQUIRED_ARGUMENT,null,'m');
		opts[2] = new LongOpt("properties",LongOpt.REQUIRED_ARGUMENT,null,'p');
		opts[3] = new LongOpt("encoding",LongOpt.REQUIRED_ARGUMENT,null,'e');
		opts[4] = new LongOpt("directory",LongOpt.REQUIRED_ARGUMENT,null,'o');
		opts[5] = new LongOpt("help",LongOpt.NO_ARGUMENT,null,'h');
		opts[6] = new LongOpt("indent",LongOpt.REQUIRED_ARGUMENT,null,'i');
		opts[7] = new LongOpt("define",LongOpt.REQUIRED_ARGUMENT,null,'d');
		opts[8] = new LongOpt("version",LongOpt.NO_ARGUMENT,null,'v');
		opts[9] = new LongOpt("resolve-entities",LongOpt.NO_ARGUMENT,null,'r');
		opts[10] = new LongOpt("suppress-comments",LongOpt.NO_ARGUMENT,null,'C');
		opts[11] = new LongOpt("transform",LongOpt.REQUIRED_ARGUMENT,null,'x');
		opts[12] = new LongOpt("macros",LongOpt.NO_ARGUMENT,null,'M');
		opts[13] = new LongOpt("skip-validation",LongOpt.NO_ARGUMENT,null,'V');
		
		Getopt go = new Getopt("dexter",args,"m::o::p::e::i::t::d::x::hvrCMV",opts,false);
		int s;
		while((s = go.getopt()) != -1)
		{
			switch(s)
			{
				case 'M':
					displayMacros = true;
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
					preserveEntities = false;
				break;
				
				case 'v' :
					System.out.println(Dexter.DEXTER_VERSION);
					System.out.println(Dexter.DEXTER_COPYRIGHT);
					System.exit(0);
				break;
				case 'e' :
					encoding = go.getOptarg();
				break;
				case 't' :
					mediaType = go.getOptarg();
				break;
				case 'm' :
					method = go.getOptarg();
					break;
				case 'i' :
					indent = go.getOptarg();
				break;
				case 'p' :
					File userProperties = null;
					try {
						userProperties = new File(go.getOptarg());
						if(!userProperties.canRead()) {
							throw new DexterHaltException("unable to read properties file: " + userProperties.getName());
						}
						dexterProps.load(new FileInputStream(userProperties));
					} catch(IOException e) {
						throw new DexterHaltException("error reading properties file: " + userProperties.getName(),e);
					}
				break;
				case 'd' :
					String ps = go.getOptarg();
					if(ps.indexOf('=') != -1)
					{
						String b[] = ps.split("[=]",2);
						String v = b.length > 1 ? b[1] : "";
						System.setProperty(b[0], v);
					}
					else
					{
						throw new DexterException("invalid property definition: " + ps);
					}
				break;
				case 'o' :
					outputDirectory = go.getOptarg();
					File ff = new File(outputDirectory);
					if(!ff.isDirectory() || ff.canWrite())
					{
						throw new DexterException(outputDirectory + " is not a writeable directory");
					}
				break;
				case '?' :
				case 'h' :
					showHelpFile();
					System.exit(0);
				break;
				default :
					System.out.println("invalid switch specified `" + (char)s + "'");
					showHelpFile();
					System.exit(0);
			}
		}
		
		argp = go.getOptind();
		try
		{
			if ((!displayMacros) && args.length <= argp)
			{
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

			TransformerFactory transFact = TransformerFactory.newInstance( );
			if(inputXSL != null) {
				Templates templates = transFact.newTemplates(
					new StreamSource(new File((inputXSL))));
				 
				while(argp < args.length) {
					Transformer transformer = templates.newTransformer();
					Source source = new StreamSource(new File(args[argp]));
					source.setSystemId(args[argp]);
					transformer.transform(source, 
							new StreamResult(System.out));
					++argp;
				}
				System.out.println();
				System.exit(0);
			}

			Dexter dexter;
			
			if(userProperties == null) {
				dexter = new Dexter(encoding);
			}
			else {
				Properties p = new Properties();
				InputStream in = new FileInputStream(userProperties);
				p.load(in);
				in.close();
				dexter = new Dexter(encoding,p);
			}
			
			if(displayMacros) {
				dexter.baseResolver.getPropertiesMatching("macro");
				System.out.println(" defined macros:");
				for(Map.Entry<Object, Object> entry : 
					dexter.baseResolver.getPropertiesMatching("macro").entrySet()) {
					String k = entry.getKey().toString();
					System.out.print("   " + k);
					for(int i = 20 -k.length(); i >= 0; --i) {
						System.out.print(" ");
					}
					System.out.println("\t" + entry.getValue().toString());
				}
				System.out.println();
				System.exit(0);
			}
			dexter.setPropigateComments(propComments);
			
			dexter.setMediaType(mediaType);
			dexter.setMethod(method);
			dexter.setIndent(indent.equals("yes"));

			String fn;
			Map<String, Document> docs = null;
			while(argp < args.length)
			{
				fn = args[argp];
				try {
					Document impl = builder.parse(new FileInputStream(fn));
					docs = dexter.generateXSLT(fn,impl);
					Iterator<String> k = docs.keySet().iterator();
					while(k.hasNext())
					{
						String name = k.next();
		 				if(!name.endsWith(".dispose.xsl"))
						{
							putToDisk(name, docs.get(name));
						}
					}
				}
				catch(Exception e) {
					System.out.println("error while processing source file `" + fn + "'");
					throw e;
				}
				++argp;
			}
			
			Iterator<String> k = docs.keySet().iterator();
			if(checkValidity && docs != null) while(k.hasNext())
			{
				String name = k.next();
				if(!name.endsWith(".dispose.xsl")) {
					try {
						File f;
						if(outputDirectory == null) f = new File(name);
						else f = new File(outputDirectory,name);
						Source source = new StreamSource(f);
						source.setSystemId(name);
						Transformer transformer= transFact.newTransformer(source);
						transformer.hashCode();
					} catch(Exception e) {
						System.out.println("error while validating result file " + name);
						throw e;
					}
				}
			}
		}
		catch (DexterHaltException e)
		{
			// just end it quietly
		}
		catch (DexteritySyntaxException e)
		{
			System.err.println("Syntax exception in dexterity descriptor: " + e.getMessage());
		}
		catch (DexterException e)
		{
			System.err.println("DexterException: " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println("unexpected exception");
			e.printStackTrace();
		}
	}
	
	protected static void showHelpFile()
	{
		try
		{
			InputStream in = Dexter.class.getResourceAsStream("help.txt");
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String  line;
			while((line = read.readLine())!= null)
				System.out.println(line);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static void putToDisk(String name, Document doc) throws Exception
	{
		File f;
		if(outputDirectory == null) f = new File(name);
		else f = new File(outputDirectory,name);

		if (outputFile.contains(f)) 
			throw new DexterException("duplicate output names: " + f.getPath());
		else	outputFile.add(f);

		HackWriter writer = new HackWriter(new FileWriter(f));
		writer.setPreserveEntities(preserveEntities);
		writer.setEntities((Map<String,String>)doc.getUserData("entity-map"));
		
		write(doc, writer, encoding);
		writer.close();
	}

	protected static void write(Document document, Writer writer, String encoding)
	{
		try
		{
			Transformer tranformer = transformerFactory.newTransformer();
			tranformer.setOutputProperty("indent", "no");
			tranformer.setOutputProperty("method", "xml");
			tranformer.setOutputProperty("media-type","text/xsl");
			tranformer.setOutputProperty("encoding", encoding);

			Result result = new javax.xml.transform.stream.StreamResult(writer);
			Source source = new javax.xml.transform.dom.DOMSource(document);

			tranformer.transform(source, result);
		}
		catch (Exception e)
		{
			throw new DexterException("error while rendering document: " 
					+ e.getMessage(),e);
		}
	}
}