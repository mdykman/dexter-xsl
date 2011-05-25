package org.dykman.dexter.gossamer;

import org.dykman.dexter.base.XSLTDocSequencer;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ImageDescriptor extends PathDescriptor {

	public ImageDescriptor(Descriptor descriptor) {
		super(descriptor);
	}


	@Override
	public void attributes() {
		boolean useDefault = false;
		
		String[] x =value.split("[|]",2);
		String exp = x[0];
		String infix = x.length > 1 ? x[1] : null;
		
		if(exp.startsWith("!")) {
			exp = exp.substring(1);
		}
		
		String src = element.getAttribute("src");
		element.removeAttribute("src");

		super.attributes();
		
		if(src != null) {
			int n = src.lastIndexOf('.');
			if(n != -1) {
				sequencer.loadTemplate("gossamer-image");

				Element attr = sequencer.createElement(XSLTDocSequencer.XSLATTRIBUTE);
				attr.setAttribute("name","src");
				Document doc = attr.getOwnerDocument();
				Element val = doc.createElement(XSLTDocSequencer.XSLCALLTEMPLATE);
				val.setAttribute("name","gossamer-image");
				attr.appendChild(val);
				
				attr = doc.createElement(XSLTDocSequencer.XSLWITHPARAM);
				attr.setAttribute(name, "param1");
				attr.setAttribute("select", exp);
				val.appendChild(attr);

				attr = doc.createElement(XSLTDocSequencer.XSLWITHPARAM);
				attr.setAttribute(name, "param2");
				attr.setAttribute("select", infix);
				val.appendChild(attr);
				
			}
		}
	}

}
