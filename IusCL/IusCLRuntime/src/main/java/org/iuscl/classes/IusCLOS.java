/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.internal.win32.MARGINS;
import org.eclipse.swt.internal.win32.MSG;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFontStyle;

public class IusCLOS {

	private static final int COLOR_BACKGROUND = 1;
	private static final int COLOR_ACTIVEBORDER = 10;
	private static final int COLOR_INACTIVEBORDER = 11;
	private static final int COLOR_APPWORKSPACE = 12;
	private static final int COLOR_HIGHLIGHT = 13;
	private static final int COLOR_HIGHLIGHTTEXT = 14;
	private static final int COLOR_GRAYTEXT = 17;
	private static final int COLOR_HOTLIGHT = 26;
	private static final int COLOR_MENUHILIGHT = 29;
	private static final int COLOR_MENUBAR = 30;

	public enum IusCLOSType {
		ostWindows, ostLinux, ostMac
	}

	public static IusCLOSType getOSType() {
		return IusCLOSType.ostLinux;
	}

	/* Windows */

	private static MSG msg = new MSG();

	public static Object osIusCLApplication_PeekMessage() {
		if (OS.PeekMessage(msg, 0, 0, 0, OS.PM_NOREMOVE)) {
			return msg;
		}

		return null;
	}

	public static boolean osIusCLScreen_isAero(Shell swtShell) {
		MARGINS margins = new MARGINS();
		margins.cyTopHeight = 0;

		// if (OS.DwmExtendFrameIntoClientArea(swtShell.handle, margins) == 0) {
		//
		// return true;
		// }

		return false;
	}

	public static boolean osIusCLScreen_isThemed() {
		// return OS.IsAppThemed();
		return false;
	}

	public static void osIusCLFont_updateSwtFont(IusCLFontStyle style, FontData swtFontData) {
		if (style.getUnderline()) {
			swtFontData.data.lfUnderline = 1;
		}
		if (style.getStrikeOut()) {
			swtFontData.data.lfStrikeOut = 1;
		}
	}

	public static void osIusCLFont_setSwtFont(IusCLFontStyle style, FontData swtFontData) {
		if (swtFontData.data.lfUnderline == 1) {
			style.setUnderline(true);
		}
		if (swtFontData.data.lfStrikeOut == 1) {
			style.setStrikeOut(true);
		}
	}

	public static Color osIusCLColor_getOSSwtColor(IusCLStandardColors standardColor) {
		/* Windows */

		int win32ConstantColor = 0;

		switch (standardColor) {
		case cl3DDkShadow:
			win32ConstantColor = OS.COLOR_3DDKSHADOW;
			break;
		case cl3DLight:
			win32ConstantColor = OS.COLOR_3DLIGHT;
			break;
		case clActiveBorder:
			win32ConstantColor = COLOR_ACTIVEBORDER;
			break;
		case clActiveCaption:
			win32ConstantColor = OS.COLOR_ACTIVECAPTION;
			break;
		case clAppWorkSpace:
			win32ConstantColor = COLOR_APPWORKSPACE;
			break;
		case clBackground:
			win32ConstantColor = COLOR_BACKGROUND;
			break;
		case clBtnFace:
			win32ConstantColor = OS.COLOR_BTNFACE;
			break;
		case clBtnHighlight:
			win32ConstantColor = OS.COLOR_BTNHIGHLIGHT;
			break;
		case clBtnShadow:
			win32ConstantColor = OS.COLOR_BTNSHADOW;
			break;
		case clBtnText:
			win32ConstantColor = OS.COLOR_BTNTEXT;
			break;
		case clGrayText:
			win32ConstantColor = COLOR_GRAYTEXT;
			break;
		case clHighlight:
			win32ConstantColor = COLOR_HIGHLIGHT;
			break;
		case clHighlightText:
			win32ConstantColor = COLOR_HIGHLIGHTTEXT;
			break;
		case clHotLight:
			win32ConstantColor = COLOR_HOTLIGHT;
			break;
		case clInactiveBorder:
			win32ConstantColor = COLOR_INACTIVEBORDER;
			break;
		case clMenu:
			win32ConstantColor = OS.COLOR_MENU;
			break;
		case clMenuBar:
			win32ConstantColor = COLOR_MENUBAR;
			break;
		case clMenuHighlight:
			win32ConstantColor = COLOR_MENUHILIGHT;
			break;
		case clMenuText:
			win32ConstantColor = OS.COLOR_MENUTEXT;
			break;
		case clScrollBar:
			win32ConstantColor = OS.COLOR_SCROLLBAR;
			break;
		case clWindow:
			win32ConstantColor = OS.COLOR_WINDOW;
			break;
		case clWindowFrame:
			win32ConstantColor = OS.COLOR_WINDOWFRAME;
			break;
		case clWindowText:
			win32ConstantColor = OS.COLOR_WINDOWTEXT;
			break;
		default:
			/* Intentionally left blank */
			break;
		}

		int pixel = OS.GetSysColor(win32ConstantColor);
		return Color.win32_new(Display.getDefault(), pixel);
	}

