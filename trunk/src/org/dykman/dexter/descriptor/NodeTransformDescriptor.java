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
	protected String value;
	protected String name;
	
	public NodeTransformDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	public final void setArgs(Element node, String name,String value)
	{
		this.element = node;
		this.name = name;
		this.value = normalizeWhitespace(value);
		
		onScan();
	}
	
	protected String normalizeWhitespace(String in)
	{
		return in.replaceAll("[ \t\r\n]+", " ");
	}
}
