package org.dykman.dexter.didi;

import org.dykman.dexter.base.AbstractDocumentEditor;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;


public class FormCompletionEditor extends AbstractDocumentEditor
{
	protected void processInput(
			String fieldname, 
			NamedNodeMap attr,
			String value,
			String dexterity) {
		Attr newAtt = null;
		String inputType = "text";
		Node inputTypeNode = attr.getNamedItem("type");
		if(inputTypeNode != null) {
			inputType = inputTypeNode.getNodeValue();
		}
		
		if("text".equalsIgnoreCase(inputType)
			|| "submit".equalsIgnoreCase(inputType)
			|| "reset".equalsIgnoreCase(inputType)
			|| "button".equalsIgnoreCase(inputType)) {
			newAtt = document.createAttribute(
				dexterity + ":attr");
			String exp = "value:" + value + "/" + fieldname;
			newAtt.setNodeValue(exp);
			attr.setNamedItem(newAtt);
		}
		else if("image".equalsIgnoreCase(inputType)) {
			newAtt = document.createAttribute(
					dexterity + ":attr");
				String exp = "src:" + value + "/" + fieldname;
				newAtt.setNodeValue(exp);
				attr.setNamedItem(newAtt);
		}
		else if("radio".equalsIgnoreCase(inputType)
			|| "checkbox".equalsIgnoreCase(inputType)) {
			Node val = attr.getNamedItem("value");
			if(val == null) {
				System.err.println(
						"WARNING: " + inputType + " has no defined value");
			} else {
				newAtt = document.createAttribute(
					dexterity + ":cattr");
				String exp = "checked:" + value + "/" + fieldname
					+ "@!eq:" + val.getNodeValue() + " @!str:true";
				newAtt.setNodeValue(exp);
				attr.setNamedItem(newAtt);
			}
		}
		
	}
	public void edit(String namespace, String name, String value)
	{
		String dexterity = DexterityConstants.BASE_NAMESPACE;

		DocumentTraversal tr = (DocumentTraversal)document;
		NodeIterator it = tr.createNodeIterator(
				element, NodeFilter.FILTER_ACCEPT, 
				new InputFilter(), false);
		Element e;
		String[] ns = dexter.namespaces();
		while((e = (Element)it.nextNode()) != null) {
			NamedNodeMap attr = e.getAttributes();
			if(! usesNamespace(attr,ns)) {
				Node nn = attr.getNamedItem("name");
				if(nn == null) {
					System.err.println("WARNING: form element has no name");
				} else {
					String nodename = e.getNodeName();
					String fieldname = nn.getNodeValue();
					if("input".equalsIgnoreCase(nodename)) {
						processInput(fieldname, attr, value, dexterity);
					} else { // apparently, it's a textarea
						Attr newAtt = document.createAttribute(
							dexterity + ":value");
						String exp = value + "/" + fieldname;
						newAtt.setNodeValue(exp);
						attr.setNamedItem(newAtt);
					}
				}
			}
		}
	}
	
	protected boolean usesNamespace(NamedNodeMap attr,String[] ns) {
		int len = attr.getLength();
//		dexter.
		for(int i = 0; i < len; ++i) {
			Node a = attr.item(i);
			for(String n : ns) {
				if(a.getNodeName().startsWith(n + ":")) {
					return true;
				}
			}
		}
		return false;
	}
	
	static class InputFilter implements NodeFilter {

		public short acceptNode(Node n)
        {
			if(n instanceof Element) {
				if("input".equalsIgnoreCase(n.getNodeName())) {
					return FILTER_ACCEPT;
				}
				else if("textarea".equalsIgnoreCase(n.getNodeName())) {
					return FILTER_ACCEPT;
				}
			}
	        return FILTER_SKIP;
        }
	}

}