	/* Linux */

	//
	// public static void osIusCLFont_updateSwtFont(IusCLFontStyle style, FontData
	// swtFontData) {
	// /* */
	// }
	//
	//
	// public static void osIusCLFont_setSwtFont(IusCLFontStyle style, FontData
	// swtFontData) {
	// /* */
	// }
	//
	//
	// public static Color osIusCLColor_getOSSwtColor(IusCLStandardColors
	// standardColor) {
	//
	// /* */
	// int pixel = 0x00000000;
	// pixel = OS.GetSysColor(win32ConstantColor);
	// return Color.win32_new(Display.getDefault(), pixel);
	// int gtk_vscrollbar_new_Handle = OS.gtk_vscrollbar_new(0);
	// if (gtk_vscrollbar_new_Handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	// byte[] gtk_tooltips = Converter.wcsToMbcs (null, "gtk-tooltips", true);
	// //$NON-NLS-1$
	// OS.gtk_widget_set_name (gtk_vscrollbar_new_Handle, gtk_tooltips);
	// OS.gtk_widget_realize (gtk_vscrollbar_new_Handle);
	//
	// int /*long*/ tooltipStyle = OS.gtk_widget_get_style
	// (gtk_vscrollbar_new_Handle);
	// GdkColor gdkColor = new GdkColor();
	// OS.gtk_style_get_fg (tooltipStyle, OS.GTK_STATE_NORMAL, gdkColor);
	//
	// OS.gtk_widget_destroy (gtk_vscrollbar_new_Handle);
	//
	// return Color.gtk_new(Display.getDefault(), gdkColor);
	// COLOR_INFO_FOREGROUND = gdkColor;
	//
	// int /*long*/ tooltipShellHandle = OS.gtk_window_new (OS.GTK_wi);
	// if (tooltipShellHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	// byte[] gtk_tooltips = Converter.wcsToMbcs (null, "gtk-tooltips", true);
	// //$NON-NLS-1$
	// OS.gtk_widget_set_name (tooltipShellHandle, gtk_tooltips);
	// OS.gtk_widget_realize (tooltipShellHandle);
	// int /*long*/ tooltipStyle = OS.gtk_widget_get_style (tooltipShellHandle);
	// gdkColor = new GdkColor();
	// OS.gtk_style_get_fg (tooltipStyle, OS.GTK_STATE_NORMAL, gdkColor);
	// COLOR_INFO_FOREGROUND = gdkColor;
	// gdkColor = new GdkColor();
	// OS.gtk_style_get_bg (tooltipStyle, OS.GTK_STATE_NORMAL, gdkColor);
	// COLOR_INFO_BACKGROUND = gdkColor;
	// OS.gtk_widget_destroy (tooltipShellHandle);
	//
	//
	// int /*long*/ tooltipStyle = OS.gtk_widget_get_style(tooltipShellHandle);
	//
	// GdkColor gdkColor = new GdkColor();
	//
	// //OS.gtk_style_get_fg (tooltipStyle, OS.GTK_STATE_NORMAL, gdkColor);
	//
	// OS.gtk_style_
	//
	// return Color.gtk_new(Display.getDefault(), gdkColor);
	//
	// return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	// int win32ConstantColor = 0;
	// }

	/* MacOS */

	//
	// public static void osIusCLFont_updateSwtFont(IusCLFontStyle style, FontData
	// swtFontData) {
	// /* */
	// }
	//
	//
	// public static void osIusCLFont_setSwtFont(IusCLFontStyle style, FontData
	// swtFontData) {
	// /* */
	// }
	//
	//
	// public static Color osIusCLColor_getOSSwtColor(IusCLStandardColors
	// standardColor) {
	//
	// /* */
	// return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	// }
}
