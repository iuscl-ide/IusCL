/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLCollectionItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTreeNode extends IusCLCollectionItem {

	@Getter
	TreeItem swtTreeItem = null;

	@Getter
	String caption = "";
	@Getter
	Integer imageIndex = -1;
	@Getter
	IusCLTreeNodes childNodes = null;
	@Getter
	IusCLTreeView treeView = null;
	@Getter
	boolean expanded = false;

	public IusCLTreeNode(IusCLTreeNodes treeNodes) {
		this(treeNodes, null);
	}

	public IusCLTreeNode(IusCLTreeNodes treeNodes, Integer index) {
		super(treeNodes);

		defineProperty("Caption", IusCLPropertyType.ptString, "");
		defineProperty("ImageIndex", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Expanded", IusCLPropertyType.ptBoolean, "false");

		treeView = treeNodes.getPropertyComponent();
		childNodes = new IusCLTreeNodes(this);

		if (treeNodes.getParentNode() == null) {
			Tree swtTree = (Tree) treeView.getSwtControl();
			if (index == null) {
				swtTreeItem = new TreeItem(swtTree, SWT.NONE);
			} else {
				swtTreeItem = new TreeItem(swtTree, SWT.NONE, index);
			}
		} else {
			if (index == null) {
				swtTreeItem = new TreeItem(treeNodes.getParentNode().getSwtTreeItem(), SWT.NONE);
			} else {
				swtTreeItem = new TreeItem(treeNodes.getParentNode().getSwtTreeItem(), SWT.NONE, index);
			}
		}
		swtTreeItem.setData(this);
		assign();
	}

	public IusCLTreeNodes getParentTreeNodes() {
		return (IusCLTreeNodes) getCollection();
	}

	@Override
	public void free() {
		super.free();

		if (swtTreeItem != null) {
			swtTreeItem.dispose();
		}
	}

	public IusCLTreeNode getParentNode() {
		return getParentTreeNodes().getParentNode();
	}

	public void setCaption(String caption) {
		this.caption = caption;

		swtTreeItem.setText(caption);
	}

	public void setImageIndex(Integer imageIndex) {
		this.imageIndex = imageIndex;

		if (imageIndex == -1) {
			swtTreeItem.setImage((Image) null);
			return;
		}

		if (swtTreeItem != null) {
			IusCLImageList images = treeView.getImages();
			if (images != null) {
				Image swtImage = images.getAsSizedSwtImage(imageIndex);
				swtTreeItem.setImage(swtImage);
			} else {
				swtTreeItem.setImage((Image) null);
			}
		}
	}

	public void setSwtTreeItem(TreeItem swtTreeItem) {
		this.swtTreeItem = swtTreeItem;

		this.swtTreeItem.setData(this);
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;

		swtTreeItem.setExpanded(expanded);
	}
}
