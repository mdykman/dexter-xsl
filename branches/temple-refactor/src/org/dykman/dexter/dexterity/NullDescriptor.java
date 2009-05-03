/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeTransformDescriptor;

public class NullDescriptor extends NodeTransformDescriptor
{
	public NullDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	@Override
	public void start()
	{
	}
	@Override 
	public void attributes()
	{
	}
	@Override 
	public void children()
	{
	}
	@Override 
	public void end()
	{
	}
}
