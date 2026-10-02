/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCollection extends IusCLPersistent {

	@Getter
	IusCLComponent propertyComponent = null;
	@Getter
	@Setter
	String propertyName = null;
	@Getter
	final List<IusCLCollectionItem> items = new ArrayList<>();

	public IusCLCollection(IusCLComponent propertyComponent) {
		super();
		this.propertyComponent = propertyComponent;
	}

	public IusCLCollectionItem add() {
		return new IusCLCollectionItem(this);
	}

	public IusCLCollectionItem insert(int index) {
		return new IusCLCollectionItem(this, index);
	}

	public void delete(int index) {
		IusCLCollectionItem collectionItem = items.get(index);
		collectionItem.free();
		items.remove(index);
	}

	public IusCLCollectionItem get(int index) {
		return items.get(index);
	}

	public Integer indexOf(IusCLCollectionItem item) {
		return items.indexOf(item);
	}

	public Integer getCount() {
		return items.size();
	}

	public Class<?> getItemClass() {
		return IusCLCollectionItem.class;
	}
}
