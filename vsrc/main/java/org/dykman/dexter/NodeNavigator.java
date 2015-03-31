/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeNavigator
{
	private DocumentTraversal traversal = null;

	public NodeNavigator(DocumentTraversal traversal)
	{
		this.traversal = traversal;
	}

	public NodeIterator pathIterator(Node node, String[] path)
	{
		return pathIterator(traversal, node, path);

	}

	public static NodeIterator pathIterator(DocumentTraversal traversal,
	      Node node, String[] path)
	{
		return traversal.createNodeIterator(node, NodeFilter.FILTER_ACCEPT,
		      new PathNodeFilter(path), false);

	}

	public static Node firstNodeResolver(Node node, String[] path)
	{
		Node n = node;
		for (int i = 0; n != null && i < path.length; ++i)
		{
			if (path[i].equals("."))
			{
				// do nothing
			}
			else if (path[i].equals(".."))
			{
				n = n.getParentNode();
				if (n.getNodeType() != Node.ELEMENT_NODE)
				{
					n = null;
				}
			}
			else if (path[i].equals("*"))
			{
				Node r = null;
				String[] subPath = Arrays.copyOfRange(path, i + 1, path.length);
				NodeList children = n.getChildNodes();
				int nc = children.getLength();
				for (int j = 0; r == null && j < nc; ++j)
				{
					r = firstNodeResolver(children.item(j), subPath);
					if (r != null && r.getNodeType() != Node.ELEMENT_NODE)
					{
						r = null;
					}
				}
				return r;
			}
			else if (path[i].equals("**"))
			{
				if (i < path.length - 1)
				{
					String[] subPath = Arrays.copyOfRange(path, i + 1, path.length);
					Node sn = n;
					while (true)
					{
						Node r = firstNodeResolver(sn, subPath);
						if (r != null)
						{
							return r;
						}
						else
						{
							NodeList children = sn.getChildNodes();
							int len = children.getLength();
							for (int j = 0; j < len; ++j)
							{
								r = firstNodeResolver(children.item(j), path);
								if (r != null)
								{
									return r;
								}
							}
							return null;
						}
					}
				}
			}
			else
			{
				n = getFirstChildElementByName(n, path[i]);
			}
		}

		return n;
	}

	public static Node getFirstChildElementByName(Node node, String name)
	{
		NodeList children = node.getChildNodes();
		int length = children.getLength();
		Node result = null;
		for (int i = 0; i < length; ++i)
		{
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
			      && name.equals(n.getNodeName()))
			{
				result = n;
				break;
			}
		}
		return result;

	}

	public static void main(String[] args)
	{

		try
		{

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			// builder.
			InputStream data = NodeNavigator.class
			      .getResourceAsStream("nodenavigatortest.xml");
			Document impl = builder.parse(data);
			InputStream paths = NodeNavigator.class
			      .getResourceAsStream("paths.txt");
			BufferedReader reader = new BufferedReader(
			      new InputStreamReader(paths));

			Node mynode;
			String line;

			int result = 0;
			while ((line = reader.readLine()) != null)
			{
				line.trim();
				if (line.length() > 0)
				{
					String[] path = line.split("/");
					System.out.print("testing path `" + line + "': ");
					if ((mynode = NodeNavigator.firstNodeResolver(impl, path)) == null)
					{
						System.out.println("not found");
						result++;
					}
					else
					{
						System.out.println(" found!  name = " + mynode.getNodeName());
					}

					NodeIterator nit = pathIterator((DocumentTraversal) impl, impl,
					      path);
					int counter = 0;
					while (nit.nextNode() != null)
					{
						++counter;
					}
					System.out.println("found " + counter + " nodes for path "
					      + line);
				}
			}
			System.exit(result);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String toString(Node node)
	{
		StringBuilder buffer = new StringBuilder("Node: ");
		buffer.append(node.getNodeName() + " (" + node.getNodeType() + " ");
		NamedNodeMap attr = node.getAttributes();
		if (attr != null)
		{
			int n = attr.getLength();
			for (int i = 0; i < n; ++i)
			{
				Node a = attr.item(i);
				buffer.append(a.getNodeName()).append(":").append(a.getNodeValue())
				      .append(" ");
			}
		}

		return buffer.toString();
	}
}
