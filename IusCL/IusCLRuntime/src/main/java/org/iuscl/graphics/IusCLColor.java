/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.iuscl.classes.IusCLOS;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLColor {

	public enum IusCLStandardColors {

		clScrollBar, clBackground, clActiveCaption, clInactiveCaption, clMenu, clWindow, clWindowFrame, clMenuText, clWindowText, clCaptionText,
		clActiveBorder, clInactiveBorder, clAppWorkSpace, clHighlight, clHighlightText, clBtnFace, clBtnShadow, clGrayText, clBtnText,
		clInactiveCaptionText, clBtnHighlight, cl3DDkShadow, cl3DLight, clInfoText, clInfoBk, clHotLight, clGradientActiveCaption,
		clGradientInactiveCaption, clMenuHighlight, clMenuBar,

		clBlack, clMaroon, clGreen, clOlive, clNavy, clPurple, clTeal, clGray, clSilver, clRed, clLime, clYellow, clBlue, clFuchsia, clAqua, clLtGray,
		clDkGray, clWhite,

		clMoneyGreen, clSkyBlue, clCream, clMedGray
	}

	private static final int ALPHA_OPAQUE = 255;

	/* No idea - clSystemColor, clNone, clDefault */

	private static Map<Long, IusCLStandardColors> standardColorFromRGBValue = new HashMap<>();

	private static Map<IusCLStandardColors, Color> swtFromStandardColor = new EnumMap<>(IusCLStandardColors.class);

	static {
		/* 255 + Blue) => standard color */
		putRGBValue(SWT.COLOR_CYAN, IusCLStandardColors.clAqua);
		putRGBValue(SWT.COLOR_BLACK, IusCLStandardColors.clBlack);
		putRGBValue(SWT.COLOR_BLUE, IusCLStandardColors.clBlue);
		putRGBValue(SWT.COLOR_DARK_GRAY, IusCLStandardColors.clDkGray);
		putRGBValue(SWT.COLOR_MAGENTA, IusCLStandardColors.clFuchsia);
		putRGBValue(SWT.COLOR_DARK_GRAY, IusCLStandardColors.clGray);
		putRGBValue(SWT.COLOR_DARK_GREEN, IusCLStandardColors.clGreen);
		putRGBValue(SWT.COLOR_GREEN, IusCLStandardColors.clLime);
		putRGBValue(SWT.COLOR_DARK_RED, IusCLStandardColors.clMaroon);
		putRGBValue(SWT.COLOR_DARK_BLUE, IusCLStandardColors.clNavy);
		putRGBValue(SWT.COLOR_DARK_YELLOW, IusCLStandardColors.clOlive);
		putRGBValue(SWT.COLOR_DARK_MAGENTA, IusCLStandardColors.clPurple);
		putRGBValue(SWT.COLOR_RED, IusCLStandardColors.clRed);
		putRGBValue(SWT.COLOR_GRAY, IusCLStandardColors.clSilver);
		putRGBValue(SWT.COLOR_DARK_CYAN, IusCLStandardColors.clTeal);
		putRGBValue(SWT.COLOR_WHITE, IusCLStandardColors.clWhite);
		putRGBValue(SWT.COLOR_YELLOW, IusCLStandardColors.clYellow);

		/* Standard */
		putStandardColor(IusCLStandardColors.clAqua, getSwtColor(SWT.COLOR_CYAN));
		putStandardColor(IusCLStandardColors.clBlack, getSwtColor(SWT.COLOR_BLACK));
		putStandardColor(IusCLStandardColors.clBlue, getSwtColor(SWT.COLOR_BLUE));
		putStandardColor(IusCLStandardColors.clDkGray, getSwtColor(SWT.COLOR_DARK_GRAY));
		putStandardColor(IusCLStandardColors.clFuchsia, getSwtColor(SWT.COLOR_MAGENTA));
		putStandardColor(IusCLStandardColors.clGray, getSwtColor(SWT.COLOR_DARK_GRAY));
		putStandardColor(IusCLStandardColors.clGreen, getSwtColor(SWT.COLOR_DARK_GREEN));
		putStandardColor(IusCLStandardColors.clLime, getSwtColor(SWT.COLOR_GREEN));
		putStandardColor(IusCLStandardColors.clMaroon, getSwtColor(SWT.COLOR_DARK_RED));
		putStandardColor(IusCLStandardColors.clNavy, getSwtColor(SWT.COLOR_DARK_BLUE));
		putStandardColor(IusCLStandardColors.clOlive, getSwtColor(SWT.COLOR_DARK_YELLOW));
		putStandardColor(IusCLStandardColors.clPurple, getSwtColor(SWT.COLOR_DARK_MAGENTA));
		putStandardColor(IusCLStandardColors.clRed, getSwtColor(SWT.COLOR_RED));
		putStandardColor(IusCLStandardColors.clSilver, getSwtColor(SWT.COLOR_GRAY));
		putStandardColor(IusCLStandardColors.clTeal, getSwtColor(SWT.COLOR_DARK_CYAN));
		putStandardColor(IusCLStandardColors.clWhite, getSwtColor(SWT.COLOR_WHITE));
		putStandardColor(IusCLStandardColors.clYellow, getSwtColor(SWT.COLOR_YELLOW));

		/* Aux */
		putStandardColor(IusCLStandardColors.clCream, new Color(Display.getDefault(), 255, 251, 240));
		putStandardColor(IusCLStandardColors.clLtGray, new Color(Display.getDefault(), 192, 192, 192));
		putStandardColor(IusCLStandardColors.clMedGray, new Color(Display.getDefault(), 160, 160, 164));
		putStandardColor(IusCLStandardColors.clMoneyGreen, new Color(Display.getDefault(), 192, 220, 192));
		putStandardColor(IusCLStandardColors.clSkyBlue, new Color(Display.getDefault(), 166, 202, 240));

		/* System */
		putStandardColor(IusCLStandardColors.clCaptionText, getSwtColor(SWT.COLOR_TITLE_FOREGROUND));
		putStandardColor(IusCLStandardColors.clGradientActiveCaption, getSwtColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		putStandardColor(IusCLStandardColors.clGradientInactiveCaption, getSwtColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		putStandardColor(IusCLStandardColors.clInactiveCaption, getSwtColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		putStandardColor(IusCLStandardColors.clInactiveCaptionText, getSwtColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		putStandardColor(IusCLStandardColors.clInfoBk, getSwtColor(SWT.COLOR_INFO_BACKGROUND));
		putStandardColor(IusCLStandardColors.clInfoText, getSwtColor(SWT.COLOR_INFO_FOREGROUND));

		/* OS */
		putStandardOSColor(IusCLStandardColors.cl3DDkShadow);
		putStandardOSColor(IusCLStandardColors.cl3DLight);
		putStandardOSColor(IusCLStandardColors.cl3DLight);
		putStandardOSColor(IusCLStandardColors.clActiveBorder);
		putStandardOSColor(IusCLStandardColors.clActiveCaption);
		putStandardOSColor(IusCLStandardColors.clAppWorkSpace);
		putStandardOSColor(IusCLStandardColors.clBackground);
		putStandardOSColor(IusCLStandardColors.clBtnFace);
		putStandardOSColor(IusCLStandardColors.clBtnHighlight);
		putStandardOSColor(IusCLStandardColors.clBtnShadow);
		putStandardOSColor(IusCLStandardColors.clBtnText);
		putStandardOSColor(IusCLStandardColors.clGrayText);
		putStandardOSColor(IusCLStandardColors.clHighlight);
		putStandardOSColor(IusCLStandardColors.clHighlightText);
		putStandardOSColor(IusCLStandardColors.clHotLight);
		putStandardOSColor(IusCLStandardColors.clInactiveBorder);
		putStandardOSColor(IusCLStandardColors.clMenu);
		putStandardOSColor(IusCLStandardColors.clMenuBar);
		putStandardOSColor(IusCLStandardColors.clMenuHighlight);
		putStandardOSColor(IusCLStandardColors.clMenuText);
		putStandardOSColor(IusCLStandardColors.clScrollBar);
		putStandardOSColor(IusCLStandardColors.clWindow);
		putStandardOSColor(IusCLStandardColors.clWindowFrame);
		putStandardOSColor(IusCLStandardColors.clWindowText);

		/* Other - no idea */
		// putStandardColor(IusCLStandardColors.clSystemColor,
		// getSwtWin32Color(OS.COLOR_BTNFACE));
		// putStandardColor(IusCLStandardColors.clDefault, new
		// Color(Display.getDefault(), 0, 0, 0));
		// putStandardColor(IusCLStandardColors.clNone, new Color(Display.getDefault(),
		// 0, 0, 0));
	}

	private static Color getSwtColor(int swtConstantColor) {
		return Display.getDefault().getSystemColor(swtConstantColor);
	}

	private static void putStandardColor(IusCLStandardColors standardColor, Color swtColor) {
		swtFromStandardColor.put(standardColor, swtColor);
		// standardColorFromRGBValue.put(getAsRGBValue(swtColor.getRed(),
		// swtColor.getGreen(), swtColor.getBlue()), standardColor);
	}

	private static void putRGBValue(int swtConstantColorId, IusCLStandardColors standardColor) {
		Color swtColor = Display.getDefault().getSystemColor(swtConstantColorId);
		standardColorFromRGBValue.put(getAsRGBValue(swtColor.getRed(), swtColor.getGreen(), swtColor.getBlue()), standardColor);
	}

	private static void putStandardOSColor(IusCLStandardColors standardColor) {
		putStandardColor(standardColor, IusCLOS.osIusCLColor_getOSSwtColor(standardColor));
	}

	private static Long getAsRGBValue(Integer red, Integer green, Integer blue) {
		return Long.valueOf(red * 255 * 255 + green * 255l + blue);
	}

	public static IusCLColor getStandardColor(IusCLStandardColors standardColor) {
		return new IusCLColor(standardColor);
	}

	IusCLStandardColors cachedStandardColor = IusCLStandardColors.clBlack;

	@Getter
	int red = 0;
	@Getter
	int green = 0;
	@Getter
	int blue = 0;
	@Getter
	int alpha = ALPHA_OPAQUE;

	public IusCLColor() {
		/*  */
	}

	public IusCLColor(IusCLStandardColors standardColor) {
		loadFromStandardColor(standardColor);
	}

	public IusCLColor(int red, int green, int blue) {
		loadFromRedGreenBlueAlpha(red, green, blue, ALPHA_OPAQUE);
	}

	public IusCLColor(int red, int green, int blue, int alpha) {
		loadFromRedGreenBlueAlpha(red, green, blue, alpha);
	}

	public IusCLColor(String colorString) {
		loadFromString(colorString);
	}

	public void loadFromStandardColor(IusCLStandardColors standardColor) {
		Color swtColor = swtFromStandardColor.get(standardColor);
		this.red = swtColor.getRed();
		this.green = swtColor.getGreen();
		this.blue = swtColor.getBlue();
		this.alpha = ALPHA_OPAQUE;

		cachedStandardColor = standardColor;
	}

	public IusCLStandardColors getAsStandardColor() {
		if (cachedStandardColor != null) {
			return cachedStandardColor;
		}
		return standardColorFromRGBValue.get(getAsRGBValue(red, green, blue));
	}

	public void loadFromString(String colorString) {
		if (colorString.indexOf("(") == 0) {
			String parseRGB = colorString.replace("(", "").replace(")", "");
			String[] vals = parseRGB.split(",");

			Integer redValue = Integer.parseInt(vals[0].trim());
			Integer greenValue = Integer.parseInt(vals[1].trim());
			Integer blueValue = Integer.parseInt(vals[2].trim());

			loadFromRedGreenBlueAlpha(redValue, greenValue, blueValue, ALPHA_OPAQUE);
		} else {
			loadFromStandardColor(IusCLStandardColors.valueOf(colorString));
		}
	}

	public String getAsString() {
		IusCLStandardColors standardColor = getAsStandardColor();

		if (standardColor == null) {
			return "(" + red + ", " + green + ", " + blue + ")";
		}

		return standardColor.name();
	}

	public void loadFromSwtColor(Color swtColor) {
		loadFromRedGreenBlueAlpha(swtColor.getRed(), swtColor.getGreen(), swtColor.getBlue(), ALPHA_OPAQUE);
	}

	public Color getAsSwtColor() {
		Color swtColor = null;

		IusCLStandardColors standardColor = getAsStandardColor();

		if (standardColor != null) {
			swtColor = swtFromStandardColor.get(standardColor);
		} else {
			swtColor = new Color(Display.getDefault(), red, green, blue);
		}

		return swtColor;
	}

	public void loadFromColor(IusCLColor color) {
		cachedStandardColor = color.getAsStandardColor();

		red = color.getRed();
		green = color.getGreen();
		blue = color.getBlue();
		alpha = color.getAlpha();
	}

	public void loadFromRedGreenBlue(int red, int green, int blue) {
		loadFromRedGreenBlueAlpha(red, green, blue, ALPHA_OPAQUE);
	}

	public void loadFromRedGreenBlueAlpha(int red, int green, int blue, int alpha) {
		cachedStandardColor = null;

		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public void loadFromHTMLColor(String htmlColor) {
		loadFromHTMLColor(htmlColor, 100);
	}

	public void loadFromHTMLColor(String htmlColor, Integer alphaPercent) {
		if (htmlColor.length() == 6) {
			htmlColor = "#" + htmlColor;
		}

		if (htmlColor.length() == 7) {
			loadFromRedGreenBlueAlpha(Integer.decode("0x" + htmlColor.substring(1, 2)), Integer.decode("0x" + htmlColor.substring(3, 4)),
					Integer.decode("0x" + htmlColor.substring(5, 6)), (int) Math.floor((alphaPercent * 255d) / 100));
		}

		if (htmlColor.length() == 3) {
			loadFromRedGreenBlueAlpha(Integer.decode("0x0" + htmlColor.substring(1, 2)), Integer.decode("0x0" + htmlColor.substring(3, 4)),
					Integer.decode("0x0" + htmlColor.substring(5, 6)), (int) Math.floor((alphaPercent * 255d) / 100));
		}
	}

	public Long getAsValue() {
		return Long.valueOf(red * 255 * 255 + green * 255l + blue);
	}

	public String getAsHTMLColor() {
		String r = (red < 16) ? "0" + Integer.toHexString(red) : Integer.toHexString(red);
		String g = (green < 16) ? "0" + Integer.toHexString(green) : Integer.toHexString(green);
		String b = (blue < 16) ? "0" + Integer.toHexString(blue) : Integer.toHexString(blue);

		return "#" + r + g + b;
	}

	public boolean equalValue(IusCLColor anotherColor) {
		return this.getAsValue().equals(anotherColor.getAsValue());
	}

	public float getHue() {
		RGB swtRGB = new RGB(red, green, blue);
		float[] hsb = swtRGB.getHSB();

		return hsb[0];
	}

	public float getSaturation() {
		RGB swtRGB = new RGB(red, green, blue);
		float[] hsb = swtRGB.getHSB();

		return hsb[1];
	}

	public float getBrightness() {
		RGB swtRGB = new RGB(red, green, blue);
		float[] hsb = swtRGB.getHSB();

		return hsb[2];
	}

	public void loadFromHueSaturationBrightness(float hue, float saturation, float brightness) {
		RGB swtRGB = new RGB(hue, saturation, brightness);

		loadFromRedGreenBlue(swtRGB.red, swtRGB.green, swtRGB.blue);
	}

	public void setRed(Integer red) {
		cachedStandardColor = null;

		this.red = red;
	}

	public void setGreen(Integer green) {
		cachedStandardColor = null;

		this.green = green;
	}

	public void setBlue(Integer blue) {
		cachedStandardColor = null;

		this.blue = blue;
	}

	public void setAlpha(Integer alpha) {
		cachedStandardColor = null;

		this.alpha = alpha;
	}
}
