/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import java.util.ArrayList;
import java.util.List;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLFont;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLParentControl extends IusCLWinControl {

	@Getter
	final List<IusCLControl> controls = new ArrayList<>();

	public IusCLParentControl(IusCLComponent aOwner) {
		super(aOwner);

	}

	@Override
	public void free() {
		/* free also the child controls */
		for (int index = controls.size() - 1; index >= 0; index--) {
			controls.get(index).free();
		}

		super.free();
	}

	@Override
	public void setColor(IusCLColor color) {
		super.setColor(color);

		parentColorChanged();
	}

	@Override
	public void setParentColor(boolean parentColor) {
		super.setParentColor(parentColor);

		parentColorChanged();
	}

	protected void parentColorChanged() {
		for (int index = 0; index < controls.size(); index++) {
			IusCLControl childControl = controls.get(index);
			childControl.setParentColor(childControl.getParentColor());
		}
	}

	@Override
	public void setShowHint(boolean showHint) {
		super.setShowHint(showHint);

		parentShowHintChanged();
	}

	@Override
	public void setParentShowHint(boolean parentShowHint) {
		super.setParentShowHint(parentShowHint);

		parentShowHintChanged();
	}

	private void parentShowHintChanged() {
		for (int index = 0; index < controls.size(); index++) {
			IusCLControl childControl = controls.get(index);
			childControl.setParentShowHint(childControl.getParentShowHint());
		}
	}

	@Override
	public void setFont(IusCLFont font) {
		super.setFont(font);

		parentFontChanged();
	}

	@Override
	public void setParentFont(boolean parentFont) {
		super.setParentFont(parentFont);

		parentFontChanged();
	}

	private void parentFontChanged() {
		for (int index = 0; index < controls.size(); index++) {
			IusCLControl childControl = controls.get(index);
			childControl.setParentFont(childControl.getParentFont());
		}
	}
}
