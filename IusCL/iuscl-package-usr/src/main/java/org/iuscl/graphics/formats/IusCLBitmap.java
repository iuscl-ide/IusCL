/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics.formats;

import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.iuscl.graphics.IusCLGraphic;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLBitmap extends IusCLGraphic {

	public enum IusCLBitmapHandleType {
		bmDIB, bmDDB
	}

	/* http://www.efg2.com/Lab/Library/Delphi/Graphics/BMP.htm */
	IusCLBitmapHandleType handleType = IusCLBitmapHandleType.bmDIB;

	private void makeHandleType(String bitmapName) {
		String extension = IusCLFileUtils.extractFileExt(bitmapName);

		if (IusCLStrUtils.equalValues(extension, "bmp")) {
			handleType = IusCLBitmapHandleType.bmDIB;
		} else if (IusCLStrUtils.equalValues(extension, "rle")) {
			handleType = IusCLBitmapHandleType.bmDDB;
		}
	}

	@Override
	public void loadFromFile(String fileName) {
		super.loadFromFile(fileName);

		makeHandleType(IusCLFileUtils.extractFileName(fileName));
	}

	@Override
	public void loadFromResource(Class<?> relativeClass, String resourceName) {
		super.loadFromResource(relativeClass, resourceName);

		makeHandleType(IusCLFileUtils.extractResourceFileName(resourceName));
	}

	@Override
	public void saveToFile(String fileName) {
		super.saveToFile(fileName);

		if (handleType == IusCLBitmapHandleType.bmDIB) {
			swtImageLoader.save(fileName, SWT.IMAGE_BMP);
		} else if (handleType == IusCLBitmapHandleType.bmDDB) {
			swtImageLoader.save(fileName, SWT.IMAGE_BMP_RLE);
		}
	}

	@Override
	public void saveToStream(OutputStream outputStream) {
		super.saveToStream(outputStream);

		if (handleType == IusCLBitmapHandleType.bmDIB) {
			swtImageLoader.save(outputStream, SWT.IMAGE_BMP);
		} else if (handleType == IusCLBitmapHandleType.bmDDB) {
			swtImageLoader.save(outputStream, SWT.IMAGE_BMP_RLE);
		}
	}
}
