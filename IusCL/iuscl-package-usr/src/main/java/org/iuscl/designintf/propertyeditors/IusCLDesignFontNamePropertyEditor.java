/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.forms.IusCLScreen;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDesignFontNamePropertyEditor extends IusCLDesignPropertyEditor {

	Combo enumFontNameComboEditor = null;

	public IusCLDesignFontNamePropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		enumFontNameComboEditor = new Combo(swtTreeItem.getParent(), SWT.BORDER);

		/* Load font names */
		for (int index = 0; index < IusCLScreen.getFonts().size(); index++) {
			enumFontNameComboEditor.add(IusCLScreen.getFonts().get(index));
		}

		swtEditorFocusControl = enumFontNameComboEditor;
		initializeEditor();

		enumFontNameComboEditor.setText(swtTreeItem.getText(1));
		enumFontNameComboEditor.setFocus();
	}

	@Override
	public String getEditorValue() {
		return enumFontNameComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		enumFontNameComboEditor.dispose();

		super.closeEditor();
	}
}
