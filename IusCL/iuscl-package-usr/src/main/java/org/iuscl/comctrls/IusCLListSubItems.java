/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;
import org.iuscl.classes.IusCLStrings;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListSubItems extends IusCLStrings {

	final List<Integer> imageIndices = new ArrayList<>();

	IusCLListItem listItem = null;

	public IusCLListSubItems(IusCLListItem listItem) {
		super();

		this.listItem = listItem;
	}

	public Integer getImageIndex(int index) {
		return imageIndices.get(index);
	}

	public void setImageIndex(int index, int imageIndex) {
		imageIndices.set(index, imageIndex);

		Integer col = index + 1;

		TableItem swtTableItem = listItem.getSwtTableItem();

		if (imageIndex == -1) {
			swtTableItem.setImage(col, (Image) null);
			return;
		}

		IusCLImageList images = listItem.getListItems().getListView().getSmallImages();
		if (images != null) {
			Image swtImage = images.getAsSizedSwtImage(imageIndex);
			swtTableItem.setImage(col, swtImage);
		} else {
			swtTableItem.setImage(col, (Image) null);
		}
	}

	public String getCaption(int index) {
		return get(index);
	}

	public void setCaption(int index, String caption) {
		set(index, caption);

		listItem.getSwtTableItem().setText(index + 1, caption);
	}

	@Override
	public void add(String caption) {
		super.add(caption);

		imageIndices.add(-1);

		listItem.getSwtTableItem().setText(this.size(), caption);
	}

	@Override
	public void insert(Integer index, String caption) {
		super.insert(index, caption);

		imageIndices.add(index, -1);

		for (int col = index; col < this.size(); col++) {
			listItem.getSwtTableItem().setText(col + 1, this.get(col));
		}
	}

	@Override
	public void delete(Integer index) {
		super.delete(index);

		for (int col = 0; col < this.size(); col++) {
			listItem.getSwtTableItem().setText(col + 1, this.get(col));
		}
		listItem.getSwtTableItem().setText(this.size(), "");
	}
}
