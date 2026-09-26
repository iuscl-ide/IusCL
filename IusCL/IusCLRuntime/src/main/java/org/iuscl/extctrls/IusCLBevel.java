/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLGraphicControl;
import org.iuscl.graphics.IusCLCanvas;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.sysutils.IusCLGraphUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLBevel extends IusCLGraphicControl {

	public enum IusCLBevelShape {
		bsBox, bsFrame, bsTopLine, bsBottomLine, bsLeftLine, bsRightLine, bsSpacer
	}

	public enum IusCLBevelStyle {
		bsLowered, bsRaised
	}

	IusCLBevelShape bevelShape = IusCLBevelShape.bsBox;
	IusCLBevelStyle bevelStyle = IusCLBevelStyle.bsLowered;

	@Getter
	boolean systemBevelColors = true;

	boolean inDesignMode = false;

	public IusCLBevel(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Shape", IusCLPropertyType.ptEnum, "bsBox", IusCLBevelShape.bsBox);
		defineProperty("Style", IusCLPropertyType.ptEnum, "bsLowered", IusCLBevelStyle.bsLowered);

		defineProperty("SystemBevelColors", IusCLPropertyType.ptBoolean, "true");

		this.removeProperty("Caption");

		this.removeProperty("Color");
		this.removeProperty("ParentColor");

		this.removeProperty("Enabled");

		this.removeProperty("Font");
		this.removeProperty("ParentFont");
		IusCLFont.removeFontProperties(this, "Font");

		createWnd(swtCanvas);
	}

	@Override
	protected void paint() {
		IusCLCanvas canvas = this.getCanvas();

		IusCLColor colorHighlight = new IusCLColor(IusCLStandardColors.clBtnHighlight);
		IusCLColor colorShadow = new IusCLColor(IusCLStandardColors.clBtnShadow);

		if (!systemBevelColors && getParent() != null) {
			colorHighlight = IusCLGraphUtils.getHighlightColor(getParent().getColor());
			colorShadow = IusCLGraphUtils.getShadowColor(getParent().getColor());
		}

		IusCLColor color1 = colorHighlight;
		IusCLColor color2 = colorShadow;

		if (bevelStyle == IusCLBevelStyle.bsLowered) {
			color1 = colorShadow;
			color2 = colorHighlight;
		}

		int bevelHeight = getHeight() - 1;
		int bevelWidth = getWidth() - 1;

		switch (bevelShape) {
		case bsBottomLine:
			canvas.getPen().setColor(color1);
			canvas.moveTo(0, bevelHeight - 1);
			canvas.lineTo(bevelWidth, bevelHeight - 1);

			canvas.getPen().setColor(color2);
			canvas.moveTo(0, bevelHeight);
			canvas.lineTo(bevelWidth, bevelHeight);
			break;
		case bsBox:
			canvas.getPen().setColor(color1);
			canvas.moveTo(0, 0);
			canvas.lineTo(bevelWidth - 1, 0);
			canvas.moveTo(0, 0);
			canvas.lineTo(0, bevelHeight);

			canvas.getPen().setColor(color2);
			canvas.moveTo(1, bevelHeight);
			canvas.lineTo(bevelWidth, bevelHeight);
			canvas.lineTo(bevelWidth, 0);
			break;
		case bsFrame:
			canvas.getPen().setColor(color2);
			canvas.moveTo(1, 1);
			canvas.lineTo(bevelWidth, 1);
			canvas.lineTo(bevelWidth, bevelHeight);
			canvas.lineTo(1, bevelHeight);
			canvas.lineTo(1, 1);

			canvas.getPen().setColor(color1);
			canvas.moveTo(0, 0);
			canvas.lineTo(bevelWidth - 1, 0);
			canvas.lineTo(bevelWidth - 1, bevelHeight - 1);
			canvas.lineTo(0, bevelHeight - 1);
			canvas.lineTo(0, 0);
			break;
		case bsLeftLine:
			canvas.getPen().setColor(color1);
			canvas.moveTo(0, 0);
			canvas.lineTo(0, bevelHeight);

			canvas.getPen().setColor(color2);
			canvas.moveTo(1, 0);
			canvas.lineTo(1, bevelHeight);
			break;
		case bsRightLine:
			canvas.getPen().setColor(color1);
			canvas.moveTo(bevelWidth - 1, 0);
			canvas.lineTo(bevelWidth - 1, bevelHeight);

			canvas.getPen().setColor(color2);
			canvas.moveTo(bevelWidth, 0);
			canvas.lineTo(bevelWidth, bevelHeight);
			break;
		case bsSpacer:
			if (inDesignMode) {
				drawDesignBox();
			}
			break;
		case bsTopLine:
			canvas.getPen().setColor(color1);
			canvas.moveTo(0, 0);
			canvas.lineTo(bevelWidth, 0);

			canvas.getPen().setColor(color2);
			canvas.moveTo(0, 1);
			canvas.lineTo(bevelWidth, 1);
			break;
		}
	}

	public IusCLBevelShape getShape() {
		return bevelShape;
	}

	public void setShape(IusCLBevelShape bevelShape) {
		this.bevelShape = bevelShape;

		repaint();
	}

	public IusCLBevelStyle getStyle() {
		return bevelStyle;
	}

	public void setStyle(IusCLBevelStyle bevelStyle) {
		this.bevelStyle = bevelStyle;

		repaint();
	}

	public void setSystemBevelColors(boolean systemBevelColors) {
		this.systemBevelColors = systemBevelColors;

		repaint();
	}

	@Override
	public void setColor(IusCLColor color) {
		/* Intentionally nothing */
	}

	@Override
	public void setParentColor(boolean parentColor) {
		/* Intentionally nothing */
	}

	@Override
	public void setParentFont(boolean parentFont) {
		/* Intentionally nothing */
	}
}
