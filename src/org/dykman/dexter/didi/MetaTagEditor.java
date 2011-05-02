/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.didi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.dykman.dexter.DexterException;
import org.dykman.dexter.base.AbstractDocumentEditor;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MetaTagEditor extends AbstractDocumentEditor
{

	public MetaTagEditor()
	{
	}

	public void edit(String namespace, String name, String value)
	{
		try
		{
			DocumentFragment frag = document.createDocumentFragment();
			InputStream in = new FileInputStream(value);
			Properties props = new Properties();
			props.load(in);
			in.close();
			Iterator it = props.keySet().iterator();
			Element el; 
			while(it.hasNext())
			{
				String k = (String) it.next();
				String v = props.getProperty(k);
				el = document.createElement("meta");
				el.setAttribute("name", k);
				el.setAttribute("content", v);
				frag.appendChild(el);
			}
			Node parent = element.getParentNode();
			parent.replaceChild(frag, element);
		}
		catch(IOException e)
		{
			throw new DexterException(e);
		}
	}

}
