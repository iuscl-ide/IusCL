/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.iuscl.classes.IusCLCollection;
import org.iuscl.classes.IusCLCollectionItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLControl;
import org.iuscl.controls.IusCLGraphicControl;
import org.iuscl.controls.IusCLParentControl;
import org.iuscl.designintf.componenteditors.IusCLDesignComponentEditor;
import org.iuscl.designintf.componenteditors.IusCLDesignComponentEditorVerb;
import org.iuscl.designintf.componenteditors.IusCLDesignSelection;
import org.iuscl.forms.IusCLForm;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.plugin.ide.IusCLDesignIDE;
import org.iuscl.plugin.ide.IusCLDesignIDE.IusCLDesignIDEState;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLObjectTreeViewView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.iuscl.plugin.views.IusCLObjectTreeViewView";

	/* Toolbar images */
	Image imageNewItem = IusCLDesignIDE.loadImageFromResource("IusCLActionNewItem.gif");
	Image imageNewItemDisabled = IusCLDesignIDE.loadImageFromResource("IusCLActionNewItem_disabled.gif");
	Image imageDelete = IusCLDesignIDE.loadImageFromResource("IusCLActionDelete.gif");
	Image imageDeleteDisabled = IusCLDesignIDE.loadImageFromResource("IusCLActionDelete_disabled.gif");
	Image imageMoveUp = IusCLDesignIDE.loadImageFromResource("IusCLActionMoveUp.gif");
	Image imageMoveUpDisabled = IusCLDesignIDE.loadImageFromResource("IusCLActionMoveUp_disabled.gif");
	Image imageMoveDown = IusCLDesignIDE.loadImageFromResource("IusCLActionMoveDown.gif");
	Image imageMoveDownDisabled = IusCLDesignIDE.loadImageFromResource("IusCLActionMoveDown_disabled.gif");

	/* Tree images */
	Image imageForm = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewForm.gif");
	Image imageControl = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewControl.gif");
	Image imageComponent = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewComponent.gif");
	Image imageGraphicControl = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewGraphic.gif");
	Image imageContainer = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewContainer.gif");

	// private Image imageSubForm =
	// IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewSubForm.gif");
	Image imageSubControl = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewSubControl.gif");
	Image imageSubComponent = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewSubComponent.gif");
	Image imageSubGraphicControl = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewSubGraphic.gif");
	Image imageSubContainer = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewSubContainer.gif");

	Image imageCollection = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewCollection.gif");
	Image imageCollectionItem = IusCLDesignIDE.loadImageFromResource("IusCLObjectTreeViewCollectionItem.gif");

	Action actionNewItem;
	Action actionDelete;
	Action actionMoveUp;
	Action actionMoveDown;

	@Getter
	@Setter
	IusCLForm designForm;
	@Getter
	IusCLDesignSelection designSelection;

	Tree objectsTree;

	int itemHeight = 0;

	public void setDesignSelection(IusCLDesignSelection designSelection) {
		this.designSelection = designSelection;

		if (designSelection == null) {
			return;
		}

		selectDesignTreeItem();
	}

	private void selectDesignTreeItem() {
		if (designSelection.getDesignComponent() == null) {
			return;
		}

		IusCLPersistent designPersistent = designSelection.findPersistent();

		TreeItem treeItemSelected = recursiveFindTreeItem(designPersistent, objectsTree.getItems()[0]);
		objectsTree.select(treeItemSelected);
		objectsTree.showSelection();
		findActionsStatus(treeItemSelected);
	}

	private TreeItem recursiveFindTreeItem(IusCLPersistent persistent, TreeItem parentTreeItem) {
		if (parentTreeItem.getData().equals(persistent)) {
			return parentTreeItem;
		}
		for (int index = 0; index < parentTreeItem.getItems().length; index++) {
			TreeItem childTreeItem = parentTreeItem.getItems()[index];
			TreeItem foundTreeItem = recursiveFindTreeItem(persistent, childTreeItem);
			if (foundTreeItem != null) {
				return foundTreeItem;
			}
		}
		return null;
	}

	private Class<?> findDeclaringClass(IusCLComponent ownedComponent, IusCLComponent ownerComponent) {
		String componentFiledName = ownedComponent.getName();

		Class<?> ancestorFormClass = ownerComponent.getClass().getSuperclass();

		while (ancestorFormClass != IusCLComponent.class) {
			try {
				ancestorFormClass.getDeclaredField(componentFiledName);

				return ancestorFormClass;
			} catch (NoSuchFieldException noSuchFieldException) {
				ancestorFormClass = ancestorFormClass.getSuperclass();
			}
		}

		return null;
	}

	private void recursivePutComponents(TreeItem parentTreeItemComponent, IusCLComponent ownerComponent) {
		for (int index = 0; index < ownerComponent.getComponents().size(); index++) {
			IusCLComponent ownedComponent = ownerComponent.getComponents().get(index);

			TreeItem parentTreeItem = parentTreeItemComponent;
			/* Parent component */
			IusCLComponent parentComponent = ownedComponent.getParentComponent();
			if (ownedComponent instanceof IusCLControl ownedControl) {
				/* Parent control */
				parentComponent = ownedControl.getParent();
			}
			parentTreeItem = recursiveFindTreeItem(parentComponent, parentTreeItem);
			TreeItem childTreeItem = new TreeItem(parentTreeItem, 0);
			// childTreeItem.setText(ownedComponent.getName() + " - " +
			// ownedComponent.getClass().getSimpleName());

			Class<?> declaringClass = findDeclaringClass(ownedComponent, ownerComponent);

			if (declaringClass == null) {
				childTreeItem.setText(ownedComponent.getDisplayName() + " - " + ownedComponent.getClass().getSimpleName());
			} else {
				childTreeItem.setText(ownedComponent.getDisplayName() + " - " + ownedComponent.getClass().getSimpleName() + " ("
						+ declaringClass.getSimpleName() + ")");
			}

			childTreeItem.setData(ownedComponent);
			childTreeItem.setImage(findComponentImage(ownedComponent));

			IusCLCollection subCollection = ownedComponent.getSubCollection();

			if (subCollection != null && subCollection.getCount() > 0) {
				TreeItem collectionTreeItem = new TreeItem(childTreeItem, 0);
				collectionTreeItem.setText(subCollection.getPropertyName() + " - " + subCollection.getClass().getSimpleName());
				collectionTreeItem.setData(subCollection);
				collectionTreeItem.setImage(imageCollection);

				for (int indexItem = 0; indexItem < subCollection.getCount(); indexItem++) {
					IusCLCollectionItem subCollectionItem = subCollection.get(indexItem);

					TreeItem collectionItemTreeItem = new TreeItem(collectionTreeItem, 0);
					String displayName = subCollectionItem.getDisplayName();

					if (IusCLStrUtils.isNotNullNotEmpty(displayName)) {
						collectionItemTreeItem.setText(indexItem + " { " + displayName + " } - " + subCollectionItem.getClass().getSimpleName());
					} else {
						collectionItemTreeItem.setText(indexItem + " - " + subCollectionItem.getClass().getSimpleName());
					}
					collectionItemTreeItem.setData(subCollectionItem);
					collectionItemTreeItem.setImage(imageCollectionItem);
				}
			}

			recursivePutComponents(childTreeItem, ownedComponent); // maybe in the future

			if (!ownedComponent.getIsSubComponent()) {
				parentTreeItem.setExpanded(true);
			}
		}
	}

	private Image findComponentImage(IusCLComponent component) {
		if (component instanceof IusCLControl) {
			if (component instanceof IusCLGraphicControl) {
				if (component.getIsSubComponent()) {
					return imageSubGraphicControl;
				}
				return imageGraphicControl;
			} else {
				if (component instanceof IusCLContainerControl) {
					if (component.getIsSubComponent()) {
						return imageSubContainer;
					}
					return imageContainer;
				} else {
					if (component.getIsSubComponent()) {
						return imageSubControl;
					}
					return imageControl;
				}
			}
		} else {
			if (component.getIsSubComponent()) {
				return imageSubComponent;
			}
			return imageComponent;
		}
	}

	public void initObjectTreeView(IusCLForm designForm, IusCLDesignSelection designSelection) {
		objectsTree.removeAll();

		if (designForm == null) {
			return;
		}

		this.designForm = designForm;
		this.designSelection = designSelection;

		TreeItem treeItem = new TreeItem(objectsTree, 0);
		treeItem.setText(designForm.getDisplayName() + " - " + designForm.getClass().getSimpleName());
		treeItem.setData(designForm);
		treeItem.setImage(imageForm);

		recursivePutComponents(treeItem, designForm);

		treeItem.setExpanded(true);

		selectDesignTreeItem();
	}

	public IusCLObjectTreeViewView() {
		/*  */
	}

	public void createPartControl(Composite parent) {
		if (itemHeight == 0) {
			Combo measureCombo = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
			itemHeight = measureCombo.getSize().y;
			measureCombo.dispose();
		}

		Listener objectsTreeListener = swtEvent -> {
			final TreeItem treeItem = (TreeItem) swtEvent.item;
			switch (swtEvent.type) {
			case SWT.MeasureItem:
				swtEvent.height = itemHeight;
				break;
			case SWT.Selection:
				Object treeItemObject = treeItem.getData();

				if (treeItemObject instanceof IusCLCollectionItem collectionItem) {
					designSelection.setDesignComponent((IusCLComponent) treeItem.getParentItem().getParentItem().getData());
					designSelection.setDesignCollection((IusCLCollection) treeItem.getParentItem().getData());
					designSelection.setDesignCollectionItem(collectionItem);
				} else if (treeItemObject instanceof IusCLCollection collection) {
					designSelection.setDesignComponent((IusCLComponent) treeItem.getParentItem().getData());
					designSelection.setDesignCollection(collection);
					designSelection.setDesignCollectionItem(null);
				} else {
					designSelection.setDesignComponent((IusCLComponent) treeItemObject);
					designSelection.setDesignCollection(null);
					designSelection.setDesignCollectionItem(null);
				}
				findActionsStatus(treeItem);
				IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsSelection);
				break;
			default:
				break;
			}
		};

		GridLayout objectTreeViewLayout = new GridLayout(1, false);
		objectTreeViewLayout.marginTop = 0;
		objectTreeViewLayout.marginLeft = 0;
		objectTreeViewLayout.horizontalSpacing = 0;
		objectTreeViewLayout.marginWidth = 0;
		objectTreeViewLayout.marginBottom = 0;
		objectTreeViewLayout.marginHeight = 0;
		objectTreeViewLayout.marginRight = 0;
		objectTreeViewLayout.verticalSpacing = 2;

		parent.setLayout(objectTreeViewLayout);

		objectsTree = new Tree(parent, SWT.READ_ONLY);

		GridData objectsTreeGridData = new GridData();
		objectsTreeGridData.horizontalAlignment = SWT.FILL;
		objectsTreeGridData.grabExcessHorizontalSpace = true;
		objectsTreeGridData.verticalAlignment = SWT.FILL;
		objectsTreeGridData.grabExcessVerticalSpace = true;
		objectsTree.setLayoutData(objectsTreeGridData);
		objectsTree.setHeaderVisible(false);
		objectsTree.addListener(SWT.Selection, objectsTreeListener);
		objectsTree.addListener(SWT.MeasureItem, objectsTreeListener);

		/* Drag & drop parent */
		Transfer[] swtTransferTypes = new Transfer[] { TextTransfer.getInstance() };

		int swtDragOperations = DND.DROP_MOVE;
		DragSource swtDragSource = new DragSource(objectsTree, swtDragOperations);
		swtDragSource.setTransfer(swtTransferTypes);
		swtDragSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent dragSourceEvent) {
				IusCLComponent designComponent = designSelection.getDesignComponent();
				if ((designSelection.getDesignCollection() != null) || (designSelection.getDesignCollectionItem() != null)) {
					dragSourceEvent.doit = false;
					return;
				}
				if (!(designComponent instanceof IusCLControl)) {
					dragSourceEvent.doit = false;
					return;
				}
				if (designComponent.getIsSubComponent()) {
					dragSourceEvent.doit = false;
					return;
				}
				if (designComponent instanceof IusCLForm) {
					dragSourceEvent.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent dragSourceEvent) {
				dragSourceEvent.data = "OK";
			}
		});

		int swtDropOperations = DND.DROP_MOVE;
		DropTarget swtDropTarget = new DropTarget(objectsTree, swtDropOperations);
		swtDropTarget.setTransfer(swtTransferTypes);
		swtDropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void drop(DropTargetEvent dropTargetEvent) {
				TreeItem swtOverTreeItem = (TreeItem) dropTargetEvent.item;
				IusCLContainerControl parentContainerControl = (IusCLContainerControl) swtOverTreeItem.getData();
				IusCLControl designControl = (IusCLControl) designSelection.getDesignComponent();
				IusCLDesignIDE.getFormDesignEditor().setDesignComponent(parentContainerControl);
				designControl.setParent(parentContainerControl);
				/* Adjust */
				int parentClientHeight = parentContainerControl.getSwtComposite().getClientArea().height
						- 2 * parentContainerControl.getBorderWidth();
				if (designControl.getTop() + designControl.getHeight() > parentClientHeight) {
					designControl.setTop(parentClientHeight - designControl.getHeight());
				}
				int parentClientWidth = parentContainerControl.getSwtComposite().getClientArea().width - 2 * parentContainerControl.getBorderWidth();
				if (designControl.getLeft() + designControl.getWidth() > parentClientWidth) {
					designControl.setLeft(parentClientWidth - designControl.getWidth());
				}
				IusCLDesignIDE.getFormDesignEditor().setDesignComponent(designControl);
				makeOwnerComponentsList(designControl.getOwner());
				IusCLDesignIDE.getFormDesignEditor().serializeAndBroadcastChange();
			}

			@Override
			public void dragOver(DropTargetEvent dropTargetEvent) {
				dropTargetEvent.detail = DND.DROP_NONE;
				dropTargetEvent.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
				TreeItem swtOverTreeItem = (TreeItem) dropTargetEvent.item;
				if (!(swtOverTreeItem.getData() instanceof IusCLContainerControl)) {
					return;
				}
				IusCLContainerControl overContainerControl = (IusCLContainerControl) swtOverTreeItem.getData();
				/* Over control child of drag control */
				IusCLParentControl overParentControl = overContainerControl.getParent();
				while (overParentControl != null) {
					if (overContainerControl.getParent() == designSelection.getDesignComponent()) {
						return;
					}
					overParentControl = overParentControl.getParent();
				}
				dropTargetEvent.detail = DND.DROP_MOVE;
				dropTargetEvent.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
			}
		});

		// // Create the help context id for the viewer's control
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(objectTreeViewer.getControl(),
		// "org.iuscl.plugin.viewer");

		makeActions();
		contributeToActionBars();

		IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsCreate);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		// fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	// private void fillLocalPullDown(IMenuManager manager) {
	// manager.add(actionNewItem);
	// manager.add(new Separator());
	// manager.add(actionDelete);
	// }

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionNewItem);
		manager.add(actionDelete);
		manager.add(new Separator());
		manager.add(actionMoveUp);
		manager.add(actionMoveDown);
	}

	private void makeActions() {
		/* New Item */
		actionNewItem = new Action() {

			@Override
			public void run() {
				IusCLDesignComponentEditor componentEditor = IusCLDesignIDE.getDesignComponentEditor(
						designSelection.getDesignComponent().getClass().getCanonicalName(), designSelection.getDesignComponent());

				IusCLDesignComponentEditorVerb verb = componentEditor.verbAdd();
				/* Invoke the verb */
				IusCLObjUtils.invokeMethod(IusCLDesignIDE.getFormDesignEditor(), verb.getMethodName(), verb.getParams());
			}
		};
		actionNewItem.setText("New Item");
		actionNewItem.setToolTipText("New Item");
		actionNewItem.setImageDescriptor(ImageDescriptor.createFromImage(imageNewItem));
		actionNewItem.setDisabledImageDescriptor(ImageDescriptor.createFromImage(imageNewItemDisabled));

		/* Delete */
		actionDelete = new Action() {

			@Override
			public void run() {
				/* Delete */
				if (designSelection.getDesignCollectionItem() != null) {
					/* Delete collection item */
					int deleteIndex = designSelection.getDesignCollection().indexOf(designSelection.getDesignCollectionItem());
					designSelection.getDesignCollection().delete(deleteIndex);
					designSelection.setDesignCollectionItem(null);
					designSelection.setDesignCollection(null);
					IusCLDesignIDE.getFormDesignEditor().serializeAndBroadcastChange();
				} else if (designSelection.getDesignCollection() != null) {
					/* Delete collection */
				} else {
					/* Delete component */
					IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsDelete);
				}
			}
		};
		actionDelete.setText("Delete");
		actionDelete.setToolTipText("Delete");
		actionDelete.setImageDescriptor(ImageDescriptor.createFromImage(imageDelete));
		actionDelete.setDisabledImageDescriptor(ImageDescriptor.createFromImage(imageDeleteDisabled));

		/* Move Up */
		actionMoveUp = new Action() {

			@Override
			public void run() {
				/* Move up */
				if (designSelection.getDesignCollectionItem() != null) {
					/* Move up collection item */
					int firstIndex = designSelection.getDesignCollection().indexOf(designSelection.getDesignCollectionItem());
					int secondIndex = firstIndex - 1;
					IusCLDesignComponentEditor componentEditor = IusCLDesignIDE.getDesignComponentEditor(
							designSelection.getDesignComponent().getClass().getCanonicalName(), designSelection.getDesignComponent());
					IusCLDesignComponentEditorVerb verb = componentEditor.verbOrder(firstIndex, secondIndex);
					/* Invoke the verb */
					IusCLObjUtils.invokeMethod(IusCLDesignIDE.getFormDesignEditor(), verb.getMethodName(), verb.getParams());
					/* Move selection */
					designSelection.setDesignCollectionItem(designSelection.getDesignCollection().get(secondIndex));
					selectDesignTreeItem();
					IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsSelection);
				} else if (designSelection.getDesignCollection() != null) {
					/* Move up collection */
				} else {
					/* Move up component */
					moveDesignComponent(-1);
				}
			}
		};
		actionMoveUp.setText("Move Up");
		actionMoveUp.setToolTipText("Move Up");
		actionMoveUp.setImageDescriptor(ImageDescriptor.createFromImage(imageMoveUp));
		actionMoveUp.setDisabledImageDescriptor(ImageDescriptor.createFromImage(imageMoveUpDisabled));

		/* Move Down */
		actionMoveDown = new Action() {

			@Override
			public void run() {
				/* Move down */
				if (designSelection.getDesignCollectionItem() != null) {
					/* Move down collection item */
					int firstIndex = designSelection.getDesignCollection().indexOf(designSelection.getDesignCollectionItem());
					int secondIndex = firstIndex + 1;

					IusCLDesignComponentEditor componentEditor = IusCLDesignIDE.getDesignComponentEditor(
							designSelection.getDesignComponent().getClass().getCanonicalName(), designSelection.getDesignComponent());

					IusCLDesignComponentEditorVerb verb = componentEditor.verbOrder(firstIndex, secondIndex);
					/* Invoke the verb */
					IusCLObjUtils.invokeMethod(IusCLDesignIDE.getFormDesignEditor(), verb.getMethodName(), verb.getParams());
					/* Move selection */
					designSelection.setDesignCollectionItem(designSelection.getDesignCollection().get(secondIndex));
					selectDesignTreeItem();
					IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsSelection);
				} else if (designSelection.getDesignCollection() != null) {
					/* Move down collection */
				} else {
					/* Move down component */
					moveDesignComponent(1);
				}
			}
		};
		actionMoveDown.setText("Move Down");
		actionMoveDown.setToolTipText("Move Down");
		actionMoveDown.setImageDescriptor(ImageDescriptor.createFromImage(imageMoveDown));
		actionMoveDown.setDisabledImageDescriptor(ImageDescriptor.createFromImage(imageMoveDownDisabled));
	}

	private void moveDesignComponent(int pos) {
		IusCLComponent designComponent = designSelection.getDesignComponent();
		IusCLComponent parentComponent = designComponent.getParentComponent();
		IusCLComponent destComponent = null;

		if (designComponent instanceof IusCLControl designControl) {
			IusCLParentControl parentControl = designControl.getParent();
			int controlIndexInParent = parentControl.getControls().indexOf(designControl);

			int destControlIndexInParent = controlIndexInParent + pos;

			IusCLControl destControl = parentControl.getControls().get(destControlIndexInParent);

			if (pos > 0) {
				designControl.bringToFront(destControl);
			} else {
				designControl.sendToBack(destControl);
			}

			// destComponent = destControl;
		} else {
			int componentIndexInParent = parentComponent.getChildren().indexOf(designComponent);

			int destComponentIndexInParent = componentIndexInParent + pos;

			destComponent = parentComponent.getChildren().get(destComponentIndexInParent);

			parentComponent.getChildren().set(componentIndexInParent, destComponent);
			parentComponent.getChildren().set(destComponentIndexInParent, designComponent);

			if (pos > 0) {
				IusCLDesignIDE.getFormDesignEditor().serializeZOrder(destComponent, designComponent);
			} else {
				IusCLDesignIDE.getFormDesignEditor().serializeZOrder(designComponent, destComponent);
			}

			// if (designComponent instanceof IusCLMenuItem) {
			// MenuItem swtMenuItem = ((IusCLMenuItem)designComponent).getSwtMenuItem();
			// //swtMenuItem.
			//
			// }
		}

		makeOwnerComponentsList(designComponent.getOwner());
		IusCLDesignIDE.getFormDesignEditor().serializeAndBroadcastChange();
		IusCLDesignIDE.dispatchObjectTreeViewView(IusCLObjectTreeViewView.this, IusCLDesignIDEState.dsSelection);
	}

	private void makeOwnerComponentsList(IusCLComponent owner) {
		owner.getComponents().clear();
		recursiveChildControlsComponents(owner, owner);
	}

	private void recursiveChildControlsComponents(IusCLComponent component, IusCLComponent owner) {
		if (!(component instanceof IusCLForm)) {
			owner.getComponents().add(component);
		}

		/* Controls */
		if (component instanceof IusCLParentControl parentControl) {
			for (int index = 0; index < parentControl.getControls().size(); index++) {
				recursiveChildControlsComponents(parentControl.getControls().get(index), owner);
			}
		}

		/* Non-visual Components */
		for (int index = 0; index < component.getChildren().size(); index++) {
			recursiveChildControlsComponents(component.getChildren().get(index), owner);
			// owner.getComponents().add(component.getChildren().get(index));
		}
	}

	private void findActionsStatus(TreeItem treeItem) {
		/* New */
		actionNewItem.setEnabled(false);
		IusCLDesignComponentEditor componentEditor = IusCLDesignIDE
				.getDesignComponentEditor(designSelection.getDesignComponent().getClass().getCanonicalName(), designSelection.getDesignComponent());
		if (componentEditor.getHasAdd()) {
			actionNewItem.setEnabled(true);
		}

		/* Delete */
		actionDelete.setEnabled(true);
		if (designSelection.getDesignComponent() instanceof IusCLForm) {
			actionDelete.setEnabled(false);
		}
		if ((designSelection.getDesignCollection() != null) && (designSelection.getDesignCollectionItem() == null)) {
			actionDelete.setEnabled(false);
		}

		/* Up, Down */
		actionMoveUp.setEnabled(true);
		actionMoveDown.setEnabled(true);

		if (designSelection.getDesignCollectionItem() != null && !(componentEditor.getHasOrder())) {
			actionMoveUp.setEnabled(false);
			actionMoveDown.setEnabled(false);
		}

		/* Move up, down component */
		TreeItem parentTreeItem = treeItem.getParentItem();
		if (parentTreeItem != null) {
			int indexInParent = parentTreeItem.indexOf(treeItem);

			if (indexInParent == 0) {
				actionMoveUp.setEnabled(false);
			} else {
				if (designSelection.getDesignCollection() == null && (!(treeItem.getData() instanceof IusCLControl))
						&& (parentTreeItem.getItem(indexInParent - 1).getData() instanceof IusCLControl)) {
					actionMoveUp.setEnabled(false);
				}
			}
			if (indexInParent == (parentTreeItem.getItemCount() - 1)) {
				actionMoveDown.setEnabled(false);
			} else {
				if (designSelection.getDesignCollection() == null && (treeItem.getData() instanceof IusCLControl)
						&& (!(parentTreeItem.getItem(indexInParent + 1).getData() instanceof IusCLControl))) {
					actionMoveDown.setEnabled(false);
				}
			}
		} else {
			/* The form */
			actionMoveUp.setEnabled(false);
			actionMoveDown.setEnabled(false);
		}

		if ((designSelection.getDesignCollection() != null) && (designSelection.getDesignCollectionItem() == null)) {
			/* Move up, down collection */
			actionMoveUp.setEnabled(false);
			actionMoveDown.setEnabled(false);
		}
	}

	public void setFocus() {
		objectsTree.setFocus();
	}
}
