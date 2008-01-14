package org.dykman.dexter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;



public class Transformer
{
	private static TransformerFactory factory = TransformerFactory.newInstance();
	public static void main(String[] args)
	{
		try
		{
			if(args.length == 0)
			{
				System.err.println("please specify a transofmr");
				System.exit(1);
			}
			InputStream in = new FileInputStream(args[0]);
			
			if(args.length > 1)
			{
				javax.xml.transform.stream.StreamSource source 
					= new StreamSource(in);
				javax.xml.transform.Templates templates
					= factory.newTemplates(source);
				in.close();
				javax.xml.transform.Transformer transformer
					= templates.newTransformer();
				File inf = new File(args[1]);
				in = new FileInputStream(inf);
				// throw one transofrm away, just to learn the
				// output extension
				javax.xml.transform.dom.DOMResult domresult
					= new javax.xml.transform.dom.DOMResult();
				transformer.transform(new StreamSource(in), 
						domresult);
				in.close();
				Node node = domresult.getNode();
				if(node instanceof Document)
				{
					node = ((Document)node).getDocumentElement();
				}
				
				String type = null;
				if(node != null)
				{
					type = node.getNodeName().toLowerCase();
				}
				
				if(type == null || type.length() == 0)
				{
					type= "xslout";
				}
				for(int i = 1; i < args.length; ++i)
				{
					transformer
						= templates.newTransformer();
					inf = new File(args[i]);
					in = new FileInputStream(inf);
					OutputStream out = new FileOutputStream(args[i]
					    + "." + type);
					transformer.transform(new StreamSource(in), 
							new StreamResult(out));
					in.close();
					out.close();
				}
			}
			else
			{
				javax.xml.transform.stream.StreamSource source = new StreamSource(in);
				javax.xml.transform.Transformer transformer = factory.newTransformer(source);
				in.close();
				transformer.transform(new StreamSource(System.in), new StreamResult(System.out));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
