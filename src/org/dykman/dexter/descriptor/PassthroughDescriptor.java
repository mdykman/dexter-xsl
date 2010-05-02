package org.dykman.dexter.descriptor;

import org.w3c.dom.Node;

public class PassthroughDescriptor extends AbstractDescriptor
{
	private Node  node;
	public PassthroughDescriptor(Node node) {
		this.node = node;
	}
	public void attributes()
	{
		// TODO Auto-generated method stub
	}

	public void children()
	{
		sequencer.cloneNode(node);
	}

	public void end()
	{
		// TODO Auto-generated method stub
	}

	public void start()
	{
		// TODO Auto-generated method stub
	}

}
