/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.swt.program.Program;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.system.IusCLLog;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/* **************************************************************************************************** */
public class IusCLPdfDocument extends IusCLPdfComponent {

	/*
	 * http://www.roseindia.net/java/itext/index.shtml
	 * http://www.coderanch.com/t/63855/open-source/footer-itext
	 */

	public enum IusCLPdfPageSize { psLETTER, psNOTE, psLEGAL, psTABLOID, psEXECUTIVE, psPOSTCARD, 
		psA0, psA1, psA2, psA3, psA4, psA5, psA6, psA7, psA8, psA9, psA10, 
		psB0, psB1, psB2, psB3, psB4, psB5, psB6, psB7, psB8, psB9, psB10, 
		psARCH_E, psARCH_D, psARCH_C, psARCH_B, psARCH_A, 
		psFLSA, psFLSE, psHALFLETTER, ps_11X17, psID_1, psID_2, psID_3, psLEDGER, 
		psCROWN_QUARTO, psLARGE_CROWN_QUARTO, psDEMY_QUARTO, psROYAL_QUARTO, 
		psCROWN_OCTAVO, psLARGE_CROWN_OCTAVO, psDEMY_OCTAVO, psROYAL_OCTAVO, 
		psSMALL_PAPERBACK, psPENGUIN_SMALL_PAPERBACK, psPENGUIN_LARGE_PAPERBACK, 
		psLETTER_LANDSCAPE, psLEGAL_LANDSCAPE, psA4_LANDSCAPE, psCustom };
	
	/* Properties */
	private String fileName = null;
	
	private String author = null;
	private String creator = null;
	private String subject = null;
	private String title = null;
	
	
	private IusCLFont headerAndFooterFont = new IusCLFont();
	private String headerText = null;
	
	private Boolean showHeaderAndFooter = true;
	
	private IusCLPdfPageSize pageSize = IusCLPdfPageSize.psA4;
	private String pageSizeWidth = getSize(PageSize.A4.getWidth());
	private String pageSizeHeight = getSize(PageSize.A4.getHeight());
	
	private String marginLeft = transformUnit("2.00 cm");
	private String marginRight = transformUnit("2.00 cm");
	private String marginTop = transformUnit("2.00 cm");
	private String marginBottom = transformUnit("2.00 cm");

	/* iText */
	private IusCLPdfHeaderAndFooter pdfHeaderAndFooter = null;
	private Document iTextDocument = null;
	private PdfWriter iTextWriter = null;

	/* **************************************************************************************************** */
	public IusCLPdfDocument(IusCLComponent aOwner) {
		super(aOwner);
		
		/* Properties */
		defineProperty("Author", IusCLPropertyType.ptString, "");
		defineProperty("Creator", IusCLPropertyType.ptString, "");
		defineProperty("Subject", IusCLPropertyType.ptString, "");
		defineProperty("Title", IusCLPropertyType.ptString, "");

		defineProperty("FileName", IusCLPropertyType.ptString, "");
		defineProperty("HeaderText", IusCLPropertyType.ptString, "");
		
		defineProperty("ShowHeaderAndFooter", IusCLPropertyType.ptBoolean, "true");

		defineProperty("PageSize", IusCLPropertyType.ptEnum, "psA4", IusCLPdfPageSize.psA4);

		defineProperty("PageSizeWidth", IusCLPropertyType.ptString, pageSizeWidth);
		defineProperty("PageSizeHeight", IusCLPropertyType.ptString, pageSizeHeight);

		defineProperty("PageMargins.MarginLeft", IusCLPropertyType.ptString, marginLeft);
		defineProperty("PageMargins.MarginRight", IusCLPropertyType.ptString, marginRight);
		defineProperty("PageMargins.MarginTop", IusCLPropertyType.ptString, marginTop);
		defineProperty("PageMargins.MarginBottom", IusCLPropertyType.ptString, marginBottom);
		
		defineProperty("HeaderAndFooterFont", IusCLPropertyType.ptFont, "(IusCLFont)");
		IusCLFont.defineFontProperties(this, "HeaderAndFooterFont", headerAndFooterFont);
	}

	/* **************************************************************************************************** */
	public void startDoc() {
		
		Float marginLeftF = getPoints(marginLeft);
		Float marginRightF = getPoints(marginRight);
		Float marginTopF = getPoints(marginTop);
		Float marginBottomF = getPoints(marginBottom);

		Float headerHeight = 0f;
		Float footerHeight = 0f;

		if (showHeaderAndFooter) {
			
			if (pdfHeaderAndFooter == null) {
				pdfHeaderAndFooter = new IusCLPdfDefaultHeaderAndFooter(this);
			}
			headerHeight = pdfHeaderAndFooter.getHeaderHeight();
			footerHeight = pdfHeaderAndFooter.getFooterHeight();
		}
		
		Rectangle iTextPageSize = null;
		if (pageSize != IusCLPdfPageSize.psCustom) {

			iTextPageSize = PageSize.getRectangle(pageSize.name().substring(2));
		}
		else {
			
			iTextPageSize = new Rectangle(getPoints(pageSizeWidth), getPoints(pageSizeHeight));
		}
		
		/* iText */
		iTextDocument = new Document(iTextPageSize, marginLeftF, marginRightF, 
				marginTopF + headerHeight, marginBottomF + footerHeight);
		
		try {
			
			iTextWriter = PdfWriter.getInstance(iTextDocument, new FileOutputStream(fileName));
			iTextWriter.setStrictImageSequence(true);

			if (author != null) {
				iTextDocument.addAuthor(author);
			}

			if (creator != null) {
				iTextDocument.addCreator(creator);	
			}
			else {
				iTextDocument.addCreator("IusCL Application");
			}

			if (subject != null) {
				iTextDocument.addSubject(subject);	
			}
			
			if (title != null) {
				iTextDocument.addTitle(title);
			}
			else {
				iTextDocument.addTitle(IusCLApplication.getTitle());
			}

			iTextWriter.setBoxSize("contentOnly", new Rectangle(
					marginLeftF, // llx 
					marginBottomF + footerHeight, // lly
					iTextPageSize.getWidth() - marginRightF, // urx
					iTextPageSize.getHeight() - (marginTopF + headerHeight))); // ury

			iTextWriter.setPageEvent(pdfHeaderAndFooter);
			
			iTextDocument.open();
			
		} catch (DocumentException documentException) {
		
			IusCLLog.logError("iText Document Error", documentException);
		}
		catch (FileNotFoundException fileNotFoundException) {
			
			IusCLLog.logError("iText File Not Found Error", fileNotFoundException);
		}
	}

