/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.forms.IusCLForm.IusCLModalResult;
import org.iuscl.plugin.forms.IusCLPluginApplicationOptions;
import org.iuscl.plugin.forms.IusCLPluginApplicationOptionsForm;
import org.iuscl.plugin.ide.IusCLDesignErrorUtils;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLApplicationOptionsAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow iWorkbenchWindow;
	IusCLPluginApplicationOptions pluginApplicationOptions = new IusCLPluginApplicationOptions();

	public IusCLApplicationOptionsAction() {
		/*  */
	}

	public void run(IAction action) {
		if (iWorkbenchWindow.getActivePage() != null && iWorkbenchWindow.getActivePage().getActiveEditor() == null) {
			MessageDialog.openInformation(iWorkbenchWindow.getShell(), "Application Options",
					"No editor is open, so the project/application is not known");
			return;
		}

		String pd = IusCLFileUtils.getPathDelimiter();

		IEditorPart editorPart = iWorkbenchWindow.getActivePage().getActiveEditor();
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
		IFile file = input.getFile();
		final IProject activeProject = file.getProject();
		String projectFolder = activeProject.getLocation().toOSString();
		String projectName = activeProject.getName();

		pluginApplicationOptions.setProjectFolder(projectFolder);
		pluginApplicationOptions.setProjectName(projectName);

		IusCLStrings launchFile = new IusCLStrings();
		launchFile.loadFromFile(projectFolder + '\\' + projectName + ".launch");
		String mainClassName = IusCLStrUtils.subStringBetween(launchFile.getText(), "org.eclipse.jdt.launching.MAIN_TYPE\" value=\"", "\"/>");
		String applicationJavaSourceFileFullPath = projectFolder + pd + "src" + pd + "main" + pd + "java" + pd + mainClassName.replace(".", pd)
				+ ".java";

		IusCLStrings javaFile = new IusCLStrings();
		javaFile.loadFromFile(applicationJavaSourceFileFullPath);
		String applicationTitle = IusCLStrUtils.subStringBetween(javaFile.getText(), "IusCLApplication.setTitle(\"", "\")");

		if (IusCLStrUtils.isNotNullNotEmpty(applicationTitle)) {
			pluginApplicationOptions.setApplicationTitle(applicationTitle);
		} else {
			pluginApplicationOptions.setApplicationTitle("");
		}

		String applicationIcon = projectFolder + pd + "resources" + pd + "application" + pd + "MAINICON.ico";
		pluginApplicationOptions.setApplicationIcon(applicationIcon);

		IusCLPluginApplicationOptionsForm applicationOptionsForm = new IusCLPluginApplicationOptionsForm();
		applicationOptionsForm.setPluginApplicationOptions(pluginApplicationOptions);
		IusCLModalResult modalResult = applicationOptionsForm.showModal();

		if (modalResult == IusCLModalResult.mrOk) {
			pluginApplicationOptions = applicationOptionsForm.getPluginApplicationOptions();

			/* Title */
			String newApplicationTitle = pluginApplicationOptions.getApplicationTitle();

			if (!(IusCLStrUtils.equalValues(applicationTitle, newApplicationTitle))) {
				String appSource = javaFile.getText();

				String initLine = "IusCLApplication.initialize();";

				if (IusCLStrUtils.isNotNullNotEmpty(newApplicationTitle)) {
					if (appSource.indexOf("IusCLApplication.setTitle(") > -1) {
						appSource = appSource.replace("IusCLApplication.setTitle(\"" + applicationTitle + "\")",
								"IusCLApplication.setTitle(\"" + newApplicationTitle + "\")");
					} else {
						appSource = appSource.replace(initLine,
								initLine + IusCLStrUtils.sLineBreak() + "\t\t" + "IusCLApplication.setTitle(\"" + newApplicationTitle + "\");");
					}
				} else {
					if (appSource.indexOf("IusCLApplication.setTitle(") > -1) {
						appSource = appSource
								.replace("\t\t" + "IusCLApplication.setTitle(\"" + applicationTitle + "\");" + IusCLStrUtils.sLineBreak(), "");
					}
				}

				javaFile.setText(appSource);
				javaFile.saveToFile(applicationJavaSourceFileFullPath);

				try {
					IPath location = Path.fromOSString(applicationJavaSourceFileFullPath);
					IFile javaSourceFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
					javaSourceFile.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException coreException) {
					String exceptionMessage = "CoreException in getting Java text editor";
					log.error(exceptionMessage, coreException);
					IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, coreException);
				}
			}

			/* Icon */
			String newIconFileName = pluginApplicationOptions.getApplicationIcon();
			if (IusCLStrUtils.isNotNullNotEmpty(newIconFileName)) {
				IusCLFileUtils.copyFile(newIconFileName, applicationIcon);
			}
		}

		applicationOptionsForm.release();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		/*  */
	}

	public void dispose() {
		/*  */
	}

	public void init(IWorkbenchWindow window) {
		this.iWorkbenchWindow = window;
	}
}
