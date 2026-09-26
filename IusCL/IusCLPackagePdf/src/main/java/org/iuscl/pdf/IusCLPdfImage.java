/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.graphics.formats.IusCLPng;
import org.iuscl.system.IusCLLog;
import org.iuscl.sysutils.IusCLGraphUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;

/* **************************************************************************************************** */
public class IusCLPdfImage extends IusCLPdfComponent {

	public enum IusCLPdfImageAlignment { iaLeft, iaRight, iaCenter };

	/* Properties */
	private IusCLPdfImageAlignment imageAlignment = IusCLPdfImageAlignment.iaLeft;
	private IusCLPicture image = null;
	private IusCLPdfDocument pdfDocument = null;
	private Integer percentOfWidth = 100;

	/* **************************************************************************************************** */
	public IusCLPdfImage(IusCLComponent aOwner) {
		super(aOwner);
		
		/* Properties */
		defineProperty("ImageAlignment", IusCLPropertyType.ptEnum, "iaLeft", IusCLPdfImageAlignment.iaLeft);
		defineProperty("Image", IusCLPropertyType.ptPicture, "");
		defineProperty("PdfDocument", IusCLPropertyType.ptComponent, "", IusCLPdfDocument.class);
		defineProperty("PercentOfWidth", IusCLPropertyType.ptInteger, "100");
	}

	/* **************************************************************************************************** */
	public void draw() {
		
		if (IusCLGraphUtils.isEmptyPicture(image)) {
			return;
		}
		
		try {
			
			IusCLPng png = new IusCLPng();
			png.loadFromPicture(image);
			
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			png.saveToStream(byteArrayOutputStream);
			
			Image itextImage = Image.getInstance(byteArrayOutputStream.toByteArray());
			
			byteArrayOutputStream.close();
			png.getSwtImage().dispose();
			
			Rectangle iTextContentOnlyRectangle = pdfDocument.getiTextWriter().getBoxSize("contentOnly");
			Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();
			Float height = iTextContentOnlyRectangle.getTop() - iTextContentOnlyRectangle.getBottom();
			
			width = (width * percentOfWidth) / 100f;
			
			if (itextImage.getWidth() < width) {
				
				width = itextImage.getWidth();
			}
			
			itextImage.scaleToFit(width, height);
			
			switch (imageAlignment) {
			case iaCenter:
				itextImage.setAlignment(Image.ALIGN_CENTER);
				break;
			case iaLeft:
				itextImage.setAlignment(Image.ALIGN_LEFT);
				break;
			case iaRight:
				itextImage.setAlignment(Image.ALIGN_RIGHT);
				break;
			}
			
			pdfDocument.getiTextDocument().add(itextImage);

		} catch (IOException ioException) {
			
			IusCLLog.logError("iText Image IO Error", ioException);
			
		} catch (BadElementException badElementException) {
			
			IusCLLog.logError("iText Image Error", badElementException);
			
		} catch (DocumentException documentException) {
			
			IusCLLog.logError("iText Document Error", documentException);
		}
	}

	/* **************************************************************************************************** */

	public IusCLPdfImageAlignment getImageAlignment() {
		return imageAlignment;
	}

	public void setImageAlignment(IusCLPdfImageAlignment imageAlignment) {
		this.imageAlignment = imageAlignment;
	}

	public IusCLPicture getImage() {
		return image;
	}

	public void setImage(IusCLPicture image) {
		this.image = image;
	}

	public IusCLPdfDocument getPdfDocument() {
		return pdfDocument;
	}

	public void setPdfDocument(IusCLPdfDocument pdfDocument) {
		this.pdfDocument = pdfDocument;
	}

	public Integer getPercentOfWidth() {
		return percentOfWidth;
	}

	public void setPercentOfWidth(Integer percentOfWidth) {
		this.percentOfWidth = percentOfWidth;
	}
	
}
