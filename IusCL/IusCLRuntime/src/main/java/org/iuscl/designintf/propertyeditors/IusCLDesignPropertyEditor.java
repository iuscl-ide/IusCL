/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

import lombok.Getter;

public class IusCLDesignPropertyEditor extends IusCLObject {

	protected Control swtEditorFocusControl = null;
	protected TreeItem swtTreeItem = null;

	@Getter
	protected String editorValue = null;

	protected IusCLPersistent persistent = null;
	protected String propertyName = null;

	protected FocusListener swtFocusLostListener = FocusListener.focusLostAdapter(_ -> IusCLDesignPropertyEditor.this.closeAndSaveEditor());

	protected KeyListener swtKeyReleasedListener = KeyListener.keyReleasedAdapter(keyEvent -> {
		switch (keyEvent.character) {
		case SWT.ESC:
			editorValue = swtTreeItem.getText(1);
			IusCLDesignPropertyEditor.this.closeEditor();
			break;
		case SWT.CR:
			IusCLDesignPropertyEditor.this.closeAndSaveEditor();
			break;
		default:
			break;
		}
	});

	public IusCLDesignPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		this.propertyName = (String) swtTreeItem.getData();
		this.swtTreeItem = swtTreeItem;
		this.persistent = persistent;
	}

	public Control getEditor() {
		return swtEditorFocusControl;
	}

	public String getEditorDisplayValue() {
		return null;
	}

	public void closeAndSaveEditor() {
		editorValue = getEditorValue();
		IusCLDesignPropertyEditor.this.closeEditor();
	}

	public void closeEditor() {
		if (swtTreeItem.isDisposed()) {
			return;
		}

		Object theView = swtTreeItem.getParent().getData();
		IusCLObjUtils.invokeMethod(theView, "changePropertyValue", new IusCLParam(TreeItem.class, swtTreeItem),
				new IusCLParam(String.class, editorValue));
	}

	protected void initializeEditor() {
		swtEditorFocusControl.setData(IusCLDesignPropertyEditor.this);
		swtEditorFocusControl.addFocusListener(swtFocusLostListener);
		swtEditorFocusControl.addKeyListener(swtKeyReleasedListener);
	}

	public static String getDisplayValue(String value) {
		return value;
	}
}
