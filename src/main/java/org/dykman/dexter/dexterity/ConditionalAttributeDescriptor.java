package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ConditionalAttributeDescriptor extends AbstractAttributeDescriptor
{
	public ConditionalAttributeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	public void attributes()
	{
		String [] tests = getTests();
		NamedNodeMap  attr = element.getAttributes();

		int ntests = tests.length;
		for(int i = 0; i < ntests; i+=2)
		{
			String [] nv = tests[i].split(DexterityConstants.ATTR_SEP,2);
			String name = nv[0];
			String exp = (nv.length > 1) ? nv[1] : "";
			if(exp.length() == 0) {
				exp="''";
			}
			
			String test = tests[i+1];

			String value = null;
			Node a = attr.getNamedItem(name);
			if(a != null)
			{
				value = a.getNodeValue();
				attr.removeNamedItem(name);
			}
			this.sequencer.startTest(test);			

			// no concept of default value. it appears or not dependent on conditional
			sequencer.mapAttribute(name, 
					valueTemplateParams(exp),  value);
			
			this.sequencer.endTest();			
		}
		
		super.attributes();
	}

}
