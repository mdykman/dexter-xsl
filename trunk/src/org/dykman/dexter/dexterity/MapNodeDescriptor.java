/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Element;

public class MapNodeDescriptor extends PathDescriptor
{
	public MapNodeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	@Override
	public void children()
	{
		boolean useDefault = false;
//		boolean force = false;
//		if(value.startsWith("~")) {
//			value = value.substring(1);
//			disableEscape = true;
//		}
		if(value.startsWith("!")) {
			value = value.substring(1);
			if(value.startsWith("!")) {
//				force = true;
				value = value.substring(1);
			}
			useDefault = true;
		}

//		if(value.startsWith("~")) {
//			value = value.substring(1);
//			disableEscape = true;
//		}
		sequencer.mapNode(valueTemplateParams(value), 
				useDefault ? element.getTextContent() : null);
	}
}
