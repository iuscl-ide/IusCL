/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.graphics.IusCLFontStyle;
import org.iuscl.sysutils.IusCLFileUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IusCLPdfFont {

	private final String fontsFolder = "C:/Windows/Fonts";

	static {
		if (IusCLFileUtils.folderExists(fontsFolder)) {
			FontFactory.registerDirectory(fontsFolder);
		}
	}

	public void addFontsFolder(String fontsFolder) {
		FontFactory.registerDirectory(fontsFolder);
	}

	public Font getiTextFont(IusCLFont font) {
		int iTextFontStyle = Font.NORMAL;
		IusCLFontStyle fontStyle = font.getStyle();
		if (fontStyle.getBold()) {
			iTextFontStyle = iTextFontStyle | Font.BOLD;
		}
		if (fontStyle.getItalic()) {
			iTextFontStyle = iTextFontStyle | Font.ITALIC;
		}
		if (fontStyle.getUnderline()) {
			iTextFontStyle = iTextFontStyle | Font.UNDERLINE;
		}
		if (fontStyle.getStrikeOut()) {
			iTextFontStyle = iTextFontStyle | Font.STRIKETHRU;
		}
		IusCLColor fontColor = font.getColor();
		BaseColor iTextBaseColor = new BaseColor(fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue());
		return FontFactory.getFont(font.getName(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font.getSize().floatValue(),
				iTextFontStyle, iTextBaseColor);
	}
}