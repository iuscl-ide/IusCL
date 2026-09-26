/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLContainerControl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLGroupBox extends IusCLContainerControl {

	Group swtGroup = null;

	public IusCLGroupBox(IusCLComponent aOwner) {
		super(aOwner);

		swtGroup = new Group(this.getFormSwtComposite(), SWT.SHADOW_NONE);
		createWnd(swtGroup);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("105");
		setHeight(105);
		this.getProperty("Width").setDefaultValue("185");
		setWidth(185);
	}

	@Override
	public Composite getSwtComposite() {
		return this.swtGroup;
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		swtGroup.setText(caption);
	}
}
