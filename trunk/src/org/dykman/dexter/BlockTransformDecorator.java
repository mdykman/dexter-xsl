/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import java.lang.reflect.Constructor;

import org.dykman.dexter.descriptor.BlockDescriptor;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;
import org.w3c.dom.Element;

public class BlockTransformDecorator extends TransformDecorator
{
	protected Element[] block;
	protected String[] names;
	protected String[] values;
	
	public BlockTransformDecorator(String fqn) throws Exception
	{
		super(fqn);
	}

	public BlockTransformDecorator(Class<TransformDescriptor> klass)
	{
		super((Class<TransformDescriptor>)klass);
	}
	
	public void setArgs(Element[]block,String[]names,String[]values)
	{
		this.block = block;
		this.names = names;
		this.values = values;
	}
	public TransformDescriptor decorate(Descriptor descriptor)
	{
		try
		{
//System.out.println("constructor for " + klass.getName());			
			Constructor cc = klass.getConstructor();
			BlockDescriptor td = (BlockDescriptor)cc.newInstance();
			Descriptor[] cd = new Descriptor[block.length];
			for(int i = 0 ; i < block.length; ++i)
			{
				cd[i] = Marshall.marshall(block[i],dexter);
			}
			td.setProperties(properties);
			td.setArgs(block, cd,names,values);
			return td;
		}
		catch (Exception e)
		{
			throw new DexterException(e);
		}
	}
}
