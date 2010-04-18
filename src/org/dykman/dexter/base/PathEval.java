/**
 * 
 */
package org.dykman.dexter.base;

import java.util.ArrayList;
import java.util.List;

import org.dykman.dexter.dexterity.DexteritySyntaxException;

public class PathEval {
	public static final int LITERAL = 1;
	public static final int XPATH = 2;
	public static final int LOOKUP = 3;
	public static final int TEMPLATE = 4;
	
	public final String path;
	public final int type;
	
	
	
	
	public static PathEval parseSingle(String s) {
		List<PathEval> list = parse(s);
		if(list.size() != 1) {
			throw new DexteritySyntaxException("" + list.size() + " elements found in parseSingle");
		}
		return list.get(0);
	}
		public static List<PathEval> parse(String s) {
		List<PathEval> list = new ArrayList<PathEval> ();
		
		if(s.indexOf("{") == -1) {
			list.add(new PathEval(s,XPATH));
			return list;
		}
		
		Tokenizer tok = new Tokenizer(s);
		while(tok.hasNext()) {
			String t = tok.nextToken();
			if(t.equals("{")) {
				if(!tok.hasNext()) throw new DexteritySyntaxException("untermiated xpath phrase");
				t = tok.nextToken();
				if(t.equals("}")) {
					list.add(new PathEval(".",XPATH));
				} else {
					list.add(new PathEval(t,XPATH));
					if(!"}".equals(tok.nextToken()))
						throw new DexteritySyntaxException("failed to close expression phrase");
				}
			}
			else  if(t.equals("{{")) {
				if(!tok.hasNext()) throw new DexteritySyntaxException("untermiated lookup phrase");
				t = tok.nextToken();
				if(t.equals("}}")) {
					list.add(new PathEval(".",LOOKUP));
				} else {
					list.add(new PathEval(t,LOOKUP));
					if(!"}}".equals(tok.nextToken()))
						throw new DexteritySyntaxException("failed to close lookup phrase");
				}
			}
			else {
				list.add(new PathEval(t,LITERAL));
			}
		}
		return list;
	}

	public PathEval(PathEval eval, String path) {
		this.path = path;
		this.type = eval.type;
	}

	protected PathEval(String path, int type) {
		this.path = path;
		this.type = type;
	}
	
	protected PathEval(String path) {
		this.path = path;
		this.type = XPATH;
	}

	public String getPath()
    {
    	return path;
    }

	public int getType() {
		return type;
	}
	
	static class Tokenizer {
		int pos;
		StringBuilder sb;
		boolean inplace = false;

		public Tokenizer(String s) {
			pos = 0;
			sb = new StringBuilder(s);
//			System.out.println("NEW TOKENIZER");
		}

		public boolean hasNext() {
			return pos < sb.length();
		}

		public String nextToken() {
			String result;
			if(sb.charAt(pos) == '{') {
				if(pos+1 < sb.length() && sb.charAt(pos+1)== '{') {
					pos +=2;
					result = "{{";
				} else {
					pos ++;
					result = "{";
				}
				inplace = true;
			} else if(sb.charAt(pos) == '}') {
				if(pos+1 < sb.length() && sb.charAt(pos+1)== '}') {
					pos +=2;
					result = "}}";
				} else {
					pos ++;
					result = "}";
				}
				inplace = false;
			} else {
				int n = sb.indexOf(inplace ? "}" : "{",pos);
				if(n!= -1) {
					int p = pos;
					pos = n;
					result = sb.substring(p, n);
				} else {
					result = sb.substring(pos);
					pos = sb.length();
					
				}
			}
//			System.out.println("\t\ttoken " + result);
			return result;
		}
	}

}