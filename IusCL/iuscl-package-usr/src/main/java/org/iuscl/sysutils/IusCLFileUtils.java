/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.iuscl.classes.IusCLStrings;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class IusCLFileUtils {

	private final Integer BUFFER_SIZE = 8 * 1024;

	public String extractFileExt(String fileName) {
		return extractFileExt(fileName, false);
	}

	public String extractFileExt(String fileName, boolean toLowerCase) {
		int lastDotPos = fileName.lastIndexOf('.');
		String extension = "";
		if (lastDotPos > -1) {
			extension = fileName.substring(lastDotPos + 1);
		}
		if (toLowerCase) {
			extension = extension.toLowerCase();
		}
		return extension;
	}

	public String extractFileName(String fileName) {
		String sep = getPathDelimiter();

		int lastSepPos = fileName.lastIndexOf(sep);
		if (lastSepPos > -1) {
			return fileName.substring(lastSepPos + 1);
		}
		return fileName;
	}

	public String extractResourceFileName(String resourceName) {
		String sep = "/";

		int lastSepPos = resourceName.lastIndexOf(sep);
		if (lastSepPos > -1) {
			return resourceName.substring(lastSepPos + 1);
		}
		return resourceName;
	}

	public String extractResourcePath(String resourceName) {
		String sep = "/";

		int lastSepPos = resourceName.lastIndexOf(sep);
		if (lastSepPos > -1) {
			return resourceName.substring(0, lastSepPos);
		}
		return "";
	}

	public String extractFilePath(String fileName) {
		String sep = getPathDelimiter();

		int lastSepPos = fileName.lastIndexOf(sep);
		if (lastSepPos > -1) {
			return fileName.substring(0, lastSepPos);
		}
		return "";
	}

	public boolean fileExists(String fileName) {
		File javaFile = new File(fileName);
		return javaFile.exists() && javaFile.isFile();
	}

	public boolean folderExists(String folder) {
		File javaFolder = new File(folder);
		return javaFolder.exists() && javaFolder.isDirectory();
	}

	public void createFolder(String folder) {
		if (!folderExists(folder)) {
			File javaFolder = new File(folder);
			if (!javaFolder.mkdirs()) {
				String exceptionMessage = MessageFormat.format("\"Error creating the folder: \"{0}\"", folder);
				log.error(exceptionMessage);
				IusCLErrorUtils.showErrorDialog(exceptionMessage);
			}
		}
	}

	public void createFile(String fileName) {
		String filePath = extractFilePath(fileName);
		createFolder(filePath);

		File javaFile = new File(fileName);
		try {
			if (!javaFile.createNewFile()) {
				String exceptionMessage = MessageFormat.format("\"Error creating new file: \"{0}\"", fileName);
				log.error(exceptionMessage);
				IusCLErrorUtils.showErrorDialog(exceptionMessage);
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("\"Error creating new file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public String createPathName(String pathFirstPartName, String... pathMorePartNames) {
		return Paths.get(pathFirstPartName, pathMorePartNames).toAbsolutePath().toString();
	}

	public String getCurrentFolder() {
		String currentFolder = null;

		File javaFile = new File(".");
		try {
			currentFolder = javaFile.getCanonicalPath();
		} catch (IOException ioException) {
			String exceptionMessage = "Error getting current folder";
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}

		return currentFolder;
	}

	public String getPathDelimiter() {
		return File.separator;
	}

	public String includeTrailingPathDelimiter(String pathName) {
		String delimiterPath = pathName;

		if (delimiterPath.lastIndexOf(File.separator) != (delimiterPath.length() - 1)) {
			delimiterPath = delimiterPath + File.separator;
		}

		return delimiterPath;
	}

	public void deleteFile(String fileName) {
		try {
			Files.deleteIfExists(Paths.get(fileName));
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("\"Error deleting file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	private void deleteEmptyFolder(String folderName) {
		try {
			Files.deleteIfExists(Paths.get(folderName));
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("\"Error deleting empty folder: \"{0}\"", folderName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void deleteFolderIncludingName(String folderName) {
		deleteFolderIncludingName(folderName, null);
	}

	public void deleteFolderIncludingName(String folderName, IusCLProgressMonitor deleteProgressMonitor) {
		deleteFolderContent(folderName, deleteProgressMonitor);
		deleteEmptyFolder(folderName);
	}

	public void deleteFolderContent(String folderName) {
		deleteFolderContent(folderName, null);
	}

	public void deleteFolderContent(String folderName, IusCLProgressMonitor deleteProgressMonitor) {
		IusCLFileSearchRec srcSearchRec = new IusCLFileSearchRec();

		IusCLStrings srcFileNames = findFiles(folderName, srcSearchRec);

		srcSearchRec.setReturnFolders(true);
		srcSearchRec.setReturnFiles(false);
		IusCLStrings srcFolderNames = findFiles(folderName, srcSearchRec);

		if (deleteProgressMonitor != null) {
			deleteProgressMonitor.setBegin(0);
			deleteProgressMonitor.setEnd(srcFileNames.size() + srcFolderNames.size());
		}

		int pos = 0;

		for (int index = 0; index < srcFileNames.size(); index++) {
			String delFileName = srcFileNames.get(index);
			if (deleteProgressMonitor != null) {
				deleteProgressMonitor.setShortMessage(delFileName);
			}
			deleteFile(delFileName);
			if (deleteProgressMonitor != null) {
				pos = pos + index;
				deleteProgressMonitor.setPosition(pos);
			}
		}

		for (int index = 0; index < srcFolderNames.size(); index++) {
			String delFolderName = srcFolderNames.get(srcFolderNames.size() - (index + 1));
			if (deleteProgressMonitor != null) {
				deleteProgressMonitor.setShortMessage(delFolderName);
			}
			deleteEmptyFolder(delFolderName);
			if (deleteProgressMonitor != null) {
				pos = pos + index;
				deleteProgressMonitor.setPosition(pos);
			}
		}
	}

	public Integer getFileSize(String fileName) {
		File javaFile = new File(fileName);
		return Integer.valueOf((int) javaFile.length());
	}

	public void copyFile(String fileNameSrc, String fileNameDest) {
		copyFile(fileNameSrc, fileNameDest, null);
	}

	public void copyFile(String fileNameSrc, String fileNameDest, IusCLProgressMonitor progressMonitor) {
		createFile(fileNameDest);
		try (InputStream inputStream = new FileInputStream(fileNameSrc); OutputStream outputStream = new FileOutputStream(fileNameDest)) {
			if (progressMonitor != null) {
				progressMonitor.setBegin(0);
				progressMonitor.setEnd(getFileSize(fileNameSrc));
				progressMonitor.setShortMessage(fileNameSrc);
				progressMonitor.setLongMessage(fileNameDest);
			}
			Integer pos = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
				if (progressMonitor != null) {
					pos = pos + BUFFER_SIZE;
					progressMonitor.setPosition(pos);
				}
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Error on copy file: \"{0}\" into the file: \"{1}\"", fileNameSrc, fileNameDest);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public void copyFolderIncludingName(String folderNameSrc, String folderNameDest) {
		copyFolderIncludingName(folderNameSrc, folderNameDest, null, null);
	}

	public void copyFolderIncludingName(String folderNameSrc, String folderNameDest, IusCLProgressMonitor folderProgressMonitor,
			IusCLProgressMonitor fileProgressMonitor) {
		folderNameDest = includeTrailingPathDelimiter(folderNameDest) + extractFileName(folderNameSrc);
		copyFolderContent(folderNameSrc, folderNameDest, folderProgressMonitor, fileProgressMonitor);
	}

	public void copyFolderContent(String folderNameSrc, String folderNameDest) {
		copyFolderContent(folderNameSrc, folderNameDest, null, null);
	}

	public void copyFolderContent(String folderNameSrc, String folderNameDest, IusCLProgressMonitor folderProgressMonitor,
			IusCLProgressMonitor fileProgressMonitor) {
		int posSrc = folderNameSrc.length();

		IusCLFileSearchRec srcSearchRec = new IusCLFileSearchRec();
		srcSearchRec.setReturnFolders(true);

		IusCLStrings srcFileNames = findFiles(folderNameSrc, srcSearchRec);

		if (folderProgressMonitor != null) {
			folderProgressMonitor.setBegin(0);
			folderProgressMonitor.setEnd(srcFileNames.size());
		}

		for (int index = 0; index < srcFileNames.size(); index++) {
			String fileNameSrc = srcFileNames.get(index);
			String fileNameDest = folderNameDest + fileNameSrc.substring(posSrc);

			if (folderProgressMonitor != null) {
				folderProgressMonitor.setShortMessage(fileNameSrc);
			}

			if (fileExists(fileNameSrc)) {
				copyFile(fileNameSrc, fileNameDest, fileProgressMonitor);
			} else {
				createFolder(fileNameDest);
			}

			if (folderProgressMonitor != null) {
				folderProgressMonitor.setPosition(index);
			}
		}
	}

	public void renameFile(String fileOldNameIncludingPath, String fileNewNameNoPath) {
		if (!fileExists(fileOldNameIncludingPath)) {
			return;
		}

		String newFileName = extractFilePath(fileOldNameIncludingPath);
		newFileName = includeTrailingPathDelimiter(newFileName);
		newFileName = newFileName + fileNewNameNoPath;

		if (fileExists(newFileName)) {
			deleteFile(newFileName);
		}

		File oldfile = new File(fileOldNameIncludingPath);
		File newfile = new File(newFileName);

		if (!oldfile.renameTo(newfile)) {
			String exceptionMessage = MessageFormat.format("\"Error renaming file: \"{0}\" as \"{1}\"", fileOldNameIncludingPath, newFileName);
			log.error(exceptionMessage);
			IusCLErrorUtils.showErrorDialog(exceptionMessage);
		}
	}

	public void renameFolder(String folderOldNameIncludingPath, String folderNewNameNoPath) {
		if (!folderExists(folderOldNameIncludingPath)) {
			return;
		}

		String newFolderName = extractFilePath(folderOldNameIncludingPath);
		newFolderName = includeTrailingPathDelimiter(newFolderName);
		newFolderName = newFolderName + folderNewNameNoPath;

		if (folderExists(newFolderName)) {
			return;
		}

		File oldFolder = new File(folderOldNameIncludingPath);
		File newFolder = new File(newFolderName);

		if (!oldFolder.renameTo(newFolder)) {
			String exceptionMessage = MessageFormat.format("\"Error renaming folder: \"{0}\" as \"{1}\"", folderOldNameIncludingPath, newFolderName);
			log.error(exceptionMessage);
			IusCLErrorUtils.showErrorDialog(exceptionMessage);
		}
	}

	public byte[] readFileIntoBuffer(String fileName) {
		if (!fileExists(fileName)) {
			return new byte[0];
		}
		File javaFile = new File(fileName);
		try (InputStream inputStream = new FileInputStream(javaFile)) {
			return inputStream.readAllBytes();
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Error reading into buffer from file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
		return new byte[0];
	}

	public void writeBufferIntoFile(String fileName, byte[] bytes) {
		createFile(fileName);
		File javaFile = new File(fileName);
		try (OutputStream outputStream = new FileOutputStream(javaFile)) {
			outputStream.write(bytes);
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Error writing from buffer into file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	public IusCLStrings findFiles(String startingFolder, final IusCLFileSearchRec fileSearchRec) {
		FilenameFilter javaFilenameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				String ps = getPathDelimiter();

				if (IusCLFileUtils.folderExists(dir + ps + name) && !fileSearchRec.getReturnFolders()) {
					return true;
				}

				String includeNamePattern = fileSearchRec.getIncludeNamePattern();

				if (IusCLStrUtils.isNotNullNotEmpty(includeNamePattern) && name.indexOf(includeNamePattern) == -1) {
					return false;
				}

				String excludeNamePattern = fileSearchRec.getExcludeNamePattern();

				return !(IusCLStrUtils.isNotNullNotEmpty(excludeNamePattern) && name.indexOf(excludeNamePattern) > -1);
			}
		};

		IusCLStrings files = new IusCLStrings();

		try {
			File javaStartingFolder = new File(startingFolder);
			File[] javaFilesAndFolders = javaStartingFolder.listFiles(javaFilenameFilter);

			if (javaFilesAndFolders == null) {
				return files;
			}

			for (File javaFile : javaFilesAndFolders) {
				if (javaFile.isDirectory()) {
					if (fileSearchRec.getReturnFolders()) {
						files.add(javaFile.getCanonicalPath());
					}
					if (fileSearchRec.getIsRecursive()) {
						files.append(findFiles(javaFile.getAbsolutePath(), fileSearchRec));
					}
				} else {
					if (fileSearchRec.getReturnFiles()) {
						files.add(javaFile.getCanonicalPath());
					}
				}
			}

			if (fileSearchRec.getIsSorted()) {
				files.sort();
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Error on finding files in starting folder: \"{0}\"", startingFolder);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}

		return files;
	}
}
