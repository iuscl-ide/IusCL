/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.comctrls.IusCLWindowsStatusBar;
import org.iuscl.comctrls.IusCLWindowsStatusPanel;

public class IusCLDesignStatusBarComponentEditor extends IusCLDesignDefaultComponentEditor {

	public IusCLDesignStatusBarComponentEditor() {
		setHasAdd(true);
		setHasOrder(true);
	}

	@Override
	public IusCLDesignComponentEditorVerb verbAdd() {
		/* Add a new panel */
		IusCLWindowsStatusBar statusBar = (IusCLWindowsStatusBar) this.getComponent();
		statusBar.getPanels().add();

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public IusCLDesignComponentEditorVerb verbOrder(int firstIndex, int secondIndex) {
		/* Move order */
		IusCLWindowsStatusBar statusBar = (IusCLWindowsStatusBar) this.getComponent();
		IusCLWindowsStatusPanel firstStatusPanel = statusBar.getPanels().get(firstIndex);
		IusCLWindowsStatusPanel secondStatusPanel = statusBar.getPanels().get(secondIndex);

		String firstText = firstStatusPanel.getText();
		Integer firstImageIndex = firstStatusPanel.getImageIndex();
		Integer firstWidth = firstStatusPanel.getWidth();

		firstStatusPanel.setText(secondStatusPanel.getText());
		firstStatusPanel.setImageIndex(secondStatusPanel.getImageIndex());
		firstStatusPanel.setWidth(secondStatusPanel.getWidth());

		secondStatusPanel.setText(firstText);
		secondStatusPanel.setImageIndex(firstImageIndex);
		secondStatusPanel.setWidth(firstWidth);

		return getVerbSerializeAndBroadcastChange();
	}
}
