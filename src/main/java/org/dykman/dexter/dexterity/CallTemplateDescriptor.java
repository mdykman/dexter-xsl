package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class CallTemplateDescriptor extends PathDescriptor
{
    public CallTemplateDescriptor(Descriptor descriptor)
    {
            super(descriptor);
    }
	@Override
	public void beforeNode() {
	}

	@Override
	public void afterNode() {
	}

	@Override
	public void start() {
	}

	@Override
	public void end() {
	}

	@Override
	public void attributes() {
	}

	@Override
	public void children() {
		sequencer.callNamedTemplate(value);
	}

}
