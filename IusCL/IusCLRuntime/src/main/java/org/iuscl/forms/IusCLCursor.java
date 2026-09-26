/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.iuscl.graphics.IusCLGraphic;
import org.iuscl.graphics.IusCLPicture;

public class IusCLCursor {

	public enum IusCLPredefinedCursors {
		crDefault, crNone, crArrow, crCross, crIBeam, crSizeNESW, crSizeNS, crSizeNWSE, crSizeWE, crUpArrow, crHourGlass, crDrag, crNoDrop, crHSplit,
		crVSplit, crMultiDrag, crSQLWait, crNo, crAppStart, crHelp, crHandPoint, crSize, crSizeAll
	}

	private static HashMap<String, Cursor> cursors = new HashMap<>();

	private static Cursor getPredefinedSwtCursor(IusCLPredefinedCursors predefinedCursor) {
		Cursor cursor = null;

		switch (predefinedCursor) {
		case crDefault:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;
		case crNone:
			cursor = getSystemSwtCursor(SWT.CURSOR_NO);
			break;
		case crArrow:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;
		case crCross:
			cursor = getSystemSwtCursor(SWT.CURSOR_CROSS);
			break;
		case crIBeam:
			cursor = getSystemSwtCursor(SWT.CURSOR_IBEAM);
			break;
		case crSizeNESW:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZENESW);
			break;
		case crSizeNS:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZENS);
			break;
		case crSizeNWSE:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZENWSE);
			break;
		case crSizeWE:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZEWE);
			break;
		case crUpArrow:
			cursor = getSystemSwtCursor(SWT.CURSOR_UPARROW);
			break;
		case crHourGlass:
			cursor = getSystemSwtCursor(SWT.CURSOR_WAIT);
			break;

		case crNo:
			cursor = getSystemSwtCursor(SWT.CURSOR_NO);
			break;
		case crAppStart:
			cursor = getSystemSwtCursor(SWT.CURSOR_APPSTARTING);
			break;
		case crHelp:
			cursor = getSystemSwtCursor(SWT.CURSOR_HELP);
			break;
		case crHandPoint:
			cursor = getSystemSwtCursor(SWT.CURSOR_HAND);
			break;
		case crSize:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZES);
			break;
		case crSizeAll:
			cursor = getSystemSwtCursor(SWT.CURSOR_SIZEALL);
			break;

		/* Specific */
		case crDrag:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;
		case crNoDrop:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;
		case crHSplit:
			loadFromResource("crHSplit", IusCLScreen.class, "resources/cursors/crHSplit.gif", 15, 15);
			cursor = cursors.get("crHSplit");
			break;
		case crVSplit:
			loadFromResource("crVSplit", IusCLScreen.class, "resources/cursors/crVSplit.gif", 15, 15);
			cursor = cursors.get("crVSplit");
			break;
		case crMultiDrag:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;
		case crSQLWait:
			cursor = getSystemSwtCursor(SWT.CURSOR_ARROW);
			break;

		default:
			break;
		}

		return cursor;
	}

	private static Cursor getSystemSwtCursor(int swtSystemCursorValue) {
		return Display.getDefault().getSystemCursor(swtSystemCursorValue);
	}

	public static Cursor getAsSwtCursor(IusCLPredefinedCursors predefinedCursor) {
		return getAsSwtCursor(predefinedCursor.name());
	}

	public static Cursor getAsSwtCursor(String cursorName) {
		Cursor swtCursor = null;

		IusCLPredefinedCursors predefinedCursor = null;

		try {
			predefinedCursor = IusCLPredefinedCursors.valueOf(cursorName);
		} catch (Exception exception) {
			/*  */
		}

		if (predefinedCursor != null) {
			swtCursor = IusCLCursor.getPredefinedSwtCursor(predefinedCursor);
		} else {
			swtCursor = cursors.get(cursorName);
		}

		return swtCursor;
	}

	public static void loadFromFile(String cursorName, String fileName, int hotspotX, int hotspotY) {
		if (cursors.get(cursorName) != null) {
			/* Already a cursor with this name */
			return;
		}
		IusCLPicture picture = new IusCLPicture();
		picture.loadFromFile(fileName);

		loadFromGraphic(cursorName, picture.getGraphic(), hotspotX, hotspotY);
	}

	public static void loadFromResource(String cursorName, Class<?> relativeClass, String resourceName, int hotspotX, int hotspotY) {
		if (cursors.get(cursorName) != null) {
			/* Already a cursor with this name */
			return;
		}
		IusCLPicture picture = new IusCLPicture();
		picture.loadFromResource(relativeClass, resourceName);

		loadFromGraphic(cursorName, picture.getGraphic(), hotspotX, hotspotY);
	}

	public static void loadFromGraphic(String cursorName, IusCLGraphic graphic, int hotspotX, int hotspotY) {
		if (cursors.get(cursorName) != null) {
			/* Already a cursor with this name */
			return;
		}
		ImageData swtImageData = graphic.getSwtImage().getImageData();

		Cursor swtCursor = new Cursor(Display.getCurrent(), swtImageData, hotspotX, hotspotY);
		cursors.put(cursorName, swtCursor);
	}
}
