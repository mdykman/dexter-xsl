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
	public void edit(String namespace, String name, String value)
	{
		String s = dexter.getProperty("module");
		String[] modules = s.split(",");
		String dexterity = DexterityConstants.BASE_NAMESPACE;

		DocumentTraversal tr = (DocumentTraversal)document;
		NodeIterator it = tr.createNodeIterator(
				element, NodeFilter.FILTER_ACCEPT, 
				new InputFilter(), false);
		Element e;
		while((e = (Element)it.nextNode()) != null) {
			NamedNodeMap attr = e.getAttributes();
			if(! usesNamespace(attr,modules)) {
				Node nn = attr.getNamedItem("name");
				if(nn == null) {
					System.err.println("WARNING: form element has no name");
				} else {
					String nodename = e.getNodeName();
					String fieldname = nn.getNodeValue();
					if("input".equalsIgnoreCase(nodename)) {
						String inputType = "text";
						Node inputTypeNode = attr.getNamedItem("type");
						if(inputTypeNode != null) {
							inputType = inputTypeNode.getNodeValue();
						}
						
						if("text".equalsIgnoreCase(inputType)
							|| "submit".equalsIgnoreCase(inputType)) {
							Attr newAtt = document.createAttribute(
								dexterity + ":attr");
							String exp = "value:" + value + "/" + fieldname;
							newAtt.setNodeValue(exp);
							attr.setNamedItem(newAtt);
						}
						else if("radio".equalsIgnoreCase(inputType)
							|| "checkbox".equalsIgnoreCase(inputType)) {
							Node val = attr.getNamedItem("value");
							if(val == null) {
								System.err.println(
										"WARNING: radio button has no defined value");
							} else {
								Attr newAtt = document.createAttribute(
									dexterity + ":cattr");
								String exp = "checked:" + value + "/" + fieldname
									+ "@!eq:" + val.getNodeValue() + " @!str:true";
								newAtt.setNodeValue(exp);
								attr.setNamedItem(newAtt);
							}
						}
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
	
	protected boolean usesNamespace(NamedNodeMap attr, String[] ns) {
		int len = attr.getLength();
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
