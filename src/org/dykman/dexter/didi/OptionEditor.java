package org.dykman.dexter.didi;

import org.dykman.dexter.DexteritySyntaxException;
import org.dykman.dexter.base.AbstractDocumentEditor;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

public class OptionEditor extends AbstractDocumentEditor
{

	public OptionEditor()
	{
		// TODO Auto-generated constructor stub
	}

	public void edit(String namespace, String name, String value)
	{
		String sep = null;
		if(name.endsWith("s")) {
			sep = value.substring(0,1);
		}
		
		String[] args = splitArgs(value);
		if(args.length < 2) {
			throw new DexteritySyntaxException("too few arguments in " + name + " attribute");
		}

		String v;
		NamedNodeMap attr = element.getAttributes();

		Attr a = document.createAttribute("dx:each");
		v = args[0];
		a.setValue(v);
		attr.setNamedItem(a);
		
		a = document.createAttribute( "dx:value");
		v = args[1];
		a.setValue(v);
		attr.setNamedItem(a);

		if(args.length > 2) {
			if(sep == null) {
				a = document.createAttribute("dx:attr");
				v = "value:" + args[2];
				a.setValue(v);
				attr.setNamedItem(a);
			} else {
				a = document.createAttribute("dx:attrs");
				v = sep + "value:" + args[2];
				a.setValue(v);
				attr.setNamedItem(a);
			}
		}
		
		if(args.length > 3) {
			if(sep == null) {
				a = document.createAttribute("dx:cattr");
				v = "selected:" + args[3] + " @!str:true";
				a.setValue(v);
				attr.setNamedItem(a);
			} else {
				a = document.createAttribute("dx:cattrs");
				v = sep + "selected:" + args[3] + sep + "@!str:true";
				a.setValue(v);
				attr.setNamedItem(a);
			}
		}
	}
}
