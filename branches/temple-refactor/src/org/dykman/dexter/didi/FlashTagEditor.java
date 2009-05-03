package org.dykman.dexter.didi;

import org.dykman.dexter.base.AbstractDocumentEditor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class FlashTagEditor extends AbstractDocumentEditor
{
	
	public FlashTagEditor()
	{
	}

	public void edit(String namespace, String name, String value)
	{
		Element newElement = document.createElement("object");
		NamedNodeMap attr = element.getAttributes();
		int len = attr.getLength();
		String [] ns = dexter.namespaces();
		for(int i = 0; i < len; ++i) {
			Node a = attr.item(i);
			for(String n : ns) {
				if(a.getNodeName().startsWith(n + ":")) {
					newElement.setAttribute(a.getNodeName(), 
						a.getNodeValue());
						break;
				}
			}
		}
		
		newElement.setAttribute("width",element.getAttribute("width"));
		newElement.setAttribute("height",element.getAttribute("height"));
		newElement.setAttribute("dx:attr","movie:" + value);

		Element embed = document.createElement("embed");
		embed.setAttribute("type","application/x-shockwave-flash"); 
		embed.setAttribute("width",element.getAttribute("width"));
		embed.setAttribute("height",element.getAttribute("height"));
		embed.setAttribute("src",value);
		newElement.setAttribute("dx:attr","src:" + value);

		DocumentTraversal tr = (DocumentTraversal)document;
		NodeIterator it = tr.createNodeIterator(
				element, NodeFilter.FILTER_ACCEPT, 
				new ParamFilter(), false);

		Element n;
		while((n = (Element)it.nextNode()) != null) {
			newElement.appendChild(n.cloneNode(false));
			embed.setAttribute(n.getAttribute("name"),
				n.getAttribute("value"));
		}

		newElement.appendChild(embed);
		// replace the current element with this one
		Node parent = element.getParentNode();
		parent.replaceChild(newElement, element);
	}

	static class ParamFilter implements NodeFilter {
		public short acceptNode(Node n) {
			if("param".equalsIgnoreCase(n.getNodeName())) {
				return NodeFilter.FILTER_ACCEPT;
			}
			return NodeFilter.FILTER_REJECT;
		
		}
	}

}
