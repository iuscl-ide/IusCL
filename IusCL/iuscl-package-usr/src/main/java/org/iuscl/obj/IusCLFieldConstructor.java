/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLFieldConstructor {

	String fieldName = null;
	IusCLParam[] constructorParams = null;

	public IusCLFieldConstructor(String fieldName, IusCLParam... constructorParams) {
		this.fieldName = fieldName;
		this.constructorParams = constructorParams;
	}

	public Object invokeFieldConstructor(Object declaringInstance) {
		Object fieldInstance = null;
		try {
			Field field = declaringInstance.getClass().getField(fieldName);
			fieldInstance = IusCLObjUtils.invokeConstructor(field.getType().getCanonicalName(), constructorParams);
			field.set(declaringInstance, fieldInstance); // NOSONAR
		} catch (Exception exception) {
			String exceptionMessage = MessageFormat.format("Error in invokeFieldConstructor, for the field name: \"{0}\"", fieldName);
			log.error(exceptionMessage, exception);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, exception);
			return null;
		}
		return fieldInstance;
	}
}
