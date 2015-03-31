/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.didi;

import org.w3c.dom.Element;

public class AnchorsDesc
{
	private String id;
	private Element element; 
	private String params;
	
	public AnchorsDesc(String id,Element element, String params)
	{
		this.id = id;
		this.element = element;
		this.params = params;
	}
	public Element getElement()
   {
   	return element;
   }
	public String getId()
   {
   	return id;
   }
	public String getParams()
   {
   	return params;
   }
	
}
