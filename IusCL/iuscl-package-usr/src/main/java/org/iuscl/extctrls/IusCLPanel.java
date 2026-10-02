/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.graphics.IusCLCanvas;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.sysutils.IusCLGraphUtils;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPanel extends IusCLContainerControl {

	public enum IusCLPanelBevel {
		bvNone, bvLowered, bvRaised, bvSpace
	}

	Composite swtPanelComposite = null;

	@Getter
	IusCLBorderStyle borderStyle = IusCLBorderStyle.bsNone;

	@Getter
	IusCLPanelBevel bevelInner = IusCLPanelBevel.bvNone;
	@Getter
	IusCLPanelBevel bevelOuter = IusCLPanelBevel.bvRaised;
	@Getter
	Integer bevelWidth = 1;

	@Getter
	boolean systemBevelColors = true;

	public IusCLPanel(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("BorderStyle", IusCLPropertyType.ptEnum, "bsNone", IusCLBorderStyle.bsNone);

		defineProperty("BevelInner", IusCLPropertyType.ptEnum, "bvNone", IusCLPanelBevel.bvNone);
		defineProperty("BevelOuter", IusCLPropertyType.ptEnum, "bvRaised", IusCLPanelBevel.bvRaised);
		defineProperty("BevelWidth", IusCLPropertyType.ptInteger, "1");

		defineProperty("SystemBevelColors", IusCLPropertyType.ptBoolean, "true");

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		if (borderStyle == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		swtPanelComposite = new Composite(this.getFormSwtComposite(), swtCreateParams);

		return swtPanelComposite;
	}

	@Override
	protected void create() {
		super.create();

		this.removeProperty("Caption");

		this.getProperty("Height").setDefaultValue("41");
		setHeight(41);
		this.getProperty("Width").setDefaultValue("185");
		setWidth(185);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtPanelComposite.addPaintListener(swtPaintEvent -> {
			boolean drawOuter = (bevelOuter == IusCLPanelBevel.bvRaised) || (bevelOuter == IusCLPanelBevel.bvLowered);

			boolean drawInner = (bevelInner == IusCLPanelBevel.bvRaised) || (bevelInner == IusCLPanelBevel.bvLowered);

			if (drawOuter || drawInner) {
				IusCLCanvas canvas = new IusCLCanvas(swtPaintEvent.gc);

				IusCLColor colorHighlight = new IusCLColor(IusCLStandardColors.clBtnHighlight);
				IusCLColor colorShadow = new IusCLColor(IusCLStandardColors.clBtnShadow);

				if (!systemBevelColors) {
					colorHighlight = IusCLGraphUtils.getHighlightColor(getColor());
					colorShadow = IusCLGraphUtils.getShadowColor(getColor());
				}

				IusCLColor color1 = colorHighlight;
				IusCLColor color2 = colorShadow;

				Integer bevelLeftValue = 0;
				Integer bevelTopValue = 0;
				Integer bevelWidthValue = swtPanelComposite.getClientArea().width - 1;
				Integer bevelHeightValue = swtPanelComposite.getClientArea().height - 1;
				Integer bevelBorderValue = IusCLPanel.this.bevelWidth;

				if (drawOuter) {
					if (bevelOuter == IusCLPanelBevel.bvLowered) {
						color1 = colorShadow;
						color2 = colorHighlight;
					}

					drawBevel(canvas, color1, color2, bevelLeftValue, bevelTopValue, bevelWidthValue, bevelHeightValue, bevelBorderValue);

					bevelLeftValue = bevelLeftValue + bevelBorderValue;
					bevelTopValue = bevelTopValue + bevelBorderValue;
					bevelWidthValue = bevelWidthValue - bevelBorderValue;
					bevelHeightValue = bevelHeightValue - bevelBorderValue;
				}

				if (drawInner) {
					color1 = colorHighlight;
					color2 = colorShadow;
					if (bevelInner == IusCLPanelBevel.bvLowered) {
						color1 = colorShadow;
						color2 = colorHighlight;
					}

					Integer borderWidth = getBorderWidth();

					bevelLeftValue = bevelLeftValue + borderWidth;
					bevelTopValue = bevelTopValue + borderWidth;
					bevelWidthValue = bevelWidthValue - borderWidth;
					bevelHeightValue = bevelHeightValue - borderWidth;

					drawBevel(canvas, color1, color2, bevelLeftValue, bevelTopValue, bevelWidthValue, bevelHeightValue, bevelBorderValue);
				}
			}
		});
	}

	private void drawBevel(IusCLCanvas canvas, IusCLColor color1, IusCLColor color2, Integer bevelLeft, Integer bevelTop, Integer bevelWidth,
			Integer bevelHeight, Integer bevelBorder) {
		for (int border = 0; border < bevelBorder; border++) {
			canvas.getPen().setColor(color1);
			canvas.moveTo(bevelLeft, bevelTop);
			canvas.lineTo(bevelWidth - 1, bevelTop);
			canvas.moveTo(bevelLeft, bevelTop);
			canvas.lineTo(bevelLeft, bevelHeight);

			canvas.getPen().setColor(color2);
			canvas.moveTo(bevelLeft + 1, bevelHeight);
			canvas.lineTo(bevelWidth, bevelHeight);
			canvas.lineTo(bevelWidth, bevelTop);

			bevelLeft = bevelLeft + 1;
			bevelTop = bevelTop + 1;

			bevelWidth = bevelWidth - 1;
			bevelHeight = bevelHeight - 1;
		}
	}

	@Override
	public Composite getSwtComposite() {
		return this.swtPanelComposite;
	}

	public void setBorderStyle(IusCLBorderStyle borderStyle) {
		if (this.borderStyle != borderStyle) {
			this.borderStyle = borderStyle;

			reCreateWnd();
		}
	}

	@Override
	public void setBorderWidth(Integer borderWidth) {
		super.setBorderWidth(borderWidth);
		swtPanelComposite.redraw();
	}

	@Override
	public Integer getContainerBorderWidth() {
		int borderWidth = super.getContainerBorderWidth();

		if (bevelInner != IusCLPanelBevel.bvNone) {
			borderWidth = borderWidth + bevelWidth;
		}
		if (bevelOuter != IusCLPanelBevel.bvNone) {
			borderWidth = borderWidth + bevelWidth;
		}

		return borderWidth;
	}

	public void setBevelWidth(Integer bevelWidth) {
		this.bevelWidth = bevelWidth;

		doAlignControls(new IusCLSize(0, 0));
		swtPanelComposite.redraw();
	}

	public void setBevelInner(IusCLPanelBevel bevelInner) {
		boolean needsClientResize = needsClientResize(bevelInner, this.bevelInner);

		this.bevelInner = bevelInner;

		swtPanelComposite.redraw();

		if (needsClientResize) {
			doAlignControls(new IusCLSize(0, 0));
		}
	}

	public void setBevelOuter(IusCLPanelBevel bevelOuter) {
		boolean needsClientResize = needsClientResize(bevelOuter, this.bevelOuter);

		this.bevelOuter = bevelOuter;

		swtPanelComposite.redraw();

		if (needsClientResize) {
			doAlignControls(new IusCLSize(0, 0));
		}
	}

	public void setSystemBevelColors(boolean systemBevelColors) {
		this.systemBevelColors = systemBevelColors;

		swtPanelComposite.redraw();
	}

	private boolean needsClientResize(IusCLPanelBevel panelBevel, IusCLPanelBevel oldPanelBevel) {
		if ((panelBevel == IusCLPanelBevel.bvNone) && (oldPanelBevel != IusCLPanelBevel.bvNone)) {
			return true;
		}
		return (panelBevel != IusCLPanelBevel.bvNone) && (oldPanelBevel == IusCLPanelBevel.bvNone);
	}
}
