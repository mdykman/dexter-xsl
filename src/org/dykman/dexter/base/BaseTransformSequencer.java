/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.base;

import java.util.HashMap;
import java.util.Map;

import org.dykman.dexter.Dexter;
import org.dykman.dexter.descriptor.Descriptor;

public abstract class BaseTransformSequencer implements TransformSequencer {
	protected Map<String, String>	    metaData	  = new HashMap<String, String>();
	protected Map<String, PathFunction>	pathFunctions	= new HashMap<String, PathFunction>();

	Dexter	                            dexter;

	public BaseTransformSequencer(Dexter dexter) {
		this.dexter = dexter;
	}

	public void definePathFunction(String name, PathFunction function) {
		this.pathFunctions.put(name, function);
	}

	public void setMeta(String key, String value) {
		if (value == null) {
			metaData.remove(key);
		} else {
			metaData.put(key, value);
		}
	}

	public String getMeta(String key) {
		return metaData.get(key);
	}

	public final void runDescriptor(Descriptor descriptor) {
//System.out.println(descriptor.getClass().getName());
		descriptor.setTransformSequencer(this);
		descriptor.start();
		descriptor.attributes();
		descriptor.children();
		descriptor.end();
	}

	protected String getLastToken(String path) {
		int n = path.lastIndexOf('/');
		String result = null;
		if (n > -1) {
			result = path.substring(n + 1);
		} else {
			result = path;
		}
		return result;
	}
}
