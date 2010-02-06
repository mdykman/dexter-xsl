/**
 * 
 */
package org.dykman.dexter.base;

public class PathEval {
	String path;
	boolean lookup = false;
	
	public PathEval(String path) {
		this(path,true);
	}

	public PathEval(String path,  boolean lookup) {
		this.path = path;
		this.lookup = lookup;
	}
}