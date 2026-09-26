/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.plugin.ide.IusCLDesignErrorUtils;
import org.osgi.framework.BundleContext;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IusCLPlugin extends AbstractUIPlugin {

	/* The plug-in ID */
	public static final String PLUGIN_ID = "org.iuscl.ide";

	/* The shared instance */
	private static IusCLPlugin plugin;

	/* The plugin's installation folder */
	@Getter
	private static String pluginFolder;

	public IusCLPlugin() {
		// System.setProperty("java.util.logging.config.class",
		// "org.iuscl.system.IusCLLogging");

		plugin = this; // NOSONAR
		/* Setup a custom leak hunter handler */
		Resource.setNonDisposeHandler(null);
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		try {
			URL pluginURL = FileLocator.find(plugin.getBundle(), new Path("/"), null);
			pluginURL = FileLocator.toFileURL(pluginURL);
			pluginFolder = new File(pluginURL.getFile()).getCanonicalPath(); // NOSONAR
		} catch (IOException ioException) {
			String exceptionMessage = "IOException finding the IusCL plugin folder";
			log.error(exceptionMessage, ioException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, ioException);
		}
		/* Default application for the plug-in to be used for dialogs */
		IusCLApplication.setTitle("IusCL Design Application");
		IusCLApplication.setInitialDir(pluginFolder);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null; // NOSONAR
		super.stop(context);
	}

	public static IusCLPlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
