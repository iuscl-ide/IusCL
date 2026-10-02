/* ****************************************************************************************************
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
**************************************************************************************************** */
package org.iuscl.plugin.help;

import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;

/* **************************************************************************************************** */
public class IusCLHelpReference extends IusCLHelpChapter {

	/* **************************************************************************************************** */
	public static void main(String[] args) {
		
		IusCLHelpReference helpReference = new IusCLHelpReference(args[0]);
		
		helpReference.generate("reference", "IusCLHelpReference.txt");
	}

	/* **************************************************************************************************** */
	public IusCLHelpReference(String homeFolder) {

		super(homeFolder);
	}

	/* **************************************************************************************************** */
	@Override
	public void generate(String chapterFolderName, String srcFileName) {

		super.generate(chapterFolderName, srcFileName);
		
		/* Index */
		IusCLFileUtils.copyFile(helpSrcFolder + chapterFolderName + "\\_index_dispatch.html", helpDestFolder + chapterFolderName + "\\_index_dispatch.html");
		//IusCLFileUtils.copyFile(helpSrcFolder + chapterFolderName + "\\_index_dispatch.html", webDestFolder + chapterFolderName + "\\_index_dispatch.html");

		
		String srcReferenceJavaFolder = homeFolder + "\\IusCLRuntime\\src\\main\\java";
		String srcReferenceImagesFolder = homeFolder + "\\IusCLRuntime\\res\\usr\\resources\\designintf\\images\\";

		LinkedHashMap<String, String> prefixGrouping = new LinkedHashMap<String, String>();
		prefixGrouping.put("IusCL", "IusCL classes");
		prefixGrouping.put("IusCLSwt", "IusCL SWT classes");
		prefixGrouping.put("IusCLDesign", "IusCL Design classes");
		

		
		Document jdomTocDocument = new Document();
		jdomTocDocument.addContent(new ProcessingInstruction("NLS", "TYPE=\"org.eclipse.help.toc\""));

		Document jdomIndexDocument = new Document();
		jdomIndexDocument.addContent(new ProcessingInstruction("NLS", "TYPE=\"org.eclipse.help.index\""));
		/* Index */
		Element indexElement = new Element("index");
		jdomIndexDocument.setRootElement(indexElement);

		
		JavaDocBuilder builder = new JavaDocBuilder();
		
		/* Adding all .java files in a source tree (recursively). */
		builder.addSourceTree(new File(srcReferenceJavaFolder));
		
		final Map<String, String> hierarchy = new HashMap<>();
	
		Vector<String> classesFullyQualifiedNames = new Vector<String>();
		
		for (int index = 0; index < builder.getSources().length; index++) {
			
			JavaSource javaSource = builder.getSources()[index];
			
			JavaClass javaClass = javaSource.getClasses().get(0);
			
			String parents = javaClass.getName() + ".";
			
			JavaClass superClass = javaClass.getSuperJavaClass();
			
			while (superClass != null) {
				parents = superClass.getName() + "." + parents;
				superClass = superClass.getSuperJavaClass();
			}

			//if (javaClass.getName().startsWith("IusCLSwt"))
			hierarchy.put(javaClass.getName(), parents);
			
			
			classesFullyQualifiedNames.add(javaClass.getFullyQualifiedName());

//			System.out.println(javaClass.getName() + " " + 
//					parent);
			
		}
		
		/* TOC */
		Element tocElement = new Element("toc");
		tocElement.setAttribute("label", "IusCL Object and Component Reference");
		tocElement.setAttribute("link_to", tocsFolder + "toc_userguide.xml#" + "reference");
		tocElement.setAttribute("href", destHRef + "reference" + ".html");
		jdomTocDocument.setRootElement(tocElement);
		
		/* Root */
		IusCLHelpContent referenceTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, "referenceTopic");
		
		referenceTextContent.addTitle("IusCL Object and Component Reference");
		referenceTextContent.addShortDescriptionWithBar("Object and Component Help is a reference to all objects, components, controls and static functions");
		String seeAlsoContent = "&nbsp;<br>" + 
			"<a href=\"hierarchyUsrPdf.html\"><b>IusCL Usr and Pdf Hierarchy</b></a><br>" +
			"<a href=\"packages.html\"><b>Packages list</b></a><br>";
		referenceTextContent.addContent(seeAlsoContent);

		
		saveToFile(referenceTextContent.getContent(), chapterFolderName + "\\reference.html");
		
