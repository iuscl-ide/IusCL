/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.plugin.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.classes.IusCLPersistent.IusCLPropertyType;
import org.iuscl.classes.IusCLProperty;
import org.iuscl.designintf.componenteditors.IusCLDesignSelection;
import org.iuscl.designintf.propertyeditors.IusCLDesignPropertyEditor;
import org.iuscl.forms.IusCLForm;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.plugin.IusCLPlugin;
import org.iuscl.plugin.ide.IusCLDesignIDE;
import org.iuscl.plugin.ide.IusCLDesignIDE.IusCLDesignIDEState;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLObjectInspectorView extends ViewPart {

	private static final Color VALUE_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private static final Color COMPONENT_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
	Font fontBold = null;
	Font fontNormal = null;

	/* The ID of the view as specified by the extension. */
	public static final String ID = "org.iuscl.plugin.views.IusCLObjectInspectorView";

	/* Toolbar images */
	Image swtImageHelp = IusCLDesignIDE.loadImageFromResource("IusCLActionHelp.gif");

	Action actionHelp;

	private static final String HELP_REFERENCE_PREFIX = "/" + IusCLPlugin.PLUGIN_ID + "/help/reference/";

	@Getter
	IusCLDesignSelection designSelection;

	IusCLPersistent designPersistent;
	@Getter
	String designPropertyName;

	@Getter
	String oldDesignPropertyValue;

	Combo swtObjectCombo;
	Hashtable<String, IusCLComponent> formComponents = new Hashtable<>();

	TabFolder swtPropertiesEventsTabFolder;

	Tree swtPropertiesTree;
	Tree swtEventsTree;

	TreeEditor swtPropertiesTreeEditor;
	TreeEditor swtEventsTreeEditor;

	public IusCLObjectInspectorView() {
		/*  */
	}

	private void recursivePutComponents(IusCLComponent parentComponent) {
		for (int index = 0; index < parentComponent.getComponents().size(); index++) {
			IusCLComponent childComponent = parentComponent.getComponents().get(index);
			formComponents.put(childComponent.getName() + " - " + childComponent.getClass().getCanonicalName(), childComponent);

			recursivePutComponents(childComponent);
		}
	}

	public void setDesignSelection(IusCLDesignSelection designSelection) {
		if (swtPropertiesTreeEditor.getEditor() != null && !swtPropertiesTreeEditor.getEditor().isDisposed()) {
			IusCLDesignPropertyEditor propertyEditor = (IusCLDesignPropertyEditor) swtPropertiesTreeEditor.getEditor().getData();

			propertyEditor.closeAndSaveEditor();
		}

		this.designSelection = designSelection;
		if (designSelection == null) {
			return;
		}
		designPersistent = designSelection.findPersistent();

		if (swtObjectCombo.getItems().length > 0) {
			/* Already loaded */
			for (int index = 0; index < swtObjectCombo.getItems().length; index++) {
				String formComponentName = swtObjectCombo.getItem(index);
				if (swtObjectCombo.getData(formComponentName).equals(designSelection.getDesignComponent())) {
					swtObjectCombo.select(index);
					initObjectListPropertiesEvents();
					break;
				}
			}
		}
	}

	public void initObjectCombo(IusCLForm designForm, IusCLDesignSelection designSelection) {
		swtObjectCombo.removeAll();

		if (designForm == null) {
			this.designSelection = null;
			this.designPersistent = null;

			swtPropertiesTree.removeAll();
			swtEventsTree.removeAll();
			return;
		}

		formComponents.clear();
		formComponents.put(designForm.getName() + " - " + designForm.getClass().getCanonicalName(), designForm);
		recursivePutComponents(designForm);

		Vector<String> formComponentsNamesVector = new Vector<String>(formComponents.keySet());
		Collections.sort(formComponentsNamesVector);
		Iterator<String> formComponentsNamesIterator = formComponentsNamesVector.iterator();
		while (formComponentsNamesIterator.hasNext()) {
			String formComponentName = formComponentsNamesIterator.next();
			swtObjectCombo.add(formComponentName);
			swtObjectCombo.setData(formComponentName, formComponents.get(formComponentName));
		}

		setDesignSelection(designSelection);
	}

	private void initObjectListPropertiesEvents() {
		/* Previous selection */
		List<String> propertiesSelectedNodes = new ArrayList<>();
		if (swtPropertiesTree.getSelectionCount() > 0) {
			TreeItem selectedTreeItem = swtPropertiesTree.getSelection()[0];
			propertiesSelectedNodes.add(selectedTreeItem.getText(0));

			while (selectedTreeItem.getParentItem() != null) {
				selectedTreeItem = selectedTreeItem.getParentItem();
				propertiesSelectedNodes.add(selectedTreeItem.getText(0));
			}
		}

		/* Properties and Events */
		swtPropertiesTree.removeAll();
		swtEventsTree.removeAll();

		/* Combo Component, Collection or CollectionItem */
		if (designSelection.getDesignCollectionItem() != null) {
			int itemIndex = -1;
			for (int index = 0; index < designSelection.getDesignCollection().getCount(); index++) {
				if (designSelection.getDesignCollection().get(index).equals(designSelection.getDesignCollectionItem())) {
					itemIndex = index;
				}
			}

			swtObjectCombo.setText(designSelection.getDesignComponent().getName() + "." + designSelection.getDesignCollection().getPropertyName()
					+ "[" + itemIndex + "]" + " - " + designSelection.getDesignCollectionItem().getClass().getSimpleName());
		} else if (designSelection.getDesignCollection() != null) {
			swtObjectCombo.setText(designSelection.getDesignComponent().getName() + "." + designSelection.getDesignCollection().getPropertyName()
					+ " - " + designSelection.getDesignCollection().getClass().getSimpleName());
		}

		/* Load lists */
		Vector<String> propertiesNamesVector = new Vector<String>(designPersistent.getProperties().keySet());
		Collections.sort(propertiesNamesVector);
		Iterator<String> propertiesNamesIterator = propertiesNamesVector.iterator();
		while (propertiesNamesIterator.hasNext()) {
			/* Property */
			String propertyName = propertiesNamesIterator.next();

			String propertyType = designPersistent.getProperty(propertyName).getType();
			String propertyValue = designPersistent.getPropertyValue(propertyName);
			String propertyDefaultValue = designPersistent.getProperty(propertyName).getDefaultValue();

			/* Property null */
			if (propertyValue == null) {
				propertyValue = "";
			}

			if (designPersistent.getProperty(propertyName).getPublished()) {
				/* Is published, put in the list */
				if (propertyType.equalsIgnoreCase(IusCLPropertyType.ptEvent.name())) {
					/* Is published and is an event */
					TreeItem childTreeItem = new TreeItem(swtEventsTree, 0);
					childTreeItem.setData(propertyName);
					childTreeItem.setText(0, propertyName);
					childTreeItem.setForeground(1, VALUE_COLOR);
					childTreeItem.setText(1, getPropertyValueDisplay(propertyType, propertyValue));
					/* Is not default */
					childTreeItem.setFont(1, fontBold);
				} else {
					/* Is published and is not an event */
					String propertyValueName = propertyName;

					TreeItem parentTreeItem = null;
					TreeItem childTreeItem = null;
					Object parentInstance = designPersistent;

					if (propertyName.indexOf(".") > -1) {
						/* Multiple parents */
						String[] splits = propertyName.split("\\.");

						for (int index = 0; index < splits.length - 1; index++) {
							/* Parents */
							String childName = splits[index];
							String childGetterMethodName = "get" + childName;
							parentInstance = IusCLObjUtils.invokeMethod(parentInstance, childGetterMethodName);
							boolean isCreating = false;

							if (parentTreeItem == null) {
								/* Search first level */
								childTreeItem = findTreeItem(swtPropertiesTree.getItems(), childName);
								if (childTreeItem == null) {
									/* Create on first level */
									childTreeItem = new TreeItem(swtPropertiesTree, 0);
									isCreating = true;
								}
							} else {
								/* Search child level */
								childTreeItem = findTreeItem(parentTreeItem.getItems(), childName);
								if (childTreeItem == null) {
									/* Create on child level */
									childTreeItem = new TreeItem(parentTreeItem, 0);
									isCreating = true;
								}
							}

							if (isCreating) {
								if (propertyType.equalsIgnoreCase(IusCLPropertyType.ptComponent.name())) {
									childTreeItem.setForeground(0, COMPONENT_COLOR);
								}
								childTreeItem.setText(0, childName);

								// parentInstance = IusCLObjUtils.invokeMethod(parentInstance,
								// childGetterMethodName);

								childTreeItem.setForeground(1, VALUE_COLOR);
								childTreeItem.setText(1, "(" + parentInstance.getClass().getSimpleName() + ")");
							}

							parentTreeItem = childTreeItem;
						}

						propertyValueName = splits[splits.length - 1];
					}

					if (parentTreeItem == null) {
						childTreeItem = new TreeItem(swtPropertiesTree, 0);
					} else {
						childTreeItem = new TreeItem(parentTreeItem, 0);
					}

					childTreeItem.setData(propertyName);
					if (propertyType.equalsIgnoreCase(IusCLPropertyType.ptComponent.name())) {
						childTreeItem.setForeground(0, COMPONENT_COLOR);
					}
					childTreeItem.setText(0, propertyValueName);
					childTreeItem.setForeground(1, VALUE_COLOR);
					childTreeItem.setText(1, getPropertyValueDisplay(propertyType, propertyValue));

					/* Replace with calculate non default */
					if (!IusCLStrUtils.equalValues(propertyValue, propertyDefaultValue)) {
						/* Not default */
						childTreeItem.setFont(1, fontBold);
						if (propertyName.indexOf(".") > -1) {
							TreeItem parentTreeItemBold = childTreeItem.getParentItem();
							while (parentTreeItemBold != null) {
								parentTreeItemBold.setFont(1, fontBold);
								parentTreeItemBold = parentTreeItemBold.getParentItem();
							}
						}
					}
				}
			}
		}

		/* Previous selection */
		if (!propertiesSelectedNodes.isEmpty()) {
			TreeItem newSelectedItem = null;

			String firstLevelNode = propertiesSelectedNodes.get(propertiesSelectedNodes.size() - 1);
			int firstNodeIndex = -1;
			for (int i = 0; i < swtPropertiesTree.getItems().length; i++) {
				if (IusCLStrUtils.equalValues(swtPropertiesTree.getItems()[i].getText(0), firstLevelNode)) {
					firstNodeIndex = i;
					break;
				}
			}
			if (firstNodeIndex > -1) {
				newSelectedItem = swtPropertiesTree.getItem(firstNodeIndex);

				for (int index = propertiesSelectedNodes.size() - 2; index >= 0; index--) {
					String nextLevelNode = propertiesSelectedNodes.get(index);
					int nextNodeIndex = -1;
					for (int i = 0; i < newSelectedItem.getItems().length; i++) {
						if (IusCLStrUtils.equalValues(newSelectedItem.getItems()[i].getText(0), nextLevelNode)) {
							nextNodeIndex = i;
							break;
						}
					}
					if (nextNodeIndex > -1) {
						newSelectedItem = newSelectedItem.getItem(nextNodeIndex);
					}
				}
			}
			if (newSelectedItem != null) {
				swtPropertiesTree.setSelection(newSelectedItem);
			} else {
				swtPropertiesTree.setSelection(swtPropertiesTree.getItem(0));
			}
		}
	}

	private TreeItem findTreeItem(TreeItem[] items, String treeItemName) {
		TreeItem foundTreeItem = null;
		for (int index = 0; index < items.length; index++) {
			TreeItem treeItem = items[index];
			if (treeItem.getText(0).equalsIgnoreCase(treeItemName)) {
				foundTreeItem = treeItem;
				break;
			}
		}
		return foundTreeItem;
	}

	public void changePropertyValue(TreeItem treeItem, String newPropertyValue) {
		String propertyName = (String) treeItem.getData();
		String oldPropertyValue = designPersistent.getPropertyValue(propertyName);

		if (!IusCLStrUtils.equalValues(oldPropertyValue, newPropertyValue)) {
			if (newPropertyValue == null) {
				treeItem.setText(1, "");
			} else {
				treeItem.setText(1, getPropertyValueDisplay(designPersistent.getProperty(propertyName).getType(), newPropertyValue));
			}

			designPersistent.setPropertyValue(propertyName, newPropertyValue);

			/* Bold text */
			if (!IusCLStrUtils.equalValues(newPropertyValue, designPersistent.getProperty(propertyName).getDefaultValue())) {
				/* Not default, bold value */
				treeItem.setFont(1, fontBold);
				if (propertyName.indexOf(".") > -1) {
					/* Bold parents */
					TreeItem parentTreeItem = treeItem.getParentItem();
					while (parentTreeItem != null) {
						parentTreeItem.setFont(1, fontBold);
						parentTreeItem = parentTreeItem.getParentItem();
					}
				}
			} else {
				/* Default, not bold value */
				treeItem.setFont(1, fontNormal);
				if (propertyName.indexOf(".") > -1) {
					TreeItem parentTreeItem = treeItem.getParentItem();

					while (parentTreeItem != null) {
						/* If all the children not bold now */
						boolean allChildrenNotBold = true;
						for (int index = 0; index < parentTreeItem.getItems().length; index++) {
							TreeItem childTreeItem = parentTreeItem.getItems()[index];
							int style = childTreeItem.getFont(1).getFontData()[0].getStyle();
							if (style == SWT.BOLD) {
								allChildrenNotBold = false;
								break;
							}
						}

						if (allChildrenNotBold) {
							parentTreeItem.setFont(1, fontNormal);
							parentTreeItem = parentTreeItem.getParentItem();
						} else {
							parentTreeItem = null;
						}
					}
				}
			}

			/* Broadcast */
			this.oldDesignPropertyValue = oldPropertyValue;
			this.designPropertyName = propertyName;
			IusCLDesignIDE.dispatchObjectInspectorView(this, IusCLDesignIDEState.dsChange);

			initObjectListPropertiesEvents();
		} else {
			/* For events always go into code */
			if (IusCLStrUtils.equalValues(designPersistent.getProperty(propertyName).getType(), IusCLPropertyType.ptEvent.name())) {
				this.oldDesignPropertyValue = oldPropertyValue;
				this.designPropertyName = propertyName;
				IusCLDesignIDE.dispatchObjectInspectorView(this, IusCLDesignIDEState.dsChange);
			}
		}
	}

	SelectionListener objectComboSelectionAdapter = SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
		Object selectedData = swtObjectCombo.getData(swtObjectCombo.getText());
		if (selectedData == null) {
			return;
		}
		IusCLComponent selectedComponent = (IusCLComponent) selectedData;
		designSelection.setDesignComponent(selectedComponent);
		designSelection.setDesignCollection(null);
		designSelection.setDesignCollectionItem(null);
		setDesignSelection(designSelection);
		IusCLDesignIDE.dispatchObjectInspectorView(IusCLObjectInspectorView.this, IusCLDesignIDEState.dsSelection);
	});

	Listener propertiesTreeListener = swtEvent -> {
		Tree tree = (Tree) swtEvent.widget;
		final TreeItem treeItem = (TreeItem) swtEvent.item;

		switch (swtEvent.type) {
		case SWT.MeasureItem:
			swtEvent.height = swtObjectCombo.getSize().y;
			break;
		case SWT.PaintItem:
			if (swtEvent.index == 1) {
				String propertyName = (String) treeItem.getData();
				if (propertyName != null) {
					IusCLProperty paintProperty = designPersistent.getProperty(propertyName);
					boolean customDisplayPaint = IusCLDesignIDE.getDesignPropertyInfos().get(paintProperty.getType()).getCustomDisplayPaint();
					if (customDisplayPaint) {
						getPropertyDisplayPaint(paintProperty, treeItem, swtEvent, treeItem.getText(1));
					}
				}
			}
			break;
		case SWT.Resize:
			int treeWidth = tree.getBounds().width;
			int columnWidth = (treeWidth - tree.getVerticalBar().getSize().x) / 2;
			tree.getColumn(0).setWidth(columnWidth);
			tree.getColumn(1).setWidth(columnWidth);
			break;
		case SWT.Selection:
			if (treeItem.getData() != null) {
				IusCLDesignPropertyEditor propertyEditor = getPropertyEditor(treeItem);
				swtPropertiesTreeEditor.setEditor(propertyEditor.getEditor(), treeItem, 1);
			}
			break;
		default:
			break;
		}
	};

	Listener eventsTreeListener = swtEvent -> {
		final TreeItem treeItem = (TreeItem) swtEvent.item;
		if (swtEvent.type == SWT.Selection) {
			IusCLDesignPropertyEditor propertyEditor = getPropertyEditor(treeItem);
			swtPropertiesTreeEditor.setEditor(propertyEditor.getEditor(), treeItem, 1);
		}
	};

	public void createPartControl(Composite parent) {
		GridLayout objectInspectorLayout = new GridLayout(1, false);
		objectInspectorLayout.marginTop = 0;
		objectInspectorLayout.marginLeft = 0;
		objectInspectorLayout.horizontalSpacing = 0;
		objectInspectorLayout.marginWidth = 0;
		objectInspectorLayout.marginBottom = 0;
		objectInspectorLayout.marginHeight = 0;
		objectInspectorLayout.marginRight = 0;
		objectInspectorLayout.verticalSpacing = 2;

		parent.setLayout(objectInspectorLayout);

		// objectCombo = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
		swtObjectCombo = new Combo(parent, SWT.BORDER);
		GridData objectComboGridData = new GridData();
		objectComboGridData.horizontalAlignment = SWT.FILL;
		objectComboGridData.grabExcessHorizontalSpace = true;
		swtObjectCombo.setLayoutData(objectComboGridData);
		swtObjectCombo.addSelectionListener(objectComboSelectionAdapter);

		fontNormal = swtObjectCombo.getFont();
		FontData oldFontData = fontNormal.getFontData()[0];
		fontBold = new Font(Display.getCurrent(), oldFontData.getName(), oldFontData.getHeight(), SWT.BOLD);

		swtPropertiesEventsTabFolder = new TabFolder(parent, SWT.NULL);
		GridData propertiesEventsTabFolderGridData = new GridData();
		propertiesEventsTabFolderGridData.horizontalAlignment = SWT.FILL;
		propertiesEventsTabFolderGridData.grabExcessHorizontalSpace = true;
		propertiesEventsTabFolderGridData.verticalAlignment = SWT.FILL;
		propertiesEventsTabFolderGridData.grabExcessVerticalSpace = true;
		swtPropertiesEventsTabFolder.setLayoutData(propertiesEventsTabFolderGridData);

		GridLayout tabItemLayout = new GridLayout(1, false);
		tabItemLayout.marginTop = 2;
		tabItemLayout.marginLeft = 0;
		tabItemLayout.horizontalSpacing = 0;
		tabItemLayout.marginWidth = 0;
		tabItemLayout.marginBottom = 0;
		tabItemLayout.marginHeight = 0;
		tabItemLayout.marginRight = 0;
		tabItemLayout.verticalSpacing = 0;

		Composite compositeProperties = new Composite(swtPropertiesEventsTabFolder, SWT.NULL);
		compositeProperties.setLayout(tabItemLayout);
		Composite compositeEvents = new Composite(swtPropertiesEventsTabFolder, SWT.NULL);
		compositeEvents.setLayout(tabItemLayout);

		TabItem propertiesTabItem = new TabItem(swtPropertiesEventsTabFolder, SWT.NULL);
		propertiesTabItem.setText("Properties");
		propertiesTabItem.setControl(compositeProperties);
		TabItem eventsTabItem = new TabItem(swtPropertiesEventsTabFolder, SWT.NONE);
		eventsTabItem.setText("Events");
		eventsTabItem.setControl(compositeEvents);

		swtPropertiesTree = new Tree(compositeProperties, SWT.FULL_SELECTION);
		swtPropertiesTree.setLayoutData(propertiesEventsTabFolderGridData);
		swtPropertiesTree.setData(IusCLObjectInspectorView.this);

		swtPropertiesTree.setHeaderVisible(false);
		swtPropertiesTree.setLinesVisible(true);
		swtPropertiesTree.addListener(SWT.Resize, propertiesTreeListener);
		swtPropertiesTree.addListener(SWT.Selection, propertiesTreeListener);
		swtPropertiesTree.addListener(SWT.MeasureItem, propertiesTreeListener);
		swtPropertiesTree.addListener(SWT.PaintItem, propertiesTreeListener);

		TreeColumn propertyNameTreeColumn = new TreeColumn(swtPropertiesTree, SWT.LEFT);
		propertyNameTreeColumn.setAlignment(SWT.LEFT);
		propertyNameTreeColumn.setText("Property Name");

		TreeColumn propertyValueTreeColumn = new TreeColumn(swtPropertiesTree, SWT.RIGHT);
		propertyValueTreeColumn.setAlignment(SWT.LEFT);
		propertyValueTreeColumn.setText("Property Value");

		swtPropertiesTreeEditor = new TreeEditor(swtPropertiesTree);
		swtPropertiesTreeEditor.horizontalAlignment = SWT.LEFT;
		swtPropertiesTreeEditor.grabHorizontal = true;

		/* Events */
		swtEventsTree = new Tree(compositeEvents, SWT.FULL_SELECTION);
		swtEventsTree.setLayoutData(propertiesEventsTabFolderGridData);
		swtEventsTree.setData(IusCLObjectInspectorView.this);

		swtEventsTree.setHeaderVisible(false);
		swtEventsTree.setLinesVisible(true);
		swtEventsTree.addListener(SWT.Resize, propertiesTreeListener);
		swtEventsTree.addListener(SWT.Selection, eventsTreeListener);
		swtEventsTree.addListener(SWT.MeasureItem, propertiesTreeListener);

		TreeColumn eventNameTreeColumn = new TreeColumn(swtEventsTree, SWT.LEFT);
		eventNameTreeColumn.setAlignment(SWT.LEFT);
		eventNameTreeColumn.setText("Event Name");

		TreeColumn eventValueTreeColumn = new TreeColumn(swtEventsTree, SWT.RIGHT);
		eventValueTreeColumn.setAlignment(SWT.LEFT);
		eventValueTreeColumn.setText("Event Value");

		swtEventsTreeEditor = new TreeEditor(swtEventsTree);
		swtEventsTreeEditor.horizontalAlignment = SWT.LEFT;
		swtEventsTreeEditor.grabHorizontal = true;

		makeActions();
		contributeToActionBars();

		IusCLDesignIDE.dispatchObjectInspectorView(this, IusCLDesignIDEState.dsCreate);
	}

	private IusCLDesignPropertyEditor getPropertyEditor(TreeItem treeItem) {
		String propertyName = (String) treeItem.getData();
		String editorClassName = IusCLDesignIDE.getDesignPropertyInfos().get(designPersistent.getProperty(propertyName).getType())
				.getPropertyEditorClass();

		return (IusCLDesignPropertyEditor) IusCLObjUtils.invokeConstructor(editorClassName, new IusCLParam(TreeItem.class, treeItem),
				new IusCLParam(IusCLPersistent.class, designPersistent));
	}

	private String getPropertyValueDisplay(String propertyType, String value) {
		if (!IusCLDesignIDE.getDesignPropertyInfos().get(propertyType).getCustomDisplayValue()) {
			return value;
		}

		String editorClassName = IusCLDesignIDE.getDesignPropertyInfos().get(propertyType).getPropertyEditorClass();

		return (String) IusCLObjUtils.invokeStaticMethod(editorClassName, "getDisplayValue", new IusCLParam(String.class, value));
	}

	private void getPropertyDisplayPaint(IusCLProperty property, TreeItem swtTreeItem, Event swtEvent, String value) {
		String editorClassName = IusCLDesignIDE.getDesignPropertyInfos().get(property.getType()).getPropertyEditorClass();

		IusCLObjUtils.invokeStaticMethod(editorClassName, "getDisplayPaint", new IusCLParam(IusCLPersistent.class, designPersistent),
				new IusCLParam(String.class, property.getName()), new IusCLParam(TreeItem.class, swtTreeItem), new IusCLParam(Event.class, swtEvent),
				new IusCLParam(String.class, value));
	}

	private void makeActions() {
		/* New Item */
		actionHelp = new Action() {

			@Override
			public void run() {
				/* Display help */
				String helpResource = HELP_REFERENCE_PREFIX;

				if (designSelection != null && designSelection.getDesignComponent() != null) {
					String helpComponent = designSelection.getDesignComponent().getClass().getSimpleName();
					if (designSelection.getDesignComponent() instanceof IusCLForm) {
						helpComponent = "IusCLForm";
					}

					String helpMethodLink = null;

					if (!swtObjectCombo.isFocusControl()) {
						if (swtPropertiesEventsTabFolder.getSelectionIndex() == 0) {
							if (swtPropertiesTree.getSelectionCount() > 0) {
								TreeItem sel = swtPropertiesTree.getSelection()[0];

								if (sel.getItemCount() == 0) {
									/* It's a property */
									String propertyName = (String) sel.getData();
									helpMethodLink = propertyName + "_property";
								} else {
									helpComponent = swtPropertiesTree.getSelection()[0].getText(1);
									helpComponent = helpComponent.replace("(", "").replace(")", "");
								}
							}
						} else {
							if (swtEventsTree.getSelectionCount() > 0) {
								String eventName = swtEventsTree.getSelection()[0].getText(0);
								helpMethodLink = eventName + "_event";
							}
						}
					}

					helpResource = helpResource + helpComponent + ".html";

					if (helpMethodLink != null) {
						helpResource = helpResource + "#" + helpMethodLink;
					}
				}

				if (IusCLStrUtils.equalValues(helpResource, HELP_REFERENCE_PREFIX)) {
					helpResource = helpResource + "reference.html";
				}

				PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(helpResource);
			}
		};
		actionHelp.setText("Component/Property/Event Help");
		actionHelp.setToolTipText("Component/Property/Event Help");
		actionHelp.setImageDescriptor(ImageDescriptor.createFromImage(swtImageHelp));
		actionHelp.setDisabledImageDescriptor(ImageDescriptor.createFromImage(swtImageHelp));
		actionHelp.setEnabled(true);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(actionHelp);
	}

	public void setFocus() {
		/*  */
	}
}
