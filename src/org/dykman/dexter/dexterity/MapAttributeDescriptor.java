/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MapAttributeDescriptor extends AbstractAttributeDescriptor
{
	public MapAttributeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
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
			String nref = (nv.length > 1) ? nv[1] : "";
			
			
			if(nref.startsWith("!"))
			{
				nref = nref.substring(1);
				if(nref.startsWith("!"))
				{
//					force = true;
					nref = nref.substring(1);
				}
			}
			else
			{
				value = null;
			}

			sequencer.mapAttribute(
					nv[0], valueTemplateParams(nref),  value);
		}
		
		super.attributes();
	}
	
}
