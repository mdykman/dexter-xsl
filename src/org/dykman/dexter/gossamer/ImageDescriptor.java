package org.dykman.dexter.gossamer;

import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;

public class ImageDescriptor extends PathDescriptor {

	public ImageDescriptor(Descriptor descriptor) {
		super(descriptor);
	}


	@Override
	public void attributes() {
		String src = element.getAttribute("src");
		if(src != null) {
			int n = src.lastIndexOf('.');
			if(n != -1) {
				element.setAttribute("src",src.substring(0,n) + value + src.substring(n));
			}
		}
		super.attributes();
	}

}
