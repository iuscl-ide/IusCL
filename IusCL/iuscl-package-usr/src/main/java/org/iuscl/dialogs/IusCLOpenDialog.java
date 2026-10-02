/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.iuscl.classes.IusCLComponent;

public class IusCLOpenDialog extends IusCLFileDialog {

	public IusCLOpenDialog(IusCLComponent aOwner) {
		super(aOwner);

		createParam = SWT.OPEN;
		swtFileDialog = new FileDialog(this.findForm().getSwtShell(), createParam);

		this.getProperty("Title").setDefaultValue("Open");
		setTitle("Open");

		assign();
	}
}
