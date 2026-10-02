/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import org.iuscl.classes.IusCLPersistent;

import lombok.Getter;

public class IusCLGraphicsObject extends IusCLPersistent {

	@Getter
	protected IusCLCanvas canvas = null;
}
