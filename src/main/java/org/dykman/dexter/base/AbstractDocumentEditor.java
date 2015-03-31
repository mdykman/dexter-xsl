/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;


import org.dykman.dexter.Dexter;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class AbstractDocumentEditor implements DocumentEditor
{
	protected PropertyResolver properties;
	protected Document document;
	protected Element element;
	protected Dexter dexter;
	protected String namespace;
	protected String name;
	protected String value;
	
	public AbstractDocumentEditor()
	{
	}
	public void setPropertyResolver(PropertyResolver properties)
	{
		this.properties = properties;
	}
	protected String getDexterAttribute(Element element,String key)
	{
		return getDexterAttribute(element, key, true);
	}
	
	protected String [] splitArgs(String v) {
		return v.split(DexterityConstants.ARG_SEP);
	}


	protected String getDexterAttribute(Element element,String key, boolean erase)
	{
		String result = null;
		String k;
		if(namespace != null)
		{
			k = namespace + ':' + key; 
		}
		else
		{
			k = key;
		}
		
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
	protected boolean usesNamespace(NamedNodeMap attr, String[] ns)
    {
    		int len = attr.getLength();
    		for(int i = 0; i < len; ++i) {
    			Node a = attr.item(i);
    			for(String n : ns) {
    				if(a.getNodeName().startsWith(n + ":")) {
    					return true;
    				}
    			}
    		}
    		return false;
    	}

}
