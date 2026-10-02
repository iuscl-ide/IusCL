/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;
import org.iuscl.classes.IusCLPersistent;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.sysutils.IusCLErrorUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLGraphic extends IusCLPersistent {

	@Getter
	@Setter
	protected Image swtImage = null;
	@Getter
	@Setter
	protected PaletteData swtPaletteData = null;

	protected ImageLoader swtImageLoader = null;

	@Getter
	@Setter
	boolean modified = true;
	@Getter
	@Setter
	boolean transparent = true;

	IusCLCanvas canvas = null;

	Integer height = 0;
	Integer width = 0;

	public IusCLGraphic() {
		super();
		swtImageLoader = new ImageLoader();
	}

	protected void loadFromSwtImageData(ImageData swtImageData) {
		if (swtImage != null) {
			swtImage.dispose();
		}

		if (swtImageData == null) {
			return;
		}

		swtImage = new Image(Display.getDefault(), swtImageData);
		canvas = null;
		setWidth(swtImage.getBounds().width);
		setHeight(swtImage.getBounds().height);
	}

	public void loadFromPicture(IusCLPicture picture) {
		loadFromGraphic(picture.getGraphic());
	}

	public void loadFromGraphic(IusCLGraphic graphic) {
		loadFromSwtImageData(graphic.getSwtImage().getImageData());

		modified = graphic.getModified();
		transparent = graphic.getTransparent();
	}

	public void loadFromFile(String fileName) {
		try {
			swtImageLoader.load(fileName);
		} catch (IllegalArgumentException illegalArgumentException) {
			String exceptionMessage = MessageFormat.format("Error loading image from file: \"{0}\"", fileName);
			log.error(exceptionMessage, illegalArgumentException);
			IusCLErrorUtils.showErrorDialog(exceptionMessage, illegalArgumentException);
			return;
		}
		loadFromSwtImageData(swtImageLoader.data[0]);
	}

	public void loadFromResource(Class<?> relativeClass, String resourceName) {
		loadFromStream(relativeClass.getClassLoader().getResourceAsStream(resourceName));
	}

	public void loadFromStream(InputStream stream) {
		if (stream == null) {
			return;
		}
		swtImageLoader.load(stream);
		loadFromSwtImageData(swtImageLoader.data[0]);
	}

	public IusCLCanvas getCanvas() {
		if (canvas == null) {
			canvas = new IusCLCanvas(swtImage);
		}
		return canvas;
	}

	public void saveToFile(String fileName) {
		swtImageLoader.data = new ImageData[] { swtImage.getImageData() };
	}

	public void saveToStream(OutputStream stream) {
		swtImageLoader.data = new ImageData[] { swtImage.getImageData() };
	}

	public boolean getIsEmpty() {
		return swtImage == null;
	}

	public Integer getHeight() {
		if (swtImage == null) {
			return swtImage.getBounds().height;
		}
		return swtImage.getBounds().height;
	}

	public void setHeight(Integer height) {
		if (swtImage == null) {
			this.height = height;
			makeInitialGraphic();
			return;
		}

		if (swtImage.getBounds().height != height) {
			this.height = height;
			this.width = swtImage.getBounds().width;

			makeRedimGraphic();
		}
	}

	public Integer getWidth() {
		if (swtImage == null) {
			return width;
		}
		return swtImage.getBounds().width;
	}

	public void setWidth(Integer width) {
		if (swtImage == null) {
			this.width = width;
			makeInitialGraphic();
			return;
		}

		if (swtImage.getBounds().width != width) {
			this.width = width;
			this.height = swtImage.getBounds().height;

			makeRedimGraphic();
		}
	}

	private void makeInitialGraphic() {
		if ((this.height > 0) && (this.width > 0)) {
			swtImage = new Image(Display.getDefault(), this.width, this.height);
			canvas = null;
			getCanvas().getBrush().setColor(IusCLColor.getStandardColor(IusCLStandardColors.clWhite));
			getCanvas().roundRect(0, 0, this.width, this.height, 0, 0);
		}
	}

	private void makeRedimGraphic() {
		IusCLGraphic tempGraphic = new IusCLGraphic();
		tempGraphic.loadFromGraphic(this);

		makeInitialGraphic();
		getCanvas().draw(0, 0, tempGraphic);
	}

	public IusCLGraphic resizeGraphic(Integer newWidth, Integer newHeight) {
		Image swtScaledImage = new Image(Display.getDefault(), newWidth, newHeight);

		GC gc = new GC(swtScaledImage);

		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);

		gc.drawImage(swtImage, 0, 0, swtImage.getBounds().width, swtImage.getBounds().height, 0, 0, newWidth, newHeight);
		gc.dispose();

		IusCLGraphic newGraphic = new IusCLGraphic();
		newGraphic.setSwtImage(swtScaledImage);

		return newGraphic;
	}
}
