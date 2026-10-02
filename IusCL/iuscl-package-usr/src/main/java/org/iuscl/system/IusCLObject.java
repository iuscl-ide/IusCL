/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.system;

import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLObject {

	IusCLObject notifyObject = null;
	String notifyObjectMethod = null;

	public void setNotify(IusCLObject notifyObject, String notifyObjectMethod) {
		this.notifyObject = notifyObject;
		this.notifyObjectMethod = notifyObjectMethod;
	}

	public void invokeNotify() {
		if (notifyObject != null) {
			IusCLObjUtils.invokeMethod(notifyObject, notifyObjectMethod, new IusCLParam(this.getClass(), this));
		}
	}

	public void free() {
		/* To free in here the memory (?) */
	}
}
