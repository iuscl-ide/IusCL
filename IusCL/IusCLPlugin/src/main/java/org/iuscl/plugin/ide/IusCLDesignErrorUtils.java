/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.ide;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.iuscl.plugin.IusCLPlugin;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IusCLDesignErrorUtils {

	public void showEclipseErrorDialog(String message, Throwable exception) {
		IStatus status = new Status(IStatus.ERROR, IusCLPlugin.PLUGIN_ID, message, exception);
		ErrorDialog.openError(Display.getCurrent().getActiveShell(), "IusCL Plugin Error", message, status);

		/* Log */
		// ExceptionHandler.getInstance().handleException(exception);
	}
}
