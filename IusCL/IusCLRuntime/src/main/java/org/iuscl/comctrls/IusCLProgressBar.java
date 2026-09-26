/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.graphics.IusCLFont;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLProgressBar extends IusCLWinControl {

	public enum IusCLProgressBarOrientation {
		pbHorizontal, pbVertical
	}

	public enum IusCLProgressBarState {
		psNormal, psError, psPause
	}

	ProgressBar swtProgressBar = null;

	@Getter
	Integer max = 100;
	@Getter
	Integer min = 0;
	@Getter
	IusCLProgressBarOrientation orientation = IusCLProgressBarOrientation.pbHorizontal;
	@Getter
	IusCLProgressBarState state = IusCLProgressBarState.psNormal;
	@Getter
	Integer position = 0;
	@Getter
	boolean smooth = false;
	@Getter
	@Setter
	Integer step = 10;
	@Getter
	boolean indeterminate = false;

	public IusCLProgressBar(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Max", IusCLPropertyType.ptInteger, "100");
		defineProperty("Min", IusCLPropertyType.ptInteger, "0");
		defineProperty("Orientation", IusCLPropertyType.ptEnum, "pbHorizontal", IusCLProgressBarOrientation.pbHorizontal);
		defineProperty("State", IusCLPropertyType.ptEnum, "psNormal", IusCLProgressBarState.psNormal);
		defineProperty("Position", IusCLPropertyType.ptInteger, "0");
		defineProperty("Smooth", IusCLPropertyType.ptBoolean, "false");
		defineProperty("Step", IusCLPropertyType.ptInteger, "10");
		defineProperty("Indeterminate", IusCLPropertyType.ptBoolean, "false");

		createWnd(createSwtControl());
	}

	@Override
	protected void create() {
		super.create();

		this.getProperties().get("TabStop").setDefaultValue("false");
		this.setTabStop(false);
		this.removeProperty("Caption");
		this.removeProperty("Font");
		this.removeProperty("ParentFont");
		IusCLFont.removeFontProperties(this, "Font");
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		switch (orientation) {
		case pbHorizontal:
			swtCreateParams = SWT.HORIZONTAL;
			break;
		case pbVertical:
			swtCreateParams = SWT.VERTICAL;
			break;
		}

		if (smooth) {
			swtCreateParams = swtCreateParams | SWT.SMOOTH;
		}

		if (indeterminate) {
			swtCreateParams = swtCreateParams | SWT.INDETERMINATE;
		}

		swtProgressBar = new ProgressBar(this.getFormSwtComposite(), swtCreateParams);

		return swtProgressBar;
	}

	public void setMax(Integer max) {
		this.max = max;

		swtProgressBar.setMaximum(max);
	}

	public void setMin(Integer min) {
		this.min = min;

		swtProgressBar.setMinimum(min);
	}

	public void setOrientation(IusCLProgressBarOrientation orientation) {
		if (this.orientation != orientation) {
			this.orientation = orientation;

			Integer height = this.getHeight();
			Integer width = this.getWidth();

			this.setWidth(height);
			this.setHeight(width);

			reCreateWnd();
		}
	}

	public void setState(IusCLProgressBarState state) {
		this.state = state;

		switch (state) {
		case psError:
			swtProgressBar.setState(SWT.ERROR);
			break;
		case psNormal:
			swtProgressBar.setState(SWT.NORMAL);
			break;
		case psPause:
			swtProgressBar.setState(SWT.PAUSE);
			break;
		}
	}

	public void setPosition(Integer position) {
		this.position = position;

		swtProgressBar.setSelection(position);
	}

	public void setSmooth(boolean smooth) {
		if (this.smooth != smooth) {
			this.smooth = smooth;
			reCreateWnd();
		}
	}

	public void setIndeterminate(boolean indeterminate) {
		if (this.indeterminate != indeterminate) {
			this.indeterminate = indeterminate;
			reCreateWnd();
		}
	}

	public void stepIt() {
		stepBy(step);
	}

	public void stepBy(Integer delta) {
		int newPos = position + delta;

		if (newPos < max) {
			setPosition(newPos);
		} else {
			setPosition(max);
		}
	}

	@Override
	public void setParentFont(boolean parentFont) {
		/* Intentionally nothing */
	}
}
