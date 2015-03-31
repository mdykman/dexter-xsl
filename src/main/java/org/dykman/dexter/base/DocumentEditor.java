/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import org.dykman.dexter.Dexter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface DocumentEditor
{
	public  void setReference(Document document, Element element);
	public void edit(String namespace, String name, String value);
	public void setPropertyResolver(PropertyResolver properties);
	public void setDexter(Dexter dexter);
}
