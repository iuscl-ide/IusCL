/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics.formats;

import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.iuscl.graphics.IusCLGraphic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLJpeg extends IusCLGraphic {

	Integer compressionQuality = 75; /* 1 ... 100 */

	@Override
	public void saveToFile(String fileName) {
		super.saveToFile(fileName);

		swtImageLoader.compression = compressionQuality;
		swtImageLoader.save(fileName, SWT.IMAGE_JPEG);
	}

	@Override
	public void saveToStream(OutputStream outputStream) {
		super.saveToStream(outputStream);

		swtImageLoader.compression = compressionQuality;
		swtImageLoader.save(outputStream, SWT.IMAGE_JPEG);
	}
}
