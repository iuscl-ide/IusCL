/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.actions;

import java.io.File;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.dialogs.IusCLDialogs;
import org.iuscl.forms.IusCLForm.IusCLModalResult;
import org.iuscl.plugin.IusCLPlugin;
import org.iuscl.plugin.forms.IusCLPluginApplicationDeploy;
import org.iuscl.plugin.forms.IusCLPluginApplicationDeployForm;
import org.iuscl.plugin.preferences.IusCLDesignPreferences;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLApplicationDeployAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow iWorkbenchWindow;

	IusCLPluginApplicationDeploy pluginApplicationDeploy = new IusCLPluginApplicationDeploy();

	public IusCLApplicationDeployAction() {
		/*  */
	}

	public void run(IAction action) {
		if (iWorkbenchWindow.getActivePage() != null && iWorkbenchWindow.getActivePage().getActiveEditor() == null) {
			MessageDialog.openInformation(iWorkbenchWindow.getShell(), "Application Deploy",
					"No editor is open, so the project/application is not known");
			return;
		}

		pluginApplicationDeploy.setPluginFolder(IusCLPlugin.getPluginFolder());

		IEditorPart editorPart = iWorkbenchWindow.getActivePage().getActiveEditor();
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
		IFile file = input.getFile();
		final IProject activeProject = file.getProject();
		pluginApplicationDeploy.setProjectFolder(activeProject.getLocation().toOSString());

		pluginApplicationDeploy.setDistributionFolder(IusCLDesignPreferences.getEnvDistributionLocation());

		pluginApplicationDeploy.setProjectName(activeProject.getName());

		IusCLPluginApplicationDeployForm applicationDeployForm = new IusCLPluginApplicationDeployForm();
		applicationDeployForm.setPluginApplicationDeploy(pluginApplicationDeploy);
		IusCLModalResult modalResult = applicationDeployForm.showModal();

		if (modalResult == IusCLModalResult.mrOk) {
			pluginApplicationDeploy = applicationDeployForm.getPluginApplicationDeploy();

			String distributionFolder = "";

			if (IusCLStrUtils.isNotNullNotEmpty(pluginApplicationDeploy.getDistributionFolder())
					&& IusCLStrUtils.isNotNullNotEmpty(pluginApplicationDeploy.getProjectName())) {
				distributionFolder = IusCLFileUtils.createPathName(pluginApplicationDeploy.getDistributionFolder(),
						pluginApplicationDeploy.getProjectName());
			}

			if (!IusCLStrUtils.isNotNullNotEmpty(distributionFolder)) {
				return;
			}

			String projectFolder = pluginApplicationDeploy.getProjectFolder();

			String antBuildFilePath = IusCLFileUtils.createPathName(projectFolder, "dist", "build.xml");

			File antBuildFile = new File(antBuildFilePath);

			Project antProject = new Project();
			antProject.setUserProperty("ant.file", antBuildFilePath);
			antProject.init();

			DefaultLogger consoleLogger = new DefaultLogger();
			consoleLogger.setErrorPrintStream(System.err);
			consoleLogger.setOutputPrintStream(System.out);
			consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
			antProject.addBuildListener(consoleLogger);

			String projectName = activeProject.getName();
			antProject.setProperty("project.name", projectName);

			String jarPrefix = projectName.substring(0, 1).toLowerCase() + projectName.substring(1);
			antProject.setProperty("project.jar.prefix", jarPrefix);

			// antProject.setProperty("project.jar.version", "1.0.0");

			IusCLStrings launchFile = new IusCLStrings();
			launchFile.loadFromFile(IusCLFileUtils.createPathName(projectFolder, projectName + ".launch"));
			String mainClassName = IusCLStrUtils.subStringBetween(launchFile.getText(), "org.eclipse.jdt.launching.MAIN_TYPE\" value=\"", "\"/>");
			antProject.setProperty("project.jar.mainclass", mainClassName);

			antProject.setProperty("project.folder", pluginApplicationDeploy.getProjectFolder());
			antProject.setProperty("plugin.folder", pluginApplicationDeploy.getPluginFolder());
			antProject.setProperty("distribution.folder", distributionFolder);

			ProjectHelper2 antProjectHelper = new ProjectHelper2();
			antProject.addReference("ant.projectHelper", antProjectHelper);
			antProjectHelper.parse(antProject, antBuildFile);
			antProject.executeTarget(antProject.getDefaultTarget());

			IusCLDialogs.showinfo("The application was deployed on:\n" + distributionFolder, "Application Deploy");
		}
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
