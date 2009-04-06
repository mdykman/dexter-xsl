/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import org.dykman.dexter.descriptor.CrossPathResolver;
import org.dykman.dexter.descriptor.Descriptor;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface TransformSequencer
{
	public void setDocType(DocumentType dt);

	public void runDescriptor(Descriptor descriptor);
	public Node getCurrentNode();
	public Node getTextExpression(String key, String value);
	public String translateXSLPath(CrossPathResolver resolver,String p);
	public void setIdNames(java.util.List<String> names);
	public Element textContainer(String content);

	public void startNode(String name, int type);
	public void endNode();

	public void mapAttribute(
			CrossPathResolver resolver,
			String name,
			String[] path, 
			String def,
			boolean force);
	public void mapNode(
			CrossPathResolver resolver, 
			String []path, 
			String def,
			boolean disableEscaping, 
			boolean force);
	/**
     * @deprecated Use {@link #copyNodes(String,String,boolean)} instead
     */
    public void copyNodes(CrossPathResolver resolver,String path, String def);

	public void copyNodes(
			CrossPathResolver resolver,
			String path, 
			String def, 
			boolean children);
	
	public void startSubdoc(CrossPathResolver resolver,String altDoc,String name,String match,boolean keep);
	public void endSubdoc();
	
	public void setAttribute(String key,String value);
	public void setIdentityAttribute(String key,String value);

	public void setMeta(String key,String value);
	public String getMeta(String key);

	public void startIterator(CrossPathResolver resolver,String path);
	public void endIterator();

	public void startCaseBlock();
	public void endCaseBlock();

	public void startCase(CrossPathResolver resolver,String tests);
	public void endCase();

	public void startTest(CrossPathResolver resolver,String tests);
	public void endTest();
}
