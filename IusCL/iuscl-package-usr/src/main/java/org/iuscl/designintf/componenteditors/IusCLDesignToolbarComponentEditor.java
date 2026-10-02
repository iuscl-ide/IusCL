/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.comctrls.IusCLToolBar;
import org.iuscl.comctrls.IusCLToolButton;
import org.iuscl.comctrls.IusCLToolButton.IusCLToolButtonStyle;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.menus.IusCLPopupMenu;

public class IusCLDesignToolbarComponentEditor extends IusCLDesignDefaultComponentEditor {

	public IusCLDesignToolbarComponentEditor() {
		setHasAdd(true);
		setHasOrder(true);
	}

	@Override
	public IusCLDesignComponentEditorVerb verbAdd() {
		/* Add a new button */
		IusCLToolBar toolBar = (IusCLToolBar) this.getComponent();
		toolBar.getButtons().add();

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public IusCLDesignComponentEditorVerb verbOrder(int firstIndex, int secondIndex) {
		/* Move button */
		IusCLToolBar toolBar = (IusCLToolBar) this.getComponent();
		IusCLToolButton firstButton = toolBar.getButtons().get(firstIndex);
		IusCLToolButton secondButton = toolBar.getButtons().get(secondIndex);

		String firstCaption = firstButton.getCaption();
		Integer firstImageIndex = firstButton.getImageIndex();
		Integer firstHotImageIndex = firstButton.getHotImageIndex();
		Integer firstDisabledImageIndex = firstButton.getDisabledImageIndex();
		boolean firstEnabled = firstButton.getEnabled();
		IusCLToolButtonStyle firstStyle = firstButton.getStyle();
		IusCLNotifyEvent firstOnClick = firstButton.getOnClick();
		boolean firstDown = firstButton.getDown();
		IusCLPopupMenu firstDropDownMenu = firstButton.getDropDownMenu();

		firstButton.setCaption(secondButton.getCaption());
		firstButton.setImageIndex(secondButton.getImageIndex());
		firstButton.setHotImageIndex(secondButton.getHotImageIndex());
		firstButton.setDisabledImageIndex(secondButton.getDisabledImageIndex());
		firstButton.setEnabled(secondButton.getEnabled());
		firstButton.setStyle(secondButton.getStyle());
		firstButton.setDown(secondButton.getDown());
		firstButton.setDropDownMenu(secondButton.getDropDownMenu());
		firstButton.setOnClick(secondButton.getOnClick());

		secondButton.setCaption(firstCaption);
		secondButton.setImageIndex(firstImageIndex);
		secondButton.setHotImageIndex(firstHotImageIndex);
		secondButton.setDisabledImageIndex(firstDisabledImageIndex);
		secondButton.setEnabled(firstEnabled);
		secondButton.setStyle(firstStyle);
		secondButton.setDown(firstDown);
		secondButton.setDropDownMenu(firstDropDownMenu);
		secondButton.setOnClick(firstOnClick);

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public IusCLDesignComponentEditorVerb executeVerb(int index) {
		IusCLToolBar toolBar = (IusCLToolBar) this.getComponent();

		switch (index) {
		case 0:
			/* Add a new button */
			toolBar.getButtons().add();

			break;
		case 1:
			/* Add a new separator */
			IusCLToolButton toolButton = toolBar.getButtons().add();
			toolButton.setStyle(IusCLToolButtonStyle.tbsSeparator);

			break;
		default:
			break;
		}

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public String getVerb(int index) {
		String verb = null;

		switch (index) {
		case 0:
			verb = "New Button";
			break;
		case 1:
			verb = "New Separator";
			break;
		default:
			break;
		}

		return verb;
	}

	@Override
	public int getVerbCount() {
		return 2;
	}
}
