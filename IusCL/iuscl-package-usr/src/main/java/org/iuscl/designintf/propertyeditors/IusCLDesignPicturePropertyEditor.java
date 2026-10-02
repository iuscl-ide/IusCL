/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.propertyeditors;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDesignPicturePropertyEditor extends IusCLDesignPropertyEditor {

	Composite pictureEditorComposite;
	Button pictureEditorButton;
	Text pictureEditorText;

	String propertyValue;

	IusCLPicture picture = null;
	String pictureFileName = null;

	public IusCLDesignPicturePropertyEditor(final TreeItem swtTreeItem, IusCLPersistent persistent) {
		super(swtTreeItem, persistent);

		propertyValue = swtTreeItem.getText(1);

		/* Editor cell */
		pictureEditorComposite = new Composite(swtTreeItem.getParent(), SWT.NONE);
		GridLayout stringsCompositeGridLayout = new GridLayout();
		stringsCompositeGridLayout.numColumns = 2;
		stringsCompositeGridLayout.marginTop = 0;
		stringsCompositeGridLayout.marginBottom = 0;
		stringsCompositeGridLayout.marginLeft = 0;
		stringsCompositeGridLayout.marginRight = 0;
		stringsCompositeGridLayout.verticalSpacing = 0;
		stringsCompositeGridLayout.horizontalSpacing = 0;
		stringsCompositeGridLayout.marginWidth = 0;
		stringsCompositeGridLayout.marginHeight = 0;

		pictureEditorComposite.setLayout(stringsCompositeGridLayout);
		swtEditorFocusControl = pictureEditorComposite;

		pictureEditorText = new Text(pictureEditorComposite, SWT.READ_ONLY | SWT.BORDER);
		GridData gridDataText = new GridData();
		gridDataText.heightHint = 10;
		gridDataText.horizontalAlignment = GridData.FILL;
		gridDataText.verticalAlignment = GridData.FILL;
		gridDataText.grabExcessHorizontalSpace = true;
		gridDataText.grabExcessVerticalSpace = true;
		pictureEditorText.setLayoutData(gridDataText);
		pictureEditorText.setText("(IusCLPicture)");
		// pictureText.setEnabled(false);

		pictureEditorButton = new Button(pictureEditorComposite, SWT.PUSH);
		pictureEditorButton.setText("...");
		GridData gridDataButton = new GridData();
		gridDataButton.heightHint = 10;
		gridDataButton.horizontalAlignment = GridData.END;
		gridDataButton.verticalAlignment = GridData.FILL;
		pictureEditorButton.setLayoutData(gridDataButton);

		swtEditorFocusControl.setData(IusCLDesignPicturePropertyEditor.this);

		pictureEditorComposite.addKeyListener(swtKeyReleasedListener);
		pictureEditorText.addKeyListener(swtKeyReleasedListener);
		pictureEditorButton.addKeyListener(swtKeyReleasedListener);

		pictureEditorButton.addFocusListener(swtFocusLostListener);
		pictureEditorText.addFocusListener(swtFocusLostListener);

		pictureEditorButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> clickButton()));

		pictureEditorButton.setFocus();
	}

	private void clickButton() {
		/* Picture shell */
		final Shell pictureShell = new Shell(SWT.CLOSE | SWT.BORDER | SWT.RESIZE | SWT.APPLICATION_MODAL);
		pictureShell.setText("Picture Editor");

		InputStream inputStream = IusCLDesignPicturePropertyEditor.class.getResourceAsStream("/resources/images/IusCLPerspective.png");
		Image imageIusCL = new Image(Display.getCurrent(), inputStream);
		pictureShell.setImage(imageIusCL);

		Monitor primary = Display.getDefault().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		pictureShell.setSize(640, 640);
		Rectangle rect = pictureShell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		pictureShell.setLocation(x, y);
		pictureShell.setMinimumSize(600, 600);

		GridLayout pictureShellGridLayout = new GridLayout();
		pictureShellGridLayout.numColumns = 2;
		pictureShellGridLayout.marginTop = 8;
		pictureShellGridLayout.marginBottom = 8;
		pictureShellGridLayout.marginLeft = 8;
		pictureShellGridLayout.marginRight = 8;
		pictureShellGridLayout.verticalSpacing = 0;
		pictureShellGridLayout.horizontalSpacing = 8;
		pictureShellGridLayout.marginWidth = 0;
		pictureShellGridLayout.marginHeight = 0;
		pictureShell.setLayout(pictureShellGridLayout);

		/* Left */
		Composite leftComposite = new Composite(pictureShell, SWT.BORDER);
		// upComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		GridData gridDataLeftComposite = new GridData();
		gridDataLeftComposite.horizontalAlignment = GridData.FILL;
		gridDataLeftComposite.verticalAlignment = GridData.FILL;
		gridDataLeftComposite.grabExcessHorizontalSpace = true;
		gridDataLeftComposite.grabExcessVerticalSpace = true;
		leftComposite.setLayoutData(gridDataLeftComposite);

		GridLayout leftCompositeGridLayout = new GridLayout();
		leftCompositeGridLayout.numColumns = 1;
		leftCompositeGridLayout.marginTop = 8;
		leftCompositeGridLayout.marginBottom = 8;
		leftCompositeGridLayout.marginLeft = 8;
		leftCompositeGridLayout.marginRight = 8;
		leftCompositeGridLayout.verticalSpacing = 8;
		leftCompositeGridLayout.horizontalSpacing = 0;
		leftCompositeGridLayout.marginWidth = 0;
		leftCompositeGridLayout.marginHeight = 0;

		leftComposite.setLayout(leftCompositeGridLayout);

		/* Info */
		final Label infoLabel = new Label(leftComposite, SWT.NONE);
		GridData gridDataInfoLabel = new GridData();
		gridDataInfoLabel.horizontalAlignment = GridData.FILL;
		gridDataInfoLabel.grabExcessHorizontalSpace = true;
		infoLabel.setLayoutData(gridDataInfoLabel);

		/* Picture */
		final Composite pictureComposite = new Composite(leftComposite, SWT.BORDER);
		pictureComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridData gridDataPictureComposite = new GridData();
		gridDataPictureComposite.horizontalAlignment = GridData.FILL;
		gridDataPictureComposite.verticalAlignment = GridData.FILL;
		gridDataPictureComposite.grabExcessHorizontalSpace = true;
		gridDataPictureComposite.grabExcessVerticalSpace = true;
		pictureComposite.setLayoutData(gridDataPictureComposite);

		if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
			picture = new IusCLPicture();
			picture.loadFromFile(IusCLApplication.getFormsResFolder(persistent.getPersistentForm().getClass()) + propertyValue);
		}

		pictureComposite.addPaintListener(swtPaintEvent -> {
			infoLabel.setText(imageInfo());

			GC gc = new GC(pictureComposite);

			if (picture != null) {
				Image swtImage = picture.getGraphic().getSwtImage();

				// swtImage = new Image(Display.getCurrent(), swtImage, SWT.IMAGE_DISABLE);

				int pictureWidth = swtImage.getBounds().width;
				int pictureHeight = swtImage.getBounds().height;

				int canvasWidth = swtPaintEvent.width;
				int canvasHeight = swtPaintEvent.height;

				if ((pictureWidth > canvasWidth) || (pictureHeight > canvasHeight)) {
					double imageProportion = (double) pictureWidth / (double) pictureHeight;
					double canvasProportion = (double) canvasWidth / (double) canvasHeight;

					int newLeft = 0;
					int newTop = 0;
					int newWidth = canvasWidth;
					int newHeight = canvasHeight;

					if (imageProportion > canvasProportion) {
						double reduceProportion = (double) canvasWidth / (double) pictureWidth;
						newHeight = (int) (pictureHeight * reduceProportion);
						newTop = (canvasHeight - newHeight) / 2;
					} else {
						double reduceProportion = (double) canvasHeight / (double) pictureHeight;
						newWidth = (int) (pictureWidth * reduceProportion);
						newLeft = (canvasWidth - newWidth) / 2;
					}

					gc.drawImage(swtImage, 0, 0, pictureWidth, pictureHeight, newLeft, newTop, newWidth, newHeight);
				} else {
					gc.drawImage(swtImage, (canvasWidth - pictureWidth) / 2, (canvasHeight - pictureHeight) / 2);
				}
			}
		});

		/* Down */
		Composite downComposite = new Composite(leftComposite, SWT.NONE);
		GridData gridDataDownComposite = new GridData();
		gridDataDownComposite.minimumWidth = 286;
		gridDataDownComposite.horizontalAlignment = GridData.FILL;
		gridDataDownComposite.grabExcessHorizontalSpace = true;
		downComposite.setLayoutData(gridDataDownComposite);

		GridLayout downCompositeGridLayout = new GridLayout();
		downCompositeGridLayout.numColumns = 4;
		downCompositeGridLayout.marginTop = 0;
		downCompositeGridLayout.marginBottom = 0;
		downCompositeGridLayout.marginLeft = 0;
		downCompositeGridLayout.marginRight = 0;
		downCompositeGridLayout.verticalSpacing = 0;
		downCompositeGridLayout.horizontalSpacing = 8;
		downCompositeGridLayout.marginWidth = 0;
		downCompositeGridLayout.marginHeight = 0;

		downComposite.setLayout(downCompositeGridLayout);

		/* Down left */
		Composite downLeftComposite = new Composite(downComposite, SWT.NONE);
		GridData gridDataDownLeftComposite = new GridData();
		gridDataDownLeftComposite.heightHint = 10;
		gridDataDownLeftComposite.horizontalAlignment = GridData.FILL;
		gridDataDownLeftComposite.grabExcessHorizontalSpace = true;
		downLeftComposite.setLayoutData(gridDataDownLeftComposite);

		Button loadButton = new Button(downComposite, SWT.PUSH);
		GridData gridDataLoadButton = new GridData();
		gridDataLoadButton.widthHint = 90;
		loadButton.setLayoutData(gridDataLoadButton);
		loadButton.setText("Load...");

		loadButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			FileDialog swtFileDialog = new FileDialog(pictureShell, SWT.OPEN);

			swtFileDialog.setText("Load Picture");
			swtFileDialog.setFileName("");
			swtFileDialog.setFilterPath("C:\\Iustin");

			swtFileDialog.setOverwrite(true);

			swtFileDialog.setFilterIndex(0);

			String imageFilter = "All Pictures (*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf)|*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf|"
					+ "Gifs (*.gif)|*.gif|" + "Portable Network Graphics (*.png)|*.png|" + "JPEG Image File (*.jpg)|*.jpg|"
					+ "JPEG Image File (*.jpeg)|*.jpeg|" + "Bitmaps (*.bmp)|*.bmp|" + "Icons (*.ico)|*.ico|" + "Enhanced Metafiles (*.emf)|*.emf|"
					+ "Metafiles (*.wmf)|*.wmf";

			String[] filters = imageFilter.split("\\|");
			String[] filterNames = new String[filters.length / 2];
			String[] filterExtensions = new String[filters.length / 2];

			for (int index = 0; index < filters.length / 2; index++) {
				filterNames[index] = filters[index * 2];
				filterExtensions[index] = filters[index * 2 + 1];
			}
			swtFileDialog.setFilterNames(filterNames);
			swtFileDialog.setFilterExtensions(filterExtensions);

			String resultString = swtFileDialog.open();
			if (resultString != null) {
				String filePath = IusCLFileUtils.includeTrailingPathDelimiter(swtFileDialog.getFilterPath());
				pictureFileName = filePath + swtFileDialog.getFileName();

				if (picture == null) {
					picture = new IusCLPicture();
				}
				picture.loadFromFile(pictureFileName);

				pictureComposite.redraw();
			}
		}));

		Button saveButton = new Button(downComposite, SWT.PUSH);
		GridData gridDataSaveButton = new GridData();
		gridDataSaveButton.widthHint = 90;
		saveButton.setLayoutData(gridDataSaveButton);
		saveButton.setText("Save...");

		Button clearButton = new Button(downComposite, SWT.PUSH);
		GridData gridDataClearButton = new GridData();
		gridDataClearButton.widthHint = 90;
		clearButton.setLayoutData(gridDataClearButton);
		clearButton.setText("Clear");

		clearButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			if (picture != null) {
				picture = null;
			}
			if (IusCLStrUtils.isNotNullNotEmpty(pictureFileName)) {
				pictureFileName = null;
			}

			pictureComposite.redraw();
		}));

		/* Right */
		Composite rightComposite = new Composite(pictureShell, SWT.NONE);
		GridData gridDataRightComposite = new GridData();
		gridDataRightComposite.verticalAlignment = GridData.FILL;
		gridDataRightComposite.grabExcessVerticalSpace = true;
		rightComposite.setLayoutData(gridDataRightComposite);

		GridLayout rightCompositeGridLayout = new GridLayout();
		rightCompositeGridLayout.numColumns = 1;
		rightCompositeGridLayout.marginTop = 0;
		rightCompositeGridLayout.marginBottom = 0;
		rightCompositeGridLayout.marginLeft = 0;
		rightCompositeGridLayout.marginRight = 0;
		rightCompositeGridLayout.verticalSpacing = 8;
		rightCompositeGridLayout.horizontalSpacing = 0;
		rightCompositeGridLayout.marginWidth = 0;
		rightCompositeGridLayout.marginHeight = 0;

		rightComposite.setLayout(rightCompositeGridLayout);

		Button okButton = new Button(rightComposite, SWT.PUSH);
		GridData gridDataOkButton = new GridData();
		gridDataOkButton.widthHint = 90;
		okButton.setLayoutData(gridDataOkButton);
		okButton.setText("OK");

		okButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			if (pictureFileName != null) {
				if (IusCLStrUtils.isNotNullNotEmpty(propertyValue)) {
					IusCLFileUtils.deleteFile(IusCLApplication.getFormsResFolder(persistent.getPersistentForm().getClass()) + propertyValue);
				}

				String picFile = persistent.getPersistentFormAndResName(propertyName) + "." + IusCLFileUtils.extractFileExt(pictureFileName);
				String destFileName = IusCLApplication.getFormsResFolder(persistent.getPersistentForm().getClass())
						+ picFile.replace("/", IusCLFileUtils.getPathDelimiter());
				IusCLFileUtils.copyFile(pictureFileName, destFileName);

				propertyValue = "*" + picFile;
			} else {
				/* Delete resource */
				propertyValue = "";
			}

			pictureEditorButton.addFocusListener(swtFocusLostListener);
			pictureShell.dispose();
		}));

		Button cancelButton = new Button(rightComposite, SWT.PUSH);
		GridData gridDataCancelButton = new GridData();
		gridDataCancelButton.widthHint = 90;
		cancelButton.setLayoutData(gridDataCancelButton);
		cancelButton.setText("Cancel");

		cancelButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(_ -> {
			pictureEditorButton.addFocusListener(swtFocusLostListener);
			pictureShell.dispose();
		}));

		pictureEditorButton.removeFocusListener(swtFocusLostListener);
		pictureShell.open();
	}

	private String imageInfo() {
		if (picture != null) {
			if (picture.getGraphic() != null) {
				return "(" + picture.getGraphic().getWidth() + " x " + picture.getGraphic().getHeight() + ")";
			}
			return "(none)";
		}
		return "(none)";
	}

	@Override
	public String getEditorValue() {
		return propertyValue;
	}

	@Override
	public void closeEditor() {
		pictureEditorText.dispose();
		pictureEditorButton.dispose();
		pictureEditorComposite.dispose();

		super.closeEditor();
	}
}
