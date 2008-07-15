/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.didi;

import org.dykman.dexter.DexteritySyntaxException;


public class AjaxAnchorEditor extends AjaxEditor
{

	public void edit(String namespace, String name, String value)
	{
		if (!element.hasAttribute("id"))
		{
			throw new DexteritySyntaxException("ajax target must have an `id' attribute");
		}
		String id = element.getAttribute("id");
		
		String pre = getDexterAttribute(element, "pre");
		String post = getDexterAttribute(element, "post");
		String params = getDexterAttribute(element, "params");
		if(params != null)
		{
			element.setUserData(PARAMS, params,null);
		}

		boolean applyToChildren = false;
		String method;
		
		if(value.startsWith("#"))
		{
			applyToChildren = true;
			value = value.substring(1);
		}
		
		if(value.charAt(0) == '!')
		{
			method="post";
			value = value.substring(1);
		}
		else if(value.charAt(1) == '!')
		{
			switch(value.charAt(0))
			{
				case 'P' :
					method = "put";
				break;
				case 'H' :
					method = "head";
				break;
				case 'D' :
					method = "delete";
				break;
				case 'O' :
					method = "options";
				break;
				default :
						throw new DexteritySyntaxException("illegal method specifier");
			}
			value = value.substring(2);
		}
		else
		{
			method = "get";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("   didi_ajax.handler_").append(id)
			.append(" = didi_ajax.create_handler(\n      '")
			.append(value).append("','")
			.append(method).append("',")
			.append(pre == null ? "null" : pre).append(",")
			.append(post == null ? "null" : post).append(",")
			.append(applyToChildren ? "true" : " false" ).append(");\n");
		writeScript(sb.toString());
	}
}
