/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.sysutils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLProgressMonitor {

	public enum IusCLProgressCancelAction {
		pcaNone, pcaCancelOnly, pcaCancelAndRollback, pcaCommitAndCancel
	}

	Integer begin = 0;
	Integer end = 100;
	Integer position = 0;

	String shortMessage = null;
	String longMessage = null;

	IusCLProgressCancelAction progressCancelAction = IusCLProgressCancelAction.pcaNone;

	public IusCLProgressMonitor() {
		super();

		this.begin = 0;
		this.end = 100;
	}

	public IusCLProgressMonitor(Integer begin, Integer end) {
		super();

		this.begin = begin;
		this.end = end;
	}

	public Integer getPositionAsPercent() {
		return ((position - begin) * 100) / (end - begin);
	}

	public void setPositionToPercent(Integer positionPercent) {
		setPosition(((end - begin) * positionPercent) / 100);
	}
}
