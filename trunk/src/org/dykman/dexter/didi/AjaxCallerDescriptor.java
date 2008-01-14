/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */


package org.dykman.dexter.didi;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
// didi_ajax_handler_messages('', 
// {'action':'reload', 
// 'var':document.forms[''].field.value, },null, targetpostfetch); return false;

public class AjaxCallerDescriptor extends PathDescriptor
{

	public AjaxCallerDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}

	@Override
	public void attributes()
	{
		Map<String, String> paramMap = new TreeMap<String, String>();

		String attrName = getAttributeName(name);
		Node base = sequencer.getCurrentNode();
		Document document = base.getOwnerDocument();
		
		Element at = document.createElement("xsl:attribute");
		at.setAttribute("name", attrName);
		base.appendChild(at);

// remove it so the node descriptor does noy try to write the default value		
		if (element.hasAttribute(attrName))
			element.removeAttribute(attrName);

		String prefix = properties.getProperty(DexterityConstants.PREFIX);

		System.out.println("AJAX  target " + value);
		Element ajax = element.getOwnerDocument().getElementById(value);
		if(ajax == null) System.out.println("FAILED TO FIND " + value + " by id");
		String params  = null;
		if(ajax != null)
		{
			params = (String)ajax.getUserData(AjaxEditor.PARAMS);
		}
		
		if (params != null)
		{
			String[] pp = params.split(DexterityConstants.ARG_SEP);
			for (int i = 0; i < pp.length; ++i)
			{
				int n = pp[i].indexOf(":");
				if(n > -1)
				{
//System.out.println("ON INPUT:: source: " + pp[i] + " " + pp[i].substring(0,n)+"="+ pp[i].substring(n+1));					
					paramMap.put(pp[i].substring(0,n), pp[i].substring(n+1));
				}
				else
				{
					System.out.println("illegal argument formation");
				}
			}
		}
		params = getPrefixAttribute(prefix, "params");
		if (params != null)
		{
			String[] pp = params.split(DexterityConstants.ARG_SEP);
			for (int i = 0; i < pp.length; ++i)
			{
				int n = pp[i].indexOf(":");
				if(n > -1)
				{
					paramMap.put(pp[i].substring(0,n), pp[i].substring(n+1));
				}
				else
				{
					System.out.println("illegal argument formation");
				}
			}
		}
		// method name

		at.appendChild(sequencer.textContainer(
				"didi_ajax.handler_" + value + "('"));
		
		Node n = sequencer.getIdentityValueTemplate("id", value);
		document.adoptNode(n);
		 
		at.appendChild(n);
		at.appendChild(sequencer.textContainer("', {"));

		Iterator<String>it = paramMap.keySet().iterator();
		while(it.hasNext())
		{
			String k = it.next();
			at.appendChild(sequencer.textContainer("'" + k + "':"));

			String exp = paramMap.get(k);
			if (exp.startsWith("!"))
			{
				at.appendChild(sequencer.textContainer(
						"'" + exp.substring(1) + "'"));
			}
			else if (exp.startsWith("$"))
			{
				String s = exp.substring(1);
				String[] kk = s.split("[.]", 2);
				at.appendChild(sequencer.textContainer("document.forms['"));
				at.appendChild(sequencer.getIdentityValueTemplate("name", kk[0]));
				at.appendChild(sequencer.textContainer("']." + kk[1] + ".value"));
			}
			else	// assume its a path exp
			{
				at.appendChild(sequencer.textContainer("'"));
				Element e = document.createElement("xsl:value-of");
//System.out.println("MAKING A PATH WITH " + exp);				
				exp = dequalify(getIteratorContext(),exp);
//System.out.println("QUALIFIED " + exp);				
				exp = sequencer.translateXSLPath(exp);
//System.out.println("TRANSLATED " + exp);				
				e.setAttribute("select", exp);
				at.appendChild(e);
				at.appendChild(sequencer.textContainer("'"));
			}
			if(it.hasNext())
				at.appendChild(sequencer.textContainer(", "));
		}
		String pre = getPrefixAttribute(prefix, "pre");
		String post = getPrefixAttribute(prefix, "post");

		at.appendChild(sequencer.textContainer("},"
		      + (pre == null ? "null" : pre) + ", "
		      + (post == null ? "null" : post) + "); return false;"));

		super.attributes();
	}

	private String getPrefixAttribute(String prefix, String name)
	{
		return getPrefixAttribute(element, prefix, name);
	}
	private String getPrefixAttribute(Element element, String prefix, String name)
	{
		String result = null;
		String k = prefix + name;
		if (element.hasAttribute(k))
		{
			result = element.getAttribute(k);
			element.removeAttribute(k);
		}
		return result;
	}

	private String getAttributeName(String nodeName)
	{
		// TODO: return appropriate handler names
		// based on the current element, or fail!!
		return "onclick";
	}

}
