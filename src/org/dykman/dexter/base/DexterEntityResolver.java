package org.dykman.dexter.base;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DexterEntityResolver implements EntityResolver {
	String encoding;
	public DexterEntityResolver(String encoding) {
		this.encoding = encoding;
	}
	public InputSource resolveEntity(String publicId, String systemId)
	      throws SAXException, IOException {

System.out.println(">> " + publicId + " -- " + systemId);
		InputSource source = null;
		if(publicId.startsWith("&")) {
			source = new InputSource(systemId);
			source.setEncoding(encoding);
			source.setPublicId(publicId);
			Reader reader = new StringReader(publicId);
			source.setCharacterStream(reader);
		} else {
			if(publicId.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd")) {
				source = new InputSource(getClass().getResourceAsStream(
					"xhtml1-transitional.dtd"));
			} else if(publicId.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent")) {
				source = new InputSource(getClass().getResourceAsStream(
					"xhtml-lat1.ent"));
			}
		}
		return source;
	}
}
