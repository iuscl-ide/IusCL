/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.iuscl.classes.IusCLPersistent.IusCLPropertyType;
import org.iuscl.controls.IusCLContainerControl;
import org.iuscl.controls.IusCLControl;
import org.iuscl.controls.IusCLParentControl;
import org.iuscl.controls.IusCLWinControl;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.forms.IusCLForm;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class IusCLFiler extends IusCLObject {

	private final String OBJECT = "object";
	private final String COLLECTION = "collection";
	private final String NAME = "name";
	private final String ITEM = "item";

	/* Read component */

	public void readComponentRes(IusCLComponent ownerComponent, String resourceName, boolean isInherited) {
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document jdomDocument = null;

		InputStream inputStream = ownerComponent.getClass().getResourceAsStream(resourceName);
		try {
			if (inputStream != null) {
				/* Resource, runtime */
				jdomDocument = jdomBuilder.build(inputStream);
			} else {
				/* File, design time */
				jdomDocument = jdomBuilder.build(new File(resourceName));
			}
		} catch (IOException ioException) {
			/* IusCLLog.getLog().debug("No .iusclfm found, use default params"); */

//			String exceptionMessage = MessageFormat.format("IO exception loading resource: \"{0}\"", resourceName);
//			log.error(exceptionMessage, ioException);
//			IusCLErrorUtils.showErrorDialog(exceptionMessage, ioException);
			return;
		} catch (JDOMException jdomException) {
			String exceptionMessage = MessageFormat.format("jdom XML exception loading resource: \"{0}\"", resourceName);
			log.error(exceptionMessage, jdomException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, jdomException);
			return;
		}

		Element jdomElementRoot = jdomDocument.getRootElement();
		readRecursiveElement(jdomElementRoot, ownerComponent, ownerComponent, isInherited);
	}

	private void readRecursiveElement(Element jdomElementParent, IusCLComponent component, IusCLComponent ownerComponent, boolean isInherited) {
		/* The field is inherited */
		boolean isInheritedField = false;

		if (isInherited) {
			if (component instanceof IusCLForm) {
				isInheritedField = true;
			} else {
				try {
					ownerComponent.getClass().getDeclaredField(component.getName());
				} catch (NoSuchFieldException noSuchFieldException) {
					isInheritedField = true;
				}
			}
		}

		/* Read element properties */
		List<?> streamedProperties = jdomElementParent.getChildren();

		component.setIsLoading(true);

		for (int index = 0; index < streamedProperties.size(); index++) {
			Element jdomElementProperty = (Element) streamedProperties.get(index);
			String propertyName = jdomElementProperty.getName();

			if (propertyName.equalsIgnoreCase(COLLECTION)) {
				/* It's a collection */
				String collectionName = jdomElementProperty.getAttributeValue(NAME);
				IusCLCollection collection = (IusCLCollection) IusCLObjUtils.invokeMethod(component, "get" + collectionName);
				List<?> jdomChildItems = jdomElementProperty.getChildren(ITEM);
				for (int indexItem = 0; indexItem < jdomChildItems.size(); indexItem++) {
					Element jdomElementItem = (Element) jdomChildItems.get(indexItem);

					IusCLCollectionItem collectionItem = null;
					try {
						collectionItem = collection.get(indexItem);
					} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
						collectionItem = collection.add();
					}

					/* Read collection item's properties, called 'attributes'.. */
					List<?> streamedAttrs = jdomElementItem.getChildren();
					for (int indexAttr = 0; indexAttr < streamedAttrs.size(); indexAttr++) {
						Element jdomElementAttr = (Element) streamedAttrs.get(indexAttr);
						String attrName = jdomElementAttr.getName();
						String attrValue = jdomElementAttr.getValue();
						collectionItem.setPropertyValue(attrName, attrValue);

						if (isInheritedField) {
							collectionItem.getProperty(attrName).setDefaultValue(attrValue);
						}
					}
				}
				/* Delete previous items */
				while (collection.getCount() > jdomChildItems.size()) {
					collection.delete(collection.getCount() - 1);
				}
			} else if (!(propertyName.equalsIgnoreCase(OBJECT))) {
				/* It's a string property */
				String propertyValue = jdomElementProperty.getValue();
				component.setPropertyValue(propertyName, propertyValue);

				if (isInheritedField) {
					component.getProperty(propertyName).setDefaultValue(propertyValue);
				}
			}
		}

		component.setIsLoading(false);

		if (component instanceof IusCLControl controlInstance) {
			controlInstance.reCreateWnd();
		}

		/* Component references */
		IusCLApplication.findRefComponent(component);

		/* Read recursive element */
		List<?> jdomChildObjects = jdomElementParent.getChildren(OBJECT);

		for (int index = 0; index < jdomChildObjects.size(); index++) {
			Element jdomElementObject = (Element) jdomChildObjects.get(index);
			String objectName = jdomElementObject.getAttributeValue(NAME);
			/* See if the field was constructed in an ancestor form */
			IusCLComponent childComponent = ownerComponent.findOwnedComponentByName(objectName);
			if (childComponent == null) {
				/* Construct the form declared component */
				childComponent = (IusCLComponent) IusCLObjUtils.invokeFieldConstructor(ownerComponent, objectName,
						new IusCLParam(IusCLComponent.class, ownerComponent));

				/* Name */
				childComponent.setName(objectName);

				/* Parent */
				if ((childComponent instanceof IusCLControl childControl) && (component instanceof IusCLParentControl parentControl)) {
					childControl.setParent(parentControl);
				} else {
					childComponent.setParentComponent(component);
				}
			}
			readRecursiveElement(jdomElementObject, childComponent, ownerComponent, isInherited);
		}

		/* All children loaded */
		if (component instanceof IusCLContainerControl containerControl) {
			containerControl.doUpdateTabOrder();
		}
	}

	/* Write component */

	public String writeComponentRes(IusCLComponent componentInstance) {
		Hashtable<String, Element> jdomElementsByName = new Hashtable<>();
		Element jdomRootElement = new Element(OBJECT);
		writeRecursiveElement(jdomRootElement, componentInstance, jdomElementsByName);

		Document jdomDocument = new Document(jdomRootElement);
		Format jdomSerializeFormat = Format.getRawFormat();
		jdomSerializeFormat.setIndent("  ");
		jdomSerializeFormat.setLineSeparator("\n");
		XMLOutputter jdomSerializer = new XMLOutputter(jdomSerializeFormat);
		return jdomSerializer.outputString(jdomDocument);
	}

	private void writeRecursiveElement(Element jdomElementComponent, IusCLComponent componentInstance,
			Hashtable<String, Element> jdomElementsByName) {
		/* Write element properties */
		Vector<String> propertiesNamesVector = new Vector<>(componentInstance.getProperties().keySet());
		Collections.sort(propertiesNamesVector);
		Iterator<String> propertiesNamesIterator = propertiesNamesVector.iterator();
		while (propertiesNamesIterator.hasNext()) {
			String propertyName = propertiesNamesIterator.next();

			IusCLProperty property = componentInstance.getProperty(propertyName);

			if (property.getType().equalsIgnoreCase(IusCLPropertyType.ptCollection.name())) {
				/* It's a collection, yes!! */
				IusCLCollection collection = (IusCLCollection) IusCLObjUtils.invokeMethod(componentInstance, "get" + propertyName);

				if (collection.getCount() > 0) {
					Element jdomElementCollection = new Element(COLLECTION);
					Attribute jdomAttributeName = new Attribute(NAME, propertyName);
					jdomElementCollection.setAttribute(jdomAttributeName);
					jdomElementComponent.addContent(jdomElementCollection);

					for (int index = 0; index < collection.getCount(); index++) {
						IusCLCollectionItem collectionItem = collection.get(index);

						Element jdomElementItem = new Element(ITEM);

						/* Write collection item's properties, called 'attributes'.. */
						Vector<String> attrsNamesVector = new Vector<>(collectionItem.getProperties().keySet());
						Collections.sort(attrsNamesVector);
						Iterator<String> attrsNamesIterator = attrsNamesVector.iterator();
						while (attrsNamesIterator.hasNext()) {
							String attrName = attrsNamesIterator.next();
							String attrValue = collectionItem.getPropertyValue(attrName);

							if ((attrValue != null) && (!(attrValue.equalsIgnoreCase(collectionItem.getProperty(attrName).getDefaultValue())))) {
								/* Is not default, serialize attribute */
								Element elementAttr = new Element(attrName);
								elementAttr.setText(attrValue);
								jdomElementItem.addContent(elementAttr);
							}
						}
						jdomElementCollection.addContent(jdomElementItem);
					}
				}
			} else {
				/* It's a string value property */
				String propertyValue = componentInstance.getPropertyValue(propertyName);
				if ((propertyValue != null) && (!(propertyValue.equalsIgnoreCase(property.getDefaultValue())))) {
					/* Is not default, serialize */
					Element elementProperty = new Element(propertyName);
					elementProperty.setText(propertyValue);
					jdomElementComponent.addContent(elementProperty);
				}
			}
		}

		/* Write recursive element */
		for (int index = 0; index < componentInstance.getComponents().size(); index++) {
			IusCLComponent componentObject = componentInstance.getComponents().get(index);
			Element jdomElementObject = new Element(OBJECT);
			Attribute jdomAttributeName = new Attribute(NAME, componentObject.getName());
			jdomElementObject.setAttribute(jdomAttributeName);
			jdomElementsByName.put(componentObject.getName(), jdomElementObject);
			/* Parent */
			if (componentObject instanceof IusCLControl control) {
				IusCLWinControl parentWinControl = control.getParent();
				if (parentWinControl instanceof IusCLForm) {
					jdomElementComponent.addContent(jdomElementObject);
				} else {
					Element jdomParentElement = jdomElementsByName.get(parentWinControl.getName());
					jdomParentElement.addContent(jdomElementObject);
				}
			} else {
				IusCLComponent parentComponent = componentObject.getParentComponent();
				if (parentComponent == null) {
					jdomElementComponent.addContent(jdomElementObject);
				} else {
					if (parentComponent instanceof IusCLForm) {
						jdomElementComponent.addContent(jdomElementObject);
					} else {
						Element jdomParentElement = jdomElementsByName.get(parentComponent.getName());
						jdomParentElement.addContent(jdomElementObject);
					}
				}
			}
			writeRecursiveElement(jdomElementObject, componentObject, jdomElementsByName);
		}
	}
}
