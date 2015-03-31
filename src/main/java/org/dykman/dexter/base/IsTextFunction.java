/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

public class IsTextFunction implements PathFunction
{

	public String apply(String path, String arg)
	{
		StringBuilder sb = new StringBuilder(path);
		if(path.length() > 0 &&
			path.charAt(path.length() - 1) != '/')
		{
			sb.append("/");
		}
		sb.append("self::text()");
		return sb.toString();
	}
}
