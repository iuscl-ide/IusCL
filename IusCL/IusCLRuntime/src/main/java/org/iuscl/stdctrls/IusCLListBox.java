/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.controls.IusCLMultiSelectListControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListBox extends IusCLMultiSelectListControl {

	// AutoComplete
	// Columns
	// IntegralHeight
	// ItemHeight
	// ScrollWidth
	// Style
	// TabWidth

	List swtList = null;

	@Getter
	IusCLStrings items = new IusCLStrings();
	@Getter
	boolean extendedSelect = true;

	public IusCLListBox(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Items", IusCLPropertyType.ptStrings, "");
		defineProperty("ExtendedSelect", IusCLPropertyType.ptBoolean, "true");

		createWnd(createSwtControl());
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("97");
		setHeight(97);
		this.getProperty("Width").setDefaultValue("121");
		setWidth(121);
	}

	@Override
	protected Control createSwtControl() {
		// int swtCreateParams = SWT.NONE;
		int swtCreateParams = SWT.V_SCROLL;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		if (this.getMultiSelect()) {
			swtCreateParams = swtCreateParams | SWT.MULTI;

			if (!extendedSelect) {
				swtCreateParams = swtCreateParams | SWT.SIMPLE;
			}
		} else {
			swtCreateParams = swtCreateParams | SWT.SINGLE;
		}

		swtList = new List(this.getFormSwtComposite(), swtCreateParams);

		swtList.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> itemIndex = swtList.getSelectionIndex()));

		return swtList;
	}

	public void setItems(IusCLStrings items) {
		this.items = items;
		items.setNotify(this, "setItems");
		itemIndex = -1;

		swtList.removeAll();
		for (int index = 0; index < items.size(); index++) {
			swtList.add(items.get(index));
		}
	}

	@Override
	public void setItemIndex(Integer itemIndex) {
		super.setItemIndex(itemIndex);

		swtList.select(itemIndex);
	}

	public void setExtendedSelect(boolean extendedSelect) {
		if (this.extendedSelect != extendedSelect) {
			this.extendedSelect = extendedSelect;

			if (this.getMultiSelect()) {
				reCreateWnd();
			}
		}
	}

	@Override
	public Integer getSelCount() {
		return swtList.getSelectionCount();
	}

	@Override
	public boolean getSelection(Integer index) {
		return swtList.isSelected(index);
	}

	@Override
	public void setSelection(Integer index, boolean selected) {
		if (selected) {
			swtList.select(index);
		} else {
			swtList.deselect(index);
		}
	}

	@Override
	public void setSelection(Integer index, Integer length, boolean selected) {
		if (selected) {
			swtList.select(index, index + length);
		} else {
			swtList.deselect(index, index + length);
		}
	}

	@Override
	public Integer getFirstVisibleIndex() {
		return swtList.getTopIndex();
	}

	@Override
	public void setFirstVisibleIndex(Integer topIndex) {
		swtList.setTopIndex(topIndex);
	}
}
