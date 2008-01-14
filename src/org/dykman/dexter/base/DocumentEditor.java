/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.Properties;

import org.dykman.dexter.Dexter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface DocumentEditor
{
	public  void setReference(Document document, Element element);
	public void edit(String name, String value);
	public void setProperties(Properties properties);
	public void setDexter(Dexter dexter);
}
