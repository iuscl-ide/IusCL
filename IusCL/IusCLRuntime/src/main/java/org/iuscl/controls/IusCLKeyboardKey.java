/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import java.util.HashMap;

import org.eclipse.swt.SWT;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLKeyboardKey {

	public enum IusCLStandardKeys {
		vkEsc, vkF1, vkF2, vkF3, vkF4, vkF5, vkF6, vkF7, vkF8, vkF9, vkF10, vkF11, vkF12, vkF13, vkF14, vkF15, vkF16, vkF17, vkF18, vkF19, vkTab,
		vkCapsLock, vkBackspace, vkEnter, vk1, vk2, vk3, vk4, vk5, vk6, vk7, vk8, vk9, vk0, vkA, vkB, vkC, vkD, vkE, vkF, vkG, vkH, vkI, vkJ, vkK,
		vkL, vkM, vkN, vkO, vkP, vkQ, vkR, vkS, vkT, vkU, vkV, vkW, vkX, vkY, vkZ, vkShift, vkLeftShift, vkRightShift, vkCtrl, vkLeftCtrl,
		vkRightCtrl, vkAlt, vkLeftAlt, vkRightAlt, vkCmd, vkLeftCmd, vkRightCmd, vkInsert, vkDel, vkHome, vkEnd, vkPageUp, vkPageDown, vkArrowLeft,
		vkArrowUp, vkArrowDown, vkArrowRigth, vkNumLock, vkNumPadEqual, vkNumPadEnter, vkNumPadDecimal, vkNumPadDivide, vkNumPadMultiply,
		vkNumPadSubstract, vkNumPadAdd, vkNumPad1, vkNumPad2, vkNumPad3, vkNumPad4, vkNumPad5, vkNumPad6, vkNumPad7, vkNumPad8, vkNumPad9, vkNumPad0,
	}

	public enum IusCLKeyPosition {
		kpLeft, kpRight, kpNumericPad
	}

	private static HashMap<Integer, IusCLStandardKeys> standardKeyFromSwtKey = new HashMap<>();

	static {
		standardKeyFromSwtKey.put((int) SWT.ESC, IusCLStandardKeys.vkEsc);

		standardKeyFromSwtKey.put(SWT.SHIFT, IusCLStandardKeys.vkShift);
		standardKeyFromSwtKey.put(SWT.CTRL, IusCLStandardKeys.vkCtrl);
		standardKeyFromSwtKey.put(SWT.ALT, IusCLStandardKeys.vkAlt);
		standardKeyFromSwtKey.put(SWT.COMMAND, IusCLStandardKeys.vkCmd);

		standardKeyFromSwtKey.put(SWT.F1, IusCLStandardKeys.vkF1);
		standardKeyFromSwtKey.put(SWT.F2, IusCLStandardKeys.vkF2);
		standardKeyFromSwtKey.put(SWT.F3, IusCLStandardKeys.vkF3);
		standardKeyFromSwtKey.put(SWT.F4, IusCLStandardKeys.vkF4);
		standardKeyFromSwtKey.put(SWT.F5, IusCLStandardKeys.vkF5);
		standardKeyFromSwtKey.put(SWT.F6, IusCLStandardKeys.vkF6);
		standardKeyFromSwtKey.put(SWT.F7, IusCLStandardKeys.vkF7);
		standardKeyFromSwtKey.put(SWT.F8, IusCLStandardKeys.vkF8);
		standardKeyFromSwtKey.put(SWT.F9, IusCLStandardKeys.vkF9);
		standardKeyFromSwtKey.put(SWT.F10, IusCLStandardKeys.vkF10);
		standardKeyFromSwtKey.put(SWT.F11, IusCLStandardKeys.vkF11);
		standardKeyFromSwtKey.put(SWT.F12, IusCLStandardKeys.vkF12);
		standardKeyFromSwtKey.put(SWT.F13, IusCLStandardKeys.vkF13);
		standardKeyFromSwtKey.put(SWT.F14, IusCLStandardKeys.vkF14);
		standardKeyFromSwtKey.put(SWT.F15, IusCLStandardKeys.vkF15);
		standardKeyFromSwtKey.put(SWT.F16, IusCLStandardKeys.vkF16);
		standardKeyFromSwtKey.put(SWT.F17, IusCLStandardKeys.vkF17);
		standardKeyFromSwtKey.put(SWT.F18, IusCLStandardKeys.vkF18);
		standardKeyFromSwtKey.put(SWT.F19, IusCLStandardKeys.vkF19);

		standardKeyFromSwtKey.put((int) 'a', IusCLStandardKeys.vkA);
		standardKeyFromSwtKey.put((int) 'b', IusCLStandardKeys.vkB);
		standardKeyFromSwtKey.put((int) 'c', IusCLStandardKeys.vkC);
		standardKeyFromSwtKey.put((int) 'd', IusCLStandardKeys.vkD);
		standardKeyFromSwtKey.put((int) 'e', IusCLStandardKeys.vkE);
		standardKeyFromSwtKey.put((int) 'f', IusCLStandardKeys.vkF);
		standardKeyFromSwtKey.put((int) 'g', IusCLStandardKeys.vkG);
		standardKeyFromSwtKey.put((int) 'h', IusCLStandardKeys.vkH);
		standardKeyFromSwtKey.put((int) 'i', IusCLStandardKeys.vkI);
		standardKeyFromSwtKey.put((int) 'j', IusCLStandardKeys.vkJ);
		standardKeyFromSwtKey.put((int) 'k', IusCLStandardKeys.vkK);
		standardKeyFromSwtKey.put((int) 'l', IusCLStandardKeys.vkL);
		standardKeyFromSwtKey.put((int) 'm', IusCLStandardKeys.vkM);
		standardKeyFromSwtKey.put((int) 'n', IusCLStandardKeys.vkN);
		standardKeyFromSwtKey.put((int) 'o', IusCLStandardKeys.vkO);
		standardKeyFromSwtKey.put((int) 'p', IusCLStandardKeys.vkP);
		standardKeyFromSwtKey.put((int) 'q', IusCLStandardKeys.vkQ);
		standardKeyFromSwtKey.put((int) 'r', IusCLStandardKeys.vkR);
		standardKeyFromSwtKey.put((int) 's', IusCLStandardKeys.vkS);
		standardKeyFromSwtKey.put((int) 't', IusCLStandardKeys.vkT);
		standardKeyFromSwtKey.put((int) 'u', IusCLStandardKeys.vkU);
		standardKeyFromSwtKey.put((int) 'v', IusCLStandardKeys.vkV);
		standardKeyFromSwtKey.put((int) 'w', IusCLStandardKeys.vkW);
		standardKeyFromSwtKey.put((int) 'x', IusCLStandardKeys.vkX);
		standardKeyFromSwtKey.put((int) 'y', IusCLStandardKeys.vkY);
		standardKeyFromSwtKey.put((int) 'z', IusCLStandardKeys.vkZ);

		standardKeyFromSwtKey.put((int) SWT.TAB, IusCLStandardKeys.vkTab);
		standardKeyFromSwtKey.put(SWT.CAPS_LOCK, IusCLStandardKeys.vkCapsLock);
		standardKeyFromSwtKey.put((int) SWT.CR, IusCLStandardKeys.vkEnter);
		standardKeyFromSwtKey.put((int) SWT.BS, IusCLStandardKeys.vkBackspace);

		standardKeyFromSwtKey.put(SWT.KEYPAD_1, IusCLStandardKeys.vkNumPad1);
		standardKeyFromSwtKey.put(SWT.KEYPAD_2, IusCLStandardKeys.vkNumPad2);
		standardKeyFromSwtKey.put(SWT.KEYPAD_3, IusCLStandardKeys.vkNumPad3);
		standardKeyFromSwtKey.put(SWT.KEYPAD_4, IusCLStandardKeys.vkNumPad4);
		standardKeyFromSwtKey.put(SWT.KEYPAD_5, IusCLStandardKeys.vkNumPad5);
		standardKeyFromSwtKey.put(SWT.KEYPAD_6, IusCLStandardKeys.vkNumPad6);
		standardKeyFromSwtKey.put(SWT.KEYPAD_7, IusCLStandardKeys.vkNumPad7);
		standardKeyFromSwtKey.put(SWT.KEYPAD_8, IusCLStandardKeys.vkNumPad8);
		standardKeyFromSwtKey.put(SWT.KEYPAD_9, IusCLStandardKeys.vkNumPad9);
		standardKeyFromSwtKey.put(SWT.KEYPAD_0, IusCLStandardKeys.vkNumPad0);

		standardKeyFromSwtKey.put(SWT.KEYPAD_EQUAL, IusCLStandardKeys.vkNumPadEqual);
		standardKeyFromSwtKey.put(SWT.KEYPAD_DIVIDE, IusCLStandardKeys.vkNumPadDivide);
		standardKeyFromSwtKey.put(SWT.KEYPAD_MULTIPLY, IusCLStandardKeys.vkNumPadMultiply);
		standardKeyFromSwtKey.put(SWT.KEYPAD_ADD, IusCLStandardKeys.vkNumPadAdd);
		standardKeyFromSwtKey.put(SWT.KEYPAD_SUBTRACT, IusCLStandardKeys.vkNumPadSubstract);
		standardKeyFromSwtKey.put(SWT.KEYPAD_CR, IusCLStandardKeys.vkNumPadEnter);
		standardKeyFromSwtKey.put(SWT.KEYPAD_DECIMAL, IusCLStandardKeys.vkNumPadDecimal);

		standardKeyFromSwtKey.put(SWT.CAPS_LOCK, IusCLStandardKeys.vkCapsLock);

		standardKeyFromSwtKey.put(SWT.ARROW_DOWN, IusCLStandardKeys.vkArrowDown);
		standardKeyFromSwtKey.put(SWT.ARROW_UP, IusCLStandardKeys.vkArrowUp);
		standardKeyFromSwtKey.put(SWT.ARROW_LEFT, IusCLStandardKeys.vkArrowLeft);
		standardKeyFromSwtKey.put(SWT.ARROW_RIGHT, IusCLStandardKeys.vkArrowRigth);

		standardKeyFromSwtKey.put(SWT.INSERT, IusCLStandardKeys.vkInsert);
		standardKeyFromSwtKey.put((int) SWT.DEL, IusCLStandardKeys.vkDel);
		standardKeyFromSwtKey.put(SWT.HOME, IusCLStandardKeys.vkHome);
		standardKeyFromSwtKey.put(SWT.END, IusCLStandardKeys.vkEnd);
		standardKeyFromSwtKey.put(SWT.PAGE_UP, IusCLStandardKeys.vkPageUp);
		standardKeyFromSwtKey.put(SWT.PAGE_DOWN, IusCLStandardKeys.vkPageDown);
	}

	@Getter
	Integer swtKeyCode = null;
	@Getter
	IusCLKeyPosition keyPosition = null;

	public IusCLKeyboardKey(Integer swtKeyCode, Integer swtKeyLocation) {
		this.swtKeyCode = swtKeyCode;
		switch (swtKeyLocation) {
		case SWT.LEFT:
			keyPosition = IusCLKeyPosition.kpLeft;
			break;
		case SWT.RIGHT:
			keyPosition = IusCLKeyPosition.kpRight;
			break;
		case SWT.KEYPAD:
			keyPosition = IusCLKeyPosition.kpNumericPad;
			break;
		default:
			break;
		}
	}

	public IusCLStandardKeys getStandardKey() {
		IusCLStandardKeys standardKey = null;
		standardKey = standardKeyFromSwtKey.get(swtKeyCode);
		if (standardKey == null) {
			return null;
		}
		if (keyPosition == IusCLKeyPosition.kpLeft) {
			switch (standardKey) {
			case vkShift:
				standardKey = IusCLStandardKeys.vkLeftShift;
				break;
			case vkCtrl:
				standardKey = IusCLStandardKeys.vkLeftCtrl;
				break;
			case vkAlt:
				standardKey = IusCLStandardKeys.vkLeftAlt;
				break;
			case vkCmd:
				standardKey = IusCLStandardKeys.vkLeftCmd;
				break;
			default:
				/* Intentionally left blank */
				break;
			}
		}
		if (keyPosition == IusCLKeyPosition.kpRight) {
			switch (standardKey) {
			case vkShift:
				standardKey = IusCLStandardKeys.vkRightShift;
				break;
			case vkCtrl:
				standardKey = IusCLStandardKeys.vkRightCtrl;
				break;
			case vkAlt:
				standardKey = IusCLStandardKeys.vkRightAlt;
				break;
			case vkCmd:
				standardKey = IusCLStandardKeys.vkRightCmd;
				break;
			default:
				/* Intentionally left blank */
				break;
			}
		}
		return standardKey;
	}
}
