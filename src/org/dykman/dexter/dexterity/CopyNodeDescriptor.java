/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Element;

public class CopyNodeDescriptor extends PathDescriptor
{
	public CopyNodeDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	@Override
	public void children()
	{
		boolean useDefault = false;
		if(value.startsWith("!"))
		{
			value = value.substring(1);
			useDefault = true;
		}
		
		String nref = mapPath(getPath(),value);
//		String p = getIteratorContext();
		sequencer.copyNodes(dequalify(getIteratorContext(), nref), 
				useDefault ? ((Element)element).getTextContent() : null, false);
	}
}