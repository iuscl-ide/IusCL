/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.iuscl.classes.IusCLCollectionItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListItem extends IusCLCollectionItem {

	@Getter
	@Setter
	TableItem swtTableItem = null;

	@Getter
	String caption = "";
	@Getter
	Integer imageIndex = -1;
	@Getter
	IusCLListSubItems subItems = null;

	public IusCLListItem(IusCLListItems listItems) {
		this(listItems, null);
	}

	public IusCLListItem(IusCLListItems listItems, Integer index) {
		super(listItems, index);

		defineProperty("Caption", IusCLPropertyType.ptString, "");
		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");

		IusCLListView listView = listItems.getPropertyComponent();
		Table swtTable = (Table) listView.getSwtControl();

		subItems = new IusCLListSubItems(this);

		if (index == null) {
			swtTableItem = new TableItem(swtTable, SWT.NONE);
		} else {
			swtTableItem = new TableItem(swtTable, SWT.NONE, index);
		}
		assign();
	}

	public IusCLListItems getListItems() {
		return (IusCLListItems) getCollection();
	}

	@Override
	public void free() {
		super.free();

		if (swtTableItem != null) {
			swtTableItem.dispose();
		}
	}

	@Override
	public String getDisplayName() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;

		swtTableItem.setText(caption);
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (imageIndex == -1) {
			swtTableItem.setImage((Image) null);
			return;
		}

		IusCLListItems listItems = getListItems();
		IusCLListView listView = listItems.getPropertyComponent();

		if (swtTableItem != null) {
			IusCLImageList images = listView.getSmallImages();
			if (images != null) {
				Image swtImage = images.getAsSizedSwtImage(imageIndex);
				swtTableItem.setImage(swtImage);
			} else {
				swtTableItem.setImage((Image) null);
			}
		}
	}
}
