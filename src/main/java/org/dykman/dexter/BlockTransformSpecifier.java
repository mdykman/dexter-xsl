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

public class BlockTransformSpecifier extends TransformSpecifier
{
	protected String namespace;
	protected Element[] block;
	protected String[] names;
	protected String[] values;
	
	public BlockTransformSpecifier(String fqn) throws Exception
	{
		super(fqn);
	}

	public BlockTransformSpecifier(Class<TransformDescriptor> klass)
	{
		super((Class<TransformDescriptor>)klass);
	}
	
	public void setArgs(String namespace, Element[]block,String[]names,String[]values)
	{
		this.namespace = namespace;
		this.block = block;
		this.names = names;
		this.values = values;
	}
	public TransformDescriptor enclose(Descriptor descriptor)
	{
		try
		{
//System.out.println("constructor for " + klass.getName());			
			Constructor cc = klass.getConstructor();
			BlockDescriptor td = (BlockDescriptor)cc.newInstance();
			Descriptor[] cd = new Descriptor[block.length];
			for(int i = 0 ; i < block.length; ++i)
			{
				cd[i] = dexter.marshall(block[i]);
			}
			td.setPropertyResolver(properties);
			td.setArgs(block, cd,names,values);
			return td;
		}
		catch (Exception e)
		{
			throw new DexterException(e);
		}
	}
}
