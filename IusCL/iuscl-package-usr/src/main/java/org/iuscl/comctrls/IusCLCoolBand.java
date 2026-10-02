/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLControl;
import org.iuscl.controls.IusCLParentControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCoolBand extends IusCLContainerControl {

	CoolItem swtCoolItem = null;
	Composite swtCoolItemComposite = null;

	@Getter
	boolean autoSize = true;
	boolean isBreak = true;

	IusCLCoolBar coolBar = null;

	public IusCLCoolBand(IusCLComponent aOwner) {
		super(aOwner);

		setIsSubComponent(true);

		this.removeProperty("Left");
		this.removeProperty("Top");

		defineProperty("AutoSize", IusCLPropertyType.ptBoolean, "true");
		defineProperty("Break", IusCLPropertyType.ptBoolean, "true");

		/*
		 * Delete remains to be done.. And align controls inside
		 */
	}

	@Override
	public void setParent(IusCLParentControl parentControl) {
		super.setParent(parentControl);

		coolBar = (IusCLCoolBar) parentControl;
		CoolBar swtCoolBar = (CoolBar) coolBar.getSwtControl();
		swtCoolItem = new CoolItem(swtCoolBar, SWT.NONE);

		swtCoolItemComposite = new Composite(swtCoolBar, SWT.NONE);
		this.setSwtControl(swtCoolItemComposite);
		swtCoolItem.setControl(swtCoolItemComposite);

		assign();
		this.setParentColor(true);
		this.setParentShowHint(true);
		this.setParentFont(true);
	}

	@Override
	public Composite getSwtComposite() {
		return this.swtCoolItemComposite;
	}

	@Override
	public void destroy() {
		super.destroy();
		swtCoolItem.dispose();
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		if (swtCoolItem != null) {
			swtCoolItem.setText(this.getCaption());
		}
	}

	@Override
	public void setTop(Integer top) {
		/* Intentionally nothing */
	}

	@Override
	public void setLeft(Integer left) {
		/* Intentionally nothing */
	}

	@Override
	public void setWidth(Integer width) {
		super.setWidth(width);
		swtCoolItem.setSize(this.getWidth(), this.getHeight());

		coolBar.update();
	}

	@Override
	public void setHeight(Integer height) {
		super.setHeight(height);
		swtCoolItem.setSize(this.getWidth(), this.getHeight());

		coolBar.update();
	}

	public boolean getBreak() {
		return isBreak;
	}

	public void setBreak(boolean isBreak) {
		this.isBreak = isBreak;

		coolBar.update();
	}

	@Override
	public void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;

		if (autoSize) {
			int packHeight = 0;
			for (int index = 0; index < this.getControls().size(); index++) {
				IusCLControl childControl = this.getControls().get(index);
				int controlDown = childControl.getTop() + childControl.getHeight();
				if (packHeight < controlDown) {
					packHeight = controlDown;
				}
			}

			// packHeight = packHeight + 1;
			setHeight(packHeight);
		}
	}
}
