/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class EnvDescriptor extends PathDescriptor
{
	public EnvDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}

	@Override
	public void start()
	{
		sequencer.startSelect(null,value,true);
		super.start();
	}

	@Override
	public void end()
	{
		super.end();
		sequencer.endSelect();
	}

}
