/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import java.util.EnumSet;
import java.util.Set;

import org.iuscl.controls.IusCLControl.IusCLShiftState;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

public class IusCLMouseWheelEvent extends IusCLEvent {

	public boolean invoke(IusCLObject sender, Set<IusCLShiftState> shift, Integer wheelDelta, Integer x, Integer y) {
		return (boolean) super.invoke(new IusCLParam(IusCLObject.class, sender), new IusCLParam(EnumSet.class, shift),
				new IusCLParam(Integer.class, wheelDelta), new IusCLParam(Integer.class, x), new IusCLParam(Integer.class, y));
	}
}
