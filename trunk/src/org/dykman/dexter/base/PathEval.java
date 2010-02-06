/**
 * 
 */
package org.dykman.dexter.base;

public class PathEval {
	String path;
	boolean force = false;
	boolean disableEscape = false;
	boolean lookup = false;
	
	public PathEval(String path) {
		this(path,true,true,true);
	}

	public PathEval(String path, boolean force, boolean disableEscape, boolean lookup) {
		this.path = path;
		this.force = force;
		this.disableEscape = disableEscape;
		this.lookup = lookup;
	}
}