		/* Hierarchy */
		Element hierarchyElement = new Element("topic");
		hierarchyElement.setAttribute("label", "Hierarchy");
		hierarchyElement.setAttribute("href", destHRef + "hierarchy" + ".html");
		tocElement.addContent(hierarchyElement);

		IusCLHelpContent hierarchyTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, "hierarchyTopic");
		
		hierarchyTextContent.addTitle("Hierarchy");
		hierarchyTextContent.addShortDescriptionWithBar("Class hierarchies per IusCL package");
		
		hierarchyTextContent.addContent("<a href=\"hierarchyUsrPdf.html\">IusCL Usr and Pdf Hierarchy</a><br>");
		
		hierarchyTextContent.addCategoryOnBar("seealso", "See also");
		hierarchyTextContent.addEmptyContentLine();
		hierarchyTextContent.addCategoryHeader("seealso", "See also");
		seeAlsoContent = "&nbsp;<br>" + 
			"<a href=\"reference.html\"><b>IusCL Object and Component Reference</b></a><br>" +
			"<a href=\"packages.html\"><b>Packages list</b></a><br>";
		hierarchyTextContent.addContent(seeAlsoContent);


		saveToFile(hierarchyTextContent.getContent(), chapterFolderName + "\\hierarchy.html");

		Element hierarchyUsrPdfElement = new Element("topic");
		hierarchyUsrPdfElement.setAttribute("label", "IusCL Usr and Pdf Hierarchy");
		hierarchyUsrPdfElement.setAttribute("href", destHRef + "hierarchyUsrPdf.html");
		hierarchyElement.addContent(hierarchyUsrPdfElement);

		IusCLHelpContent hierarchyUsrPdfTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, "hierarchyUsrPdf");
		
		hierarchyUsrPdfTextContent.addTitle("IusCL Usr and Pdf Hierarchy");
		hierarchyUsrPdfTextContent.addShortDescriptionWithBar("Navigate the classes by parent");
		
		Vector<String> hierarchyVector = new Vector<String>(hierarchy.values());
		Collections.sort(hierarchyVector);

		Vector<String> prefixes = new Vector<String>(prefixGrouping.keySet());

		for (String prefix: prefixes) {
			
			String deep = "Object.";
			Integer indent = 0;
			Boolean addedTitle = false;
			
			for (String parent : hierarchyVector) {
				
				while (!parent.startsWith(deep)) {
	
					deep = deep.substring(0, deep.lastIndexOf("."));
					deep = deep.substring(0, deep.lastIndexOf(".") + 1);
	
					indent = indent - 1;
	
				}
				
				/* Trailer class name */
				String par[] = parent.split("\\.");
				
				String currentClass = par[par.length - 1]; 
				String currentParentClass = par[par.length - 2]; 
				
				indent = indent + 1;
	
				if (IusCLStrUtils.equalValues(IusCLStrUtils.findBiggestPrefix(currentClass, prefixes), prefix)) {
					
					if (indent > 1) {
						if (!IusCLStrUtils.equalValues(IusCLStrUtils.findBiggestPrefix(currentClass, prefixes), 
								IusCLStrUtils.findBiggestPrefix(currentParentClass, prefixes))) {
							indent = 1;
						}
					}
					
					String classBox = "\n    <div style=\"padding-left: " + (indent - 1) * 24 + "px; padding-bottom: 2px\">" + 
					"<a href=\"" + currentClass +".html\"><b>" + currentClass + 
					"</b></a>" + "</div>";
		
					if (addedTitle == false) {

						hierarchyUsrPdfTextContent.addHTML("&nbsp;");
						hierarchyUsrPdfTextContent.addHeader(prefixGrouping.get(prefix));
						hierarchyUsrPdfTextContent.addHTML("&nbsp;");

						addedTitle = true;
					}
					
					hierarchyUsrPdfTextContent.addHTML(classBox);
					
					System.out.println(String.format("%" + 2 * indent + "s", " ") + currentClass);
				}
				
				deep = deep + currentClass + ".";
			}

		}
		
		hierarchyUsrPdfTextContent.addCategoryOnBar("seealso", "See also");
		hierarchyUsrPdfTextContent.addEmptyContentLine();
		hierarchyUsrPdfTextContent.addCategoryHeader("seealso", "See also");
		seeAlsoContent = "&nbsp;<br>" + 
			"<a href=\"reference.html\"><b>IusCL Object and Component Reference</b></a><br>" +
			"<a href=\"packages.html\"><b>Packages list</b></a><br>";
		hierarchyUsrPdfTextContent.addContent(seeAlsoContent);

		saveToFile(hierarchyUsrPdfTextContent.getContent(), chapterFolderName + "\\hierarchyUsrPdf.html");

		/* Packages */
		Element packagesElement = new Element("topic");
		packagesElement.setAttribute("label", "Packages");
		packagesElement.setAttribute("href", destHRef + "packages" + ".html");
		tocElement.addContent(packagesElement);

		IusCLHelpContent packagesTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, "packagesTopic");

		packagesTextContent.addTitle("Packages");
		
		packagesTextContent.addShortDescriptionWithBar("Navigate the classes by Java package");
		
		packagesTextContent.addStartList("IusCL Usr and Pdf Packages");

		Collections.sort(classesFullyQualifiedNames);
		
		String oldPackageName = null;
		for (String fullClassName : classesFullyQualifiedNames) {
			
			JavaClass javaClass = builder.getClassByName(fullClassName);
		
			String packageName = javaClass.getPackageName();
			
			if (!IusCLStrUtils.equalValues(oldPackageName, packageName)) {
				
				packagesTextContent.addSubItem("<a href=\"" + packageName +".html\">" + packageName + "</a>");
				oldPackageName = packageName;
			}
		}

		packagesTextContent.addEndList();
		
		packagesTextContent.addCategoryOnBar("seealso", "See also");
		packagesTextContent.addEmptyContentLine();
		packagesTextContent.addCategoryHeader("seealso", "See also");
		seeAlsoContent = "&nbsp;<br>" + 
			"<a href=\"reference.html\"><b>IusCL Object and Component Reference</b></a><br>" +
			"<a href=\"hierarchyUsrPdf.html\"><b>IusCL Usr and Pdf Hierarchy</b></a><br>";
		packagesTextContent.addContent(seeAlsoContent);
		
		saveToFile(packagesTextContent.getContent(), chapterFolderName + "\\packages.html");
		
		
