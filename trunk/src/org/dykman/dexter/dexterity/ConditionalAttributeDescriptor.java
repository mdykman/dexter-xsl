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
		String path = getMeta(DexterityConstants.ITER_CONTEXT);
		// TODO  do I need this?		
		if (path == null) path = "/";

		int ntests = tests.length;
		for(int i = 0; i < ntests; i+=2)
		{
			String [] nv = tests[i].split(DexterityConstants.ATTR_SEP,2);
			String name = nv[0];
			String test = nv[1];
			String exp = tests[i+1];

			String value = null;
			Node a = attr.getNamedItem(name);
			if(a != null)
			{
				value = a.getNodeValue();
				attr.removeNamedItem(name);
			}
			StringBuffer buffer = new StringBuffer();
			String[] subx = test.split("[ |]");

			int p = 0;
			for (int j = 0; j < subx.length; ++j)
			{
				String t = subx[j];
				if(t.startsWith("!"))
				{
					buffer.append('!');
					t = t.substring(1);
				}
				t = mapPath(t);
				buffer.append(dequalify(path, t));
				char c = nextOf(test,p, new char[] { ' ' , '|' });
				if(c != 0)
				{
					buffer.append(c);
					p = value.indexOf(c, p)+1;
				}
			}
			
			this.sequencer.startTest(buffer.toString());			
			
			
			if(exp.startsWith("!")) exp = exp.substring(1);
			else  value = null; 

			sequencer.mapAttribute(name, attributeTemplate(exp),  value);
			this.sequencer.endTest();			
		}
		
		super.attributes();
	}

}
