/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import java.io.IOException;
import java.io.StringReader;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLErrorUtils;

import com.itextpdf.text.html.simpleparser.HTMLWorker;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPdfRichText extends IusCLPdfComponent {

	/* Properties */
	IusCLStrings htmlLines = new IusCLStrings();
	IusCLPdfDocument pdfDocument = null;

	public IusCLPdfRichText(IusCLComponent aOwner) {
		super(aOwner);

		/* Properties */
		defineProperty("HtmlLines", IusCLPropertyType.ptStrings, "");
		defineProperty("PdfDocument", IusCLPropertyType.ptComponent, "", IusCLPdfDocument.class);
	}

	public void write() {
		if (htmlLines.getIsEmpty()) {
			return;
		}
		HTMLWorker iTextHTMLWorker = new HTMLWorker(pdfDocument.getITextDocument());
		String htmlText = htmlLines.getText();
		/*
		 * TODO
		 * 
		 * Embed the fonts defined in styles as 'font-family'
		 */
		try {
			iTextHTMLWorker.parse(new StringReader(htmlText));
		} catch (IOException ioException) {
			String exceptionMessage = "iText document IO error on write";
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}
}