/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.ImageList;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.internal.win32.TCHAR;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.windows.IusCLSwtWindowsStatusBar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLWindowsStatusBar extends IusCLWinControl {

	IusCLSwtWindowsStatusBar swtStatusBar = null;

	@Getter
	@Setter
	IusCLWindowsStatusPanels panels = null;
	@Getter
	IusCLImageList images = null;

	Integer minSystemHeight = -1;

	public IusCLWindowsStatusBar(IusCLComponent aOwner) {
		super(aOwner);

		panels = new IusCLWindowsStatusPanels(this);
		defineProperty("Panels", IusCLPropertyType.ptCollection, "", false);
		defineProperty("Images", IusCLPropertyType.ptComponent, "", IusCLImageList.class);

		/* Collection */
		this.setSubCollection(panels);
		panels.setPropertyName("Panels");

		swtStatusBar = new IusCLSwtWindowsStatusBar(this.getFormSwtComposite(), SWT.NONE);
		minSystemHeight = swtStatusBar.getSize().y;
		createWnd(swtStatusBar);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue(minSystemHeight.toString());
		setHeight(minSystemHeight);

		this.setAlign(IusCLAlign.alBottom);
		this.getProperty("Align").setDefaultValue("alBottom");
		this.getProperty("Align").setPublished(false);

		this.getAnchors().setTop(false);
		this.getProperty("Anchors.Top").setDefaultValue("false");
		this.getProperty("Anchors.Left").setPublished(false);
		this.getProperty("Anchors.Top").setPublished(false);
		this.getProperty("Anchors.Right").setPublished(false);
		this.getProperty("Anchors.Bottom").setPublished(false);

		this.removeProperty("Caption");

		this.removeProperty("Color");
		this.removeProperty("ParentColor");

		this.getProperties().get("TabStop").setDefaultValue("false");
		this.setTabStop(false);

		this.removeProperty("Constraints.MaxHeight");
		this.removeProperty("Constraints.MaxWidth");
		this.removeProperty("Constraints.MinHeight");
		this.removeProperty("Constraints.MinWidth");

	}

	public void setImages(IusCLImageList images) {
		this.images = images;

		for (int index = 0; index < panels.getCount(); index++) {
			IusCLWindowsStatusPanel statusPanel = panels.get(index);
			statusPanel.setImageIndex(statusPanel.getImageIndex());
		}
	}

	@Override
	public void setHeight(Integer height) {
		if (height < minSystemHeight) {
			height = minSystemHeight;
		}

		this.height = height;

		RECT clientRect = new RECT();
		OS.SendMessage(swtStatusBar.handle, IusCLSwtWindowsStatusBar.SB_GETRECT, 0, clientRect);

		int oldClientHeight = clientRect.bottom - clientRect.top;
		int marginHeight = swtStatusBar.getSize().y - oldClientHeight;

		int newClientHeight = height - marginHeight;

		OS.SendMessage(swtStatusBar.handle, IusCLSwtWindowsStatusBar.SB_SETMINHEIGHT, newClientHeight, 0);
	}

	public void update() {
		if (swtStatusBar != null) {
			int panelsCount = panels.getCount();
			int[] panelsWidths = new int[panelsCount];

			int distanceFromLeft = 0;
			for (int index = 0; index < panelsCount; index++) {
				IusCLWindowsStatusPanel statusPanel = panels.get(index);
				int panelWidth = statusPanel.getWidth();
				if (panelWidth == -1) {
					panelsWidths[index] = -1;
				} else {
					distanceFromLeft = distanceFromLeft + panelWidth;
					panelsWidths[index] = distanceFromLeft;
				}
			}

			OS.SendMessage(swtStatusBar.handle, IusCLSwtWindowsStatusBar.SB_SETPARTS, panelsCount, panelsWidths);

			for (int index = 0; index < panels.getCount(); index++) {
				IusCLWindowsStatusPanel statusPanel = panels.get(index);

				/* Image */
				int panelImageIndex = statusPanel.getImageIndex();

				if (images != null) {
					int hIcon = 0;

					if (panelImageIndex > -1) {
						Image swtImage = images.getAsSizedSwtImage(panelImageIndex);
						if (swtImage != null) {
							ImageList swtImageList = new ImageList(SWT.NONE, 1);
							swtImageList.add(swtImage);
							// hIcon = OS.ImageList_GetIcon(swtImageList.getHandle(1), 0, 0);
							swtImageList.dispose();
						}
					}
					OS.SendMessage(swtStatusBar.handle, IusCLSwtWindowsStatusBar.SB_SETICON, index, hIcon);
				}

				/* Text */
				String panelText = statusPanel.getText();

				// TCHAR lpszText = new TCHAR(OS.CP_ACP, panelText, true);
				TCHAR lpszText = new TCHAR(panelText, true);
				OS.SendMessage(swtStatusBar.handle, IusCLSwtWindowsStatusBar.SB_SETTEXTW,
						OS.MAKEWPARAM(index, IusCLSwtWindowsStatusBar.SBT_NOBORDERS), lpszText);
			}
		}
	}

	@Override
	public void setColor(IusCLColor color) {
		/* Intentionally nothing */
	}

	@Override
	public void setParentColor(boolean parentColor) {
		/* Intentionally nothing */
	}
}
