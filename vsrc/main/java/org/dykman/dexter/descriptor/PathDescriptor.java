/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import java.util.List;

import org.dykman.dexter.base.PathEval;
import org.dykman.dexter.base.TransformSequencer;
import org.dykman.dexter.dexterity.DexterityConstants;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class PathDescriptor extends NodeTransformDescriptor
{
	public static String idHash = null;

	public PathDescriptor(Descriptor  descriptor) {
		super(descriptor);
	}
	
	@Override
	public void onScan() {
		if(value.startsWith("#")) {
			applyToChildren =true;
			value = value.substring(1);
		}
	};

	protected String [] getTests() {
		return value.split(DexterityConstants.ARG_SEP);
	}

	public static char nextOf(String s,int offset, char[] set) {
		char c = 0;
//		int[] ff = new int[set.length];
		int l = s.length();
		for(int i = 0; i < set.length; ++i) {
			int ff = s.indexOf(set[i], offset);
			if(ff > -1) {
				if(ff < l) {
					l = ff;
					c = set[i];
				}
			}
		}
		return c;
	}

	
	public String setPath(String path) {
		String root = getMeta(DexterityConstants.PATH);
		
		String result =  mapPath(root, path); 
		setMeta(DexterityConstants.PATH,	result);
		return result;
	}
	
	public DocumentFragment getChildren(Node node) {
		Document doc = node.getOwnerDocument();
		DocumentFragment df = doc.createDocumentFragment();
		NodeList nl = node.getChildNodes();
		for(int i = 0; i < nl.getLength(); ++i) {
			df.appendChild(nl.item(1).cloneNode(true));
		}
		return df;
	}

	public String getIteratorContext() {
		String s = getMeta(DexterityConstants.ITER_CONTEXT); 
		return s == null ? "/" : s;
	}

	public String setIteratorContext(String path) {
		String root = getMeta(DexterityConstants.ITER_CONTEXT);
		
		String result =  mapPath(root, path); 
		setMeta(DexterityConstants.ITER_CONTEXT,	result);
		return result;
	}

	public static String getPath(TransformSequencer sequencer) {
		return getMeta(sequencer, DexterityConstants.PATH);
	}

	public String getPath() {
		return getMeta(DexterityConstants.PATH);
	}
	
	public String[] mapPaths(String[] p) {
		return mapPaths(getPath(),p);
	}

	public static String[] mapPaths(String root,String[] p) {
		String[] args=new String[p.length];
		for(int i = 0; i < p.length;++i) {
			
			args[i] = mapPath(root,p[i]);
//System.out.println(p[i] + " =>> " + args[i]);
		}
		return args;
	}

	public String mapPath(String path) {
		return mapPath(getPath(),path);
	}

	public static String mapPath(String root, String path) {
	   	StringBuilder buffer;
	   	if (path.startsWith("/")) {
	   		buffer = new StringBuilder(path);
	   	} else {
	   		buffer = new StringBuilder(root == null ? "/" : root);
	   		if (!path.startsWith(":")) {
	   			if (root != null && !root.endsWith("/")) {
	   				buffer.append('/');
	   			}
	   		}
	   		buffer.append(path);
	   	}
	   
	   	return buffer.toString();
    }
	
	public static String dequalify(String path, String in) {
		if(in.startsWith(path)) {
			in = in.substring(path.length());
			if(in.startsWith("/")) {
				in = in.substring(1);
			}
		}
		return in;
	}
	

	protected List<PathEval>  valueTemplateParams(String value) {
		return PathEval.parse(value);
	}
}
