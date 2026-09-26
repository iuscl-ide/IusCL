/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.graphics.IusCLFont;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTrackBar extends IusCLWinControl {

	public enum IusCLTrackBarOrientation {
		trHorizontal, trVertical
	}

	/*
	 * Frequency SelEnd SelStart SliderVisible ThumbLength TickMarks TickStyle
	 * 
	 * :(
	 * 
	 */

	@Getter
	Integer max = 10;
	@Getter
	Integer min = 0;

	@Getter
	Integer lineSize = 1;
	@Getter
	Integer pageSize = 2;

	@Getter
	IusCLTrackBarOrientation orientation = IusCLTrackBarOrientation.trHorizontal;

	@Getter
	Integer position = 0;

	@Getter
	@Setter
	IusCLNotifyEvent onChange = null;

	Scale swtScale = null;

	public IusCLTrackBar(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Max", IusCLPropertyType.ptInteger, "10");
		defineProperty("Min", IusCLPropertyType.ptInteger, "0");

		defineProperty("LineSize", IusCLPropertyType.ptInteger, "1");
		defineProperty("PageSize", IusCLPropertyType.ptInteger, "2");

		defineProperty("Orientation", IusCLPropertyType.ptEnum, "trHorizontal", IusCLTrackBarOrientation.trHorizontal);

		defineProperty("Position", IusCLPropertyType.ptInteger, "0");

		defineProperty("OnChange", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		createWnd(createSwtControl());
	}

	@Override
	protected void create() {
		super.create();

		this.removeProperty("Caption");
		this.removeProperty("Font");
		this.removeProperty("ParentFont");
		IusCLFont.removeFontProperties(this, "Font");
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		switch (orientation) {
		case trHorizontal:
			swtCreateParams = SWT.HORIZONTAL;
			break;
		case trVertical:
			swtCreateParams = SWT.VERTICAL;
			break;
		}

		swtScale = new Scale(this.getFormSwtComposite(), swtCreateParams);

		swtScale.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			position = swtScale.getSelection();
			if (IusCLEvent.isDefinedEvent(onChange)) {
				onChange.invoke(IusCLTrackBar.this);
			}
		}));

		return swtScale;
	}

	public void setMax(Integer max) {
		this.max = max;

		swtScale.setMaximum(max);
	}

	public void setMin(Integer min) {
		this.min = min;

		swtScale.setMinimum(min);
	}

	public void setLineSize(Integer lineSize) {
		this.lineSize = lineSize;

		swtScale.setIncrement(lineSize);
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;

		swtScale.setPageIncrement(pageSize);
	}

	public void setOrientation(IusCLTrackBarOrientation orientation) {
		if (this.orientation != orientation) {
			this.orientation = orientation;

			Integer height = this.getHeight();
			Integer width = this.getWidth();

			this.setWidth(height);
			this.setHeight(width);

			reCreateWnd();
		}
	}

	public void setPosition(Integer position) {
		this.position = position;

		swtScale.setSelection(position);
	}

	@Override
	public void setParentFont(boolean parentFont) {
		/* Intentionally nothing */
	}
}
