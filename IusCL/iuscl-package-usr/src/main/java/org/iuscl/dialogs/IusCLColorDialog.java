/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Dialog;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLColorDialog extends IusCLCommonDialog {

	ColorDialog swtColorDialog = null;

	@Getter
	String title = "";
	@Getter
	IusCLColor color = IusCLColor.getStandardColor(IusCLStandardColors.clBlack);

	public IusCLColorDialog(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Title", IusCLPropertyType.ptString, "Color");
		defineProperty("Color", IusCLPropertyType.ptColor, "clBlack", IusCLStandardColors.clBlack);

		swtColorDialog = new ColorDialog(this.findForm().getSwtShell());
	}

	@Override
	public boolean execute() {
		RGB returnRGB = swtColorDialog.open();

		if (returnRGB != null) {
			this.color = new IusCLColor(returnRGB.red, returnRGB.green, returnRGB.blue);

			return true;
		}

		return false;
	}

	@Override
	public Dialog getSwtDialog() {
		return swtColorDialog;
	}

	public void setTitle(String title) {
		this.title = title;

		swtColorDialog.setText(title);
	}

	public void setColor(IusCLColor color) {
		this.color = color;

		swtColorDialog.setRGB(new RGB(color.getRed(), color.getGreen(), color.getBlue()));
	}
}
