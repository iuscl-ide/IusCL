/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.iuscl.forms.IusCLForm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLComponent extends IusCLPersistent {

	protected boolean formIsInDesignMode = false;

	IusCLComponent ownerComponent = null;
	final List<IusCLComponent> ownedComponents = new ArrayList<>();
	final List<IusCLComponent> childComponents = new ArrayList<>();

	@Getter
	@Setter
	Object tag = null;

	/*
	 * This is used only for non-visual components, for controls is the in
	 * IusCLParentControl
	 */
	@Getter
	IusCLComponent parentComponent = null;

	@Getter
	@Setter
	IusCLCollection subCollection = null;

	@Getter
	@Setter
	boolean isSubComponent = false;

	@Getter
	boolean isEmbedded = false;

	@Getter
	@Setter
	String name = null;

	public IusCLComponent() {
		this(null);
	}

	public IusCLComponent(IusCLComponent aOwner) {
		super();

		this.ownerComponent = aOwner;
		if (ownerComponent != null) {
			ownerComponent.getComponents().add(this);
		}
		formIsInDesignMode = this.findForm().getIsInDesignMode();

		defineProperty("Name", IusCLPropertyType.ptString, "defaultName");
	}

	public void destroy() {
		/* */
	}

	@Override
	public void free() {
		/* To free in here */
		if (this != null) {
			for (int index = ownedComponents.size() - 1; index >= 0; index--) {
				ownedComponents.get(index).free();
			}

			/* Free also the child components */
			for (int index = childComponents.size() - 1; index >= 0; index--) {
				childComponents.get(index).free();
			}

			/* Remove from ownerComponent */
			if (this.getOwner() != null) {
				this.getOwner().getComponents().remove(this);
			}

			/* Remove from parentComponent */
			if (this.getParentComponent() != null) {
				this.getParentComponent().getChildren().remove(this);
			}

			this.destroy();
		}
	}

	public IusCLComponent findOwnedComponentByName(String ownedComponentName) {
		for (int index = 0; index < ownedComponents.size(); index++) {
			IusCLComponent ownedComponent = ownedComponents.get(index);
			if (ownedComponent.getName().equalsIgnoreCase(ownedComponentName)) {
				return ownedComponent;
			}
		}

		return null;
	}

	public IusCLForm findForm() {
		IusCLComponent component = this;
		while (!(component instanceof IusCLForm)) {
			component = component.getOwner();
		}
		return (IusCLForm) component;
	}

	public Composite getFormSwtComposite() {
		return this.findForm().getSwtComposite();
	}

	public void selectSubComponent(IusCLComponent subComponent) {
		/* For IusCLToolButtons, IusCLTabSheets (and maybe others) */
	}

	public IusCLComponent getOwner() {
		return ownerComponent;
	}

	public List<IusCLComponent> getComponents() {
		return ownedComponents;
	}

	public void setParentComponent(IusCLComponent parentComponent) {
		if (this.parentComponent != null) {
			if (!(this.parentComponent.equals(parentComponent))) {
				this.parentComponent.getChildren().remove(this);
				parentComponent.getChildren().add(this);
			}
		} else {
			parentComponent.getChildren().add(this);
		}

		this.parentComponent = parentComponent;
	}

	public List<IusCLComponent> getChildren() {
		return childComponents;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public void setIsEmbedded(boolean isEmbedded) {
		/* No dis-embedd (what would suppose to do?) */
		if (isEmbedded) {
			this.getOwner().getComponents().remove(this);
			this.isEmbedded = isEmbedded;
		}
	}
}
