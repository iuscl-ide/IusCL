/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import java.util.EnumSet;
import java.util.Set;

import org.iuscl.controls.IusCLControl.IusCLShiftState;
import org.iuscl.controls.IusCLKeyboardKey;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

public class IusCLKeyEvent extends IusCLEvent {

	public void invoke(IusCLObject sender, IusCLKeyboardKey key, Set<IusCLShiftState> shift) {
		super.invoke(new IusCLParam(IusCLObject.class, sender), new IusCLParam(IusCLKeyboardKey.class, key), new IusCLParam(EnumSet.class, shift));
	}
}
