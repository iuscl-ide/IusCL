/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.obj;

import java.text.MessageFormat;

import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLStaticMethod {

	String className = null;
	String staticMethodName = null;
	IusCLParam[] invokeParams = null;

	public IusCLStaticMethod(String className, String staticMethodName, IusCLParam... invokeParams) {
		this.className = className;
		this.staticMethodName = staticMethodName;
		this.invokeParams = invokeParams;
	}

	public Object invokeStatic() {
		Object res = null;

		try {
			Class<?> staticClass = IusCLObjUtils.classForName(className);
			IusCLMethod method = new IusCLMethod(staticClass, staticMethodName, invokeParams);
			res = method.invokeMethod(null);
		} catch (Exception exception) {
			String exceptionMessage = MessageFormat.format("Error in invokeStaticMethod: \"{0}\"", staticMethodName);
			log.error(exceptionMessage, exception);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, exception);
		}

		return res;
	}
}
