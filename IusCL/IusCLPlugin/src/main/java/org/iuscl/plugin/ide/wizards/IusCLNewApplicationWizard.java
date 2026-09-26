/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.ide.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.plugin.IusCLPlugin;
import org.iuscl.plugin.ide.IusCLDesignErrorUtils;
import org.iuscl.plugin.ide.IusCLDesignIDE;
import org.iuscl.plugin.preferences.IusCLDesignPreferences;
import org.iuscl.sysutils.IusCLFileSearchRec;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLNewApplicationWizard extends Wizard implements INewWizard {

	private static final String DOTIUSCLFM = ".iusclfm";
	private static final String FORM_SHORT_CLASS_NAME = "${formShortClassName}";
	private static final String PROJECT = "${project}";
	private static final String PACKAGE = "${package}";
	private static final String DOTJAVA = ".java";
	private static final String DOTIUSCL = ".iuscl";
	IusCLNewApplicationWizardPage newApplicationWizardPage;
	ISelection selection;

	IFile javaFormFile;
	IFile fmFormFile;

	public IusCLNewApplicationWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.setWindowTitle("New IusCL Application");
		this.setDefaultPageImageDescriptor(
				ImageDescriptor.createFromURL(this.getClass().getResource("/resources/images/IusCLApplicationDesignWizardBanner.gif")));
	}

	@Override
	public void addPages() {
		newApplicationWizardPage = new IusCLNewApplicationWizardPage(selection);
		addPage(newApplicationWizardPage);
	}

	public boolean performFinish() {
		final String projectName = newApplicationWizardPage.getProjectName();
		final String formName = newApplicationWizardPage.getFormName();
		final String packageName = newApplicationWizardPage.getPackageName();
		final String projectPath = newApplicationWizardPage.getProjectPath();

		final IProject projectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		/* Create the new project operation */
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				monitor.beginTask("Eclipse generate Java project...", 100);

				String varFormName = formName.substring(0, 1).toLowerCase() + formName.substring(1);
				String formWidth = IusCLDesignPreferences.getNewFormFormWidth();
				String formHeight = IusCLDesignPreferences.getNewFormFormHeight();
				String formLeft = IusCLDesignPreferences.getNewFormFormLeft();
				String formTop = IusCLDesignPreferences.getNewFormFormTop();

				try {
					/* Create the generic project in workspace */
//					String projectFolder = projectPath;

					File file = new File(projectPath);
					URI projectLocation = file.toURI();

//					projectFolder = IusCLFileUtils.includeTrailingPathDelimiter(projectFolder);
					// String ps = IusCLFileUtils.getPathDelimiter();

					final IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(projectHandle.getName());
					projectDescription.setLocationURI(projectLocation);

					CreateProjectOperation createProjectOperation = new CreateProjectOperation(projectDescription, "New Project");
					PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(createProjectOperation, monitor,
							WorkspaceUndoUtil.getUIInfoAdapter(getShell()));

					monitor.beginTask("Customize the project as IusCL application", 100);

					/* We have the project created */
					IResource projectResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(projectName));

					/* .project */
					monitor.subTask(".project");
					monitor.worked(10);

					String projectContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewProject.project.xml");
					projectContents = projectContents.replace(PROJECT, projectName);
					IusCLStrUtils.saveStringToFile(projectContents, IusCLFileUtils.createPathName(projectPath, ".project"));

					monitor.subTask("lib");
					monitor.worked(30);
					/* classes */
					IusCLFileUtils.createFolder(IusCLFileUtils.createPathName(projectPath, "classes"));

					/* lib */
					String pluginFolder = IusCLPlugin.getPluginFolder();

					String libFolder = IusCLFileUtils.createPathName(projectPath, "lib", "iuscl-packages");
					IusCLFileUtils.createFolder(libFolder);

					String libPackagesFolder = IusCLFileUtils.createPathName(pluginFolder, "dist", "application", "lib", "iuscl-packages");

					IusCLFileUtils.copyFolderContent(libPackagesFolder, libFolder);

					/* .classpath */
					monitor.subTask(".classpath");
					monitor.worked(10);

					String classpathContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewProject.classpath.xml");

					IusCLFileSearchRec fileSearchRec = new IusCLFileSearchRec();
					fileSearchRec.setIncludeNamePattern(".jar");
					IusCLStrings list = IusCLFileUtils.findFiles(libPackagesFolder, fileSearchRec);

					for (int index = 0; index < list.size(); index++) {
						String lineFile = list.get(index).replace("\\", "/");
						String libLine = "\t<classpathentry kind=\"lib\" path=\"" + lineFile.substring(lineFile.indexOf("lib/iuscl-packages"))
								+ "\"/>" + IusCLStrUtils.sLineBreak();

						classpathContents = classpathContents.replace("</classpath>", libLine + "</classpath>");
					}

					IusCLStrUtils.saveStringToFile(classpathContents, IusCLFileUtils.createPathName(projectPath, ".classpath"));

					/* dist */
					monitor.subTask("dist");
					monitor.worked(10);

					String distFolder = IusCLFileUtils.createPathName(projectPath, "dist");
					IusCLFileUtils.createFolder(distFolder);

					String buildContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewProjectDeployAnt.build.xml.xml");
					IusCLStrUtils.saveStringToFile(buildContents, IusCLFileUtils.createPathName(distFolder, "build.xml"));

					/* src */
					monitor.subTask("Main form");
					monitor.worked(30);

					String srcFolder = IusCLFileUtils.createPathName(projectPath, "src", "main", "java");
					IusCLFileUtils.createFolder(srcFolder);

					String[] splits = packageName.split("\\.");
					String srcPath = IusCLFileUtils.createPathName("src", "main", "java");

					for (int index = 0; index < splits.length; index++) {
						String childName = splits[index];
						srcPath = IusCLFileUtils.createPathName(srcPath, childName);
						srcFolder = IusCLFileUtils.createPathName(srcFolder, childName);
						IusCLFileUtils.createFolder(srcFolder);
					}

					srcPath = IusCLFileUtils.createPathName(srcPath, "iuscl");
					srcFolder = IusCLFileUtils.createPathName(srcFolder, "iuscl");
					IusCLFileUtils.createFolder(srcFolder);

					/* Java Form */
					String javaFormContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewForm.java.java");
					javaFormContents = javaFormContents.replace(PACKAGE, packageName + DOTIUSCL);
					javaFormContents = javaFormContents.replace(FORM_SHORT_CLASS_NAME, formName);
					// javaFormContents = javaFormContents.replace("${formVar}", varFormName);
					javaFormContents = javaFormContents.replace("${parentFormCanonicalClassName}", "org.iuscl.forms.IusCLForm");
					javaFormContents = javaFormContents.replace("${parentFormShortClassName}", "IusCLForm");
					IusCLStrUtils.saveStringToFile(javaFormContents, IusCLFileUtils.createPathName(srcFolder, formName + DOTJAVA));

					javaFormFile = projectHandle.getFile(IusCLFileUtils.createPathName(srcPath, formName + DOTJAVA));

					/* FM Form */
					String dfmFormContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewForm.iusclfm.xml");
					// dfmFormContents = dfmFormContents.replace("${form}", varFormName);

					dfmFormContents = dfmFormContents.replace("${name}", varFormName);

					dfmFormContents = dfmFormContents.replace("${caption}", formName + " Caption");

					dfmFormContents = dfmFormContents.replace("${height}", formHeight);
					dfmFormContents = dfmFormContents.replace("${left}", formLeft);
					dfmFormContents = dfmFormContents.replace("${width}", formWidth);
					dfmFormContents = dfmFormContents.replace("${top}", formTop);
					IusCLStrUtils.saveStringToFile(dfmFormContents, IusCLFileUtils.createPathName(srcFolder, formName + DOTIUSCLFM));

					fmFormFile = projectHandle.getFile(srcPath + formName + DOTIUSCLFM);

					/* Java Project */
					String javaProjectContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewProject.java.java");
					javaProjectContents = javaProjectContents.replace(PACKAGE, packageName + DOTIUSCL);
					javaProjectContents = javaProjectContents.replace(PROJECT, projectName);
					javaProjectContents = javaProjectContents.replace("${form}", formName);
					javaProjectContents = javaProjectContents.replace("${formVar}", varFormName);
					IusCLStrUtils.saveStringToFile(javaProjectContents, IusCLFileUtils.createPathName(srcFolder, projectName + DOTJAVA));

					/* launch */
					String launchContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewProject.launch.xml");
					launchContents = launchContents.replace(PACKAGE, packageName + DOTIUSCL);
					launchContents = launchContents.replace(PROJECT, projectName);
					String projectClassPath = IusCLFileUtils.createPathName("", projectName, "src", "main", "java", projectName + DOTJAVA);
					launchContents = launchContents.replace("${path}", projectClassPath);
					IusCLStrUtils.saveStringToFile(launchContents, IusCLFileUtils.createPathName(projectPath, projectName + ".launch"));

					/* refresh */
					monitor.worked(5);
					monitor.subTask("refresh workspace");

					projectResource.refreshLocal(IResource.DEPTH_INFINITE, null);

					monitor.subTask("build");
				} catch (CoreException coreException) {
					String exceptionMessage = "CoreException in new application wizzard";
					log.error(exceptionMessage, coreException);
					IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, coreException);
				} catch (ExecutionException executionException) {
					String exceptionMessage = "ExecutionException in new application wizzard";
					log.error(exceptionMessage, executionException);
					IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, executionException);
				}
			}
		};

		/* Run the new project creation operation */
		try {
			getContainer().run(true, true, runnableWithProgress);
		} catch (InterruptedException interruptedException) {
			String exceptionMessage = "InterruptedException in new application wizzard";
			log.error(exceptionMessage, interruptedException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, interruptedException);
		} catch (InvocationTargetException invocationTargetException) {
			String exceptionMessage = "InvocationTargetException in new application wizzard";
			log.error(exceptionMessage, invocationTargetException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, invocationTargetException);
		}

		/* Build project */
		IusCLNewFormWizard.buildAndShowForm(projectHandle, javaFormFile, fmFormFile);

		return true;
	}
}
