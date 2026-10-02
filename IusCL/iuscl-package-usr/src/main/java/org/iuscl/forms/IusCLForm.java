/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.forms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.iuscl.classes.IusCLFiler;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLSizeConstraints;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.events.IusCLCanResizeEvent;
import org.iuscl.events.IusCLCloseEvent;
import org.iuscl.events.IusCLCloseQueryEvent;
import org.iuscl.events.IusCLConstrainedResizeEvent;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.graphics.IusCLMultiGraphic;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.menus.IusCLMainMenu;
import org.iuscl.menus.IusCLMenuItem;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLGraphUtils;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLForm extends IusCLContainerControl {

	public enum IusCLCloseAction {
		caNone, caHide, caFree, caMinimize
	}

	public enum IusCLModalResult {
		mrNone, mrOk, mrCancel, mrAbort, mrRetry, mrIgnore, mrYes, mrNo, mrAll, mrNoToAll, mrYesToAll
	}

	public enum IusCLFormStyle {
		fsNormal, fsModal, fsModalDialog, fsStayOnTop, fsStayOnTopDialog
	}

	public enum IusCLWindowState {
		wsNormal, wsMinimized, wsMaximized
	}

	public enum IusCLFormBorderStyle {
		bsNone, bsSingle, bsSizeable, bsDialog, bsSizeDialog, bsToolWindow, bsSizeToolWindow
	}

	public enum IusCLFormPosition {
		poDesigned, poScreenCenter, poDesktopCenter, poMainFormCenter
	}

	// public enum IusCLFormPosition { poDesigned, poDefault, poDefaultPosOnly,
	// poDefaultSizeOnly,
	// poScreenCenter, poDesktopCenter, poMainFormCenter, poOwnerFormCenter };

	private static final String IUSCLFM = ".iusclfm";

	@Getter
	@Setter
	Shell swtShell = null;

	@Getter
	@Setter
	IusCLModalResult modalResult = IusCLModalResult.mrNone;

	@Getter
	@Setter
	boolean isFormStateModal = false;

	Integer menuBarHeight = 0;

	@Getter
	boolean isInDesignMode = false;

	@Getter
	IusCLFormStyle formStyle = IusCLFormStyle.fsNormal;
	@Getter
	IusCLWindowState windowState = IusCLWindowState.wsNormal;
	@Getter
	IusCLFormBorderStyle borderStyle = IusCLFormBorderStyle.bsSizeable;
	@Getter
	IusCLFormPosition position = IusCLFormPosition.poDesigned;
	IusCLBorderIcons borderIcons = new IusCLBorderIcons();
	@Getter
	IusCLMainMenu menu = null;
	@Getter
	IusCLPicture icon = null;
	@Getter
	IusCLWinControl activeControl = null;
	@Getter
	@Setter
	Integer gridX = 8;
	@Getter
	@Setter
	Integer gridY = 8;

	// DefaultMonitor
	// KeyPreview
	// Visible
	// Canvas

	@Getter
	@Setter
	IusCLNotifyEvent onHide = null;
	@Getter
	@Setter
	IusCLNotifyEvent onShow = null;

	@Getter
	@Setter
	IusCLNotifyEvent onActivate = null;
	@Getter
	@Setter
	IusCLNotifyEvent onDeactivate = null;

	@Getter
	@Setter
	IusCLNotifyEvent onCreate = null;
	@Getter
	@Setter
	IusCLNotifyEvent onDestroy = null;

	@Getter
	@Setter
	IusCLCloseQueryEvent onCloseQuery = null;
	@Getter
	@Setter
	IusCLCloseEvent onClose = null;

	public IusCLForm() {
		super(null);

		defineProperty("FormStyle", IusCLPropertyType.ptEnum, "fsNormal", IusCLFormStyle.fsNormal);
		defineProperty("WindowState", IusCLPropertyType.ptEnum, "wsNormal", IusCLWindowState.wsNormal);
		defineProperty("BorderStyle", IusCLPropertyType.ptEnum, "bsSizeable", IusCLFormBorderStyle.bsSizeable);
		defineProperty("Position", IusCLPropertyType.ptEnum, "poDesigned", IusCLFormPosition.poDesigned);

		defineProperty("ActiveControl", IusCLPropertyType.ptComponent, "", IusCLWinControl.class);

		defineProperty("BorderIcons.SystemMenu", IusCLPropertyType.ptBoolean, "true");
		defineProperty("BorderIcons.Minimize", IusCLPropertyType.ptBoolean, "true");
		defineProperty("BorderIcons.Maximize", IusCLPropertyType.ptBoolean, "true");

		defineProperty("ClientWidth", IusCLPropertyType.ptInteger, "640");
		defineProperty("ClientHeight", IusCLPropertyType.ptInteger, "480");

		defineProperty("GridX", IusCLPropertyType.ptInteger, "8");
		defineProperty("GridY", IusCLPropertyType.ptInteger, "8");

		defineProperty("Menu", IusCLPropertyType.ptComponent, "", IusCLMainMenu.class);

		defineProperty("Icon", IusCLPropertyType.ptIcon, "");

		this.removeProperty("Anchors.akLeft");
		this.removeProperty("Anchors.akTop");
		this.removeProperty("Anchors.akRight");
		this.removeProperty("Anchors.akBottom");

		// this.removeProperty("ParentColor");
		this.removeProperty("ParentShowHint");

		defineProperty("OnActivate", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnDeactivate", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		defineProperty("OnHide", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnShow", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		defineProperty("OnCreate", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);
		defineProperty("OnDestroy", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		defineProperty("OnCloseQuery", IusCLPropertyType.ptEvent, null, IusCLCloseQueryEvent.class);
		defineProperty("OnClose", IusCLPropertyType.ptEvent, null, IusCLCloseEvent.class);

		/* Design */
		if (IusCLApplication.getPutNextCreatedFormInDesignMode()) {
			this.isInDesignMode = true;
			IusCLApplication.setPutNextCreatedFormInDesignMode(false);
		}

		/* Constructor */
		createWnd(createSwtControl());

		/* Load from file */
		readFormRes();

		/*
		 * To call 'setMenuBar' and made the correct shell area without activate when
		 * the menu is higher than 0
		 */
		// setMenu(getMenu());

		/* Declare it in IusCLApplication */
		IusCLApplication.addForm(IusCLForm.this);

		/* OnCreate event */
		if (IusCLEvent.isDefinedEvent(onCreate)) {
			onCreate.invoke(IusCLForm.this);
		}
	}

	protected void readFormRes() {
		/* Keep the resource/file path for each form canonical name */
		final Map<String, String> forms = new LinkedHashMap<>();

		Class<?> ancestorFormClass = this.getClass();

		String thisFormCanonicalName = ancestorFormClass.getCanonicalName();

		String pd = IusCLFileUtils.getPathDelimiter();
		String fmFolder = IusCLApplication.getFMFile(thisFormCanonicalName);

		/* Where source folder starts */
		if (fmFolder != null) {
			fmFolder = fmFolder.replace(thisFormCanonicalName.replace(".", pd) + IUSCLFM, "");
		}

		while (ancestorFormClass != IusCLContainerControl.class) {
			String ancestorCanonicalName = ancestorFormClass.getCanonicalName();

			String fmFile = IusCLApplication.getFMFile(ancestorCanonicalName);

			if (fmFile == null) {
				if (fmFolder != null) {
					/* "iusclfm" file on disc, found by base source folder */
					fmFile = fmFolder + ancestorCanonicalName.replace(".", pd) + IUSCLFM;
				} else {
					/* Resource on jar */
					fmFile = "/" + ancestorCanonicalName.replace(".", "/") + IUSCLFM;
				}
			}

			forms.put(ancestorCanonicalName, fmFile);

			ancestorFormClass = ancestorFormClass.getSuperclass();
		}

		Vector<String> formCanonicalNames = new Vector<>(forms.keySet());

		/* Load inherited ones */
		for (int index = formCanonicalNames.size() - 1; index >= 1; index--) {
			String formCanonicalName = formCanonicalNames.elementAt(index);

			IusCLFiler.readComponentRes(this, forms.get(formCanonicalName), true);
		}

		/* Load this one */
		IusCLFiler.readComponentRes(this, forms.get(thisFormCanonicalName), false);
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		switch (borderStyle) {
		case bsNone:
			swtCreateParams = swtCreateParams | SWT.NO_TRIM;
			break;
		case bsSingle:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.BORDER;
			break;
		case bsSizeable:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.BORDER | SWT.RESIZE;
			break;
		case bsDialog:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.DIALOG_TRIM;
			break;
		case bsSizeDialog:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.DIALOG_TRIM | SWT.RESIZE;
			break;
		case bsToolWindow:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.BORDER | SWT.TOOL;
			break;
		case bsSizeToolWindow:
			swtCreateParams = swtCreateParams | SWT.TITLE | SWT.BORDER | SWT.RESIZE | SWT.TOOL;
			break;

		default:
			break;
		}

		if (borderIcons.getMaximize()) {
			swtCreateParams = swtCreateParams | SWT.MAX;
		}
		if (borderIcons.getMinimize()) {
			swtCreateParams = swtCreateParams | SWT.MIN;
		}
		if (borderIcons.getSystemMenu()) {
			swtCreateParams = swtCreateParams | SWT.CLOSE;
		}

		switch (formStyle) {
		case fsNormal:
			swtCreateParams = swtCreateParams | SWT.MODELESS;
			break;
		case fsModal, fsModalDialog:
			swtCreateParams = swtCreateParams | SWT.APPLICATION_MODAL;
			break;
		case fsStayOnTop, fsStayOnTopDialog:
			swtCreateParams = swtCreateParams | SWT.ON_TOP;
			break;
		}

		/* Constructor */
		if ((formStyle == IusCLFormStyle.fsModalDialog) || (formStyle == IusCLFormStyle.fsStayOnTopDialog)) {
			/* To not appear in taskbar */
			swtShell = new Shell(IusCLApplication.getSwtApplicationShell(), swtCreateParams);
		} else {
			/* Appear in taskbar */
			swtShell = new Shell(IusCLApplication.getSwtDisplay(), swtCreateParams);
		}

		swtShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		return swtShell;
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("640");
		setHeight(640);
		this.getProperty("Width").setDefaultValue("800");
		setWidth(800);
	}

	@Override
	protected void updateBounds(Integer aLeft, Integer aTop, Integer aWidth, Integer aHeight) {
		left = aLeft;
		top = aTop;
		width = aWidth;
		height = aHeight;

		if (swtShell != null) {
			swtShell.setLocation(left - (IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth() / 2),
					top - (IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight() / 2));
			swtShell.setSize(width + IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth(),
					height + IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight());
		}
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		/*
		 * TODO See if KeyPreview possible other way than on each IusCLWinControl key
		 * event
		 * 
		 * 
		 */

		/* Resize */
		swtShell.addControlListener(ControlListener.controlResizedAdapter(_ -> {
			Integer newWidth = swtShell.getSize().x - IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth();
			Integer newHeight = swtShell.getSize().y - IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight();

			/* OnCanResize event */
			IusCLSize newSize = new IusCLSize(newWidth, newHeight);

			boolean canResize = true;
			IusCLCanResizeEvent canResizeEvent = IusCLForm.this.getOnCanResize();
			if (IusCLEvent.isDefinedEvent(canResizeEvent)) {
				canResize = canResizeEvent.invoke(IusCLForm.this, newSize);
			}
			if (!canResize) {
				IusCLForm.this.setBounds(left, top, width, height);
				return;
			}

			/* OnConstrainedResize event */
			IusCLSizeConstraints newSizeConstraints = new IusCLSizeConstraints();
			newSizeConstraints.setMaxHeight(IusCLForm.this.getConstraints().getMaxHeight());
			newSizeConstraints.setMaxWidth(IusCLForm.this.getConstraints().getMaxWidth());
			newSizeConstraints.setMinHeight(IusCLForm.this.getConstraints().getMinHeight());
			newSizeConstraints.setMinWidth(IusCLForm.this.getConstraints().getMinWidth());

			IusCLConstrainedResizeEvent constrainedResizeEvent = IusCLForm.this.getOnConstrainedResize();
			if (IusCLEvent.isDefinedEvent(constrainedResizeEvent)) {
				constrainedResizeEvent.invoke(IusCLForm.this, newSizeConstraints);
			}

			Integer minWidth = newSizeConstraints.getMinWidth();
			Integer maxWidth = newSizeConstraints.getMaxWidth();
			Integer minHeight = newSizeConstraints.getMinHeight();
			Integer maxHeight = newSizeConstraints.getMaxHeight();

			boolean needsConstraint = false;
			Integer constrainedWidth = newWidth;
			Integer constrainedHeight = newHeight;

			if ((minWidth > 0) && (minWidth > constrainedWidth)) {
				constrainedWidth = minWidth;
				needsConstraint = true;
			}
			if ((maxWidth > 0) && (maxWidth < constrainedWidth)) {
				constrainedWidth = maxWidth;
				needsConstraint = true;
			}
			if ((minHeight > 0) && (minHeight > constrainedHeight)) {
				constrainedHeight = minHeight;
				needsConstraint = true;
			}
			if ((maxHeight > 0) && (maxHeight < constrainedHeight)) {
				constrainedHeight = maxHeight;
				needsConstraint = true;
			}

			if (needsConstraint) {
				IusCLForm.this.setBounds(left, top, constrainedWidth, constrainedHeight);

				if ((minWidth > 0) || (minHeight > 0)) {
					swtShell.setMinimumSize(minWidth, minHeight);
				}

				return;
			}

			IusCLSize parentDelta = new IusCLSize(newWidth - width, newHeight - height);

			width = newWidth;
			height = newHeight;

			/* OnResize event */
			IusCLNotifyEvent onResizeEvent = IusCLForm.this.getOnResize();
			if (IusCLEvent.isDefinedEvent(onResizeEvent)) {
				onResizeEvent.invoke(IusCLForm.this);
			}

			IusCLForm.this.doAlignControls(parentDelta);
		}));

		/* Move */
		swtShell.addControlListener(ControlListener.controlMovedAdapter(_ -> {
			left = swtShell.getLocation().x + (IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth() / 2);
			top = swtShell.getLocation().y + (IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight() / 2);
		}));

		swtShell.addShellListener(ShellListener.shellActivatedAdapter(_ -> {
			IusCLApplication.setActiveForm(IusCLForm.this);
			activate();
		}));

		swtShell.addShellListener(ShellListener.shellDeactivatedAdapter(_ -> {
			if (IusCLApplication.getActiveForm() != null && IusCLApplication.getActiveForm().equals(IusCLForm.this)) {
				IusCLApplication.setActiveForm(null);
			}
			deactivate();
		}));

		swtShell.addShellListener(ShellListener.shellClosedAdapter(swtShellEvent -> {
			swtShellEvent.doit = false;
			close();
		}));
	}

	protected void activate() {
		/* OnActivate event */
		if (IusCLEvent.isDefinedEvent(onActivate)) {
			onActivate.invoke(IusCLForm.this);
		}
	}

	protected void deactivate() {
		/* OnDeactivate event */
		if (IusCLEvent.isDefinedEvent(onDeactivate)) {
			onDeactivate.invoke(IusCLForm.this);
		}
	}

	public void focusControl(IusCLWinControl winControl) {
		/*  */
	}

	public void close() {
		if (getIsFormStateModal()) {
			setModalResult(IusCLModalResult.mrCancel);
			return;
		}

		/* OnCloseQuery event */
		boolean canClose = true;

		if (IusCLEvent.isDefinedEvent(onCloseQuery)) {
			canClose = onCloseQuery.invoke(IusCLForm.this);
		}

		if (!canClose) {
			return;
		}

		/* OnClose event */
		IusCLCloseAction closeAction = IusCLCloseAction.caHide;

		if (IusCLEvent.isDefinedEvent(onClose)) {
			closeAction = onClose.invoke(IusCLForm.this);
		}

		if (closeAction == IusCLCloseAction.caNone) {
			return;
		}

		if (IusCLApplication.getMainForm() == IusCLForm.this) {
			IusCLForm.this.release();
			IusCLApplication.setIsTerminated();
			return;
		}

		switch (closeAction) {
		case caFree:
			IusCLForm.this.release();
			break;
		case caHide:
			IusCLForm.this.hide();
			break;
		case caMinimize:
			IusCLForm.this.setWindowState(IusCLWindowState.wsMinimized);
			break;
		default:
			/* Intentionally left blank */
			break;
		}
	}

	private void closeModal() {
		IusCLCloseAction closeAction = IusCLCloseAction.caHide;

		/* OnCloseQuery event */
		boolean canClose = true;

		if (IusCLEvent.isDefinedEvent(onCloseQuery)) {
			canClose = onCloseQuery.invoke(IusCLForm.this);
		}

		if (canClose) {
			/* OnClose event */
			if (IusCLEvent.isDefinedEvent(onClose)) {
				closeAction = onClose.invoke(IusCLForm.this);
			}
		} else {
			closeAction = IusCLCloseAction.caNone;
		}

		if (closeAction == IusCLCloseAction.caNone) {
			modalResult = IusCLModalResult.mrNone;
		}
	}

	public void release() {
		/* OnDestroy event */
		if (IusCLEvent.isDefinedEvent(onDestroy)) {
			onDestroy.invoke(IusCLForm.this);
		}
		/* Remove it from IusCLApplication */
		if (IusCLApplication.getActiveForm() != null && IusCLApplication.getActiveForm().equals(IusCLForm.this)) {
			IusCLApplication.setActiveForm(null);
		}
		IusCLApplication.removeForm(IusCLForm.this);
		this.free();
	}

	@Override
	public void bringToFront() {
		swtShell.moveAbove(null);
	}

	@Override
	public void setFocus() {
		super.setFocus();

		swtShell.setActive();
	}

	public void show() {
		this.setVisible(true);
		this.bringToFront();
		this.setFocus();

		if (activeControl != null) {
			activeControl.setFocus();
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			/* OnShow event */
			if (IusCLEvent.isDefinedEvent(onShow)) {
				onShow.invoke(IusCLForm.this);
			}
		} else {
			/* OnHide event */
			if (IusCLEvent.isDefinedEvent(onHide)) {
				onHide.invoke(IusCLForm.this);
			}
		}

		super.setVisible(visible);
	}

	public IusCLModalResult showModal() {
		/* IusCLApplication.modalStarted(); */

		boolean verifyIsRunning = true;
		if (!IusCLApplication.getIsRunning()) {
			/* Opened from an non-loop application */
			verifyIsRunning = false;
		}

		if ((this.formStyle != IusCLFormStyle.fsModal) && (this.formStyle != IusCLFormStyle.fsModalDialog)) {
			// IusCLLog.logError("Try to modal show a form that doesn't has a modal style");
		}

		this.isFormStateModal = true;

		show();

		modalResult = IusCLModalResult.mrNone;

		while (modalResult == IusCLModalResult.mrNone) {
			IusCLApplication.handleMessage();

			if ((verifyIsRunning) && (!IusCLApplication.getIsRunning())) {
				modalResult = IusCLModalResult.mrCancel;
			} else {
//				if (modalResult != IusCLModalResult.mrNone) {
				closeModal();
//				}
			}
		}

		IusCLForm.this.hide();

		this.isFormStateModal = false;

		/* IusCLApplication.modalFinished(); */

		return modalResult;
	}

	public boolean getIsActivated() {
		return swtShell.isFocusControl();
	}

	public void hide() {
		this.setVisible(false);
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		swtShell.setText(this.getCaption());
	}

	public void setWindowState(IusCLWindowState windowState) {
		this.windowState = windowState;

		switch (windowState) {
		case wsNormal:
			swtShell.setMinimized(false);
			swtShell.setMaximized(false);
			break;
		case wsMinimized:
			swtShell.setMinimized(true);
			break;
		case wsMaximized:
			swtShell.setMaximized(true);
			break;
		default:
			break;
		}
	}

	public IusCLBorderIcons getBorderIcons() {
		borderIcons.setNotify(this, "setBorderIcons");

		return borderIcons;
	}

	public void setBorderIcons(IusCLBorderIcons borderIcons) {
		this.borderIcons = borderIcons;

		reCreateWnd();
	}

	public void setBorderStyle(IusCLFormBorderStyle borderStyle) {
		this.borderStyle = borderStyle;

		reCreateWnd();
	}

	public void setFormStyle(IusCLFormStyle formStyle) {
		this.formStyle = formStyle;

		reCreateWnd();
	}

	public Integer getClientWidth() {
		return width - (IusCLScreen.getFormMargins().get(borderStyle).getLeft() + IusCLScreen.getFormMargins().get(borderStyle).getRight()
				- IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth());
	}

	public void setClientWidth(Integer clientWidth) {
		this.setWidth(clientWidth + IusCLScreen.getFormMargins().get(borderStyle).getLeft() + IusCLScreen.getFormMargins().get(borderStyle).getRight()
				- IusCLScreen.getFormCompensateSize().get(borderStyle).getWidth());
	}

	public Integer getClientHeight() {
		return height - (IusCLScreen.getFormMargins().get(borderStyle).getTop() + IusCLScreen.getFormMargins().get(borderStyle).getBottom()
				- IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight() + menuBarHeight);
	}

	public void setClientHeight(Integer clientHeight) {
		this.setHeight(
				clientHeight + IusCLScreen.getFormMargins().get(borderStyle).getTop() + IusCLScreen.getFormMargins().get(borderStyle).getBottom()
						- IusCLScreen.getFormCompensateSize().get(borderStyle).getHeight() + menuBarHeight);
	}

	public void setPosition(IusCLFormPosition position) {
		this.position = position;

		switch (position) {
		case poScreenCenter:
			Monitor swtPrimaryMonitor = IusCLApplication.getSwtDisplay().getPrimaryMonitor();
			Rectangle swtMonitorRectangle = swtPrimaryMonitor.getClientArea();

			this.setLeft(swtMonitorRectangle.x + (swtMonitorRectangle.width - this.getWidth()) / 2);
			this.setTop(swtMonitorRectangle.y + (swtMonitorRectangle.height - this.getHeight()) / 2);

			break;
		case poDesigned:
			// swtShell.setLocation(this.getLeft(), this.getTop());
			break;
		case poDesktopCenter:

			Monitor[] swtMonitors = IusCLApplication.getSwtDisplay().getMonitors();

			int desktopWidth = 0;
			int desktopHeight = 0;
			for (int index = 0; index < swtMonitors.length; index++) {
				desktopWidth = desktopWidth + swtMonitors[index].getClientArea().width;

				if ((desktopHeight == 0) || (desktopHeight < swtMonitors[index].getClientArea().height)) {
					desktopHeight = swtMonitors[index].getClientArea().height;
				}
			}

			this.setLeft((desktopWidth - this.getWidth()) / 2);
			this.setTop((desktopHeight - this.getHeight()) / 2);

			break;
		case poMainFormCenter:

			IusCLForm mainForm = IusCLApplication.getMainForm();

			if (mainForm != null) {
				if (mainForm.equals(this)) {
					setPosition(IusCLFormPosition.poScreenCenter);
				} else {
					this.setLeft(mainForm.getLeft() + (mainForm.getWidth() - this.getWidth()) / 2);
					this.setTop(mainForm.getTop() + (mainForm.getHeight() - this.getHeight()) / 2);
				}
			}

			break;
		}
	}

	@Override
	public Composite getSwtComposite() {
		return this.swtShell;
	}

	public void setMenu(IusCLMainMenu menu) {
		if ((this.menu != null) && (menu != null) && this.menu != menu && this.menu.getName().equalsIgnoreCase("transientMainMenu")) {
			this.menu.free();
		}

		this.menu = menu;

		swtShell.setMenuBar(null);

		if (menu == null) {
			menuBarHeight = 0;
			return;
		}

		if (menu.getSwtMenu() != null) {
			if (menu.getSwtMenu().getShell() != swtShell) {
				Menu swtMenu = new Menu(swtShell, SWT.BAR);
				menu.setSwtMenu(swtMenu);

				for (int index = 0; index < menu.getItemCount(); index++) {
					IusCLMenuItem menuItem = menu.getItem(index);

					menuItem.setCaption(menuItem.getCaption());

					menuItem.setParentMenu(menu);
				}
			}

			menuBarHeight = IusCLScreen.getMenuBarHeight();

			swtShell.setMenuBar(menu.getSwtMenu());
		}
	}

	public void setIcon(IusCLPicture icon) {
		this.icon = icon;

		createIcon(swtShell, icon);
	}

	public void createIcon(Shell swtShell, IusCLPicture paramIcon) {
		if (IusCLGraphUtils.isEmptyPicture(paramIcon)) {
			/* Try to load from application resource */
			paramIcon = new IusCLPicture();
			/*
			 * Load from application, need to know the jar by the form class because
			 * IusCLApplication is in the framework jar
			 */
			IusCLApplication.loadFromApplicationResource(paramIcon, this.getClass(), "MAINICON.ico");
			if (paramIcon.getIsEmpty()) {
				/* Load from framework */
				paramIcon = IusCLApplication.getIcon();
			}
		}

		/* Assign icon to shell */
		if (paramIcon.getGraphic() instanceof IusCLMultiGraphic multiGraphic) {
			Image[] swtImages = new Image[multiGraphic.size()];

			for (int index = 0; index < multiGraphic.size(); index++) {
				swtImages[index] = multiGraphic.get(index).getSwtImage();
			}

			swtShell.setImages(swtImages);
		} else {
			swtShell.setImage(paramIcon.getGraphic().getSwtImage());
		}
	}

	public void setActiveControl(IusCLWinControl activeControl) {
		this.activeControl = activeControl;

		if (activeControl != null) {
			activeControl.setFocus();
		}
	}

	public void setActiveControlValue(IusCLWinControl activeControl) {
		this.activeControl = activeControl;
	}
}
