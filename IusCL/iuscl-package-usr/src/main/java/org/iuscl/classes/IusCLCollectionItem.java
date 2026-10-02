/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCollectionItem extends IusCLPersistent {

	@Getter
	IusCLCollection collection = null;

	@Getter
	@Setter
	Object tag = null;

	public IusCLCollectionItem(IusCLCollection collection) {
		this(collection, null);
	}

	public IusCLCollectionItem(IusCLCollection collection, Integer index) {
		this.collection = collection;

		if (index == null) {
			this.collection.getItems().add(this);
		} else {
			this.collection.getItems().add(index, this);
		}
	}

	public void delete() {
		Integer index = collection.indexOf(this);
		collection.delete(index);
	}
}
