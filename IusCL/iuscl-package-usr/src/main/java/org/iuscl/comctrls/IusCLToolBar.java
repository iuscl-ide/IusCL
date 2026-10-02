/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLToolBar extends IusCLWinControl {

	ToolBar swtToolBar = null;

	@Getter
	@Setter
	IusCLToolButtons buttons = null;
	@Getter
	IusCLImageList images = null;
	@Getter
	IusCLImageList hotImages = null;
	@Getter
	IusCLImageList disabledImages = null;

	public IusCLToolBar(IusCLComponent aOwner) {
		super(aOwner);

		buttons = new IusCLToolButtons(this);
		defineProperty("Buttons", IusCLPropertyType.ptCollection, "", false);
		defineProperty("Images", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
		defineProperty("HotImages", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
		defineProperty("DisabledImages", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
		defineProperty("AutoSize", IusCLPropertyType.ptBoolean, "true");

		/* Collection */
		this.setSubCollection(buttons);
		buttons.setPropertyName("Buttons");

		swtToolBar = new ToolBar(this.getFormSwtComposite(), SWT.WRAP);
		createWnd(swtToolBar);
	}

	@Override
	protected void create() {
		super.create();

		this.removeProperty("Caption");

		this.setAutoSize(true);
	}

	//
	// @Override
	// public Composite getSwtComposite() {
	// return swtToolBar;
	// }

	public void setImages(IusCLImageList images) {
		this.images = images;

		for (int index = 0; index < buttons.getCount(); index++) {
			IusCLToolButton toolButton = buttons.get(index);
			toolButton.setImageIndex(toolButton.getImageIndex());
		}
	}

	public void setHotImages(IusCLImageList hotImages) {
		this.hotImages = hotImages;

		for (int index = 0; index < buttons.getCount(); index++) {
			IusCLToolButton toolButton = buttons.get(index);
			toolButton.setHotImageIndex(toolButton.getHotImageIndex());
		}
	}

	public void setDisabledImages(IusCLImageList disabledImages) {
		this.disabledImages = disabledImages;

		for (int index = 0; index < buttons.getCount(); index++) {
			IusCLToolButton toolButton = buttons.get(index);
			toolButton.setDisabledImageIndex(toolButton.getDisabledImageIndex());
		}
	}

//	@Override
//	public boolean getAutoSize() {
//		return super.getAutoSize();
//	}

	@Override
	public void setAutoSize(boolean autoSize) {
		super.setAutoSize(autoSize);
	}

	@Override
	public void doAutoSizeParentAlignControls() {
		super.doAutoSizeParentAlignControls();
	}
}
