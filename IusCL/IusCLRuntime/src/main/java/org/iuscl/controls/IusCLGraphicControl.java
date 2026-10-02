/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLCanvas;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLPen.IusCLPenStyle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLGraphicControl extends IusCLControl {

	protected Canvas swtCanvas = null;

	@Getter
	@Setter
	IusCLCanvas canvas = null;

	public IusCLGraphicControl(IusCLComponent aOwner) {
		super(aOwner);

		swtCanvas = new Canvas(this.getFormSwtComposite(), SWT.NONE);
		canvas = new IusCLCanvas(swtCanvas);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtCanvas.addPaintListener(_ -> IusCLGraphicControl.this.paint());
	}

	protected void paint() {
		/* Do the drawing in here */
	}

	@Override
	public void destroy() {
		if (canvas != null && canvas.getGC() != null) {
			canvas.getGC().dispose();
		}

		super.destroy();
	}

	@Override
	public void repaint() {
		swtCanvas.redraw();
	}

	protected void drawDesignBox() {
		// IusCLForm form = this.findForm();
		// Integer gridX = form.getGridX();
		// Integer gridY = form.getGridY();

		IusCLColor color = canvas.getPen().getColor();
		IusCLPenStyle penStyle = canvas.getPen().getStyle();
		Integer width = canvas.getPen().getWidth();

		canvas.getPen().setStyle(IusCLPenStyle.psDashDotDot);
		canvas.getPen().setColor(new IusCLColor(IusCLStandardColors.clBlack));
		canvas.getPen().setWidth(2);

		canvas.roundRect(1, 1, this.getWidth() - 1, this.getHeight() - 1, 0, 0, false);

		canvas.getPen().setStyle(penStyle);
		canvas.getPen().setColor(color);
		canvas.getPen().setWidth(width);
	}
}
