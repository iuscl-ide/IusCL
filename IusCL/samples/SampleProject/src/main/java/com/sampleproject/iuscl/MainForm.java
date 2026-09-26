/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package com.sampleproject.iuscl;

import org.iuscl.dialogs.IusCLDialogs;
import org.iuscl.dialogs.IusCLDialogs.IusCLMessageBoxFlags;
import org.iuscl.dialogs.IusCLDialogs.IusCLMessageBoxIcon;
import org.iuscl.forms.IusCLForm;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.system.IusCLError;
import org.iuscl.system.IusCLObject;

import lombok.extern.slf4j.Slf4j;
import org.iuscl.extctrls.IusCLShape;
import org.iuscl.extctrls.IusCLBevel;
import org.iuscl.sysctrls.IusCLApplicationEvents;
import java.lang.Object;
import org.iuscl.extctrls.IusCLImage;
import org.iuscl.controls.IusCLKeyboardKey;
import org.iuscl.controls.IusCLControl.IusCLShiftState;
import java.util.EnumSet;
import org.iuscl.types.IusCLSize;
import org.iuscl.controls.IusCLSizeConstraints;
import org.iuscl.extctrls.IusCLPanel;

/** IusCL form class */
@Slf4j
public class MainForm extends IusCLForm {

	public IusCLShape shape1;
	public IusCLBevel bevel1;
	public IusCLApplicationEvents applicationEvents1;
	public IusCLImage image1;
	public IusCLPanel panel1;
	/* IusCL Components */
	public IusCLButton firstButton;
	/** firstButton.OnClick event implementation */
	public void firstButtonClick(IusCLObject sender) {

		
		
		//ConfigurationFactory.getInstance().getConfiguration(null, null, Paths.get("C:\\Endava\\EndevLocal\\IusCL\\IusCL\\samples\\SampleProject\\conf\\log4j2.properties").toUri());
		//Logger log = LogManager.getLogger();
		
		//log.info("info2");
		
log.info("info2");
log.warn("warn2");
//		IusCLLogger.logInfo("info");
		
//		log.info("{}{}", "\n", IusCLLogger.getLogConf());
		
try {
	int a  = 1 / 0;
} catch (Exception e) {
	
	log.error("error2", e);

	IusCLError.showErrorDialog("error2-1");
	IusCLError.showErrorDialog("error2", e);
}
log.info("info3");
		
		IusCLDialogs.messageBox("Hello World!", "Sample Project Message Box", IusCLMessageBoxIcon.mbiInformation, IusCLMessageBoxFlags.mbOK);
	}
	/** mainForm.OnKeyDown event implementation */
	public void mainFormKeyDown(IusCLObject sender,
		IusCLKeyboardKey key, EnumSet<IusCLShiftState> shift) {

		IusCLError.showErrorDialog("" + key);
	}
	/** image1.OnCanResize event implementation */
	public Boolean image1CanResize(IusCLObject sender, IusCLSize newSize) {

		
		return true;
	}	/** image1.OnConstrainedResize event implementation */
	public void image1ConstrainedResize(IusCLObject sender, IusCLSizeConstraints newSizeConstraints) {

		
	}
}