/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;

public class IusCLDesignStringPropertyEditor extends IusCLDesignPropertyEditor {

	Text swtStringEditorText = null;

	public IusCLDesignStringPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		swtStringEditorText = new Text(swtTreeItem.getParent(), SWT.BORDER);
		swtEditorFocusControl = swtStringEditorText;
		initializeEditor();

		swtStringEditorText.setText(swtTreeItem.getText(1));
		swtStringEditorText.selectAll();
		swtStringEditorText.setFocus();
	}

	@Override
	public String getEditorValue() {
		return swtStringEditorText.getText();
	}

	@Override
	public void closeEditor() {
		swtStringEditorText.dispose();

		super.closeEditor();
	}
}
