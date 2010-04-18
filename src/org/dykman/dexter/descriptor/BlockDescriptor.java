/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import org.dykman.dexter.base.PropertyResolver;
import org.w3c.dom.Element;

public abstract class BlockDescriptor extends PathDescriptor
{
	protected Element[] block;
	protected String[] names;
	protected String[] values;
	protected Descriptor[] descriptors;
	protected PropertyResolver properties = null;
	
	public BlockDescriptor()
	{
		super(null);
	}
	public void setPropertyResolver(PropertyResolver properties)
   {
   	this.properties = properties;
   }


	public void setArgs(
			Element[] block,
			Descriptor[] descriptors, 
			String[] names,
			String[] values)
	{
		this.block = block;
		this.descriptors = descriptors;
		this.names = names;
		this.values = values;
		
//		onScan();
	}
	public void attributes()
	{
	}

	public void end()
	{
	}

	public void start()
	{
	}

	@Override
	public void appendChild(Descriptor child)
	{
		this.children.add(child);
	}
}
