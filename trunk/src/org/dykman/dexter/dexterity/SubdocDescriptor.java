/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.DexteritySyntaxException;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class SubdocDescriptor extends PathDescriptor
{
	private String id;

	public SubdocDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}


	@Override
	public void beforeNode()
	{
		id = element.getAttribute("id");
		if (id == null)
		{
			throw new DexteritySyntaxException("a subdoc element must have an 'id' attribute");
		}
		String altDoc = null;
		if(value.indexOf(':') > -1)
		{
			String bits[] = value.split(":",2);
			value = bits[0];
			altDoc = bits[1];
		}
//		String path = mapPath(value);
//		setPath(path);
		sequencer.startSubdoc(altDoc,id, value,true);
	}

	@Override
	public void afterNode()
	{
		sequencer.endSubdoc();
	}

}
