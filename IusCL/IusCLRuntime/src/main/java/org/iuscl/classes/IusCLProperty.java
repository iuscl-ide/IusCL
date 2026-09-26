/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import org.iuscl.designintf.propertyeditors.IusCLDesignPropertyEditor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLProperty {

	String name;
	String propertyValueSetter;
	String propertyValueGetter;

	String type;
	boolean published;
	String defaultValue;

	Enum<?> enumeration;

	Class<?> refClass;

	IusCLDesignPropertyEditor propertyEditor;
}
