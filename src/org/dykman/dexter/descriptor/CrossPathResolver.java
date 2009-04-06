/**
 * 
 */
package org.dykman.dexter.descriptor;

public class CrossPathResolver {
		String ic,base;
		public CrossPathResolver(PathDescriptor pd) {
			ic = pd.getIteratorContext();
			base = pd.getPath();
		}
		public String translate(String path) {
			if(path.startsWith("/")) return path;
			String out =  PathDescriptor.dequalify(ic,PathDescriptor.mapPath(base,path));
//System.out.println(" ==>>" + path + " translated to " + out);
			return out;
		}
	}