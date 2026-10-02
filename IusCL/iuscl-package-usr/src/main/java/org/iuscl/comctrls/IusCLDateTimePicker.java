/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.iuscl.classes.IusCLComponent;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDateTimePicker extends IusCLCommonCalendar {

	DateTime swtDateTime = null;

	public IusCLDateTimePicker(IusCLComponent aOwner) {
		super(aOwner);

		swtDateTime = new DateTime(this.getFormSwtComposite(), SWT.DATE);
		createWnd(swtDateTime);
	}
}
