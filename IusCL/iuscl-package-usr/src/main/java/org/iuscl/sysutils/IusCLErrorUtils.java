/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysutils;

import java.text.MessageFormat;
import java.util.StringJoiner;

import org.iuscl.classes.IusCLStrings;
import org.iuscl.dialogs.IusCLDialogs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IusCLErrorUtils {

	private final String SEPARATOR = "--";

	public void showErrorDialog(String errorMessage) {
		IusCLStrings errorDialogLines = new IusCLStrings();
		errorDialogLines.add("IusCL exception message:");
		errorDialogLines.add(SEPARATOR);
		errorDialogLines.add(errorMessage);
		IusCLDialogs.showError(errorDialogLines.getText());
	}

	public void showErrorDialog(String errorMessage, Throwable throwable) {
		StringJoiner stringJoiner = new StringJoiner(",\n");
		int maxStackTraceLines = 3;
		for (int index = 0; index < maxStackTraceLines; index++) {
			stringJoiner.add(MessageFormat.format("at\n{0}", throwable.getStackTrace()[index]));
		}
		IusCLStrings exceptionDialogLines = new IusCLStrings();
		exceptionDialogLines.add("IusCL exception:");
		exceptionDialogLines.add(SEPARATOR);
		exceptionDialogLines.add(errorMessage);
		exceptionDialogLines.add(SEPARATOR);
		exceptionDialogLines.add(throwable.getClass().toString());
		exceptionDialogLines.add(SEPARATOR);
		exceptionDialogLines.add(throwable.getMessage());
		exceptionDialogLines.add(SEPARATOR);
		exceptionDialogLines.add(stringJoiner.toString());
		IusCLDialogs.showError(exceptionDialogLines.getText());
	}
}
