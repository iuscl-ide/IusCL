/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.forms;

import org.iuscl.extctrls.IusCLImage;
import org.iuscl.extctrls.IusCLPanel;
import org.iuscl.forms.IusCLForm;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.system.IusCLObject;

public class IusCLPluginActionsBaseForm extends IusCLForm {

	/* IusCL components */
	public IusCLImage headerImage;
	public IusCLPanel panel1;
	public IusCLPanel panel2;
	public IusCLPanel panel3;
	public IusCLButton btnCancel;
	public IusCLButton btnOK;
	public IusCLPanel panel4;
	public IusCLPanel panel5;

	/* form.OnCreate event implementation */
	public void formCreate(IusCLObject sender) {
		this.getConstraints().setMinWidth(this.getWidth());
		this.getConstraints().setMinHeight(this.getHeight());
	}

	/* btnOK.OnClick event implementation */
	public void btnOKClick(IusCLObject sender) {
		this.setModalResult(IusCLModalResult.mrOk);
	}

	/* btnCancel.OnClick event implementation */
	public void btnCancelClick(IusCLObject sender) {
		this.setModalResult(IusCLModalResult.mrCancel);
	}
}
