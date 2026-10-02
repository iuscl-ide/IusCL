/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLGraphicControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.graphics.IusCLColor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPaintBox extends IusCLGraphicControl {

	IusCLNotifyEvent onPaint = null;

	public IusCLPaintBox(IusCLComponent aOwner) {
		super(aOwner);

		this.removeProperty("Caption");

		this.removeProperty("Enabled");

		// this.removeProperty("Font");
		// this.removeProperty("ParentFont");
		// IusCLFont.removeFontProperties(this, "Font");

		defineProperty("OnPaint", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		createWnd(swtCanvas);
	}

	@Override
	protected void paint() {
		if (formIsInDesignMode) {
			drawDesignBox();
			return;
		}

		if (IusCLEvent.isDefinedEvent(onPaint)) {
			onPaint.invoke(IusCLPaintBox.this);
		}
	}

	@Override
	public void setColor(IusCLColor color) {
		super.setColor(color);

		paint();
	}
}
