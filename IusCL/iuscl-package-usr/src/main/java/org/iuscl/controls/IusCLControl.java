/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.events.IusCLCanResizeEvent;
import org.iuscl.events.IusCLConstrainedResizeEvent;
import org.iuscl.events.IusCLContextPopupEvent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLMouseEvent;
import org.iuscl.events.IusCLMouseHoverEvent;
import org.iuscl.events.IusCLMouseMoveEvent;
import org.iuscl.events.IusCLMouseWheelEvent;
import org.iuscl.events.IusCLMouseWheelUpDownEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.forms.IusCLCursor;
import org.iuscl.forms.IusCLCursor.IusCLPredefinedCursors;
import org.iuscl.forms.IusCLForm;
import org.iuscl.forms.IusCLScreen;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.menus.IusCLMenuItem;
import org.iuscl.menus.IusCLPopupMenu;
import org.iuscl.sysutils.IusCLStrUtils;
import org.iuscl.types.IusCLPoint;
import org.iuscl.types.IusCLRectangle;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLControl extends IusCLComponent {

	public enum IusCLAlign {
		alNone, alTop, alBottom, alLeft, alRight, alClient, alCustom
	}

	public enum IusCLMouseButton {
		mbLeft, mbRight, mbMiddle
	}

	public enum IusCLShiftState {
		ssShift, ssAlt, ssAltGr, ssCtrl, ssCmd, ssMouseLeft, ssMouseRight, ssMouseMiddle, ssMouseDouble
	}

	private static final String ZERO = "0";
	private static final String ONE_HUNDRED = "100";
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	@Getter
	IusCLParentControl parent = null;
	@Getter
	Control swtControl = null;

	@Getter
	protected Integer top = 0;
	@Getter
	protected Integer left = 0;
	@Getter
	protected Integer width = 100;
	@Getter
	protected Integer height = 100;

	IusCLPoint originalPos = new IusCLPoint(0, 0);
	IusCLSize compensatePos = new IusCLSize(0, 0);
	IusCLSize compensateSize = new IusCLSize(0, 0);

	@Getter
	@Setter
	protected String caption = "";
	@Setter
	protected String text = "";

	@Getter
	protected boolean enabled = true;
	@Getter
	boolean visible = true;

	@Getter
	boolean autoSize = false;
	@Getter
	@Setter
	IusCLAnchors anchors = new IusCLAnchors();
	@Getter
	IusCLAlign align = IusCLAlign.alNone;
	@Getter
	@Setter
	IusCLSizeConstraints constraints = new IusCLSizeConstraints();

	@Getter
	String cursor = "crDefault";

	@Getter
	IusCLFont font = new IusCLFont();
	@Getter
	boolean parentFont = true;

	@Getter
	String hint = "";
	@Getter
	boolean parentShowHint = true;
	@Getter
	boolean showHint = false;

	@Getter
	IusCLColor color = IusCLColor.getStandardColor(IusCLStandardColors.clBtnFace);
	@Getter
	boolean parentColor = true;

	/* Resize */
	@Getter
	@Setter
	IusCLCanResizeEvent onCanResize = null;
	@Getter
	@Setter
	IusCLConstrainedResizeEvent onConstrainedResize = null;
	@Getter
	@Setter
	IusCLNotifyEvent onResize = null;

	/* Selection */
	MenuDetectListener swtMenuDetectListener = null;

	@Getter
	IusCLPopupMenu popupMenu = null;
	@Getter
	IusCLContextPopupEvent onContextPopup = null;

	/* Mouse */
	MouseListener swtMouseDownListener = null;
	MouseListener swtMouseUpListener = null;
	MouseListener swtMouseDoubleClickListener = null;

	MouseTrackListener swtMouseTrackEnterListener = null;
	MouseTrackListener swtMouseTrackExitListener = null;
	MouseTrackListener swtMouseTrackHoverListener = null;

	MouseMoveListener swtMouseMoveListener = null;

	MouseWheelListener swtMouseWheelListener = null;

	@Getter
	IusCLMouseEvent onMouseDown = null;
	@Getter
	IusCLMouseWheelUpDownEvent onMouseWheelDown = null;
	@Getter
	IusCLMouseEvent onMouseUp = null;
	@Getter
	IusCLMouseWheelUpDownEvent onMouseWheelUp = null;

	@Getter
	IusCLNotifyEvent onDoubleClick = null;

	@Getter
	IusCLNotifyEvent onMouseEnter = null;
	@Getter
	IusCLNotifyEvent onMouseExit = null;

	@Getter
	IusCLMouseMoveEvent onMouseMove = null;
	@Getter
	IusCLMouseHoverEvent onMouseHover = null;

	@Getter
	IusCLMouseWheelEvent onMouseWheel = null;

	public IusCLControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Top", IusCLPropertyType.ptInteger, ZERO);
		defineProperty("Left", IusCLPropertyType.ptInteger, ZERO);
		defineProperty("Width", IusCLPropertyType.ptInteger, ONE_HUNDRED);
		defineProperty("Height", IusCLPropertyType.ptInteger, ONE_HUNDRED);
		defineProperty("Caption", IusCLPropertyType.ptString, "defaultCaption");

		defineProperty("Anchors.Left", IusCLPropertyType.ptBoolean, TRUE);
		defineProperty("Anchors.Top", IusCLPropertyType.ptBoolean, TRUE);
		defineProperty("Anchors.Right", IusCLPropertyType.ptBoolean, FALSE);
		defineProperty("Anchors.Bottom", IusCLPropertyType.ptBoolean, FALSE);

		defineProperty("ParentShowHint", IusCLPropertyType.ptBoolean, TRUE);
		defineProperty("ShowHint", IusCLPropertyType.ptBoolean, FALSE);
		defineProperty("Hint", IusCLPropertyType.ptString, "");

		defineProperty("Enabled", IusCLPropertyType.ptBoolean, TRUE);
		defineProperty("Visible", IusCLPropertyType.ptBoolean, TRUE);

		defineProperty("Align", IusCLPropertyType.ptEnum, "alNone", IusCLAlign.alNone);

		defineProperty("Constraints.MaxHeight", IusCLPropertyType.ptInteger, ZERO);
		defineProperty("Constraints.MaxWidth", IusCLPropertyType.ptInteger, ZERO);
		defineProperty("Constraints.MinHeight", IusCLPropertyType.ptInteger, ZERO);
		defineProperty("Constraints.MinWidth", IusCLPropertyType.ptInteger, ZERO);

		defineProperty("Cursor", IusCLPropertyType.ptCursor, "crDefault", IusCLPredefinedCursors.crDefault);

		defineProperty("Color", IusCLPropertyType.ptColor, "clBtnFace", IusCLStandardColors.clBtnFace);
		defineProperty("ParentColor", IusCLPropertyType.ptBoolean, TRUE);

		defineProperty("PopupMenu", IusCLPropertyType.ptComponent, "", IusCLPopupMenu.class);

		/* Font */
		// IusCLFont defaultFont = new IusCLFont();
		font.setNotify(this, "setFont");
		defineProperty("Font", IusCLPropertyType.ptFont, "(IusCLFont)");
		IusCLFont.defineFontProperties(this, "Font", font);
		defineProperty("ParentFont", IusCLPropertyType.ptBoolean, TRUE);

		/* Resize */
		defineProperty("OnCanResize", IusCLPropertyType.ptEvent, null, IusCLCanResizeEvent.class);
		defineProperty("OnConstrainedResize", IusCLPropertyType.ptEvent, null, IusCLConstrainedResizeEvent.class);
		defineProperty("OnResize", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		/* Selection */
		defineProperty("OnContextPopup", IusCLPropertyType.ptEvent, null, IusCLContextPopupEvent.class);

		/* Mouse */
		defineProperty("OnMouseDown", IusCLPropertyType.ptEvent, null, IusCLMouseEvent.class);
		defineProperty("OnMouseWheelDown", IusCLPropertyType.ptEvent, null, IusCLMouseWheelUpDownEvent.class);
		defineProperty("OnMouseUp", IusCLPropertyType.ptEvent, null, IusCLMouseEvent.class);
		defineProperty("OnMouseWheelUp", IusCLPropertyType.ptEvent, null, IusCLMouseWheelUpDownEvent.class);

		defineProperty("OnDoubleClick", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		defineProperty("OnMouseEnter", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnMouseExit", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		defineProperty("OnMouseMove", IusCLPropertyType.ptEvent, null, IusCLMouseMoveEvent.class);
		defineProperty("OnMouseHover", IusCLPropertyType.ptEvent, null, IusCLMouseHoverEvent.class);

		defineProperty("OnMouseWheel", IusCLPropertyType.ptEvent, null, IusCLMouseWheelEvent.class);

		/* TODO Drag'n'drop */
	}

	@Override
	public void free() {
		if (this != null && this.getParent() != null) {
			this.getParent().getControls().remove(this);
		}

		super.free();
	}

	@Override
	public void destroy() {
		if (swtControl != null) {
			// if (storedColor != null) {
			// if (storedColor.getColorConstant() == null) {
			// swtControl.getBackground().dispose();
			// }
			// }
			swtControl.dispose();
		}

		super.destroy();
	}

	public void setSwtControl(Control swtControl) {
		this.swtControl = swtControl;
		this.swtControl.setData(IusCLControl.this);
		reCreate();
	}

	protected void reCreate() {
		/* Listeners applied to swtControl always */
	}

	public void repaint() {
		if (swtControl != null) {
			swtControl.redraw();
		}
	}

	public void setTop(Integer top) {
		updateBounds(this.left, top, this.width, this.height);
		doParentAlignControls();
	}

	public void setLeft(Integer left) {
		updateBounds(left, this.top, this.width, this.height);
		doParentAlignControls();
	}

	public void setWidth(Integer width) {
		updateBounds(this.left, this.top, width, this.height);
		doParentAlignControls();
	}

	public void setHeight(Integer height) {
		updateBounds(this.left, this.top, this.width, height);
		doParentAlignControls();
	}

	public void setBounds(Integer aLeft, Integer aTop, Integer aWidth, Integer aHeight) {
		updateBounds(aLeft, aTop, aWidth, aHeight);
		doParentAlignControls();
	}

	protected void updateBounds(Integer aLeft, Integer aTop, Integer aWidth, Integer aHeight) {
		// if (this.left.equals(aLeft) && this.top.equals(aTop) &&
		// this.width.equals(aWidth) && this.height.equals(aHeight)) {
		//
		// return;
		// }

		IusCLSize parentDelta = new IusCLSize(aWidth - width, aHeight - height);

		this.left = aLeft;
		this.top = aTop;
		this.width = aWidth;
		this.height = aHeight;

		int containerClientLeft = 0;
		int containerClientTop = 0;

		if (this.getParent() instanceof IusCLContainerControl containerControl) {
			containerClientLeft = containerControl.getContainerClientLeft();
			containerClientTop = containerControl.getContainerClientTop();
		}

		if (swtControl != null) {
			swtControl.setLocation(containerClientLeft + this.left, containerClientTop + this.top);
			swtControl.setSize(this.width, this.height);

			if (this instanceof IusCLContainerControl containerControl) {
				containerControl.doAlignControls(parentDelta);
			}
		}

		originalPos.setX(left);
		originalPos.setY(top);
		compensatePos.setWidthHeight(0, 0);
		compensateSize.setWidthHeight(0, 0);
	}

	public void bringToFront() {
		bringToFront(null);
	}

	public void bringToFront(IusCLControl onFrontOfControl) {
		int controlIndexInParent = parent.getControls().indexOf(this);

		if (onFrontOfControl == null) {
			parent.getControls().remove(controlIndexInParent);
			parent.getControls().add(this);
			swtControl.moveAbove(null);
			doParentAlignControls();
		} else {
			parent.getControls().remove(controlIndexInParent);

			int onFrontOfControlIndexInParent = parent.getControls().indexOf(onFrontOfControl);

			parent.getControls().add(onFrontOfControlIndexInParent + 1, this);

			swtControl.moveAbove(onFrontOfControl.getSwtControl());
			doParentAlignControls();
		}
	}

	public void sendToBack() {
		sendToBack(null);
	}

	public void sendToBack(IusCLControl onBackOfControl) {
		int controlIndexInParent = parent.getControls().indexOf(this);

		if (onBackOfControl == null) {
			parent.getControls().remove(controlIndexInParent);
			parent.getControls().add(0, this);
			swtControl.moveBelow(null);
			doParentAlignControls();
		} else {
			parent.getControls().remove(controlIndexInParent);

			int onBackOfControlIndexInParent = parent.getControls().indexOf(onBackOfControl);

			parent.getControls().add(onBackOfControlIndexInParent, this);

			swtControl.moveBelow(onBackOfControl.getSwtControl());
			doParentAlignControls();
		}
	}

	public void doParentAlignControls() {
		if (this.getParent() instanceof IusCLContainerControl containerControl) {
			containerControl.doAlignControls(new IusCLSize(0, 0));
		}
	}

	private Integer doConstraint(Integer size, Integer min, Integer max) {
		if (size <= min) {
			return min;
		} else {
			if ((max > 0) && (size > max)) {
				return max;
			} else {
				return size;
			}
		}
	}

	private Integer doCompensate(Integer size, Integer min, Integer max) {
		if (size <= min) {
			return size - min;
		} else {
			if ((max > 0) && (size > max)) {
				return size - max;
			} else {
				return 0;
			}
		}
	}

	public IusCLRectangle doPositionAndSize(IusCLSize parentDelta, IusCLRectangle availableClientRect) {
		Integer newLeft = left;
		Integer newTop = top;
		Integer newWidth = width;
		Integer newHeight = height;

		/* AutoSize */
		if (autoSize) {
			swtControl.pack();
			newWidth = swtControl.getSize().x;
			newHeight = swtControl.getSize().y;
		}

		/* OnCanResize event */
		IusCLSize newSize = new IusCLSize(newWidth, newHeight);

		boolean canResize = true;
		if (IusCLEvent.isDefinedEvent(onCanResize) && (!formIsInDesignMode)) {
			canResize = onCanResize.invoke(IusCLControl.this, newSize);
		}
		if (!canResize) {
			return availableClientRect;
		}
		newWidth = newSize.getWidth();
		newHeight = newSize.getHeight();

		/* OnConstrainedResize event */
		IusCLSizeConstraints newSizeConstraints = new IusCLSizeConstraints();
		newSizeConstraints.setMaxHeight(constraints.getMaxHeight());
		newSizeConstraints.setMaxWidth(constraints.getMaxWidth());
		newSizeConstraints.setMinHeight(constraints.getMinHeight());
		newSizeConstraints.setMinWidth(constraints.getMinWidth());

		if (IusCLEvent.isDefinedEvent(onConstrainedResize) && (!formIsInDesignMode)) {
			onConstrainedResize.invoke(IusCLControl.this, newSizeConstraints);
		}

		Integer minWidth = newSizeConstraints.getMinWidth();
		Integer maxWidth = newSizeConstraints.getMaxWidth();
		Integer minHeight = newSizeConstraints.getMinHeight();
		Integer maxHeight = newSizeConstraints.getMaxHeight();

		Integer newAvailableClientLeft = availableClientRect.getLeft();
		Integer newAvailableClientTop = availableClientRect.getTop();
		Integer newAvailableClientWidth = availableClientRect.getWidth();
		Integer newAvailableClientHeight = availableClientRect.getHeight();

		/* Compensations */
		Integer originalLeft = originalPos.getX();
		Integer originalTop = originalPos.getY();

		Integer newCompensateLeft = compensatePos.getWidth();
		Integer newCompensateTop = compensatePos.getHeight();

		Integer newCompensateWidth = compensateSize.getWidth();
		Integer newCompensateHeight = compensateSize.getHeight();

		boolean anchorLeft = anchors.getLeft();
		boolean anchorTop = anchors.getTop();
		boolean anchorRight = anchors.getRight();
		boolean anchorBottom = anchors.getBottom();

		Integer parentDeltaWidth = parentDelta.getWidth();
		Integer parentDeltaHeight = parentDelta.getHeight();

		IusCLContainerControl parentContainer = (IusCLContainerControl) this.getParent();

		Integer containerClientWidth = parentContainer.getContainerClientWidth();
		Integer containerClientHeight = parentContainer.getContainerClientHeight();

		switch (align) {
		case alNone:
			/* Horizontal */
			if (!anchorLeft) {
				if (!anchorRight) {
					newCompensateLeft = newCompensateLeft + parentDeltaWidth;

					int originalParentWidth = containerClientWidth - newCompensateLeft;

					newLeft = originalLeft + (width / 2);
					newLeft = (int) ((newLeft * containerClientWidth) / originalParentWidth);
					newLeft = newLeft - (width / 2);
				} else {
					newLeft = left + parentDeltaWidth;
				}
			} else {
				if (anchorRight) {
					newWidth = width + newCompensateWidth + parentDeltaWidth;
					newCompensateWidth = doCompensate(newWidth, minWidth, maxWidth);
					newWidth = doConstraint(newWidth, minWidth, maxWidth);
				}
			}

			/* Vertical */
			if (!anchorTop) {
				if (!anchorBottom) {
					newCompensateTop = newCompensateTop + parentDeltaHeight;

					int originalParentHeight = containerClientHeight - newCompensateTop;

					newTop = originalTop + (height / 2);
					newTop = (int) ((newTop * containerClientHeight) / originalParentHeight);
					newTop = newTop - (height / 2);
				} else {
					newTop = top + parentDeltaHeight;
				}
			} else {
				if (anchorBottom) {
					newHeight = height + newCompensateHeight + parentDeltaHeight;
					newCompensateHeight = doCompensate(newHeight, minHeight, maxHeight);
					newHeight = doConstraint(newHeight, minHeight, maxHeight);
				}
			}

			break;
		case alLeft:

			newLeft = newAvailableClientLeft;
			newTop = newAvailableClientTop;
			newHeight = newAvailableClientHeight;

			if (anchorRight) {
				newWidth = width + newCompensateWidth + parentDeltaWidth;
				newCompensateWidth = doCompensate(newWidth, minWidth, maxWidth);
			}
			newWidth = doConstraint(newWidth, minWidth, maxWidth);

			newAvailableClientLeft = newAvailableClientLeft + newWidth;
			newAvailableClientWidth = newAvailableClientWidth - newWidth;

			break;
		case alRight:

			if (anchorLeft) {
				newWidth = width + newCompensateWidth + parentDeltaWidth;
				newCompensateWidth = doCompensate(newWidth, minWidth, maxWidth);
			}
			newWidth = doConstraint(newWidth, minWidth, maxWidth);

			newLeft = newAvailableClientLeft + newAvailableClientWidth - newWidth;
			newTop = newAvailableClientTop;
			newHeight = newAvailableClientHeight;

			newAvailableClientWidth = newAvailableClientWidth - newWidth;

			break;
		case alTop:

			newTop = newAvailableClientTop;
			newLeft = newAvailableClientLeft;
			newWidth = newAvailableClientWidth;

			if (anchorBottom) {
				newHeight = height + newCompensateHeight + parentDeltaHeight;
				newCompensateHeight = doCompensate(newHeight, minHeight, maxHeight);
			}
			newHeight = doConstraint(newHeight, minHeight, maxHeight);

			newAvailableClientTop = newAvailableClientTop + newHeight;
			newAvailableClientHeight = newAvailableClientHeight - newHeight;

			break;
		case alBottom:

			if (anchorTop) {
				newHeight = height + newCompensateHeight + parentDeltaHeight;
				newCompensateHeight = doCompensate(newHeight, minHeight, maxHeight);
			}
			newHeight = doConstraint(newHeight, minHeight, maxHeight);

			newTop = newAvailableClientTop + newAvailableClientHeight - newHeight;
			newLeft = newAvailableClientLeft;
			newWidth = newAvailableClientWidth;

			newAvailableClientHeight = newAvailableClientHeight - newHeight;

			break;
		case alClient:

			newLeft = newAvailableClientLeft;
			newTop = newAvailableClientTop;
			newWidth = newAvailableClientWidth;
			newHeight = newAvailableClientHeight;

			break;
		case alCustom:
			/* ? */
			break;
		default:
			break;
		}

		updateBounds(newLeft, newTop, newWidth, newHeight);

		/* OnResize event */
		if (IusCLEvent.isDefinedEvent(onResize) && (!formIsInDesignMode)) {
			onResize.invoke(this);
		}

		originalPos.setXY(originalLeft, originalTop);
		compensatePos.setWidthHeight(newCompensateLeft, newCompensateTop);
		compensateSize.setWidthHeight(newCompensateWidth, newCompensateHeight);
		return new IusCLRectangle(newAvailableClientLeft, newAvailableClientTop, newAvailableClientWidth, newAvailableClientHeight);
	}

	public String getText() {
		if (IusCLStrUtils.isNotNullNotEmpty(text)) {
			return text;
		}
		return getName();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (swtControl != null) {
			swtControl.setEnabled(enabled);
		}
	}

	public void setOnContextPopup(IusCLContextPopupEvent onContextPopup) {
		this.onContextPopup = onContextPopup;

		swtControl.removeMenuDetectListener(swtMenuDetectListener);
		if (this.onContextPopup != null) {
			swtControl.addMenuDetectListener(swtMenuDetectListener);
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;

		if (swtControl != null) {
			swtControl.setVisible(visible);
		}
	}

	public void setParent(IusCLParentControl parentControl) {
		boolean hadParent = false;

		if (parent != null) {
			parent.getControls().remove(this);
			doParentAlignControls();

			hadParent = true;
			if (this instanceof IusCLWinControl winControl) {
				winControl.removeFromParentTabOrder();
			}
		}

		parent = parentControl;

		if (parent != null) {
			parent.getControls().add(this);

			if (parent instanceof IusCLContainerControl containerControl) {
				Composite swtParentComposite = containerControl.getSwtComposite();

				swtControl.setParent(swtParentComposite);
				swtControl.moveAbove(null);
			}
			doParentAlignControls();

			if (hadParent && this instanceof IusCLWinControl winControl) {
				/* Last */
				winControl.setTabOrder(-1);
			}
		}

		this.setParentColor(parentColor);
		this.setParentShowHint(parentShowHint);
		this.setParentFont(parentFont);
	}

	public void makeEmbedded(IusCLControl embeddingControl) {
		setIsEmbedded(true);
		swtControl.setData(embeddingControl);
	}

	public void transferSwtListeners(final IusCLControl transferControl) {
		transferSwtListeners(swtControl, transferControl.getSwtControl());
	}

	public void setOnMouseDown(IusCLMouseEvent onMouseDown) {
		this.onMouseDown = onMouseDown;
		if (!IusCLEvent.isDefinedEvent(onMouseDown)) {
			swtControl.removeMouseListener(swtMouseDownListener);
		} else {
			swtControl.addMouseListener(swtMouseDownListener);
		}
	}

	public void setOnMouseUp(IusCLMouseEvent onMouseUp) {
		this.onMouseUp = onMouseUp;
		if (!IusCLEvent.isDefinedEvent(onMouseUp)) {
			swtControl.removeMouseListener(swtMouseUpListener);
		} else {
			swtControl.addMouseListener(swtMouseUpListener);
		}
	}

	public void setOnDoubleClick(IusCLNotifyEvent onDoubleClick) {
		this.onDoubleClick = onDoubleClick;
		if (!IusCLEvent.isDefinedEvent(onDoubleClick)) {
			swtControl.removeMouseListener(swtMouseDoubleClickListener);
		} else {
			swtControl.addMouseListener(swtMouseDoubleClickListener);
		}
	}

	public void setOnMouseWheelDown(IusCLMouseWheelUpDownEvent onMouseWheelDown) {
		this.onMouseWheelDown = onMouseWheelDown;
		if (!IusCLEvent.isDefinedEvent(onMouseWheelDown)) {
			swtControl.removeMouseListener(swtMouseDownListener);
		} else {
			swtControl.addMouseListener(swtMouseDownListener);
		}
	}

	public void setOnMouseWheelUp(IusCLMouseWheelUpDownEvent onMouseWheelUp) {
		this.onMouseWheelUp = onMouseWheelUp;
		if (!IusCLEvent.isDefinedEvent(onMouseWheelUp)) {
			swtControl.removeMouseListener(swtMouseUpListener);
		} else {
			swtControl.addMouseListener(swtMouseUpListener);
		}
	}

	public void setOnMouseEnter(IusCLNotifyEvent onMouseEnter) {
		this.onMouseEnter = onMouseEnter;
		if (!IusCLEvent.isDefinedEvent(onMouseEnter)) {
			swtControl.removeMouseTrackListener(swtMouseTrackEnterListener);
		} else {
			swtControl.addMouseTrackListener(swtMouseTrackEnterListener);
		}
	}

	public void setOnMouseExit(IusCLNotifyEvent onMouseExit) {
		this.onMouseExit = onMouseExit;
		if (!IusCLEvent.isDefinedEvent(onMouseExit)) {
			swtControl.removeMouseTrackListener(swtMouseTrackExitListener);
		} else {
			swtControl.addMouseTrackListener(swtMouseTrackExitListener);
		}
	}

	public void setOnMouseHover(IusCLMouseHoverEvent onMouseHover) {
		this.onMouseHover = onMouseHover;
		if (!IusCLEvent.isDefinedEvent(onMouseHover)) {
			swtControl.removeMouseTrackListener(swtMouseTrackHoverListener);
		} else {
			swtControl.addMouseTrackListener(swtMouseTrackHoverListener);
		}
	}

	public void setOnMouseMove(IusCLMouseMoveEvent onMouseMove) {
		this.onMouseMove = onMouseMove;
		if (!IusCLEvent.isDefinedEvent(onMouseMove)) {
			swtControl.removeMouseMoveListener(swtMouseMoveListener);
		} else {
			swtControl.addMouseMoveListener(swtMouseMoveListener);
		}
	}

	public void setOnMouseWheel(IusCLMouseWheelEvent onMouseWheel) {
		this.onMouseWheel = onMouseWheel;
		if (!IusCLEvent.isDefinedEvent(onMouseWheel)) {
			swtControl.removeMouseWheelListener(swtMouseWheelListener);
		} else {
			swtControl.addMouseWheelListener(swtMouseWheelListener);
		}
	}

	protected void setAutoSize(boolean autoSize) {
		if (this.autoSize != autoSize) {
			this.autoSize = autoSize;
			if (this.autoSize) {
				doParentAlignControls();
			}
		}
	}

	protected void doAutoSizeParentAlignControls() {
		if (autoSize) {
			doParentAlignControls();
		}
	}

	public void setAlign(IusCLAlign align) {
		this.align = align;
		doParentAlignControls();
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
		if (swtControl != null) {
			swtControl.setCursor(IusCLCursor.getAsSwtCursor(cursor));
		}
	}

	public IusCLPredefinedCursors getAsPredefinedCursor() {
		if (cursor != null) {
			IusCLPredefinedCursors predefinedCursor = null;
			try {
				predefinedCursor = IusCLPredefinedCursors.valueOf(cursor);
			} catch (Exception _) {
				/*  */
			}
			if (predefinedCursor != null) {
				return predefinedCursor;
			}
		}

		return null;
	}

	public void setPredefinedCursor(IusCLPredefinedCursors predefinedCursor) {
		setCursor(predefinedCursor.name());
	}

	public void setFont(IusCLFont font) {
		// this.font = font;
		this.font.setNotify(null, null);

		this.font.setSwtFont(font.getSwtFont());
		this.font.setColor(font.getColor());

		this.font.setNotify(this, "setFont");

		if (parent != null) {
			this.parentFont = false;
		}
		putFont();
	}

	public void setParentFont(boolean parentFont) {
		this.parentFont = parentFont;
		if (parent != null && this.parentFont) {
			// this.getProperty("Font").setDefaultValue(parent.getShowHint().toString());
			IusCLFont.putFontPropertiesDefaultValues(this, "Font", parent.getFont());
			setFont(parent.getFont());
			this.parentFont = true;
		}
	}

	private void putFont() {
		if (swtControl != null) {
			swtControl.setFont(font.getSwtFont());
			swtControl.setForeground(font.getColor().getAsSwtColor());
		}
	}

	public void setParentShowHint(boolean parentShowHint) {
		this.parentShowHint = parentShowHint;
		if (parent != null && this.parentShowHint) {
			this.getProperty("ShowHint").setDefaultValue(IusCLStrUtils.booleanToString(parent.getShowHint()));
			setShowHint(parent.getShowHint());
			this.parentShowHint = true;
		}
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
		if (parent != null) {
			this.parentShowHint = false;
		}
		putHint();
	}

	public void setHint(String hint) {
		this.hint = hint;
		putHint();
	}

	private void putHint() {
		if (swtControl != null) {
			if (showHint) {
				swtControl.setToolTipText(hint);
			} else {
				swtControl.setToolTipText(null);
			}
		}
	}

	public void setColor(IusCLColor color) {
		this.color = color;
		if (parent != null) {
			this.parentColor = false;
		}
		putColor(this.color);
	}

	public void setParentColor(boolean parentColor) {
		this.parentColor = parentColor;
		if (parent != null) {
			if (this.parentColor) {
				this.getProperty("Color").setDefaultValue(parent.getColor().getAsString());
				setColor(parent.getColor());
				this.parentColor = true;
				if (IusCLScreen.getIsThemed()) {
					putColor(null);
				}
			} else {
				if (IusCLStrUtils.equalValues(color.getAsString(), getProperty("Color").getDefaultValue())) {
					putColor(color);
				}
			}
		}
	}

	private void putColor(IusCLColor color) {
		if (swtControl != null) {
			if (color == null) {
				swtControl.setBackground(null);
			} else {
				swtControl.setBackground(color.getAsSwtColor());
			}
		}
	}

	public void setPopupMenu(IusCLPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
		if (swtControl != null) {
			swtControl.setMenu(null);
			if (popupMenu == null) {
				return;
			}
			if (popupMenu.getSwtMenu() != null) {
				Shell swtShell = IusCLControl.this.findForm().getSwtShell();
				if (popupMenu.getSwtMenu().getShell() != swtShell) {
					Menu swtPopupMenu = new Menu(swtShell, SWT.POP_UP);
					popupMenu.setSwtMenu(swtPopupMenu);

					for (int index = 0; index < popupMenu.getItemCount(); index++) {
						IusCLMenuItem popupMenuItem = popupMenu.getItem(index);
						popupMenuItem.setParentMenu(popupMenu);
					}
				}
				swtControl.setMenu(popupMenu.getSwtMenu());
				// try {
				//
				// swtControl.setMenu(popupMenu.getSwtMenu());
				// }
				// catch (Exception exception) {
				//
				// IusCLLog.logError("Pop-up menu");
				// /* Won't work in design */
				// }
			}
		}
	}

	protected Control createSwtControl() {
		return null;
	}

	protected void createWnd(Control swtControl) {
		this.setSwtControl(swtControl);
		create();
		assign();
		reCreateWnd();
	}

	public void reCreateWnd() {
		if (this.getIsLoading()) {
			/* recreate at the end of loading */
			return;
		}
		Control newSwtControl = createSwtControl();
		if (newSwtControl == null) {
			/* No SWT constructor need to call again for this IusCLControl */
			return;
		}
		Control oldSwtControl = this.swtControl;
		Composite swtParentComposite = this.swtControl.getParent();
		// this.swtControl.dispose();
		boolean parentColorOld = parentColor;
		boolean parentShowHintOld = parentShowHint;
		boolean parentFontOld = parentFont;
		this.setSwtControl(newSwtControl);
		assign();
		this.setParentColor(parentColorOld);
		this.setParentShowHint(parentShowHintOld);
		this.setParentFont(parentFontOld);
		/* Parent */
		IusCLParentControl parentControl = this.getParent();
		if (parentControl != null) {
			List<IusCLControl> siblingControls = this.getParent().getControls();

			newSwtControl.setParent(swtParentComposite);
			int index = siblingControls.indexOf(this);

			if (index > 0) {
				Control swtPrevControl = siblingControls.get(index - 1).getSwtControl();
				newSwtControl.moveAbove(swtPrevControl);
			}
			if (index < siblingControls.size() - 1) {
				Control swtNextControl = siblingControls.get(index + 1).getSwtControl();
				newSwtControl.moveBelow(swtNextControl);
			}

			if (parentControl instanceof IusCLContainerControl containerControl) {
				containerControl.doUpdateTabOrder();
			}
		}
		if (this instanceof IusCLContainerControl containerControl) {
			for (int index = 0; index < containerControl.getControls().size(); index++) {
				containerControl.getControls().get(index).getSwtControl().setParent(containerControl.getSwtComposite());
				containerControl.getControls().get(index).getSwtControl().moveAbove(null);
			}
		}
		if (IusCLApplication.getIsRunning() && IusCLControl.this.getVisible()) {
			setVisible(true);
		}
		oldSwtControl.setMenu(null);
		/* Pop-up menus, change the parent shell */
		if (IusCLControl.this instanceof IusCLForm) {
			IusCLForm form = (IusCLForm) this;
			for (int index = 0; index < form.getComponents().size(); index++) {
				IusCLComponent ownedComponent = form.getComponents().get(index);
				if (ownedComponent instanceof IusCLControl control && control.getPopupMenu() != null) {
					control.setPopupMenu(control.getPopupMenu());
				}
			}
		}
		oldSwtControl.dispose();
	}

	protected void create() {
		/* Listeners applied to control once */
		Point defaultSize = swtControl.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.getProperty("Height").setDefaultValue(Integer.toString(defaultSize.y));
		setHeight(defaultSize.y);
		this.getProperty("Width").setDefaultValue(Integer.toString(defaultSize.x));
		setWidth(defaultSize.x);

		/* Selection */
		swtMenuDetectListener = swtMenuDetectEvent -> {
			if (IusCLEvent.isDefinedEvent(onContextPopup)) {
				boolean handled = onContextPopup.invoke(IusCLControl.this, swtMenuDetectEvent.x, swtMenuDetectEvent.y);
				if (handled) {
					swtMenuDetectEvent.doit = false;
				}
			}
		};

		/* Mouse */
		swtMouseDownListener = MouseListener.mouseDownAdapter(swtMouseEvent -> {
			IusCLMouseButton button = findMouseButtonFromMouseEvent(swtMouseEvent);
			boolean handled = false;
			if (button == IusCLMouseButton.mbMiddle && IusCLEvent.isDefinedEvent(onMouseWheelDown)) {
				EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
				handled = onMouseWheelDown.invoke(IusCLControl.this, shift, swtMouseEvent.x, swtMouseEvent.y);
			}
			if (!handled && IusCLEvent.isDefinedEvent(onMouseDown)) {
				EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
				onMouseDown.invoke(IusCLControl.this, button, shift, swtMouseEvent.x, swtMouseEvent.y);
			}
		});

		swtMouseUpListener = MouseListener.mouseUpAdapter(swtMouseEvent -> {
			IusCLMouseButton button = findMouseButtonFromMouseEvent(swtMouseEvent);
			boolean handled = false;
			if (button == IusCLMouseButton.mbMiddle && IusCLEvent.isDefinedEvent(onMouseWheelUp)) {
				EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
				handled = onMouseWheelUp.invoke(IusCLControl.this, shift, swtMouseEvent.x, swtMouseEvent.y);
			}
			if (!handled && IusCLEvent.isDefinedEvent(onMouseUp)) {
				// TODO why is empty
			}
			EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
			onMouseUp.invoke(IusCLControl.this, button, shift, swtMouseEvent.x, swtMouseEvent.y);
		});

		swtMouseDoubleClickListener = MouseListener.mouseDoubleClickAdapter(_ -> {
			if (IusCLEvent.isDefinedEvent(onDoubleClick)) {
				onDoubleClick.invoke(IusCLControl.this);
			}
		});

		swtMouseTrackEnterListener = MouseTrackListener.mouseEnterAdapter(_ -> {
			if (IusCLEvent.isDefinedEvent(onMouseEnter)) {
				onMouseEnter.invoke(IusCLControl.this);
			}
		});

		swtMouseTrackExitListener = MouseTrackListener.mouseExitAdapter(_ -> {
			if (IusCLEvent.isDefinedEvent(onMouseExit)) {
				onMouseExit.invoke(IusCLControl.this);
			}
		});

		swtMouseTrackHoverListener = MouseTrackListener.mouseHoverAdapter(swtMouseEvent -> {
			if (IusCLEvent.isDefinedEvent(onMouseHover)) {
				onMouseHover.invoke(IusCLControl.this, swtMouseEvent.x, swtMouseEvent.y);
			}
		});

		swtMouseMoveListener = swtMouseEvent -> {
			if (IusCLEvent.isDefinedEvent(onMouseMove)) {
				EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
				onMouseMove.invoke(IusCLControl.this, shift, swtMouseEvent.x, swtMouseEvent.y);
			}
		};

		swtMouseWheelListener = swtMouseEvent -> {
			if (IusCLEvent.isDefinedEvent(onMouseWheel)) {
				EnumSet<IusCLShiftState> shift = findShiftStateFromSwtMouseEvent(swtMouseEvent);
				/* Clockwise */
				Integer wheelDelta = swtMouseEvent.count;
				onMouseWheel.invoke(IusCLControl.this, shift, wheelDelta, swtMouseEvent.x, swtMouseEvent.y);
			}
		};
	}

	protected IusCLMouseButton findMouseButtonFromMouseEvent(MouseEvent swtMouseEvent) {
		IusCLMouseButton mouseButton = null;
		switch (swtMouseEvent.button) {
		case 1:
			mouseButton = IusCLMouseButton.mbLeft;
			break;
		case 2:
			mouseButton = IusCLMouseButton.mbMiddle;
			break;
		case 3:
			mouseButton = IusCLMouseButton.mbRight;
			break;
		default:
			break;
		}
		return mouseButton;
	}

	protected EnumSet<IusCLShiftState> findShiftStateFromSwtMouseEvent(MouseEvent swtMouseEvent) {
		return findShiftStateFromSwtStateMask(swtMouseEvent.stateMask);
	}

	protected EnumSet<IusCLShiftState> findShiftStateFromSwtStateMask(int stateMask) {
		EnumSet<IusCLShiftState> shiftState = EnumSet.noneOf(IusCLShiftState.class);
		if ((stateMask & SWT.MODIFIER_MASK) != 0) {
			if ((stateMask & SWT.SHIFT) != 0) {
				shiftState.add(IusCLShiftState.ssShift);
			}
			if ((stateMask & SWT.ALT) != 0) {
				shiftState.add(IusCLShiftState.ssAlt);
			}
			if ((stateMask & SWT.CTRL) != 0) {
				shiftState.add(IusCLShiftState.ssCtrl);
			}
			if ((stateMask & SWT.COMMAND) != 0) {
				shiftState.add(IusCLShiftState.ssShift);
			}
			if (((stateMask & SWT.CTRL) != 0) && ((stateMask & SWT.ALT) != 0) && ((stateMask & SWT.SHIFT) == 0) && ((stateMask & SWT.COMMAND) == 0)) {
				shiftState.add(IusCLShiftState.ssAltGr);
			}
		}
		if ((stateMask & SWT.BUTTON_MASK) != 0) {
			if ((stateMask & SWT.BUTTON1) != 0) {
				shiftState.add(IusCLShiftState.ssMouseLeft);
			}
			if ((stateMask & SWT.BUTTON2) != 0) {
				shiftState.add(IusCLShiftState.ssMouseMiddle);
			}
			if ((stateMask & SWT.BUTTON3) != 0) {
				shiftState.add(IusCLShiftState.ssMouseRight);
			}
		}
		return shiftState;
	}

	protected static void transferSwtListeners(Control swtSourceControl, final Control swtDestinationControl) {
		Integer[] eventTypes = new Integer[] { SWT.Selection, SWT.MenuDetect, SWT.MouseDown, SWT.MouseUp, SWT.MouseMove, SWT.MouseHover,
				SWT.MouseDoubleClick, SWT.MouseEnter, SWT.MouseExit, SWT.MouseWheel, SWT.KeyDown, SWT.KeyUp };
		for (int index = 0; index < eventTypes.length; index++) {
			transferSwtListener(eventTypes[index], swtSourceControl, swtDestinationControl);
		}
	}

	private static void transferSwtListener(final int eventType, Control swtSourceControl, final Control swtDestinationControl) {
		swtSourceControl.addListener(eventType, swtEvent -> swtDestinationControl.notifyListeners(eventType, swtEvent));
	}
}
