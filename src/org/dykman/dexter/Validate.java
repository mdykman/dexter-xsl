package org.dykman.dexter;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Validate
{


	public static void main(String[] args)
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			builder.parse(new File(args[0]));
//			System.out.println("validate succeeded");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//		builder.setEntityResolver(new DexterEntityResolver(encoding));
	}

}
