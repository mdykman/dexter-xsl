/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeTransformDescriptor;

public class GhostDescriptor extends NodeTransformDescriptor
{
	public GhostDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	// intercept everything except the children
	@Override
	public void start()
	{
		if(!applyToChildren) beforeNode();
	}
	@Override 
	public void attributes()
	{
	}
	@Override 
	public void end()
	{
		if(!applyToChildren) afterNode();
	}
}
