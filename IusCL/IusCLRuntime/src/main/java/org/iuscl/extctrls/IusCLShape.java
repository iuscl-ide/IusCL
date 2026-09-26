/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLGraphicControl;
import org.iuscl.graphics.IusCLBrush;
import org.iuscl.graphics.IusCLBrush.IusCLBrushStyle;
import org.iuscl.graphics.IusCLCanvas;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.graphics.IusCLPen;
import org.iuscl.graphics.IusCLPen.IusCLPenMode;
import org.iuscl.graphics.IusCLPen.IusCLPenStyle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLShape extends IusCLGraphicControl {

	public enum IusCLShapeType {
		stRectangle, stSquare, stRoundRect, stRoundSquare, stEllipse, stCircle
	}

	@Getter
	IusCLShapeType shape = IusCLShapeType.stRectangle;
	@Getter
	IusCLBrush brush = null;
	@Getter
	IusCLPen pen = null;

	public IusCLShape(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Brush.Picture", IusCLPropertyType.ptPicture, "");
		defineProperty("Brush.Color", IusCLPropertyType.ptColor, "clWhite", IusCLStandardColors.clWhite);
		defineProperty("Brush.Style", IusCLPropertyType.ptEnum, "bsSolid", IusCLBrushStyle.bsSolid);

		defineProperty("Shape", IusCLPropertyType.ptEnum, "stRectangle", IusCLShapeType.stRectangle);

		defineProperty("Pen.Color", IusCLPropertyType.ptColor, "clBlack", IusCLStandardColors.clBlack);
		defineProperty("Pen.Style", IusCLPropertyType.ptEnum, "psSolid", IusCLPenStyle.psSolid);
		defineProperty("Pen.Mode", IusCLPropertyType.ptEnum, "pmCopy", IusCLPenMode.pmCopy);
		defineProperty("Pen.Width", IusCLPropertyType.ptInteger, "1");

		this.removeProperty("Caption");

		this.removeProperty("Color");
		this.removeProperty("ParentColor");

		this.removeProperty("Enabled");

		this.removeProperty("Font");
		this.removeProperty("ParentFont");
		IusCLFont.removeFontProperties(this, "Font");

		brush = getCanvas().getBrush();
		pen = getCanvas().getPen();
		createWnd(swtCanvas);
	}

	@Override
	protected void paint() {
		/* https://bugs.eclipse.org/bugs/show_bug.cgi?id=181676 */

		IusCLCanvas canvas = this.getCanvas();

		int side = getWidth();
		int x = 0;
		int y = (getHeight() - side) / 2;
		if (side > getHeight()) {
			side = getHeight();
			x = (getWidth() - side) / 2;
			y = 0;
		}
		side = side - 1;

		Double cornerd = side / 4.25;
		int corner = cornerd.intValue();

		switch (shape) {
		case stCircle:
			canvas.roundRect(x, y, side + x, side + y, side, side);
			break;
		case stEllipse:
			canvas.roundRect(0, 0, getWidth() - 1, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			break;
		case stRectangle:
			canvas.roundRect(0, 0, getWidth() - 1, getHeight() - 1, 0, 0);
			break;
		case stRoundRect:
			canvas.roundRect(0, 0, getWidth() - 1, getHeight() - 1, corner, corner);
			break;
		case stRoundSquare:
			canvas.roundRect(x, y, side + x, side + y, corner, corner);
			break;
		case stSquare:
			canvas.roundRect(x, y, side + x, side + y, 0, 0);
			break;
		}
	}

	public void setBrush(IusCLBrush brush) {
		this.brush = brush;

		repaint();
	}

	public void setShape(IusCLShapeType shape) {
		this.shape = shape;

		repaint();
	}

	public void setPen(IusCLPen pen) {
		this.pen = pen;

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
