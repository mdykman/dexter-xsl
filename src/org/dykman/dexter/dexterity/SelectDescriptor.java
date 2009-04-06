/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.CrossPathResolver;
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
		sequencer.startIterator(new CrossPathResolver(this),value);
		String path = mapPath(value);
//System.out.println("value in:" + value + " path out:" + path);		
		setPath(path);
		setIteratorContext(path);
//		String ic = getIteratorContext();
//		path = dequalify(ic, value);
//		String ic = getIteratorContext();
		
	}

	@Override
	public void afterNode()
	{
		sequencer.endIterator();
	}

}
