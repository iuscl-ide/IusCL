/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.iuscl.classes.IusCLComponent;

public class IusCLPopupMenu extends IusCLMenu {

	public IusCLPopupMenu(IusCLComponent aOwner) {
		super(aOwner);

		swtMenu = new Menu(this.findForm().getSwtShell(), SWT.POP_UP);
	}

	public void popup(Integer screenX, Integer screenY) {
		swtMenu.setLocation(screenX, screenY);
		swtMenu.setVisible(true);
	}
}
