/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysutils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLFileSearchRec {

	// private Integer time;
	// private Integer size;
	// private Integer includeAttr;
	// private Integer excludeAttr;

	String includeNamePattern = null;
	String excludeNamePattern = null;

	boolean returnFiles = true;
	boolean returnFolders = false;

	boolean isRecursive = true;
	boolean isSorted = true;

	// Mode: mode_t;
	// FindHandle: Pointer;

	// private String pathOnly;
	// private String pattern;
}
