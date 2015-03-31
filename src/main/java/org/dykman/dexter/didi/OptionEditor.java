package org.dykman.dexter.didi;

import org.dykman.dexter.DexteritySyntaxException;
import org.dykman.dexter.base.AbstractDocumentEditor;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class OptionEditor extends AbstractDocumentEditor
{

	public void edit(String namespace, String name, String value)
	{
		String sep = null;
		if(name.endsWith("s")) {
			sep = value.substring(0,1);
		}
		String dexterity = DexterityConstants.BASE_NAMESPACE;
		
		String[] args = splitArgs(value);
		if(args.length < 1) {
			throw new DexteritySyntaxException("too few arguments in " + name + " attribute");
		}

		if(args[0].startsWith("!")) {
			// make a strict and insert it previously
			args[0] = args[0].substring(1);
			Node nn = element.cloneNode(true);
			NamedNodeMap attr = nn.getAttributes();
			attr.removeNamedItem(name);
			Node parent = element.getParentNode();
			parent.insertBefore(nn, element);
		}
		
		String v;
		NamedNodeMap attr = element.getAttributes();

		Attr a = document.createAttribute(dexterity + ":each");
		v = args[0];
		a.setValue(v);
		attr.setNamedItem(a);
		
		a = document.createAttribute( dexterity + ":text");
		if(args.length > 1) {
			a.setValue(args[1]);
		} else {
			a.setValue(".");
		}
		attr.setNamedItem(a);

		if(args.length > 2) {
			if(sep == null) {
				a = document.createAttribute(dexterity + ":attr");
				v = "value:" + args[2];
				a.setValue(v);
				attr.setNamedItem(a);
			} else {
				a = document.createAttribute(dexterity + ":attrs");
				v = sep + "value:" + args[2];
				a.setValue(v);
				attr.setNamedItem(a);
			}
		}
		
		if(args.length > 3) {
			if(sep == null) {
				a = document.createAttribute(dexterity + ":cattr");
				v = "selected:'true' "  + args[3];
				a.setValue(v);
				attr.setNamedItem(a);
			} else {
				a = document.createAttribute(dexterity + ":cattrs");
				v = sep + "selected:'true'" + sep  + args[3];
				a.setValue(v);
				attr.setNamedItem(a);
			}
		}
	}
}
