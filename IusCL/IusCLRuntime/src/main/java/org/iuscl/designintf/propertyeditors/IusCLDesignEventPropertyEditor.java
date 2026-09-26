/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.classes.IusCLPersistent.IusCLPropertyType;
import org.iuscl.classes.IusCLProperty;
import org.iuscl.forms.IusCLForm;
import org.iuscl.sysutils.IusCLStrUtils;

public class IusCLDesignEventPropertyEditor extends IusCLDesignPropertyEditor {

	Combo eventComboEditor = null;

	public IusCLDesignEventPropertyEditor(final TreeItem swtTreeItem, final IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		eventComboEditor = new Combo(swtTreeItem.getParent(), SWT.BORDER);

		IusCLForm form = persistent.getPersistentForm();

		/* Load from enumeration */
		Class<?> eventClass = persistent.getProperty(propertyName).getRefClass();

		for (int indexComponent = 0; indexComponent < form.getComponents().size(); indexComponent++) {
			IusCLComponent eventComponent = form.getComponents().get(indexComponent);
			for (Iterator<IusCLProperty> iterator = eventComponent.getProperties().values().iterator(); iterator.hasNext();) {
				IusCLProperty property = iterator.next();
				if ((IusCLStrUtils.equalValues(property.getType(), IusCLPropertyType.ptEvent.name())) && (property.getRefClass() == eventClass)) {
					String eventImplementation = eventComponent.getPropertyValue(property.getName());
					if (IusCLStrUtils.isNotNullNotEmpty(eventImplementation)) {
						eventComboEditor.add(eventImplementation);
					}
				}
			}
		}

		swtEditorFocusControl = eventComboEditor;
		initializeEditor();

		eventComboEditor.addMouseListener(MouseListener.mouseDoubleClickAdapter(swtMouseEvent -> {
			if (!IusCLStrUtils.isNotNullNotEmpty(eventComboEditor.getText())) {
				String persistentName = persistent.getPersistentName();
				String eventFunctionName = persistentName + propertyName.substring(2);
				eventComboEditor.setText(eventFunctionName);
			}
			IusCLDesignEventPropertyEditor.this.closeAndSaveEditor();
		}));

		eventComboEditor.setText(swtTreeItem.getText(1));
		eventComboEditor.setFocus();
	}

	@Override
	public String getEditorValue() {
		return eventComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		eventComboEditor.dispose();

		super.closeEditor();
	}
}
