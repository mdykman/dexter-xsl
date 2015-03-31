package org.dykman.dexter.base;

public class TrimFunction implements PathFunction
{
	public String apply(String path, String arg)
	{
		return "normalize-space(" + path + ")";
	}

}
