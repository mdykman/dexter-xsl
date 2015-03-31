package org.dykman.dexter.base;

import java.util.Properties;

public interface PropertyResolver
{
	public String getProperty(String key);
	public String getProperty(String module, String key);
	public void setProperty(String key,String value);
	public Properties getPropertiesMatching(String key);
}
