/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.BlockDescriptor;

public class CaseDescriptor extends BlockDescriptor
{

	@Override
	public void start()
	{
		this.sequencer.startCaseBlock();
	}

	@Override
	public void children()
	{
		for (int i = 0; i < block.length; ++i)
		{
			if(values[i] == null || values[i].length() == 0)
			{
				sequencer.startCase(null);
			}
			else
			{
				sequencer.startCase(values[i]);
			}
			sequencer.runDescriptor(descriptors[i]);
			this.sequencer.endCase();
		}
	}

	@Override
	public void end()
	{
		this.sequencer.endCaseBlock();
	}
}
