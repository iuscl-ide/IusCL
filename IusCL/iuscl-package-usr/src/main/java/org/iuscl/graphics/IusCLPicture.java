/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.iuscl.classes.IusCLPersistent;
import org.iuscl.graphics.formats.IusCLBitmap;
import org.iuscl.graphics.formats.IusCLGif;
import org.iuscl.graphics.formats.IusCLIcon;
import org.iuscl.graphics.formats.IusCLJpeg;
import org.iuscl.graphics.formats.IusCLPng;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.sysutils.IusCLFileUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPicture extends IusCLPersistent {

	@Getter
	IusCLGraphic graphic = null;
	@Getter
	@Setter
	String initialSimpleFileName = null;
	@Getter
	@Setter
	String pictureExtension = null;
	@Getter
	String sourceFileName = null;
	@Getter
	Class<?> sourceRelativeClass = null;
	@Getter
	String sourceResourceName = null;

	private static final Map<String, Class<?>> registeredGraphicClasses = new HashMap<>();

	static {
		IusCLPicture.registerGraphicClass("bmp", IusCLBitmap.class);

		IusCLPicture.registerGraphicClass("rle", IusCLBitmap.class);

		IusCLPicture.registerGraphicClass("jpg", IusCLJpeg.class);
		IusCLPicture.registerGraphicClass("jpeg", IusCLJpeg.class);

		IusCLPicture.registerGraphicClass("gif", IusCLGif.class);

		IusCLPicture.registerGraphicClass("ico", IusCLIcon.class);

		IusCLPicture.registerGraphicClass("png", IusCLPng.class);
	}

	public static void registerGraphicClass(String fileExtension, Class<?> graphicClass) {
		registeredGraphicClasses.put(fileExtension, graphicClass);
	}

	private IusCLGraphic makeGraphicFromPictureExtension() {
		IusCLGraphic typedGraphic = null;

		if (registeredGraphicClasses.containsKey(pictureExtension)) {
			Class<?> graphicClass = registeredGraphicClasses.get(pictureExtension);
			typedGraphic = (IusCLGraphic) IusCLObjUtils.invokeConstructor(graphicClass);
		} else {
			typedGraphic = new IusCLGraphic();
		}

		return typedGraphic;
	}

	public void loadFromFile(String fileName) {
		initialSimpleFileName = IusCLFileUtils.extractFileName(fileName);
		pictureExtension = IusCLFileUtils.extractFileExt(initialSimpleFileName);
		graphic = makeGraphicFromPictureExtension();

		graphic.loadFromFile(fileName);

		sourceFileName = fileName;
		sourceRelativeClass = null;
		sourceResourceName = null;

		invokeNotify();
	}

	public void loadFromResource(Class<?> relativeClass, String resourceName) {
		initialSimpleFileName = IusCLFileUtils.extractResourceFileName(resourceName);
		pictureExtension = IusCLFileUtils.extractFileExt(initialSimpleFileName);
		graphic = makeGraphicFromPictureExtension();

		graphic.loadFromStream(relativeClass.getClassLoader().getResourceAsStream(resourceName));

		sourceFileName = null;
		sourceRelativeClass = relativeClass;
		sourceResourceName = resourceName;

		invokeNotify();
	}

	public void loadFromStream(InputStream stream) {
		initialSimpleFileName = null;
		pictureExtension = null;
		graphic = makeGraphicFromPictureExtension();

		graphic.loadFromStream(stream);

		sourceFileName = null;
		sourceRelativeClass = null;
		sourceResourceName = null;

		invokeNotify();
	}

	public boolean getIsEmpty() {
		if (graphic == null) {
			return true;
		}
		return graphic.getIsEmpty();
	}

	public void setGraphic(IusCLGraphic graphic) {
		this.graphic = graphic;

		invokeNotify();
	}
}
