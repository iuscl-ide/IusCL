/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLCollectionItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLWindowsStatusPanel extends IusCLCollectionItem {

	@Getter
	String text = "";
	@Getter
	Integer width = 50;
	@Getter
	Integer imageIndex = -1;

	IusCLWindowsStatusBar windowsStatusBar = null;

	public IusCLWindowsStatusPanel(IusCLWindowsStatusPanels statusPanels) {
		this(statusPanels, null);
	}

	public IusCLWindowsStatusPanel(IusCLWindowsStatusPanels statusPanels, Integer index) {
		super(statusPanels);

		defineProperty("Text", IusCLPropertyType.ptString, "");
		defineProperty("Width", IusCLPropertyType.ptInteger, "50");
		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");

		windowsStatusBar = statusPanels.getWindowsStatusBar();
		windowsStatusBar.update();
	}

	public IusCLWindowsStatusPanels getWindowsStatusPanels() {
		return (IusCLWindowsStatusPanels) getCollection();
	}

	@Override
	public String getDisplayName() {
		return text;
	}

	public void setText(String text) {
		this.text = text;

		windowsStatusBar.update();
	}

	public void setWidth(Integer width) {
		this.width = width;

		windowsStatusBar.update();
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		windowsStatusBar.update();
	}
}
