/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import java.util.ArrayList;

import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.system.IusCLLog;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/* **************************************************************************************************** */
public class IusCLPdfDefaultHeaderAndFooter extends IusCLPdfHeaderAndFooter {

	/*
	 * http://itextpdf.com/examples/iia.php?id=104
	 * 
	 * http://itextpdf.com/examples/iia.php?id=103
	 */
	
	private ArrayList<PdfTemplate> pageNumbers = new ArrayList<PdfTemplate>();
	
	private IusCLPdfDocument pdfDocument = null;
	private Font iTextFont = null;
	private BaseColor iTextColor = null;
	
	/* **************************************************************************************************** */
	public IusCLPdfDefaultHeaderAndFooter(IusCLPdfDocument pdfDocument) {
		super();
		
		this.pdfDocument = pdfDocument;
		IusCLFont font = pdfDocument.getHeaderAndFooterFont();
		iTextFont = IusCLPdfFont.getiTextFont(font);
		IusCLColor color = font.getColor();
		iTextColor = new BaseColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	/* **************************************************************************************************** */
	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		
		try {
			Rectangle iTextContentOnlyRectangle = writer.getBoxSize("contentOnly");
			Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();
			 
			Float fontSize = pdfDocument.getHeaderAndFooterFont().getSize().floatValue();
			
			/* Header */
			PdfPTable iTextHeaderTable = new PdfPTable(1);
			iTextHeaderTable.setWidths(new int[]{1});
			iTextHeaderTable.setTotalWidth(width);
			
			PdfPCell iTextHeaderCell = new PdfPCell();
			iTextHeaderCell.setBorder(Rectangle.BOTTOM);
			iTextHeaderCell.setBorderColor(iTextColor);
//			iTextHeaderCell.setBorder(Rectangle.BOX);
			iTextHeaderCell.setFixedHeight(fontSize * 1.5f);
			
			iTextHeaderTable.addCell(iTextHeaderCell);
			iTextHeaderTable.writeSelectedRows(0, -1, 
					iTextContentOnlyRectangle.getLeft(), 
					iTextContentOnlyRectangle.getTop() + fontSize * 2f, 
					writer.getDirectContent());

			String headerText = pdfDocument.getHeaderText();
			if (headerText == null) {
				headerText = IusCLApplication.getTitle();
			}
			
			ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_LEFT, new Phrase(headerText, iTextFont),
					iTextContentOnlyRectangle.getLeft(), 
					iTextContentOnlyRectangle.getTop() + fontSize, 
					0);

			/* Footer */
			PdfTemplate pageNumber = writer.getDirectContent().createTemplate(width, fontSize * 1.5f);
			pageNumbers.add(pageNumber);

			PdfPTable iTextFooterTable = new PdfPTable(1);
			iTextFooterTable.setWidths(new int[]{width.intValue()});
			iTextFooterTable.setTotalWidth(width);
			
			PdfPCell iTextFooterCell = new PdfPCell(Image.getInstance(pageNumber));
            iTextFooterCell.setBorder(Rectangle.TOP);
            iTextFooterCell.setBorderColor(iTextColor);
//            iTextFooterCell.setBorder(Rectangle.BOX);
//            iTextHeaderCell.setFixedHeight(fontSize * 1.5f);
            
            iTextFooterTable.addCell(iTextFooterCell);
            iTextFooterTable.writeSelectedRows(0, -1, 
            		iTextContentOnlyRectangle.getLeft(), 
            		iTextContentOnlyRectangle.getBottom() - fontSize * 0.5f, 
            		writer.getDirectContent());

		} catch (DocumentException documentException) {
			
			IusCLLog.logError("iText Document Error", documentException);
		}
		
	}

	/* **************************************************************************************************** */
	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {

		int pages = pageNumbers.size();
		
		Rectangle iTextContentOnlyRectangle = writer.getBoxSize("contentOnly");
		Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();
		
		Float fontSize = pdfDocument.getHeaderAndFooterFont().getSize().floatValue();

		for (int index = 0; index < pages; index++) {
			ColumnText.showTextAligned(pageNumbers.get(index), Element.ALIGN_RIGHT,
					new Phrase("Page " + String.valueOf((index + 1) + " / " + pages), iTextFont), 
					width, fontSize * 0.5f, 0);
		}
	}

	/* **************************************************************************************************** */
	@Override
	public Float getHeaderHeight() {
		
		Float fontSize = pdfDocument.getHeaderAndFooterFont().getSize().floatValue();
		return fontSize * 2f;
	}

	/* **************************************************************************************************** */
	@Override
	public Float getFooterHeight() {

		Float fontSize = pdfDocument.getHeaderAndFooterFont().getSize().floatValue();
		return fontSize * 2f;
	}
	
	
}
