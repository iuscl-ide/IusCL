/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.stdctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;

public class IusCLEdit extends IusCLEditControl {

	public IusCLEdit(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Text", IusCLPropertyType.ptString, "");

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.SINGLE;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		swtText = new Text(this.getFormSwtComposite(), swtCreateParams);

		return swtText;
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Width").setDefaultValue("121");
		setWidth(121);

		this.getProperty("MaxLength").setDefaultValue(Integer.toString(Text.LIMIT));
		setMaxLength(Text.LIMIT);

		swtModifyListener = swtModifyEvent -> {
			IusCLNotifyEvent onChangeEvent = IusCLEdit.this.getOnChange();
			if (IusCLEvent.isDefinedEvent(onChangeEvent)) {
				onChangeEvent.invoke(IusCLEdit.this);
			}
		};
	}
}
