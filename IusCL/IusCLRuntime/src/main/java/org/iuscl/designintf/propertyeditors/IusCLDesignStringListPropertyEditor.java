/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDesignStringListPropertyEditor extends IusCLDesignPropertyEditor {

	Composite stringsComposite;
	Button stringsButton;
	Text stringsText;

	String propertyValue;

	String lines = null;

	public IusCLDesignStringListPropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		propertyValue = swtTreeItem.getText(1);

		/* Editor cell */
		stringsComposite = new Composite(swtTreeItem.getParent(), SWT.NONE);
		// stringsComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		GridLayout stringsCompositeGridLayout = new GridLayout();
		stringsCompositeGridLayout.numColumns = 2;
		stringsCompositeGridLayout.marginTop = 0;
		stringsCompositeGridLayout.marginBottom = 0;
		stringsCompositeGridLayout.marginLeft = 0;
		stringsCompositeGridLayout.marginRight = 0;
		stringsCompositeGridLayout.verticalSpacing = 0;
		stringsCompositeGridLayout.horizontalSpacing = 0;
		stringsCompositeGridLayout.marginWidth = 0;
		stringsCompositeGridLayout.marginHeight = 0;

		stringsComposite.setLayout(stringsCompositeGridLayout);
		swtEditorFocusControl = stringsComposite;

		stringsText = new Text(stringsComposite, SWT.READ_ONLY | SWT.BORDER);
		GridData gridDataText = new GridData();
		gridDataText.heightHint = 10;
		gridDataText.horizontalAlignment = GridData.FILL;
		gridDataText.verticalAlignment = GridData.FILL;
		gridDataText.grabExcessHorizontalSpace = true;
		gridDataText.grabExcessVerticalSpace = true;
		stringsText.setLayoutData(gridDataText);
		stringsText.setText("(IusCLStrings)");

		stringsButton = new Button(stringsComposite, SWT.PUSH);
		stringsButton.setText("...");
		GridData gridDataButton = new GridData();
		gridDataButton.heightHint = 10;
		gridDataButton.horizontalAlignment = GridData.END;
		gridDataButton.verticalAlignment = GridData.FILL;
		stringsButton.setLayoutData(gridDataButton);

		swtEditorFocusControl.setData(IusCLDesignStringListPropertyEditor.this);

		stringsComposite.addKeyListener(swtKeyReleasedListener);
		stringsText.addKeyListener(swtKeyReleasedListener);
		stringsButton.addKeyListener(swtKeyReleasedListener);

		stringsButton.addFocusListener(swtFocusLostListener);
		stringsText.addFocusListener(swtFocusLostListener);

		stringsButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> clickButton()));

		stringsButton.setFocus();
	}

	private void clickButton() {
		/* Editor shell */
		final Shell stringsShell = new Shell(SWT.CLOSE | SWT.BORDER | SWT.RESIZE | SWT.APPLICATION_MODAL);
		stringsShell.setText("String List Editor");

		InputStream inputStream = IusCLDesignStringListPropertyEditor.class.getResourceAsStream("/resources/images/IusCLPerspective.gif");
		Image imageIusCL = new Image(Display.getCurrent(), inputStream);
		stringsShell.setImage(imageIusCL);

		Monitor primary = Display.getDefault().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		stringsShell.setSize(640, 640);
		Rectangle rect = stringsShell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		stringsShell.setLocation(x, y);

		GridLayout stringsShellGridLayout = new GridLayout();
		stringsShellGridLayout.numColumns = 1;
		stringsShellGridLayout.marginTop = 8;
		stringsShellGridLayout.marginBottom = 8;
		stringsShellGridLayout.marginLeft = 8;
		stringsShellGridLayout.marginRight = 8;
		stringsShellGridLayout.verticalSpacing = 8;
		stringsShellGridLayout.horizontalSpacing = 0;
		stringsShellGridLayout.marginWidth = 0;
		stringsShellGridLayout.marginHeight = 0;
		stringsShell.setLayout(stringsShellGridLayout);

		Composite upComposite = new Composite(stringsShell, SWT.BORDER);
		GridData gridDataUpComposite = new GridData();
		gridDataUpComposite.horizontalAlignment = GridData.FILL;
		gridDataUpComposite.verticalAlignment = GridData.FILL;
		gridDataUpComposite.grabExcessHorizontalSpace = true;
		gridDataUpComposite.grabExcessVerticalSpace = true;
		upComposite.setLayoutData(gridDataUpComposite);
		upComposite.setLayout(stringsShellGridLayout);

		final Label stringsLabel = new Label(upComposite, SWT.NONE);
		GridData gridDataStringsLabel = new GridData();
		gridDataStringsLabel.horizontalAlignment = GridData.FILL;
		gridDataStringsLabel.grabExcessHorizontalSpace = true;
		stringsLabel.setLayoutData(gridDataStringsLabel);
		stringsLabel.setText("0 lines");

		final Text stringsMemo = new Text(upComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridDataStringsMemo = new GridData();
		gridDataStringsMemo.horizontalAlignment = GridData.FILL;
		gridDataStringsMemo.verticalAlignment = GridData.FILL;
		gridDataStringsMemo.grabExcessHorizontalSpace = true;
		gridDataStringsMemo.grabExcessVerticalSpace = true;
		stringsMemo.setLayoutData(gridDataStringsMemo);

		// IusCLForm form = persistent.getPersistentForm();
		// String formClassName = form.getClass().getCanonicalName();
		// final String resFileName = IusCLFileUtils.includeTrailingPathDelimiter(
		// IusCLApplication.getResFolder(formClassName));

		/* Strings */
		if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
			IusCLStrings strings = new IusCLStrings();
			strings.loadFromFile(IusCLApplication.getFormsResFolder(persistent.getPersistentForm().getClass()) + propertyValue);
			lines = strings.getText();
			stringsMemo.setText(lines);

			// editorLines =
			// ((IusCLStrings)persistent.getPropertyValueInvoke(propertyName)).getText();
		} else {
			stringsMemo.setText("");
		}

		stringsMemo.addModifyListener(swtModifyEvent -> stringsLabel.setText(noOfLines(stringsMemo)));

		stringsLabel.setText(noOfLines(stringsMemo));

		Composite downComposite = new Composite(stringsShell, SWT.NONE);
		GridData gridDataDownComposite = new GridData();
		gridDataDownComposite.horizontalAlignment = GridData.FILL;
		gridDataDownComposite.grabExcessHorizontalSpace = true;
		downComposite.setLayoutData(gridDataDownComposite);

		GridLayout downCompositeGridLayout = new GridLayout();
		downCompositeGridLayout.numColumns = 3;
		downCompositeGridLayout.marginTop = 0;
		downCompositeGridLayout.marginBottom = 0;
		downCompositeGridLayout.marginLeft = 0;
		downCompositeGridLayout.marginRight = 0;
		downCompositeGridLayout.verticalSpacing = 0;
		downCompositeGridLayout.horizontalSpacing = 8;
		downCompositeGridLayout.marginWidth = 0;
		downCompositeGridLayout.marginHeight = 0;
		downComposite.setLayout(downCompositeGridLayout);

		// final Button checkButton = new Button(downComposite, SWT.CHECK);
		// GridData gridDataCheckButton = new GridData();
		// gridDataCheckButton.heightHint = 24;
		// gridDataCheckButton.horizontalAlignment = GridData.FILL;
		// gridDataCheckButton.grabExcessHorizontalSpace = true;
		// checkButton.setLayoutData(gridDataCheckButton);
		// checkButton.setText("The editor lines (not the saved lines)");
		//
		// checkButton.addSelectionL istener(new SelectionAdapter() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent swtSelectionEvent) {
		//
		// if (checkButton.getSelection()) {
		//
		// savedLines = stringsMemo.getText();
		// stringsMemo.setText(editorLines);
		// }
		// else {
		//
		// editorLines = stringsMemo.getText();
		// stringsMemo.setText(savedLines);
		// }
		// }
		// });

		// Composite leftComposite = new Composite(downComposite, SWT.NONE);
		// GridData gridDataLeftComposite = new GridData();
		// gridDataLeftComposite.heightHint = 10;
		// gridDataLeftComposite.horizontalAlignment = GridData.FILL;
		// gridDataLeftComposite.grabExcessHorizontalSpace = true;
		// leftComposite.setLayoutData(gridDataLeftComposite);

		Button okButton = new Button(downComposite, SWT.PUSH);
		GridData gridDataOkButton = new GridData();
		gridDataOkButton.widthHint = 90;
		okButton.setLayoutData(gridDataOkButton);
		okButton.setText("OK");

		okButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			String retText = stringsMemo.getText();

			if (IusCLStrUtils.isNotNullNotEmpty(retText)) {
				IusCLStrings strings = new IusCLStrings();
				strings.setText(retText);
				String txtFile = persistent.getPersistentFormAndResName(propertyName) + ".txt";
				String destFileName = IusCLApplication.getFormsResFolder(persistent.getPersistentForm().getClass())
						+ txtFile.replace("/", IusCLFileUtils.getPathDelimiter());
				strings.saveToFile(destFileName);

				propertyValue = "*" + txtFile;
			} else {
				/* Delete resource */
				propertyValue = "";
			}

			stringsButton.addFocusListener(swtFocusLostListener);
			stringsShell.dispose();
		}));

		Button cancelButton = new Button(downComposite, SWT.PUSH);
		GridData gridDataCancelButton = new GridData();
		gridDataCancelButton.widthHint = 90;
		cancelButton.setLayoutData(gridDataCancelButton);
		cancelButton.setText("Cancel");

		cancelButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			stringsButton.addFocusListener(swtFocusLostListener);
			stringsShell.dispose();
		}));

		stringsButton.removeFocusListener(swtFocusLostListener);
		stringsShell.open();
	}

	private String noOfLines(Text text) {
		String res = "";

		if (text.getLineCount() == 1) {
			if (text.getText().isEmpty()) {
				res = "0 lines";
			} else {
				res = "1 line";
			}
		} else {
			res = text.getLineCount() + " lines";
		}

		return res;
	}

	@Override
	public String getEditorValue() {
		return propertyValue;
	}

	@Override
	public void closeEditor() {
		stringsText.dispose();
		stringsButton.dispose();
		stringsComposite.dispose();

		super.closeEditor();
	}
}
