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
		// push an attribute querier into  
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
		String path = mapPath(value);
		String ic = getIteratorContext();
		setPath(path);
		setIteratorContext(path);
		path = dequalify(ic, value);
		sequencer.startSubdoc(altDoc,id, path,true);
	}

	@Override
	public void afterNode()
	{
		sequencer.endSubdoc();
	}

}
