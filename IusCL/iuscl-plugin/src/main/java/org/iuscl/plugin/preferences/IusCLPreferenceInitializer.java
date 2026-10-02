
package org.iuscl.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.iuscl.plugin.IusCLPlugin;

public class IusCLPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferencesStore = IusCLPlugin.getDefault().getPreferenceStore();
		/* Environment */
		String distributionsLocation = "C:/IusCL_Distributions";
		if ("win32".equalsIgnoreCase(SWT.getPlatform())) {
			distributionsLocation = distributionsLocation.replace("/", "\\");
		}
		preferencesStore.setDefault(IusCLDesignPreferences.ENV_DISTRIBUTIONLOCATION, distributionsLocation);
		/* New application */
		preferencesStore.setDefault(IusCLDesignPreferences.NEWAPP_PROJECTNAME, "Application");
		String projectLocation = "C:/IusCL_Applications";
		if ("win32".equalsIgnoreCase(SWT.getPlatform())) {
			projectLocation = projectLocation.replace("/", "\\");
		}
		preferencesStore.setDefault(IusCLDesignPreferences.NEWAPP_PROJECTLOCATION, projectLocation);
		preferencesStore.setDefault(IusCLDesignPreferences.NEWAPP_PACKAGENAME, "com.application");
		preferencesStore.setDefault(IusCLDesignPreferences.NEWAPP_FORMNAME, "MainForm");
		/* New form */
		preferencesStore.setDefault(IusCLDesignPreferences.NEWFORM_FORMNAME, "Form");
		preferencesStore.setDefault(IusCLDesignPreferences.NEWFORM_WIDTH, "800");
		preferencesStore.setDefault(IusCLDesignPreferences.NEWFORM_HEIGHT, "640");
		preferencesStore.setDefault(IusCLDesignPreferences.NEWFORM_LEFT, "100");
		preferencesStore.setDefault(IusCLDesignPreferences.NEWFORM_TOP, "100");
	}
}
