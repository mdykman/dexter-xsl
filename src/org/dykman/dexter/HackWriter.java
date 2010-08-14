package org.dykman.dexter;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class HackWriter extends Writer
{
	protected Writer inner;
	protected Map<String, String> entities = null;
	protected boolean started = false;
	protected boolean preserveEntities = true;
	protected boolean ampersandPending = false;
	
	public HackWriter(Writer inner)
	{
		this.inner = inner;
	}
	public void setEntities(Map<String, String> entities)
	{
		this.entities = entities;
	}
	public void close()
		throws IOException
	{
		inner.close();
	}
	public void flush()
		throws IOException
	{
		inner.flush();
	}
	public void writeDocType()
		throws IOException
	{
		StringBuilder buffer = new StringBuilder();

		if(preserveEntities && entities != null && entities.size() > 0)
		{
			buffer.append("<!DOCTYPE xsl:stylesheet [\n");
			for(String key : entities.keySet())
			{
				buffer.append(" <!ENTITY ")
					.append(key).append(" \"")
					.append(entities.get(key))
					.append("\" >\n");
			}
	  		buffer.append(" ]");
			buffer.append(">\n");
			inner.write(buffer.toString());
		}
	}
	
	public void writeWithEntities(String s)
		throws IOException {
//	System.out.println("writing = |" + s + "|");
		inner.write(s);
	}
	public void writeWithEntitiesX(String s)
		throws IOException
	{
//	System.out.println("start of write, amppend = " + ampersandPending);
	if(ampersandPending) System.out.println("writing = " + s);
		int off = s.indexOf("&amp;");
		if(ampersandPending) {
			if(off == 0) {
				inner.write("&amp;");
				if(s.length() > 5) {
					writeWithEntities(s.substring(5));
				}
			} 
		
			ampersandPending = false;
			int n = s.indexOf(';');
			if(n == -1) {
//System.out.println("writing single");
				inner.write("&amp;");
			} else {
				String ent = s.substring(0, n);
System.out.println("does this hit? ent = " + ent);
				if(entities.containsKey(ent)) {
					if(preserveEntities) {
						inner.write("&");
						inner.write(ent);
						inner.write(";");
					} else {
						inner.write(entities.get(ent));
					}
					s = s.substring(n+1);
				} else {
System.out.println("unable to locate key = " + ent);
				}
			}
		}

		off = s.indexOf("&amp;");
		if(off == 0) {
			ampersandPending = true;
			if(s.length() > 5) {
				writeWithEntities(s.substring(5));
			}
		} else if(off != -1) {
			inner.write(s.substring(0,off));
			ampersandPending = true;
			if(s.length() > off + 5) {
				writeWithEntities(s.substring(off+5));
			}
		} else {
			inner.write(s);
		}
		
	}

	public void write(char[] ch, int off, int len)
		throws IOException
	{
		String s = new String(ch,off,len);
		
		if(!started)
		{
			
			int n = s.indexOf('>');
			if(n == -1)
			{
				inner.write(s);
			}
			else
			{
				inner.write(s.substring(0,n+1));
				started = true;
				inner.write('\n');
				writeDocType();
				if(s.length() > n+1)
				{
					writeWithEntities(s.substring(n, len - (n+1)));
				}
			}
		}
		else
		{
			writeWithEntities(s);
		}
	}
	public void setPreserveEntities(boolean preserveEntities)
    {
    	this.preserveEntities = preserveEntities;
    }
}
