/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.pdf.packages;

import org.iuscl.designintf.packages.IusCLDesignPackage;
import org.iuscl.pdf.IusCLPdfDocument;
import org.iuscl.pdf.IusCLPdfImage;
import org.iuscl.pdf.IusCLPdfRichText;
import org.iuscl.pdf.IusCLPdfText;

public class IusCLDesignPackagePdf extends IusCLDesignPackage {

	protected static String pnPdf = "Pdf";

	public IusCLDesignPackagePdf() {
		super();

		String res = "resources/designintf/pdf/images/";

		/* Components */
		defineDesignComponentInfo(IusCLPdfDocument.class.getCanonicalName(), "IusCLPdfDocument", pnPdf,
				res + "IusCLDesignPdfDocument.png", "Doc");

		defineDesignComponentInfo(IusCLPdfText.class.getCanonicalName(), "IusCLPdfText", pnPdf,
				res + "IusCLDesignPdfText.png", "Text");

		defineDesignComponentInfo(IusCLPdfImage.class.getCanonicalName(), "IusCLPdfImage", pnPdf,
				res + "IusCLDesignPdfImage.png", "Image");

		defineDesignComponentInfo(IusCLPdfRichText.class.getCanonicalName(), "IusCLPdfRichText", pnPdf,
				res + "IusCLDesignPdfRichText.png", "Rich");

		// defineDesignComponentInfo(IusCLPrintDialog.class.getCanonicalName(),
		// "IusCLPrintDialog", pnDialogs, res + "IusCLDesignPrintDialog.png", "Print");
		//
		// defineDesignComponentInfo(IusCLPrinterSetupDialog.class.getCanonicalName(),
		// "IusCLPrinterSetupDialog", pnDialogs, res +
		// "IusCLDesignPrinterSetupDialog.png", "Printer");
		//
		// defineDesignComponentInfo(IusCLPageSetupDialog.class.getCanonicalName(),
		// "IusCLPageSetupDialog", pnDialogs, res + "IusCLDesignPageSetupDialog.png",
		// "Page");
	}
}