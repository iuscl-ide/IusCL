/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import org.iuscl.obj.IusCLParam;
import org.iuscl.stdctrls.IusCLScrollBar.IusCLScrollCode;
import org.iuscl.system.IusCLObject;

public class IusCLScrollEvent extends IusCLEvent {

	public Integer invoke(IusCLObject sender, IusCLScrollCode scrollCode, Integer scrollPos) {
		return (Integer) super.invoke(new IusCLParam(IusCLObject.class, sender), new IusCLParam(IusCLScrollCode.class, scrollCode),
				new IusCLParam(Integer.class, scrollPos));
	}
}
