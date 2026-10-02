/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.MessageFormat;

import org.eclipse.swt.program.Program;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.sysutils.IusCLErrorUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPdfDocument extends IusCLPdfComponent {

	private static final String UNIT_2_00_CM = "2.00 cm";

	/*
	 * http://www.roseindia.net/java/itext/index.shtml
	 * http://www.coderanch.com/t/63855/open-source/footer-itext
	 */

	public enum IusCLPdfPageSize {
		psLETTER, psNOTE, psLEGAL, psTABLOID, psEXECUTIVE, psPOSTCARD, psA0, psA1, psA2, psA3, psA4, psA5, psA6, psA7,
		psA8, psA9, psA10, psB0, psB1, psB2, psB3, psB4, psB5, psB6, psB7, psB8, psB9, psB10, psARCH_E, psARCH_D,
		psARCH_C, psARCH_B, psARCH_A, psFLSA, psFLSE, psHALFLETTER, ps_11X17, psID_1, psID_2, psID_3, psLEDGER,
		psCROWN_QUARTO, psLARGE_CROWN_QUARTO, psDEMY_QUARTO, psROYAL_QUARTO, psCROWN_OCTAVO, psLARGE_CROWN_OCTAVO,
		psDEMY_OCTAVO, psROYAL_OCTAVO, psSMALL_PAPERBACK, psPENGUIN_SMALL_PAPERBACK, psPENGUIN_LARGE_PAPERBACK,
		psLETTER_LANDSCAPE, psLEGAL_LANDSCAPE, psA4_LANDSCAPE, psCustom
	}

	/* Properties */
	String fileName = null;

	String author = null;
	String creator = null;
	String subject = null;
	String title = null;

	IusCLFont headerAndFooterFont = new IusCLFont();
	String headerText = null;

	boolean showHeaderAndFooter = true;

	IusCLPdfPageSize pageSize = IusCLPdfPageSize.psA4;
	String pageSizeWidth = getSize(PageSize.A4.getWidth());
	String pageSizeHeight = getSize(PageSize.A4.getHeight());

	String marginLeft = transformUnit(UNIT_2_00_CM);
	String marginRight = transformUnit(UNIT_2_00_CM);
	String marginTop = transformUnit(UNIT_2_00_CM);
	String marginBottom = transformUnit(UNIT_2_00_CM);

	/* iText */
	@Getter(AccessLevel.NONE)
	IusCLPdfHeaderAndFooter pdfHeaderAndFooter = null;
	@Setter(AccessLevel.NONE)
	Document iTextDocument = null;
	@Setter(AccessLevel.NONE)
	PdfWriter iTextWriter = null;

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
		} else {
			iTextPageSize = new Rectangle(getPoints(pageSizeWidth), getPoints(pageSizeHeight));
		}
		/* iText */
		iTextDocument = new Document(iTextPageSize, marginLeftF, marginRightF, marginTopF + headerHeight,
				marginBottomF + footerHeight);
		try {
			iTextWriter = PdfWriter.getInstance(iTextDocument, new FileOutputStream(fileName));
			iTextWriter.setStrictImageSequence(true);
			if (author != null) {
				iTextDocument.addAuthor(author);
			}
			if (creator != null) {
				iTextDocument.addCreator(creator);
			} else {
				iTextDocument.addCreator("IusCL Application");
			}
			if (subject != null) {
				iTextDocument.addSubject(subject);
			}
			if (title != null) {
				iTextDocument.addTitle(title);
			} else {
				iTextDocument.addTitle(IusCLApplication.getTitle());
			}
			iTextWriter.setBoxSize("contentOnly", new Rectangle(marginLeftF, // llx
					marginBottomF + footerHeight, // lly
					iTextPageSize.getWidth() - marginRightF, // urx
					iTextPageSize.getHeight() - (marginTopF + headerHeight))); // ury
			iTextWriter.setPageEvent(pdfHeaderAndFooter);
			iTextDocument.open();
		} catch (DocumentException documentException) {
			String exceptionMessage = MessageFormat.format("iText document error on start doc for file name: \"{0}\"", fileName);
			log.error(exceptionMessage, documentException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, documentException);
		} catch (FileNotFoundException fileNotFoundException) {
			String exceptionMessage = MessageFormat.format("iText file not found error for file name: \"{0}\"", fileName);
			log.error(exceptionMessage, fileNotFoundException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, fileNotFoundException);
		}
	}

	public void endDoc() {
		if (iTextDocument.isOpen()) {
			iTextDocument.close();
		}
		Program.launch(fileName);
	}

	public void insertSpace(String spaceHeight) {
		Float height = getPoints(spaceHeight);
		Rectangle iTextContentOnlyRectangle = iTextWriter.getBoxSize("contentOnly");
		Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();
		PdfTemplate iTextSpaceTemplate = iTextWriter.getDirectContent().createTemplate(width, height);
		try {
			Image iTextSpaceTemplateImage = Image.getInstance(iTextSpaceTemplate);
			iTextDocument.add(iTextSpaceTemplateImage);
		} catch (DocumentException documentException) {
			String exceptionMessage = "iText document error on insert space";
			log.error(exceptionMessage, documentException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, documentException);
		}
	}

	public void setPageSize(IusCLPdfPageSize pageSize) {
		this.pageSize = pageSize;
		Float width = null;
		Float height = null;
		if (pageSize != IusCLPdfPageSize.psCustom) {
			Rectangle iTextPageSize = PageSize.getRectangle(pageSize.name().substring(2));
			width = iTextPageSize.getWidth();
			height = iTextPageSize.getHeight();
		} else {
			width = getPoints(pageSizeWidth);
			height = getPoints(pageSizeHeight);
		}
		pageSizeWidth = getSize(width);
		pageSizeHeight = getSize(height);
	}

	public void setPageSizeWidth(String pageSizeWidth) {
		if (pageSize == IusCLPdfPageSize.psCustom) {
			this.pageSizeWidth = pageSizeWidth;
		}
	}

	public void setPageSizeHeight(String pageSizeHeight) {
		if (pageSize == IusCLPdfPageSize.psCustom) {
			this.pageSizeHeight = pageSizeHeight;
		}
	}

	@Override
	public void setSizeUnit(IusCLPdfSizeUnit sizeUnit) {
		super.setSizeUnit(sizeUnit);
		setPageSize(pageSize);
		setMarginBottom(marginBottom);
		setMarginLeft(marginLeft);
		setMarginRight(marginRight);
		setMarginTop(marginTop);
	}

	public IusCLPdfDocument getPageMargins() {
		return this;
	}
}