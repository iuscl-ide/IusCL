/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.packages;

import java.util.EnumSet;

import org.iuscl.buttons.IusCLBitButton;
import org.iuscl.buttons.IusCLSpeedButton;
import org.iuscl.classes.IusCLPersistent.IusCLPropertyType;
import org.iuscl.comctrls.IusCLCoolBar;
import org.iuscl.comctrls.IusCLDateTimePicker;
import org.iuscl.comctrls.IusCLImageList;
import org.iuscl.comctrls.IusCLListView;
import org.iuscl.comctrls.IusCLMonthCalendar;
import org.iuscl.comctrls.IusCLPageControl;
import org.iuscl.comctrls.IusCLProgressBar;
import org.iuscl.comctrls.IusCLRichEdit;
import org.iuscl.comctrls.IusCLToolBar;
import org.iuscl.comctrls.IusCLTrackBar;
import org.iuscl.comctrls.IusCLTreeNode;
import org.iuscl.comctrls.IusCLTreeView;
import org.iuscl.comctrls.IusCLUpDown;
import org.iuscl.comctrls.IusCLWindowsStatusBar;
import org.iuscl.controls.IusCLControl.IusCLMouseButton;
import org.iuscl.controls.IusCLControl.IusCLShiftState;
import org.iuscl.controls.IusCLKeyboardKey;
import org.iuscl.controls.IusCLSizeConstraints;
import org.iuscl.designintf.componenteditors.IusCLDesignCoolBarComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignImageListComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignListViewComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignMenuComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignMenuItemComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignPageControlComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignStatusBarComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignToolbarComponentEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignBooleanPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignColorPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignEnumPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignEventPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignFilterPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignFontNamePropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignFontPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignIconPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignIntegerPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignPicturePropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignRefComponentPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignStringListPropertyEditor;
import org.iuscl.designintf.propertyeditors.IusCLDesignStringPropertyEditor;
import org.iuscl.dialogs.IusCLColorDialog;
import org.iuscl.dialogs.IusCLFolderDialog;
import org.iuscl.dialogs.IusCLFontDialog;
import org.iuscl.dialogs.IusCLOpenDialog;
import org.iuscl.dialogs.IusCLOpenPictureDialog;
import org.iuscl.dialogs.IusCLSaveDialog;
import org.iuscl.dialogs.IusCLSavePictureDialog;
import org.iuscl.extctrls.IusCLBevel;
import org.iuscl.extctrls.IusCLImage;
import org.iuscl.extctrls.IusCLPaintBox;
import org.iuscl.extctrls.IusCLPanel;
import org.iuscl.extctrls.IusCLRadioGroup;
import org.iuscl.extctrls.IusCLShape;
import org.iuscl.extctrls.IusCLSplitter;
import org.iuscl.extctrls.IusCLTrayIcon;
import org.iuscl.forms.IusCLScrollBox;
import org.iuscl.menus.IusCLMainMenu;
import org.iuscl.menus.IusCLMenuItem;
import org.iuscl.menus.IusCLPopupMenu;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.stdctrls.IusCLCheckBox;
import org.iuscl.stdctrls.IusCLComboBox;
import org.iuscl.stdctrls.IusCLEdit;
import org.iuscl.stdctrls.IusCLGroupBox;
import org.iuscl.stdctrls.IusCLLabel;
import org.iuscl.stdctrls.IusCLListBox;
import org.iuscl.stdctrls.IusCLMemo;
import org.iuscl.stdctrls.IusCLPasswordEdit;
import org.iuscl.stdctrls.IusCLRadioButton;
import org.iuscl.stdctrls.IusCLScrollBar;
import org.iuscl.stdctrls.IusCLScrollBar.IusCLScrollCode;
import org.iuscl.stdctrls.IusCLStaticText;
import org.iuscl.sysctrls.IusCLApplicationEvents;
import org.iuscl.sysctrls.IusCLScheduler;
import org.iuscl.sysctrls.IusCLTimer;
import org.iuscl.sysctrls.IusCLUnzip;
import org.iuscl.sysctrls.IusCLZip;
import org.iuscl.system.IusCLObject;
import org.iuscl.types.IusCLSize;

