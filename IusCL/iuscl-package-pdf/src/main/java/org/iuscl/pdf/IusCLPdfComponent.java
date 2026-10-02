/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.pdf;

import java.text.DecimalFormat;

import org.iuscl.classes.IusCLComponent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class IusCLPdfComponent extends IusCLComponent {

	public enum IusCLPdfSizeUnit {
		suCentimeters, suInches, suPoints
	}

	/* Properties */
	IusCLPdfSizeUnit sizeUnit = IusCLPdfSizeUnit.suCentimeters;

	public IusCLPdfComponent(IusCLComponent aOwner) {
		super(aOwner);

		/* Properties */
		defineProperty("SizeUnit", IusCLPropertyType.ptEnum, "suCentimeters", IusCLPdfSizeUnit.suCentimeters);
	}

	public static Float getPoints(String size) {
		/*
		 * Units. iText uses point as the typographic unit of measure. One inch is
		 * equivalent to 72 points. One cm is equivalent to 28.35 points.
		 */
		if (size.contains("cm")) {
			size = size.replace("cm", "").replace(" ", "").replace(",", ".");
			return Float.valueOf(size) * 28.35f;
		}
		if (size.contains("in")) {
			size = size.replace("inch", "").replace("in", "").replace(" ", "").replace(",", ".");
			return Float.valueOf(size) * 72f;
		}
		return Float.valueOf(size.replace(",", "."));
	}

	protected String getSize(Float points) {
		return getSize(points, sizeUnit);
	}

	public static String getSize(Float points, IusCLPdfSizeUnit sizeUnit) {
		switch (sizeUnit) {
			case suCentimeters :
				Float cm = points / 28.35f;
				return new DecimalFormat("0.00").format(cm) + " cm";
			case suInches :
				Float in = points / 72f;
				return new DecimalFormat("0.00").format(in) + " in";
			case suPoints :
			default :
				return new DecimalFormat("0.00").format(points);
		}
	}

	protected String transformUnit(String size) {
		return transformUnit(size, sizeUnit);
	}

	public static String transformUnit(String size, IusCLPdfSizeUnit sizeUnit) {
		return getSize(getPoints(size), sizeUnit);
	}
}