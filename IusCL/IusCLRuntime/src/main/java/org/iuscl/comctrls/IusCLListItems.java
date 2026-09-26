/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLCollection;

public class IusCLListItems extends IusCLCollection {

	public IusCLListItems(IusCLListView listView) {
		super(listView);
	}

	@Override
	public IusCLListView getPropertyComponent() {
		return (IusCLListView) super.getPropertyComponent();
	}

	public IusCLListView getListView() {
		return getPropertyComponent();
	}

	@Override
	public Class<?> getItemClass() {
		return IusCLListItem.class;
	}

	@Override
	public IusCLListItem add() {
		return new IusCLListItem(this);
	}

	@Override
	public IusCLListItem insert(int index) {
		return new IusCLListItem(this, index);
	}

	@Override
	public IusCLListItem get(int index) {
		return (IusCLListItem) super.get(index);
	}

	public Integer indexOf(IusCLListItem listItem) {
		return super.indexOf(listItem);
	}
}
