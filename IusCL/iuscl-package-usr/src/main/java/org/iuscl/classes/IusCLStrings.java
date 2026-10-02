/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import org.iuscl.system.IusCLObject;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLStrings extends IusCLObject {

	final List<String> strings = new ArrayList<>();

	public IusCLStrings() {
		super();
	}

	public void add(String string) {
		strings.add(string);
		invokeNotify();
	}

	public void append(IusCLStrings strings) {
		this.strings.addAll(strings.strings);
		invokeNotify();
	}

	public void insert(Integer index, String string) {
		strings.add(index, string);
		invokeNotify();
	}

	public Integer size() {
		return strings.size();
	}

	public String get(Integer index) {
		return strings.get(index);
	}

	public void set(Integer index, String line) {
		strings.set(index, line);
		invokeNotify();
	}

	public void delete(Integer index) {
		strings.remove(index.intValue());
		invokeNotify();
	}

	public Integer indexOf(String string) {
		return strings.indexOf(string);
	}

	public void sort() {
		Collections.sort(strings);
		invokeNotify();
	}

	public String getText() {
		if (strings.isEmpty()) {
			return "";
		}
		StringJoiner stringJoiner = new StringJoiner(IusCLStrUtils.sLineBreak());
		for (String line : strings) {
			stringJoiner.add(line);
		}
		return stringJoiner.toString();
	}

	public void setText(String text) {
		strings.clear();
		if (text == null) {
			return;
		}
		String[] splitStrings = text.split(IusCLStrUtils.sLineBreak());
		strings.addAll(Arrays.asList(splitStrings));
//		for (int index = 0; index < splitStrings.length; index++) {
//			strings.add(splitStrings[index]);
//		}
	}

	public void loadFromFile(String fileName) {
//		if (!IusCLFileUtils.fileExists(fileName)) {
//			strings.clear();
//			return;
//		}
		try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
			loadFromStream(fileInputStream);
		} catch (FileNotFoundException fileNotFoundException) {
			String exceptionMessage = MessageFormat.format("FileNotFoundException loading strings from file: \"{0}\"", fileName);
			log.error(exceptionMessage, fileNotFoundException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, fileNotFoundException);
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("IOException loading strings from file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void loadFromResource(Class<?> relativeClass, String resourceName) {
		try (InputStream inputStream = relativeClass.getClassLoader().getResourceAsStream(resourceName)) {
			loadFromStream(inputStream);
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("IOException loading strings from resource: \"{0}\"", resourceName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void loadFromStream(InputStream inputStream) {
		strings.clear();
		try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strings.add(line);
			}
			invokeNotify();
		} catch (IOException ioException) {
			String exceptionMessage = "Exception loading strings from stream";
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void saveToFile(String fileName) {
		IusCLFileUtils.createFile(fileName);
		try (OutputStream outputStream = new FileOutputStream(fileName)) {
			saveToStream(outputStream);
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Exception saving strings to file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void saveToStream(OutputStream outputStream) {
		try (PrintStream printStream = new PrintStream(outputStream)) {
			int lastIndex = strings.size() - 1;
			for (int index = 0; index < lastIndex; index++) {
				printStream.println(strings.get(index));
			}
			printStream.print(strings.get(lastIndex));
		}
//		catch (IOException ioException) {
//			String exceptionMessage = "Exception saving strings to stream";
//			log.error(exceptionMessage, ioException);
//			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
//		}
	}

	public boolean getIsEmpty() {
		if (strings.size() > 1) {
			return false;
		}
		if (strings.isEmpty()) {
			return true;
		}
		return strings.get(0).isEmpty();
	}
}
