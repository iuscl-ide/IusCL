/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import org.iuscl.classes.IusCLComponent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLMultiSelectListControl extends IusCLListControl {

	@Getter
	boolean multiSelect = true;

	public IusCLMultiSelectListControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("MultiSelect", IusCLPropertyType.ptBoolean, "true");

	}

	public void setMultiSelect(boolean multiSelect) {
		if (this.multiSelect != multiSelect) {
			this.multiSelect = multiSelect;

			reCreateWnd();
		}
	}

	public Integer getSelCount() {
		return 0;
	}

	public boolean getSelection(Integer index) {
		return false;
	}

	public void setSelection(Integer index, boolean selected) {
		/* Nothing */
	}

	public void setSelection(Integer index, Integer length, boolean selected) {
		/* Nothing */
	}

	public Integer getFirstVisibleIndex() {
		return 0;
	}

	public void setFirstVisibleIndex(Integer topIndex) {
		/* Nothing */
	}
}
