package org.dykman.dexter.didi;

import org.dykman.dexter.base.AbstractDocumentEditor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class LivenessEditor extends AbstractDocumentEditor
{
	public void edit(String namespace, String name, String value) {
		String [] parts = value.split("[,]");

		String hash = dexter.getIdHash();

		DocumentTraversal tr = (DocumentTraversal)document;
		NodeIterator it = tr.createNodeIterator(
				element, NodeFilter.FILTER_ACCEPT, 
				new JsTagFilter(), false);
		Element el;
		while((el = (Element)it.nextNode()) != null) {
			String path = el.getAttribute("src");
			int n = path.lastIndexOf('.');
			if(n > -1) {
				String base = path.substring(0,n);
				el.setAttribute("src", base + '@' + hash + path.substring(n));
			}
		}

		 it = tr.createNodeIterator(
					element, NodeFilter.FILTER_ACCEPT, 
					new CssTagFilter(), false);
		while((el = (Element)it.nextNode()) != null) {
			String path = el.getAttribute("href");
			int n = path.lastIndexOf('.'); 
			if(n > -1) {
				String base = path.substring(0,n);
				el.setAttribute("href", base + '@' + hash + path.substring(n));
			}
		}
	}
	

	static class JsTagFilter implements NodeFilter {
		public short acceptNode(Node n) {
			short result = NodeFilter.FILTER_REJECT;
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) n;
				result = "script".equalsIgnoreCase(element.getNodeName()) && element.getAttribute("src") != null ?
					NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT;
			}
			return result;
		}
	}

	static class CssTagFilter implements NodeFilter {
		public short acceptNode(Node n) {
			short result = NodeFilter.FILTER_REJECT;
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) n;
				result = "link".equalsIgnoreCase(element.getNodeName()) && "stylesheet".equalsIgnoreCase(element.getAttribute("rel")) ?
					NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_REJECT;
			}
			return result;
		}
	}
}
