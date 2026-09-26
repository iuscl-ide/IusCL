/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.plugin.forms.iuscl;

import org.iuscl.forms.IusCLApplication;

/* **************************************************************************************************** */
public class IusCLPluginForms {

	/* Program */
	public static void main(String[] args) {

		/* Initialize the application */
		IusCLApplication.initialize();

		/* Auto-create forms */
		IusCLPluginTestForm pluginTestForm = new IusCLPluginTestForm();
		//IusCLPluginActionsBaseForm pluginActionsBaseForm = new IusCLPluginActionsBaseForm();

		/* The main form */
		IusCLApplication.setMainForm(pluginTestForm);

		/* Application loop */
		IusCLApplication.run();
	}
}
