package org.dykman.dexter.dexterity;

import org.dykman.dexter.base.PathEval;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Node;

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
		Node def = useDefault ? getChildren(element) : null;

		if(value.length() > 0) {
			sequencer.copyNodes(PathEval.parseSingle(value),def, true);
		}	
		else {
			super.children();
		}
		

	}

}
