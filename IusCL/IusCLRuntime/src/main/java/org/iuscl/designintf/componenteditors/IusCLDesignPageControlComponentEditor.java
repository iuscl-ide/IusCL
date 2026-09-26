/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.comctrls.IusCLTabSheet;
import org.iuscl.controls.IusCLControl;
import org.iuscl.obj.IusCLParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IusCLDesignPageControlComponentEditor extends IusCLDesignDefaultComponentEditor {

	@Override
	public IusCLDesignComponentEditorVerb executeVerb(int index) {
		IusCLDesignComponentEditorVerb verb = new IusCLDesignComponentEditorVerb();

		switch (index) {
		case 0:
			verb.setMethodName("addDesignControl");

			IusCLParam[] params = new IusCLParam[2];
			params[0] = new IusCLParam(String.class, IusCLTabSheet.class.getCanonicalName());
			params[1] = new IusCLParam(IusCLControl.class, this.getComponent());

			verb.setParams(params);

			break;
		case 1:
			log.debug("Next Page");
			break;
		case 2:
			log.debug("Previous Page");
			break;
		case 3:
			log.debug("Delete Page");
			break;
		default:
			break;
		}

		return verb;
	}

	@Override
	public String getVerb(int index) {
		String verb = null;

		switch (index) {
		case 0:
			verb = "New Page";
			break;
		case 1:
			verb = "Next Page";
			break;
		case 2:
			verb = "Previous Page";
			break;
		case 3:
			verb = "Delete Page";
			break;
		default:
			break;
		}

		return verb;
	}

	@Override
	public int getVerbCount() {
		/* TODO The others, maybe never */

		return 1;
	}
}
