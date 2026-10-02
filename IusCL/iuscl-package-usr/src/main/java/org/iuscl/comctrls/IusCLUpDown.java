/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLUpDown extends IusCLWinControl {

	Spinner swtSpinner = null;

	@Getter
	Integer max = 100;
	@Getter
	Integer min = 0;
	@Getter
	Integer position = 0;
	@Getter
	Integer increment = 1;
	@Getter
	Integer pageIncrement = 10;
	@Getter
	Integer digits = 0;
	@Getter
	Integer textLimit = Spinner.LIMIT;
	@Getter
	@Setter
	boolean wrap = false;

	/*
	 * TODO IusCLUpDown Events
	 * 
	 * OnChanging OnChangingEx OnClick
	 * 
	 */

	public IusCLUpDown(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Max", IusCLPropertyType.ptInteger, "100");
		defineProperty("Min", IusCLPropertyType.ptInteger, "0");
		defineProperty("Position", IusCLPropertyType.ptInteger, "0");
		defineProperty("Increment", IusCLPropertyType.ptInteger, "1");
		defineProperty("PageIncrement", IusCLPropertyType.ptInteger, "10");
		defineProperty("Digits", IusCLPropertyType.ptInteger, "0");
		defineProperty("TextLimit", IusCLPropertyType.ptInteger, Integer.toString(Spinner.LIMIT));
		defineProperty("Wrap", IusCLPropertyType.ptBoolean, "false");

		swtSpinner = new Spinner(this.getFormSwtComposite(), SWT.BORDER);
		createWnd(swtSpinner);
	}

	@Override
	protected void create() {
		super.create();

		this.removeProperty("Caption");

		swtSpinner.addModifyListener(_ -> {
			IusCLUpDown.this.position = swtSpinner.getSelection();
			if (IusCLUpDown.this.wrap && IusCLUpDown.this.position >= IusCLUpDown.this.max) {
				setPosition(IusCLUpDown.this.min);
			}
		});
	}

	public void setMax(Integer max) {
		this.max = max;

		swtSpinner.setMaximum(max);
	}

	public void setMin(Integer min) {
		this.min = min;

		swtSpinner.setMinimum(min);
	}

	public void setPosition(Integer position) {
		this.position = position;

		swtSpinner.setSelection(position);
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;

		swtSpinner.setIncrement(increment);
	}

	public void setPageIncrement(Integer pageIncrement) {
		this.pageIncrement = pageIncrement;

		swtSpinner.setPageIncrement(pageIncrement);
	}

	public void setDigits(Integer digits) {
		this.digits = digits;

		swtSpinner.setDigits(digits);
	}

	public void setTextLimit(Integer textLimit) {
		this.textLimit = textLimit;

		swtSpinner.setTextLimit(textLimit);
	}

	@Override
	public String getText() {
		return swtSpinner.getText();
	}
}
