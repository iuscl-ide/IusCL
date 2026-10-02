/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.editors;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.iuscl.plugin.ide.IusCLDesignErrorUtils;
import org.iuscl.sysutils.IusCLFileUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLFormClassLoader extends ClassLoader {

	final Map<String, Class<?>> classes = new HashMap<>();
	@Getter
	@Setter
	ClassLoader parentClassLoader;
	@Getter
	@Setter
	String classesFolder;

	public IusCLFormClassLoader() {
		/*  */
	}

	private byte[] getClassImplFromDisk(String classesFolder, String canonicalClassName) {
		byte[] result = new byte[0];
		String sep = IusCLFileUtils.getPathDelimiter();
		String fileClassName = canonicalClassName.replace(".", sep) + ".class";
		String fileClassFullPath = classesFolder + sep + fileClassName;
		try (FileInputStream fileInputStream = new FileInputStream(fileClassFullPath)) {
			result = fileInputStream.readAllBytes();
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("\"Error getting implementation class: \"{0}\" from folder: \"{1}\"", canonicalClassName,
					classesFolder);
			log.error(exceptionMessage, ioException);
			IusCLDesignErrorUtils.showEclipseErrorDialog(exceptionMessage, ioException);
		}
		return result;
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return (loadClass(className, true));
	}

	@Override
	public synchronized Class<?> loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
		Class<?> resultClass;
		byte[] classData;

		resultClass = classes.get(className);
		if (resultClass != null) {
			return resultClass;
		}

		try {
			resultClass = parentClassLoader.loadClass(className);
			return resultClass;
		} catch (ClassNotFoundException classNotFoundException) {
			/* OK */
		}

		try {
			resultClass = super.findSystemClass(className);
			return resultClass;
		} catch (ClassNotFoundException classNotFoundException) {
			/* OK */
		}

		classData = getClassImplFromDisk(classesFolder, className);
		if (classData == null) {
			throw new ClassNotFoundException();
		}

		resultClass = defineClass(null, classData, 0, classData.length);
		if (resultClass == null) {
			throw new ClassFormatError();
		}
		if (resolveIt) {
			resolveClass(resultClass);
		}

		classes.put(className, resultClass);
		/* OK */
		return resultClass;
	}
}