	/* **************************************************************************************************** */
	public void endDoc() {
		
		if (iTextDocument.isOpen()) {
			iTextDocument.close();
		}
		Program.launch(fileName);
	}

	/* **************************************************************************************************** */
	public void insertSpace(String spaceHeight) {

		Float height = getPoints(spaceHeight);

		Rectangle iTextContentOnlyRectangle = iTextWriter.getBoxSize("contentOnly");
		Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();

		PdfTemplate iTextSpaceTemplate = iTextWriter.getDirectContent().createTemplate(width, height);
		
		try {
			
			Image iTextSpaceTemplateImage = Image.getInstance(iTextSpaceTemplate);
			iTextDocument.add(iTextSpaceTemplateImage);
			
		} catch (DocumentException documentException) {
		
			IusCLLog.logError("iText Document Error", documentException);
		}
	}

	/* **************************************************************************************************** */
	public Document getiTextDocument() {

		return iTextDocument;
	}

	/* **************************************************************************************************** */
	public PdfWriter getiTextWriter() {

		return iTextWriter;
	}
	
	public void setPdfHeaderAndFooter(IusCLPdfHeaderAndFooter pdfHeaderAndFooter) {
		
		this.pdfHeaderAndFooter = pdfHeaderAndFooter;
	}
	
	/* **************************************************************************************************** */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public IusCLFont getHeaderAndFooterFont() {
		return headerAndFooterFont;
	}

	public void setHeaderAndFooterFont(IusCLFont headerAndFooterFont) {
		this.headerAndFooterFont = headerAndFooterFont;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public IusCLPdfPageSize getPageSize() {
		return pageSize;
	}

	/* **************************************************************************************************** */
	public void setPageSize(IusCLPdfPageSize pageSize) {
		this.pageSize = pageSize;

		Float width = null;
		Float height = null;
		
		if (pageSize != IusCLPdfPageSize.psCustom) {

			Rectangle iTextPageSize = PageSize.getRectangle(pageSize.name().substring(2));
			width = iTextPageSize.getWidth();
			height = iTextPageSize.getHeight();
		}
		else {

			width = getPoints(pageSizeWidth);
			height = getPoints(pageSizeHeight);
		}

		pageSizeWidth = getSize(width);
		pageSizeHeight = getSize(height);
	}

	public String getPageSizeWidth() {
		return pageSizeWidth;
	}

	/* **************************************************************************************************** */
	public void setPageSizeWidth(String pageSizeWidth) {
		
		if (pageSize == IusCLPdfPageSize.psCustom) {
			
			this.pageSizeWidth = pageSizeWidth;
		}
	}

	public String getPageSizeHeight() {
		return pageSizeHeight;
	}

	/* **************************************************************************************************** */
	public void setPageSizeHeight(String pageSizeHeight) {
		
		if (pageSize == IusCLPdfPageSize.psCustom) {
		
			this.pageSizeHeight = pageSizeHeight;
		}
	}

	/* **************************************************************************************************** */
	@Override
	public void setSizeUnit(IusCLPdfSizeUnit sizeUnit) {
		super.setSizeUnit(sizeUnit);
		
		setPageSize(pageSize);
		
		setMarginBottom(marginBottom);
		setMarginLeft(marginLeft);
		setMarginRight(marginRight);
		setMarginTop(marginTop);
	}

	/* **************************************************************************************************** */
	public IusCLPdfDocument getPageMargins() {
		
		return this;
	}

	public String getMarginLeft() {
		return marginLeft;
	}

	/* **************************************************************************************************** */
	public void setMarginLeft(String marginLeft) {
		this.marginLeft = transformUnit(marginLeft);
	}

	public String getMarginRight() {
		return marginRight;
	}

	/* **************************************************************************************************** */
	public void setMarginRight(String marginRight) {
		this.marginRight = transformUnit(marginRight);
	}

	public String getMarginTop() {
		return marginTop;
	}

	/* **************************************************************************************************** */
	public void setMarginTop(String marginTop) {
		this.marginTop = transformUnit(marginTop);
	}

	public String getMarginBottom() {
		return marginBottom;
	}

	/* **************************************************************************************************** */
	public void setMarginBottom(String marginBottom) {
		this.marginBottom = transformUnit(marginBottom);
	}

	public Boolean getShowHeaderAndFooter() {
		return showHeaderAndFooter;
	}

	public void setShowHeaderAndFooter(Boolean showHeaderAndFooter) {
		this.showHeaderAndFooter = showHeaderAndFooter;
	}

}
