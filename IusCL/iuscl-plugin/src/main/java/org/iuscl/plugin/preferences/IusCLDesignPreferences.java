/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.iuscl.plugin.IusCLPlugin;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IusCLDesignPreferences {

	private final IPreferenceStore preferencesStore = IusCLPlugin.getDefault().getPreferenceStore();

	public final String ENV_DISTRIBUTIONLOCATION = "P_ENV_DISTRIBUTIONLOCATION";

	public final String NEWAPP_PROJECTNAME = "P_NEWAPP_PROJECTNAME";
	public final String NEWAPP_PROJECTLOCATION = "P_NEWAPP_PROJECTLOCATION";
	public final String NEWAPP_PACKAGENAME = "P_NEWAPP_PACKAGENAME";
	public final String NEWAPP_FORMNAME = "P_NEWAPP_FORMNAME";

	public final String NEWFORM_FORMNAME = "P_NEWFORM_FORMNAME";
	public final String NEWFORM_WIDTH = "P_NEWFORM_WIDTH";
	public final String NEWFORM_HEIGHT = "P_NEWFORM_HEIGHT";
	public final String NEWFORM_LEFT = "P_NEWFORM_LEFT";
	public final String NEWFORM_TOP = "P_NEWFORM_TOP";

	public String getEnvDistributionLocation() {
		return preferencesStore.getString(ENV_DISTRIBUTIONLOCATION);
	}

	public String getNewAppProjectName() {
		return preferencesStore.getString(NEWAPP_PROJECTNAME);
	}

	public String getNewAppProjectLocation() {
		return preferencesStore.getString(NEWAPP_PROJECTLOCATION);
	}

	public String getNewAppPackageName() {
		return preferencesStore.getString(NEWAPP_PACKAGENAME);
	}

	public String getNewAppFormName() {
		return preferencesStore.getString(NEWAPP_FORMNAME);
	}

	public String getNewFormFormName() {
		return preferencesStore.getString(NEWFORM_FORMNAME);
	}

	public String getNewFormFormWidth() {
		return preferencesStore.getString(NEWFORM_WIDTH);
	}

	public String getNewFormFormHeight() {
		return preferencesStore.getString(NEWFORM_HEIGHT);
	}

	public String getNewFormFormLeft() {
		return preferencesStore.getString(NEWFORM_LEFT);
	}

	public String getNewFormFormTop() {
		return preferencesStore.getString(NEWFORM_TOP);
	}
}
