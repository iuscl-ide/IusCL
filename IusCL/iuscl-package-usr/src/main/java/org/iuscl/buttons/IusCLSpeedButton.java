/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLGraphic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLSpeedButton extends IusCLCustomButton {

	@Getter
	boolean down = false;

	public IusCLSpeedButton(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Down", IusCLPropertyType.ptBoolean, "false");

		/* TODO GroupIndex */
		// defineProperty("GroupIndex", IusCLPropertyType.ptInteger, "0");

		swtButton = new Button(this.getFormSwtComposite(), SWT.TOGGLE);
		createWnd(swtButton);
	}

	@Override
	protected void create() {
		super.create();

		Integer defaultHeight = getHeight();
		this.getProperty("Width").setDefaultValue(defaultHeight.toString());
		setWidth(defaultHeight);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			if (swtButton.getSelection()) {
				down = true;
				state = IusCLButtonState.bsDown;
			} else {
				down = false;
				state = IusCLButtonState.bsUp;
			}
		}));
	}

	public void setDown(boolean down) {
		this.down = down;
		if (down) {
			swtButton.setSelection(true);
			state = IusCLButtonState.bsDown;
		} else {
			swtButton.setSelection(false);
			state = IusCLButtonState.bsUp;
		}
	}

//	public Integer getGroupIndex() {
//		return groupIndex;
//	}
//
//	public void setGroupIndex(Integer groupIndex) {
//		this.groupIndex = groupIndex;
//	}

	@Override
	public IusCLGraphic getGraphic() {
		loadGraphic();
		return super.getGraphic();
	}
}
