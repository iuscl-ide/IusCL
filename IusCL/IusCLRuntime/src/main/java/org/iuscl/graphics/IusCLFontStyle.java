/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import org.iuscl.system.IusCLObject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLFontStyle extends IusCLObject {

	@Getter
	boolean bold = false;
	@Getter
	boolean italic = false;
	@Getter
	boolean underline = false;
	@Getter
	boolean strikeOut = false;

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
}
