/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import org.w3c.dom.Element;

public class NodeTransformDescriptor extends MetaDescriptor
{
	protected Element element;
	protected String namespace;
	protected String name;
	protected String value;
	
	public NodeTransformDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	public final void setArgs(Element node, String name,String value)
	{
		if(name.indexOf(':') != - 1)
		{
			String[] bb  = name.split("[:]");
			namespace = bb[0];
			this.name = bb[1];
		}
		else
		{
			namespace = null;
			this.name = name;
		}
		
		this.element = node;
		this.value = normalizeWhitespace(value);
		
		// TODO.. tihs is pretty archaic..  does this really do anything anymore???
		onScan();
	}
	
	protected String normalizeWhitespace(String in)
	{
		return in.replaceAll("[ \t\r\n]+", " ");
	}
}
