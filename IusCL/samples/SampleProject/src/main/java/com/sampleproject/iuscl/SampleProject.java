/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package com.sampleproject.iuscl;

import org.iuscl.forms.IusCLApplication;

import lombok.extern.slf4j.Slf4j;

/** IusCL application class */
@Slf4j
public class SampleProject {

	/* Program */
	public static void main(String[] args) {

		/* Initialize the application */
		IusCLApplication.initialize();
		IusCLApplication.setTitle("Sample Project Title");

		/* Auto-create forms */
		//MainForm mainForm = new MainForm();
		Form3 form3 = new Form3();
		
		/* The main form */
		IusCLApplication.setMainForm(form3);

		/* Application loop */
		IusCLApplication.run();
	}
}