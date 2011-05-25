package org.dykman.dexter.base;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DexterEntityResolver implements EntityResolver
{
	String encoding;
	public DexterEntityResolver(String encoding)
	{
		this.encoding = encoding;
	}
	public InputSource resolveEntity(String publicId, String systemId)
	      throws SAXException, IOException
	{
System.out.println("resolving " + publicId + ", " + systemId);
		InputSource source = new InputSource(systemId);
		source.setEncoding(encoding);
		source.setPublicId(publicId);
		if(publicId.startsWith("&"))
		{
			Reader reader = new StringReader(publicId);
			source.setCharacterStream(reader);
		}
		else
		{
			Reader reader = new StringReader("");
			source.setCharacterStream(reader);
		}
		return source;
	}

}
