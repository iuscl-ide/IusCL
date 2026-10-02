/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.types.IusCLRectangle;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLContainerControl extends IusCLParentControl {

	@Getter
	Integer borderWidth = 0;

	public IusCLContainerControl(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("BorderWidth", IusCLPropertyType.ptInteger, "0");
	}

	public Composite getSwtComposite() {
		return null; // never null but here
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;

		doAlignControls(new IusCLSize(0, 0));
	}

	public Integer getContainerBorderWidth() {
		return borderWidth;
	}

	public Integer getContainerClientLeft() {
		int clientLeft = 0;
		if (this.getSwtComposite() != null) {
			clientLeft = this.getSwtComposite().getClientArea().x;
		}

		return clientLeft + this.getContainerBorderWidth();
	}

	public Integer getContainerClientTop() {
		int clientTop = 0;
		if (this.getSwtComposite() != null) {
			clientTop = this.getSwtComposite().getClientArea().y;
		}

		return clientTop + this.getContainerBorderWidth();
	}

	public Integer getContainerClientWidth() {
		int clientWidth = 0;
		if (this.getSwtComposite() != null) {
			clientWidth = this.getSwtComposite().getClientArea().width;
		}

		return clientWidth - (2 * this.getContainerBorderWidth());
	}

	public Integer getContainerClientHeight() {
		int clientHeight = 0;
		if (this.getSwtComposite() != null) {
			clientHeight = this.getSwtComposite().getClientArea().height;
		}

		return clientHeight - (2 * this.getContainerBorderWidth());
	}

	public List<IusCLWinControl> getWinControls() {
		final List<IusCLWinControl> winControls = new ArrayList<>();

		for (int index = 0; index < this.getControls().size(); index++) {
			IusCLControl control = this.getControls().get(index);

			if (control instanceof IusCLWinControl winControl) {
				winControls.add(winControl);
			}
		}

		return winControls;
	}

	public void doUpdateTabOrder() {
		final Map<Integer, IusCLWinControl> tabStopWinControls = new HashMap<>();

		for (int index = 0; index < this.getControls().size(); index++) {
			IusCLControl control = this.getControls().get(index);
			if (control instanceof IusCLWinControl winControl && winControl.getTabStop()) {
				tabStopWinControls.put(winControl.getTabOrder(), winControl);
			}
		}

		Control[] swtTabControls = new Control[tabStopWinControls.size()];

		Vector<Integer> tabOrdersVector = new Vector<>(tabStopWinControls.keySet());
		Collections.sort(tabOrdersVector);

		for (int index = 0; index < tabOrdersVector.size(); index++) {
			IusCLWinControl winControl = tabStopWinControls.get(tabOrdersVector.get(index));

			if (winControl.getSwtControl().getParent() != this.getSwtComposite()) {
				return;
			}

			swtTabControls[index] = winControl.getSwtControl();
		}

		this.getSwtComposite().setTabList(swtTabControls);
	}

	public void doAlignControls(IusCLSize parentDelta) {
		int clientWidth = this.getContainerClientWidth();
		int clientHeight = this.getContainerClientHeight();

		IusCLRectangle availableClientRect = new IusCLRectangle(0, 0, clientWidth, clientHeight);

		// System.out.println("doAlignControls " + this.getName());

		// System.out.println("layout containerControl " + containerControl.getName() +
		// " : " + clientTop + " - " + clientLeft +
		// " ! " + clientWidth + " - " + clientHeight + " si " +
		// containerControl.getWidth() + " - " + containerControl.getHeight());

		final List<IusCLControl> controlsAlignOrder = new ArrayList<>();

		final List<ArrayList<IusCLControl>> controlsAlignOrders = new ArrayList<>();
		for (int index = 0; index < 7; index++) {
			controlsAlignOrders.add(new ArrayList<IusCLControl>());
		}

		for (int zOrder = 0; zOrder < this.getControls().size(); zOrder++) {
			IusCLControl childControl = this.getControls().get(zOrder);

			switch (childControl.getAlign()) {
			case alTop:
				controlsAlignOrders.get(0).add(childControl);
				break;
			case alBottom:
				controlsAlignOrders.get(1).add(childControl);
				break;
			case alLeft:
				controlsAlignOrders.get(2).add(childControl);
				break;
			case alRight:
				controlsAlignOrders.get(3).add(childControl);
				break;
			case alClient:
				controlsAlignOrders.get(4).add(childControl);
				break;
			case alNone:
				controlsAlignOrders.get(5).add(childControl);
				break;
			case alCustom:
				controlsAlignOrders.get(6).add(childControl);
				break;
			default:
				break;
			}
		}
		for (int index = 0; index < 7; index++) {
			controlsAlignOrder.addAll(controlsAlignOrders.get(index));
		}

		for (int indexOrder = 0; indexOrder < controlsAlignOrder.size(); indexOrder++) {
			IusCLControl childControl = controlsAlignOrder.get(indexOrder);

			// System.out.println(" availableClientRect = " + availableClientRect.getWidth()
			// +
			// " childControl = " + childControl.getName());
			availableClientRect = childControl.doPositionAndSize(parentDelta, availableClientRect);
		}
	}
}
