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

public class IusCLDesignIntegerPropertyEditor extends IusCLDesignPropertyEditor {

	Text swtIntegerEditorText = null;

	public IusCLDesignIntegerPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		swtIntegerEditorText = new Text(swtTreeItem.getParent(), SWT.BORDER);
		swtEditorFocusControl = swtIntegerEditorText;
		initializeEditor();

		swtIntegerEditorText.setText(swtTreeItem.getText(1));
		swtIntegerEditorText.selectAll();
		swtIntegerEditorText.setFocus();
	}

	@Override
	public String getEditorValue() {
		return swtIntegerEditorText.getText();
	}

	@Override
	public void closeEditor() {
		swtIntegerEditorText.dispose();

		super.closeEditor();
	}
}
