/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;


public class NoopDescriptor extends PathDescriptor
{
	public NoopDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
}
