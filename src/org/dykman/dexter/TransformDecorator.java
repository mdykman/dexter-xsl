/**
 * copyright 2007, 2008 Michael Dykman
 * user is granted unlimited use under the Artistic License
 * http://www.opensource.org/licenses/artistic-license.php
 */

package org.dykman.dexter;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeTransformDescriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;

public class TransformDecorator extends AbstractNodeDecorator
{
	protected Class<TransformDescriptor> klass;
	protected Dexter dexter;
	protected Properties properties = null;

	public TransformDecorator(String fqn)
		throws Exception
	{
		this((Class<TransformDescriptor>)Class.forName(fqn));
	}
	public TransformDecorator(Class<TransformDescriptor> klass)
	{
		this.klass = klass;
	}
	

	public Class getDescriptorClass()
	{
		return klass;
	}
	public void setDescriptorClass(Class klass)
	{
		this.klass = klass;
	}

	public TransformDescriptor decorate(Descriptor descriptor)
	{
		try
		{
			Class[] parameterTypes = new Class[]
				{ Descriptor.class };
			Constructor cc 
				= klass
			      .getConstructor(parameterTypes);
			NodeTransformDescriptor td = (NodeTransformDescriptor) cc.newInstance(new Object[] {descriptor});
			td.setProperties(properties);
			td.setArgs(node, name, value);
			td.setDexter(dexter);
			return td;
		}
		catch (Exception e)
		{
			throw new DexterException("error instantiating descriptor",e);
		}
	}
	public void setProperties(Properties properties)
   {
   	this.properties = properties;
   }
	public Dexter getDexter()
   {
   	return dexter;
   }
	public void setDexter(Dexter dexter)
   {
   	this.dexter = dexter;
   }

}
