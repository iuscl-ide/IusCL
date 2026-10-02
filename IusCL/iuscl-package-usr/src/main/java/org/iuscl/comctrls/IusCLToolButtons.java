/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLCollection;

public class IusCLToolButtons extends IusCLCollection {

	public IusCLToolButtons(IusCLToolBar toolBar) {
		super(toolBar);
	}

	@Override
	public IusCLToolBar getPropertyComponent() {
		return (IusCLToolBar) super.getPropertyComponent();
	}

	public IusCLToolBar getToolBar() {
		return getPropertyComponent();
	}

	@Override
	public Class<?> getItemClass() {
		return IusCLToolButton.class;
	}

	@Override
	public IusCLToolButton add() {
		return new IusCLToolButton(this);
	}

	@Override
	public IusCLToolButton insert(int index) {
		return new IusCLToolButton(this, index);
	}

	@Override
	public IusCLToolButton get(int index) {
		return (IusCLToolButton) super.get(index);
	}

	public Integer indexOf(IusCLToolButton toolButton) {
		return super.indexOf(toolButton);
	}
}
