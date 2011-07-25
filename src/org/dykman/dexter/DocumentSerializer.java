package org.dykman.dexter;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentSerializer {
	public String encoding = "UTF-8";
	protected Map<String,String> entities;

	boolean xmlDeclaration = false;

	public DocumentSerializer(String encoding, boolean xmlDeclaration) {
		this.encoding = encoding;
		this.xmlDeclaration = xmlDeclaration;
	}
	public DocumentSerializer(String encoding) {
		this(encoding,false);
	}
	public DocumentSerializer() {
		this("UTF-8",false);
	}
	
	public void setEntities(Map<String,String> entities) {
		this.entities = entities;
	}
	protected void writeDocType(Document document, Writer writer)
			throws IOException {
		DocumentType dt = document.getDoctype();
		Element docEl = document.getDocumentElement();
		if (dt != null) {
			// <!DOCTYPE Configure PUBLIC
			// "-//Mort Bay Consulting//DTD Configure//EN"
			// "http://jetty.mortbay.org/configure.dtd">
			String publicId = dt.getPublicId();
			String systemId = dt.getSystemId();
			writer.append("<!DOCTYPE ").append(docEl.getNodeName());
			/*
			if (publicId != null) {
				writer.append(" PUBLIC \"").append(publicId).append("\"");
			}
			if (systemId != null) {
				writer.append(" \"").append(systemId).append("\"");
			}
			*/
			if(entities.size() > 0) {
			writer.append(" [\n");
			for(String key : entities.keySet())
			{
				writer.append("  <!ENTITY ")
					.append(key).append(" \"")
					.append(entities.get(key))
					.append("\" >\n");
			}
			writer.append("  ]");
			}
			writer.append(">\n");
		}

	}

	protected String formatAttributeValue(String s) {
		return formatText(s).replaceAll("\"", "&quot;")
				.replaceAll("'", "&apos;").replaceAll("\n", "&#10;")
				.replaceAll("\r", "&#13;");
	}

	protected String formatText(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;");
	}

	protected String formatComment(String s) {
		return formatText(s).replaceAll("--", "-&#45;");
	}

	public void writeNode(Node node, Writer writer) throws IOException {
		switch (node.getNodeType()) {
		case Node.DOCUMENT_TYPE_NODE:
//			writer.write("AHHH!!!!");
			break;
		case Node.ELEMENT_NODE:
			writer.append("<").append(node.getNodeName());
			NamedNodeMap nm = node.getAttributes();
			if (nm != null)
				for (int i = 0; i < nm.getLength(); ++i) {
					Node nn = nm.item(i);
					writer.append(' ').append(nn.getNodeName()).append("=\"")
							.append(formatAttributeValue(nn.getNodeValue()))
							.append('"');
				}
			writer.append(">");

			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); ++i) {
				writeNode(nl.item(i), writer);
			}

			writer.append("</").append(node.getNodeName()).append(">");
			break;
		case Node.COMMENT_NODE:
			writer.append("<!-- ").append(formatComment(node.getNodeValue()))
					.append(" -->");
			break;
		case Node.TEXT_NODE:
System.out.println("TEXT ||" + node.getNodeValue() + "||");			
			writer.append(formatText(node.getNodeValue()));
			break;
		case Node.ENTITY_REFERENCE_NODE:
System.out.println("ENTITY_REFERENCE_NODE ||" + node.getNodeValue() + "||");			
			writer.append('&').append(node.getNodeName()).append(';');
			break;
		case Node.ENTITY_NODE:
System.out.println("ENTITY_NODE ||" + node.getNodeValue() + "||");			
			break;
		}
	}

	public void serialize(Document document, Writer writer) throws IOException {
		if (xmlDeclaration) {
			writer.write("<?xml version=\"1.0\" encoding=\"");
			writer.write(encoding);
			writer.write("\" ?>\n");
		}
		writeDocType(document, writer);
		writer.write("\n");
		writeNode(document.getDocumentElement(), writer);
	}
}
