/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import org.dykman.dexter.base.PropertyResolver;
import org.dykman.dexter.base.TransformSequencer;

public interface Descriptor
{
	public void setTransformSequencer(TransformSequencer sequencer);

	public Descriptor[] getChildDescriptors();
	public void appendChild(Descriptor child);
	public void setPropertyResolver(PropertyResolver properties);

	public void start();
	public void attributes();
	public void children();
	public void end();

}
