/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.extctrls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.graphics.IusCLFont;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLRadioGroup extends IusCLWinControl {

	Group swtRadioGroup = null;

	@Getter
	IusCLStrings items = new IusCLStrings();
	@Getter
	Integer itemIndex = -1;
	@Getter
	Integer columns = 1;

	final List<Button> swtRadioButtons = new ArrayList<>();

	public IusCLRadioGroup(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Items", IusCLPropertyType.ptStrings, "");
		defineProperty("ItemIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Columns", IusCLPropertyType.ptInteger, "1");

		swtRadioGroup = new Group(this.getFormSwtComposite(), SWT.SHADOW_NONE);
		createWnd(swtRadioGroup);

		/* Layout */
		swtRadioGroup.setLayout(new Layout() {

			@Override
			protected void layout(Composite composite, boolean flushCache) {
				if (swtRadioButtons.isEmpty()) {
					return;
				}

				Rectangle swtClientRectangle = composite.getClientArea();

				int margin = 6; // Ochiometric

				int clientLeft = swtClientRectangle.x + margin;
				int clientTop = swtClientRectangle.y + margin;

				int clientWidth = swtClientRectangle.width - 2 * margin;
				int clientHeight = swtClientRectangle.height - 2 * margin;

				int buttonsPerCol = (swtRadioButtons.size() + columns - 1) / columns;
				int buttonWidth = clientWidth / columns;
				int buttonHeight = clientHeight / buttonsPerCol;

				for (int index = 0; index < swtRadioButtons.size(); index++) {
					Button swtRadioButton = swtRadioButtons.get(index);

					int x = (index / buttonsPerCol) * buttonWidth + clientLeft;
					int y = (index % buttonsPerCol) * buttonHeight + clientTop;

					swtRadioButton.setBounds(x, y, buttonWidth, buttonHeight);
				}
			}

			@Override
			protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
				/* For '.pack()' */
				return null;
			}
		});
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("105");
		setHeight(105);
		this.getProperty("Width").setDefaultValue("185");
		setWidth(185);
	}

	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		swtRadioGroup.setText(caption);
	}

	public void setItems(IusCLStrings items) {
		this.items = items;

		for (int index = 0; index < items.size(); index++) {
			Button swtRadioButton = null;

			if (swtRadioButtons.size() > index) {
				swtRadioButton = swtRadioButtons.get(index);
			} else {
				swtRadioButton = new Button(swtRadioGroup, SWT.RADIO);

				swtRadioButton.setFont(this.getFont().getSwtFont());
				swtRadioButton.setBackground(null);

				swtRadioButton.setData(index);

				final Button swtRadioButtonL = swtRadioButton;

				swtRadioButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
					if (swtRadioButtonL.getSelection()) {
						itemIndex = (Integer) swtRadioButtonL.getData();

						if (IusCLEvent.isDefinedEvent(IusCLRadioGroup.this.getOnClick())) {
							IusCLRadioGroup.this.getOnClick().invoke(IusCLRadioGroup.this);
						}
					}
				}));

				swtRadioButtons.add(swtRadioButton);
			}

			swtRadioButton.setText(items.get(index));
			if (index == itemIndex) {
				swtRadioButton.setSelection(true);
			}
		}

		swtRadioGroup.layout();
	}

	public void setItemIndex(Integer itemIndex) {
		if ((swtRadioButtons.size() > this.itemIndex) && (this.itemIndex > -1)) {
			swtRadioButtons.get(this.itemIndex).setSelection(false);
		}

		this.itemIndex = itemIndex;

		if ((swtRadioButtons.size() > itemIndex) && (itemIndex > -1)) {
			swtRadioButtons.get(itemIndex).setSelection(true);
		}
	}

	@Override
	public void setFont(IusCLFont font) {
		super.setFont(font);

		for (int index = 0; index < swtRadioButtons.size(); index++) {
			Button swtRadioButton = swtRadioButtons.get(index);
			swtRadioButton.setFont(font.getSwtFont());
		}

		swtRadioGroup.layout();
	}

	public void setColumns(Integer columns) {
		this.columns = columns;

		swtRadioGroup.layout();
	}
}
