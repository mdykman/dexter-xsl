/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.dykman.dexter.base.TransformSequencer;



public abstract class AbstractDescriptor implements Descriptor
{
	protected List<Descriptor> children;
	protected TransformSequencer sequencer;
	
	public AbstractDescriptor()
	{
		children = new ArrayList<Descriptor>();
	}
	public Descriptor[] getChildDescriptors()
	{
		return children.toArray(new Descriptor[children.size()]);
	}
	
	public void appendChild(Descriptor child)
	{
		children.add(child);
	}
	public void setTransformSequencer(TransformSequencer sequencer)
	{
		this.sequencer = sequencer;
	}
	
}
