/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.iuscl.plugin.ide.IusCLDesignIDE;
import org.iuscl.plugin.ide.wizards.IusCLNewFormWizard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLNewFormAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow workbenchWindow;
	IStructuredSelection structuredSelection;

	public IusCLNewFormAction() {
		/* */
	}

	public void run(IAction action) {
		if (workbenchWindow.getActivePage() != null && workbenchWindow.getActivePage().getActiveEditor() == null) {
			MessageDialog.openInformation(workbenchWindow.getShell(), "New form wizard",
					"No editor is open, so the project/application is not known");
			return;
		}

		IusCLNewFormWizard newFormWizard = new IusCLNewFormWizard();
		newFormWizard.init(PlatformUI.getWorkbench(), structuredSelection);
        WizardDialog wizardDialog = new WizardDialog(workbenchWindow.getShell(), newFormWizard);
        wizardDialog.setHelpAvailable(false);
        wizardDialog.create();
		wizardDialog.getShell().setImage(IusCLDesignIDE.loadImageFromResource("IusCLFormDesignWizardNew.png"));
        wizardDialog.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection structuredSelection) {
			this.structuredSelection = structuredSelection;
		}
	}

	public void dispose() {
		/* */
	}

	public void init(IWorkbenchWindow window) {
		this.workbenchWindow = window;
	}
}
