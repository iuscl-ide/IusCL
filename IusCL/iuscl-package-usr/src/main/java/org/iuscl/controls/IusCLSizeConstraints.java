/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import org.iuscl.classes.IusCLPersistent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLSizeConstraints extends IusCLPersistent {

	Integer maxHeight = 0;
	Integer maxWidth = 0;
	Integer minHeight = 0;
	Integer minWidth = 0;
}
