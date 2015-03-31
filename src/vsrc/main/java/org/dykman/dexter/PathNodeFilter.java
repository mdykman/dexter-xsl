/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

class PathNodeFilter implements NodeFilter
{
	private String[] path;

	public PathNodeFilter(String[] path)
	{
		this.path = path;
	}

	public short acceptNode(Node node)
	{
		return acceptNode(node,this.path);
	}
	private short acceptNode(Node node, String [] path)
	{
		short result =(short) ((
				node.getNodeType() == Node.ELEMENT_NODE) ? 1  : 0);
		for (int i = path.length - 1; result != 0 && i >= 0; --i)
		{
			if (node == null)
			{
				return 0;
			}
			else
			{
				String el = path[i];

				if (el.equals(node.getNodeName()))
				{
					// literal match
					node = node.getParentNode();
				}
				else if (el.equals(".."))
				{
					// make a new filter of the remaining expression
//					NodeFilter nf = new PathNodeFilter(Arrays
//					      .copyOfRange(path, 0, i));
					String []sp = Arrays.copyOfRange(path, 0, i);
					NodeList children = node.getChildNodes();
					int cl = children.getLength();
					result = 0;
					// if any childe matches filter, return true
					for (int j = 0; result == 0 && j < cl; ++j)
					{
						result = acceptNode(children.item(j),sp);
					}
					return result;
				}
				else if (el.equals("**"))
				{
					// make a new filter of the remaining expression
//					NodeFilter nf = new PathNodeFilter(Arrays
//					      .copyOfRange(path, 0, i));
					String []sp = Arrays.copyOfRange(path, 0, i);
					result = 0;
					// if self or any parent matches filter, return true
					while (node != null && result == 0)
					{
						result = acceptNode(node,sp);
						node = node.getParentNode();
					}
					return result;
				}
				else if (el.equals("*"))
				{
					node = node.getParentNode(); // match anything, carry on
				}
				else if (el.equals("."))
				{
					// keep reference bound to current element
				}
				else
				{
					result = 0; // failed to match a element
				}
			}
		}
		return result;
	}

	public static void main(String[] args)
	{
		try
		{
			String[] path = null;
			if (args.length == 0)
			{
				System.out.println("please specify an input file");
				System.exit(1);
			}
			File f = new File(args[0]);
			if (!(f.exists() && f.canRead()))
			{
				System.out.println("no such file: " + f.getPath());
				System.exit(2);
			}
			if (args.length >= 2)
			{
				path = args[1].split("/");
			}
			else
			{
				System.out.println("please specify a element path");
				System.exit(3);
			}

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			// builder.
			Document impl = builder.parse(new FileInputStream(f));
			DocumentTraversal traversal = (DocumentTraversal) impl;

			NodeIterator nit = traversal.createNodeIterator(impl,
			      NodeFilter.FILTER_ACCEPT, new PathNodeFilter(path), false);

			int counter = 0;
			while (nit.nextNode() != null)
			{
				++counter;
			}
			System.out.println("found " + counter + " nodes for path " + args[1]);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
