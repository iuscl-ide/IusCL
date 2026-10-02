/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLControl;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.forms.IusCLForm;

public class IusCLDesignRefComponentPropertyEditor extends IusCLDesignPropertyEditor {

	Combo componentComboEditor = null;

	public IusCLDesignRefComponentPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		componentComboEditor = new Combo(swtTreeItem.getParent(), SWT.BORDER);

		/* Load from enumeration */
		Class<?> refComponentClass = persistent.getProperty(propertyName).getRefClass();

		IusCLComponent persistentComponent = persistent.getPersistentComponent();

		if ((persistentComponent instanceof IusCLWinControl) && (IusCLControl.class.isAssignableFrom(refComponentClass))) {
			/* Control */
			IusCLContainerControl parentControl = (IusCLContainerControl) persistentComponent;

			for (int indexControl = 0; indexControl < parentControl.getControls().size(); indexControl++) {
				IusCLControl typeControl = parentControl.getControls().get(indexControl);

				if (refComponentClass.isAssignableFrom(typeControl.getClass())) {
					componentComboEditor.add(typeControl.getName());
				}
			}
		} else {
			/* Non-visual component */
			IusCLForm form = persistent.getPersistentForm();

			for (int indexComponent = 0; indexComponent < form.getComponents().size(); indexComponent++) {
				IusCLComponent typeComponent = form.getComponents().get(indexComponent);

				if (refComponentClass.isAssignableFrom(typeComponent.getClass())) {
					componentComboEditor.add(typeComponent.getName());
				}
			}
		}

		swtEditorFocusControl = componentComboEditor;
		initializeEditor();

		componentComboEditor.setText(swtTreeItem.getText(1));
		componentComboEditor.setFocus();
	}

	@Override
	public String getEditorValue() {
		return componentComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		componentComboEditor.dispose();

		super.closeEditor();
	}
}
