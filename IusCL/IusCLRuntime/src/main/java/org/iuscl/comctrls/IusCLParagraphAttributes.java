/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import java.util.ArrayList;
import java.util.List;

import org.iuscl.classes.IusCLPersistent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLParagraphAttributes extends IusCLPersistent {

	public enum IusCLParagraphAlignment {
		paLeftJustify, paRightJustify, paCenter
	}

	public enum IusCLNumberingStyle {
		nsNone, nsBullet
	}

	@Getter
	IusCLParagraphAlignment alignment = IusCLParagraphAlignment.paLeftJustify;

	@Getter
	Boolean justified = false;

	@Getter
	Integer firstIndent = 0;

	@Getter
	Integer leftIndent = 0;
	@Getter
	Integer rightIndent = 0;

	@Getter
	final List<Integer> tab = new ArrayList<>();
	@Getter
	Integer tabCount = 0;

	@Getter
	IusCLNumberingStyle numbering = IusCLNumberingStyle.nsNone;

	public IusCLParagraphAttributes() {
		super();

	}

	public void emptyAttributes() {
		alignment = IusCLParagraphAlignment.paLeftJustify;

		justified = false;

		firstIndent = 0;

		leftIndent = 0;
		rightIndent = 0;

		tab.clear();
		tabCount = 0;

		numbering = IusCLNumberingStyle.nsNone;
	}

	public void loadFromParagraphAttributes(IusCLParagraphAttributes paragraphAttributes) {
		alignment = paragraphAttributes.getAlignment();

		justified = paragraphAttributes.getJustified();

		firstIndent = paragraphAttributes.getFirstIndent();

		leftIndent = paragraphAttributes.getLeftIndent();
		rightIndent = paragraphAttributes.rightIndent;

		tab.clear();
		tab.addAll(paragraphAttributes.getTab());

		tabCount = paragraphAttributes.getTabCount();

		numbering = paragraphAttributes.numbering;
	}

	public void setAlignment(IusCLParagraphAlignment alignment) {
		this.alignment = alignment;

		invokeNotify();
	}

	public void setJustified(Boolean justified) {
		this.justified = justified;

		invokeNotify();
	}

	public void setFirstIndent(Integer firstIndent) {
		this.firstIndent = firstIndent;

		invokeNotify();
	}

	public void setLeftIndent(Integer leftIndent) {
		this.leftIndent = leftIndent;

		invokeNotify();
	}

	public void setRightIndent(Integer rightIndent) {
		this.rightIndent = rightIndent;

		invokeNotify();
	}

	public void setTabCount(Integer tabCount) {
		this.tabCount = tabCount;

		invokeNotify();
	}

	public void setNumbering(IusCLNumberingStyle numbering) {
		this.numbering = numbering;

		invokeNotify();
	}
}
