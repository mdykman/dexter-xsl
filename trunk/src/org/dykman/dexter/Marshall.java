/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import java.util.Iterator;
import java.util.List;

import org.dykman.dexter.descriptor.NodeDescriptor;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.TransformDescriptor;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Marshall
{
	private static Marshall marshall = null;

	public static Marshall instance()
	{
		if (marshall == null)
		{
			marshall = new Marshall();
		}
		return marshall;
	}

	private Marshall()
	{
	}

	public static Descriptor marshall(Node node,Dexter dexter)
	{
//System.out.println("marshalling " + element.getNodeName());
		Descriptor parent = marshallNode(node,dexter);
		Descriptor c;
		NodeList children = node.getChildNodes();
		int nc = children.getLength();
		for (int i = 0; i < nc; ++i)
		{
			Node child = children.item(i);
			if(child != null &&	child.getParentNode() != null)
			{
				c = marshall(child,dexter);
				parent.appendChild(c);
			}
		}
		return parent;
	}

	public static Descriptor marshallNode(Node node,Dexter dexter)
	{
		Descriptor descriptor = new NodeDescriptor(node);
		List<NodeDecorator> list = (List<NodeDecorator>) node
		      .getUserData(DexterityConstants.DEXTER);
		if (list != null)
		{
			Iterator<NodeDecorator> it = list.iterator();
			TransformDescriptor td;
			while (it.hasNext())
			{
				NodeDecorator decorator = it.next();
				descriptor = td = decorator.decorate(descriptor);
				td.setDexter(dexter);
			}
		}
		return descriptor;
	}

//	public void runDescriptor(Descriptor descriptor, TransformSequencer sequencer)
//	{
//		descriptor.setTransformSequencer(sequencer);
//		descriptor.start();
//		descriptor.attributes();
//		Descriptor[] children = descriptor.getChildDescriptors();
//		for (int i = 0; i < children.length; ++i)
//		{
//			runDescriptor(children[i], sequencer);
//		}
//		descriptor.end();
//	}
}
