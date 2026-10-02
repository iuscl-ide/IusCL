/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.menus;

import org.eclipse.swt.widgets.Menu;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.comctrls.IusCLImageList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLMenu extends IusCLComponent {

	@Getter
	@Setter
	protected Menu swtMenu = null;

	@Getter
	IusCLImageList images = null;

	public IusCLMenu(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Images", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
	}

	public Integer getItemCount() {
		return this.getChildren().size();
	}

	public IusCLMenuItem getItem(Integer index) {
		return (IusCLMenuItem) this.getChildren().get(index);
	}

	public void setImages(IusCLImageList images) {
		this.images = images;

		for (int index = 0; index < this.getItemCount(); index++) {
			IusCLMenuItem menuItem = getItem(index);
			menuItem.setImageIndex(menuItem.getImageIndex());
			recursivePutImageIndex(menuItem);
		}
	}

	private void recursivePutImageIndex(IusCLMenuItem menuItem) {
		for (int index = 0; index < menuItem.getItemCount(); index++) {
			IusCLMenuItem subMenuItem = menuItem.getItem(index);
			subMenuItem.setImageIndex(subMenuItem.getImageIndex());
			recursivePutImageIndex(subMenuItem);
		}
	}
}
