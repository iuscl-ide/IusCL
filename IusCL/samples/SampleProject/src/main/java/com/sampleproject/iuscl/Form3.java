/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */

package com.sampleproject.iuscl;

import org.iuscl.forms.IusCLForm;
import org.iuscl.pdf.IusCLPdfDocument;
import org.iuscl.pdf.IusCLPdfText;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.system.IusCLObject;

/** IusCL form class */
public class Form3 extends IusCLForm {

	/* IusCL Components */
	public IusCLPdfDocument pdfDocument1;
	public IusCLPdfText pdfText1;
	public IusCLButton button1;
	/** button1.OnClick event implementation */
	public void button1Click(IusCLObject sender) {

		pdfDocument1.setFileName("a.pdf");
		pdfDocument1.startDoc();
		pdfDocument1.insertSpace("2.0 cm");
		pdfDocument1.endDoc();
	}
}