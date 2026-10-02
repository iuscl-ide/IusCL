/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLCollection;

public class IusCLWindowsStatusPanels extends IusCLCollection {

	public IusCLWindowsStatusPanels(IusCLWindowsStatusBar customStatusBar) {
		super(customStatusBar);
	}

	@Override
	public IusCLWindowsStatusBar getPropertyComponent() {
		return (IusCLWindowsStatusBar) super.getPropertyComponent();
	}

	public IusCLWindowsStatusBar getWindowsStatusBar() {
		return getPropertyComponent();
	}

	@Override
	public Class<?> getItemClass() {
		return IusCLWindowsStatusPanel.class;
	}

	@Override
	public IusCLWindowsStatusPanel add() {
		return new IusCLWindowsStatusPanel(this);
	}

	@Override
	public IusCLWindowsStatusPanel insert(int index) {
		return new IusCLWindowsStatusPanel(this, index);
	}

	@Override
	public IusCLWindowsStatusPanel get(int index) {
		return (IusCLWindowsStatusPanel) super.get(index);
	}

	public Integer indexOf(IusCLWindowsStatusPanel statusPanel) {
		return super.indexOf(statusPanel);
	}

	@Override
	public void delete(int index) {
		super.delete(index);

		getWindowsStatusBar().update();
	}
}
