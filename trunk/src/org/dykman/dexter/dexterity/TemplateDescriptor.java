package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class TemplateDescriptor extends PathDescriptor
{
    public TemplateDescriptor(Descriptor descriptor)
    {
            super(descriptor);
    }
	@Override
	public void beforeNode() {
		sequencer.callNamedTemplate(value);
		sequencer.startNamedTemplate(value);
		super.beforeNode();
	}

	@Override
	public void afterNode() {
		super.afterNode();
		sequencer.endNamedTemplate();
	}

}
