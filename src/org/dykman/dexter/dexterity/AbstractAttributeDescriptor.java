package org.dykman.dexter.dexterity;

import java.util.ArrayList;
import java.util.List;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class AbstractAttributeDescriptor extends PathDescriptor
{
	public AbstractAttributeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	protected String[] attributeTemplate(String value)
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
