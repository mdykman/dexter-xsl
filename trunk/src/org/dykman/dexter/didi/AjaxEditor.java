/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.didi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.dykman.dexter.NodeNavigator;
import org.dykman.dexter.base.AbstractDocumentEditor;
import org.dykman.dexter.dexterity.DexteritySyntaxException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class AjaxEditor extends AbstractDocumentEditor
{
	public static final String PARAMS = "DIDI_PARAMS";
//	static Element scratch = null;
//	static StringBuffer sb = new StringBuffer();

	public Element getScriptNode()
	{
		return getScriptNode(document);
	}
	public static void appendScript()
	{
	}
	public static Element getScriptNode(Document document)
	{
		Element script = (Element)	document.getUserData("DIDI_AJAX_SCRIPT_PAD");
		if (script == null)
		{
			script = document.createElement("script");
			script.setAttribute("src", "didi-ajax.js");
			script.setAttribute("language", "JavaScript");
			script.setAttribute("type", "text/javascript");
			
			Node n = NodeNavigator.firstNodeResolver(document, new String[]
				{ "html", "head" });
			n.appendChild(script);

			script = document.createElement("script");
			script.setAttribute("language", "JavaScript");
			script.setAttribute("type", "text/javascript");
			n = NodeNavigator.firstNodeResolver(document, new String[]
				{ "html", "body" });
			n.appendChild(script);

			document.setUserData("DIDI_AJAX_SCRIPT_PAD",script,null);

//			String notice = 
//				"\n// this node is inserted to workaround an apparent bug in Mozilla\n" + 
//				"// which fails to execute a script element where it is the \n" + 
//				"// first element in the body  and there only one script element\n" + 
//				"// in the head.  the next block seems to execute just fine.\n";
//			
//			Element hack = document.createElement("script");
//			hack.appendChild(document.createCDATASection(notice));
//			hack.setAttribute("language", "JavaScript");
//			hack.setAttribute("type", "text/javascript");
//			n.insertBefore(hack,script);
		}
		return script;
	}

	protected String readStringResource(String name)
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = getClass().getResourceAsStream(name);
			byte[] bytes = new byte[4096];
			int len;
			while ((len = in.read(bytes)) > -1)
			{
				out.write(bytes, 0, len);
			}
			return out.toString();
		}
		catch (Exception e)
		{
			throw new DexteritySyntaxException(e);
		}
	}
	public void writeScript(String s)
	{
		writeScript(document, s);
	}

	public static void writeScript(Document document, String s)
	{
		Element script = getScriptNode(document);
		script.appendChild(document.createCDATASection(s));
		
	}

}
