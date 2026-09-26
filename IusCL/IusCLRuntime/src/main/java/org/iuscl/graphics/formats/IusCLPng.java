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
public class IusCLPng extends IusCLGraphic {

	public enum IusCLPngCompressionType {
		ctNoCompression, ctLargeFast, ctDefault, ctSmallSlow
	}

	IusCLPngCompressionType compressionType = IusCLPngCompressionType.ctDefault; /* 0, 1, 2, 3 */

	@Override
	public void saveToFile(String fileName) {
		super.saveToFile(fileName);

		swtImageLoader.compression = compressionType.ordinal();
		swtImageLoader.save(fileName, SWT.IMAGE_PNG);
	}

	@Override
	public void saveToStream(OutputStream outputStream) {
		super.saveToStream(outputStream);

		swtImageLoader.compression = compressionType.ordinal();
		swtImageLoader.save(outputStream, SWT.IMAGE_PNG);
	}
}
