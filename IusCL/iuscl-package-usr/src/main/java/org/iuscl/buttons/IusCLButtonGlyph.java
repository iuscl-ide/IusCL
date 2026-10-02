/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.buttons;

import org.iuscl.graphics.IusCLPicture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLButtonGlyph {

	IusCLPicture up;
	IusCLPicture disabled;
	IusCLPicture clicked;
	IusCLPicture down;
}
