package org.iuscl.devtools;

import java.io.File;
import java.io.IOException;

import org.iuscl.classes.IusCLStrings;
import org.iuscl.sysutils.IusCLFileSearchRec;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import lombok.Getter;
import lombok.Setter;

public class RoasterToolOnlyGetterSetter {

	public static void main(String[] args) {
		RoasterToolOnlyGetterSetter roasterTool = new RoasterToolOnlyGetterSetter();
		roasterTool.run();
	}

	private void run() {
		
		IusCLFileSearchRec fileSearchRec = new IusCLFileSearchRec();
		fileSearchRec.setIsRecursive(true);
		fileSearchRec.setIncludeNamePattern(".java");
		
		IusCLStrings fileNames = IusCLFileUtils.findFiles("C:\\Endava\\EndevLocal\\IusCL\\IusCL\\IusCL\\IusCLPackagePdf\\src\\main\\java", fileSearchRec);
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
				
//				if (!javaClassSource.getPackage().startsWith("org.iuscl.graphics")) {
//					continue;
//				}
				
//				if (!javaClassSource.getPackage().equals("org.iuscl.windows")) {
//					continue;
//				}
				//System.out.println(javaClass.getClass().toString());
				
				
				/* Has fields */
				if (!javaClassSource.getFields().isEmpty()) {

//					System.out.println("===================================================");
//					System.out.println(javaClassSource.getCanonicalName());
//					System.out.println("===================================================");
					/* Fields */
					boolean hasOnlyGettersSettersFields = true;
					for (FieldSource<JavaClassSource> fieldSource : javaClassSource.getFields()) {
//						String fieldName = fieldSource.getName();

						boolean hasGetter = false;
						boolean hasSetter = false;
						for (Annotation<?> annotation : fieldSource.getAnnotations()) {
							if (annotation.getName().equals("Getter")) {
								hasGetter = true;
							}
							if (annotation.getName().equals("Setter")) {
								hasSetter = true;
							}
						}

						if (hasGetter && hasSetter) {
							//System.out.println(fieldName + " > " + fieldSource.getVisibility() + " > " + fieldSource.getType());
						}
						else {
							hasOnlyGettersSettersFields = false;
						}
					} /* Fields */

					if (hasOnlyGettersSettersFields) {
						System.out.println("------------------------------------------------------------------------------");
						System.out.println(javaClassSource.getCanonicalName());
						System.out.println("------------------------------------------------------------------------------");
						
						for (FieldSource<JavaClassSource> fieldSource : javaClassSource.getFields()) {
							fieldSource.removeAllAnnotations();
						}
						
						javaClassSource.addAnnotation(Getter.class);
						javaClassSource.addAnnotation(Setter.class);
						
						System.out.println("------------------------------------------------------------------------------");
						System.out.println(javaClassSource.toString());
						System.out.println("------------------------------------------------------------------------------");
						
						String saveInto = "C:\\Endava\\EndevLocal\\IusCL\\IusCL\\IusCL\\IusCLPackagePdf\\src\\main\\java\\" + javaClassSource.getQualifiedName().replace(".", "\\") + ".java";
						IusCLStrUtils.saveStringToFile(javaClassSource.toString(), saveInto);
						System.out.println("");
						System.out.println("Saved into: " + saveInto);
						System.out.println("");
					}
				}/* Has fields */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
