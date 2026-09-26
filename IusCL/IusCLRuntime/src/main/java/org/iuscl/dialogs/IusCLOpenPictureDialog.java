/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.iuscl.classes.IusCLComponent;

import lombok.Getter;

public class IusCLOpenPictureDialog extends IusCLFileDialog {

	@Getter
	private static final String pictureFilter = "All (*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf)|*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf|"
			+ "Gifs (*.gif)|*.gif|" + "Portable Network Graphics (*.png)|*.png|" + "JPEG Image File (*.jpg)|*.jpg|"
			+ "JPEG Image File (*.jpeg)|*.jpeg|" + "Bitmaps (*.bmp)|*.bmp|" + "Icons (*.ico)|*.ico|" + "Enhanced Metafiles (*.emf)|*.emf|"
			+ "Metafiles (*.wmf)|*.wmf";

	public IusCLOpenPictureDialog(IusCLComponent aOwner) {
		super(aOwner);

		createParam = SWT.OPEN;
		swtFileDialog = new FileDialog(this.findForm().getSwtShell(), createParam);

		this.getProperty("Filter").setDefaultValue(pictureFilter);
		setFilter(pictureFilter);
		this.getProperty("Title").setDefaultValue("Open picture");
		setTitle("Open picture");

		assign();
	}
}
