/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.events.IusCLMessageEvent;
import org.iuscl.forms.IusCLApplication;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLApplicationEvents extends IusCLComponent {

	@Getter
	boolean enabled = false;

	@Getter
	@Setter
	IusCLMessageEvent onMessage = null;

	public IusCLApplicationEvents(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Enabled", IusCLPropertyType.ptBoolean, "false");

		defineProperty("OnMessage", IusCLPropertyType.ptEvent, null, IusCLMessageEvent.class);

	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (IusCLApplication.getApplicationEvents().indexOf(this) == -1) {
			if (enabled) {
				IusCLApplication.getApplicationEvents().add(this);
			}
		} else {
			if (!enabled) {
				IusCLApplication.getApplicationEvents().remove(this);
			}
		}
	}
}
