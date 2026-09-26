/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.ide.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.iuscl.plugin.preferences.IusCLDesignPreferences;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLNewFormWizardPage extends WizardPage {

	Text containerText;
	Text formNameText;
	Text parentFormCanonicalNameText;
	Text formWidthText;
	Text formHeightText;
	Text formLeftText;
	Text formTopText;

	Label projectNameLabel;
	Label packageNameLabel;
	Label formInstanceNameLabel;

	ISelection selection;

	public IusCLNewFormWizardPage(ISelection selection) {
		super("IusCLNewFormWizardPage");
		setTitle("IusCL Form");
		setDescription(
				"This wizard creates a new IusCL Form - a Java file containing the form code and an XML file with 'iusclfm' extension containing the form properties.");
		this.selection = selection;
	}

	String extractPath(ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object element = structuredSelection.getFirstElement();
		if (element instanceof IResource resourceElement) {
			return (resourceElement.getFullPath().removeLastSegments(1)).toString();
		}
		if (!(element instanceof IAdaptable)) {
			return null;
		}
		IAdaptable adaptable = (IAdaptable) element;
		Object adapter = adaptable.getAdapter(IResource.class);
		if (adapter == null) {
			return null;
		}
		if (adapter instanceof IFolder) {
			return ((IResource) adapter).getFullPath().toString();
		}
		if (adapter instanceof IFile) {
			return (((IResource) adapter).getFullPath().removeLastSegments(1)).toString();
		}

		return null;
	}

	public void createControl(Composite parent) {
		Font fontNormal = parent.getFont();
		FontData oldFontData = fontNormal.getFontData()[0];
		Font fontItalic = new Font(Display.getCurrent(), oldFontData.getName(), oldFontData.getHeight(), SWT.ITALIC);

		Composite pageComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		pageComposite.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 8;

		/* Container */
		Label label = new Label(pageComposite, SWT.NULL);
		label.setText("Container");

		containerText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		String containerPath = extractPath(selection);
		if (containerPath == null) {
			containerPath = "";
		}
		containerText.setText(containerPath);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(swtModifyEvent -> dialogChanged());

		Button button = new Button(pageComposite, SWT.PUSH);
		button.setText("Change...");
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> handleBrowse()));

		label = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("The form will be created in the project:");

		projectNameLabel = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		projectNameLabel.setLayoutData(gd);
		projectNameLabel.setFont(fontItalic);

		label = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("And in the package:");

		packageNameLabel = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		packageNameLabel.setLayoutData(gd);
		packageNameLabel.setFont(fontItalic);

		/* Horizontal separator */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Form name */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Form short class name");

		formNameText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formNameText.setLayoutData(gd);
		formNameText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		label = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		label.setText("The form name will be:");

		formInstanceNameLabel = new Label(pageComposite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		formInstanceNameLabel.setLayoutData(gd);
		formInstanceNameLabel.setFont(fontItalic);

		/* Parent form */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Parent form canonical class name");

		/*
		 * TODO
		 * 
		 * Browse button to choose the parent class
		 * 
		 */

		parentFormCanonicalNameText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		parentFormCanonicalNameText.setLayoutData(gd);
		parentFormCanonicalNameText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Horizontal separator */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");
		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Form width */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Form initial width");

		formWidthText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formWidthText.setLayoutData(gd);
		formWidthText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Form height */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Form initial height");

		formHeightText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formHeightText.setLayoutData(gd);
		formHeightText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Form left */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Form initial left");

		formLeftText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formLeftText.setLayoutData(gd);
		formLeftText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		/* Form top */
		label = new Label(pageComposite, SWT.NULL);
		label.setText("Form initial top");

		formTopText = new Text(pageComposite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		formTopText.setLayoutData(gd);
		formTopText.addModifyListener(swtModifyEvent -> dialogChanged());

		label = new Label(pageComposite, SWT.NULL);
		label.setText("");

		initialize();
		dialogChanged();
		setControl(pageComposite);
	}

	private void initialize() {
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection ssel) {
			if (ssel.size() > 1) {
				return;
			}
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource objResource) {
				if (obj instanceof IContainer objContainer) {
					containerText.setText(objContainer.getFullPath().toString());
				} else {
					containerText.setText(objResource.getParent().getFullPath().toString());
				}
			}
		}
		formNameText.setText(IusCLDesignPreferences.getNewFormFormName());
		formWidthText.setText(IusCLDesignPreferences.getNewFormFormWidth());
		formHeightText.setText(IusCLDesignPreferences.getNewFormFormHeight());
		formLeftText.setText(IusCLDesignPreferences.getNewFormFormLeft());
		formTopText.setText(IusCLDesignPreferences.getNewFormFormTop());
	}

	private void handleBrowse() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		ContainerSelectionDialog containerSelectionDialog = new ContainerSelectionDialog(getShell(), root, false, "Select new form container");

		/*
		 * Not working, another SWT bug | V
		 */
		containerSelectionDialog.setInitialSelections(root.findMember(new Path(getContainerName())));

		if (containerSelectionDialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = containerSelectionDialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	private void dialogChanged() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IResource container = root.findMember(new Path(getContainerName()));
		String formName = getFormName();

		if (getContainerName().isEmpty()) {
			updateStatus("Form container must be specified");
			return;
		}
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("Form container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (formName.isEmpty()) {
			updateStatus("Form name must be specified");
			return;
		}
		if (formName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("Form name must be valid");
			return;
		}

		try {
			IJavaProject javaProject = JavaCore.create(container.getProject());
			projectNameLabel.setText(javaProject.getElementName());
			IPackageFragment packageFragment = javaProject.findPackageFragment(new Path(getContainerName()));
			packageNameLabel.setText(packageFragment.getElementName());
			formInstanceNameLabel.setText(getFormName().substring(0, 1).toLowerCase() + getFormName().substring(1));
		} catch (Exception exception) {
			updateStatus("Package must be valid");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFormName() {
		return formNameText.getText();
	}

	public String getParentFormCanonicalName() {
		return parentFormCanonicalNameText.getText();
	}

	public String getFormWidth() {
		return formWidthText.getText();
	}

	public String getFormHeight() {
		return formHeightText.getText();
	}

	public String getFormLeft() {
		return formLeftText.getText();
	}

	public String getFormTop() {
		return formTopText.getText();
	}
}
