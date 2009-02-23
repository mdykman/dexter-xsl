package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;

public class ConditionalAttributeDescriptorS extends
        ConditionalAttributeDescriptor
{

	public ConditionalAttributeDescriptorS(Descriptor descriptor) {
		super(descriptor);
	}

	@Override
	protected String[] getTests() {
		if(value.length() > 0 ) {
			StringBuffer buffer = new StringBuffer();
			buffer.append('[').append(value.substring(0, 1)).append(']');
			return value.substring(1).split(buffer.toString());
		}
		else {
			return  new String[0];
		}
	}
}
