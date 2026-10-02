/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.events;

import org.iuscl.comctrls.IusCLTreeNode;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;

public class IusCLTreeViewCollapsedEvent extends IusCLEvent {

	public void invoke(IusCLObject sender, IusCLTreeNode treeNode) {
		super.invoke(new IusCLParam(IusCLObject.class, sender), new IusCLParam(IusCLTreeNode.class, treeNode));
	}
}
