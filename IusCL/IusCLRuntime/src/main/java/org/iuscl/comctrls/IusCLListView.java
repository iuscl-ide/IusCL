/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLMultiSelectListControl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListView extends IusCLMultiSelectListControl {

	// AllocBy
	// BoundingRect
	// Checkboxes
	// Column
	// ColumnClick
	// DropTarget
	// FlatScrollBars
	// FullDrag
	// HotTrack
	// HotTrackStyles
	// HoverTime
	// IconOptions
	// ItemFocused
	// LargeImages
	// OwnerData
	// OwnerDraw
	// ReadOnly
	// ShowWorkAreas
	// SortType
	// StateImages
	// ViewOrigin
	// ViewStyle
	// VisibleRowCount
	// WorkAreas

	Table swtTable = null;

	@Getter
	@Setter
	IusCLListColumns columns = new IusCLListColumns(this);
	@Getter
	boolean showColumnHeaders = true;
	@Getter
	boolean rowSelect = true;
	@Getter
	boolean hideSelection = true;
	@Getter
	boolean gridLines = false;

	@Getter
	IusCLImageList headerImages = null;
	@Getter
	@Setter
	IusCLImageList smallImages = null;

	@Getter
	IusCLListItems listItems = new IusCLListItems(this);

	public IusCLListView(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Columns", IusCLPropertyType.ptCollection, "", false);

		defineProperty("ShowColumnHeaders", IusCLPropertyType.ptBoolean, "true");
		defineProperty("RowSelect", IusCLPropertyType.ptBoolean, "true");
		defineProperty("HideSelection", IusCLPropertyType.ptBoolean, "true");
		defineProperty("GridLines", IusCLPropertyType.ptBoolean, "false");

		defineProperty("HeaderImages", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
		defineProperty("SmallImages", IusCLPropertyType.ptComponent, "", IusCLImageList.class);

		/* Collection */
		this.setSubCollection(columns);
		columns.setPropertyName("Columns");

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		if (this.getMultiSelect()) {
			swtCreateParams = swtCreateParams | SWT.MULTI;
		} else {
			swtCreateParams = swtCreateParams | SWT.SINGLE;
		}

		if (rowSelect) {
			swtCreateParams = swtCreateParams | SWT.FULL_SELECTION;
		}

		if (hideSelection) {
			swtCreateParams = swtCreateParams | SWT.HIDE_SELECTION;
		}

		swtTable = new Table(this.getFormSwtComposite(), swtCreateParams);

		swtTable.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> itemIndex = swtTable.getSelectionIndex()));

		return swtTable;
	}

	@Override
	public void reCreateWnd() {
		if (this.getIsLoading()) {
			/* recreate at the end of loading */
			return;
		}

		/* Save columns width */
		for (int index = 0; index < columns.getCount(); index++) {
			IusCLListColumn listColumn = columns.get(index);
			listColumn.setWidth(swtTable.getColumn(index).getWidth());
		}

		super.reCreateWnd();

		/* Columns */
		for (int index = 0; index < columns.getCount(); index++) {
			IusCLListColumn listColumn = columns.get(index);
			TableColumn swtTableColumn = new TableColumn(swtTable, SWT.NONE, index);
			listColumn.setSwtTableColumn(swtTableColumn);
			listColumn.assign();
		}

		/* Items */
		for (int index = 0; index < listItems.getCount(); index++) {
			IusCLListItem listItem = listItems.get(index);
			TableItem swtTableItem = new TableItem(swtTable, SWT.NONE, index);
			listItem.setSwtTableItem(swtTableItem);
			listItem.assign();

			IusCLListSubItems listSubItems = listItem.getSubItems();

			for (int subIndex = 0; subIndex < listSubItems.size(); subIndex++) {
				listSubItems.setCaption(subIndex, listSubItems.getCaption(subIndex));
				listSubItems.setImageIndex(subIndex, listSubItems.getImageIndex(subIndex));
			}
		}
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("150");
		setHeight(150);
		this.getProperty("Width").setDefaultValue("250");
		setWidth(250);
	}

	public void setShowColumnHeaders(boolean showColumnHeaders) {
		this.showColumnHeaders = showColumnHeaders;

		swtTable.setHeaderVisible(showColumnHeaders);
	}

	public void setHeaderImages(IusCLImageList headerImages) {
		this.headerImages = headerImages;

		for (int index = 0; index < columns.getCount(); index++) {
			IusCLListColumn listColumn = columns.get(index);
			listColumn.setImageIndex(listColumn.getImageIndex());
		}
	}

	public void setRowSelect(boolean rowSelect) {
		if (this.rowSelect != rowSelect) {
			this.rowSelect = rowSelect;

			reCreateWnd();
		}
	}

	public void setHideSelection(boolean hideSelection) {
		if (this.hideSelection != hideSelection) {
			this.hideSelection = hideSelection;

			reCreateWnd();
		}
	}

	@Override
	public void setItemIndex(Integer itemIndex) {
		super.setItemIndex(itemIndex);

		swtTable.select(itemIndex);
	}

	@Override
	public Integer getSelCount() {
		return swtTable.getSelectionCount();
	}

	@Override
	public boolean getSelection(Integer index) {
		return swtTable.isSelected(index);
	}

	@Override
	public void setSelection(Integer index, boolean selected) {
		if (selected) {
			swtTable.select(index);
		} else {
			swtTable.deselect(index);
		}
	}

	@Override
	public void setSelection(Integer index, Integer length, boolean selected) {
		if (selected) {
			swtTable.select(index, index + length);
		} else {
			swtTable.deselect(index, index + length);
		}
	}

	@Override
	public Integer getFirstVisibleIndex() {
		return swtTable.getTopIndex();
	}

	@Override
	public void setFirstVisibleIndex(Integer topIndex) {
		swtTable.setTopIndex(topIndex);
	}

	public void setGridLines(boolean gridLines) {
		this.gridLines = gridLines;

		swtTable.setLinesVisible(gridLines);
	}
}
