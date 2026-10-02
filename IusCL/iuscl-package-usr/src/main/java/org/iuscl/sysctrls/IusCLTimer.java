/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysctrls;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.forms.IusCLApplication;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTimer extends IusCLComponent {

	@Getter
	boolean enabled = false;
	@Getter
	@Setter
	Integer interval = 1000;

	@Getter
	@Setter
	IusCLNotifyEvent onTimer = null;

	Runnable swtTimer = null;

	public IusCLTimer(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Enabled", IusCLPropertyType.ptBoolean, "false");
		defineProperty("Interval", IusCLPropertyType.ptInteger, "1000");

		defineProperty("OnTimer", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		swtTimer = new Runnable() {

			public void run() {
				if (formIsInDesignMode) {
					return;
				}

				if (IusCLEvent.isDefinedEvent(onTimer)) {
					onTimer.invoke(IusCLTimer.this);
				}

				if (IusCLTimer.this.enabled) {
					IusCLApplication.getSwtDisplay().timerExec(interval, swtTimer);
				}
			}
		};
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (enabled) {
			IusCLApplication.getSwtDisplay().timerExec(interval, swtTimer);
		} else {
			IusCLApplication.getSwtDisplay().timerExec(-1, swtTimer);
		}
	}

	@Override
	public void free() {
		super.free();

		setEnabled(false);
	}
}
