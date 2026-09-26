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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPasswordEdit extends IusCLEditControl {

	@Getter
	String passwordChar = "\u25CF"; // NOSONAR

	public IusCLPasswordEdit(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("PasswordChar", IusCLPropertyType.ptString, "\u25CF");

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.PASSWORD | SWT.SINGLE;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		swtText = new Text(this.getFormSwtComposite(), swtCreateParams);
		swtText.setEchoChar("*".toCharArray()[0]);

		return swtText;
	}

	@Override
	public void reCreateWnd() {
		if (this.getIsLoading()) {
			/* recreate at the end of loading */
			return;
		}

		char[] thePassPhrase = this.getPassPhrase();

		super.reCreateWnd();

		this.setPassPhrase(thePassPhrase);
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Width").setDefaultValue("121");
		setWidth(121);

		this.getProperty("MaxLength").setDefaultValue(Integer.toString(12));
		setMaxLength(12);

		swtModifyListener = swtModifyEvent -> {
			IusCLNotifyEvent onChangeEvent = IusCLPasswordEdit.this.getOnChange();
			if (IusCLEvent.isDefinedEvent(onChangeEvent)) {
				onChangeEvent.invoke(IusCLPasswordEdit.this);
			}
		};
	}

	public void setPasswordChar(String passwordChar) {
		this.passwordChar = passwordChar;

		swtText.setEchoChar(passwordChar.toCharArray()[0]);
	}

	public char[] getPassPhrase() {
		return swtText.getTextChars();
	}

	public void setPassPhrase(char[] passPhrase) {
		swtText.setTextChars(passPhrase);
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setText(String text) {
		/* Intentionally empty */
	}
}
