/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.packages;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class IusCLDesignEventInfo {

	@Setter
	String eventName = null;
	@Setter
	String codeTemplateContent = null;
	@Setter
	String codeTemplateName = null;

	final List<String> imports = new ArrayList<>();
}
