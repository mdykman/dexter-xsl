/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */


package org.dykman.dexter.didi;

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

public class JSParamDescriptor extends PathDescriptor
{

	public JSParamDescriptor(Descriptor descriptor)
	{
		super(descriptor);
//		System.out.println("CONSTRUCTED!!!");		
	}

	@Override
	public void attributes()
	{
		String targetAttr 
			= properties.getProperty("dexter.didi.data");
		if(element.hasAttribute(targetAttr))
		{
			element.removeAttribute(targetAttr);
		}
		super.attributes();

		Node base = sequencer.getCurrentNode();
		Document document = base.getOwnerDocument();
		
		Element at = document.createElement("xsl:attribute");
		at.setAttribute("name", targetAttr);
		base.appendChild(at);

		
		at.appendChild(sequencer.textContainer("{"));
		if (value != null)
		{
			String[] pp = value.split(DexterityConstants.ARG_SEP);
			for (int i = 0; i < pp.length; ++i)
			{
				int n = pp[i].indexOf(":");
				if(n > -1)
				{
					String k = pp[i].substring(0,n);
					String exp = pp[i].substring(n+1);
					
					at.appendChild(sequencer.textContainer("\"" + k + "\":"));

					if (exp.startsWith("!"))
					{
						at.appendChild(sequencer.textContainer(
								"\"" + exp.substring(1) + "\""));
					}
					else if (exp.startsWith("$"))
					{
						String s = exp.substring(1);
						String[] kk = s.split("[.]", 2);
						at.appendChild(sequencer.textContainer("document.forms[\""));
						at.appendChild(sequencer.getIdentityValueTemplate("name", kk[0]));
						at.appendChild(sequencer.textContainer("\"]." + kk[1] + ".value"));
					}
					else	// assume its a path exp
					{
						at.appendChild(sequencer.textContainer("\""));
						Element e = document.createElement("xsl:value-of");
						exp = dequalify(getIteratorContext(),exp);
						exp = sequencer.translateXSLPath(exp);
						e.setAttribute("select", exp);
						at.appendChild(e);
						at.appendChild(sequencer.textContainer("\""));
					}
				}
				else
				{
					System.out.println("illegal argument formation");
				}
				if(i+1 < pp.length)
				{
					at.appendChild(sequencer.textContainer(", "));
				}

			}
		}
		at.appendChild(sequencer.textContainer("}"));
		 
	}
}
