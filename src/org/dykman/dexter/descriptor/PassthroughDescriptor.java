package org.dykman.dexter.descriptor;

import org.w3c.dom.Node;

public class PassthroughDescriptor extends AbstractDescriptor
{
	private Node  node;
	public PassthroughDescriptor(Node node) {
		this.node = node;
	}

	public void start() { }

	public void attributes() { } 

	public void children() {
//	System.out.println("pass " + node.getNodeType());
		sequencer.cloneNode(node);
	}

	public void end() { }


}
