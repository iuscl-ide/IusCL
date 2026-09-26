/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.ide.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.iuscl.plugin.preferences.IusCLDesignPreferences;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLNewApplicationWizardPage extends WizardPage {

	Text projectNameText;
	Text projectLocationText;
	Text packageNameText;
	Text formNameText;

	Label projectPathLabel;
	Label packageLabel;
	Label formInstanceNameLabel;

	public IusCLNewApplicationWizardPage(ISelection selection) {
		super("IusCLNewApplicationWizardPage");
		setTitle("IusCL Application");
		setDescription("This wizard creates a new IusCL Application.");
	}

	public void createControl(Composite parent) {
		Font fontNormal = parent.getFont();
		FontData oldFontData = fontNormal.getFontData()[0];
		Font fontItalic = new Font(Display.getCurrent(), oldFontData.getName(), oldFontData.getHeight(), SWT.ITALIC);

		/* Wizard layout */
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 8;

		/* Project name */
		Label label = new Label(container, SWT.NULL);
		label.setText("Project name");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectNameText.setText(IusCLDesignPreferences.getNewAppProjectName());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectNameText.setLayoutData(gd);
		projectNameText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(container, SWT.NULL);
		label.setText("");

		/* Horizontal separator */
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");

		/* Project location */
		label = new Label(container, SWT.NULL);
		label.setText("Project location");

		projectLocationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectLocationText.setText(IusCLDesignPreferences.getNewAppProjectLocation());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectLocationText.setLayoutData(gd);
		projectLocationText.addModifyListener(swtModifyEvent -> dialogChanged());

		Button button = new Button(container, SWT.PUSH);
		button.setText("Change...");
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> handleBrowse()));

		label = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("The project will be created in:");

		projectPathLabel = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		projectPathLabel.setLayoutData(gd);
		projectPathLabel.setFont(fontItalic);

		/* Horizontal separator */
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");

		/* Package name */
		label = new Label(container, SWT.NULL);
		label.setText("Package name");

		packageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		packageNameText.setLayoutData(gd);
		packageNameText.setText(IusCLDesignPreferences.getNewAppPackageName());
		packageNameText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(container, SWT.NULL);
		label.setText("");

		label = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("The application classes will be generated in:");

		packageLabel = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		packageLabel.setLayoutData(gd);
		packageLabel.setFont(fontItalic);

		/* Horizontal separator */
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");
		label = new Label(container, SWT.NULL);
		label.setText("");

		/* Main form name */
		label = new Label(container, SWT.NULL);
		label.setText("Form name");

		formNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formNameText.setLayoutData(gd);
		formNameText.setText(IusCLDesignPreferences.getNewAppFormName());
		formNameText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(container, SWT.NULL);
		label.setText("");

		label = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("The form instance name will be:");

		formInstanceNameLabel = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		formInstanceNameLabel.setLayoutData(gd);
		formInstanceNameLabel.setFont(fontItalic);

		dialogChanged();
		setControl(container);
	}

	private void handleBrowse() {
		DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
		directoryDialog.setFilterPath(getProjectLocation());
		directoryDialog.setText("Project location");
		directoryDialog.setMessage("Select project location");
		String newPath = directoryDialog.open();
		if (newPath != null) {
			projectLocationText.setText(newPath);
		}
	}

	private void dialogChanged() {
		if (getProjectName().isEmpty()) {
			updateStatus("Project name must be specified");
			return;
		}

		if (getProjectLocation().isEmpty()) {
			updateStatus("Project location must be specified");
			return;
		}
		// if (getProjectLocation().replace('\\', '/').indexOf('/', 1) > 0) {
		// updateStatus("Project location must be valid");
		// return;
		// }

		if (getPackageName().isEmpty()) {
			updateStatus("Package name must be specified");
			return;
		}

		if (getFormName().isEmpty()) {
			updateStatus("Form name must be specified");
			return;
		}

		if ("win32".equalsIgnoreCase(SWT.getPlatform())) {
			projectPathLabel.setText(getProjectLocation() + "\\" + getProjectName());
		} else {
			projectPathLabel.setText(getProjectLocation() + "/" + getProjectName());
		}

		packageLabel.setText(getPackageName() + ".iuscl");

		formInstanceNameLabel.setText(getFormName().substring(0, 1).toLowerCase() + getFormName().substring(1));

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectNameText.getText().trim();
	}

	public String getProjectLocation() {
		return projectLocationText.getText().trim();
	}

	public String getProjectPath() {
		return projectPathLabel.getText().trim();
	}

	public String getPackageName() {
		return packageNameText.getText().trim();
	}

	public String getPackagePath() {
		return packageLabel.getText().trim();
	}

	public String getFormName() {
		return formNameText.getText().trim();
	}
}
