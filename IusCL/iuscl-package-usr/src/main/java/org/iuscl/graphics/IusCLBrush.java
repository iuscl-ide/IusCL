/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;
import org.iuscl.sysutils.IusCLGraphUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLBrush extends IusCLGraphicsObject {

	public enum IusCLBrushStyle {
		bsSolid, bsClear, bsHorizontal, bsVertical, bsFDiagonal, bsBDiagonal, bsCross, bsDiagCross
	}

	@Getter
	IusCLPicture picture = new IusCLPicture();
	@Getter
	IusCLColor color = new IusCLColor();

	@Getter
	IusCLBrushStyle style = IusCLBrushStyle.bsSolid;

	public IusCLBrush(IusCLCanvas canvas) {
		this.canvas = canvas;

		setColor(color);
		setStyle(style);
	}

	public void setPicture(IusCLPicture picture) {
		this.picture = picture;

		if (!IusCLGraphUtils.isEmptyPicture(picture)) {
			canvas.getGC().setBackgroundPattern(new Pattern(Display.getDefault(), picture.getGraphic().getSwtImage()));
		}
	}

	public void setColor(IusCLColor color) {
		this.color = color;

		canvas.getGC().setBackground(color.getAsSwtColor());
	}

	public void setStyle(IusCLBrushStyle style) {
		this.style = style;

		switch (style) {
		case bsBDiagonal:
			// canvas.getGC()
			break;
		case bsClear: // NOSONAR
			// canvas.getGC()
			break;
		case bsCross: // NOSONAR
			// canvas.getGC())
			break;
		case bsDiagCross: // NOSONAR
			// canvas.getGC())
			break;
		case bsFDiagonal: // NOSONAR
			// canvas.getGC())
			break;
		case bsHorizontal: // NOSONAR
			// canvas.getGC())
			break;
		case bsSolid: // NOSONAR
			// canvas.getGC())
			break;
		case bsVertical: // NOSONAR
			// canvas.getGC())
			break;
		}
	}
}
