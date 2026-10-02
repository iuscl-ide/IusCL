/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysctrls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.iuscl.sysutils.IusCLFileSearchRec;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLProgressMonitor;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLZip extends IusCLComponent {

	@Getter
	@Setter
	String zipFileName = null;
	@Getter
	@Setter
	String rootFolder = null;

	String internalRootFolder = "";
	ZipOutputStream javaZipOutputStream = null;

	public IusCLZip(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("ZipFileName", IusCLPropertyType.ptString, "");
		defineProperty("RootFolder", IusCLPropertyType.ptString, "");

	}

	public void addFolderIncludingName(String folderName) {
		addFolderIncludingName(folderName, null, null);
	}

	public void addFolderIncludingName(String folderName, IusCLProgressMonitor listZipProgressMonitor, IusCLProgressMonitor fileZipProgressMonitor) {
		internalRootFolder = IusCLFileUtils.extractFilePath(folderName);
		internalRootFolder = IusCLFileUtils.includeTrailingPathDelimiter(internalRootFolder);
		writeFolder(folderName, listZipProgressMonitor, fileZipProgressMonitor);
	}

	public void addFolderContent(String folderName) {
		addFolderContent(folderName, null, null);
	}

	public void addFolderContent(String folderName, IusCLProgressMonitor listZipProgressMonitor, IusCLProgressMonitor fileZipProgressMonitor) {
		internalRootFolder = IusCLFileUtils.includeTrailingPathDelimiter(folderName);
		writeFolder(folderName, listZipProgressMonitor, fileZipProgressMonitor);
	}

	public void addFile(String fileName) {
		addFile(fileName, null);
	}

	public void addFile(String fileName, IusCLProgressMonitor fileZipProgressMonitor) {
		internalRootFolder = IusCLFileUtils.extractFilePath(fileName);
		internalRootFolder = IusCLFileUtils.includeTrailingPathDelimiter(internalRootFolder);

		startWrite();
		write(fileName, fileZipProgressMonitor);
		endWrite();
	}

	public void addFilesAndFoldersFromRootFolder(IusCLStrings filesAndFolders) {
		addFilesAndFoldersFromRootFolder(filesAndFolders, null, null);
	}

	public void addFilesAndFoldersFromRootFolder(IusCLStrings filesAndFolders, IusCLProgressMonitor listZipProgressMonitor,
			IusCLProgressMonitor fileZipProgressMonitor) {
		if (IusCLStrUtils.isNotNullNotEmpty(rootFolder)) {
			internalRootFolder = rootFolder;
		} else {
			internalRootFolder = "";
		}

		writeFilesAndFolders(filesAndFolders, listZipProgressMonitor, fileZipProgressMonitor);
	}

	private void writeFilesAndFolders(IusCLStrings filesAndFolders, IusCLProgressMonitor listZipProgressMonitor,
			IusCLProgressMonitor fileZipProgressMonitor) {
		startWrite();

		if (listZipProgressMonitor != null) {
			listZipProgressMonitor.setBegin(0);
			listZipProgressMonitor.setEnd(filesAndFolders.size());
		}

		for (int index = 0; index < filesAndFolders.size(); index++) {
			String fileName = filesAndFolders.get(index);

			if (listZipProgressMonitor != null) {
				listZipProgressMonitor.setShortMessage(fileName);
			}

			write(fileName, fileZipProgressMonitor);

			if (listZipProgressMonitor != null) {
				listZipProgressMonitor.setPosition(index);
			}
		}

		endWrite();
	}

	private void startWrite() {
		try {
			javaZipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
		} catch (FileNotFoundException fileNotFoundException) {
			String exceptionMessage = MessageFormat.format("Zip file not found in start write of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, fileNotFoundException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, fileNotFoundException);
		}
	}

	private void write(String fileName, IusCLProgressMonitor fileZipProgressMonitor) {
		String entryName = fileName;

		entryName = entryName.substring(internalRootFolder.length());
		entryName = entryName.replace(IusCLFileUtils.getPathDelimiter(), "/");

		try {
			if (IusCLFileUtils.fileExists(fileName)) {
				ZipEntry javaZipEntry = new ZipEntry(entryName);
				javaZipOutputStream.putNextEntry(javaZipEntry);
				byte[] readBuffer = IusCLFileUtils.readFileIntoBuffer(fileName);

				if (fileZipProgressMonitor != null) {
					fileZipProgressMonitor.setBegin(0);
					fileZipProgressMonitor.setEnd(readBuffer.length);
				}
				javaZipOutputStream.write(readBuffer);
			} else {
				ZipEntry javaZipEntry = new ZipEntry(entryName + "/");
				javaZipOutputStream.putNextEntry(javaZipEntry);
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("File exception in zipping of file: \"{0}\"", fileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	private void writeFolder(String folderName, IusCLProgressMonitor listZipProgressMonitor, IusCLProgressMonitor fileZipProgressMonitor) {
		IusCLFileSearchRec srcSearchRec = new IusCLFileSearchRec();
		srcSearchRec.setReturnFolders(true);

		IusCLStrings filesAndFolders = IusCLFileUtils.findFiles(folderName, srcSearchRec);
		writeFilesAndFolders(filesAndFolders, listZipProgressMonitor, fileZipProgressMonitor);
	}

	private void endWrite() {
		try {
			javaZipOutputStream.close();
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("File exception in zipping of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}
}
