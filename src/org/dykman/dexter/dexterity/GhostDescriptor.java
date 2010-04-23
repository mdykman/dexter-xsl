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
	
	@Override
	public void start()
	{
		if(!applyToChildren) beforeNode();
//		inner.start();

	}

	@Override
	public void children()
	{
		if(applyToChildren)
		{
			beforeNode();
		}
		
		inner.children();
		if(applyToChildren)
		{
			afterNode();
		}

	}
	@Override 
	public void attributes()
	{
	}
	@Override 
	public void end()
	{
//		inner.end();
		if(!applyToChildren) afterNode();
	}
}
