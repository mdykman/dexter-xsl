package org.dykman.dexter.mandark;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import org.w3c.tidy.Tidy;

/**
 * * This program shows how HTML could be tidied directly from * a URL stream,
 * and running on separate threads. Note the use * of the 'parse' method to
 * parse from an InputStream, and send * the pretty-printed result to an
 * OutputStream. * In this example thread th1 outputs XML, and thread th2
 * outputs * HTML. This shows that properties are per instance of Tidy.
 */
public class Nn implements Runnable
{
	private String	url;
	private String	outFileName;
	private String	errOutFileName;
	private boolean	xmlOut;

	public Nn(String url, String outFileName, String errOutFileName,
	        boolean xmlOut)
	{
		this.url = url;
		this.outFileName = outFileName;
		this.errOutFileName = errOutFileName;
		this.xmlOut = xmlOut;
	}

	public void run()
	{
		URL u;
		BufferedInputStream in;
		FileOutputStream out;
		Tidy tidy = new Tidy();
		tidy.setXmlOut(xmlOut);
		try
		{
			tidy
			        .setErrout(new PrintWriter(new FileWriter(errOutFileName),
			                true));
			u = new URL(url);
			in = new BufferedInputStream(u.openStream());
			out = new FileOutputStream(outFileName);
			tidy.parseDOM(in, out);
			tidy.parse(in, out);
		} catch (IOException e)
		{
			System.out.println(this.toString() + e.toString());
		}
	}

	public static void main(String[] args)
	{
		Nn t1 = new Nn(args[0], args[1], args[2], true);
		Nn t2 = new Nn(args[3], args[4], args[5], false);
		Thread th1 = new Thread(t1);
		Thread th2 = new Thread(t2);
		th1.start();
		th2.start();
	}
}