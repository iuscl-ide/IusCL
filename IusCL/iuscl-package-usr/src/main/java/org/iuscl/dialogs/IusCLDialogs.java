/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.iuscl.forms.IusCLApplication;

public class IusCLDialogs {

	public enum IusCLMessageBoxIcon {
		mbiError, mbiInformation, mbiQuestion, mbiWarning, mbiWorking
	}

	public enum IusCLMessageBoxFlags {
		mbAbortRetryIgnore, mbOK, mbOKCancel, mbRetryCancel, mbYesNo, mbYesNoCancel
	}

	public enum IusCLMessageBoxReturn {
		idOK, idCancel, idAbort, idRetry, idIgnore, idYes, idNo
	}

	public static void showMessage(String msg) {
		messageBox(msg, IusCLApplication.getTitle(), null, IusCLMessageBoxFlags.mbOK);
	}

	public static void showinfo(String infoMsg) {
		messageBox(infoMsg, IusCLApplication.getTitle(), IusCLMessageBoxIcon.mbiInformation, IusCLMessageBoxFlags.mbOK);
	}

	public static void showinfo(String infoMsg, String infoCaption) {
		messageBox(infoMsg, infoCaption, IusCLMessageBoxIcon.mbiInformation, IusCLMessageBoxFlags.mbOK);
	}

	public static void showinfo(String infoMsg, String infoCaption, IusCLMessageBoxIcon infoIcon) {
		messageBox(infoMsg, infoCaption, infoIcon, IusCLMessageBoxFlags.mbOK);
	}

	public static void showError(String errorMsg) {
		messageBox(errorMsg, IusCLApplication.getTitle(), IusCLMessageBoxIcon.mbiError, IusCLMessageBoxFlags.mbOK);
	}

	public static void showError(String errorMsg, String errorCaption) {
		messageBox(errorMsg, errorCaption, IusCLMessageBoxIcon.mbiError, IusCLMessageBoxFlags.mbOK);
	}

	public static IusCLMessageBoxReturn showQuestion(String questionMsg, String questionCaption) {
		return messageBox(questionMsg, questionCaption, IusCLMessageBoxIcon.mbiQuestion, IusCLMessageBoxFlags.mbYesNo);
	}

	public static IusCLMessageBoxReturn messageBox(String text, String caption, IusCLMessageBoxIcon icon, IusCLMessageBoxFlags flags) {
		IusCLMessageBoxReturn messageBoxReturn = IusCLMessageBoxReturn.idOK;

		int createParams = SWT.NONE;

		/* Icon */
		if (icon != null) {
			switch (icon) {
			case mbiError:
				createParams = createParams | SWT.ICON_ERROR;
				break;
			case mbiInformation:
				createParams = createParams | SWT.ICON_INFORMATION;
				break;
			case mbiQuestion:
				createParams = createParams | SWT.ICON_QUESTION;
				break;
			case mbiWarning:
				createParams = createParams | SWT.ICON_WARNING;
				break;
			case mbiWorking:
				createParams = createParams | SWT.ICON_WORKING;
				break;
			default:
				break;
			}
		}

		/* Buttons */
		switch (flags) {
		case mbAbortRetryIgnore:
			createParams = createParams | SWT.ABORT | SWT.RETRY | SWT.IGNORE;
			break;
		case mbOK:
			createParams = createParams | SWT.OK;
			break;
		case mbOKCancel:
			createParams = createParams | SWT.OK | SWT.CANCEL;
			break;
		case mbRetryCancel:
			createParams = createParams | SWT.RETRY | SWT.CANCEL;
			break;
		case mbYesNo:
			createParams = createParams | SWT.YES | SWT.NO;
			break;
		case mbYesNoCancel:
			createParams = createParams | SWT.YES | SWT.NO | SWT.CANCEL;
			break;
		default:
			break;
		}

		/* Show modal */
		Shell swtParentShell = null;

		if (IusCLApplication.getActiveForm() != null) {
			swtParentShell = IusCLApplication.getActiveForm().getSwtShell();
		} else if (IusCLApplication.getMainForm() != null) {
			swtParentShell = IusCLApplication.getMainForm().getSwtShell();
		} else {
			swtParentShell = IusCLApplication.getSwtApplicationShell();
		}

		MessageBox swtMessageBox = new MessageBox(swtParentShell, createParams);
		swtMessageBox.setText(caption);
		swtMessageBox.setMessage(text);
		int buttonID = swtMessageBox.open();

		/* Pressed button */
		switch (buttonID) {
		case SWT.YES:
			messageBoxReturn = IusCLMessageBoxReturn.idYes;
			break;
		case SWT.NO:
			messageBoxReturn = IusCLMessageBoxReturn.idNo;
			break;
		case SWT.CANCEL:
			messageBoxReturn = IusCLMessageBoxReturn.idCancel;
			break;
		case SWT.OK:
			messageBoxReturn = IusCLMessageBoxReturn.idOK;
			break;
		case SWT.RETRY:
			messageBoxReturn = IusCLMessageBoxReturn.idRetry;
			break;
		case SWT.ABORT:
			messageBoxReturn = IusCLMessageBoxReturn.idAbort;
			break;
		case SWT.IGNORE:
			messageBoxReturn = IusCLMessageBoxReturn.idIgnore;
			break;
		default:
			break;
		}

		return messageBoxReturn;
	}
}
