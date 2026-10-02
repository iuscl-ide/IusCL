/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.forms;

import org.iuscl.dialogs.IusCLFolderDialog;
import org.iuscl.stdctrls.IusCLButton;
import org.iuscl.stdctrls.IusCLEdit;
import org.iuscl.stdctrls.IusCLLabel;
import org.iuscl.system.IusCLObject;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPluginApplicationDeployForm extends IusCLPluginActionsBaseForm {

	/* IusCL Components */
	public IusCLLabel pluginFolderTitleLabel;
	public IusCLLabel pluginFolderLabel;
	public IusCLLabel projectFolderTitleLabel;
	public IusCLLabel projectFolderLabel;
	public IusCLLabel distributionTitleLabel;
	public IusCLLabel deployFolderTitleLabel;
	public IusCLLabel deployFolderLabel;
	public IusCLEdit distributionEdit;
	public IusCLButton btnChange;

	public IusCLFolderDialog folderChangeDialog;
	/* Fields */
	IusCLPluginApplicationDeploy pluginApplicationDeploy;

	public IusCLPluginApplicationDeploy getPluginApplicationDeploy() {
		pluginApplicationDeploy.setDistributionFolder(distributionEdit.getText().trim());

		return pluginApplicationDeploy;
	}

	public void setPluginApplicationDeploy(IusCLPluginApplicationDeploy pluginApplicationDeploy) {
		this.pluginApplicationDeploy = pluginApplicationDeploy;

		pluginFolderLabel.setCaption(pluginApplicationDeploy.getPluginFolder());
		projectFolderLabel.setCaption(pluginApplicationDeploy.getProjectFolder());
		distributionEdit.setText(pluginApplicationDeploy.getDistributionFolder());
	}

	/* distributionEdit.OnChange event implementation */
	public void distributionEditChange(IusCLObject sender) {
		String deployFolder = "";

		if (IusCLStrUtils.isNotNullNotEmpty(distributionEdit.getText())
				&& IusCLStrUtils.isNotNullNotEmpty(pluginApplicationDeploy.getProjectName())) {
			deployFolder = IusCLFileUtils.includeTrailingPathDelimiter(distributionEdit.getText().trim()) + pluginApplicationDeploy.getProjectName();
		}

		deployFolderLabel.setCaption(deployFolder);
	}

	/* btnChange.OnClick event implementation */
	public void btnChangeClick(IusCLObject sender) {
		folderChangeDialog.setFolderName(distributionEdit.getText());
		if (folderChangeDialog.execute()) {
			distributionEdit.setText(folderChangeDialog.getFolderName());
		}
	}
}
