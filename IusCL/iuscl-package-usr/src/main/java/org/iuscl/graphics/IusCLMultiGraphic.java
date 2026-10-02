/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.graphics;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLMultiGraphic extends IusCLGraphic {

	final List<IusCLGraphic> graphics = new ArrayList<>();

	protected void loadFromSwtImageDatas(ImageData[] swtImageDatas) {
		graphics.clear();

		for (int index = 0; index < swtImageDatas.length; index++) {
			IusCLGraphic graphic = new IusCLGraphic();
			graphic.loadFromSwtImageData(swtImageDatas[index]);

			graphics.add(graphic);
		}
	}

	protected void saveToSwtImageDatas() {
		ImageData[] swtImageDatas = new ImageData[graphics.size()];

		for (int index = 0; index < graphics.size(); index++) {
			IusCLGraphic graphic = graphics.get(index);
			swtImageDatas[index] = graphic.getSwtImage().getImageData();
		}

		swtImageLoader.data = swtImageDatas;
	}

	@Override
	public void loadFromGraphic(IusCLGraphic graphic) {
		super.loadFromGraphic(graphic);

		loadFromSwtImageDatas(swtImageLoader.data);
	}

	@Override
	public void loadFromFile(String fileName) {
		super.loadFromFile(fileName);

		loadFromSwtImageDatas(swtImageLoader.data);
	}

	@Override
	public void loadFromStream(InputStream stream) {
		if (stream == null) {
			return;
		}

		super.loadFromStream(stream);

		loadFromSwtImageDatas(swtImageLoader.data);
	}

	@Override
	public void saveToFile(String fileName) {
		saveToSwtImageDatas();
	}

	@Override
	public void saveToStream(OutputStream stream) {
		saveToSwtImageDatas();
	}

	public void add(IusCLGraphic graphic) {
		graphics.add(graphic);
		invokeNotify();
	}

	public void insert(Integer index, IusCLGraphic graphic) {
		graphics.add(index, graphic);
		invokeNotify();
	}

	public Integer size() {
		return graphics.size();
	}

	public IusCLGraphic get(Integer index) {
		return graphics.get(index);
	}

	public void set(Integer index, IusCLGraphic graphic) {
		graphics.set(index, graphic);
		invokeNotify();
	}

	public void delete(Integer index) {
		graphics.remove(index.intValue());
		invokeNotify();
	}

	public Integer indexOf(IusCLGraphic graphic) {
		return graphics.indexOf(graphic);
	}
}
