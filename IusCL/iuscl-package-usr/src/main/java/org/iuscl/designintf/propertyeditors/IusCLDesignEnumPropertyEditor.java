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

public class IusCLDesignEnumPropertyEditor extends IusCLDesignPropertyEditor {

	Combo enumComboEditor = null;

	public IusCLDesignEnumPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		Enum<?> enumeration = persistent.getProperty(propertyName).getEnumeration();

		enumComboEditor = new Combo(swtTreeItem.getParent(), SWT.READ_ONLY | SWT.BORDER);

		/* Load from enumeration */
		for (int index = 0; index < enumeration.getClass().getEnumConstants().length; index++) {
			enumComboEditor.add(enumeration.getClass().getEnumConstants()[index].name());
		}

		swtEditorFocusControl = enumComboEditor;
		initializeEditor();

		enumComboEditor.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			editorValue = getEditorValue();
			IusCLDesignEnumPropertyEditor.this.closeEditor();
		}));

		enumComboEditor.setText(swtTreeItem.getText(1));
		enumComboEditor.setFocus();
	}

	@Override
	public String getEditorValue() {
		return enumComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		enumComboEditor.dispose();

		super.closeEditor();
	}
}
