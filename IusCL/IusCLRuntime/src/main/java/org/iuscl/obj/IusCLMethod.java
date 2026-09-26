/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLMethod {

	Class<?> objectClass = null;
	String methodName = null;
	IusCLParam[] invokeParams = null;

	public IusCLMethod(Class<?> objectClass, String methodName, IusCLParam... invokeParams) {
		this.objectClass = objectClass;
		this.methodName = methodName;
		this.invokeParams = invokeParams;
	}

	public IusCLMethod(String methodName, IusCLParam... invokeParams) {
		this.methodName = methodName;
		this.invokeParams = invokeParams;
	}

	public Object invokeMethod(Object objectInstance) {
		Class<?> invokeObjectClass = null;
		if (this.objectClass == null) {
			invokeObjectClass = objectInstance.getClass();
		} else {
			invokeObjectClass = this.objectClass;
		}
		boolean hasParams = true;
		if (invokeParams != null) {
			if (invokeParams.length == 0) {
				hasParams = false;
			}
		} else {
			hasParams = false;
		}
		Object methodResult = null;
		try {
			if (hasParams) {
				Class<?>[] parameterTypes = new Class<?>[invokeParams.length];
				Object[] parameterValues = new Object[invokeParams.length];
				int index = 0;
				for (IusCLParam param : invokeParams) {
					parameterTypes[index] = param.getParameterType();
					parameterValues[index] = param.getParameterValue();
					index++;
				}
				Method method = invokeObjectClass.getMethod(methodName, parameterTypes);
				methodResult = method.invoke(objectInstance, parameterValues);
			} else {
				Method method = invokeObjectClass.getMethod(methodName);
				methodResult = method.invoke(objectInstance);
			}
		} catch (IllegalAccessException illegalAccessException) {
			String exceptionMessage = MessageFormat.format("IllegalAccessException in Reflection, invoked method: \"{0}\" from class: \"{1}\"",
					methodName, objectInstance.getClass().getSimpleName());
			log.error(exceptionMessage, illegalAccessException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, illegalAccessException);
		} catch (NoSuchMethodException noSuchMethodException) {
			String exceptionMessage = MessageFormat.format("NoSuchMethodException in Reflection, invoked method: \"{0}\" from class: \"{1}\"",
					methodName, objectInstance.getClass().getSimpleName());
			log.error(exceptionMessage, noSuchMethodException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, noSuchMethodException);
		} catch (InvocationTargetException invocationTargetException) {
			String exceptionMessage = MessageFormat.format("InvocationTargetException in Reflection, invoked method: \"{0}\" from class: \"{1}\"",
					methodName, objectInstance.getClass().getSimpleName());
			log.error(exceptionMessage, invocationTargetException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, invocationTargetException);
		}
		return methodResult;
	}
}
