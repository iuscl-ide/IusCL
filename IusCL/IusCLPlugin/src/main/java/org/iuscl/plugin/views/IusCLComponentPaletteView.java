/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.views;

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.iuscl.plugin.ide.IusCLDesignIDE;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLComponentPaletteView extends ViewPart {

	public static final String ID = "org.iuscl.plugin.views.IusCLComponentPaletteView";

	/* Toolbar images */
	Image swtImageCursor = IusCLDesignIDE.loadImageFromResource("IusCLActionPaletteCursor.gif");

	Action actionCursor;

	PaletteViewer paletteViewer;

	public IusCLComponentPaletteView() {
		/*  */
	}

	public void createPartControl(Composite parent) {
		makeActions();
		contributeToActionBars();

		paletteViewer = IusCLDesignIDE.createPaletteViewer(parent, actionCursor);
	}

	private void makeActions() {
		/* Action cursor */
		actionCursor = new Action() {

			@Override
			public void run() {
				IusCLDesignIDE.resetPaletteComponent();
				actionCursor.setChecked(true);
			}
		};
		actionCursor.setText(null);
		actionCursor.setToolTipText(null);
		actionCursor.setImageDescriptor(ImageDescriptor.createFromImage(swtImageCursor));
		actionCursor.setDisabledImageDescriptor(ImageDescriptor.createFromImage(swtImageCursor));
		actionCursor.setEnabled(true);
		actionCursor.setChecked(true);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(actionCursor);
	}

	public void setFocus() {
		paletteViewer.getControl().setFocus();
	}
}
