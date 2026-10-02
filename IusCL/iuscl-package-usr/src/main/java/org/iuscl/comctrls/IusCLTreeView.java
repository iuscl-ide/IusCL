/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.events.IusCLEvent;
import org.iuscl.events.IusCLTreeViewCollapsedEvent;
import org.iuscl.events.IusCLTreeViewExpandedEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTreeView extends IusCLWinControl {

	Tree swtTree = null;

	@Getter
	IusCLBorderStyle borderStyle = IusCLBorderStyle.bsSingle;
	@Getter
	@Setter
	IusCLImageList images = null;
	@Getter
	boolean showLines = false;
	@Getter
	boolean multiSelect = false;
	@Getter
	boolean rowSelect = false;

	@Getter
	@Setter
	IusCLTreeViewExpandedEvent onExpanded = null;
	@Getter
	@Setter
	IusCLTreeViewCollapsedEvent onCollapsed = null;

	@Getter
	IusCLTreeNodes rootTreeNodes = new IusCLTreeNodes(this);

	public IusCLTreeView(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("BorderStyle", IusCLPropertyType.ptEnum, "bsSingle", IusCLBorderStyle.bsSingle);
		defineProperty("Images", IusCLPropertyType.ptComponent, "", IusCLImageList.class);
		defineProperty("ShowLines", IusCLPropertyType.ptBoolean, "false");
		defineProperty("MultiSelect", IusCLPropertyType.ptBoolean, "false");
		defineProperty("RowSelect", IusCLPropertyType.ptBoolean, "false");

		defineProperty("OnExpanded", IusCLPropertyType.ptEvent, null, IusCLTreeViewExpandedEvent.class);
		defineProperty("OnCollapsed", IusCLPropertyType.ptEvent, null, IusCLTreeViewCollapsedEvent.class);

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.NONE;

		if (borderStyle == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		if (multiSelect) {
			swtCreateParams = swtCreateParams | SWT.MULTI;
		} else {
			swtCreateParams = swtCreateParams | SWT.SINGLE;
		}

		if (rowSelect) {
			swtCreateParams = swtCreateParams | SWT.FULL_SELECTION;
		}

		swtTree = new Tree(this.getFormSwtComposite(), swtCreateParams);

		swtTree.addTreeListener(TreeListener.treeExpandedAdapter(swtTreeEvent -> {
			TreeItem swtTreeItem = (TreeItem) swtTreeEvent.item;
			expand((IusCLTreeNode) swtTreeItem.getData());
		}));

		swtTree.addTreeListener(TreeListener.treeCollapsedAdapter(swtTreeEvent -> {
			TreeItem swtTreeItem = (TreeItem) swtTreeEvent.item;
			collapse((IusCLTreeNode) swtTreeItem.getData());
		}));

		return swtTree;
	}

	@Override
	public void reCreateWnd() {
		if (this.getIsLoading()) {
			/* recreate at the end of loading */
			return;
		}

		super.reCreateWnd();

		/* Nodes */
		for (int index = 0; index < rootTreeNodes.getCount(); index++) {
			IusCLTreeNode rootTreeNode = rootTreeNodes.get(index);

			TreeItem swtTreeItem = new TreeItem(swtTree, SWT.NONE, index);
			rootTreeNode.setSwtTreeItem(swtTreeItem);
			rootTreeNode.assign();

			recursiveRecreateNodes(rootTreeNode.getChildNodes());
		}
	}

	private void recursiveRecreateNodes(IusCLTreeNodes treeNodes) {
		IusCLTreeNode parentTreeNode = treeNodes.getParentNode();

		for (int index = 0; index < treeNodes.getCount(); index++) {
			IusCLTreeNode treeNode = treeNodes.get(index);

			TreeItem swtTreeItem = new TreeItem(parentTreeNode.getSwtTreeItem(), SWT.NONE, index);
			treeNode.setSwtTreeItem(swtTreeItem);
			treeNode.assign();

			recursiveRecreateNodes(treeNode.getChildNodes());
		}
	}

	public void expand(IusCLTreeNode treeNode) {
		treeNode.setExpanded(true);

		if (IusCLEvent.isDefinedEvent(onExpanded)) {
			onExpanded.invoke(IusCLTreeView.this, treeNode);
		}
	}

	public void collapse(IusCLTreeNode treeNode) {
		treeNode.setExpanded(false);

		if (IusCLEvent.isDefinedEvent(onCollapsed)) {
			onCollapsed.invoke(IusCLTreeView.this, treeNode);
		}
	}

	@Override
	protected void create() {
		super.create();

		this.getProperty("Height").setDefaultValue("97");
		setHeight(97);
		this.getProperty("Width").setDefaultValue("121");
		setWidth(121);
	}

	public List<IusCLTreeNode> getAllTreeNodes() {
		final List<IusCLTreeNode> treeNodes = new ArrayList<>();

		for (int index = 0; index < rootTreeNodes.getCount(); index++) {
			IusCLTreeNode rootTreeNode = rootTreeNodes.get(index);
			treeNodes.add(rootTreeNode);
			recursiveAddNodes(treeNodes, rootTreeNode);
		}

		return treeNodes;
	}

	private void recursiveAddNodes(List<IusCLTreeNode> treeNodes, IusCLTreeNode addTreeNode) {
		for (int index = 0; index < addTreeNode.getChildNodes().getCount(); index++) {
			IusCLTreeNode childTreeNode = addTreeNode.getChildNodes().get(index);
			treeNodes.add(childTreeNode);
			recursiveAddNodes(treeNodes, childTreeNode);
		}
	}

	public IusCLTreeNode getSelectedNode() {
		IusCLTreeNode selectedTreeNode = null;

		if (swtTree.getSelection().length > 0) {
			selectedTreeNode = (IusCLTreeNode) (swtTree.getSelection()[0].getData());
		}

		return selectedTreeNode;
	}

	public List<IusCLTreeNode> getSelectedNodes() {
		final List<IusCLTreeNode> selectedTreeNodes = new ArrayList<>();

		for (int index = 0; index < swtTree.getSelection().length; index++) {
			selectedTreeNodes.add((IusCLTreeNode) (swtTree.getSelection()[index].getData()));
		}

		return selectedTreeNodes;
	}

	public Integer getSelectionCount() {
		return swtTree.getSelectionCount();
	}

	public void selectNode(IusCLTreeNode node) {
		swtTree.select(node.getSwtTreeItem());
	}

	public IusCLTreeNode getFirstVisibleTreeNode() {
		return (IusCLTreeNode) (swtTree.getTopItem().getData());
	}

	public void setFirstVisibleTreeNode(IusCLTreeNode treeNode) {
		swtTree.setTopItem(treeNode.getSwtTreeItem());
	}

	public void setBorderStyle(IusCLBorderStyle borderStyle) {
		if (this.borderStyle != borderStyle) {
			this.borderStyle = borderStyle;

			reCreateWnd();
		}
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;

		swtTree.setLinesVisible(showLines);
	}

	public void setMultiSelect(boolean multiSelect) {
		if (this.multiSelect != multiSelect) {
			this.multiSelect = multiSelect;

			reCreateWnd();
		}
	}

	public void setRowSelect(boolean rowSelect) {
		if (this.rowSelect != rowSelect) {
			this.rowSelect = rowSelect;
			reCreateWnd();
		}
	}
}
