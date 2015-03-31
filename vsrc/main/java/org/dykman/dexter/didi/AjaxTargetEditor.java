/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.didi;

import org.w3c.dom.Element;

public class AjaxTargetEditor extends AjaxEditor
{
	static int counter = 1;

	public void edit(String namespace, String name, String value)
	{
		Element element = document.getElementById(value);
		
		String aparams = (String)element.getUserData(PARAMS);
		String a = namespace + ':'  +  "anchorparams";
		element.setAttribute(a, aparams);
	}

}
