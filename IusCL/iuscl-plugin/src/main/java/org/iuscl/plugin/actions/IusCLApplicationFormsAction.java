/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.iuscl.plugin.forms.IusCLPluginApplicationFormsForm;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLApplicationFormsAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow iWorkbenchWindow;

	public IusCLApplicationFormsAction() {
		/*  */
	}

	public void run(IAction action) {
		if (iWorkbenchWindow.getActivePage() != null && iWorkbenchWindow.getActivePage().getActiveEditor() == null) {
			MessageDialog.openInformation(iWorkbenchWindow.getShell(), "Application Forms",
					"No editor is open, so the project/application is not known");
			return;
		}

		IusCLPluginApplicationFormsForm applicationFormsForm = new IusCLPluginApplicationFormsForm();

		applicationFormsForm.showModal();

		/*
		 * TODO
		 * 
		 * Not implemented
		 */
	}

	public void selectionChanged(IAction action, ISelection selection) {
		/*  */
	}

	public void dispose() {
		/*  */
	}

	public void init(IWorkbenchWindow window) {
		this.iWorkbenchWindow = window;
	}
}
