/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import org.iuscl.classes.IusCLComponent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListControl extends IusCLWinControl {

	@Getter
	IusCLBorderStyle borderStyle = IusCLBorderStyle.bsSingle;
	@Getter
	@Setter
	protected Integer itemIndex = -1;

	public IusCLListControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("BorderStyle", IusCLPropertyType.ptEnum, "bsSingle", IusCLBorderStyle.bsSingle);

		removeProperty("Caption");
	}

	public void setBorderStyle(IusCLBorderStyle borderStyle) {
		if (this.borderStyle != borderStyle) {
			this.borderStyle = borderStyle;

			reCreateWnd();
		}
	}
}
