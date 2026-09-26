package org.iuscl.devtools;

import java.io.File;
import java.io.IOException;

import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLFileSearchRec;
import org.iuscl.sysutils.IusCLFileUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaSource;


public class RoasterToolEnums {

	public static void main(String[] args) {
		
		RoasterToolEnums roasterTool = new RoasterToolEnums();
		roasterTool.run();
	}

	private void run() {
		
		IusCLFileSearchRec fileSearchRec = new IusCLFileSearchRec();
		fileSearchRec.setIsRecursive(true);
		
		IusCLStrings fileNames = IusCLFileUtils.findFiles("C:\\Endava\\EndevLocal\\IusCL\\IusCL\\IusCLRuntime\\src\\main\\java", fileSearchRec);
		for (int index = 0; index < fileNames.size(); index++) {
			//System.out.println(fileNames.get(index));
			File file = new File(fileNames.get(index));
			try {
				JavaType<?> javaType = Roaster.parse(file);
				JavaClassSource javaClassSource = (JavaClassSource) javaType;
				
//				if (!javaClassSource.getName().equals("IusCLFileSearchRec")) {
//					continue;
//				}
//				if (!javaClassSource.getName().equals("IusCLTimer")) {
//					continue;
//				}
//				if (!javaClassSource.getName().equals("IusCLSwtWindowsStatusBar")) {
//					continue;
//				}
//				if (!javaClassSource.getName().equals("IusCLCollectionItem")) {
//					continue;
//				}

//				if (!javaClassSource.getName().equals("IusCLControl")) {
//					continue;
//				}

				if (!javaClassSource.getName().equals("IusCLBevel")) {
				continue;
			}

//				if (!javaClassSource.getPackage().startsWith("org.iuscl.graphics")) {
//					continue;
//				}
				
//				if (!javaClassSource.getPackage().equals("org.iuscl.windows")) {
//					continue;
//				}
				//System.out.println(javaClass.getClass().toString());
				
				if (javaClassSource.getNestedTypes().size() > 0) {
					System.out.println("");
					System.out.println(javaClassSource.getCanonicalName());
				
					for (JavaSource<?> nestedType : javaClassSource.getNestedTypes()) {
						if (nestedType.isEnum()) {
							
							JavaEnumSource javaEnumSource = (JavaEnumSource) nestedType;
							System.out.println(javaEnumSource.getCanonicalName());
							for (EnumConstantSource enumConstantSource : javaEnumSource.getEnumConstants()) {
								
								
								String name = enumConstantSource.getName();
								String newName = "";
								System.out.print(name + " --> ");
								
								boolean putIt = false;
								for (int indexCh = 0; indexCh < name.length(); indexCh++) {
									char ch = name.charAt(indexCh);
									if (Character.isUpperCase(ch)) {
										putIt = true;
									}
									if (putIt) {
										newName = newName + ch;
									}
								}
								System.out.println(newName);
								
								//enumConstantSource.setName(newName);
								
								enumConstantSource.getJavaDoc().setText(javaClassSource.getName() + " " + javaEnumSource.getName() + " " + newName);
								
								//enumConstantSource.getBody().
							}
						}
					}
				}
				
				
				
				
				
//				/* Has fields */
//				if (javaClassSource.getFields().size() > 0) {
//
//					System.out.println("===================================================");
//					System.out.println(javaClassSource.getCanonicalName());
//					System.out.println("===================================================");
//					
//					/* Fields */
//					boolean hasPrivateFields = false;
//					for (FieldSource<JavaClassSource> fieldSource : javaClassSource.getFields()) {
//						String fieldName = fieldSource.getName();
//						System.out.println(fieldName + " > " + fieldSource.getVisibility() + " > " + fieldSource.getType());
//						for (Annotation<?> annotation : fieldSource.getAnnotations()) {
//							System.out.println("\t" + annotation.getName() + " > " + annotation.getValues());
//						}
//						
//						/* GETTER */
//						String getMethodSignature = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//						if (javaClassSource.hasMethodSignature(getMethodSignature)) {
//							//System.out.println("\t\t has " + getMethodSignature);
//							Method<JavaClassSource, ?> getterMethod = javaClassSource.getMethod(getMethodSignature);
//							//System.out.println("\t\t has " + getterMethod.getBody());
//							if (getterMethod.getBody().lines().count() == 1) {
//								System.out.println("\t\thas default getter " + getMethodSignature);
//								
//								javaClassSource.removeMethod(getterMethod);
//								fieldSource.addAnnotation(Getter.class);
//								
//							} else {
//								System.out.println("\t\t!!!!!!!!! has MODIFIED getter " + getMethodSignature);
//							};
//						};
//						
//						/* SETTER */
//						String setMethodSignature = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//						if (javaClassSource.hasMethodSignature(setMethodSignature, fieldSource.getType().toString())) {
//							//System.out.println("\t\t has " + setMethodSignature);
//							Method<JavaClassSource, ?> setterMethod = javaClassSource.getMethod(setMethodSignature, fieldSource.getType().toString());
//							//System.out.println("\t\t has " + setterMethod.getBody());
//							if (setterMethod.getBody().lines().count() == 1) {
//								System.out.println("\t\thas default setter " + setMethodSignature);
//								
//								javaClassSource.removeMethod(setterMethod);
//								fieldSource.addAnnotation(Setter.class);
//								
//							} else {
//								System.out.println("\t\t!!!!!!!!! has MODIFIED setter " + setMethodSignature);
//							};
//						};
//						
//						if (fieldSource.getVisibility() == Visibility.PRIVATE) {
//							hasPrivateFields = true;
//							fieldSource.setVisibility(Visibility.PACKAGE_PRIVATE);
//						}
//						
//						
//					} /* Fields */
//
//					if (hasPrivateFields) {
//						javaClassSource.addAnnotation(FieldDefaults.class).setEnumValue("level", AccessLevel.PRIVATE);	
//					}
//					
//					System.out.println("");
//					System.out.println("------------------------------------------------------------------------------");
//					System.out.println("Will save into: " + "C:\\Endava\\EndevLocal\\IusCL\\IusCL\\IusCLRuntime\\src\\main\\java\\" + javaClassSource.getQualifiedName().replace(".", "\\") + ".java");
//					System.out.println("------------------------------------------------------------------------------");
//					System.out.println("");
//					
					System.out.println(javaClassSource.toString());
//					
//					String saveInto = "C:\\Endava\\EndevLocal\\IusCL\\IusCL\\IusCLRuntime\\src\\main\\java\\" + javaClassSource.getQualifiedName().replace(".", "\\") + ".java";
//					//IusCLStrUtils.saveStringToFile(javaClassSource.toString(), saveInto);
//					System.out.println("");
//					System.out.println("Saved into: " + saveInto);
//					System.out.println("");
//					
//					
//				}/* Has fields */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
