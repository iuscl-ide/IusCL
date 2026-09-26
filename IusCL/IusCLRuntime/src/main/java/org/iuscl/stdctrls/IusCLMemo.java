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

public class IusCLMemo extends IusCLMultiLineEditControl {

	public IusCLMemo(IusCLComponent aOwner) {
		super(aOwner);

		createWnd(createSwtControl());
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("89");
		this.setHeight(89);
		this.getProperty("Width").setDefaultValue("185");
		this.setWidth(185);

		this.getProperty("MaxLength").setDefaultValue(Integer.toString(Text.LIMIT));
		setMaxLength(Text.LIMIT);

		swtModifyListener = swtModifyEvent -> {
			IusCLNotifyEvent onChangeEvent = IusCLMemo.this.getOnChange();
			if (IusCLEvent.isDefinedEvent(onChangeEvent)) {
				onChangeEvent.invoke(IusCLMemo.this);
			}
		};
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}
		if (this.getWordWrap()) {
			swtCreateParams = swtCreateParams | SWT.WRAP;
		}

		swtText = new Text(this.getFormSwtComposite(), swtCreateParams);
		swtVerticalScrollBar = swtText.getVerticalBar();
		swtHorizontalScrollBar = swtText.getHorizontalBar();

		return swtText;
	}
}
