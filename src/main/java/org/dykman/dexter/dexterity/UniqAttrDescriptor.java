/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class UniqAttrDescriptor extends PathDescriptor
{

	public UniqAttrDescriptor(Descriptor descriptor)
	{
		super(descriptor);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void attributes()
	{
		if(!element.hasAttribute(value))
		{
System.err.println("WARNING: attribute specified  by " + name + " is not defined");
		}
		else
		{
			String v = element.getAttribute(value);
			element.removeAttribute(value);
			sequencer.setIdentityAttribute( value,v);
		}
		super.attributes();
	}

}
