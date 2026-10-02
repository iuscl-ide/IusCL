/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLPersistent;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFont;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTextAttributes extends IusCLPersistent {

	public enum IusCLUnderlineStyle {
		usSingle, usDouble, usError, usSquiggle, usLink
	}

	@Getter
	String name = null;
	@Getter
	Integer size = null;
	@Getter
	Integer height = null;

	@Getter
	Boolean bold = null;
	@Getter
	Boolean italic = null;
	@Getter
	Boolean underline = null;
	@Getter
	Boolean strikeOut = null;

	@Getter
	IusCLColor color = null;
	@Getter
	IusCLColor highlightColor = null;
	@Getter
	IusCLColor underlineColor = null;
	@Getter
	IusCLColor strikeOutColor = null;

	@Getter
	IusCLUnderlineStyle underlineStyle = null;

	public IusCLTextAttributes() {
		super();

		IusCLFont defaultFont = new IusCLFont();
		this.loadFromFont(defaultFont);
	}

	public void emptyAttributes() {
		name = null;
		size = null;
		height = null;

		bold = null;
		italic = null;
		underline = null;
		strikeOut = null;

		color = null;
		highlightColor = null;
		underlineColor = null;
		strikeOutColor = null;

		underlineStyle = null;
	}

	public void loadFromFont(IusCLFont font) {
		loadFromFont(font, true);
	}

	public void loadFromFont(IusCLFont font, boolean allAttributes) {
		name = font.getName();
		size = font.getSize();
		height = font.getHeight();

		bold = font.getStyle().getBold();
		italic = font.getStyle().getItalic();
		underline = font.getStyle().getUnderline();
		strikeOut = font.getStyle().getStrikeOut();

		color = font.getColor();

		if (allAttributes) {
			highlightColor = IusCLColor.getStandardColor(IusCLStandardColors.clWindow);
			underlineColor = color;
			strikeOutColor = color;

			underlineStyle = IusCLUnderlineStyle.usSingle;
		}
	}

	public IusCLFont getAsFont() {
		return getAsFont(null);
	}

	public IusCLFont getAsFont(IusCLFont defaultFont) {
		IusCLFont font = new IusCLFont();
		if (defaultFont != null) {
			font.setSwtFont(defaultFont.getSwtFont());
		}

		if (name != null) {
			font.setName(name);
		}
		if (size != null) {
			font.setSize(size);
		}
		// if (height != null) {
		//
		// font.setSize(height);
		// }

		if (bold != null) {
			font.getStyle().setBold(bold);
		}
		if (italic != null) {
			font.getStyle().setItalic(italic);
		}
		if (strikeOut != null) {
			font.getStyle().setStrikeOut(strikeOut);
		}
		if (underline != null) {
			font.getStyle().setUnderline(underline);
		}

		if (color != null) {
			font.setColor(color);
		}

		return font;
	}

	public void loadFromTextAttributes(IusCLTextAttributes textAttributes) {
		name = textAttributes.getName();
		size = textAttributes.getSize();
		height = textAttributes.getHeight();

		bold = textAttributes.getBold();
		italic = textAttributes.getItalic();
		underline = textAttributes.getUnderline();
		strikeOut = textAttributes.getStrikeOut();

		color = textAttributes.getColor();

		highlightColor = textAttributes.getHighlightColor();
		underlineColor = textAttributes.getUnderlineColor();
		strikeOutColor = textAttributes.getStrikeOutColor();

		underlineStyle = textAttributes.getUnderlineStyle();
	}

	public void setHighlightColor(IusCLColor highlightColor) {
		this.highlightColor = highlightColor;

		invokeNotify();
	}

	public void setName(String name) {
		this.name = name;

		invokeNotify();
	}

	public void setSize(Integer size) {
		this.size = size;

		invokeNotify();
	}

	public void setHeight(Integer height) {
		this.height = height;

		invokeNotify();
	}

	public void setBold(boolean bold) {
		this.bold = bold;

		invokeNotify();
	}

	public void setItalic(boolean italic) {
		this.italic = italic;

		invokeNotify();
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;

		invokeNotify();
	}

	public void setStrikeOut(boolean strikeOut) {
		this.strikeOut = strikeOut;

		invokeNotify();
	}

	public void setColor(IusCLColor color) {
		this.color = color;

		invokeNotify();
	}

	public void setUnderlineColor(IusCLColor underlineColor) {
		this.underlineColor = underlineColor;

		invokeNotify();
	}

	public void setStrikeOutColor(IusCLColor strikeOutColor) {
		this.strikeOutColor = strikeOutColor;

		invokeNotify();
	}

	public void setUnderlineStyle(IusCLUnderlineStyle underlineStyle) {
		this.underlineStyle = underlineStyle;

		invokeNotify();
	}
}
