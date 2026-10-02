/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLEvent {

	Object eventDeclaringInstance = null;
	String eventMethodName = null;

	protected Object invoke(IusCLParam... invokeParams) {
		return IusCLObjUtils.invokeMethod(eventDeclaringInstance, eventMethodName, invokeParams);
	}

	public static boolean isDefinedEvent(IusCLEvent event) {
		if (event == null) {
			return false;
		}
		if (event.getEventDeclaringInstance() == null) {
			return false;
		}
		return IusCLStrUtils.isNotNullNotEmpty(event.getEventMethodName());
	}
}
