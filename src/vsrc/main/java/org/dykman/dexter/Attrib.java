package org.dykman.dexter;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dykman.dexter.base.DexterEntityResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Attrib
{
	public void show(Node n)
	{
		if(n instanceof Element)
		{
			Element element = (Element) n;
			
			if(element.hasAttribute("xsl:test"))
			{
//System.out.println("found it");				
			}
			NodeList children = element.getChildNodes();
			if(children != null)
			{
				for(int i = 0; i < children.getLength(); ++i)
				{
					show(children.item(i));
				}
			}
		}
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
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder builder = dbf.newDocumentBuilder();

			builder.setEntityResolver(new DexterEntityResolver(encoding));
			
			while(argp < args.length)
			{
				String fn = args[argp];
				Document impl = builder.parse(new FileInputStream(fn));
				Attrib att = new Attrib();
				att.show(impl.getDocumentElement());
				++argp;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

}