//		Collections.sort(classesFullyQualifiedNames);
		oldPackageName = null;

		Element packageElement = null;

		IusCLHelpContent packageTextContent = null;
		for (String fullClassName : classesFullyQualifiedNames) {
			
			JavaClass javaClass = builder.getClassByName(fullClassName);
			
			
			String packageName = javaClass.getPackageName();
			
			if (!IusCLStrUtils.equalValues(oldPackageName, packageName)) {

				if (oldPackageName != null) {
					
					packageTextContent.addEndList();
					
					packageTextContent.addCategoryOnBar("seealso", "See also");
					packageTextContent.addEmptyContentLine();
					packageTextContent.addCategoryHeader("seealso", "See also");
					seeAlsoContent = "&nbsp;<br>" + 
						"<a href=\"packages.html\"><b>Packages list</b></a><br>";
					packageTextContent.addContent(seeAlsoContent);

					saveToFile(packageTextContent.getContent(), chapterFolderName + "\\" + oldPackageName + ".html");
				}
				
				oldPackageName = packageName;

				/* Package */
				packageElement = new Element("topic");
				packageElement.setAttribute("label", packageName);
				packageElement.setAttribute("href", destHRef + packageName + ".html");
				packagesElement.addContent(packageElement);

				packageTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, packageName);

				packageTextContent.addTitle("<i>Package</i> " + packageName);
				
				packageTextContent.addShortDescriptionWithBar("$shortDescription");
				
				packageTextContent.addCategoryOnBar("classes", "Classes");
				packageTextContent.addCategoryHeader("classes", "Classes");
				packageTextContent.addStartList("In " + packageName);
				
				System.out.println(oldPackageName);
			}
			
			String className = javaClass.getName();
			
			/* Class */
			Element classTocElement = new Element("topic");
			classTocElement.setAttribute("label", className);
			classTocElement.setAttribute("href", destHRef + className + ".html");
			packageElement.addContent(classTocElement);

			Element classIndexElement = addIndexEntry(indexElement, className, destHRef + className + ".html"); 
			
			IusCLHelpContent classTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, className);
			
			if (IusCLFileUtils.fileExists(srcReferenceImagesFolder + className.replace("IusCL", "IusCLDesign") + ".gif")) {
				
				IusCLFileUtils.copyFile(srcReferenceImagesFolder + className.replace("IusCL", "IusCLDesign") + ".gif", 
						destImagesFolder + className + ".gif");
//				IusCLFileUtils.copyFile(srcReferenceImagesFolder + className.replace("IusCL", "IusCLDesign") + ".gif", 
//						webDestImagesFolder + className + ".gif");
				
				classTextContent.addTitleWithImage(className, "reference/" + className + ".gif");
			}
			else {
				classTextContent.addTitle(className);
			}
			
			/* Class hierarchy */
			String classParents = hierarchy.get(className);
			String[] classParentClasses = classParents.split("\\.");
			
			classParents = "";
			for (int index = 1; index < classParentClasses.length - 1; index++) {
				String classParent = classParentClasses[index];
				classParents = classParents + "<a href=\"" + classParent + ".html\">" + classParent + "</a><br>&nbsp;|<br>";
			}
			classParents = classParents + "<b><a href=\"" + className + ".html\">" + className + "</a></b><br>";

			
			classTextContent.addHierarchyOnBar(classParents);
			
