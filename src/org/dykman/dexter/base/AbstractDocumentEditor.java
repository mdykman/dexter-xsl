/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.Properties;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractDocumentEditor implements DocumentEditor
{
	protected Properties properties;
	protected Document document;
	protected Element element;
	protected Dexter dexter;
	protected String prefix;
	
	public AbstractDocumentEditor()
	{
	}
	public void setProperties(Properties properties)
	{
		this.properties = properties;
		prefix = properties.getProperty(DexterityConstants.PREFIX);
	}
	protected String getDexterAttribute(Element element,String key)
	{
		return getDexterAttribute(element, key, true);
	}
	protected String getDexterAttribute(Element element,String key, boolean erase)
	{
		String result = null;
		String k = prefix + key; 
		if(element.hasAttribute(k))
		{
			result = element.getAttribute(k);
			if(erase)
			{
				element.removeAttribute(k);
			}
		}
		return result;
	}

	
	public  void setReference(Document document, Element element)
	{
		this.document = document;
		this.element = element;
	}
	public void setDexter(Dexter dexter)
   {
   	this.dexter = dexter;
   }

}
