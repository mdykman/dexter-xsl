/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import java.util.Iterator;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.DexterException;
import org.dykman.dexter.base.TransformSequencer;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeDescriptor extends AbstractDescriptor
{
	protected Node node;
	TransformSequencer sequencer;
	boolean taint;
	public void setTransformSequencer(TransformSequencer sequencer)
	{
		this.sequencer = sequencer;
	}

	public NodeDescriptor(Node node)
	{
		this.node = node;
		taint = node.getUserData(Dexter.DEXTER_TAINT) != null;
		
System.out.println("ELEMENT: " + (taint ? "TAINTED" : "UNTAINED") + " " + node.getNodeName());		
	}
	public void attributes()
	{
		NamedNodeMap attr = node.getAttributes();
		if(attr != null)
		{
			int n = attr.getLength();
			for(int i = 0; i < n ;++i)
			{
				Node a = attr.item(i);
				sequencer.setAttribute(
						a.getNodeName(),a.getNodeValue());
			}
		}
	}

	public void children()
	{
		Iterator<Descriptor> it = children.iterator();
		while(it.hasNext())
		{
			Descriptor d = it.next();
			sequencer.runDescriptor(d);
		}
	}

	public void end()
	{
		int type = node.getNodeType();
		switch(type)
		{
			case Node.ELEMENT_NODE:
			case Node.TEXT_NODE:
			case Node.ENTITY_NODE:
			case Node.ENTITY_REFERENCE_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			case Node.DOCUMENT_NODE:
				sequencer.endNode();
			break;
		}

//		sequencer.endNode();
	}

	public void start()
	{
		int type = node.getNodeType();
		switch(type)
		{
			case Node.ELEMENT_NODE:
				sequencer.startNode(node.getNodeName(),node.getNodeType());
			break;
			case Node.TEXT_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			case Node.DOCUMENT_NODE:
				sequencer.startNode(node.getNodeValue(),node.getNodeType());
			break;
			case Node.ENTITY_NODE:
			case Node.ENTITY_REFERENCE_NODE:
				sequencer.startNode(node.getNodeName(),node.getNodeType());
			break;
			case Node.DOCUMENT_TYPE_NODE:
				{
						sequencer.setDocType((DocumentType)node);
				}
			break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				{
System.err.println("WARNING: discarding processing instruction in the input");					
				}
			break;
			case Node.NOTATION_NODE:
			{
	
System.err.println("WARNING: discarding notation node in the input");					
			}
			break;
			default :
			{
				Dexter.reportInternalError("unhandled node type in NodeDescriptor: " + node.getNodeType(), null);
				throw new DexterException("unhandled node type: " + node.getNodeType());
			}
		}
	}

}
