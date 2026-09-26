/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.pdf;

import org.iuscl.graphics.IusCLFont;
import org.iuscl.sysutils.IusCLFileUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

/* **************************************************************************************************** */
public class IusCLPdfFont {

	private static final String fontsFolder = "C:/Windows/Fonts";

	/* **************************************************************************************************** */
	static {
		
		if (IusCLFileUtils.folderExists(fontsFolder)) {
			FontFactory.registerDirectory(fontsFolder);
		}
	}
	
	/* **************************************************************************************************** */
	public static void addFontsFolder(String fontsFolder) {
		FontFactory.registerDirectory(fontsFolder);
	}

	/* **************************************************************************************************** */
	public static Font getiTextFont(IusCLFont font) {
		
		int iTextFontStyle = Font.NORMAL;
		if (font.getStyle().getBold()) {
			iTextFontStyle = iTextFontStyle | Font.BOLD;
		}
		if (font.getStyle().getItalic()) {
			iTextFontStyle = iTextFontStyle | Font.ITALIC;
		}
		if (font.getStyle().getUnderline()) {
			iTextFontStyle = iTextFontStyle | Font.UNDERLINE;
		}
		if (font.getStyle().getStrikeOut()) {
			iTextFontStyle = iTextFontStyle | Font.STRIKETHRU;
		}
		
		BaseColor iTextBaseColor = new BaseColor(font.getColor().getRed(), font.getColor().getGreen(), font.getColor().getBlue());
		
		Font iTextFont = FontFactory.getFont(font.getName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 
				font.getSize().floatValue(), iTextFontStyle, iTextBaseColor);
		
		return iTextFont;
	}
}
