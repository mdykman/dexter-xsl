/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import java.util.ArrayList;
import java.util.List;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MapAttributeDescriptor extends PathDescriptor
{
	public MapAttributeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	protected String [] getTests() {
		return value.split(DexterityConstants.ARG_SEP);
	}

	@Override
	public void attributes()
	{
		String [] tests = getTests();
		NamedNodeMap  attr = element.getAttributes();
		for(int i = 0; i < tests.length; ++i)
		{
			String [] nv = tests[i].split(DexterityConstants.ATTR_SEP,2);
			Node a = attr.getNamedItem(nv[0]);
			String value = null;
			if(a != null)
			{
				value = a.getNodeValue();
				attr.removeNamedItem(nv[0]);
			}
			String nref = nv[1];
			if(nref.startsWith("!"))
			{
				nref = nref.substring(1);
			}
			else
			{
				value = null;
			}

			sequencer.mapAttribute(nv[0], attributeTemplate(nref),  value);
		}
		
		super.attributes();
	}
	
	private String[] attributeTemplate(String value)
	{
//System.out.println("template " + value);		
		List<String> list = new ArrayList<String>();
		if(value.length() == 0)
		{
			return new String[]{""};
		}
		
		if(value.startsWith("{"))
		{
			list.add("");
		}
		
		int pc = 0;
		if(value.indexOf('{') == -1)
		{
			String 	s =  mapPath(getPath(),value);
			list.add(dequalify(getIteratorContext(),s));
		}
		else while(pc < value.length())
		{
			if(value.charAt(pc) == '{')
			{
				int end = value.indexOf('}',pc);
				String s = value.substring(pc+1, end);
								
				s =  mapPath(getPath(),s);
				list.add(dequalify(getIteratorContext(),s));
				pc = end+1;
			}
			else
			{
				int end = value.indexOf('{', pc);
				if(end == -1)
				{
					list.add(value.substring(pc));
					pc = value.length();
				}
				else
				{
					list.add(value.substring(pc, end));
					pc = end;
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}
}
