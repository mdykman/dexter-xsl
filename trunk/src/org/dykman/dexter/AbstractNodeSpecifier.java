/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class AbstractNodeSpecifier implements NodeSpecifier
{
	protected String value;
	protected String name;
	protected Element node;
	public void setArg(Element node, String name,String value)
	{
		this.node = node;
		this.name = name;
		this.value = value;
	}
	public String getName()
   {
   	return name;
   }
	public Node getNode()
   {
   	return node;
   }
	public String getValue()
   {
   	return value;
   }

}
