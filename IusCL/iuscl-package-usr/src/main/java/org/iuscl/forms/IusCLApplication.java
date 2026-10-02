/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.forms;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLOS;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLMessageEvent;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.sysctrls.IusCLApplicationEvents;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class IusCLApplication extends IusCLComponent {

	@Getter
	private Display swtDisplay = null;
	@Getter
	private Shell swtApplicationShell = null;

	@Getter
	@Setter
	private Integer hintPause = 500;

	@Getter
	@Setter
	private String title = "IusCL Application";
	@Getter
	@Setter
	private String initialDir = IusCLFileUtils.getCurrentFolder();
	@Setter
	private IusCLPicture icon = null;

	private boolean run = false;

	private final Map<String, String> fmFiles = new HashMap<>();

	private final List<String> refComponentNames = new ArrayList<>();
	private final List<IusCLPersistent> refComponentDestinations = new ArrayList<>();
	private final List<String> refComponentPropertyNames = new ArrayList<>();

	@Getter
	@Setter
	private boolean putNextCreatedFormInDesignMode = false;

	/* Application events */
	@Getter
	private final List<IusCLApplicationEvents> applicationEvents = new ArrayList<>();

	/* Forms */
	@Getter
	private final List<IusCLForm> forms = new ArrayList<>();
	@Getter
	@Setter
	private IusCLForm activeForm = null;
	@Getter
	@Setter
	private IusCLForm mainForm = null;

	static {
		swtDisplay = Display.getCurrent();
		if (swtDisplay == null) {
			swtDisplay = Display.getDefault();
			if (swtDisplay == null) {
				swtDisplay = new Display();
			}
		}

		swtApplicationShell = new Shell(swtDisplay, SWT.NO_TRIM);
		swtApplicationShell.setBounds(0, 0, 0, 0);

		IusCLScreen.calculateMetrics();
	}

//	public IusCLApplication() {
//		super(null);
//	}

	public void initialize() {
		/* ? (event?) */
	}

	public void run() {
		if (mainForm != null) {
			mainForm.show();
			run = true;

			// while(!mainForm.getSwtShell().isDisposed()) {
			while (run) {
				handleMessage();
			}
			terminate();
		}
	}

	public boolean processMessage() {
		return swtDisplay.readAndDispatch();
	}

	public void idle() {
		swtDisplay.sleep();
	}

	public void processMessages() {
		/* To be used from outside, "doEvents" */

		while (processMessage()) {
			/* do */
		}
	}

	public void handleMessage() {
		/* Application events */
		if (!applicationEvents.isEmpty()) {
			Object msg = IusCLOS.osIusCLApplication_PeekMessage();

			for (int index = 0; index < applicationEvents.size(); index++) {
				IusCLApplicationEvents appEvents = applicationEvents.get(index);

				IusCLMessageEvent messageEvent = appEvents.getOnMessage();
				if (IusCLEvent.isDefinedEvent(messageEvent)) {
					boolean handled = messageEvent.invoke(appEvents.findForm(), msg);
					if (handled) {
						break;
					}
				}
			}
		}

		if (!processMessage()) {
			idle();
		}
	}

	public void terminate() {
		swtDisplay.dispose();
	}

	private IusCLPicture loadDefaultIcon() {
		IusCLPicture defaultIcon = new IusCLPicture();
		defaultIcon.loadFromResource(IusCLApplication.class, "resources/icons/IusCLMainIcon.ico");
		return defaultIcon;
	}

	public void addForm(IusCLForm form) {
		if (forms.contains(form)) {
			return;
		}
		forms.add(form);
	}

	public void removeForm(IusCLForm form) {
		if (forms.contains(form)) {
			forms.remove(form);
		}
	}

	public void setDisplay(Display display) {
		IusCLApplication.swtDisplay = display;
	}

	public boolean getIsRunning() {
		return run;
	}

	public void setIsTerminated() {
		run = false;
	}

	public IusCLPicture getIcon() {
		if (icon == null) {
			icon = loadDefaultIcon();
		}
		return icon;
	}

	public void putFMFile(String formClassName, String formFMFile) {
		fmFiles.put(formClassName, formFMFile);
	}

	public String getFMFile(String formClassName) {
		return fmFiles.get(formClassName);
	}

	public String getFormsResFolder(Class<?> formClass) {
		String fmFile = fmFiles.get(formClass.getCanonicalName());
		if (fmFile == null) {
			return null;
		}
		String resName = "resources/forms";
		String sep = IusCLFileUtils.getPathDelimiter();
		return fmFile.substring(0, fmFile.indexOf("src")) + resName.replace("/", sep) + sep;
	}

	public void putRefComponent(String refComponentName, IusCLPersistent refComponentDestination, String refComponentPropertyName) {
		refComponentNames.add(refComponentName);
		refComponentDestinations.add(refComponentDestination);
		refComponentPropertyNames.add(refComponentPropertyName);
	}

	public void findRefComponent(IusCLComponent refComponent) {
		int index = refComponentNames.indexOf(refComponent.getName());

		while (index > -1) {
			String propertyName = refComponentPropertyNames.get(index);
			IusCLPersistent destinationPersistent = refComponentDestinations.get(index);

			destinationPersistent.setPropertyValueInvoke(propertyName,
					new IusCLParam(destinationPersistent.getProperty(propertyName).getRefClass(), refComponent));

			refComponentNames.remove(index);
			refComponentDestinations.remove(index);
			refComponentPropertyNames.remove(index);

			index = refComponentNames.indexOf(refComponent.getName());
		}
	}

	public List<?> getItemsFromFormResource(Class<?> relativeClass, String resFormAndFileName) {
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document jdomDocument = null;

		String formsResFolder = getFormsResFolder(relativeClass);

		try {
			if (formsResFolder == null) {
				/* Runtime, resource */
				String resJarFileName = "resources/forms/" + resFormAndFileName;
				InputStream inputStream = relativeClass.getClassLoader().getResourceAsStream(resJarFileName);
				if (inputStream == null) {
					return Collections.emptyList();
				}
				jdomDocument = jdomBuilder.build(inputStream);
			} else {
				/* Design time, resources on disk */
				String resFileName = formsResFolder + resFormAndFileName.replace("/", IusCLFileUtils.getPathDelimiter());
				if (!(IusCLFileUtils.fileExists(resFileName))) {
					return Collections.emptyList();
				}
				jdomDocument = jdomBuilder.build(new File(resFileName));
			}
		} catch (Exception jdomException) {
			String exceptionMessage = MessageFormat.format("jdom XML exception in getting resource items for: \"{0}\"", resFormAndFileName);
			log.error(exceptionMessage, jdomException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, jdomException);
			return Collections.emptyList();
		}

		return jdomDocument.getRootElement().getChildren();
	}

	public void loadFromFormResource(Object objectInstance, Class<?> relativeClass, String resFormAndFileName) {
		String formsResFolder = getFormsResFolder(relativeClass);

		if (formsResFolder == null) {
			/* Runtime, resource */
			String resJarFileName = "resources/forms/" + resFormAndFileName;

			IusCLObjUtils.invokeMethod(objectInstance, "loadFromResource", new IusCLParam(Class.class, relativeClass),
					new IusCLParam(String.class, resJarFileName));
		} else {
			/* Design time, resources on disk */
			String resFileName = formsResFolder + resFormAndFileName.replace("/", IusCLFileUtils.getPathDelimiter());

			IusCLObjUtils.invokeMethod(objectInstance, "loadFromFile", new IusCLParam(String.class, resFileName));
		}
	}

	public void loadFromApplicationResource(Object objectInstance, Class<?> relativeClass, String resSimpleFileName) {
		String formsResFolder = getFormsResFolder(relativeClass);

		if (formsResFolder == null) {
			/* Runtime, resource */
			String resFileName = "resources/application/" + resSimpleFileName;

			IusCLObjUtils.invokeMethod(objectInstance, "loadFromResource", new IusCLParam(Class.class, relativeClass),
					new IusCLParam(String.class, resFileName));
		} else {
			/* Design time, resources on disk */
			String resFileFolder = IusCLStrUtils.subStringBetween(formsResFolder, null, "forms");
			String resFileName = IusCLFileUtils.includeTrailingPathDelimiter(resFileFolder) + "application" + IusCLFileUtils.getPathDelimiter()
					+ resSimpleFileName;

			if (IusCLFileUtils.fileExists(resFileName)) {
				IusCLObjUtils.invokeMethod(objectInstance, "loadFromFile", new IusCLParam(String.class, resFileName));
			}
		}
	}
}
