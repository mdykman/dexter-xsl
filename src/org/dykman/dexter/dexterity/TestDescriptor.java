/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class TestDescriptor extends PathDescriptor
{

	public TestDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}

	@Override
	public void beforeNode()
	{
		this.sequencer.startTest(value);
	}

	@Override
	public void afterNode()
	{
		this.sequencer.endTest();
	}
}
