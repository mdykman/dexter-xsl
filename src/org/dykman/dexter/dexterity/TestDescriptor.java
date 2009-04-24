/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.CrossPathResolver;
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
		StringBuilder buffer = new StringBuilder();
		String[] tests = value.split("[ |]");
		String path = getMeta(DexterityConstants.ITER_CONTEXT);
// TODO  do I need this?		
		if (path == null)
			path = "/";
		int p = 0;
		for (int j = 0; j < tests.length; ++j) {
			String t = tests[j];
			if(t.startsWith("!")) {
				buffer.append('!');
				t = t.substring(1);
			}
//			t = repath(t);
			buffer.append(t);
			char c = nextOf(value,p, new char[] { ' ' , '|' });
			if(c != 0) {
				buffer.append(c);
				p = value.indexOf(c, p)+1;
			}
		}
		CrossPathResolver resolver = new CrossPathResolver(this);
		this.sequencer.startTest(resolver,value);
//		this.sequencer.startTest(buffer.toString());
	}

	@Override
	public void afterNode()
	{
		this.sequencer.endTest();
	}
}
