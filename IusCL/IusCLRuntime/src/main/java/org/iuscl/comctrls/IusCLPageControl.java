/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TabFolder;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLParentControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPageControl extends IusCLParentControl {

	TabFolder swtTabFolder = null;

	@Getter
	Integer activePageIndex = 0;
	@Getter
	IusCLImageList images = null;

	@Getter
	@Setter
	IusCLNotifyEvent onChange = null;

	@Getter
	final List<IusCLTabSheet> pages = new ArrayList<>();

	public IusCLPageControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("ActivePageIndex", IusCLPropertyType.ptInteger, "0");
		defineProperty("Images", IusCLPropertyType.ptComponent, "", IusCLImageList.class);

		this.removeProperty("Caption");

		defineProperty("OnChange", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		swtTabFolder = new TabFolder(this.getFormSwtComposite(), SWT.NONE);
		createWnd(swtTabFolder);

		swtTabFolder.setLayout(null);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("193");
		this.setHeight(193);
		this.getProperty("Width").setDefaultValue("289");
		this.setWidth(289);

		swtTabFolder.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			if (pages.isEmpty()) {
				/* SWT inconsistency */
				return;
			}
			activePageIndex = swtTabFolder.getSelectionIndex();
			if (IusCLEvent.isDefinedEvent(onChange)) {
				onChange.invoke(IusCLPageControl.this);
			}
		}));
	}

	@Override
	public void selectSubComponent(IusCLComponent subComponent) {
		setActivePage((IusCLTabSheet) subComponent);
	}

	@Override
	protected void updateBounds(Integer aLeft, Integer aTop, Integer aWidth, Integer aHeight) {
		IusCLSize parentDelta = new IusCLSize(aWidth - width, aHeight - height);

		super.updateBounds(aLeft, aTop, aWidth, aHeight);

		if ((parentDelta.getWidth() != 0) || (parentDelta.getHeight() != 0)) {
			for (int index = 0; index < pages.size(); index++) {
				pages.get(index).doAlignControls(parentDelta);
			}
		}
	}

	public void setActivePageIndex(Integer activePageIndex) {
		this.activePageIndex = activePageIndex;

		swtTabFolder.setSelection(activePageIndex);
	}

	public IusCLTabSheet getActivePage() {
		return pages.get(activePageIndex);
	}

	public void setActivePage(IusCLTabSheet tabSheet) {
		setActivePageIndex(pages.indexOf(tabSheet));
	}

	public Integer getPageCount() {
		return pages.size();
	}

	public void setImages(IusCLImageList images) {
		this.images = images;

		for (int index = 0; index < pages.size(); index++) {
			IusCLTabSheet tabSheet = pages.get(index);
			tabSheet.setImageIndex(tabSheet.getImageIndex());
		}
	}
}
