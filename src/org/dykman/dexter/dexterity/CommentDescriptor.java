package org.dykman.dexter.dexterity;

import org.dykman.dexter.base.PathEval;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Element;

public class CommentDescriptor extends PathDescriptor
{
	public CommentDescriptor(Descriptor descriptor) {
		super(descriptor);
	}
	@Override
	public void start() {
		sequencer.appendText("<!--\n", true);
	}
	@Override
	public void end() {
		sequencer.appendText("\n -->", true);
	}
	@Override
	public void children() {
		
		boolean useDefault = false;
		if(value.startsWith("!")) {
			value = value.substring(1);
			useDefault = true;
		}
		sequencer.copyNodes(PathEval.parseSingle(value), 
				useDefault ? element.getTextContent() : null, true);
		// WHY would I do this?
	/*
		else {
			super.children();
		}
		*/

	}

}
