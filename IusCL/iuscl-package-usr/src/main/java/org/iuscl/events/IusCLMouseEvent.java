/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import java.util.EnumSet;
import java.util.Set;

import org.iuscl.controls.IusCLControl.IusCLMouseButton;
import org.iuscl.controls.IusCLControl.IusCLShiftState;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

public class IusCLMouseEvent extends IusCLEvent {

	public void invoke(IusCLObject sender, IusCLMouseButton button, Set<IusCLShiftState> shift, Integer x, Integer y) {
		super.invoke(new IusCLParam(IusCLObject.class, sender), new IusCLParam(IusCLMouseButton.class, button), new IusCLParam(EnumSet.class, shift),
				new IusCLParam(Integer.class, x), new IusCLParam(Integer.class, y));
	}
}
