/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.sysutils.IusCLErrorUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.MultiColumnText;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPdfText extends IusCLPdfComponent {

	/* http://itextpdf.com/examples/iia.php?id=69 */

	public enum IusCLPdfTextAlignment {
		taLeft, taRight, taCenter, taJustified
	}

	/* Properties */
	IusCLFont font = new IusCLFont();
	Integer columns = 2;
	IusCLPdfTextAlignment textAlignment = IusCLPdfTextAlignment.taJustified;
	IusCLStrings text = new IusCLStrings();
	IusCLPdfDocument pdfDocument = null;
	@Setter(AccessLevel.NONE)
	String gutterBetweenColumns = transformUnit("0.50 cm");

	public IusCLPdfText(IusCLComponent aOwner) {
		super(aOwner);

		/* Properties */
		defineProperty("Font", IusCLPropertyType.ptFont, "(IusCLFont)");
		IusCLFont.defineFontProperties(this, "Font", font);
		defineProperty("Columns", IusCLPropertyType.ptInteger, "2");
		defineProperty("TextAlignment", IusCLPropertyType.ptEnum, "taJustified", IusCLPdfTextAlignment.taJustified);
		defineProperty("Text", IusCLPropertyType.ptStrings, "");
		defineProperty("PdfDocument", IusCLPropertyType.ptComponent, "", IusCLPdfDocument.class);
		defineProperty("GutterBetweenColumns", IusCLPropertyType.ptString, gutterBetweenColumns);
	}

	public void write() {
		if (text.getIsEmpty()) {
			return;
		}
		Font itextFont = IusCLPdfFont.getiTextFont(font);
		MultiColumnText itextMultiColumnText = new MultiColumnText();
		itextMultiColumnText.addRegularColumns(pdfDocument.getITextDocument().left(),
				pdfDocument.getITextDocument().right(), getPoints(gutterBetweenColumns), columns);
		for (int index = 0; index < text.size(); index++) {
			String line = text.get(index);
			if (line.isEmpty()) {
				line = "\n";
			}
			Paragraph itextParagraph = new Paragraph(line, itextFont);
			switch (textAlignment) {
				case taCenter :
					itextParagraph.setAlignment(Element.ALIGN_CENTER);
					break;
				case taJustified :
					itextParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
					break;
				case taLeft :
					itextParagraph.setAlignment(Element.ALIGN_LEFT);
					break;
				case taRight :
					itextParagraph.setAlignment(Element.ALIGN_RIGHT);
					break;
			}
			try {
				if (columns == 1) {
					pdfDocument.getITextDocument().add(itextParagraph);
				} else {
					itextMultiColumnText.addElement(itextParagraph);
				}
			} catch (DocumentException documentException) {

				IusCLErrorUtils.showErrorDialog("iText Document Error", documentException);
			}
		}
		if (columns > 1) {
			try {
				pdfDocument.getITextDocument().add(itextMultiColumnText);
			} catch (DocumentException documentException) {
				String exceptionMessage = "iText document error on write";
				log.error(exceptionMessage, documentException);
				IusCLErrorUtils.showErrorDialog(exceptionMessage, documentException);
			}
		}
	}

	public void setGutterBetweenColumns(String gutterBetweenColumns) {
		String transformedGutterBetweenColumns = transformUnit(gutterBetweenColumns);
		this.gutterBetweenColumns = transformedGutterBetweenColumns;
	}

	@Override
	public void setSizeUnit(IusCLPdfSizeUnit sizeUnit) {
		super.setSizeUnit(sizeUnit);

		setGutterBetweenColumns(gutterBetweenColumns);
	}
}