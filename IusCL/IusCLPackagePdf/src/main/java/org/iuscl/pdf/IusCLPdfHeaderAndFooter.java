/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import com.itextpdf.text.pdf.PdfPageEventHelper;

/* **************************************************************************************************** */
public class IusCLPdfHeaderAndFooter extends PdfPageEventHelper {

	/* **************************************************************************************************** */
	public Float getHeaderHeight() {
		return 0f;
	}

	/* **************************************************************************************************** */
	public Float getFooterHeight() {
		return 0f;
	}

}
