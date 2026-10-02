/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.iuscl.plugin.ide.IusCLDesignErrorUtils;
import org.iuscl.plugin.ide.IusCLDesignIDE;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLNewFormWizard extends Wizard implements INewWizard {

	IusCLNewFormWizardPage newFormWizardPage;
	ISelection selection;

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.setWindowTitle("New IusCL Form");
		this.setDefaultPageImageDescriptor(ImageDescriptor
				.createFromURL(this.getClass().getResource("/resources/images/IusCLFormDesignWizardBanner.png")));
	}

	@Override
	public void addPages() {
		newFormWizardPage = new IusCLNewFormWizardPage(selection);
		addPage(newFormWizardPage);
	}

	public boolean performFinish() {
		final String containerName = newFormWizardPage.getContainerName();
		final String formShortClassName = newFormWizardPage.getFormName();
		final String parentFormCanonicalClassName = newFormWizardPage.getParentFormCanonicalName();
		final String formName = formShortClassName.substring(0, 1).toLowerCase() + formShortClassName.substring(1);

		final String formWidth = newFormWizardPage.getFormWidth();
		final String formHeight = newFormWizardPage.getFormHeight();
		final String formLeft = newFormWizardPage.getFormLeft();
		final String formTop = newFormWizardPage.getFormTop();

		Boolean hasParentAux = false;
		if (IusCLStrUtils.isNotNullNotEmpty(parentFormCanonicalClassName)) {
			hasParentAux = true;
		}
		final boolean hasParent = hasParentAux;

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			System.out.println("Container \"" + containerName + "\" does not exist.");
		}

		IContainer container = (IContainer) resource;
		final IFile javaFormFile = container.getFile(new Path(formShortClassName + ".java"));
		final IFile fmFormFile = container.getFile(new Path(formShortClassName + ".iusclfm"));

		/* Create the new project operation */
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				monitor.beginTask("Generate Java form...", 100);

				try {
					IJavaProject javaProject = JavaCore.create(resource.getProject());
					IPackageFragment packageFragment = javaProject.findPackageFragment(new Path(containerName));

					/* Java */
					monitor.subTask(".java");
					monitor.worked(50);
					String javaContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewForm.java.java");
					javaContents = javaContents.replace("${package}", packageFragment.getElementName());
					javaContents = javaContents.replace("${formShortClassName}", formShortClassName);

					if (hasParent) {
						javaContents = javaContents.replace("${parentFormCanonicalClassName}",
								parentFormCanonicalClassName);
						String parentFormShortClassName = parentFormCanonicalClassName;
						if (parentFormShortClassName.lastIndexOf(".") > -1) {
							parentFormShortClassName = parentFormShortClassName
									.substring(parentFormCanonicalClassName.lastIndexOf(".") + 1);
						}
						javaContents = javaContents.replace("${parentFormShortClassName}", parentFormShortClassName);
					} else {
						javaContents = javaContents.replace("${parentFormCanonicalClassName}",
								"org.iuscl.forms.IusCLForm");
						javaContents = javaContents.replace("${parentFormShortClassName}", "IusCLForm");
					}

					IusCLStrUtils.saveStringToFile(javaContents, javaFormFile.getLocation().toOSString());

					/* FM */
					monitor.subTask(".iusclfm");
					monitor.worked(30);
					String fmContents = IusCLDesignIDE.loadTextFromResource("IusCLDesignNewForm.iusclfm.xml");
					fmContents = fmContents.replace("${name}", formName);

					if (hasParent) {
						String ls = IusCLStrUtils.sLineBreak();

						fmContents = fmContents.replace("  <Caption>${caption}</Caption>" + ls, "");
						fmContents = fmContents.replace("  <Height>${height}</Height>" + ls, "");
						fmContents = fmContents.replace("  <Left>${left}</Left>" + ls, "");
						fmContents = fmContents.replace("  <Width>${width}</Width>" + ls, "");
						fmContents = fmContents.replace("  <Top>${top}</Top>" + ls, "");
					} else {
						fmContents = fmContents.replace("${caption}", formShortClassName + " Caption");
						fmContents = fmContents.replace("${width}", formWidth);
						fmContents = fmContents.replace("${height}", formHeight);
						fmContents = fmContents.replace("${left}", formLeft);
						fmContents = fmContents.replace("${top}", formTop);
					}

					IusCLStrUtils.saveStringToFile(fmContents, fmFormFile.getLocation().toOSString());

					/* Declare in application */

					/* Refresh project */
					resource.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException coreException) {
					String exceptionMessage = "CoreException in new form wizzard";
					log.error(exceptionMessage, coreException);
					IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, coreException);
				}
			}
		};

		/* Run the new form creation operation */
		try {
			getContainer().run(true, true, runnableWithProgress);
		} catch (InterruptedException interruptedException) {
			String exceptionMessage = "InterruptedException in new form wizard";
			log.error(exceptionMessage, interruptedException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, interruptedException);
		} catch (InvocationTargetException invocationTargetException) {
			String exceptionMessage = "InvocationTargetException in new form wizard";
			log.error(exceptionMessage, invocationTargetException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, invocationTargetException);
		}
		/* Build project */
		buildAndShowForm(resource.getProject(), javaFormFile, fmFormFile);
		return true;
	}

	public static void buildAndShowForm(IProject project, final IFile javaFormFile, final IFile fmFormFile) {
		IProgressMonitor buildProgressMonitor = new IProgressMonitor() {
			@Override
			public void worked(int work) {
				/* */
			}

			@Override
			public void subTask(String name) {
				/* */
			}

			@Override
			public void setTaskName(String name) {
				/* */
			}

			@Override
			public void setCanceled(boolean value) {
				/* */
			}

			@Override
			public boolean isCanceled() {
				return false;
			}

			@Override
			public void internalWorked(double work) {
				/* */
			}

			@Override
			public void done() {
				try {
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IDE.openEditor(workbenchPage, javaFormFile, true);
					IDE.openEditor(workbenchPage, fmFormFile, true);
				} catch (PartInitException partInitException) {
					String exceptionMessage = "PartInitException in build progress monitor done";
					log.error(exceptionMessage, partInitException);
					IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, partInitException);
				}
			}

			@Override
			public void beginTask(String name, int totalWork) {
				/* */
			}
		};

		try {
			project.build(IncrementalProjectBuilder.FULL_BUILD, buildProgressMonitor);
		} catch (CoreException coreException) {
			String exceptionMessage = "CoreException in build from new form wizard";
			log.error(exceptionMessage, coreException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, coreException);
		}
	}
}
