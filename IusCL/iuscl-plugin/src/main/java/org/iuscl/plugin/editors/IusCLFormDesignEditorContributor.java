/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLFormDesignEditorContributor extends MultiPageEditorActionBarContributor {

	IEditorPart activeEditorPart;

	@Getter
	private static StatusLineContributionItem statusField;

	public IusCLFormDesignEditorContributor() {
		super();
	}

	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part) {
			return;
		}

		activeEditorPart = part;
	}

	@Override
	public void contributeToMenu(IMenuManager manager) {
		/*  */
	}

	@Override
	public void contributeToToolBar(IToolBarManager manager) {
		/*  */
	}

	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		statusField = new StatusLineContributionItem("control", 80);
		statusField.setText("");
		statusLineManager.add(statusField);

		super.contributeToStatusLine(statusLineManager);
	}
}
