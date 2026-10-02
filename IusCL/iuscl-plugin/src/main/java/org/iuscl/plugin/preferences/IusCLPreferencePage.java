/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.iuscl.plugin.IusCLPlugin;

public class IusCLPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public IusCLPreferencePage() {
		super(GRID);
		setPreferenceStore(IusCLPlugin.getDefault().getPreferenceStore());
		setDescription("IusCL IDE Options");
	}

	public void createFieldEditors() {
		GridLayout la = (GridLayout) getFieldEditorParent().getLayout();
		la.verticalSpacing = 20;
		getFieldEditorParent().setLayout(la);

		int groupMargin = 5;

		/* Environment */
		Group environmentGroup = new Group(getFieldEditorParent(), SWT.NULL);
		GridData environmentGroupGridData = new GridData();
		environmentGroupGridData.horizontalAlignment = GridData.FILL;
		environmentGroupGridData.horizontalSpan = 2;
		environmentGroupGridData.grabExcessHorizontalSpace = true;
		environmentGroup.setLayoutData(environmentGroupGridData);
		environmentGroup.setText("Environment");

		StringFieldEditor distributionLocation = new DirectoryFieldEditor(IusCLDesignPreferences.ENV_DISTRIBUTIONLOCATION, "Distributions location",
				environmentGroup);

		GridLayout environmentGroupGridLayout = (GridLayout) environmentGroup.getLayout();
		environmentGroupGridLayout.marginRight = groupMargin;
		environmentGroupGridLayout.marginLeft = groupMargin;
		environmentGroupGridLayout.marginTop = groupMargin;
		environmentGroupGridLayout.marginBottom = groupMargin;
		environmentGroup.setLayout(environmentGroupGridLayout);

		addField(distributionLocation);

		/* New Application */
		Group newApplicationGroup = new Group(getFieldEditorParent(), SWT.NULL);
		GridData newApplicationGroupGridData = new GridData();
		newApplicationGroupGridData.horizontalAlignment = GridData.FILL;
		newApplicationGroupGridData.horizontalSpan = 2;
		newApplicationGroupGridData.grabExcessHorizontalSpace = true;
		newApplicationGroup.setLayoutData(newApplicationGroupGridData);
		newApplicationGroup.setText("New Application");

		StringFieldEditor projectName = new StringFieldEditor(IusCLDesignPreferences.NEWAPP_PROJECTNAME, "Project name", newApplicationGroup);
		StringFieldEditor packageName = new StringFieldEditor(IusCLDesignPreferences.NEWAPP_PACKAGENAME, "Package name", newApplicationGroup);
		StringFieldEditor formName = new StringFieldEditor(IusCLDesignPreferences.NEWAPP_FORMNAME, "Form name", newApplicationGroup);
		StringFieldEditor projectLocation = new DirectoryFieldEditor(IusCLDesignPreferences.NEWAPP_PROJECTLOCATION, "Project location",
				newApplicationGroup);

		GridLayout newApplicationGroupGridLayout = (GridLayout) newApplicationGroup.getLayout();
		newApplicationGroupGridLayout.marginRight = groupMargin;
		newApplicationGroupGridLayout.marginLeft = groupMargin;
		newApplicationGroupGridLayout.marginTop = groupMargin;
		newApplicationGroupGridLayout.marginBottom = groupMargin;
		newApplicationGroup.setLayout(newApplicationGroupGridLayout);

		addField(projectName);
		addField(packageName);
		addField(formName);
		addField(projectLocation);

		/* New Form */
		Group newFormGroup = new Group(getFieldEditorParent(), SWT.NULL);
		GridData newFormGroupGridData = new GridData();
		newFormGroupGridData.horizontalAlignment = GridData.FILL;
		newFormGroupGridData.horizontalSpan = 2;
		newFormGroupGridData.grabExcessHorizontalSpace = true;
		newFormGroup.setLayoutData(newFormGroupGridData);
		newFormGroup.setText("New Form");

		StringFieldEditor nfFormName = new StringFieldEditor(IusCLDesignPreferences.NEWFORM_FORMNAME, "Form class name", newFormGroup);
		StringFieldEditor nfWidth = new StringFieldEditor(IusCLDesignPreferences.NEWFORM_WIDTH, "Initial width", newFormGroup);
		StringFieldEditor nfHeight = new StringFieldEditor(IusCLDesignPreferences.NEWFORM_HEIGHT, "Initial height", newFormGroup);
		StringFieldEditor nfLeft = new StringFieldEditor(IusCLDesignPreferences.NEWFORM_LEFT, "Initial left", newFormGroup);
		StringFieldEditor nfTop = new StringFieldEditor(IusCLDesignPreferences.NEWFORM_TOP, "Initial top", newFormGroup);

		GridLayout newFormGroupGridLayout = (GridLayout) newFormGroup.getLayout();
		newFormGroupGridLayout.numColumns = 3;
		newFormGroupGridLayout.marginRight = groupMargin;
		newFormGroupGridLayout.marginLeft = groupMargin;
		newFormGroupGridLayout.marginTop = groupMargin;
		newFormGroupGridLayout.marginBottom = groupMargin;
		newFormGroup.setLayout(newFormGroupGridLayout);

		addField(nfFormName);
		addField(nfWidth);
		addField(nfHeight);
		addField(nfLeft);
		addField(nfTop);

//		NF_FORMNAME.fillIntoGrid(newFormGroup, 2);
//		NF_WIDTH.fillIntoGrid(newFormGroup, 2);
//		NF_HEIGHT.fillIntoGrid(newFormGroup, 2);
//		NF_LEFT.fillIntoGrid(newFormGroup, 2);
//		NF_TOP.fillIntoGrid(newFormGroup, 2);

//		addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
//		
//		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, 
//				"&Directory preference:", getFieldEditorParent()));
//		addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));

//		
//		
//		addField(
//			new BooleanFieldEditor(
//				PreferenceConstants.P_BOOLEAN,
//				"&An example of a boolean preference",
//				getFieldEditorParent()));
//
//		addField(new RadioGroupFieldEditor(
//				PreferenceConstants.P_CHOICE,
//			"An example of a multiple-choice preference",
//			1,
//			new String[][] { { "&Choice 1", "choice1" }, {
//				"C&hoice 2", "choice2" }
//		}, getFieldEditorParent()));
//		addField(
//			new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		/*  */
	}
}
