/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.controls.IusCLListControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLComboBox extends IusCLListControl {

	public enum IusCLComboBoxStyle {
		csDropDown, csSimple, csDropDownList
	}
	// , csOwnerDrawFixed, csOwnerDrawVariable};

	// AutoCloseUp
	// AutoComplete
	// AutoDropDown
	// CharCase
	// ItemHeight

	Combo swtCombo = null;
	ModifyListener swtModifyListener = null;

	@Getter
	IusCLStrings items = new IusCLStrings();
	@Getter
	IusCLComboBoxStyle style = IusCLComboBoxStyle.csDropDownList;
	@Getter
	Integer dropDownCount = 8;
	@Getter
	Integer maxLength = Combo.LIMIT;

	@Getter
	IusCLNotifyEvent onChange = null;

	Integer systemHeight = 0;

	public IusCLComboBox(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Items", IusCLPropertyType.ptStrings, "");
		defineProperty("Style", IusCLPropertyType.ptEnum, "csDropDownList", IusCLComboBoxStyle.csDropDownList);
		defineProperty("DropDownCount", IusCLPropertyType.ptInteger, "8");
		defineProperty("MaxLength", IusCLPropertyType.ptInteger, Integer.toString(Combo.LIMIT));

		defineProperty("OnChange", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		createWnd(createSwtControl());
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Width").setDefaultValue("145");
		setWidth(145);

		systemHeight = this.getHeight();

		swtModifyListener = _ -> {
			IusCLNotifyEvent onChangeEvent = IusCLComboBox.this.getOnChange();
			if (IusCLEvent.isDefinedEvent(onChangeEvent)) {
				onChangeEvent.invoke(IusCLComboBox.this);
			}
		};
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		switch (style) {
		case csDropDown:
			swtCreateParams = swtCreateParams | SWT.READ_ONLY;
			break;
		case csDropDownList:
			swtCreateParams = swtCreateParams | SWT.DROP_DOWN;
			break;
		case csSimple:
			swtCreateParams = swtCreateParams | SWT.SIMPLE;
			break;
		}

		swtCombo = new Combo(this.getFormSwtComposite(), swtCreateParams);

		swtCombo.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> itemIndex = swtCombo.getSelectionIndex()));

		/* TODO widgetDefaultSelected */
		/* When enter leaves the combo.. */

		return swtCombo;
	}

	public void setItems(IusCLStrings items) {
		this.items = items;
		items.setNotify(this, "setItems");
		itemIndex = -1;

		swtCombo.removeAll();
		for (int index = 0; index < items.size(); index++) {
			swtCombo.add(items.get(index));
		}
	}

	public void setStyle(IusCLComboBoxStyle style) {
		if (this.style != style) {
			this.style = style;

			reCreateWnd();
		}
	}

	@Override
	public void setHeight(Integer height) {
		if ((style == IusCLComboBoxStyle.csDropDown) || (style == IusCLComboBoxStyle.csDropDownList) && (systemHeight > 0)) {
			super.setHeight(systemHeight);
			return;
		}

		super.setHeight(height);
	}

	@Override
	public Integer getHeight() {
		if ((style == IusCLComboBoxStyle.csDropDown) || (style == IusCLComboBoxStyle.csDropDownList) && (systemHeight > 0)) {
			this.height = systemHeight;
		}

		return super.getHeight();
	}

	public void setDropDownCount(Integer dropDownCount) {
		this.dropDownCount = dropDownCount;

		swtCombo.setVisibleItemCount(dropDownCount);
	}

	public boolean getDroppedDown() {
		return swtCombo.getListVisible();
	}

	public void setDroppedDown(boolean droppedDown) {
		swtCombo.setListVisible(droppedDown);
	}

	@Override
	public void setItemIndex(Integer itemIndex) {
		super.setItemIndex(itemIndex);

		swtCombo.select(itemIndex);
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;

		swtCombo.setTextLimit(maxLength);
	}

	public Integer getSelStart() {
		return swtCombo.getSelection().x;
	}

	public void setSelStart(Integer selStart) {
		swtCombo.setSelection(new Point(selStart, swtCombo.getSelection().y));
	}

	public Integer getSelLength() {
		return swtCombo.getSelection().y - getSelStart();
	}

	public void setSelLength(Integer selLength) {
		swtCombo.setSelection(new Point(getSelStart(), getSelStart() + selLength));
	}

	@Override
	public String getText() {
		return swtCombo.getText();
	}

	@Override
	public void setText(String text) {
		swtCombo.setText(text);
	}

	public String getSelText() {
		return swtCombo.getText().substring(swtCombo.getSelection().x, swtCombo.getSelection().y);
	}

	public void setSelText(String selText) {
		int x = swtCombo.getSelection().x;

		String newText = swtCombo.getText().substring(0, x);
		newText = newText + selText;
		newText = newText + swtCombo.getText().substring(swtCombo.getSelection().y);

		swtCombo.setText(newText);

		swtCombo.setSelection(new Point(x, x + selText.length()));
	}

	public void setOnChange(IusCLNotifyEvent onChange) {
		this.onChange = onChange;

		swtCombo.removeModifyListener(swtModifyListener);
		if (this.getOnChange() != null) {
			swtCombo.addModifyListener(swtModifyListener);
		}
	}
}
