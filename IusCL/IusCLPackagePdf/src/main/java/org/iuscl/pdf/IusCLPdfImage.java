/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.graphics.formats.IusCLPng;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.iuscl.sysutils.IusCLGraphUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPdfImage extends IusCLPdfComponent {

	public enum IusCLPdfImageAlignment {
		iaLeft, iaRight, iaCenter
	}

	/* Properties */
	IusCLPdfImageAlignment imageAlignment = IusCLPdfImageAlignment.iaLeft;
	IusCLPicture image = null;
	IusCLPdfDocument pdfDocument = null;
	Integer percentOfWidth = 100;

	public IusCLPdfImage(IusCLComponent aOwner) {
		super(aOwner);

		/* Properties */
		defineProperty("ImageAlignment", IusCLPropertyType.ptEnum, "iaLeft", IusCLPdfImageAlignment.iaLeft);
		defineProperty("Image", IusCLPropertyType.ptPicture, "");
		defineProperty("PdfDocument", IusCLPropertyType.ptComponent, "", IusCLPdfDocument.class);
		defineProperty("PercentOfWidth", IusCLPropertyType.ptInteger, "100");
	}

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
			Rectangle iTextContentOnlyRectangle = pdfDocument.getITextWriter().getBoxSize("contentOnly");
			Float width = iTextContentOnlyRectangle.getRight() - iTextContentOnlyRectangle.getLeft();
			Float height = iTextContentOnlyRectangle.getTop() - iTextContentOnlyRectangle.getBottom();
			width = (width * percentOfWidth) / 100f;
			if (itextImage.getWidth() < width) {
				width = itextImage.getWidth();
			}
			itextImage.scaleToFit(width, height);
			switch (imageAlignment) {
				case iaCenter :
					itextImage.setAlignment(Element.ALIGN_CENTER);
					break;
				case iaLeft :
					itextImage.setAlignment(Element.ALIGN_LEFT);
					break;
				case iaRight :
					itextImage.setAlignment(Element.ALIGN_RIGHT);
					break;
			}
			pdfDocument.getITextDocument().add(itextImage);
		} catch (IOException ioException) {
			String exceptionMessage = "iText image IO error on draw";
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		} catch (BadElementException badElementException) {
			String exceptionMessage = "iText image bad element error on draw";
			log.error(exceptionMessage, badElementException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, badElementException);
		} catch (DocumentException documentException) {
			String exceptionMessage = "iText document error on draw";
			log.error(exceptionMessage, documentException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, documentException);
		}
	}
}