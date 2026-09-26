/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.system.IusCLLog;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.MultiColumnText;

/* **************************************************************************************************** */
public class IusCLPdfText extends IusCLPdfComponent {
	
	/* http://itextpdf.com/examples/iia.php?id=69 */

	public enum IusCLPdfTextAlignment { taLeft, taRight, taCenter, taJustified };

	/* Properties */
	private IusCLFont font = new IusCLFont();
	private Integer columns = 2;
	private IusCLPdfTextAlignment textAlignment = IusCLPdfTextAlignment.taJustified;
	private IusCLStrings text = new IusCLStrings();
	private IusCLPdfDocument pdfDocument = null;
	private String gutterBetweenColumns = transformUnit("0.50 cm");
	
	/* **************************************************************************************************** */
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

	/* **************************************************************************************************** */
	public void write() {
	
		if (text.getIsEmpty()) {
			return;
		}

		Font itextFont = IusCLPdfFont.getiTextFont(font);
		
		MultiColumnText itextMultiColumnText = new MultiColumnText();
		itextMultiColumnText.addRegularColumns(pdfDocument.getiTextDocument().left(), 
				pdfDocument.getiTextDocument().right(), 
				getPoints(gutterBetweenColumns), columns);

		for (int index = 0; index < text.size(); index++) {
			
			String line = text.get(index);
			
			if (line.isEmpty()) {
				line = "\n";
			}
			
			Paragraph itextParagraph = new Paragraph(line, itextFont);
			
			switch (textAlignment) {
			case taCenter:
				itextParagraph.setAlignment(Element.ALIGN_CENTER);
				break;
			case taJustified:
				itextParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
				break;
			case taLeft:
				itextParagraph.setAlignment(Element.ALIGN_LEFT);
				break;
			case taRight:
				itextParagraph.setAlignment(Element.ALIGN_RIGHT);
				break;
			}
			
			try {

				if (columns == 1) {
					
					pdfDocument.getiTextDocument().add(itextParagraph);
				}
				else {

					itextMultiColumnText.addElement(itextParagraph);
				}
			}
			catch (DocumentException documentException) {
				
				IusCLLog.logError("iText Document Error", documentException);
			}
		}

		if (columns > 1) {

			try {

				pdfDocument.getiTextDocument().add(itextMultiColumnText);
				
			}
			catch (DocumentException documentException) {
				
				IusCLLog.logError("iText Document Error", documentException);
			}
		}
	}

	/* **************************************************************************************************** */
	public IusCLFont getFont() {
		return font;
	}

	public void setFont(IusCLFont font) {
		this.font = font;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public IusCLPdfTextAlignment getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(IusCLPdfTextAlignment textAlignment) {
		this.textAlignment = textAlignment;
	}

	public IusCLStrings getText() {
		return text;
	}

	public void setText(IusCLStrings text) {
		this.text = text;
	}

	public IusCLPdfDocument getPdfDocument() {
		return pdfDocument;
	}

	public void setPdfDocument(IusCLPdfDocument pdfDocument) {
		this.pdfDocument = pdfDocument;
	}

	public String getGutterBetweenColumns() {
		return gutterBetweenColumns;
	}

	/* **************************************************************************************************** */
	public void setGutterBetweenColumns(String gutterBetweenColumns) {
		this.gutterBetweenColumns = transformUnit(gutterBetweenColumns);
	}
	
	/* **************************************************************************************************** */
	@Override
	public void setSizeUnit(IusCLPdfSizeUnit sizeUnit) {
		super.setSizeUnit(sizeUnit);
		
		setGutterBetweenColumns(gutterBetweenColumns);
	}
	
}
