package org.dykman.dexter.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderAdapter;

public class Reader extends XMLFilterImpl {

	DocumentBuilder db;
	public Document document;
	Element element;

	public Reader() throws Exception {
		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	@Override
	public InputSource resolveEntity(String arg0, String arg1)
			throws SAXException, IOException {
		System.out.print("resolving: " + arg0 + ":" + arg1);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		System.out.print("notationDecl: " + name + ":" + publicId+ ":" + systemId);

	}

	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		System.out.print("unparsedEntityDecl: " + name + ":" + publicId 
			+ ":" + systemId + ":" + notationName);
	}


	public void append(char[] arg0, int arg1, int arg2)
	throws SAXException {
		element.appendChild(document
				.createTextNode(new String(arg0, arg1, arg2)));
}
	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		this.append(arg0, arg1, arg2);
	}
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		this.append(arg0, arg1, arg2);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}


	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		document.appendChild(document.createProcessingInstruction(arg0, arg1));
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		document = db.newDocument();
	}

	@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		element = (Element) element.getParentNode();
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		System.out.print("skipped: " + arg0);
	}

	@Override
	public void startElement(String arg0, String arg1, String arg2,
			Attributes arg3) throws SAXException {
		Element el = document.createElement(arg0);
		if (element == null) {
			document.appendChild(el);
		} else {
			element.appendChild(el);
		}
		element = el;

		if (arg3 != null)
			for (int i = 0; i < arg3.getLength(); ++i) {
				element.setAttribute(arg3.getQName(i), arg3.getValue(i));
			}

	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// prefix stack?
		System.out.print("prefix mapping start: " + arg0 + " : " + arg1);
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		System.out.print("prefix mapping end: " + arg0);
	}

	public static void main(String [] args) {
		try {
			for(String arg : args) {
				Reader r = new Reader();
				File f = new File(arg);
				if(f.exists()) {
					InputStream is = new FileInputStream(f);
					InputSource source = new InputSource(is);
					r.parse(source);
					is.close();
				} else {
					System.err.print("failed to open " + f.getAbsolutePath());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
