/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import org.iuscl.classes.IusCLCollection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLTreeNodes extends IusCLCollection {

	@Getter
	IusCLTreeNode parentNode = null;

	public IusCLTreeNodes(IusCLTreeView treeView) {
		super(treeView);
	}

	public IusCLTreeNodes(IusCLTreeNode parentTreeNode) {
		super(parentTreeNode.getTreeView());

		this.parentNode = parentTreeNode;
	}

	@Override
	public IusCLTreeView getPropertyComponent() {
		return (IusCLTreeView) super.getPropertyComponent();
	}

	public IusCLTreeView getTreeView() {
		return getPropertyComponent();
	}

	@Override
	public Class<?> getItemClass() {
		return IusCLTreeNode.class;
	}

	@Override
	public IusCLTreeNode add() {
		return new IusCLTreeNode(this);
	}

	@Override
	public IusCLTreeNode insert(int index) {
		return new IusCLTreeNode(this, index);
	}

	@Override
	public IusCLTreeNode get(int index) {
		return (IusCLTreeNode) super.get(index);
	}

	public Integer indexOf(IusCLTreeNode treeNode) {
		return super.indexOf(treeNode);
	}
}
