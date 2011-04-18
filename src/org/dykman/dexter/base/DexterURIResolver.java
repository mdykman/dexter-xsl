package org.dykman.dexter.base;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class DexterURIResolver implements URIResolver {

	static Map<String,String> resources;
	static {
		resources = new HashMap<String,String>();
		resources.put("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "xhtml1-transitional.dtd");
	}
	
	@Override
	public Source resolve(String href, String base) 
		throws TransformerException {
		String pp = resources.get(base);
		Source src = null;
		if(pp != null) {
			System.out.println("resolving uri:: " + base);
			src = new StreamSource(getClass().getResourceAsStream(pp), base);
		}
		// TODO Auto-generated method stub
		return src;
	}

}
