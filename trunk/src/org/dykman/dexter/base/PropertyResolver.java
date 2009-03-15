package org.dykman.dexter.base;

public interface PropertyResolver
{
	public String getProperty(String key);
	public String getProperty(String module, String key);
	public void setProperty(String key,String value);
}
