package org.dykman.dexter.dexterity;

import java.util.List;

import org.dykman.dexter.base.PathEval;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Element;

public class CDataDescriptor extends PathDescriptor
{
	public CDataDescriptor(Descriptor descriptor) {
		super(descriptor);
	}
	@Override
	public void start() {
		sequencer.appendText("<![CDATA[", true);
	}
	@Override
	public void end() {
		sequencer.appendText("]]>", true);
	}
	@Override
	public void children() {
		
		boolean useDefault = false;
		if(value.startsWith("!")) {
			value = value.substring(1);
			useDefault = true;
		}
		if(value.length() == 0) {
			super.children();
		} else {
			List<PathEval> pp = PathEval.parse(value);
			if(pp.size() == 1) {
				sequencer.copyNodes(pp.get(0), 
					useDefault ? ((Element)element).getTextContent() : null, true);
			} 
			else {
				throw new DexteritySyntaxException("CDATA descriptor may only take a single xpath expression");
			}
		}
		// WHY would I do this?
	/*
		else {
			super.children();
		}
		*/

	}

}
