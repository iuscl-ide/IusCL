/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import org.iuscl.forms.IusCLForm.IusCLCloseAction;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

public class IusCLCloseEvent extends IusCLEvent {

	public IusCLCloseAction invoke(IusCLObject sender) {
		return (IusCLCloseAction) super.invoke(new IusCLParam(IusCLObject.class, sender));
	}
}
