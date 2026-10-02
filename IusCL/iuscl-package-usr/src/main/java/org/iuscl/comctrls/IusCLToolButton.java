/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.iuscl.classes.IusCLCollectionItem;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.menus.IusCLPopupMenu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLToolButton extends IusCLCollectionItem {

	public enum IusCLToolButtonStyle {
		tbsButton, tbsCheck, tbsDropDown, tbsSeparator, tbsRadio
	}

	@Getter
	@Setter
	ToolItem swtToolItem = null;

	@Getter
	String caption = "";
	@Getter
	boolean enabled = true;
	@Getter
	Integer imageIndex = -1;
	@Getter
	Integer hotImageIndex = -1;
	@Getter
	Integer disabledImageIndex = -1;
	@Getter
	IusCLToolButtonStyle style = IusCLToolButtonStyle.tbsButton;
	@Getter
	boolean down = false;
	@Getter
	@Setter
	IusCLPopupMenu dropDownMenu = null;

	@Getter
	@Setter
	IusCLNotifyEvent onClick = null;

	public IusCLToolButton(IusCLToolButtons toolButtons) {
		this(toolButtons, null);
	}

	public IusCLToolButton(IusCLToolButtons toolButtons, Integer index) {
		super(toolButtons);

		defineProperty("Caption", IusCLPropertyType.ptString, "");
		defineProperty("Enabled", IusCLPropertyType.ptBoolean, "true");
		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("HotImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("DisabledImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Style", IusCLPropertyType.ptEnum, "tbsButton", IusCLToolButtonStyle.tbsButton);
		defineProperty("Down", IusCLPropertyType.ptBoolean, "false");
		defineProperty("DropDownMenu", IusCLPropertyType.ptComponent, "", IusCLPopupMenu.class);

		defineProperty("OnClick", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		IusCLToolBar toolBar = toolButtons.getToolBar();
		ToolBar swtToolBar = (ToolBar) toolBar.getSwtControl();

		if (index == null) {
			swtToolItem = new ToolItem(swtToolBar, getCreateParams());
		} else {
			swtToolItem = new ToolItem(swtToolBar, getCreateParams(), index);
		}

		reCreate();
		assign();
		toolBar.doAutoSizeParentAlignControls();
	}

	private void reCreate() {
		swtToolItem.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			if (swtToolItem.getSelection()) {
				down = true;
			} else {
				down = false;
			}
			if (swtSelectionEvent.detail == SWT.ARROW && dropDownMenu != null) {
				Rectangle swtRectangle = swtToolItem.getBounds();
				Point swtPoint = swtToolItem.getParent().toDisplay(new Point(swtRectangle.x, swtRectangle.y));
				dropDownMenu.popup(swtPoint.x, swtPoint.y + swtRectangle.height);
			}
			if (IusCLEvent.isDefinedEvent(onClick)) {
				onClick.invoke(IusCLToolButton.this);
			}
		}));
	}

	public IusCLToolButtons getToolButtons() {
		return (IusCLToolButtons) getCollection();
	}

	@Override
	public void free() {
		super.free();

		if (swtToolItem != null) {
			swtToolItem.dispose();
		}
	}

	private int getCreateParams() {
		int swtStyle = SWT.PUSH;

		switch (style) {
		case tbsCheck:
			swtStyle = SWT.CHECK;
			break;
		case tbsRadio:
			swtStyle = SWT.RADIO;
			break;
		case tbsDropDown:
			swtStyle = SWT.DROP_DOWN;
			break;
		case tbsSeparator:
			swtStyle = SWT.SEPARATOR;
			break;
		default:
			/* Intentionally left blank */
			break;
		}

		return swtStyle;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		swtToolItem.setEnabled(enabled);
	}

	@Override
	public String getDisplayName() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;

		if (swtToolItem != null) {
			swtToolItem.setText(caption);
		}
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (imageIndex == -1) {
			swtToolItem.setImage(null);
			return;
		}

		IusCLToolButtons toolButtons = getToolButtons();
		IusCLToolBar toolBar = toolButtons.getToolBar();

		if (swtToolItem != null) {
			IusCLImageList images = toolBar.getImages();
			if (images != null) {
				Image swtImage = images.getAsSizedSwtImage(imageIndex);
				swtToolItem.setImage(swtImage);
			} else {
				swtToolItem.setImage(null);
			}
		}

		toolBar.doAutoSizeParentAlignControls();
	}

	public void setHotImageIndex(Integer hotImageIndex) {
		this.hotImageIndex = hotImageIndex;

		if (hotImageIndex == -1) {
			swtToolItem.setHotImage(null);
			return;
		}

		IusCLToolButtons toolButtons = getToolButtons();
		IusCLToolBar toolBar = toolButtons.getToolBar();

		if (swtToolItem != null) {
			IusCLImageList hotImages = toolBar.getHotImages();
			if (hotImages != null) {
				Image swtImage = hotImages.getAsSizedSwtImage(hotImageIndex);
				swtToolItem.setHotImage(swtImage);
			} else {
				swtToolItem.setHotImage(null);
			}
		}

		toolBar.doAutoSizeParentAlignControls();
	}

	public void setDisabledImageIndex(Integer disabledImageIndex) {
		this.disabledImageIndex = disabledImageIndex;

		if (disabledImageIndex == -1) {
			swtToolItem.setDisabledImage(null);
			return;
		}

		IusCLToolButtons toolButtons = getToolButtons();
		IusCLToolBar toolBar = toolButtons.getToolBar();

		if (swtToolItem != null) {
			IusCLImageList disabledImages = toolBar.getDisabledImages();
			if (disabledImages != null) {
				Image swtImage = disabledImages.getAsSizedSwtImage(disabledImageIndex);
				swtToolItem.setDisabledImage(swtImage);
			} else {
				swtToolItem.setDisabledImage(null);
			}
		}

		toolBar.doAutoSizeParentAlignControls();
	}

	public void setStyle(IusCLToolButtonStyle style) {
		if (this.style != style) {
			this.style = style;

			IusCLToolButtons toolButtons = getToolButtons();
			IusCLToolBar toolBar = toolButtons.getToolBar();
			ToolBar swtToolBar = (ToolBar) toolBar.getSwtControl();

			int index = swtToolBar.indexOf(swtToolItem);
			swtToolItem.dispose();

			swtToolItem = new ToolItem(swtToolBar, getCreateParams(), index);

			reCreate();
			assign();

			toolBar.doAutoSizeParentAlignControls();
		}
	}

	public void setDown(boolean down) {
		this.down = down;

		swtToolItem.setSelection(down);
	}
}
