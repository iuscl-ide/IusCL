/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.extctrls.IusCLPanel;
import org.iuscl.extctrls.IusCLPanel.IusCLPanelBevel;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLStaticText extends IusCLWinControl {

	@Getter
	IusCLBorderStyle borderStyle = IusCLBorderStyle.bsSingle;

	IusCLPanel panel = null;
	IusCLLabel label = null;

	public IusCLStaticText(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("BorderStyle", IusCLPropertyType.ptEnum, "bsSingle", IusCLBorderStyle.bsSingle);
		defineProperty("BorderWidth", IusCLPropertyType.ptInteger, "0");

		panel = new IusCLPanel(aOwner);
		panel.setBorderStyle(borderStyle);
		panel.setBevelOuter(IusCLPanelBevel.bvNone);

		label = new IusCLLabel(aOwner);
		label.setParent(panel);
		label.setAlign(IusCLAlign.alClient);

		panel.makeEmbedded(this);
		label.makeEmbedded(this);
		label.transferSwtListeners(panel);

		createWnd(panel.getSwtControl());
	}

	@Override
	protected void updateBounds(Integer aLeft, Integer aTop, Integer aWidth, Integer aHeight) {
		super.updateBounds(aLeft, aTop, aWidth, aHeight);

		panel.doAlignControls(new IusCLSize(0, 0));
	}

	public void setBorderStyle(IusCLBorderStyle borderStyle) {
		if (this.borderStyle != borderStyle) {
			this.borderStyle = borderStyle;

			panel.setBorderStyle(borderStyle);

			this.setSwtControl(panel.getSwtControl());
			assign();

			setParent(getParent());
		}
	}

	@Override
	public String getCaption() {
		return label.getCaption();
	}

	@Override
	public void setCaption(String caption) {
		label.setCaption(caption);
	}

	@Override
	public void setFont(IusCLFont font) {
		super.setFont(font);

		label.setFont(font);
	}

	public Integer getBorderWidth() {
		return panel.getBorderWidth();
	}

	public void setBorderWidth(Integer borderWidth) {
		panel.setBorderWidth(borderWidth);
	}

	@Override
	public void setHint(String hint) {
		super.setHint(hint);

		label.setHint(hint);
	}

	@Override
	public void setShowHint(boolean showHint) {
		super.setShowHint(showHint);

		label.setShowHint(showHint);
	}

	@Override
	public void setCursor(String cursor) {
		super.setCursor(cursor);

		label.setCursor(cursor);
	}

	@Override
	public void free() {
		super.free();

		label.free();
		panel.free();
	}
}
