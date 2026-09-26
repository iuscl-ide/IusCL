/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.classes.IusCLCollection;
import org.iuscl.classes.IusCLCollectionItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLDesignSelection {

	IusCLComponent designComponent = null;
	IusCLCollection designCollection = null;
	IusCLCollectionItem designCollectionItem = null;

	public IusCLDesignSelection(IusCLComponent designComponent, IusCLCollection designCollection, IusCLCollectionItem designCollectionItem) {
		this.designComponent = designComponent;
		this.designCollection = designCollection;
		this.designCollectionItem = designCollectionItem;
	}

	public IusCLPersistent findPersistent() {
		if (designCollectionItem != null) {
			return designCollectionItem;
		} else if (designCollection != null) {
			return designCollection;
		} else {
			return designComponent;
		}
	}
}
