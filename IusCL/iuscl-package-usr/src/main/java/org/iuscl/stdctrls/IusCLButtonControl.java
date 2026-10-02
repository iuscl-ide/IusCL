/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.widgets.Button;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;

public class IusCLButtonControl extends IusCLWinControl {

	protected Button swtButton = null;

	// private boolean clicksDisabled = false;
	// private boolean wordWrap = false;
	protected boolean checked = false;

	public IusCLButtonControl(IusCLComponent aOwner) {
		super(aOwner);
	}

	@Override
	protected void create() {
		super.create();

		this.removeProperty("OnDoubleClick");
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		swtButton.setText(this.getCaption());
	}

	// public boolean getClicksDisabled() {
	// return clicksDisabled;
	// }
	//
	// public void setClicksDisabled(boolean clicksDisabled) {
	// this.clicksDisabled = clicksDisabled;
	// }
	//
	// public boolean getWordWrap() {
	// return wordWrap;
	// }
	//
	// public void setWordWrap(boolean wordWrap) {
	// this.wordWrap = wordWrap;
	// }
}
