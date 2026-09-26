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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCheckBox extends IusCLButtonControl {

	public enum IusCLCheckBoxState {
		cbUnchecked, cbChecked, cbGrayed
	}

	public enum IusCLLeftRight {
		taLeftJustify, taRightJustify
	}

	@Getter
	IusCLLeftRight alignment = IusCLLeftRight.taLeftJustify;
	@Getter
	boolean allowGrayed = false;
	@Getter
	IusCLCheckBoxState state = IusCLCheckBoxState.cbUnchecked;

	public IusCLCheckBox(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Alignment", IusCLPropertyType.ptEnum, "taLeftJustify", IusCLLeftRight.taLeftJustify);
		defineProperty("AllowGrayed", IusCLPropertyType.ptBoolean, "false");
		defineProperty("Checked", IusCLPropertyType.ptBoolean, "false");
		defineProperty("State", IusCLPropertyType.ptEnum, "cbUnchecked", IusCLCheckBoxState.cbUnchecked);

		swtButton = new Button(this.getFormSwtComposite(), SWT.CHECK);
		createWnd(swtButton);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Width").setDefaultValue("97");
		setWidth(97);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			switch (state) {
			case cbChecked:
				setState(IusCLCheckBoxState.cbUnchecked);
				break;
			case cbGrayed:
				setState(IusCLCheckBoxState.cbChecked);
				break;
			case cbUnchecked:
				if (allowGrayed) {
					setState(IusCLCheckBoxState.cbGrayed);
				} else {
					setState(IusCLCheckBoxState.cbChecked);
				}
				break;
			}
		}));
	}

	public void setAlignment(IusCLLeftRight alignment) {
		this.alignment = alignment;

		switch (alignment) {
		case taLeftJustify:
			swtButton.setAlignment(SWT.LEFT);
			break;
		case taRightJustify:
			swtButton.setAlignment(SWT.RIGHT);
			break;
		}
	}

	public void setAllowGrayed(boolean allowGrayed) {
		this.allowGrayed = allowGrayed;

		if (!allowGrayed && state == IusCLCheckBoxState.cbGrayed) {
			setState(IusCLCheckBoxState.cbUnchecked);
		}
	}

	public void setState(IusCLCheckBoxState state) {
		this.state = state;

		switch (state) {
		case cbChecked:
			setChecked(true);
			swtButton.setGrayed(false);
			swtButton.setSelection(true);
			break;
		case cbGrayed:
			setAllowGrayed(true);
			setChecked(false);
			swtButton.setGrayed(true);
			swtButton.setSelection(true);
			break;
		case cbUnchecked:
			setChecked(false);
			swtButton.setGrayed(false);
			swtButton.setSelection(false);
			break;
		}
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;

		if (checked) {
			if (state != IusCLCheckBoxState.cbChecked) {
				setState(IusCLCheckBoxState.cbChecked);
			}
		} else {
			if (state == IusCLCheckBoxState.cbChecked) {
				setState(IusCLCheckBoxState.cbUnchecked);
			}
		}
	}
}
