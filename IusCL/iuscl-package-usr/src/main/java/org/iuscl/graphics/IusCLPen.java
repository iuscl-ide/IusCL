/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import org.eclipse.swt.SWT;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPen extends IusCLGraphicsObject {

	public enum IusCLPenMode {
		pmBlack, pmWhite, pmNop, pmNot, pmCopy, pmNotCopy, pmMergePenNot, pmMaskPenNot, pmMergeNotPen, pmMaskNotPen, pmMerge, pmNotMerge, pmMask,
		pmNotMask, pmXor, pmNotXor
	}

	public enum IusCLPenStyle {
		psSolid, psDash, psDot, psDashDot, psDashDotDot, psClear, psInsideFrame
	}

	@Getter
	IusCLColor color = new IusCLColor();
	@Getter
	IusCLPenMode mode = IusCLPenMode.pmCopy;
	@Getter
	IusCLPenStyle style = IusCLPenStyle.psSolid;
	@Getter
	Integer width = 1;

	public IusCLPen(IusCLCanvas canvas) {
		this.canvas = canvas;

		setColor(color);
		setStyle(style);
		setWidth(width);
	}

	public void setColor(IusCLColor color) {
		this.color = color;

		canvas.getGC().setForeground(color.getAsSwtColor());
		setMode(mode);
	}

	public void setMode(IusCLPenMode mode) {
		this.mode = mode;

		switch (mode) {
		case pmBlack:
			canvas.getGC().setForeground(IusCLColor.getStandardColor(IusCLStandardColors.clBlack).getAsSwtColor());
			break;
		case pmCopy:
			canvas.getGC().setForeground(color.getAsSwtColor());
			break;
		case pmMask:
			break;
		case pmMaskNotPen:
			break;
		case pmMaskPenNot:
			break;
		case pmMerge:
			break;
		case pmMergeNotPen:
			break;
		case pmMergePenNot:
			break;
		case pmNop:
			break;
		case pmNot:
			break;
		case pmNotCopy:
			break;
		case pmNotMask:
			break;
		case pmNotMerge:
			break;
		case pmNotXor:
			// gc.setXORMode(false);
			break;
		case pmWhite:
			canvas.getGC().setForeground(IusCLColor.getStandardColor(IusCLStandardColors.clWhite).getAsSwtColor());
			break;
		case pmXor:
			// gc.setXORMode(true);
			break;
		}
	}

	public void setStyle(IusCLPenStyle style) {
		this.style = style;

		switch (style) {
		case psClear:
			canvas.getGC().setLineStyle(SWT.NONE);
			break;
		case psDash:
			canvas.getGC().setLineStyle(SWT.LINE_DASH);
			break;
		case psDashDot:
			canvas.getGC().setLineStyle(SWT.LINE_DASHDOT);
			break;
		case psDashDotDot:
			canvas.getGC().setLineStyle(SWT.LINE_DASHDOTDOT);
			break;
		case psDot:
			canvas.getGC().setLineStyle(SWT.LINE_DOT);
			break;
		case psInsideFrame:
			// gc.setLineStyle(SWT.LINE_
			break;
		case psSolid:
			canvas.getGC().setLineStyle(SWT.LINE_SOLID);
			break;
		}
	}

	public void setWidth(Integer width) {
		this.width = width;

		canvas.getGC().setLineWidth(width);
	}
}
