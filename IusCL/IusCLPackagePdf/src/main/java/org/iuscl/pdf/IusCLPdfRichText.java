/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import java.io.IOException;
import java.io.StringReader;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.system.IusCLLog;

import com.itextpdf.text.html.simpleparser.HTMLWorker;

/* **************************************************************************************************** */
public class IusCLPdfRichText extends IusCLPdfComponent {

	/* Properties */
	private IusCLStrings htmlLines = new IusCLStrings();
	private IusCLPdfDocument pdfDocument = null;

	/* **************************************************************************************************** */
	public IusCLPdfRichText(IusCLComponent aOwner) {
		super(aOwner);
		
		/* Properties */
		defineProperty("HtmlLines", IusCLPropertyType.ptStrings, "");
		defineProperty("PdfDocument", IusCLPropertyType.ptComponent, "", IusCLPdfDocument.class);
	}

	/* **************************************************************************************************** */
	public void write() {
	
		if (htmlLines.getIsEmpty()) {
			return;
		}

		HTMLWorker iTextHTMLWorker = new HTMLWorker(pdfDocument.getiTextDocument());
		
		String htmlText = htmlLines.getText();
		
		/*
		 * TODO
		 * 
		 * Embed the fonts defined in styles as 'font-family'  		
		 */
		
		try {

			iTextHTMLWorker.parse(new StringReader(htmlText));
			
		} catch (IOException ioException) {
			
			IusCLLog.logError("iText IO Error", ioException);
		}
	}
	
	/* **************************************************************************************************** */
	
	public IusCLStrings getHtmlLines() {
		return htmlLines;
	}

	public void setHtmlLines(IusCLStrings htmlLines) {
		this.htmlLines = htmlLines;
	}

	public IusCLPdfDocument getPdfDocument() {
		return pdfDocument;
	}

	public void setPdfDocument(IusCLPdfDocument pdfDocument) {
		this.pdfDocument = pdfDocument;
	}
	
}
