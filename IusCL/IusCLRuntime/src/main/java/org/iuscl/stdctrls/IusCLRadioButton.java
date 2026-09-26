/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.iuscl.classes.IusCLComponent;

public class IusCLRadioButton extends IusCLButtonControl {

	public IusCLRadioButton(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Checked", IusCLPropertyType.ptBoolean, "false");

		swtButton = new Button(this.getFormSwtComposite(), SWT.RADIO);
		createWnd(swtButton);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Width").setDefaultValue("113");
		setWidth(113);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> checked = swtButton.getSelection()));
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;

		swtButton.setSelection(checked);
	}
}
