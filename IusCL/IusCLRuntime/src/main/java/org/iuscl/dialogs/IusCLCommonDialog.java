/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.iuscl.classes.IusCLComponent;

public class IusCLCommonDialog extends IusCLComponent {

	public IusCLCommonDialog(IusCLComponent aOwner) {
		super(aOwner);
	}

	public boolean execute() {
		return false;
	}

	public Dialog getSwtDialog() {
		return null;
	}
}
