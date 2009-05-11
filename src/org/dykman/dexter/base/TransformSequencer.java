/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import org.dykman.dexter.descriptor.Descriptor;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface TransformSequencer
{
	public void setDocType(DocumentType dt);

	public void runDescriptor(Descriptor descriptor);
	public Node getCurrentNode();
//	public Node getTextExpression(String key, String value);
//	public String translateXSLPath(String p);
	public void setIdNames(java.util.List<String> names);
	public Element textContainer(String content);

	public String randMode();
	public void startSelect(String name, String match, String mode);
	public void endSelect();
	public void startNode(String name, int type);
	public void endNode();

	public void mapAttribute(
			String name,
			String[] path, 
			String def,
			boolean force,
			boolean disable_escape);
	public void mapNode(
			String []path, 
			String def,
			boolean force,
			boolean disable_escape);

	public void copyNodes(
			String path, 
			String def, 
			boolean children);
	
     
	public void startSubdoc(String altDoc,String name,String match,boolean keep);
	public void endSubdoc();
	
	public void setAttribute(String key,String value);
	public void setIdentityAttribute(String key,String value);

	public void setMeta(String key,String value);
	public String getMeta(String key);

	public void startIterator(String path);
	public void endIterator();

	public void startCaseBlock();
	public void endCaseBlock();

	public void startCase(String tests);
	public void endCase();

	public void startTest(String tests);
	public void endTest();
	public void appendText(String s);
	public void appendText(String s,boolean escape) ;

}
