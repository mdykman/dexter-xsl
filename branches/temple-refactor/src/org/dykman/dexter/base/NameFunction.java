/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

public class NameFunction implements PathFunction
{

	public String apply(String path, String arg)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("local-name(").append(path).append(")");
		return sb.toString();	
	}

}
