/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class SelectDescriptor extends PathDescriptor
{

	public SelectDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	@Override
	public void beforeNode()
	{
		sequencer.startIterator(value);
		String path = mapPath(value);
		setPath(path);
		setIteratorContext(path);		

		String id = element.getAttribute("id");
		if(id != null && id.length() > 0) {
			id = id.replaceAll("[^0-9a-zA-Z_]","_");
			id = id.replaceAll("_[_]+","_");
			sequencer.setVariable(id,".");
			sequencer.setVariable(id + "index","position()");
		}
	}

	@Override
	public void afterNode()
	{
		sequencer.endIterator();
	}

}
