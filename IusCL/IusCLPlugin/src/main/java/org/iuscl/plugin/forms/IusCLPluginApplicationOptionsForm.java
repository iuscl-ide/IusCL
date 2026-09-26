/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.forms;

import org.iuscl.dialogs.IusCLOpenDialog;
import org.iuscl.extctrls.IusCLImage;
import org.iuscl.extctrls.IusCLPanel;
import org.iuscl.graphics.IusCLGraphic;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.graphics.formats.IusCLIcon;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.stdctrls.IusCLEdit;
import org.iuscl.stdctrls.IusCLLabel;
import org.iuscl.system.IusCLObject;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPluginApplicationOptionsForm extends IusCLPluginActionsBaseForm {

	/* IusCL Components */
	public IusCLLabel projectFolderTitleLabel;
	public IusCLLabel projectFolderLabel;
	public IusCLLabel applicationNameTitleLabel;
	public IusCLLabel applicationNameLabel;
	public IusCLLabel applicationTitleLabel;
	public IusCLLabel applicationIconLabel;
	public IusCLEdit applicationTitleEdit;
	public IusCLImage image48true;
	public IusCLPanel iconPanel;
	public IusCLImage image48;
	public IusCLImage image32true;
	public IusCLButton btnOpen;
	public IusCLImage image32;
	public IusCLImage image16true;
	public IusCLImage image16;
	public IusCLOpenDialog openIconDialog;

	/* Fields */
	IusCLPluginApplicationOptions pluginApplicationOptions;
	String applicationIcon;

	public IusCLPluginApplicationOptions getPluginApplicationOptions() {
		pluginApplicationOptions.setApplicationTitle(applicationTitleEdit.getText().trim());
		pluginApplicationOptions.setApplicationIcon(applicationIcon);

		return pluginApplicationOptions;
	}

	public void setPluginApplicationOptions(IusCLPluginApplicationOptions pluginApplicationOptions) {
		this.pluginApplicationOptions = pluginApplicationOptions;

		projectFolderLabel.setCaption(pluginApplicationOptions.getProjectFolder());
		applicationNameLabel.setCaption(pluginApplicationOptions.getProjectName());
		applicationTitleEdit.setText(pluginApplicationOptions.getApplicationTitle());

		applicationIcon = pluginApplicationOptions.getApplicationIcon();
		displayIcon();
	}

	private void displayIcon() {
		image16.setPicture(null);
		image16true.setPicture(null);
		image32.setPicture(null);
		image32true.setPicture(null);
		image48.setPicture(null);
		image48true.setPicture(null);

		if (IusCLFileUtils.fileExists(applicationIcon)) {
			IusCLIcon icon = new IusCLIcon();
			icon.loadFromFile(applicationIcon);

			for (int index = 0; index < icon.size(); index++) {
				IusCLGraphic graphic = icon.get(index);
				IusCLPicture picture = new IusCLPicture();
				picture.setGraphic(graphic);

				switch (index) {
				case 0:
					image16.setPicture(picture);
					break;
				case 1:
					image16true.setPicture(picture);
					break;
				case 2:
					image32.setPicture(picture);
					break;
				case 3:
					image32true.setPicture(picture);
					break;
				case 4:
					image48.setPicture(picture);
					break;
				case 5:
					image48true.setPicture(picture);
					break;
				default:
					break;
				}
			}
		} else {
			applicationIcon = "";
		}
	}

	/* btnOpen.OnClick event implementation */
	public void btnOpenClick(IusCLObject sender) {
		if (IusCLStrUtils.isNotNullNotEmpty(applicationIcon)) {
			openIconDialog.setFileName(applicationIcon);
		}

		if (openIconDialog.execute()) {
			applicationIcon = openIconDialog.getFileName();
			displayIcon();
		}
	}
}
