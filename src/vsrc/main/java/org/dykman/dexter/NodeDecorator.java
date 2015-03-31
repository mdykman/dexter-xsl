/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;
import org.w3c.dom.Element;

// DeXTer
// DEcorative Xml Tranform EmitteR

public interface NodeDecorator
{
	TransformDescriptor decorate(Descriptor descriptor);
	/**
	 * This is called by the Dexter during scanNode() immediately after the NodeDecorator is constructed. 
	 *  It is possible at this point to read parent attributes or modify their Decorator chain. It is axiomatic that 
	 *  parent nodes have been already scanned. No child nodes have been scanned and may therefore be modified 
	 *  affecting the scan
	 *  
	 * @param element the source element upon which the attribute has been detected
	 * @param name the nmae of the attribute
	 * @param value the value of the attribute 
	 */
	public void setArg(Element node, String name, String value);
}
//list:Decorator