public class IusCLDesignPackageUsr extends IusCLDesignPackage {

	public IusCLDesignPackageUsr() {
		super();

		String res = "resources/designintf/images/";

		/* Components */

		/* Standard */
		defineDesignComponentInfo(IusCLMainMenu.class.getCanonicalName(), "IusCLMainMenu", pnStandard, res + "IusCLDesignMainMenu.png", "Menu",
				IusCLDesignMenuComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLPopupMenu.class.getCanonicalName(), "IusCLPopupMenu", pnStandard, res + "IusCLDesignPopupMenu.png", "Popup",
				IusCLDesignMenuComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLLabel.class.getCanonicalName(), "IusCLLabel", pnStandard, res + "IusCLDesignLabel.png", "Label");

		defineDesignComponentInfo(IusCLEdit.class.getCanonicalName(), "IusCLEdit", pnStandard, res + "IusCLDesignEdit.png", "Edit");

		defineDesignComponentInfo(IusCLMemo.class.getCanonicalName(), "IusCLMemo", pnStandard, res + "IusCLDesignMemo.png", "Memo");

		defineDesignComponentInfo(IusCLButton.class.getCanonicalName(), "IusCLButton", pnStandard, res + "IusCLDesignButton.png", "Button");

		defineDesignComponentInfo(IusCLCheckBox.class.getCanonicalName(), "IusCLCheckBox", pnStandard, res + "IusCLDesignCheckBox.png", "Check");

		defineDesignComponentInfo(IusCLRadioButton.class.getCanonicalName(), "IusCLRadioButton", pnStandard, res + "IusCLDesignRadioButton.png",
				"Radio");

		defineDesignComponentInfo(IusCLListBox.class.getCanonicalName(), "IusCLListBox", pnStandard, res + "IusCLDesignListBox.png", "ListBox");

		defineDesignComponentInfo(IusCLComboBox.class.getCanonicalName(), "IusCLComboBox", pnStandard, res + "IusCLDesignComboBox.png", "Comb.");

		defineDesignComponentInfo(IusCLScrollBar.class.getCanonicalName(), "IusCLScrollBar", pnStandard, res + "IusCLDesignScrollBar.png", "SBar");

		defineDesignComponentInfo(IusCLGroupBox.class.getCanonicalName(), "IusCLGroupBox", pnStandard, res + "IusCLDesignGroupBox.png", "Group");

		defineDesignComponentInfo(IusCLRadioGroup.class.getCanonicalName(), "IusCLRadioGroup", pnStandard, res + "IusCLDesignRadioGroup.png",
				"Radios");

		defineDesignComponentInfo(IusCLPanel.class.getCanonicalName(), "IusCLPanel", pnStandard, res + "IusCLDesignPanel.png", "Panel");

		/* Additional */
		defineDesignComponentInfo(IusCLBitButton.class.getCanonicalName(), "IusCLBitButton", pnAdditional, res + "IusCLDesignBitButton.png",
				"BitBtn");

		defineDesignComponentInfo(IusCLSpeedButton.class.getCanonicalName(), "IusCLSpeedButton", pnAdditional, res + "IusCLDesignSpeedButton.png",
				"SBtn");

//		defineDesignComponentInfo(IusCLMaskEdit.class.getCanonicalName(),
//				"IusCLMaskEdit", pnAdditional, res + "IusCLDesignMaskEdit.png", "Mask");

		defineDesignComponentInfo(IusCLImage.class.getCanonicalName(), "IusCLImage", pnAdditional, res + "IusCLDesignImage.png", "Image");

		defineDesignComponentInfo(IusCLShape.class.getCanonicalName(), "IusCLShape", pnAdditional, res + "IusCLDesignShape.png", "Shape");

		defineDesignComponentInfo(IusCLBevel.class.getCanonicalName(), "IusCLBevel", pnAdditional, res + "IusCLDesignBevel.png", "Bevel");

		defineDesignComponentInfo(IusCLScrollBox.class.getCanonicalName(), "IusCLScrollBox", pnAdditional, res + "IusCLDesignScrollBox.png", "SBox");

		defineDesignComponentInfo(IusCLSplitter.class.getCanonicalName(), "IusCLSplitter", pnAdditional, res + "IusCLDesignSplitter.png", "Splitter");

		defineDesignComponentInfo(IusCLStaticText.class.getCanonicalName(), "IusCLStaticText", pnAdditional, res + "IusCLDesignStaticText.png",
				"SText");

		defineDesignComponentInfo(IusCLPasswordEdit.class.getCanonicalName(), "IusCLPasswordEdit", pnAdditional, res + "IusCLDesignPasswordEdit.png",
				"Pass");

//		defineDesignComponentInfo(IusCLControlBar.class.getCanonicalName(),
//				"IusCLControlBar", pnAdditional, res + "IusCLDesignControlBar.png", "CtrlBar");

		/* Win32 */
//		defineDesignComponentInfo(IusCLTabControl.class.getCanonicalName(),
//				"IusCLTabControl", pnWindows, res + "IusCLDesignTabControl.png", "Tab");

		defineDesignComponentInfo(IusCLPageControl.class.getCanonicalName(), "IusCLPageControl", pnWindows, res + "IusCLDesignPageControl.png",
				"Page", IusCLDesignPageControlComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLImageList.class.getCanonicalName(), "IusCLImageList", pnWindows, res + "IusCLDesignImageList.png", "Imgs",
				IusCLDesignImageListComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLRichEdit.class.getCanonicalName(), "IusCLRichEdit", pnWindows, res + "IusCLDesignRichEdit.png", "Rich");

		defineDesignComponentInfo(IusCLTrackBar.class.getCanonicalName(), "IusCLTrackBar", pnWindows, res + "IusCLDesignTrackBar.png", "Track");

		defineDesignComponentInfo(IusCLProgressBar.class.getCanonicalName(), "IusCLProgressBar", pnWindows, res + "IusCLDesignProgressBar.png",
				"PBar");

		defineDesignComponentInfo(IusCLUpDown.class.getCanonicalName(), "IusCLUpDown", pnWindows, res + "IusCLDesignUpDown.png", "Spin");

		defineDesignComponentInfo(IusCLDateTimePicker.class.getCanonicalName(), "IusCLDateTimePicker", pnWindows,
				res + "IusCLDesignDateTimePicker.png", "Date");

		defineDesignComponentInfo(IusCLMonthCalendar.class.getCanonicalName(), "IusCLMonthCalendar", pnWindows, res + "IusCLDesignMonthCalendar.png",
				"Month");

		defineDesignComponentInfo(IusCLTreeView.class.getCanonicalName(), "IusCLTreeView", pnWindows, res + "IusCLDesignTreeView.png", "Tree");

		defineDesignComponentInfo(IusCLListView.class.getCanonicalName(), "IusCLListView", pnWindows, res + "IusCLDesignListView.png", "List",
				IusCLDesignListViewComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLWindowsStatusBar.class.getCanonicalName(), "IusCLStatusBar", pnWindows, res + "IusCLDesignStatusBar.png",
				"Status", IusCLDesignStatusBarComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLToolBar.class.getCanonicalName(), "IusCLToolBar", pnWindows, res + "IusCLDesignToolBar.png", "TBar",
				IusCLDesignToolbarComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLCoolBar.class.getCanonicalName(), "IusCLCoolBar", pnWindows, res + "IusCLDesignCoolBar.png", "CBar",
				IusCLDesignCoolBarComponentEditor.class.getCanonicalName());

		defineDesignComponentInfo(IusCLTrayIcon.class.getCanonicalName(), "IusCLTrayIcon", pnWindows, res + "IusCLDesignTrayIcon.png", "Tray");

//		defineDesignComponentInfo(IusCLPageScroller.class.getCanonicalName(),
//				"IusCLPageScroller", pnWindows, res + "IusCLDesignPageScroller.png", "Scroll");

		/* System */
		defineDesignComponentInfo(IusCLApplicationEvents.class.getCanonicalName(), "IusCLApplicationEvents", pnSystem,
				res + "IusCLDesignApplicationEvents.png", "AppEv");

		defineDesignComponentInfo(IusCLTimer.class.getCanonicalName(), "IusCLTimer", pnSystem, res + "IusCLDesignTimer.png", "Timer");

		defineDesignComponentInfo(IusCLScheduler.class.getCanonicalName(), "IusCLScheduler", pnSystem, res + "IusCLDesignScheduler.png", "Sched.");

		defineDesignComponentInfo(IusCLPaintBox.class.getCanonicalName(), "IusCLPaintBox", pnSystem, res + "IusCLDesignPaintBox.png", "PBox");

		defineDesignComponentInfo(IusCLZip.class.getCanonicalName(), "IusCLZip", pnSystem, res + "IusCLDesignZip.png", "Zip");

		defineDesignComponentInfo(IusCLUnzip.class.getCanonicalName(), "IusCLUnzip", pnSystem, res + "IusCLDesignUnzip.png", "Unzip");

		/* Dialogs */
		defineDesignComponentInfo(IusCLOpenDialog.class.getCanonicalName(), "IusCLOpenDialog", pnDialogs, res + "IusCLDesignOpenDialog.png", "Open");

		defineDesignComponentInfo(IusCLSaveDialog.class.getCanonicalName(), "IusCLSaveDialog", pnDialogs, res + "IusCLDesignSaveDialog.png", "Save");

		defineDesignComponentInfo(IusCLOpenPictureDialog.class.getCanonicalName(), "IusCLOpenPictureDialog", pnDialogs,
				res + "IusCLDesignOpenPictureDialog.png", "OPic");

		defineDesignComponentInfo(IusCLSavePictureDialog.class.getCanonicalName(), "IusCLSavePictureDialog", pnDialogs,
				res + "IusCLDesignSavePictureDialog.png", "SPic");

		defineDesignComponentInfo(IusCLFolderDialog.class.getCanonicalName(), "IusCLFolderDialog", pnDialogs, res + "IusCLDesignFolderDialog.png",
				"Folder");

		defineDesignComponentInfo(IusCLFontDialog.class.getCanonicalName(), "IusCLFontDialog", pnDialogs, res + "IusCLDesignFontDialog.png", "Font");

		defineDesignComponentInfo(IusCLColorDialog.class.getCanonicalName(), "IusCLColorDialog", pnDialogs, res + "IusCLDesignColorDialog.png",
				"Color");

//		defineDesignComponentInfo("IusCLFindDialog.class.getCanonicalName()",
//				"IusCLFindDialog", pnDialogs, res + "IusCLDesignFindDialog.png", "Find");
//	
//		defineDesignComponentInfo("IusCLReplaceDialog.class.getCanonicalName()",
//				"IusCLReplaceDialog", pnDialogs, res + "IusCLDesignReplaceDialog.png", "Replace");

		/* Not in palette */
		defineDesignComponentInfo(IusCLMenuItem.class.getCanonicalName(), "IusCLMenuItem", null, null, null,
				IusCLDesignMenuItemComponentEditor.class.getCanonicalName());

		defineDesignPropertyInfo(IusCLPropertyType.ptInteger.name(), IusCLDesignIntegerPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptString.name(), IusCLDesignStringPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptBoolean.name(), IusCLDesignBooleanPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptEnum.name(), IusCLDesignEnumPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptColor.name(), IusCLDesignColorPropertyEditor.class.getCanonicalName(), false, true);
		defineDesignPropertyInfo(IusCLPropertyType.ptCursor.name(), IusCLDesignEnumPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptStrings.name(), IusCLDesignStringListPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptPicture.name(), IusCLDesignPicturePropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptIcon.name(), IusCLDesignIconPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptFilter.name(), IusCLDesignFilterPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptComponent.name(), IusCLDesignRefComponentPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptFont.name(), IusCLDesignFontPropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptFontName.name(), IusCLDesignFontNamePropertyEditor.class.getCanonicalName());
		defineDesignPropertyInfo(IusCLPropertyType.ptEvent.name(), IusCLDesignEventPropertyEditor.class.getCanonicalName());

		this.setCodeTemplatesFile("IusCLCodeTemplatesUsr.txt");

		/* Notification */
		defineEventInfo("IusCLNotifyEvent", "IusCLNotifyEvent", null, IusCLObject.class.getCanonicalName());

		/* Message */
		defineEventInfo("IusCLMessageEvent", "IusCLMessageEvent", null, IusCLObject.class.getCanonicalName(), Object.class.getCanonicalName());

		/* Mouse */
		defineEventInfo("IusCLMouseEvent", "IusCLMouseEvent", null, IusCLObject.class.getCanonicalName(), IusCLMouseButton.class.getCanonicalName(),
				IusCLShiftState.class.getCanonicalName(), EnumSet.class.getCanonicalName());
		defineEventInfo("IusCLMouseMoveEvent", "IusCLMouseMoveEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLShiftState.class.getCanonicalName(), EnumSet.class.getCanonicalName());
		defineEventInfo("IusCLMouseHoverEvent", "IusCLMouseHoverEvent", null, IusCLObject.class.getCanonicalName());
		defineEventInfo("IusCLMouseWheelEvent", "IusCLMouseWheelEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLShiftState.class.getCanonicalName(), EnumSet.class.getCanonicalName());
		defineEventInfo("IusCLMouseWheelUpDownEvent", "IusCLMouseWheelUpDownEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLShiftState.class.getCanonicalName(), EnumSet.class.getCanonicalName());
		defineEventInfo("IusCLContextPopupEvent", "IusCLContextPopupEvent", null, IusCLObject.class.getCanonicalName());

		/* Keyboard */
		defineEventInfo("IusCLKeyPressEvent", "IusCLKeyPressEvent", null, IusCLObject.class.getCanonicalName());
		defineEventInfo("IusCLKeyEvent", "IusCLKeyEvent", null, IusCLObject.class.getCanonicalName(), IusCLKeyboardKey.class.getCanonicalName(),
				IusCLShiftState.class.getCanonicalName(), EnumSet.class.getCanonicalName());

		/* Close */
		defineEventInfo("IusCLCloseQueryEvent", "IusCLCloseQueryEvent", null, IusCLObject.class.getCanonicalName());
		defineEventInfo("IusCLCloseEvent", "IusCLCloseEvent", null, IusCLObject.class.getCanonicalName());

		/* Resize */
		defineEventInfo("IusCLCanResizeEvent", "IusCLCanResizeEvent", null, IusCLObject.class.getCanonicalName(), IusCLSize.class.getCanonicalName());
		defineEventInfo("IusCLConstrainedResizeEvent", "IusCLConstrainedResizeEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLSizeConstraints.class.getCanonicalName());

		/* Scroll */
		defineEventInfo("IusCLScrollEvent", "IusCLScrollEvent", null, IusCLObject.class.getCanonicalName(), IusCLScrollCode.class.getCanonicalName());

		/* TreeView */
		defineEventInfo("IusCLTreeViewExpandedEvent", "IusCLTreeViewExpandedEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLTreeNode.class.getCanonicalName());
		defineEventInfo("IusCLTreeViewCollapsedEvent", "IusCLTreeViewCollapsedEvent", null, IusCLObject.class.getCanonicalName(),
				IusCLTreeNode.class.getCanonicalName());

	}
}
