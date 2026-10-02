/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;

public class IusCLDesignBooleanPropertyEditor extends IusCLDesignPropertyEditor {

	Combo booleanComboEditor = null;

	public IusCLDesignBooleanPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		booleanComboEditor = new Combo(swtTreeItem.getParent(), SWT.READ_ONLY | SWT.BORDER);
		/* Load from enumeration */
		booleanComboEditor.add("false");
		booleanComboEditor.add("true");

		swtEditorFocusControl = booleanComboEditor;
		initializeEditor();

		booleanComboEditor.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			editorValue = getEditorValue();
			IusCLDesignBooleanPropertyEditor.this.closeEditor();
		}));

		booleanComboEditor.setText(swtTreeItem.getText(1));
		booleanComboEditor.setFocus();
	}

	@Override
	public String getEditorValue() {
		return booleanComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		super.closeEditor();

		booleanComboEditor.dispose();
	}
}
