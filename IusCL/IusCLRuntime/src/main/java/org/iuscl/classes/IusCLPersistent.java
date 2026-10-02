/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.classes;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.iuscl.events.IusCLEvent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.forms.IusCLForm;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.menus.IusCLMainMenu;
import org.iuscl.menus.IusCLMenuItem;
import org.iuscl.obj.IusCLObjUtils;
import org.iuscl.obj.IusCLParam;
import org.iuscl.system.IusCLObject;
import org.iuscl.sysutils.IusCLErrorUtils;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLPersistent extends IusCLObject {

	public enum IusCLPropertyType {
		ptInteger, ptString, ptBoolean, ptEnum, ptEvent, ptColor, ptCursor, ptStrings, ptPicture, ptIcon, ptFilter, ptComponent, ptCollection, ptFont,
		ptFontName
	}

	@Getter
	final Map<String, IusCLProperty> properties = new HashMap<>();

	@Getter
	@Setter
	boolean isLoading = false;

	public IusCLProperty getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public void removeProperty(String propertyName) {
		properties.remove(propertyName);
	}

	/* Define properties */

	public void defineProperty(String name, IusCLPropertyType propertyType, String defaultValue) {
		defineStandardProperty(name, propertyType, defaultValue, true, null, null);
	}

	public void defineProperty(String name, IusCLPropertyType propertyType, String defaultValue, Enum<?> enumeration) {
		defineStandardProperty(name, propertyType, defaultValue, true, enumeration, null);
	}

	public void defineProperty(String name, IusCLPropertyType propertyType, String defaultValue, Class<?> refClass) {
		defineStandardProperty(name, propertyType, defaultValue, true, null, refClass);
	}

	public void defineProperty(String name, IusCLPropertyType propertyType, String defaultValue, boolean published) {
		defineStandardProperty(name, propertyType, defaultValue, published, null, null);
	}

	private void defineStandardProperty(String name, IusCLPropertyType propertyType, String defaultValue, boolean published, Enum<?> enumeration,
			Class<?> refClass) {
		defineProperty(name, null, null, propertyType.name(), defaultValue, published, enumeration, refClass);
	}

	public void defineProperty(String name, String propertyValueSetter, String propertyValueGetter, String type, String defaultValue,
			boolean published, Enum<?> enumeration, Class<?> refClass) {
		IusCLProperty property = new IusCLProperty();
		property.setName(name);

		property.setPropertyValueSetter(propertyValueSetter);
		property.setPropertyValueGetter(propertyValueGetter);

		property.setType(type);
		property.setPublished(published);
		property.setDefaultValue(defaultValue);

		property.setEnumeration(enumeration);

		property.setRefClass(refClass);

		properties.put(name, property);
	}

	/* Read properties */

	public void setPropertyValueInvoke(String propertyName, IusCLParam params) {
		String getterMethodName = null;
		String setterMethodName = null;

		Object component = this;

		if (propertyName.indexOf(".") > -1) {
			/* Here it has multiple gets */
			String[] splits = propertyName.split("\\.");

			for (int index = 0; index < splits.length - 1; index++) {
				getterMethodName = "get" + splits[index];

				component = IusCLObjUtils.invokeMethod(component, getterMethodName);
			}

			setterMethodName = "set" + splits[splits.length - 1];
		} else {
			setterMethodName = "set" + propertyName;
		}

		IusCLObjUtils.invokeMethod(component, setterMethodName, params);
	}

	private void setPropertyValueString(String propertyName, String propertyValue) {
		setPropertyValueInvoke(propertyName, new IusCLParam(String.class, propertyValue));
	}

	private void setPropertyValueInteger(String propertyName, String propertyValue) {
		setPropertyValueInvoke(propertyName, new IusCLParam(Integer.class, Integer.valueOf(propertyValue)));
	}

	private void setPropertyValueBoolean(String propertyName, String propertyValue) {
		setPropertyValueInvoke(propertyName, new IusCLParam(boolean.class, Boolean.valueOf(propertyValue)));
	}

	@SuppressWarnings("unchecked")
	private void setPropertyValueEnum(String propertyName, String propertyValue) {
		Enum<?> enumeration = properties.get(propertyName).getEnumeration();

		setPropertyValueInvoke(propertyName, new IusCLParam(enumeration.getClass(), Enum.valueOf(enumeration.getClass(), propertyValue)));
	}

	private void setPropertyValueColor(String propertyName, String propertyValue) {
		IusCLColor color = null;

		if (propertyValue.indexOf("(") == 0) {
			String parseRGB = propertyValue.replace("(", "").replace(")", "");
			String[] vals = parseRGB.split(",");

			Integer red = Integer.parseInt(vals[0].trim());
			Integer green = Integer.parseInt(vals[1].trim());
			Integer blue = Integer.parseInt(vals[2].trim());

			color = new IusCLColor(red, green, blue);
		} else {
			color = new IusCLColor(IusCLStandardColors.valueOf(propertyValue));
		}

		setPropertyValueInvoke(propertyName, new IusCLParam(IusCLColor.class, color));
	}

	private void setPropertyValueFont(String propertyName, String propertyValue) { // NOSONAR
		/* ILB to be used in descendants */
	}

	private void setPropertyValueStringsFromResName(String propertyName, String propertyValue) {
		/* New strings from design */
		if (propertyValue.indexOf("*") == 0) {
			propertyValue = propertyValue.substring(1);
		}

		String inheritedValue = properties.get(propertyName).getDefaultValue();

		/* IusCLStrings on resource */
		IusCLStrings strings = new IusCLStrings();

		if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
			IusCLApplication.loadFromFormResource(strings, getPersistentForm().getClass(), propertyValue);

			/* Reset the inherited */
			if (!IusCLStrUtils.equalValues(inheritedValue, propertyValue)) {
				properties.get(propertyName).setDefaultValue("");
			}
		} else {
			String oldPropertyValue = getPropertyValue(propertyName);

			if (!IusCLStrUtils.equalValues(inheritedValue, oldPropertyValue)) {
				strings.setText("");
				/* Delete the resource file */
				String formsResFolder = IusCLApplication.getFormsResFolder(getPersistentForm().getClass());
				if (formsResFolder != null && IusCLStrUtils.isNotNullNotEmpty(oldPropertyValue)) {
					String resFileName = formsResFolder + oldPropertyValue.replace("/", IusCLFileUtils.getPathDelimiter());
					IusCLFileUtils.deleteFile(resFileName);
				}
			}
		}

		/* Set IusCLStrings */
		setPropertyValueInvoke(propertyName, new IusCLParam(IusCLStrings.class, strings));
	}

	private void setPropertyValuePictureFromResName(String propertyName, String propertyValue) {
		/* New picture from design */
		if (propertyValue.indexOf("*") == 0) {
			propertyValue = propertyValue.substring(1);
		}

		String inheritedValue = properties.get(propertyName).getDefaultValue();

		/* IusCLPicture on resource */
		IusCLPicture picture = new IusCLPicture();

		if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
			IusCLApplication.loadFromFormResource(picture, getPersistentForm().getClass(), propertyValue);

			/* Reset the inherited */
			if (!IusCLStrUtils.equalValues(inheritedValue, propertyValue)) {
				properties.get(propertyName).setDefaultValue("");
			}
		} else {
			String oldPropertyValue = getPropertyValue(propertyName);

			if (!IusCLStrUtils.equalValues(inheritedValue, oldPropertyValue)) {
				picture.setGraphic(null);
				/* Delete the resource file */
				String formsResFolder = IusCLApplication.getFormsResFolder(getPersistentForm().getClass());
				if (formsResFolder != null && IusCLStrUtils.isNotNullNotEmpty(oldPropertyValue)) {
					String resFileName = formsResFolder + oldPropertyValue.replace("/", IusCLFileUtils.getPathDelimiter());
					IusCLFileUtils.deleteFile(resFileName);
				}
			}
		}

		/* Set IusCLPicture */
		setPropertyValueInvoke(propertyName, new IusCLParam(IusCLPicture.class, picture));
	}

	private void setPropertyValueRefComponent(String propertyName, String propertyValue) {
		Class<?> refComponentClass = properties.get(propertyName).getRefClass();

		IusCLForm form = getPersistentForm();

		IusCLComponent refComponent = null;

		if (!propertyValue.trim().isEmpty()) {
			for (int index = 0; index < form.getComponents().size(); index++) {
				IusCLComponent indexComponent = form.getComponents().get(index);
				if (indexComponent.getName().equalsIgnoreCase(propertyValue)) {
					refComponent = indexComponent;
				}
			}

			if (refComponent == null) {
				/*
				 * Put a transient component to act as a temporary reference until the real
				 * component arrives
				 */
				IusCLApplication.putRefComponent(propertyValue, this, propertyName);

				if (refComponentClass.isAssignableFrom(IusCLMainMenu.class)) {
					/* Transient menu bar */
					IusCLMainMenu transientMainMenu = new IusCLMainMenu(form);
					transientMainMenu.setName("transientMainMenu");
					IusCLMenuItem transientMenuItem = new IusCLMenuItem(form);
					transientMenuItem.setName("transientMenuItem");
					transientMenuItem.setParentMenu(transientMainMenu);
					transientMenuItem.setCaption("");
					form.setMenu(transientMainMenu);
				}
			} else {
				setPropertyValueInvoke(propertyName, new IusCLParam(refComponentClass, refComponent));
			}
		} else {
			setPropertyValueInvoke(propertyName, new IusCLParam(refComponentClass, null));
		}
	}

	private void setPropertyValueCollection(String propertyName, String propertyValue) { // NOSONAR
		/* ILB to be used in descendants */
	}

	private void setPropertyValueEvent(String propertyName, String propertyValue) {
		IusCLEvent event = null;

		if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
			event = (IusCLEvent) IusCLObjUtils.invokeConstructor(properties.get(propertyName).getRefClass());

			// event = new IusCLEvent();
			event.setEventDeclaringInstance(this.getPersistentForm());
			event.setEventMethodName(propertyValue);
		}

		setPropertyValueInvoke(propertyName, new IusCLParam(properties.get(propertyName).getRefClass(), event));
	}

	public void setPropertyValue(String propertyName, String propertyValue) {
		if (!properties.containsKey(propertyName)) {
			String exceptionMessage = MessageFormat.format("Property not found: \"{0}\", for persistent: \"{1}\", ignoring for: \"setPropertyValue\"",
					propertyName, this.getPersistentName());
			log.warn(exceptionMessage);
			IusCLErrorUtils.showErrorDialog(exceptionMessage);
			return;
		}

		IusCLProperty property = properties.get(propertyName);

		IusCLPropertyType propertyType = null;

		try {
			propertyType = IusCLPropertyType.valueOf(property.getType());
		} catch (Exception exception) {
			/* Non-standard property */
		}

		if (propertyType != null) {
			/* Standard property */
			switch (propertyType) {
			case ptBoolean:
				setPropertyValueBoolean(propertyName, propertyValue);
				break;
			case ptCollection:
				setPropertyValueCollection(propertyName, propertyValue);
				break;
			case ptColor:
				setPropertyValueColor(propertyName, propertyValue);
				break;
			case ptComponent:
				setPropertyValueRefComponent(propertyName, propertyValue);
				break;
			case ptEnum:
				setPropertyValueEnum(propertyName, propertyValue);
				break;
			case ptCursor, ptFilter, ptFontName, ptString:
				setPropertyValueString(propertyName, propertyValue);
				break;
			case ptFont:
				setPropertyValueFont(propertyName, propertyValue);
				break;
			case ptIcon, ptPicture:
				setPropertyValuePictureFromResName(propertyName, propertyValue);
				break;
			case ptInteger:
				setPropertyValueInteger(propertyName, propertyValue);
				break;
			case ptStrings:
				setPropertyValueStringsFromResName(propertyName, propertyValue);
				break;
			case ptEvent:
				setPropertyValueEvent(propertyName, propertyValue);
				break;
			}
		} else {
			/* Non-standard property, invoke setter */
			IusCLObjUtils.invokeMethod(this, property.getPropertyValueSetter(), new IusCLParam(String.class, propertyName),
					new IusCLParam(String.class, propertyValue));
		}
	}

	/* Write properties */

	public Object getPropertyValueInvoke(String propertyName) {
		String getMethodName = null;
		Object component = this;

		if (propertyName.indexOf(".") > -1) {
			/* Here it has multiple gets */
			String[] splits = propertyName.split("\\.");
			for (int index = 0; index < splits.length - 1; index++) {
				getMethodName = "get" + splits[index];
				component = IusCLObjUtils.invokeMethod(component, getMethodName);
			}
			getMethodName = "get" + splits[splits.length - 1];
		} else {
			getMethodName = "get" + propertyName;
		}
		return IusCLObjUtils.invokeMethod(component, getMethodName);
	}

	private String getPropertyValueString(String propertyName) {
		return (String) getPropertyValueInvoke(propertyName);
	}

	private String getPropertyValueInteger(String propertyName) {
		Integer propertyValue = (Integer) getPropertyValueInvoke(propertyName);
		return propertyValue.toString();
	}

	private String getPropertyValueBoolean(String propertyName) {
		boolean propertyValue = (boolean) getPropertyValueInvoke(propertyName);
		return IusCLStrUtils.booleanToString(propertyValue);
	}

	private String getPropertyValueEnum(String propertyName) {
		Enum<?> propertyValue = (Enum<?>) getPropertyValueInvoke(propertyName);
		return propertyValue.toString();
	}

	private String getPropertyValueColor(String propertyName) {
		IusCLColor color = (IusCLColor) getPropertyValueInvoke(propertyName);
		return color.getAsString();
	}

	private String getPropertyValueFont(String propertyName) { // NOSONAR
		return "(IusCLFont)"; // NOSONAR
	}

	private String getPropertyValueStringsAsResName(String propertyName) {
		String inheritedValue = properties.get(propertyName).getDefaultValue();

		if (IusCLStrUtils.isNotNullNotEmpty(inheritedValue)) {
			return inheritedValue;
		}

		IusCLStrings strings = (IusCLStrings) getPropertyValueInvoke(propertyName);

		if (strings.getIsEmpty()) {
			return "";
		}

		return getPersistentFormAndResName(propertyName) + ".txt";
	}

	private String getPropertyValuePictureAsResName(String propertyName) {
		String inheritedValue = properties.get(propertyName).getDefaultValue();

		if (IusCLStrUtils.isNotNullNotEmpty(inheritedValue)) {
			return inheritedValue;
		}

		IusCLPicture picture = (IusCLPicture) getPropertyValueInvoke(propertyName);

		if (picture == null) {
			return "";
		}

		if (picture.getIsEmpty()) {
			return "";
		}

		return getPersistentFormAndResName(propertyName) + "." + picture.getPictureExtension();
	}

	private String getPropertyValueRefComponent(String propertyName) {
		IusCLComponent refComponent = (IusCLComponent) getPropertyValueInvoke(propertyName);

		if (refComponent == null) {
			return "";
		}

		return refComponent.getName();
	}

	private String getPropertyValueCollection(String propertyName) { // NOSONAR
		return "propertyValue"; // NOSONAR
	}

	private String getPropertyValueEvent(String propertyName) {
		IusCLEvent event = (IusCLEvent) getPropertyValueInvoke(propertyName);

		if (IusCLEvent.isDefinedEvent(event)) {
			return event.getEventMethodName();
		}

		return null;
	}

	public String getPropertyValue(String propertyName) {
		String result = null;

		if (!properties.containsKey(propertyName)) {
			String exceptionMessage = MessageFormat.format(
					"Not found property: \"{0}\", for persistent: \"{1}\", returning null for: \"getPropertyValue\"", propertyName,
					this.getPersistentName());
			log.warn(exceptionMessage);
			IusCLErrorUtils.showErrorDialog(exceptionMessage);
			return null;
		}

		IusCLProperty property = properties.get(propertyName);

		IusCLPropertyType propertyType = null;
		try {
			propertyType = IusCLPropertyType.valueOf(property.getType());
		} catch (Exception exception) {
			/* Non-standard property */
		}
		if (propertyType != null) {
			/* Standard property */
			switch (propertyType) {
			case ptBoolean:
				result = getPropertyValueBoolean(propertyName);
				break;
			case ptCollection:
				result = getPropertyValueCollection(propertyName);
				break;
			case ptColor:
				result = getPropertyValueColor(propertyName);
				break;
			case ptComponent:
				result = getPropertyValueRefComponent(propertyName);
				break;
			case ptEnum:
				result = getPropertyValueEnum(propertyName);
				break;
			case ptCursor, ptFilter, ptFontName, ptString:
				result = getPropertyValueString(propertyName);
				break;
			case ptFont:
				result = getPropertyValueFont(propertyName);
				break;
			case ptIcon, ptPicture:
				result = getPropertyValuePictureAsResName(propertyName);
				break;
			case ptInteger:
				result = getPropertyValueInteger(propertyName);
				break;
			case ptStrings:
				result = getPropertyValueStringsAsResName(propertyName);
				break;
			case ptEvent:
				result = getPropertyValueEvent(propertyName);
				break;
			}
		} else {
			/* Non-standard property, invoke getter */
			result = (String) IusCLObjUtils.invokeMethod(this, properties.get(propertyName).getPropertyValueGetter(),
					new IusCLParam(String.class, propertyName));
		}

		return result;
	}

	//
	// public void assign(IusCLPersistent source) {
	// /* Copy the property values */
	// Vector<String> propertiesNamesVector = new
	// Vector<>(this.getProperties().keySet());
	// Iterator<String> propertiesNamesIterator = propertiesNamesVector.iterator();
	// while (propertiesNamesIterator.hasNext()) {
	//
	// String propertyName = propertiesNamesIterator.next();
	//
	// IusCLProperty propertySource = source.getProperty(propertyName);
	// if (propertySource != null) {
	//
	// this.setPropertyValue(propertyName, source.getPropertyValue(propertyName));
	// }
	// }
	// }

	public void assign() {
		/* Apply all defined properties */
		Vector<String> propertiesNamesVector = new Vector<>(this.getProperties().keySet());
		Iterator<String> propertiesNamesIterator = propertiesNamesVector.iterator();

		isLoading = true;

		while (propertiesNamesIterator.hasNext()) {
			String propertyName = propertiesNamesIterator.next();

			if (!((this instanceof IusCLForm) && (propertyName.equalsIgnoreCase("Visible")))) {
				this.setPropertyValue(propertyName, this.getPropertyValue(propertyName));
			}
		}

		isLoading = false;
	}

	public IusCLComponent getPersistentComponent() {
		if (this instanceof IusCLComponent component) {
			return component;
		}
		if (this instanceof IusCLCollectionItem collectionItem) {
			return collectionItem.getCollection().getPropertyComponent();
		}
		if (this instanceof IusCLCollection collection) {
			return collection.getPropertyComponent();
		}

		/* Something else? */
		return null;
	}

	public IusCLForm getPersistentForm() {
		return getPersistentComponent().findForm();
	}

	public String getPersistentName() {
		if (this instanceof IusCLComponent component) {
			return component.getName();
		}

		if (this instanceof IusCLCollection collection) {
			String resName = collection.getPropertyComponent().getName();
			resName = resName + collection.getPropertyName();

			return resName;
		}

		if (this instanceof IusCLCollectionItem collectionItem) {
			IusCLCollection collection = collectionItem.getCollection();

			String resName = collection.getPropertyComponent().getName();
			resName = resName + collection.getPropertyName();
			resName = resName + collection.indexOf(collectionItem);

			return resName;
		}

		/* Something else? */
		return null;
	}

	public String getPersistentFormAndResName(String propertyName) {
		return getPersistentForm().getClass().getSimpleName() + "/" + getPersistentName() + "_" + propertyName;
	}

	public String getDisplayName() {
		return null;
	}
}
