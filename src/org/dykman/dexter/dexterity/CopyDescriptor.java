/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class CopyDescriptor extends PathDescriptor
{
	boolean parent;
	public CopyDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	@Override
	public void children()
	{
		boolean useDefault = false;
		if(value.startsWith("!")) {
			value = value.substring(1);
			useDefault = true;
		}

		sequencer.copyNodes(value, 
			useDefault ? element.getTextContent() : null, true);
	}
}
