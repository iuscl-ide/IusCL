/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.menus.IusCLMenuItem;
import org.iuscl.obj.IusCLParam;

public class IusCLDesignMenuItemComponentEditor extends IusCLDesignDefaultComponentEditor {

	public IusCLDesignMenuItemComponentEditor() {
		setHasAdd(true);
	}

	@Override
	public IusCLDesignComponentEditorVerb verbAdd() {
		IusCLDesignComponentEditorVerb verbAdd = new IusCLDesignComponentEditorVerb();

		verbAdd.setMethodName("addDesignNonVisualComponent");

		IusCLParam[] params = new IusCLParam[2];
		params[0] = new IusCLParam(String.class, IusCLMenuItem.class.getCanonicalName());
		params[1] = new IusCLParam(IusCLComponent.class, this.getComponent());

		verbAdd.setParams(params);

		return verbAdd;
	}
}
