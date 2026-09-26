/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.comctrls.IusCLImageList;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLNotifyEvent;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLMenuItem extends IusCLComponent {

	public enum IusCLMenuItemType {
		itNormal, itRadio, itCheck, itSeparator
	}

	@Getter
	MenuItem swtMenuItem = null;
	@Getter
	Menu swtSubMenu = null;
	SelectionListener swtSelectionListener = null;

	@Getter
	@Setter
	IusCLMenuItem parent = null;
	@Getter
	@Setter
	IusCLMenu parentMenu = null;

	@Getter
	Integer imageIndex = -1;
	@Getter
	String caption = "";
	@Getter
	boolean enabled = true;
	@Getter
	IusCLMenuItemType type = IusCLMenuItemType.itNormal;
	@Getter
	boolean checked = false;

	@Getter
	IusCLNotifyEvent onClick = null;

	public IusCLMenuItem(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Caption", IusCLPropertyType.ptString, "");
		defineProperty("Enabled", IusCLPropertyType.ptBoolean, "true");
		defineProperty("Type", IusCLPropertyType.ptEnum, "itNormal", IusCLMenuItemType.itNormal);
		defineProperty("Checked", IusCLPropertyType.ptBoolean, "false");

		defineProperty("OnClick", IusCLPropertyType.ptEvent, null, IusCLNotifyEvent.class);

		swtSelectionListener = SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			if (IusCLEvent.isDefinedEvent(onClick)) {
				onClick.invoke(IusCLMenuItem.this);
			}
		});

		setIsSubComponent(true);
	}

	@Override
	public String getDisplayName() {
		String displayName = super.getDisplayName();
		String itemCaption = this.getCaption();

		if (IusCLStrUtils.isNotNullNotEmpty(itemCaption)) {
			displayName = displayName + " { " + itemCaption + " } ";
		}

		return displayName;
	}

	public Integer getItemCount() {
		return this.getChildren().size();
	}

	public IusCLMenuItem getItem(Integer index) {
		return (IusCLMenuItem) this.getChildren().get(index);
	}

	@Override
	public void destroy() {
		/* to destroy in here SWT objects */
		if (swtMenuItem != null) {
			swtMenuItem.dispose();
		}
		if (swtSubMenu != null) {
			swtSubMenu.dispose();
		}
	}

	@Override
	public void setParentComponent(IusCLComponent parentComponent) {
		boolean reParent = false;

		if (this.getParentComponent() == null) {
			reParent = true;
		} else {
			if (!this.getParentComponent().equals(parentComponent)) {
				reParent = true;
			} else {
				if (this.getParentComponent() instanceof IusCLMenu) {
					reParent = true;
				}
			}
		}

		if (reParent) {
			super.setParentComponent(parentComponent);

			if (this.getParentComponent() instanceof IusCLMenuItem menuItem) {
				parent = menuItem;
				parentMenu = null;
				parent.makeThisParentMenuItem(false);
			}
			if (this.getParentComponent() instanceof IusCLMenu menu) {
				parent = null;
				parentMenu = menu;
			}
		}

		/*
		 * TODO Remove menu item from parent
		 * 
		 * If parent remains without children, make the parent real menu item
		 */

		if (this.getItemCount() > 0) {
			makeThisParentMenuItem(reParent);
		} else {
			makeThisRealMenuItem();
		}
	}

	private void makeThisParentMenuItem(boolean reParent) {
		if (!reParent) {
			if (swtMenuItem != null) {
				if (swtMenuItem.getStyle() == SWT.CASCADE) {
					return;
				} else {
					clearSwtMenuItem();
				}
			}
		} else {
			clearSwtMenuItem();
		}

		/* Just opens a sub-menu */
		int pos = this.getParentComponent().getChildren().indexOf(this);

		if (parent == null) {
			swtMenuItem = new MenuItem(parentMenu.getSwtMenu(), SWT.CASCADE, pos);
		} else {
			swtMenuItem = new MenuItem(parent.getSwtSubMenu(), SWT.CASCADE, pos);
		}

		assign();

		/* Sub-menu */
		swtSubMenu = new Menu(swtMenuItem);
		swtMenuItem.setMenu(swtSubMenu);

		for (int index = 0; index < this.getItemCount(); index++) {
			IusCLMenuItem childMenuItem = this.getItem(index);

			if (childMenuItem.getItemCount() > 0) {
				childMenuItem.makeThisParentMenuItem(true);
			} else {
				childMenuItem.makeThisRealMenuItem();
			}
		}
	}

	private void makeThisRealMenuItem() {
		clearSwtMenuItem();

		/* Is a real item */
		int pos = this.getParentComponent().getChildren().indexOf(this);

		int swtCreateParams = SWT.PUSH;
		switch (type) {
		case itCheck:
			swtCreateParams = SWT.CHECK;
			break;
		case itRadio:
			swtCreateParams = SWT.RADIO;
			break;
		case itSeparator:
			swtCreateParams = SWT.SEPARATOR;
			break;
		default:
			/* Intentionally left blank */
			break;
		}

		if (parent == null) {
			swtMenuItem = new MenuItem(parentMenu.getSwtMenu(), swtCreateParams, pos);
		} else {
			swtMenuItem = new MenuItem(parent.getSwtSubMenu(), swtCreateParams, pos);
		}

		assign();

		/* Sub-menu */
		if (swtSubMenu != null) {
			swtSubMenu.dispose();
		}
	}

	private void clearSwtMenuItem() {
		if (swtMenuItem != null) {
			swtMenuItem.dispose();
			swtMenuItem = null;
		}
	}

	public void setCaption(String caption) {
		this.caption = caption;

		if (swtMenuItem != null) {
			swtMenuItem.setText(this.caption);
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (swtMenuItem != null) {
			swtMenuItem.setEnabled(enabled);
		}
	}

	public void setType(IusCLMenuItemType type) {
		if (this.type != type) {
			this.type = type;

			if (swtMenuItem != null && this.getItemCount() == 0) {
				makeThisRealMenuItem();
			}
		}
	}

	public void setChecked(boolean checked) {
		this.checked = checked;

		if (swtMenuItem != null) {
			swtMenuItem.setSelection(checked);
		}
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (imageIndex == -1) {
			swtMenuItem.setImage(null);
			return;
		}

		IusCLMenu topParentMenu = findTopParentMenu();

		if (topParentMenu != null) {
			IusCLImageList images = topParentMenu.getImages();

			if (images != null) {
				Image swtImage = images.getAsSizedSwtImage(imageIndex);
				swtMenuItem.setImage(swtImage);
			} else {
				swtMenuItem.setImage(null);
			}
		}
	}

	private IusCLMenu findTopParentMenu() {
		IusCLMenuItem parentMenuItem = this;

		while (parentMenuItem.getParent() != null) {
			parentMenuItem = parentMenuItem.getParent();
		}

		return parentMenuItem.getParentMenu();
	}

	public void setOnClick(IusCLNotifyEvent onClick) {
		this.onClick = onClick;

		if (swtMenuItem != null) {
			swtMenuItem.removeSelectionListener(swtSelectionListener);
			if (this.onClick != null) {
				swtMenuItem.addSelectionListener(swtSelectionListener);
			}
		}
	}
}
