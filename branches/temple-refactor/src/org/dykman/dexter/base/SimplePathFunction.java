package org.dykman.dexter.base;

public class SimplePathFunction implements PathFunction
{
	String mask;

	public SimplePathFunction(String mask)
	{
		this.mask = mask;
	}
	public String apply(String path, String arg)
	{
		String pattern = java.util.regex.Matcher.quoteReplacement("$path");
		String s = mask.replaceAll(pattern, path);
		if(arg != null)
		{
			s = s.replaceAll(pattern = java.util.regex.Matcher.quoteReplacement("$arg"), arg);
		}
		return s;
	}

}
