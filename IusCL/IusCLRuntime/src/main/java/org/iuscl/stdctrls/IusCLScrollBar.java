/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.events.IusCLScrollEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLScrollBar extends IusCLWinControl {

	public enum IusCLScrollBarKind {
		sbHorizontal, sbVertical
	}

	public enum IusCLScrollCode {
		scLineUp, scLineDown, scPageUp, scPageDown, scPosition, scTrack, scTop, scBottom, scEndScroll
	}

	Slider swtSlider = null;

	@Getter
	IusCLScrollBarKind kind = IusCLScrollBarKind.sbHorizontal;

	@Getter
	Integer smallChange = 1;
	@Getter
	Integer largeChange = 1;

	@Getter
	Integer max = 100;
	@Getter
	Integer min = 0;

	@Getter
	Integer position = 0;
	@Getter
	Integer pageSize = 0;

	@Getter
	@Setter
	IusCLNotifyEvent onChange = null;
	@Getter
	@Setter
	IusCLScrollEvent onScroll = null;

	public IusCLScrollBar(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Kind", IusCLPropertyType.ptEnum, "sbHorizontal", IusCLScrollBarKind.sbHorizontal);

		defineProperty("SmallChange", IusCLPropertyType.ptInteger, "1");
		defineProperty("LargeChange", IusCLPropertyType.ptInteger, "1");

		defineProperty("Min", IusCLPropertyType.ptInteger, "0");
		defineProperty("Max", IusCLPropertyType.ptInteger, "100");

		defineProperty("Position", IusCLPropertyType.ptInteger, "0");
		defineProperty("PageSize", IusCLPropertyType.ptInteger, "0");

		defineProperty("OnChange", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnScroll", IusCLPropertyType.ptEvent, null, IusCLScrollEvent.class);

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		switch (kind) {
		case sbHorizontal:
			swtCreateParams = SWT.HORIZONTAL;
			break;
		case sbVertical:
			swtCreateParams = SWT.VERTICAL;
			break;
		}

		swtSlider = new Slider(this.getFormSwtComposite(), swtCreateParams);

		swtSlider.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			position = swtSlider.getSelection();

			if (IusCLEvent.isDefinedEvent(onChange)) {
				onChange.invoke(IusCLScrollBar.this);
			}

			if (IusCLEvent.isDefinedEvent(onScroll)) {
				IusCLScrollCode scrollCode = IusCLScrollCode.scPosition;

				switch (swtSelectionEvent.detail) {
				case SWT.NONE:
					scrollCode = IusCLScrollCode.scEndScroll;
					break;
				case SWT.DRAG:
					scrollCode = IusCLScrollCode.scTrack;
					break;
				case SWT.HOME:
					scrollCode = IusCLScrollCode.scTop;
					break;
				case SWT.END:
					scrollCode = IusCLScrollCode.scBottom;
					break;
				case SWT.ARROW_DOWN:
					scrollCode = IusCLScrollCode.scLineDown;
					break;
				case SWT.ARROW_UP:
					scrollCode = IusCLScrollCode.scLineUp;
					break;
				case SWT.PAGE_DOWN:
					scrollCode = IusCLScrollCode.scPageDown;
					break;
				case SWT.PAGE_UP:
					scrollCode = IusCLScrollCode.scPageUp;
					break;
				default:
					break;
				}

				Integer newPosition = onScroll.invoke(IusCLScrollBar.this, scrollCode, position);
				setPosition(newPosition);
			}
		}));

		return swtSlider;
	}

	public void setKind(IusCLScrollBarKind kind) {
		if (this.kind != kind) {
			this.kind = kind;

			Integer height = this.getHeight();
			Integer width = this.getWidth();

			this.setWidth(height);
			this.setHeight(width);

			reCreateWnd();
		}
	}

	public void setSmallChange(Integer smallChange) {
		this.smallChange = smallChange;

		swtSlider.setIncrement(smallChange);
	}

	public void setLargeChange(Integer largeChange) {
		this.largeChange = largeChange;

		swtSlider.setPageIncrement(largeChange);
	}

	public void setMax(Integer max) {
		this.max = max;

		swtSlider.setMaximum(max);
	}

	public void setMin(Integer min) {
		this.min = min;

		swtSlider.setMinimum(min);
	}

	public void setPosition(Integer position) {
		this.position = position;

		swtSlider.setSelection(position);
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;

		swtSlider.setThumb(pageSize);
	}

	public void setParams(Integer position, Integer min, Integer max) {
		setMin(min);
		setMax(max);
		setPosition(position);
	}
}
