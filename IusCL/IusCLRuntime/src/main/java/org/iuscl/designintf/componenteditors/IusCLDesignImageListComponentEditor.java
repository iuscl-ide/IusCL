/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.designintf.componenteditors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLPersistent.IusCLPropertyType;
import org.iuscl.classes.IusCLProperty;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.comctrls.IusCLImageList;
import org.iuscl.forms.IusCLApplication;
import org.iuscl.forms.IusCLForm;
import org.iuscl.graphics.IusCLPicture;
import org.iuscl.obj.IusCLParam;
import org.iuscl.sysutils.IusCLFileUtils;
import org.iuscl.sysutils.IusCLStrUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLDesignImageListComponentEditor extends IusCLDesignComponentEditor {

	private static final String IMAGES = "Images";
	private static final String IMAGES_XML = "_Images.xml";

	IusCLPicture picture = null;
	String pictureFileName = null;
	IusCLForm form = null;
	boolean isInheritor = false;

	String propertyName = IMAGES;
	String initialResFolderWithForm = null;

	IusCLImageList imageList = null;
	final List<byte[]> initialImagesInBuffers = new ArrayList<>();
	final List<String> modifiedImageFileNames = new ArrayList<>();

	Table imagesTable = null;
	Shell pictureShell = null;

	@Override
	public void setComponent(IusCLComponent component) {
		super.setComponent(component);

		imageList = (IusCLImageList) component;

		form = component.findForm();

		String inheritedValue = imageList.getProperties().get(propertyName).getDefaultValue();

		if (IusCLStrUtils.isNotNullNotEmpty(inheritedValue)) {
			isInheritor = true;
		}

		String resFormAndFileName = imageList.getPropertyValue(propertyName);

		if (IusCLStrUtils.isNotNullNotEmpty(resFormAndFileName)) {
			initialResFolderWithForm = IusCLFileUtils.includeTrailingPathDelimiter(
					IusCLApplication.getFormsResFolder(form.getClass()) + resFormAndFileName.substring(0, resFormAndFileName.indexOf("/")));
		}
	}

	@Override
	public IusCLDesignComponentEditorVerb executeVerb(int index) {
		editImages();

		return getVerbSerializeAndBroadcastChange();
	}

	@Override
	public String getVerb(int index) {
		return "ImageList Editor...";
	}

	@Override
	public int getVerbCount() {
		return 1;
	}

	public void editImages() {
		reloadImageList();

		/* Picture shell */
		pictureShell = new Shell(SWT.CLOSE | SWT.BORDER | SWT.RESIZE | SWT.APPLICATION_MODAL);
		pictureShell.setText("ImageList Editor");

		InputStream inputStream = IusCLDesignImageListComponentEditor.class.getResourceAsStream("/resources/images/IusCLPerspective.gif");
		Image swtImage = new Image(Display.getCurrent(), inputStream);
		pictureShell.setImage(swtImage);

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

		/* leftComposite */
		Composite leftComposite = new Composite(pictureShell, SWT.NONE);
		// leftComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		GridData gridDataLeftComposite = new GridData();
		gridDataLeftComposite.horizontalAlignment = GridData.FILL;
		gridDataLeftComposite.verticalAlignment = GridData.FILL;
		gridDataLeftComposite.grabExcessHorizontalSpace = true;
		gridDataLeftComposite.grabExcessVerticalSpace = true;
		leftComposite.setLayoutData(gridDataLeftComposite);

		GridLayout leftCompositeGridLayout = new GridLayout();
		leftCompositeGridLayout.numColumns = 2;
		leftCompositeGridLayout.marginTop = 0;
		leftCompositeGridLayout.marginBottom = 0;
		leftCompositeGridLayout.marginLeft = 0;
		leftCompositeGridLayout.marginRight = 0;
		leftCompositeGridLayout.verticalSpacing = 0;
		leftCompositeGridLayout.horizontalSpacing = 8;
		leftCompositeGridLayout.marginWidth = 0;
		leftCompositeGridLayout.marginHeight = 0;

		leftComposite.setLayout(leftCompositeGridLayout);

		/* bottomLeftComposite */
		Composite bottomLeftComposite = new Composite(leftComposite, SWT.BORDER);
		// bottomLeftComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		GridData gridDataBottomLeftComposite = new GridData();
		gridDataBottomLeftComposite.widthHint = 124;
		gridDataBottomLeftComposite.verticalAlignment = GridData.FILL;
		gridDataBottomLeftComposite.grabExcessVerticalSpace = true;
		bottomLeftComposite.setLayoutData(gridDataBottomLeftComposite);

		GridLayout bottomLeftCompositeGridLayout = new GridLayout();
		bottomLeftCompositeGridLayout.numColumns = 1;
		bottomLeftCompositeGridLayout.marginTop = 8;
		bottomLeftCompositeGridLayout.marginBottom = 8;
		bottomLeftCompositeGridLayout.marginLeft = 8;
		bottomLeftCompositeGridLayout.marginRight = 8;
		bottomLeftCompositeGridLayout.verticalSpacing = 8;
		bottomLeftCompositeGridLayout.horizontalSpacing = 0;
		bottomLeftCompositeGridLayout.marginWidth = 0;
		bottomLeftCompositeGridLayout.marginHeight = 0;

		bottomLeftComposite.setLayout(bottomLeftCompositeGridLayout);

		/* topLeftComposite */
		Composite topLeftComposite = new Composite(leftComposite, SWT.BORDER);
		// topLeftComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		GridData gridDataTopLeftComposite = new GridData();
		gridDataTopLeftComposite.horizontalAlignment = GridData.FILL;
		gridDataTopLeftComposite.verticalAlignment = GridData.FILL;
		gridDataTopLeftComposite.grabExcessHorizontalSpace = true;
		gridDataTopLeftComposite.grabExcessVerticalSpace = true;
		topLeftComposite.setLayoutData(gridDataTopLeftComposite);

		GridLayout topLeftCompositeGridLayout = new GridLayout();
		topLeftCompositeGridLayout.numColumns = 1;
		topLeftCompositeGridLayout.marginTop = 8;
		topLeftCompositeGridLayout.marginBottom = 8;
		topLeftCompositeGridLayout.marginLeft = 8;
		topLeftCompositeGridLayout.marginRight = 8;
		topLeftCompositeGridLayout.verticalSpacing = 8;
		topLeftCompositeGridLayout.horizontalSpacing = 0;
		topLeftCompositeGridLayout.marginWidth = 0;
		topLeftCompositeGridLayout.marginHeight = 0;

		topLeftComposite.setLayout(topLeftCompositeGridLayout);

		/* Info */
		final Label infoLabel = new Label(topLeftComposite, SWT.NONE);
		GridData gridDataInfoLabel = new GridData();
		gridDataInfoLabel.horizontalAlignment = GridData.FILL;
		gridDataInfoLabel.grabExcessHorizontalSpace = true;
		infoLabel.setLayoutData(gridDataInfoLabel);

		/* Picture */
		final Composite pictureComposite = new Composite(topLeftComposite, SWT.BORDER);
		pictureComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridData gridDataPictureComposite = new GridData();
		gridDataPictureComposite.horizontalAlignment = GridData.FILL;
		gridDataPictureComposite.verticalAlignment = GridData.FILL;
		gridDataPictureComposite.grabExcessHorizontalSpace = true;
		gridDataPictureComposite.grabExcessVerticalSpace = true;
		pictureComposite.setLayoutData(gridDataPictureComposite);

		pictureComposite.addPaintListener(swtPaintEvent -> {
			infoLabel.setText(imageInfo());

			GC gc = new GC(pictureComposite);

			if (picture != null) {
				Image swtGraphicImage = picture.getGraphic().getSwtImage();
				int pictureWidth = swtGraphicImage.getBounds().width;
				int pictureHeight = swtGraphicImage.getBounds().height;

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

					gc.drawImage(swtGraphicImage, 0, 0, pictureWidth, pictureHeight, newLeft, newTop, newWidth, newHeight);
				} else {
					gc.drawImage(swtGraphicImage, (canvasWidth - pictureWidth) / 2, (canvasHeight - pictureHeight) / 2);
				}
			} else {
				gc.drawText("(none)", 10, 10);
			}
		});

		/* ListView */
		imagesTable = new Table(bottomLeftComposite, SWT.BORDER);
		GridData gridDataImagesTable = new GridData();
		gridDataImagesTable.horizontalAlignment = GridData.FILL;
		gridDataImagesTable.verticalAlignment = GridData.FILL;
		gridDataImagesTable.grabExcessHorizontalSpace = true;
		gridDataImagesTable.grabExcessVerticalSpace = true;
		imagesTable.setLayoutData(gridDataImagesTable);

		imagesTable.setHeaderVisible(false);

		imagesTable.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			selectImage();
			pictureComposite.redraw();
		}));

		/* Down */
		Composite downComposite = new Composite(bottomLeftComposite, SWT.NONE);

		GridData gridDataDownComposite = new GridData();
		gridDataDownComposite.horizontalAlignment = GridData.FILL;
		gridDataDownComposite.grabExcessHorizontalSpace = true;
		downComposite.setLayoutData(gridDataDownComposite);

		FillLayout downCompositeFillLayout = new FillLayout();
		downCompositeFillLayout.type = SWT.VERTICAL;

		downCompositeFillLayout.spacing = 8;
		downCompositeFillLayout.marginHeight = 0;
		downCompositeFillLayout.marginWidth = 0;

		downComposite.setLayout(downCompositeFillLayout);

		Composite midComposite = new Composite(downComposite, SWT.NONE);

		FillLayout midCompositeFillLayout = new FillLayout();
		midCompositeFillLayout.type = SWT.HORIZONTAL;

		midCompositeFillLayout.spacing = 8;
		midCompositeFillLayout.marginHeight = 0;
		midCompositeFillLayout.marginWidth = 0;

		midComposite.setLayout(midCompositeFillLayout);

		Button upButton = new Button(midComposite, SWT.PUSH);
		upButton.setText("Up");

		upButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			int itemIndex = imagesTable.getSelectionIndex();

			if (itemIndex == 0) {
				return;
			}

			TableItem imageTableItem = new TableItem(imagesTable, SWT.NONE, itemIndex - 1);
			imageTableItem.setImage(imagesTable.getItem(itemIndex + 1).getImage());
			modifiedImageFileNames.add(itemIndex - 1, modifiedImageFileNames.get(itemIndex));

			imagesTable.remove(itemIndex + 1);
			modifiedImageFileNames.remove(itemIndex + 1);

			renumberImages();

			imagesTable.select(itemIndex - 1);
		}));

		Button downButton = new Button(midComposite, SWT.PUSH);
		downButton.setText("Down");

		downButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			int itemIndex = imagesTable.getSelectionIndex();

			if (itemIndex == imagesTable.getItemCount() - 1) {
				return;
			}

			TableItem imageTableItem = new TableItem(imagesTable, SWT.NONE, itemIndex + 2);
			imageTableItem.setImage(imagesTable.getItem(itemIndex).getImage());
			modifiedImageFileNames.add(itemIndex + 2, modifiedImageFileNames.get(itemIndex));

			imagesTable.remove(itemIndex);
			modifiedImageFileNames.remove(itemIndex);

			renumberImages();

			imagesTable.select(itemIndex + 1);
		}));

		Button addButton = new Button(downComposite, SWT.PUSH);
		addButton.setText("Add...");

		addButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			FileDialog swtPictureDialog = openPictureDialog();
			String resultString = swtPictureDialog.open();
			if (resultString != null) {
				String filePath = IusCLFileUtils.includeTrailingPathDelimiter(swtPictureDialog.getFilterPath());
				pictureFileName = filePath + swtPictureDialog.getFileName();

				if (picture == null) {
					picture = new IusCLPicture();
				}
				picture.loadFromFile(pictureFileName);

				TableItem imageTableItem = new TableItem(imagesTable, SWT.NONE);
				imageTableItem.setImage(createTableImage(picture));
				imagesTable.select(imagesTable.getItemCount() - 1);

				modifiedImageFileNames.add(pictureFileName);

				renumberImages();

				pictureComposite.redraw();
			}
		}));

		Button replaceButton = new Button(downComposite, SWT.PUSH);
		replaceButton.setText("Replace...");

		replaceButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			FileDialog swtPictureDialog = openPictureDialog();
			String resultString = swtPictureDialog.open();
			if (resultString != null) {
				String filePath = IusCLFileUtils.includeTrailingPathDelimiter(swtPictureDialog.getFilterPath());
				pictureFileName = filePath + swtPictureDialog.getFileName();

				if (picture == null) {
					picture = new IusCLPicture();
				}
				picture.loadFromFile(pictureFileName);

				int imageIndex = imagesTable.getSelectionIndex();
				imagesTable.remove(imageIndex);

				TableItem imageTableItem = new TableItem(imagesTable, SWT.NONE, imageIndex);
				imageTableItem.setText(Integer.toString(imageIndex));
				imageTableItem.setImage(createTableImage(picture));
				imagesTable.select(imageIndex);

				modifiedImageFileNames.remove(imageIndex);
				modifiedImageFileNames.add(imageIndex, pictureFileName);

				pictureComposite.redraw();
			}
		}));

		Button deleteButton = new Button(downComposite, SWT.PUSH);
		deleteButton.setText("Delete");

		deleteButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			int itemIndex = imagesTable.getSelectionIndex();

			imagesTable.remove(itemIndex);
			modifiedImageFileNames.remove(itemIndex);

			if (imagesTable.getItemCount() > 0) {
				if (itemIndex == imagesTable.getItemCount()) {
					imagesTable.select(itemIndex - 1);
				} else {
					imagesTable.select(itemIndex);
				}

				selectImage();
			} else {
				picture = null;
			}
			pictureComposite.redraw();
		}));

		Button clearButton = new Button(downComposite, SWT.PUSH);
		clearButton.setText("Clear");

		clearButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			imagesTable.removeAll();
			modifiedImageFileNames.clear();
			pictureComposite.redraw();
		}));

		Button exportButton = new Button(downComposite, SWT.PUSH);
		exportButton.setText("Export...");

		/* rightComposite */
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

		okButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> {
			saveImages();
			pictureShell.dispose();
		}));

		Button cancelButton = new Button(rightComposite, SWT.PUSH);
		GridData gridDataCancelButton = new GridData();
		gridDataCancelButton.widthHint = 90;
		cancelButton.setLayoutData(gridDataCancelButton);
		cancelButton.setText("Cancel");

		cancelButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> pictureShell.dispose()));

		Button applyButton = new Button(rightComposite, SWT.PUSH);
		GridData gridDataApplyButton = new GridData();
		gridDataApplyButton.widthHint = 90;
		applyButton.setLayoutData(gridDataApplyButton);
		applyButton.setText("Apply");

		applyButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(swtSelectionEvent -> saveImages()));

		/* Initial table load */
		for (int index = 0; index < imageList.getCount(); index++) {
			TableItem imageTableItem = new TableItem(imagesTable, SWT.NONE);
			imageTableItem.setImage(createTableImage(imageList.getUnsizedImage(index)));
		}
		renumberImages();

		if (imageList.getCount() > 0) {
			imagesTable.select(0);
			selectImage();
		}
		pictureComposite.redraw();

		pictureShell.open();

		while (!pictureShell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}
	}

	private void saveImages() {
		/* Delete the initial images */
		if (!isInheritor) {
			for (int index = 0; index < imageList.getCount(); index++) {
				IusCLFileUtils.deleteFile(findInitialImageFileName(index));
			}
			IusCLFileUtils.deleteFile(initialResFolderWithForm + imageList.getName() + IMAGES_XML);
		}

		isInheritor = false;

		/* No images */
		if (modifiedImageFileNames.isEmpty()) {
			imageList.setPropertyValue(IMAGES, "");

			return;
		}

		/* New */
		String resFolderWithForm = IusCLFileUtils
				.includeTrailingPathDelimiter(IusCLApplication.getFormsResFolder(form.getClass()) + form.getClass().getSimpleName());

		Element jdomImagesElement = new Element("images");

		for (int index = 0; index < modifiedImageFileNames.size(); index++) {
			String modifiedImageFileName = modifiedImageFileNames.get(index);

			String newImageFileNameWithoutExt = resFolderWithForm + imageList.getName() + "_Image" + Integer.toString(index) + ".";

			Element jdomImageElement = new Element("image" + Integer.toString(index));

			Element jdomInitialSimpleFileName = new Element("initialSimpleFileName");
			jdomImageElement.addContent(jdomInitialSimpleFileName);

			int initialIndex = findInitialImageFileNameIndex(modifiedImageFileName);

			if (initialIndex == -1) {
				/* New image */
				IusCLFileUtils.copyFile(modifiedImageFileName, newImageFileNameWithoutExt + IusCLFileUtils.extractFileExt(modifiedImageFileName));

				jdomInitialSimpleFileName.setText(IusCLFileUtils.extractFileName(modifiedImageFileName));
			} else {
				/* Initial image */
				IusCLFileUtils.writeBufferIntoFile(newImageFileNameWithoutExt + IusCLFileUtils.extractFileExt(findInitialImageFileName(initialIndex)),
						initialImagesInBuffers.get(initialIndex));

				jdomInitialSimpleFileName.setText(imageList.getUnsizedImage(initialIndex).getInitialSimpleFileName());
			}

			jdomImagesElement.addContent(jdomImageElement);
		}

		Document jdomDocument = new Document(jdomImagesElement);
		Format jdomSerializeFormat = Format.getRawFormat();
		jdomSerializeFormat.setIndent("  ");
		jdomSerializeFormat.setLineSeparator("\n");
		XMLOutputter jdomSerializer = new XMLOutputter(jdomSerializeFormat);
		String jdomSerialized = jdomSerializer.outputString(jdomDocument);
		IusCLStrings strings = new IusCLStrings();
		strings.setText(jdomSerialized);
		strings.saveToFile(resFolderWithForm + imageList.getName() + IMAGES_XML);

		imageList.setPropertyValue(IMAGES, form.getClass().getSimpleName() + "/" + imageList.getName() + IMAGES_XML);

		initialResFolderWithForm = resFolderWithForm;

		reloadImageList();
	}

	private String imageInfo() {
		if (picture != null && picture.getGraphic() != null) {
			return "(" + picture.getGraphic().getWidth() + " x " + picture.getGraphic().getHeight() + ")";
		}
		return "(none)";
	}

	private Image createTableImage(IusCLPicture image) {
		Image swtImage = image.getGraphic().getSwtImage();
		Image tableImage = new Image(Display.getCurrent(), 32, 32);

		GC gc = new GC(tableImage);
		gc.setBackground(new Color(Display.getCurrent(), new RGB(255, 0, 255)));
		gc.setForeground(new Color(Display.getCurrent(), new RGB(255, 0, 255)));
		gc.fillRectangle(0, 0, 32, 32);

		drawResized(gc, swtImage, 32, 32);
		gc.dispose();

		ImageData tableImageData = tableImage.getImageData();
		int fucsiaPixel = tableImageData.palette.getPixel(new RGB(255, 0, 255));
		tableImageData.transparentPixel = fucsiaPixel;

		return new Image(Display.getCurrent(), tableImageData);
	}

	private void drawResized(GC gc, Image swtImage, int canvasWidth, int canvasHeight) {
		int pictureWidth = swtImage.getBounds().width;
		int pictureHeight = swtImage.getBounds().height;

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

	private String findInitialImageFileName(int index) {
		return initialResFolderWithForm + imageList.getName() + "_Image" + Integer.toString(index) + "."
				+ imageList.getUnsizedImage(index).getPictureExtension();
	}

	private int findInitialImageFileNameIndex(String fileName) {
		for (int index = 0; index < imageList.getCount(); index++) {
			if (IusCLStrUtils.equalValues(fileName, findInitialImageFileName(index))) {
				return index;
			}
		}

		return -1;
	}

	private void renumberImages() {
		for (int index = 0; index < imagesTable.getItemCount(); index++) {
			imagesTable.getItem(index).setText(Integer.toString(index));
		}
	}

	private void reloadImageList() {
		// imageList.clear();
		// imageList.setPropertyValueImagesFromResName(propertyName,
		// imageList.getPropertyValueImagesAsResName(propertyName));

		/* Refresh the form */
		recursiveRefreshComponents(form);

		initialImagesInBuffers.clear();
		modifiedImageFileNames.clear();

		for (int index = 0; index < imageList.getCount(); index++) {
			String imageFileName = findInitialImageFileName(index);
			initialImagesInBuffers.add(IusCLFileUtils.readFileIntoBuffer(imageFileName));
			modifiedImageFileNames.add(imageFileName);
		}
	}

	private void recursiveRefreshComponents(IusCLComponent parentComponent) {
		for (int componentIndex = 0; componentIndex < parentComponent.getComponents().size(); componentIndex++) {
			IusCLComponent component = parentComponent.getComponents().get(componentIndex);

			Iterator<IusCLProperty> propertiesIterator = component.getProperties().values().iterator();
			while (propertiesIterator.hasNext()) {
				IusCLProperty property = propertiesIterator.next();
				if (IusCLStrUtils.equalValues(property.getType(), IusCLPropertyType.ptComponent.name())) {
					String imageListName = component.getPropertyValue(property.getName());
					if (IusCLStrUtils.equalValues(imageListName, imageList.getName())) {
						component.setPropertyValueInvoke(property.getName(), new IusCLParam(property.getRefClass(), this.getComponent()));
					}
				}
			}
		}
	}

	private FileDialog openPictureDialog() {
		FileDialog swtFileDialog = new FileDialog(pictureShell, SWT.OPEN);

		swtFileDialog.setText("Load Picture");
		swtFileDialog.setFileName("");
		swtFileDialog.setFilterPath("C:\\Iustin");

		swtFileDialog.setOverwrite(true);

		swtFileDialog.setFilterIndex(0);

		String imageFilter = "All Pictures (*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf)|" + "*.gif;*.png;*.jpg;*.jpeg;*.bmp;*.ico;*.emf;*.wmf|"
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

		return swtFileDialog;
	}

	private void selectImage() {
		int index = imagesTable.getSelectionIndex();

		String modifiedImageFileName = modifiedImageFileNames.get(index);
		int initialIndex = findInitialImageFileNameIndex(modifiedImageFileName);

		if (picture == null) {
			picture = new IusCLPicture();
		}

		if (initialIndex == -1) {
			/* New image */
			picture.loadFromFile(modifiedImageFileName);
		} else {
			/* Initial image */
			picture.loadFromFile(findInitialImageFileName(initialIndex));
		}
	}
}
