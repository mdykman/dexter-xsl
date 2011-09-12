/**
 * dexter (c) 2007, 2008 Michael Dykman 
 * Free for use under version 2.0 of the Artistic License.     
 * http://www.opensource.org/licences/artistic-license.php     
 */

package org.dykman.dexter.descriptor;

import org.dykman.dexter.base.TransformSequencer;

public abstract class TransformDescriptor extends AbstractDescriptor {
	protected TransformSequencer sequencer = null;
	protected Descriptor inner;

	protected boolean applyToChildren = false;

	public TransformDescriptor(Descriptor descriptor) {
		this.inner = descriptor;
	}

	public void onScan() {
	}

	public void beforeNode() {
	}

	public void afterNode() {
	}

	public Descriptor[] getChildDescriptors() {
		return inner.getChildDescriptors();
	}

	public void attributes() {
		inner.attributes();
	}

	@Override
	public void appendChild(Descriptor child) {
		inner.appendChild(child);
	}

	public void start() {
		if (!applyToChildren) {
			beforeNode();
		}
		inner.start();
	}

	public void children() {
		if (applyToChildren) {
			beforeNode();
		}

		inner.children();
		if (applyToChildren) {
			afterNode();
		}

	}

	public void end() {
		inner.end();
		if (!applyToChildren) {
			afterNode();
		}
	}

	public void setTransformSequencer(TransformSequencer sequencer) {
		if (inner != null)
			inner.setTransformSequencer(sequencer);
		this.sequencer = sequencer;
	}

}
