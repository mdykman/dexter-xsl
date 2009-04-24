/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.BlockDescriptor;
import org.dykman.dexter.descriptor.CrossPathResolver;
import org.dykman.dexter.descriptor.PathDescriptor;

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
			CrossPathResolver resolver = new CrossPathResolver(this);
			if(values[i] == null || values[i].length() == 0)
			{
				sequencer.startCase(resolver,"");
			}
			else
			{
				StringBuilder buffer = new StringBuilder();
				String[] tests = values[i].split("[ |]");
				String path = getMeta(DexterityConstants.ITER_CONTEXT);
				
				if (path == null)
					path = "/";

				int p = 0;
				for (int j = 0; j < tests.length; ++j)
				{
					String t = tests[j];
					if(t.startsWith("!"))
					{
						buffer.append('!');
						t = t.substring(1);
					}
					buffer.append(t);
//					buffer.append(repath(t));
					char c = PathDescriptor.nextOf(values[j],p, new char[] { ' ' , '|' });
					if(c != 0) {
						buffer.append(c);
						p = values[j].indexOf(c, p)+1;
					}
				}
//				CrossPathResolver resolver = new CrossPathResolver(this);
				sequencer.startCase(resolver, buffer.toString());
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
