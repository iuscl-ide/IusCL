/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iuscl.classes.IusCLStrings;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IusCLStrUtils {

	public String sLineBreak() {
		return System.getProperty("line.separator");
	}
	
	public String replaceAllExact(String inputString, String oldValue, String newValue) {
		return inputString.replaceAll("\\Q" + oldValue + "\\E", newValue);		
	}

	public String replaceFirstExact(String inputString, String oldValue, String newValue) {
		return inputString.replaceFirst("\\Q" + oldValue + "\\E", newValue);		
	}

	public String replaceAllWholeWord(String inputString, String oldValue, String newValue) {
		String outputString = inputString;
		final Map<String, String> replacements = new HashMap<>();
		String regex = "\\W" + oldValue + "\\W";
		Matcher nameMatcher = Pattern.compile(regex).matcher(outputString);
		while (nameMatcher.find()) {
			String match = nameMatcher.group();
			String replace = match.substring(0, 1) + newValue + match.substring(match.length() - 1, match.length());
			replacements.put(match, replace);
		}
		for (Iterator<String> iterator = replacements.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			outputString = outputString.replace(key, replacements.get(key));
		}
		return outputString;
	}

	public String subStringBetween(String originalString, String beginString, String endString) {
		return subStringBetween(originalString, beginString, endString, 0);
	}

	public String subStringBetween(String originalString, String beginString, String endString, int fromIndex) {
		String finalString = "";
		int beginIndex = 0;
		if (beginString != null) {
			beginIndex = originalString.indexOf(beginString, fromIndex);
			if (beginIndex == -1) {
				return "";
			}
			beginIndex = beginIndex + beginString.length();
		}
		int endIndex = originalString.length();
		if (endString != null) {
			endIndex = originalString.indexOf(endString, beginIndex);
			if (endIndex == -1) {
				endIndex = originalString.length() - 1;
			}
		}
		finalString = originalString.substring(beginIndex, endIndex);
		return finalString;
	}

	public boolean equalValues(String oneString, String anotherString) {
		boolean result = false;
		if (oneString == null) {
			if (anotherString == null) {
				result = true;
			}
		} else {
			if (oneString.equalsIgnoreCase(anotherString)) {
				result = true;
			}
		}
		return result;
	}

	public boolean isNotNullNotEmpty(String string) {
		boolean result = false;
		if (string != null && !string.trim().isEmpty()) {
			result = true;
		}
		return result;
	}

	public String findBiggestPrefix(String string, Vector<String> prefixes) {
		String result = null;
		for (String prefix : prefixes) {
			if (string.startsWith(prefix)) {
				if (result == null) {
					result = prefix;
				} else {
					if (prefix.length() > result.length()) {
						result = prefix;
					}
				}
			}
		}
		return result;
	}

	public String booleanToString(boolean trueOrFalse) {
		return trueOrFalse ? "true" : "false";
	}

	public void saveStringToFile(String string, String fileName) {
		IusCLStrings strings = new IusCLStrings();
		strings.setText(string);
		strings.saveToFile(fileName);
	}
}
