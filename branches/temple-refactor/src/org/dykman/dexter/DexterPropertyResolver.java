package org.dykman.dexter;

import java.util.Map;
import java.util.Properties;

import org.dykman.dexter.base.PropertyResolver;

public class DexterPropertyResolver implements PropertyResolver
{
	Properties properties;
	PropertyResolver fallthrough;
	String module = null;

	public DexterPropertyResolver(
			Properties properties)
	{
		this(properties,null);
	}

	public DexterPropertyResolver(
			String module, Properties properties ,PropertyResolver fallthrough)
	{
		this.module = module;
		this.properties = properties;
		this.fallthrough = fallthrough;
	}
	public DexterPropertyResolver(
			Properties properties ,PropertyResolver fallthrough)
	{
		this(null,properties,fallthrough);
	}
	public void setProperty(String key, String value)
	{
		if(module == null)
		{
			properties.setProperty(key, value);
		}
		else
		{
			properties.setProperty(module + "." + key, value);
			
		}
	}
	

	public Properties getPropertiesMatching(String key)
	{
		String kk = module + "." + key;
		int len = kk.length() + 1;
		String k;
		Properties props = new Properties();
		for(Map.Entry<Object,Object> entry : properties.entrySet()) {
			k = entry.getKey().toString();
			if(k.startsWith(kk))
				props.put(k.substring(len), entry.getValue());
		}
		for(Map.Entry<Object,Object> entry : System.getProperties().entrySet()) {
			k = entry.getKey().toString();
			if(k.startsWith(kk))
				props.put(k.substring(len), entry.getValue());
		}
		return props;
	}

	public String getProperty(String key)
	{
		return this.getProperty(module, key);
	}

	public String getProperty(String module,String key)
	{
		if(module != null)
		{
			key = module + '.' + key;
		}
		String result = System.getProperty(key);
		if(result == null)
		{
			result = properties.getProperty(key);
		}

		if(result == null && fallthrough != null)
		{
			result = fallthrough.getProperty(key);
		}
		
		if(result != null)
		{
			result = result.trim();
			if(result.length() == 0)
			{
				result = null;
			}
		}
		return result;
	}
}
