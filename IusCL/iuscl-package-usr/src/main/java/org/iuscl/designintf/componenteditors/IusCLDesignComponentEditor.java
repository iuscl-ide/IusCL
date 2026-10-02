/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.eclipse.swt.widgets.MenuItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.system.IusCLObject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLDesignComponentEditor extends IusCLObject {

	IusCLComponent component = null;
	boolean hasAdd = false;
	boolean hasOrder = false;

	public IusCLDesignComponentEditorVerb getVerbSerializeAndBroadcastChange() {
		IusCLDesignComponentEditorVerb verb = new IusCLDesignComponentEditorVerb();
		verb.setMethodName("serializeAndBroadcastChange");
		return verb;
	}

	public IusCLDesignComponentEditorVerb edit() {
		/* Execute the first verb */
		if (getVerbCount() > 0) {
			return executeVerb(0);
		}
		return null;
	}

	public IusCLDesignComponentEditorVerb verbAdd() {
		return null;
	}

	public IusCLDesignComponentEditorVerb verbOrder(int firstIndex, int secondIndex) { // NOSONAR
		return null;
	}

	public IusCLDesignComponentEditorVerb executeVerb(int index) {
		return null;
	}

	public String getVerb(int index) {
		return null;
	}

	public int getVerbCount() {
		return 0;
	}

	public void prepareItem(int index, MenuItem swtMenuItem) {
		/*  */
	}
}
