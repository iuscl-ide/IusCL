/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.perspectives;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPerspective implements IPerspectiveFactory {

	IPageLayout factory;

	public IusCLPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addIusCLViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addIusCLViews() {
		IFolderLayout objectTreeViewFolder = factory.createFolder("objectTreeViewFolder", IPageLayout.LEFT, 0.25f, factory.getEditorArea());

		objectTreeViewFolder.addView("org.iuscl.plugin.views.IusCLObjectTreeViewView");

		IFolderLayout objectInspectorFolder = factory.createFolder("objectInspectorFolder", IPageLayout.BOTTOM, 0.5f, "objectTreeViewFolder");

		objectInspectorFolder.addView("org.iuscl.plugin.views.IusCLObjectInspectorView");

		IFolderLayout componentPaletteFolder = factory.createFolder("componentPaletteFolder", // NON-NLS-1
				IPageLayout.BOTTOM, 0.7f, factory.getEditorArea());

		componentPaletteFolder.addView("org.iuscl.plugin.views.IusCLComponentPaletteView");
		componentPaletteFolder.addView(IPageLayout.ID_PROJECT_EXPLORER);
		componentPaletteFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		componentPaletteFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}

	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); // NON-NLS-1
		// factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
		// factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
		// factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet");
		// //NON-NLS-1
		// factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
		// factory.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
		// factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); // NON-NLS-1
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		// factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective"); // NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.java.ui.JavaPerspective"); // NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.iuscl.plugin.ide.wizards.IusCLNewApplicationWizard");// NON-NLS-1
		factory.addNewWizardShortcut("org.iuscl.plugin.ide.wizards.IusCLNewFormWizard");// NON-NLS-1
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.iuscl.plugin.views.IusCLComponentPaletteView"); // NON-NLS-1
		factory.addShowViewShortcut("org.iuscl.plugin.views.IusCLObjectInspectorView"); // NON-NLS-1
		factory.addShowViewShortcut("org.iuscl.plugin.views.IusCLObjectTreeViewView"); // NON-NLS-1
	}
}
