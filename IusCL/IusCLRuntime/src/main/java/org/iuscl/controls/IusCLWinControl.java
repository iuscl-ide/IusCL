/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Listener;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLKeyEvent;
import org.iuscl.events.IusCLKeyPressEvent;
import org.iuscl.events.IusCLNotifyEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLWinControl extends IusCLControl {

	public enum IusCLBorderStyle {
		bsNone, bsSingle
	}

	public enum IusCLKeyPosition {
		kpLeft, kpRight, kpNumericPad
	}

	Listener swtOnClickListener = null;
	FocusListener swtFocusLostListener = null;
	FocusListener swtFocusGainedListener = null;
	KeyListener swtKeyPressedListener = null;
	KeyListener swtKeyReleasedListener = null;

	@Getter
	Integer tabOrder = -1;
	@Getter
	boolean tabStop = true;

	/* Selection */
	@Getter
	IusCLNotifyEvent onClick = null;

	/* Focus */
	@Getter
	IusCLNotifyEvent onEnter = null;
	@Getter
	IusCLNotifyEvent onExit = null;

	/* Keyboard */
	@Getter
	IusCLKeyPressEvent onKeyPress = null;
	@Getter
	IusCLKeyEvent onKeyDown = null;
	@Getter
	IusCLKeyEvent onKeyUp = null;

	public IusCLWinControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("TabOrder", IusCLPropertyType.ptInteger, "-1");
		defineProperty("TabStop", IusCLPropertyType.ptBoolean, "true");

		/* Selection */
		defineProperty("OnClick", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		/* Focus */
		defineProperty("OnEnter", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnExit", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		/* Keyboard */
		defineProperty("OnKeyPress", IusCLPropertyType.ptEvent, null, IusCLKeyPressEvent.class);
		defineProperty("OnKeyDown", IusCLPropertyType.ptEvent, null, IusCLKeyEvent.class);
		defineProperty("OnKeyUp", IusCLPropertyType.ptEvent, null, IusCLKeyEvent.class);
	}

	@Override
	protected void create() {
		super.create();

		/* selection event */
		swtOnClickListener = swtEvent -> {
			if (IusCLEvent.isDefinedEvent(onClick)) {
				onClick.invoke(IusCLWinControl.this);
			}
		};

		swtFocusLostListener = FocusListener.focusLostAdapter(swtFocusEvent -> {
			if (IusCLEvent.isDefinedEvent(onExit)) {
				onExit.invoke(IusCLWinControl.this);
			}
		});
		swtFocusGainedListener = FocusListener.focusGainedAdapter(swtFocusEvent -> {
			if (IusCLEvent.isDefinedEvent(onEnter)) {
				onEnter.invoke(IusCLWinControl.this);
			}
		});

		swtKeyPressedListener = KeyListener.keyPressedAdapter(swtKeyEvent -> {
			if (IusCLEvent.isDefinedEvent(onKeyDown)) {
				IusCLKeyboardKey key = new IusCLKeyboardKey(swtKeyEvent.keyCode, swtKeyEvent.keyLocation);
				onKeyDown.invoke(IusCLWinControl.this, key, findShiftStateFromSwtKeyEvent(swtKeyEvent));
			}
			if (IusCLEvent.isDefinedEvent(onKeyPress)) {
				char ch = swtKeyEvent.character;
				if (ch > 0) {
					onKeyPress.invoke(IusCLWinControl.this, ch);
				}
			}
		});
		swtKeyReleasedListener = KeyListener.keyReleasedAdapter(swtKeyEvent -> {
			if (IusCLEvent.isDefinedEvent(onKeyUp)) {
				IusCLKeyboardKey key = new IusCLKeyboardKey(swtKeyEvent.keyCode, swtKeyEvent.keyLocation);
				onKeyUp.invoke(IusCLWinControl.this, key, findShiftStateFromSwtKeyEvent(swtKeyEvent));
			}
		});
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		/* default */
		this.getSwtControl().addFocusListener(
				FocusListener.focusGainedAdapter(swtFocusEvent -> IusCLWinControl.this.findForm().setActiveControlValue(IusCLWinControl.this)));
	}

	public boolean getCanFocus() {
		return (this.getSwtControl().getStyle() & SWT.NO_FOCUS) == 0;
	}

	public boolean getHasFocus() {
		return this.getSwtControl().isFocusControl();
	}

	public void setFocus() {
		this.getSwtControl().setFocus();
	}

	public void setTabOrder(Integer tabOrder) {
		if (this.getIsLoading()) {
			this.tabOrder = tabOrder;
			return;
		}

		IusCLParentControl parentControl = this.getParent();

		if (parentControl == null) {
			this.tabOrder = -1;
			return;
		}

		if (!(parentControl instanceof IusCLContainerControl)) {
			this.tabOrder = -1;
			return;
		}

		IusCLContainerControl parent = (IusCLContainerControl) parentControl;
		List<IusCLWinControl> winControls = parent.getWinControls();

		int lastTabOrder = winControls.size() - 1;

		if ((tabOrder < 0) || (tabOrder > lastTabOrder)) {
			// if (tabOrder == -1) {
			tabOrder = lastTabOrder;
		} else {
			for (int index = 0; index < winControls.size(); index++) {
				int indexTabOrder = winControls.get(index).tabOrder;
				if ((this.tabOrder < indexTabOrder) && (indexTabOrder <= tabOrder)) {
					winControls.get(index).tabOrder = indexTabOrder - 1;
				}
				if ((tabOrder <= indexTabOrder) && (indexTabOrder < this.tabOrder)) {
					winControls.get(index).tabOrder = indexTabOrder + 1;
				}
			}
		}
		this.tabOrder = tabOrder;

		parent.doUpdateTabOrder();
	}

	protected void removeFromParentTabOrder() {
		IusCLContainerControl parent = (IusCLContainerControl) this.getParent();
		List<IusCLWinControl> winControls = parent.getWinControls();

		for (int index = 0; index < winControls.size(); index++) {
			int indexTabOrder = winControls.get(index).tabOrder;
			if (indexTabOrder > tabOrder) {
				winControls.get(index).tabOrder = indexTabOrder - 1;
			}
		}

		parent.doUpdateTabOrder();
	}

	public void setTabStop(boolean tabStop) {
		this.tabStop = tabStop;

		if (this.getIsLoading()) {
			return;
		}
		IusCLParentControl parentControl = this.getParent();
		if (parentControl == null) {
			return;
		}
		if (!(parentControl instanceof IusCLContainerControl)) {
			return;
		}
		IusCLContainerControl parent = (IusCLContainerControl) parentControl;
		parent.doUpdateTabOrder();
	}

	public void setOnClick(IusCLNotifyEvent onClick) {
		this.onClick = onClick;
		if (!IusCLEvent.isDefinedEvent(onClick)) {
			IusCLWinControl.this.getSwtControl().removeListener(SWT.Selection, swtOnClickListener);
		} else {
			IusCLWinControl.this.getSwtControl().addListener(SWT.Selection, swtOnClickListener);
		}
	}

	public void setOnEnter(IusCLNotifyEvent onEnter) {
		this.onEnter = onEnter;
		if (!IusCLEvent.isDefinedEvent(onEnter)) {
			IusCLWinControl.this.getSwtControl().removeFocusListener(swtFocusGainedListener);
		} else {
			IusCLWinControl.this.getSwtControl().addFocusListener(swtFocusGainedListener);
		}
	}

	public void setOnExit(IusCLNotifyEvent onExit) {
		this.onExit = onExit;
		if (!IusCLEvent.isDefinedEvent(onExit)) {
			IusCLWinControl.this.getSwtControl().removeFocusListener(swtFocusLostListener);
		} else {
			IusCLWinControl.this.getSwtControl().addFocusListener(swtFocusLostListener);
		}
	}

	public void setOnKeyPress(IusCLKeyPressEvent onKeyPress) {
		this.onKeyPress = onKeyPress;
		if (!IusCLEvent.isDefinedEvent(onKeyPress)) {
			IusCLWinControl.this.getSwtControl().removeKeyListener(swtKeyPressedListener);
		} else {
			IusCLWinControl.this.getSwtControl().addKeyListener(swtKeyPressedListener);
		}
	}

	public void setOnKeyDown(IusCLKeyEvent onKeyDown) {
		this.onKeyDown = onKeyDown;
		if (!IusCLEvent.isDefinedEvent(onKeyDown)) {
			IusCLWinControl.this.getSwtControl().removeKeyListener(swtKeyPressedListener);
		} else {
			IusCLWinControl.this.getSwtControl().addKeyListener(swtKeyPressedListener);
		}
	}

	public void setOnKeyUp(IusCLKeyEvent onKeyUp) {
		this.onKeyUp = onKeyUp;
		if (!IusCLEvent.isDefinedEvent(onKeyUp)) {
			IusCLWinControl.this.getSwtControl().removeKeyListener(swtKeyPressedListener);
		} else {
			IusCLWinControl.this.getSwtControl().addKeyListener(swtKeyReleasedListener);
		}
	}

	protected EnumSet<IusCLShiftState> findShiftStateFromSwtKeyEvent(KeyEvent swtKeyEvent) {
		return findShiftStateFromSwtStateMask(swtKeyEvent.stateMask);
	}
}
