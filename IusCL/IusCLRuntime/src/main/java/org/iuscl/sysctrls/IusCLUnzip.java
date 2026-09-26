/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysctrls;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLErrorUtils;
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
public class IusCLUnzip extends IusCLComponent {

	@Getter
	@Setter
	String zipFileName = null;
	@Getter
	@Setter
	String destinationFolder = null;
	@Getter
	@Setter
	String zipRootFolder = null;

	String internalRootFolder = "";
	ZipFile javaZipFile = null;
	ZipInputStream javaZipInputStream = null;

	public IusCLUnzip(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("ZipFileName", IusCLPropertyType.ptString, "");
		defineProperty("DestinationFolder", IusCLPropertyType.ptString, "");
		defineProperty("ZipRootFolder", IusCLPropertyType.ptString, "");

	}

	public IusCLStrings getFilesAndFoldersList() {
		startRead();

		IusCLStrings list = new IusCLStrings();

		try {
			ZipEntry entry = null;
			while ((entry = javaZipInputStream.getNextEntry()) != null) {
				list.add(entry.getName());
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("File exception in listing zip content of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}

		endRead();

		return list;
	}

	public void extractAll() {
		extractAll(null, null);
	}

	public void extractAll(IusCLProgressMonitor listUnzipProgressMonitor, IusCLProgressMonitor fileUnzipProgressMonitor) {
		extractFolderContent("", listUnzipProgressMonitor, fileUnzipProgressMonitor);
	}

	public void extractFolderIncludingName(String folderNameInZipIncludingPath) {
		extractFolderIncludingName(folderNameInZipIncludingPath, null, null);
	}

	public void extractFolderIncludingName(String folderNameInZipIncludingPath, IusCLProgressMonitor listUnzipProgressMonitor,
			IusCLProgressMonitor fileUnzipProgressMonitor) {
		IusCLStrings filesAndFolders = new IusCLStrings();
		filesAndFolders.add(folderNameInZipIncludingPath);
		internalRootFolder = IusCLFileUtils.extractResourcePath(folderNameInZipIncludingPath);

		extractFilesAndFoldersFromInternalRootFolder(filesAndFolders, listUnzipProgressMonitor, fileUnzipProgressMonitor);
	}

	public void extractFolderContent(String folderNameInZipIncludingPath) {
		extractFolderContent(folderNameInZipIncludingPath, null, null);
	}

	public void extractFolderContent(String folderNameInZipIncludingPath, IusCLProgressMonitor listUnzipProgressMonitor,
			IusCLProgressMonitor fileUnzipProgressMonitor) {
		IusCLStrings filesAndFolders = new IusCLStrings();
		filesAndFolders.add(folderNameInZipIncludingPath);
		internalRootFolder = folderNameInZipIncludingPath;

		extractFilesAndFoldersFromInternalRootFolder(filesAndFolders, listUnzipProgressMonitor, fileUnzipProgressMonitor);
	}

	public void extractFile(String fileNameInZipIncludingPath) {
		extractFile(fileNameInZipIncludingPath, null);
	}

	public void extractFile(String fileNameInZipIncludingPath, IusCLProgressMonitor fileUnzipProgressMonitor) {
		IusCLStrings filesAndFolders = new IusCLStrings();
		filesAndFolders.add(fileNameInZipIncludingPath);
		internalRootFolder = IusCLFileUtils.extractResourcePath(fileNameInZipIncludingPath);

		extractFilesAndFoldersFromInternalRootFolder(filesAndFolders, null, fileUnzipProgressMonitor);
	}

	public void extractFilesAndFoldersFromRootFolder(IusCLStrings filesAndFolders) {
		extractFilesAndFoldersFromRootFolder(filesAndFolders, null, null);
	}

	public void extractFilesAndFoldersFromRootFolder(IusCLStrings filesAndFolders, IusCLProgressMonitor listUnzipProgressMonitor,
			IusCLProgressMonitor fileUnzipProgressMonitor) {
		if (IusCLStrUtils.isNotNullNotEmpty(zipRootFolder)) {
			internalRootFolder = zipRootFolder;
		} else {
			internalRootFolder = "";
		}

		extractFilesAndFoldersFromInternalRootFolder(filesAndFolders, listUnzipProgressMonitor, fileUnzipProgressMonitor);
	}

	private void extractFilesAndFoldersFromInternalRootFolder(IusCLStrings filesAndFolders, IusCLProgressMonitor listUnzipProgressMonitor,
			IusCLProgressMonitor fileUnzipProgressMonitor) {
		IusCLStrings zipFilesAndFolders = new IusCLStrings();

		String rootFolderPrefix = internalRootFolder.toLowerCase();
		for (int index = 0; index < filesAndFolders.size(); index++) {
			String fileOrFolder = filesAndFolders.get(index);
			fileOrFolder = fileOrFolder.toLowerCase();

			if (fileOrFolder.startsWith(rootFolderPrefix)) {
				zipFilesAndFolders.add(fileOrFolder);
			}
		}

		zipFilesAndFolders.sort();

		/* Reduce list */
		IusCLStrings listReduce = new IusCLStrings();
		String reduceFileName = zipFilesAndFolders.get(0);
		listReduce.add(reduceFileName);
		for (int index = 0; index < zipFilesAndFolders.size(); index++) {
			String indexfileName = zipFilesAndFolders.get(index);

			if (!indexfileName.startsWith(reduceFileName)) {
				reduceFileName = indexfileName;
				listReduce.add(reduceFileName);
				// System.out.println("Reduce: " + reduceFileName);
			}
		}

		/* Real list */
		IusCLStrings listReal = new IusCLStrings();
		IusCLStrings listZip = getFilesAndFoldersList();

		for (int indexZip = 0; indexZip < listZip.size(); indexZip++) {
			String listZipFileName = listZip.get(indexZip);

			for (int indexReduce = 0; indexReduce < listReduce.size(); indexReduce++) {
				reduceFileName = listReduce.get(indexReduce);

				if (listZipFileName.toLowerCase().startsWith(reduceFileName)) {
					listReal.add(listZipFileName);
					// System.out.println("Real: " + zipFileName);
					break;
				}
			}
		}

		startRead();

		if (listUnzipProgressMonitor != null) {
			listUnzipProgressMonitor.setBegin(0);
			listUnzipProgressMonitor.setEnd(listReal.size());
		}

		Integer bufferSize = 8 * 1024;
		byte[] bytes = new byte[bufferSize];

		String destFolder = IusCLFileUtils.includeTrailingPathDelimiter(destinationFolder);
		String pd = IusCLFileUtils.getPathDelimiter();
		try {
			ZipEntry entryStream = null;
			Integer realIndex = 0;
			while (((entryStream = javaZipInputStream.getNextEntry()) != null) && (realIndex < listReal.size())) {
				String entryName = entryStream.getName();
				if (IusCLStrUtils.equalValues(entryName, listReal.get(realIndex))) {
					if (listUnzipProgressMonitor != null) {
						listUnzipProgressMonitor.setShortMessage(entryName);
					}

					entryName = entryName.substring(internalRootFolder.length());
					entryName = entryName.replace("/", pd);
					entryName = destFolder + entryName;

					if (entryName.endsWith(pd)) {
						/* Folder, create */
						IusCLFileUtils.createFolder(entryName);
					} else {
						/* File, extract */
						ZipEntry entryFile = javaZipFile.getEntry(entryName);
						Integer entrySize = (int) entryFile.getSize();

						// ByteArrayOutputStream javaByteArrayOutputStream = new
						// ByteArrayOutputStream((int)entry.getSize());
						ByteArrayOutputStream javaByteArrayOutputStream = new ByteArrayOutputStream(entrySize);

						if (fileUnzipProgressMonitor != null) {
							fileUnzipProgressMonitor.setBegin(0);
							fileUnzipProgressMonitor.setEnd(entrySize);
						}

						int count = 0;
						int countTotal = 0;
						while ((count = javaZipInputStream.read(bytes, 0, bufferSize)) != -1) {
							javaByteArrayOutputStream.write(bytes, 0, count);

							if (fileUnzipProgressMonitor != null) {
								countTotal = countTotal + count;
								fileUnzipProgressMonitor.setPosition(countTotal);
							}
						}

						IusCLFileUtils.writeBufferIntoFile(entryName, javaByteArrayOutputStream.toByteArray());
						javaByteArrayOutputStream.close();
					}

					if (listUnzipProgressMonitor != null) {
						listUnzipProgressMonitor.setPosition(realIndex);
					}
					realIndex = realIndex + 1;
				}
			}
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("File exception in unzipping of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}

		endRead();
	}

	private void startRead() {
		try {
			javaZipFile = new ZipFile(zipFileName);
			javaZipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)));
		} catch (FileNotFoundException fileNotFoundException) {
			String exceptionMessage = MessageFormat.format("Zip file not found in start read of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, fileNotFoundException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, fileNotFoundException);
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("Zip file IOException in start read of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}

	private void endRead() {
		try {
			javaZipInputStream.close();
			javaZipFile.close();
		} catch (IOException ioException) {
			String exceptionMessage = MessageFormat.format("File exception in unzipping of file: \"{0}\"", zipFileName);
			log.error(exceptionMessage, ioException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
		}
	}
}
