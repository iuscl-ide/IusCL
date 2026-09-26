/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLControl;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.forms.IusCLCursor.IusCLPredefinedCursors;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLSplitter extends IusCLWinControl {

	public enum IusCLResizeStyle {
		rsNone, rsLine, rsUpdate, rsPattern
	}

	boolean drag = false;
	int initialMouseLeft = 0;
	int initialMouseTop = 0;

	Composite swtSplitter = null;
	Composite swtSplitterTracker = null;

	/*
	 * TODO autoSnap & beveled
	 * 
	 * 
	 */

	// private boolean autoSnap = false;
	// private boolean beveled = false;
	@Getter
	@Setter
	IusCLResizeStyle resizeStyle = IusCLResizeStyle.rsUpdate;

	@Getter
	@Setter
	IusCLColor trackerColor = IusCLColor.getStandardColor(IusCLStandardColors.clBlack);

	public IusCLSplitter(IusCLComponent aOwner) {
		super(aOwner);

		// defineProperty("AutoSnap", IusCLPropertyType.ptBoolean, "false");
		// defineProperty("Beveled", IusCLPropertyType.ptBoolean, "false");
		defineProperty("ResizeStyle", IusCLPropertyType.ptEnum, "rsUpdate", IusCLResizeStyle.rsUpdate);
		defineProperty("TrackerColor", IusCLPropertyType.ptColor, "clBlack", IusCLStandardColors.clBlack);

		swtSplitter = new Composite(this.getFormSwtComposite(), SWT.NONE);
		createWnd(swtSplitter);
	}

	@Override
	protected void create() {
		super.create();

		swtSplitter.addMouseListener(MouseListener.mouseDownAdapter(swtMouseEvent -> {
			if (resizeStyle == IusCLResizeStyle.rsNone) {
				return;
			}

			if (resizeStyle == IusCLResizeStyle.rsUpdate) {
				initialMouseLeft = swtMouseEvent.x;
				initialMouseTop = swtMouseEvent.y;
			} else {
				initialMouseLeft = swtMouseEvent.x - swtSplitter.getLocation().x;
				initialMouseTop = swtMouseEvent.y - swtSplitter.getLocation().y;
			}

			if ((resizeStyle == IusCLResizeStyle.rsLine) || (resizeStyle == IusCLResizeStyle.rsPattern)) {
				swtSplitterTracker = new Composite(swtSplitter.getParent(), SWT.NONE);
				swtSplitterTracker.setBackground(IusCLSplitter.this.trackerColor.getAsSwtColor());
				swtSplitterTracker.moveAbove(null);
				swtSplitterTracker.setBounds(swtSplitter.getBounds());

				if (resizeStyle == IusCLResizeStyle.rsPattern) {
					Region trackerRegion = new Region();
					for (int indexX = 0; indexX < swtSplitterTracker.getSize().x / 2 + 1; indexX++) {
						for (int indexY = 0; indexY < swtSplitterTracker.getSize().y; indexY++) {
							if (indexY % 2 == 0) {
								trackerRegion.add(indexX * 2, indexY, 1, 1);
							} else {
								trackerRegion.add(indexX * 2 + 1, indexY, 1, 1);
							}
						}
					}
					swtSplitterTracker.setRegion(trackerRegion);
				}
			}
			drag = true;
		}));

		swtSplitter.addMouseListener(MouseListener.mouseUpAdapter(swtMouseEvent -> {
			if (swtSplitterTracker != null) {
				swtSplitter.setLocation(swtSplitterTracker.getLocation().x, swtSplitterTracker.getLocation().y);
				updateControls(swtSplitterTracker.getLocation().x, swtSplitterTracker.getLocation().y);
				swtSplitterTracker.dispose();
			}
			drag = false;
		}));

		/* mouseDoubleClick */
		/* TODO Go to min/max? */

		swtSplitter.addMouseMoveListener(swtMouseEvent -> {
			if (drag) {
				if ((IusCLSplitter.this.getAlign() == IusCLAlign.alLeft) || (IusCLSplitter.this.getAlign() == IusCLAlign.alRight)) {
					/* LeftRight */
					if (resizeStyle == IusCLResizeStyle.rsUpdate) {
						swtSplitter.setLocation(swtSplitter.getLocation().x + swtMouseEvent.x - initialMouseLeft, swtSplitter.getLocation().y);
						updateControls(swtSplitter.getLocation().x, swtSplitter.getLocation().y);
					} else {
						swtSplitterTracker.setLocation(swtMouseEvent.x - initialMouseLeft, swtSplitterTracker.getLocation().y);
					}
				} else if ((IusCLSplitter.this.getAlign() == IusCLAlign.alTop) || (IusCLSplitter.this.getAlign() == IusCLAlign.alBottom)) {
					/* TopBottom */
					if (resizeStyle == IusCLResizeStyle.rsUpdate) {
						swtSplitter.setLocation(swtSplitter.getLocation().x, swtSplitter.getLocation().y + swtMouseEvent.y - initialMouseTop);
						updateControls(swtSplitter.getLocation().x, swtSplitter.getLocation().y);
					} else {
						swtSplitterTracker.setLocation(swtSplitterTracker.getLocation().x, swtMouseEvent.y - initialMouseTop);
					}
				}
			}
		});
	}

	private void updateControls(int splitterX, int splitterY) {
		List<IusCLControl> parentControls = IusCLSplitter.this.getParent().getControls();

		int indexInParent = parentControls.indexOf(IusCLSplitter.this);

		if (indexInParent == 0) {
			return;
		}

		IusCLControl prevControl = parentControls.get(indexInParent - 1);

		if (prevControl.getAlign() != IusCLSplitter.this.getAlign()) {
			return;
		}

		/* Left */
		if (IusCLSplitter.this.getAlign() == IusCLAlign.alLeft) {
			prevControl.setWidth(splitterX - prevControl.getLeft());
		}

		/* Right */
		if (IusCLSplitter.this.getAlign() == IusCLAlign.alRight) {
			int newLeft = splitterX + IusCLSplitter.this.getWidth();
			int moved = newLeft - prevControl.getLeft();
			prevControl.setWidth(prevControl.getWidth() - moved);
			prevControl.setLeft(newLeft);
		}

		/* Top */
		if (IusCLSplitter.this.getAlign() == IusCLAlign.alTop) {
			prevControl.setHeight(splitterY - prevControl.getTop());
		}

		/* Bottom */
		if (IusCLSplitter.this.getAlign() == IusCLAlign.alBottom) {
			int newTop = splitterY + IusCLSplitter.this.getHeight();
			int moved = newTop - prevControl.getTop();
			prevControl.setHeight(prevControl.getHeight() - moved);
			prevControl.setTop(newTop);
		}

		// doParentAlignControls();
	}

	@Override
	public void setAlign(IusCLAlign align) {
		super.setAlign(align);

		if ((this.getAlign() == IusCLAlign.alLeft) || (this.getAlign() == IusCLAlign.alRight)) {
			setCursor(IusCLPredefinedCursors.crHSplit.name());
		} else if ((this.getAlign() == IusCLAlign.alTop) || (this.getAlign() == IusCLAlign.alBottom)) {
			setCursor(IusCLPredefinedCursors.crVSplit.name());
		} else {
			setCursor(IusCLPredefinedCursors.crArrow.name());
		}
	}

	// public boolean getAutoSnap() {
	// return autoSnap;
	// }
	//
	// public void setAutoSnap(boolean autoSnap) {
	// this.autoSnap = autoSnap;
	// }
	//
	// public boolean getBeveled() {
	// return beveled;
	// }
	//
	// public void setBeveled(boolean beveled) {
	// this.beveled = beveled;
	// }
}
