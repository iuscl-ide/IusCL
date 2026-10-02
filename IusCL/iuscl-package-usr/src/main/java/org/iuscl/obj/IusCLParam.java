/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLParam {

	@Getter
	Class<?> parameterType;
	@Getter
	Object parameterValue;

	public IusCLParam(Class<?> parameterType, Object parameterValue) {
		super();
		this.parameterType = parameterType;
		this.parameterValue = parameterValue;
	}
}
