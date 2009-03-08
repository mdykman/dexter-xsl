package org.dykman.dexter.didi;

public class OptionSEditor extends OptionEditor
{
	@Override
	protected String[] splitArgs(String v) {
		if(v.length() > 0 ) {
			StringBuffer buffer = new StringBuffer();
			buffer.append('[').append(v.substring(0, 1)).append(']');
			return v.substring(1).split(buffer.toString());
		}
		else {
			return  new String[0];
		}
	}
}
