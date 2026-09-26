/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.types.IusCLPoint;
import org.iuscl.types.IusCLRectangle;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCanvas extends IusCLPersistent {

	GC swtGC = null;
	@Getter
	@Setter
	IusCLPoint penPos = new IusCLPoint(0, 0);
	@Getter
	@Setter
	IusCLPen pen = null;
	@Getter
	@Setter
	IusCLBrush brush = null;
	IusCLFont font = null;

	public IusCLCanvas(GC swtGC) {
		this.swtGC = swtGC;

		pen = new IusCLPen(this);
		brush = new IusCLBrush(this);
		font = new IusCLFont();
		font.setSwtFont(swtGC.getFont());
	}

	public IusCLCanvas(Drawable swtDrawable) {
		this(new GC(swtDrawable));
	}

	public void moveTo(Integer x, Integer y) {
		penPos.setX(x);
		penPos.setY(y);
	}

	public void lineTo(Integer x, Integer y) {
		swtGC.drawLine(penPos.getX(), penPos.getY(), x, y);

		penPos.setX(x);
		penPos.setY(y);
	}

	public void draw(Integer x, Integer y, IusCLGraphic graphic) {
		swtGC.drawImage(graphic.getSwtImage(), x, y);
	}

	public void stretchDraw(IusCLRectangle rect, IusCLGraphic graphic) {
		IusCLGraphic scaledGraphic = graphic.resizeGraphic(rect.getWidth(), rect.getHeight());
		swtGC.drawImage(scaledGraphic.getSwtImage(), rect.getLeft(), rect.getTop());
	}

	public GC getGC() {
		return swtGC;
	}

	public IusCLFont getFont() {
		font.setNotify(this, "setFont");

		return font;
	}

	public void setFont(IusCLFont font) {
		this.font = font;

		swtGC.setFont(font.getSwtFont());
	}

	public void roundRect(Integer x1, Integer y1, Integer x2, Integer y2, Integer x3, Integer y3) {
		roundRect(x1, y1, x2, y2, x3, y3, true);
	}

	public void roundRect(Integer x1, Integer y1, Integer x2, Integer y2, Integer x3, Integer y3, boolean filled) {
		if (filled) {
			swtGC.fillRoundRectangle(x1, y1, x2 - x1, y2 - y1, x3, y3);
		}
		swtGC.drawRoundRectangle(x1, y1, x2 - x1, y2 - y1, x3, y3);
	}

	public void textOut(Integer x, Integer y, String text) {
		Color oldColor = swtGC.getForeground();
		swtGC.setForeground(font.getColor().getAsSwtColor());
		swtGC.drawString(text, x, y, true);
		swtGC.setForeground(oldColor);

		// swtGC.fillPolygon(pointArray)
		// swtGC.fillRoundRectangle(X1, Y1, X2 - X1, Y2 - Y1, X3, Y3);
	}

	public IusCLSize textExtent(String text) {
		Point swtPoint = swtGC.textExtent(text);

		return new IusCLSize(swtPoint.x, swtPoint.y);
	}
}
