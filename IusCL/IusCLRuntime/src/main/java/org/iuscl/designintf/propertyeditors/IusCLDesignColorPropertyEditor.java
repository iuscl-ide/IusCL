/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDesignColorPropertyEditor extends IusCLDesignPropertyEditor {

	Combo enumColorComboEditor = null;
	IusCLColor color = null;

	public IusCLDesignColorPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		color = (IusCLColor) (persistent.getPropertyValueInvoke(propertyName));

		Enum<?> enumeration = persistent.getProperty(propertyName).getEnumeration();

		enumColorComboEditor = new Combo(swtTreeItem.getParent(), SWT.BORDER);

		/* Load from enumeration */
		for (int index = 0; index < enumeration.getClass().getEnumConstants().length; index++) {
			enumColorComboEditor.add(enumeration.getClass().getEnumConstants()[index].name());
		}

		swtEditorFocusControl = enumColorComboEditor;
		initializeEditor();

		enumColorComboEditor.addMouseListener(MouseListener.mouseDoubleClickAdapter(_ -> {
			enumColorComboEditor.removeFocusListener(swtFocusLostListener);
			ColorDialog colorDialog = new ColorDialog(IusCLApplication.getSwtApplicationShell());
			colorDialog.setText(propertyName);
			colorDialog.setRGB(new RGB(color.getRed(), color.getGreen(), color.getBlue()));
			RGB returnRGB = colorDialog.open();
			if (returnRGB != null) {
				IusCLColor returnColor = new IusCLColor(returnRGB.red, returnRGB.green, returnRGB.blue);
				enumColorComboEditor.setText(returnColor.getAsString());
			}
			enumColorComboEditor.addFocusListener(swtFocusLostListener);
		}));

		enumColorComboEditor.setText(swtTreeItem.getText(1));
		enumColorComboEditor.setFocus();
	}

	public static void getDisplayPaint(IusCLPersistent persistent, String propertyName, TreeItem swtTreeItem, Event swtEvent, String value) {
		try {
			GC gc = swtEvent.gc;

			IusCLColor color = (IusCLColor) persistent.getPropertyValueInvoke(propertyName);

			int x = swtEvent.x;
			int y = swtEvent.y;
			int h = swtTreeItem.getBounds(1).height;
			int w = swtTreeItem.getBounds(1).width;

			gc.fillRectangle(x + 1, y + 1, w - 1, h - 1);

			gc.setForeground(swtTreeItem.getForeground(1));
			gc.drawRectangle(x + 2, y + 2, h - 5, h - 5);

			gc.setBackground(color.getAsSwtColor());
			gc.fillRectangle(x + 3, y + 3, h - 6, h - 6);

			gc.setFont(swtTreeItem.getFont(1));
			gc.drawString(value, x + h + 2, y + 4, true);
		} catch (Exception exception) {
			String exceptionMessage = MessageFormat.format("Color getDisplayPaint exception for property name: \"{0}\"", propertyName);
			log.error(exceptionMessage, exception);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, exception);
		}
	}

	@Override
	public String getEditorValue() {
		return enumColorComboEditor.getText();
	}

	@Override
	public void closeEditor() {
		enumColorComboEditor.dispose();

		super.closeEditor();
	}
}
