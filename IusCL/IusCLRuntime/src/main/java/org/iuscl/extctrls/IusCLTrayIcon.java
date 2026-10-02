/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.TrayItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.menus.IusCLPopupMenu;
import org.iuscl.sysutils.IusCLGraphUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTrayIcon extends IusCLComponent {

	public enum IusCLTrayBalloonIcon {
		tbiError, tbiInformation, tbiWarning
	}

	TrayItem swtTrayItem = null;
	ToolTip swtToolTip = null;

	@Getter
	boolean visible = false;

	@Getter
	boolean showHint = true;
	@Getter
	String hint = null;

	@Getter
	boolean balloonAutoHide = true;
	@Getter
	String balloonMessage = null;
	@Getter
	String balloonText = null;
	@Getter
	IusCLTrayBalloonIcon balloonIcon = IusCLTrayBalloonIcon.tbiInformation;

	@Getter
	IusCLPicture picture = null;

	@Getter
	@Setter
	IusCLPopupMenu popupMenu = null;

	public IusCLTrayIcon(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Visible", IusCLPropertyType.ptBoolean, "false");

		defineProperty("ShowHint", IusCLPropertyType.ptBoolean, "true");
		defineProperty("Hint", IusCLPropertyType.ptString, null);

		defineProperty("BalloonAutoHide", IusCLPropertyType.ptBoolean, "true");
		defineProperty("BalloonMessage", IusCLPropertyType.ptString, null);
		defineProperty("BalloonText", IusCLPropertyType.ptString, null);
		defineProperty("BalloonIcon", IusCLPropertyType.ptEnum, "tbiInformation", IusCLTrayBalloonIcon.tbiInformation);

		defineProperty("Picture", IusCLPropertyType.ptPicture, "");

		defineProperty("PopupMenu", IusCLPropertyType.ptComponent, "", IusCLPopupMenu.class);

		swtTrayItem = new TrayItem(IusCLApplication.getSwtDisplay().getSystemTray(), SWT.NONE);

		swtTrayItem.addListener(SWT.MenuDetect, _ -> {
			if (popupMenu != null) {
				popupMenu.getSwtMenu().setVisible(true);
			}
		});

		assign();
	}

	public void showBalloon() {
		if (swtTrayItem.getToolTip() != null) {
			swtToolTip.setVisible(true);
		}
	}

	private void createHint() {
		/* Hint */
		if (showHint && IusCLStrUtils.isNotNullNotEmpty(hint)) {
			swtTrayItem.setToolTipText(hint);
			return;
		}

		swtTrayItem.setToolTipText(null);
	}

	private void createBalloon() {
		/* Balloon */
		if (IusCLStrUtils.isNotNullNotEmpty(balloonText)) {
			if (swtToolTip != null) {
				swtToolTip.dispose();
				swtToolTip = null;
			}

			switch (balloonIcon) {
			case tbiError:
				swtToolTip = new ToolTip(this.findForm().getSwtShell(), SWT.BALLOON | SWT.ICON_ERROR);
				break;
			case tbiInformation:
				swtToolTip = new ToolTip(this.findForm().getSwtShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
				break;
			case tbiWarning:
				swtToolTip = new ToolTip(this.findForm().getSwtShell(), SWT.BALLOON | SWT.ICON_WARNING);
				break;
			default:
				swtToolTip = new ToolTip(this.findForm().getSwtShell(), SWT.BALLOON); // TODO ? test
				break;
			}

			swtToolTip.setMessage(balloonMessage);
			swtToolTip.setText(balloonText);
			swtToolTip.setAutoHide(balloonAutoHide);

			swtTrayItem.setToolTip(swtToolTip);

			return;
		}

		swtTrayItem.setToolTip(null);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;

		swtTrayItem.setVisible(visible);
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;

		createHint();
	}

	public void setHint(String hint) {
		this.hint = hint;

		createHint();
	}

	public void setBalloonAutoHide(boolean balloonAutoHide) {
		this.balloonAutoHide = balloonAutoHide;

		createBalloon();
	}

	public void setBalloonText(String balloonText) {
		this.balloonText = balloonText;

		createBalloon();
	}

	public void setBalloonMessage(String balloonMessage) {
		this.balloonMessage = balloonMessage;

		createBalloon();
	}

	public void setBalloonIcon(IusCLTrayBalloonIcon balloonIcon) {
		this.balloonIcon = balloonIcon;

		createBalloon();
	}

	public void setPicture(IusCLPicture picture) {
		this.picture = picture;

		if (!IusCLGraphUtils.isEmptyPicture(picture)) {
			swtTrayItem.setImage(picture.getGraphic().getSwtImage());
		}
	}
}
