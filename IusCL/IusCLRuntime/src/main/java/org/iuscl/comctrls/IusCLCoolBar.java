/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLParentControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCoolBar extends IusCLParentControl {

	CoolBar swtCoolBar = null;

	@Getter
	boolean autoSize = true;
	@Getter
	boolean fixedSize = false;

	public IusCLCoolBar(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("AutoSize", IusCLPropertyType.ptBoolean, "true");
		defineProperty("FixedSize", IusCLPropertyType.ptBoolean, "false");

		swtCoolBar = new CoolBar(this.getFormSwtComposite(), SWT.NONE);
		createWnd(swtCoolBar);

		swtCoolBar.setLayout(null);
	}

	public void update() {
		if ((swtCoolBar != null) && (!getControls().isEmpty())) {
			int bandsCount = getControls().size();

			int[] itemOrder = new int[bandsCount];
			int[] wrapIndices = null;
			Point[] sizes = new Point[bandsCount];

			int wrapIndicesSize = 0;

			for (int index = 0; index < bandsCount; index++) {
				IusCLCoolBand coolBand = (IusCLCoolBand) getControls().get(index);

				itemOrder[index] = index;

				sizes[index] = new Point(coolBand.getWidth(), coolBand.getHeight());

				if (coolBand.getBreak()) {
					wrapIndicesSize = wrapIndicesSize + 1;
				}
			}

			if (wrapIndicesSize > 0) {
				wrapIndices = new int[wrapIndicesSize];
				wrapIndicesSize = 0;

				for (int index = 0; index < bandsCount; index++) {
					IusCLCoolBand coolBand = (IusCLCoolBand) getControls().get(index);

					if (coolBand.getBreak()) {
						wrapIndices[wrapIndicesSize] = index;

						wrapIndicesSize = wrapIndicesSize + 1;
					}
				}
			}
			swtCoolBar.setItemLayout(itemOrder, wrapIndices, sizes);
			setAutoSize(autoSize);
			swtCoolBar.setLocked(fixedSize);
		}
	}

	@Override
	public void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;

		if (autoSize) {
			swtCoolBar.pack();
			this.width = swtCoolBar.getSize().x;
			this.height = swtCoolBar.getSize().y;
		}
	}

	public void setFixedSize(boolean fixedSize) {
		this.fixedSize = fixedSize;

		update();
	}

	//
	// public void setImages(IusCLCustomImageList images) {
	// this.images = images;
	//
	// for (int index = 0; index < bands.getCount(); index++) {
	//
	// IusCLCoolBand coolBand = (IusCLCoolBand)bands.getItem(index);
	// coolBand.setImageIndex(coolBand.getImageIndex());
	// }
	// }
}
