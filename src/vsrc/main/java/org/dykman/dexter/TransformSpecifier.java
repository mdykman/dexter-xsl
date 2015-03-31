/**
 * copyright 2007, 2008 Michael Dykman
 * user is granted unlimited use under the Artistic License
 * http://www.opensource.org/licenses/artistic-license.php
 */

package org.dykman.dexter;

import java.lang.reflect.Constructor;

import org.dykman.dexter.base.PropertyResolver;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeTransformDescriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;

public class TransformSpecifier extends AbstractNodeSpecifier
{
	protected Class<TransformDescriptor> klass;
	protected Dexter dexter;
	protected PropertyResolver properties = null;

	@SuppressWarnings("unchecked")
	public TransformSpecifier(String fqn)
		throws Exception
	{
		this((Class<TransformDescriptor>)Class.forName(fqn));
	}
	public TransformSpecifier(Class<TransformDescriptor> klass)
	{
		this.klass = klass;
	}
	

	@SuppressWarnings("rawtypes")
	public Class getDescriptorClass()
	{
		return klass;
	}
	@SuppressWarnings("unchecked")
	public void setDescriptorClass(@SuppressWarnings("rawtypes") Class klass)
	{
		this.klass = klass;
	}

	@SuppressWarnings("rawtypes")
	public TransformDescriptor enclose(Descriptor descriptor)
	{
		try
		{
			Class[] parameterTypes = new Class[]
				{ Descriptor.class };
			Constructor cc 
				= klass
			      .getConstructor(parameterTypes);
			NodeTransformDescriptor td = (NodeTransformDescriptor) cc.newInstance(new Object[] {descriptor});
			td.setPropertyResolver(properties);
			td.setArgs(node, name, value);
//			td.setDexter(dexter);
			return td;
		}
		catch (Exception e)
		{
			throw new DexterException("error instantiating descriptor",e);
		}
	}
	public void setPropertyResolver(PropertyResolver properties)
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
