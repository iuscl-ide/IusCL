/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLParentControl;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTabSheet extends IusCLContainerControl {

	TabItem swtTabItem = null;
	Composite swtTabItemComposite = null;

	@Getter
	Integer imageIndex = -1;

	IusCLPageControl pageControl = null;

	public IusCLTabSheet(IusCLComponent aOwner) {
		super(aOwner);
		setIsSubComponent(true);

		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");

		this.removeProperty("Left");
		this.removeProperty("Top");

		/* TODO Delete, hide/show with events, align controls inside */
	}

	@Override
	public String getDisplayName() {
		String displayName = super.getDisplayName();
		String tabCaption = this.getCaption();

		if (IusCLStrUtils.isNotNullNotEmpty(tabCaption)) {
			displayName = displayName + " { " + tabCaption + " } ";
		}

		return displayName;
	}

	@Override
	public void setParent(IusCLParentControl parentControl) {
		super.setParent(parentControl);

		pageControl = (IusCLPageControl) parentControl;
		TabFolder swtTabFolder = (TabFolder) pageControl.getSwtControl();
		swtTabItem = new TabItem(swtTabFolder, SWT.PUSH);

		swtTabItemComposite = new Composite((TabFolder) (pageControl.getSwtControl()), SWT.NONE);
		createWnd(swtTabItemComposite);

		this.setParentColor(true);
		this.setParentShowHint(true);
		this.setParentFont(true);

		swtTabItem.setControl(swtTabItemComposite);
		swtTabFolder.setSelection(swtTabItem);
		swtTabFolder.setSelection(pageControl.getActivePageIndex());

		pageControl.getPages().add(this);
	}

	@Override
	public Composite getSwtComposite() {
		return this.swtTabItemComposite;
	}

	@Override
	public void destroy() {
		super.destroy();
		swtTabItem.dispose();
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		if (swtTabItem != null) {
			swtTabItem.setText(this.getCaption());
		}
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (swtTabItem == null) {
			return;
		}

		if (imageIndex == -1) {
			swtTabItem.setImage(null);
			return;
		}

		IusCLImageList images = pageControl.getImages();
		if (images != null) {
			Image swtImage = images.getAsSizedSwtImage(imageIndex);
			swtTabItem.setImage(swtImage);
		} else {
			swtTabItem.setImage(null);
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
}
