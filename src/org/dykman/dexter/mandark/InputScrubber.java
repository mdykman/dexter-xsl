package org.dykman.dexter.mandark;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;


public class InputScrubber
{
	Tidy tidy = new Tidy();
	public Document getDocument(InputStream in, OutputStream out)
	{
		tidy.setXmlOut(true);
		return tidy.parseDOM(in, out);
		
	}
	public Document getDocument(InputStream in)
	{
		return getDocument(in, null);
	}
	
	public void print(Document doc, OutputStream out)
	{
		tidy.pprint(doc, out);
	}
	
	public static void main(String[] args)
	{
		try
		{
			InputScrubber scrubber = new InputScrubber();
			
			FileInputStream in = new FileInputStream(args[0]);
			Document doc = scrubber.getDocument(in);
			scrubber.print(doc, System.out);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
