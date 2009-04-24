package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.CrossPathResolver;
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

//System.out.println("cattr arg:" + value);		
		int ntests = tests.length;
		for(int i = 0; i < ntests; i+=2)
		{
			String [] nv = tests[i].split(DexterityConstants.ATTR_SEP,2);
			String name = nv[0];
			String exp = nv[1];
			String test = tests[i+1];
//System.out.println("cattr exp name=" + name + ", exp=" + exp + ", test=" + test);		

			String value = null;
			Node a = attr.getNamedItem(name);
			if(a != null)
			{
				value = a.getNodeValue();
				attr.removeNamedItem(name);
			}
			StringBuilder buffer = new StringBuilder();
			String[] subx = test.split("[ |]");

			int p = 0;
			for (int j = 0; j < subx.length; ++j)
			{
				String t = subx[j];
				buffer.append(t);
				char c = nextOf(test,p, new char[] { ' ' , '|' });
				if(c != 0)
				{
					buffer.append(c);
					p = value.indexOf(c, p)+1;
				}
			}
			CrossPathResolver resolver = new CrossPathResolver(this);
			this.sequencer.startTest(resolver,buffer.toString());			
			
			boolean force = false;
			boolean disable_escape = false;
			
			if(exp.startsWith("!")) {
				exp = exp.substring(1);
				if(exp.startsWith("!")) {
					force = true;
					exp = exp.substring(1);
				}
			}
			else  value = null; 

			buffer.append(exp);

			sequencer.mapAttribute(resolver, name, 
					valueTemplateParams(exp),  value,force,disable_escape);
			
			this.sequencer.endTest();			
		}
		
		super.attributes();
	}

}
