/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dykman.dexter.base.TransformSequencer;

public class MetaDescriptor extends TransformDescriptor
{
	private static String NULL_STRING_TRIED 
		= "DEXTER_NO_NOT_USE_OR_IT_ALL_FALLS_DOWN_BECAUSE_THIS_IS_THE_NEGATIVE_CACHING_MARKER";
	private Map<String, String> meta = new HashMap<String, String>();
	public MetaDescriptor(Descriptor descriptor)
	{
		super(descriptor);
	}
	protected final String getMeta(String key)
	{
		return getMeta(this.sequencer,key);
	}
	public static final String getMeta(TransformSequencer sequencer, String key)
	{
		return sequencer.getMeta(key);
	}
	protected final void setMeta(String key, String value)
	{
		setMeta(this.sequencer,meta,key,value);
	}
	public static final void setMeta(
			TransformSequencer sequencer,
			Map<String,String> meta,
			String key, String value)
	{
		String v = meta.get(key);
		// save initial state of meta vars so they can be
		// restored during end():
		if (v == null)
		{
			v = sequencer.getMeta(key);
			if (v == null)
			{
				meta.put(key, NULL_STRING_TRIED);
			}
			else
			{
				meta.put(key, v);
			}
		}

		sequencer.setMeta(key, value);
	}
	public void end()
	{
		super.end();
		
		// unwind meta varibles
		Iterator<Map.Entry<String, String>> it = meta.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
			if (entry.getValue() == NULL_STRING_TRIED)
			{
				this.sequencer.setMeta(entry.getKey(), null);
			}
			else
			{
				this.sequencer.setMeta(entry.getKey(), entry.getValue());
			}
		}
	}
}
