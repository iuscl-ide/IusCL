/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.sysutils.IusCLFileUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLFolderDialog extends IusCLCommonDialog {

	DirectoryDialog swtDirectoryDialog = null;

	@Getter
	String title = "Browse For Folder";
	@Getter
	String message = "";
	@Getter
	String folderName = "";

	public IusCLFolderDialog(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Title", IusCLPropertyType.ptString, "Browse For Folder");
		defineProperty("Message", IusCLPropertyType.ptString, "");
		defineProperty("FolderName", IusCLPropertyType.ptString, "");

		swtDirectoryDialog = new DirectoryDialog(this.findForm().getSwtShell());
	}

	@Override
	public boolean execute() {
		boolean result = false;
		String resultString = swtDirectoryDialog.open();
		if (resultString != null) {
			folderName = IusCLFileUtils.includeTrailingPathDelimiter(swtDirectoryDialog.getFilterPath());

			result = true;
		}

		return result;
	}

	@Override
	public Dialog getSwtDialog() {
		return swtDirectoryDialog;
	}

	public void setTitle(String title) {
		this.title = title;

		if (swtDirectoryDialog != null) {
			swtDirectoryDialog.setText(title);
		}
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;

		if (swtDirectoryDialog != null) {
			swtDirectoryDialog.setFilterPath(folderName);
		}
	}

	public void setMessage(String message) {
		this.message = message;

		if (swtDirectoryDialog != null) {
			swtDirectoryDialog.setMessage(message);
		}
	}
}
