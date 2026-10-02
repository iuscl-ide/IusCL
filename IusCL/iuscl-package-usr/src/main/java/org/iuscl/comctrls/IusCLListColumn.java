/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.iuscl.classes.IusCLCollectionItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLListColumn extends IusCLCollectionItem {

	public enum IusCLListColumnAlignment {
		caLeftJustify, caRightJustify, caCenter
	}

	@Getter
	@Setter
	TableColumn swtTableColumn = null;

	@Getter
	String caption = "";
	@Getter
	Integer width = 50;
	@Getter
	Integer imageIndex = -1;
	@Getter
	IusCLListColumnAlignment alignment = IusCLListColumnAlignment.caLeftJustify;
	@Getter
	boolean allowResize = true;

	public IusCLListColumn(IusCLListColumns listColumns) {
		this(listColumns, null);
	}

	public IusCLListColumn(IusCLListColumns listColumns, Integer index) {
		super(listColumns, index);

		defineProperty("Caption", IusCLPropertyType.ptString, "");
		defineProperty("Width", IusCLPropertyType.ptInteger, "50");
		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Alignment", IusCLPropertyType.ptEnum, "caLeftJustify", IusCLListColumnAlignment.caLeftJustify);
		defineProperty("AllowResize", IusCLPropertyType.ptBoolean, "true");

		IusCLListView listView = listColumns.getListView();
		Table swtTable = (Table) listView.getSwtControl();

		if (index == null) {
			swtTableColumn = new TableColumn(swtTable, SWT.NONE);
		} else {
			swtTableColumn = new TableColumn(swtTable, SWT.NONE, index);
		}
		assign();
	}

	public IusCLListColumns getListColumns() {
		return (IusCLListColumns) getCollection();
	}

	@Override
	public void free() {
		super.free();

		if (swtTableColumn != null) {
			swtTableColumn.dispose();
		}
	}

	@Override
	public String getDisplayName() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;

		if (swtTableColumn != null) {
			swtTableColumn.setText(caption);
		}
	}

	public void setWidth(Integer width) {
		this.width = width;

		if (swtTableColumn != null) {
			swtTableColumn.setWidth(width);
		}
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (imageIndex == -1) {
			swtTableColumn.setImage(null);
			return;
		}

		IusCLListColumns listColumns = getListColumns();
		IusCLListView listView = listColumns.getPropertyComponent();

		if (swtTableColumn != null) {
			IusCLImageList images = listView.getHeaderImages();
			if (images != null) {
				Image swtImage = images.getAsSizedSwtImage(imageIndex);
				swtTableColumn.setImage(swtImage);
			} else {
				swtTableColumn.setImage(null);
			}
		}
	}

	public void setAlignment(IusCLListColumnAlignment alignment) {
		this.alignment = alignment;

		if (swtTableColumn != null) {
			switch (alignment) {
			case caCenter:
				swtTableColumn.setAlignment(SWT.CENTER);
				break;
			case caLeftJustify:
				swtTableColumn.setAlignment(SWT.LEFT);
				break;
			case caRightJustify:
				swtTableColumn.setAlignment(SWT.RIGHT);
				break;
			}
		}
	}

	public void setAllowResize(boolean allowResize) {
		this.allowResize = allowResize;

		if (swtTableColumn != null) {
			swtTableColumn.setResizable(allowResize);
		}
	}
}
