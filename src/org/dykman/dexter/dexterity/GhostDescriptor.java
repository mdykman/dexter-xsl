/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.dexterity;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.NodeTransformDescriptor;



public class GhostDescriptor extends NodeTransformDescriptor
{
	public GhostDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	
	@Override
	public void start()
	{
		if(!applyToChildren) beforeNode();
//		inner.start();

	}

	@Override
	public void children()
	{
		if(applyToChildren)
		{
			beforeNode();
		}
		
		inner.children();
		if(applyToChildren)
		{
			afterNode();
		}

	}
	@Override 
	public void attributes()
	{
		NamedNodeMap  attr = element.getAttributes();

// strip any remaining attributes so the defauolt descriptor will not try to 
// render them
      for(int i = 0; i < attr.getLength(); ++i)
      {
			Node an = attr.item(i);
			element.removeAttribute(an.getNodeName());

/*
         String [] nv = tests[i].split(DexterityConstants.ATTR_SEP,2);
         Node a = attr.getNamedItem(nv[0]);
         String value = null;
         if(a != null)
         {
            value = a.getNodeValue();
            attr.removeNamedItem(nv[0]);
         }
*/
		}
// let the other operators have thier attribute hooks
		super.attributes();

	}
	@Override 
	public void end()
	{
//		inner.end();
		if(!applyToChildren) afterNode();
	}
}
