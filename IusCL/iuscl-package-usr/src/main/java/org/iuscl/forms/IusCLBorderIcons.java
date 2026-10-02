/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.forms;

import org.iuscl.system.IusCLObject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLBorderIcons extends IusCLObject {

	@Getter
	boolean systemMenu = true;
	@Getter
	boolean minimize = true;
	@Getter
	boolean maximize = true;
	@Getter
	boolean help = false;

	public void setSystemMenu(boolean systemMenu) {
		this.systemMenu = systemMenu;

		invokeNotify();
	}

	public void setMinimize(boolean minimize) {
		this.minimize = minimize;

		invokeNotify();
	}

	public void setMaximize(boolean maximize) {
		this.maximize = maximize;

		invokeNotify();
	}

	public void setHelp(boolean help) {
		this.help = help;

		invokeNotify();
	}
}