//			classTextContent.addCategoryOnBar("properties", "Properties");
//			classTextContent.addCategoryOnBar("methods", "Methods");
//			classTextContent.addCategoryOnBar("events", "Events");
//			classTextContent.addCategoryOnBar("examples", "Examples");
//			classTextContent.addCategoryOnBar("seealso", "See also");

			classTextContent.addShortDescriptionWithBar("$shortDescription");
			classTextContent.addHeader("Package");
			classTextContent.addContent("<a href=\"" + packageName +".html\">" + packageName + "</a>");
		
			Boolean putCategoryHeader = null;
			Boolean putStartParent = null;
			Boolean putEndParent = null;
			String parentClassName = null;
			

			/* Enumerations */
			putStartParent = true;
			putEndParent = false;
			String enumsContent = "";

			for (JavaClass javaSubClass: javaClass.getNestedClasses()) {
				if ((javaSubClass.isEnum()) && (javaSubClass.isPublic())) {
					
					System.out.println("enum = " + javaSubClass.getName() + " - "  + javaSubClass.getClassNamePrefix());
					
					if (putStartParent == true) {
						classTextContent.addHeader("Public Enums");
						putStartParent = false; 
						putEndParent = true;
					}
					
					String enumName = javaSubClass.getName();
					String enumLink = destHRef + enumName + ".html";

					Element enumIndexElement = addIndexEntry(indexElement, enumName, enumLink);
					addIndexEntry(classIndexElement, enumName, enumLink);

					enumsContent = enumsContent + "<a href=\"" + enumName + ".html\">" + enumName + "</a><br>";
										
					IusCLHelpContent enumTextContent = new IusCLHelpContent(homeFolder, srcText, srcFile, className + "." + enumName);
					
					enumTextContent.addTitle("<i>Enumeration</i> " + enumName);
					enumTextContent.addShortDescriptionWithBar("$shortDescription");
					
					enumTextContent.addHeader("Class");
					enumTextContent.addContent("<a href=\"" + className + ".html\">" + className + "</a>");
					
					enumTextContent.addCategoryOnBar("fields", "Fields");
					enumTextContent.addCategoryHeader("fields", "Fields");
					enumTextContent.addStartList("In " + enumName);
					
					
					for (JavaField enumField: javaSubClass.getFields()) {
						enumTextContent.addSubItem("$" + enumField.getName());
						
						addIndexEntry(enumIndexElement, enumField.getName(), enumLink);

						addIndexSubEntry(indexElement, enumField.getName(), enumName, enumLink);
					}
					
					enumTextContent.addEndList();
					
					saveToFile(enumTextContent.getContent(), chapterFolderName + "\\" + enumName + ".html");
				}
			}
			
			if (putEndParent == true) {
				classTextContent.addContent(enumsContent);
			}

			classTextContent.addHeader("Description");
			classTextContent.addContent("$longDescription");

			/* Properties */
			ArrayList<String> propertyMethodsList = new ArrayList<String>();
			final Map<String, String> propertyGetterMethod = new HashMap<>();
			final Map<String, String> propertySetterMethod = new HashMap<>();

			putCategoryHeader = false;
			parentClassName = null;
			putStartParent = false;
			putEndParent = false;
			
			if (fullClassName.equalsIgnoreCase("org.iuscl.forms.IusCLApplication")) {
				
				System.out.println(fullClassName);
			}
			for (JavaMethod javaMethod: javaClass.getMethods(true)) {
				
				String parentClassNameCurrent = javaMethod.getParentClass().getName();
				
				if (!IusCLStrUtils.equalValues(parentClassNameCurrent, "Object")) {

					if (!IusCLStrUtils.equalValues(parentClassName, parentClassNameCurrent)) {
						if (putEndParent == true) {
							classTextContent.addEndList();
							putEndParent = false;
						}
						parentClassName = parentClassNameCurrent;
						putStartParent = true; 
					}

					if (javaMethod.isConstructor()) {
						String constructorText = javaMethod.getCodeBlock();

						int pos = 0;
						while (constructorText.indexOf("defineProperty(", pos) > -1) {

							String propertyDefine = IusCLStrUtils.subStringBetween(constructorText, "defineProperty(", ");", pos);
							
							String propertyName = IusCLStrUtils.subStringBetween(propertyDefine, "\"", "\", ");
							String propertyMethods = findPropertyMethods(javaClass, propertyName, propertyMethodsList,
									propertyGetterMethod, propertySetterMethod);

							String propertyType = IusCLStrUtils.subStringBetween(propertyDefine, "\", ", ", ");
							if (!IusCLStrUtils.equalValues(propertyType, "IusCLPropertyType.ptEvent")) {

								if (putCategoryHeader == false) {
									classTextContent.addCategoryOnBar("properties", "Properties");
									classTextContent.addCategoryHeader("properties", "Properties");
									putCategoryHeader = true;
								}

								if (putStartParent == true) {
									if (IusCLStrUtils.equalValues(parentClassName, className)) {
										classTextContent.addStartList("In " + parentClassNameCurrent);
									}
									else {
										classTextContent.addStartList("Derived from " + parentClassNameCurrent);	
									}
									putStartParent = false; 
									putEndParent = true;
								}

//								String propertyLink = parentClassName + "_" + propertyName + "_property";
								String propertyLink = propertyName + "_property";

								if (IusCLStrUtils.equalValues(parentClassName, className)) {
									
									classTextContent.addStartSubItem(propertyLink, "<b>" + propertyName + "</b></a> " + propertyMethods);
									
									classTextContent.addContent(propertyLink + "_smallDescription");
									
									classTextContent.addHeader("Syntax");
									String getterMethod = propertyGetterMethod.get(propertyName);
									if (getterMethod != null) {
										classTextContent.addJavaCodeFunc(getterMethod);	
									}
									String setterMethod = propertySetterMethod.get(propertyName);
									if (setterMethod != null) {
										classTextContent.addJavaCodeFunc(setterMethod);	
									}

									classTextContent.addHeader("Description");
									classTextContent.addContent(propertyLink + "_largeDescription");
									
									classTextContent.addEndSubItem();
									
									/* Index */
									String propertyIndexEntry = propertyName + " (" + propertyMethods + ")";
									String propertyIndexLink = destHRef + "_index_dispatch.html?page=" + className + ".html#" + propertyLink;
									addIndexEntry(classIndexElement, propertyIndexEntry, propertyIndexLink);
									
									addIndexSubEntry(indexElement, propertyIndexEntry, className, propertyIndexLink);

									if (propertyMethods != null) {
										String getterMethodShort = null;
										String setterMethodShort = null;
										if (propertyMethods.indexOf("/") > -1) {
											getterMethodShort = IusCLStrUtils.subStringBetween(propertyMethods, null, " / ");
											setterMethodShort = IusCLStrUtils.subStringBetween(propertyMethods, " / ", null);
										}
										else {
											if (getterMethod != null) {
												getterMethodShort = propertyMethods;
											}
											if (setterMethod != null) {
												setterMethodShort = propertyMethods;
											}
										}
										
										if (getterMethodShort != null) {
											addIndexSubEntry(indexElement, getterMethodShort, className, propertyIndexLink);
										}
										if (setterMethodShort != null) {
											addIndexSubEntry(indexElement, setterMethodShort, className, propertyIndexLink);
										}
									}
								}
								else {
									String parentPropertyLink = parentClassName +".html#" + propertyName + "_property";
									classTextContent.addSubItem("<a id=\"" + propertyName + "_property"  + "\" href=\"" + parentPropertyLink + "\"><b>" + 
											propertyName + "</b></a> " + propertyMethods);
								}

							}
							pos = constructorText.indexOf("defineProperty(", pos) + 1;
						}
					}
				}					
			}
			
			if (putEndParent == true) {
				classTextContent.addEndList();
				putEndParent = false;
			}

			if (putCategoryHeader == true) {
				classTextContent.addEmptyContentLine();
			}

			/* Methods */
			putCategoryHeader = false;
			
			parentClassName = null;
			putStartParent = false;
			putEndParent = false;
			for (JavaMethod javaMethod: javaClass.getMethods(true)) {
				
				String parentClassNameCurrent = javaMethod.getParentClass().getName();

				if (!IusCLStrUtils.equalValues(parentClassNameCurrent, "Object")) {
					
					if (!IusCLStrUtils.equalValues(parentClassName, parentClassNameCurrent)) {
						if (putEndParent == true) {
							classTextContent.addEndList();
							putEndParent = false;
						}
						parentClassName = parentClassNameCurrent;
						putStartParent = true; 
					}
	
					if (!propertyMethodsList.contains(javaMethod.getName())) {
						
						if (putCategoryHeader == false) {
							classTextContent.addCategoryOnBar("methods", "Methods");
							classTextContent.addCategoryHeader("methods", "Methods");
							putCategoryHeader = true;
						}

						if (putStartParent == true) {
							if (IusCLStrUtils.equalValues(parentClassName, className)) {
								classTextContent.addStartList("In " + parentClassNameCurrent);
							}
							else {
								classTextContent.addStartList("Derived from " + parentClassNameCurrent);	
							}
							putStartParent = false; 
							putEndParent = true;
						}
						
						String callSignature = javaMethod.getCallSignature();
						String callSignatureLink = callSignature.replace(" ", "_") + "_method";
						
						if (IusCLStrUtils.equalValues(parentClassName, className)) {
							classTextContent.addStartSubItem(callSignatureLink, callSignature + "</a>");
							
							classTextContent.addContent(callSignatureLink + "_smallDescription");
							
							classTextContent.addHeader("Syntax");
							classTextContent.addJavaCodeFunc(javaMethod.getDeclarationSignature(false));

							classTextContent.addHeader("Description");
							classTextContent.addContent(callSignatureLink + "_largeDescription");

							classTextContent.addEndSubItem();
							
							/* Index */
							String callSignatureIndexLink = destHRef + "_index_dispatch.html?page=" + className + ".html#" + callSignatureLink;

							addIndexEntry(classIndexElement, callSignature, callSignatureIndexLink);
							
							addIndexSubEntry(indexElement, callSignature, className, callSignatureIndexLink);
						}
						else {
							String parentCallSignatureLink = parentClassName +".html#" + callSignatureLink;
							classTextContent.addSubItem("<a href=\"" + parentCallSignatureLink + "\">" + callSignature + "</a>");
						}
					}
				}

			}
			
			if (putEndParent == true) {
				classTextContent.addEndList();
				putEndParent = false;
			}
			
			if (putCategoryHeader == true) {
				classTextContent.addEmptyContentLine();
			}

			/* Events */
			putCategoryHeader = false;

			parentClassName = null;
			putStartParent = false;
			putEndParent = false;
			for (JavaMethod javaMethod: javaClass.getMethods(true)) {
				
				String parentClassNameCurrent = javaMethod.getParentClass().getName();
				
				if (!IusCLStrUtils.equalValues(parentClassNameCurrent, "Object")) {

					if (!IusCLStrUtils.equalValues(parentClassName, parentClassNameCurrent)) {
						if (putEndParent == true) {
							classTextContent.addEndList();
							putEndParent = false;
						}
						parentClassName = parentClassNameCurrent;
						putStartParent = true; 
					}

					if (javaMethod.isConstructor()) {
						String constructorText = javaMethod.getCodeBlock();

						int pos = 0;
						while (constructorText.indexOf("defineProperty(", pos) > -1) {

							String propertyDefine = IusCLStrUtils.subStringBetween(constructorText, "defineProperty(", ");", pos);
							
							String propertyName = IusCLStrUtils.subStringBetween(propertyDefine, "\"", "\", ");
							String propertyMethods = findPropertyMethods(javaClass, propertyName, propertyMethodsList,
									propertyGetterMethod, propertySetterMethod);

							String propertyType = IusCLStrUtils.subStringBetween(propertyDefine, "\", ", ", ");
							if (IusCLStrUtils.equalValues(propertyType, "IusCLPropertyType.ptEvent")) {

								if (putCategoryHeader == false) {
									classTextContent.addCategoryOnBar("events", "Events");
									classTextContent.addCategoryHeader("events", "Events");
									putCategoryHeader = true;
								}

								if (putStartParent == true) {
									if (IusCLStrUtils.equalValues(parentClassName, className)) {
										classTextContent.addStartList("In " + parentClassNameCurrent);
									}
									else {
										classTextContent.addStartList("Derived from " + parentClassNameCurrent);	
									}
									putStartParent = false; 
									putEndParent = true;
								}

								String eventLink = propertyName + "_event";

								if (IusCLStrUtils.equalValues(parentClassName, className)) {
									classTextContent.addStartSubItem(eventLink, "<b>" + propertyName + "</b></a> " + propertyMethods);
									
									classTextContent.addContent(eventLink + "_smallDescription");
									
									classTextContent.addHeader("Syntax");
									String getterMethod = propertyGetterMethod.get(propertyName);
									if (getterMethod != null) {
										classTextContent.addJavaCodeFunc(getterMethod);	
									}
									String setterMethod = propertySetterMethod.get(propertyName);
									if (setterMethod != null) {
										classTextContent.addJavaCodeFunc(setterMethod);	
									}

									classTextContent.addHeader("Description");
									classTextContent.addContent(eventLink + "_largeDescription");
									
									classTextContent.addEndSubItem();
									
									/* Index */
									String eventIndexEntry = propertyName + " (" + propertyMethods + ")";
									String eventIndexLink = destHRef + "_index_dispatch.html?page=" + className + ".html#" + eventLink;
									  
									addIndexEntry(classIndexElement, eventIndexEntry, eventIndexLink);
									
									addIndexSubEntry(indexElement, eventIndexEntry, className, eventIndexLink);

									if (propertyMethods != null) {
										String getterMethodShort = null;
										String setterMethodShort = null;
										if (propertyMethods.indexOf("/") > -1) {
											getterMethodShort = IusCLStrUtils.subStringBetween(propertyMethods, null, " / ");
											setterMethodShort = IusCLStrUtils.subStringBetween(propertyMethods, " / ", null);
										}
										else {
											if (getterMethod != null) {
												getterMethodShort = propertyMethods;
											}
											if (setterMethod != null) {
												setterMethodShort = propertyMethods;
											}
										}
										
										if (getterMethodShort != null) {
											addIndexSubEntry(indexElement, getterMethodShort, className, eventIndexLink);
										}
										if (setterMethodShort != null) {
											addIndexSubEntry(indexElement, setterMethodShort, className, eventIndexLink);
										}
									}

								}
								else {
									String parentEventLink = parentClassName +".html#" + propertyName + "_event";
									classTextContent.addSubItem("<a id=\"" + propertyName + "_event" + "\" href=\"" + parentEventLink + "\"><b>" + 
											propertyName + "</b></a> " + propertyMethods);
								}

							}
							pos = constructorText.indexOf("defineProperty(", pos) + 1;
						}
					}
				}					
			}
			
			if (putEndParent == true) {
				classTextContent.addEndList();
				putEndParent = false;
			}
			
			if (putCategoryHeader == true) {
				classTextContent.addEmptyContentLine();
			}

			/* Examples */
			
			
			
			/* See also */
			//classTextContent.addCategoryHeader("seealso", "See also");
			
			classTextContent.addUserDefined();
			
			saveToFile(classTextContent.getContent(), chapterFolderName + "\\" + className + ".html");
			
			packageTextContent.addSubItem("<a href=\"" + className + ".html\">" + className + "</a>");
			
			System.out.println("  " + className);
			
		}
		
		/* Save TOC */
		Format jdomSerializeFormat = Format.getRawFormat();
		jdomSerializeFormat.setIndent("  ");
		jdomSerializeFormat.setLineSeparator("\n");
		XMLOutputter jdomSerializer = new XMLOutputter(jdomSerializeFormat);
		
		IusCLStrings tocStrings = new IusCLStrings();
		tocStrings.setText(jdomSerializer.outputString(jdomTocDocument));
		tocStrings.saveToFile(tocsDestFolder + "toc_reference.xml");
		
		/* Save Index */
		IusCLStrings indexStrings = new IusCLStrings();
		indexStrings.setText(jdomSerializer.outputString(jdomIndexDocument));
		indexStrings.saveToFile(indexesDestFolder + "index_reference.xml");

	}

	/* **************************************************************************************************** */
	private Element addIndexEntry(Element parentElement, String keyword, String href) {

		Element entryElement = new Element("entry");
		entryElement.setAttribute("keyword", keyword);

		if (href != null) {
			Element topicElement = new Element("topic");
			//classTocElement.setAttribute("label", className);
			topicElement.setAttribute("href", href);
			entryElement.addContent(topicElement);
		}

		parentElement.addContent(entryElement);
		
		return entryElement;
	}

	/* **************************************************************************************************** */
	private void addIndexSubEntry(Element indexElement, String indexEntry, String indexSubEntry, String href) {
		
		Element indexEntryElement = indexElement.getChild(indexEntry);
		if (indexEntryElement == null) {
			indexEntryElement = addIndexEntry(indexElement, indexEntry, null);
		}
		
		addIndexEntry(indexEntryElement, "In " + indexSubEntry, href);
	}

	/* **************************************************************************************************** */
	private String findPropertyMethods(JavaClass javaClass, String propertyName, ArrayList<String> propertyMethodsList, 
			HashMap<String, String> propertyGetterMethod, HashMap<String, String> propertySetterMethod) {
		
		String prefix = "";
		
		String methods[] = propertyName.split("\\.");
		
		for (int index = 0; index < methods.length - 1; index++) {
			
			String prefixGetter = "get" + methods[index];
			
			javaClass = javaClass.getMethodBySignature(prefixGetter, null, true).getReturnType().getJavaClass();
			
			if (prefix == null) {
				prefix = prefixGetter;
			}
			else {
				prefix = prefix + prefixGetter;
			}
			
			prefix = prefix + "().";
		}
		
		String lastName = methods[methods.length - 1];
		
		String getMethod = null;
		String setMethod = null;
		
		for (JavaMethod javaMethod: javaClass.getMethods(true)) {
			
			if (IusCLStrUtils.equalValues(javaMethod.getName(), "get" + lastName)) {
				if (!propertyGetterMethod.containsKey(propertyName)) {
					propertyGetterMethod.put(propertyName, javaMethod.getDeclarationSignature(false));
				}
				getMethod = prefix + javaMethod.getCallSignature();
				propertyMethodsList.add("get" + lastName);
			}
			if (IusCLStrUtils.equalValues(javaMethod.getName(), "set" + lastName)) {
				if (!propertySetterMethod.containsKey(propertyName)) {
					propertySetterMethod.put(propertyName, javaMethod.getDeclarationSignature(false));
				}
				setMethod = prefix + javaMethod.getCallSignature();
				propertyMethodsList.add("set" + lastName);
			}

		}
		
		String propertyMethods = null;
		
		if ((getMethod != null) && (setMethod != null)) {
			propertyMethods = getMethod  + " / " + setMethod;
		}
		else {
			if (getMethod != null) {
				propertyMethods = getMethod; 
			}
			if (setMethod != null) {
				propertyMethods = setMethod; 
			}
		}
		
		return propertyMethods;
	}

}
