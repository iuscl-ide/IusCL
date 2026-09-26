/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class IusCLConstructor {

	Class<?> clasz = null;
	IusCLParam[] constructorParams = null;

	public IusCLConstructor(Class<?> clasz, IusCLParam... constructorParams) {
		this.clasz = clasz;
		this.constructorParams = constructorParams;
	}

	public IusCLConstructor(String className, IusCLParam... constructorParams) {
		this.clasz = IusCLObjUtils.classForName(className);
		this.constructorParams = constructorParams;
	}

	public Object invokeConstructor() {
		boolean hasParams = true;
		if (constructorParams != null) {
			if (constructorParams.length == 0) {
				hasParams = false;
			}
		} else {
			hasParams = false;
		}
		Object constuctorResult = null;
		try {
			if (hasParams) {
				Class<?>[] constructorParamTypes = new Class<?>[constructorParams.length];
				Object[] constructorParamValues = new Object[constructorParams.length];
				int index = 0;
				for (IusCLParam constructorParam : constructorParams) {
					constructorParamTypes[index] = constructorParam.getParameterType();
					constructorParamValues[index] = constructorParam.getParameterValue();
					index++;
				}
				Constructor<?> constructor = clasz.getConstructor(constructorParamTypes);
				constuctorResult = constructor.newInstance(constructorParamValues);
			} else {
				Constructor<?> constructor = clasz.getConstructor();
				constuctorResult = constructor.newInstance();
			}
		} catch (Exception exception) {
			String exceptionMessage = MessageFormat.format("Error in invokeConstructor, for class: \"{0}\"", clasz.getCanonicalName());
			log.error(exceptionMessage, exception);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, exception);
		}
		return constuctorResult;
	}
}
