/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dykman.dexter.DexterException;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.dexterity.DexteritySyntaxException;

public abstract class BaseTransformSequencer implements TransformSequencer
{
	protected Map<String, String> metaData = new HashMap<String, String>();

	protected Properties properties;
	public void setMeta(String key, String value)
	{
		if (value == null)
		{
			metaData.remove(key);
		}
		else
		{
			metaData.put(key, value);
		}
	}

	public void setProperties(Properties p)
	{
		properties = p;
	}
	public String getMeta(String key)
	{
		return metaData.get(key);
	}

	public final void runDescriptor(Descriptor descriptor)
	{
		// System.out.println(descriptor.getClass());
		descriptor.setTransformSequencer(this);
		descriptor.start();
		descriptor.attributes();
		descriptor.children();
		descriptor.end();
	}

	protected String getLastToken(String path)
	{
		int n = path.lastIndexOf('/');
		String result = null;
		if (n > -1)
		{
			result = path.substring(n + 1);
		}
		else
		{
			result = path;
		}

		return result;
	}

	protected String generateXSLTest(String tests)
	{
		String[] ors = tests.split("[|]");
		if (ors.length == 1)
		{
			String[] ands = ors[0].split(" ");
			return generateXSLTest(ands);
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ors.length; ++i)
			{
				String[] ands = ors[i].split(" ");
				sb.append('(').append(generateXSLTest(ands)).append(')');
				if (i + i < ors.length)
				{
					sb.append(" or ");
				}
			}
			return sb.toString();
		}
	}

	protected String generateXSLTest(String[] tests)
	{
		StringBuffer buffer = new StringBuffer();
		if (tests.length > 0)
		{
			for (int i = 0; i < tests.length; ++i)
			{
				boolean st = true;
				String t = tests[i];
//System.out.println("desconstructing " + t);				

				if(t.startsWith("!"))
				{
					st = false;
					t = t.substring(1);
					buffer.append("not(");
				}
				
				buffer.append(translateXSLPath(t));
				// end not
				if(st == false)
				{
					buffer.append(')');
				}
				if (i + 1 < tests.length)
				{
					buffer.append(" and ");
				}

			}
		}
		else
		{
			// NULL test???
			throw new DexteritySyntaxException("null test!?");
		}
		return buffer.toString().trim();
	}
	
	public PathFunction createPathFunction(String func)
	{
		try
		{
			String v = properties.getProperty(
					"dexterity.macro.def." + func);
			if(v == null)
			{
				return createClassPathFunction(func);
			}
			else
			{
				return new SimplePathFunction(v);
			}
		}
		catch(Exception e)
		{
			throw new DexterException("unable to instantiate path function", e);
		}
	}
	private PathFunction createClassPathFunction(String func)
	{
		try
		{
			String v = properties.getProperty(
					"dexterity.macro.class." + func);
			Class klass = Class.forName(v);
			return (PathFunction)klass.newInstance();
		}
		catch(Exception e)
		{
			throw new DexterException("unable to instantiate path function", e);
		}
	}
	
	
	public String translateXSLPath(String p)
	{
		StringBuffer buffer = new StringBuffer();
		int n = p.indexOf('@');
		String name = null;
		if(n != -1)
		{
			name=p.substring(n+1);
			p = p.substring(0,n);
		}

		String xslPath = rawXSLPath(p);
		if(name != null)
		{
			PathFunction tf = null;
			String arg = null;
			if(name.startsWith("!"))
			{
				String[] bits = name.substring(1).split(":",2);
				tf = createPathFunction(bits[0]);
				if(bits.length > 1)
				{
					arg = bits[1];
				}
				
			}
			else
			{
				tf = createPathFunction("cmp-name");
				arg = name;
			}
//System.out.println(tf.getClass().getName()+": " + xslPath + "." + (arg == null ? "<null>" : arg));
			if(xslPath.length() == 0)
			{
				xslPath = ".";
			}
			buffer.append(tf.apply(xslPath, arg));
		}
		else
		{
			buffer.append(xslPath);
		}
		
		return buffer.toString();
	}
	
	private String rawXSLPath(String p)
	{
		
		// System.out.print("translatexslpath: in = " + p + " " );
		StringBuffer buffer = new StringBuffer();
		String[] el = p.split("/");
		for (int i = 0; i < el.length; ++i)
		{
			String t = el[i];
			if (t.length() == 0)
			{
				// buffer.append("/");
			}
			if (t.equals("**"))
			{
				buffer.append("./");
			}
			else
			{
				// int n;
				if (t.indexOf(":") != -1)
				{
					String[] bb = t.split(":", 2);
					if (bb[0].length() == 0)
					{
						if (i > 0)
						{
							buffer.append("./");
						}
					}
					else
					{
						buffer.append(bb[0]).append("/");
					}
					buffer.append("@").append(bb[1]);
				}
				else
				{
					buffer.append(t);
				}
			}

			if (i + 1 < el.length)
			{
				buffer.append("/");
			}
		}
		// System.out.println("out = " + buffer.toString().trim());
		return buffer.toString().trim();
	}
	
	
}
