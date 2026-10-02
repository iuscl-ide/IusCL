/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import java.text.MessageFormat;

import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class IusCLObjUtils {

	public Class<?> classForName(String className) {
		Class<?> clasz = null;
		try {
			clasz = Class.forName(className);
		} catch (ClassNotFoundException classNotFoundException) {
			String exceptionMessage = MessageFormat.format("Exception in classForName, class name: \"{0}\"", className);
			log.error(exceptionMessage, classNotFoundException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, classNotFoundException);
		}
		return clasz;
	}

	public Object invokeConstructor(String className, IusCLParam... constructorParams) {
		IusCLConstructor constructor = new IusCLConstructor(className, constructorParams);
		return constructor.invokeConstructor();
	}

	public Object invokeConstructor(Class<?> clazz, IusCLParam... constructorParams) {
		IusCLConstructor constructor = new IusCLConstructor(clazz, constructorParams);
		return constructor.invokeConstructor();
	}

	public Object invokeStaticMethod(String className, String staticMethodName, IusCLParam... invokeParams) {
		IusCLStaticMethod staticMethod = new IusCLStaticMethod(className, staticMethodName, invokeParams);
		return staticMethod.invokeStatic();
	}

	public Object invokeMethod(Object objectInstance, String methodName, IusCLParam... invokeParams) {
		return invokeMethod(objectInstance, objectInstance.getClass(), methodName, invokeParams);
	}

	private Object invokeMethod(Object objectInstance, Class<?> objectClass, String methodName, IusCLParam... invokeParams) {
		IusCLMethod method = new IusCLMethod(objectClass, methodName, invokeParams);
		return method.invokeMethod(objectInstance);
	}

	public Object invokeFieldConstructor(Object declaringInstance, String fieldName, IusCLParam... constructorParams) {
		IusCLFieldConstructor fieldConstructor = new IusCLFieldConstructor(fieldName, constructorParams);
		return fieldConstructor.invokeFieldConstructor(declaringInstance);
	}

	public boolean equalInstances(Object oneInstance, Object anotherInstance) {
		boolean result = false;
		if (oneInstance == null) {
			if (anotherInstance == null) {
				result = true;
			}
		} else {
			if (oneInstance.equals(anotherInstance)) {
				result = true;
			}
		}
		return result;
	}
}
