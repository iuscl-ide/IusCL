/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.comctrls.IusCLListColumn;
import org.iuscl.comctrls.IusCLListColumn.IusCLListColumnAlignment;
import org.iuscl.comctrls.IusCLListView;

public class IusCLDesignListViewComponentEditor extends IusCLDesignDefaultComponentEditor {

	public IusCLDesignListViewComponentEditor() {
		setHasAdd(true);
		setHasOrder(true);
	}

	@Override
	public IusCLDesignComponentEditorVerb verbAdd() {
		/* Add a new column */
		IusCLListView listView = (IusCLListView) this.getComponent();
		listView.getColumns().add();

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public IusCLDesignComponentEditorVerb verbOrder(int firstIndex, int secondIndex) {
		/* Move column */
		IusCLListView listView = (IusCLListView) this.getComponent();
		IusCLListColumn firstColumn = listView.getColumns().get(firstIndex);
		IusCLListColumn secondColumn = listView.getColumns().get(secondIndex);

		String firstCaption = firstColumn.getCaption();
		Integer firstImageIndex = firstColumn.getImageIndex();
		Integer firstWidth = firstColumn.getWidth();
		IusCLListColumnAlignment firstColumnAlignment = firstColumn.getAlignment();
		boolean firstAllowResize = firstColumn.getAllowResize();

		firstColumn.setCaption(secondColumn.getCaption());
		firstColumn.setImageIndex(secondColumn.getImageIndex());
		firstColumn.setWidth(secondColumn.getWidth());
		firstColumn.setAlignment(secondColumn.getAlignment());
		firstColumn.setAllowResize(secondColumn.getAllowResize());

		secondColumn.setCaption(firstCaption);
		secondColumn.setImageIndex(firstImageIndex);
		secondColumn.setWidth(firstWidth);
		secondColumn.setAlignment(firstColumnAlignment);
		secondColumn.setAllowResize(firstAllowResize);

		return getVerbSerializeAndBroadcastChange();
	}
}
