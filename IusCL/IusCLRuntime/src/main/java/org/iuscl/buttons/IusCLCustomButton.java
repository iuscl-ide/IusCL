/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.buttons;

import org.iuscl.classes.IusCLComponent;
import org.iuscl.graphics.IusCLCanvas;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLColor.IusCLStandardColors;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.graphics.IusCLGraphic;
import org.iuscl.stdctrls.IusCLButtonControl;
import org.iuscl.sysutils.IusCLGraphUtils;
import org.iuscl.sysutils.IusCLStrUtils;
import org.iuscl.types.IusCLSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLCustomButton extends IusCLButtonControl {

	public enum IusCLButtonLayout {
		blGlyphLeft, blGlyphRight, blGlyphTop, blGlyphBottom
	}

	protected enum IusCLButtonState {
		bsUp, bsDown, bsClicked, bsDisabled
	}

	protected IusCLButtonState state = IusCLButtonState.bsUp;

	@Getter
	IusCLButtonLayout layout = IusCLButtonLayout.blGlyphLeft;
	@Getter
	IusCLButtonGlyph glyph = new IusCLButtonGlyph();

	@Getter
	Integer margin = -1;
	@Getter
	Integer spacing = 4;

	IusCLFont disabledFont = new IusCLFont();

	protected IusCLGraphic upGraphic = null;
	protected IusCLGraphic disabledGraphic = null;
	protected IusCLGraphic clickedGraphic = null;
	protected IusCLGraphic downGraphic = null;

	public IusCLCustomButton(IusCLComponent aOwner) {
		super(aOwner);

		defineProperty("Layout", IusCLPropertyType.ptEnum, "blGlyphLeft", IusCLButtonLayout.blGlyphLeft);

		defineProperty("Margin", IusCLPropertyType.ptInteger, "-1");
		defineProperty("Spacing", IusCLPropertyType.ptInteger, "4");

		defineProperty("Glyph.Up", IusCLPropertyType.ptPicture, "");
		defineProperty("Glyph.Disabled", IusCLPropertyType.ptPicture, "");
		defineProperty("Glyph.Clicked", IusCLPropertyType.ptPicture, "");
		defineProperty("Glyph.Down", IusCLPropertyType.ptPicture, "");

		/* Font */
		getDisabledFont().setColor(new IusCLColor(IusCLStandardColors.clGrayText));
		defineProperty("DisabledFont", IusCLPropertyType.ptFont, "(IusCLFont)");
		IusCLFont.defineFontProperties(this, "DisabledFont", disabledFont);
	}

	@Override
	protected void reCreate() {
		super.reCreate();

		swtButton.addPaintListener(swtPaintEvent -> {
			/*
			 * TODO Optimize paint, it will flicker when mouse move
			 * 
			 * 
			 */
			IusCLCanvas canvas = new IusCLCanvas(swtPaintEvent.gc);
			drawButton(caption, getGraphic(), canvas);
		});
	}

	private void drawButton(String caption, IusCLGraphic graphic, IusCLCanvas canvas) {
		int totalWidth = IusCLCustomButton.this.getWidth();
		int totalHeight = IusCLCustomButton.this.getHeight();

		int imageWidth = 0;
		int imageHeight = 0;

		int captionLeft = 0;
		int captionTop = 0;
		int imageLeft = 0;
		int imageTop = 0;

		if (IusCLStrUtils.isNotNullNotEmpty(caption)) {
			IusCLSize captionSize = canvas.textExtent(IusCLCustomButton.this.getCaption());

			int captionWidth = captionSize.getWidth();
			int captionHeight = captionSize.getHeight();

			if (graphic != null) {
				/* Both caption and image */

				imageWidth = graphic.getWidth();
				imageHeight = graphic.getHeight();

				switch (layout) {
				case blGlyphBottom:
					captionLeft = posOneItem(totalWidth, captionWidth, true, -1);
					captionTop = posTwoItems(totalHeight, imageHeight, captionHeight, true, false);
					imageLeft = posOneItem(totalWidth, imageWidth, true, -1);
					imageTop = posTwoItems(totalHeight, imageHeight, captionHeight, false, false);
					break;
				case blGlyphLeft:
					captionLeft = posTwoItems(totalWidth, imageWidth, captionWidth, false, true);
					captionTop = posOneItem(totalHeight, captionHeight, true, -1);
					imageLeft = posTwoItems(totalWidth, imageWidth, captionWidth, true, true);
					imageTop = posOneItem(totalHeight, imageHeight, true, -1);
					break;
				case blGlyphRight:
					captionLeft = posTwoItems(totalWidth, captionWidth, imageWidth, true, false);
					captionTop = posOneItem(totalHeight, captionHeight, true, -1);
					imageLeft = posTwoItems(totalWidth, captionWidth, imageWidth, false, false);
					imageTop = posOneItem(totalHeight, imageHeight, true, -1);
					break;
				case blGlyphTop:
					captionLeft = posOneItem(totalWidth, captionWidth, true, -1);
					captionTop = posTwoItems(totalHeight, imageHeight, captionHeight, false, true);
					imageLeft = posOneItem(totalWidth, imageWidth, true, -1);
					imageTop = posTwoItems(totalHeight, imageHeight, captionHeight, true, true);
					break;
				}
			} else {
				/* Only caption */

				switch (layout) {
				case blGlyphBottom:
					captionLeft = posOneItem(totalWidth, captionWidth, true, -1);
					captionTop = posOneItem(totalHeight, captionHeight, false, margin);
					break;
				case blGlyphLeft:
					captionLeft = posOneItem(totalWidth, captionWidth, true, margin);
					captionTop = posOneItem(totalHeight, captionHeight, true, -1);
					break;
				case blGlyphRight:
					captionLeft = posOneItem(totalWidth, captionWidth, false, margin);
					captionTop = posOneItem(totalHeight, captionHeight, true, -1);
					break;
				case blGlyphTop:
					captionLeft = posOneItem(totalWidth, captionWidth, true, -1);
					captionTop = posOneItem(totalHeight, captionHeight, true, margin);
					break;
				}
			}
		} else {
			if (graphic != null) {
				/* Only image */

				imageWidth = graphic.getWidth();
				imageHeight = graphic.getHeight();

				switch (layout) {
				case blGlyphBottom:
					imageLeft = posOneItem(totalWidth, imageWidth, true, -1);
					imageTop = posOneItem(totalHeight, imageHeight, false, margin);
					break;
				case blGlyphLeft:
					imageLeft = posOneItem(totalWidth, imageWidth, true, margin);
					imageTop = posOneItem(totalHeight, imageHeight, true, -1);
					break;
				case blGlyphRight:
					imageLeft = posOneItem(totalWidth, imageWidth, false, margin);
					imageTop = posOneItem(totalHeight, imageHeight, true, -1);
					break;
				case blGlyphTop:
					imageLeft = posOneItem(totalWidth, imageWidth, true, -1);
					imageTop = posOneItem(totalHeight, imageHeight, true, margin);
					break;
				}
			}
		}

		/* Caption */
		if (IusCLStrUtils.isNotNullNotEmpty(caption)) {
			if (state == IusCLButtonState.bsDisabled) {
				canvas.setFont(getDisabledFont());
			} else {
				canvas.setFont(getFont());
			}

			if ((state == IusCLButtonState.bsClicked) || (state == IusCLButtonState.bsDown)) {
				canvas.textOut(captionLeft + 1, captionTop + 1, caption);
			} else {
				canvas.textOut(captionLeft, captionTop, caption);
			}
		}

		/* Image */
		if (graphic != null) {
			if ((state == IusCLButtonState.bsClicked) || (state == IusCLButtonState.bsDown)) {
				canvas.draw(imageLeft + 1, imageTop + 1, graphic);
			} else {
				canvas.draw(imageLeft, imageTop, graphic);
			}
		}
	}

	private Integer posOneItem(Integer totalSize, Integer itemSize, boolean begin, Integer margin) {
		if (margin == -1) {
			/* Center */
			return (totalSize - itemSize) / 2;
		}
		if (begin) {
			return margin;
		}
		return totalSize - (itemSize + margin);
	}

	private Integer posTwoItems(Integer totalSize, Integer itemOneSize, Integer itemTwoSize, boolean first, boolean begin) {
		if (margin == -1) {
			if (spacing == -1) {
				if (first) {
					return (totalSize - (itemOneSize + itemTwoSize)) / 3;
				} else {
					return ((totalSize - (itemOneSize + itemTwoSize)) / 3) * 2 + itemOneSize;
				}
			} else {
				if (first) {
					return (totalSize - (itemOneSize + itemTwoSize + spacing)) / 2;
				} else {
					return (totalSize - (itemOneSize + itemTwoSize + spacing)) / 2 + itemOneSize + spacing;
				}
			}
		} else {
			if (spacing == -1) {
				if (first) {
					if (begin) {
						return margin;
					} else {
						return (totalSize - (itemOneSize + itemTwoSize + margin)) / 2;
					}
				} else {
					/* Second */
					if (begin) {
						return (totalSize - (itemOneSize + itemTwoSize + margin)) / 2 + itemOneSize + margin;
					} else {
						return totalSize - (itemTwoSize + margin);
					}
				}
			} else {
				if (first) {
					if (begin) {
						return margin;
					} else {
						return totalSize - (itemOneSize + spacing + itemTwoSize + margin);
					}
				} else {
					/* Second */
					if (begin) {
						return margin + itemOneSize + spacing;
					} else {
						return totalSize - (itemTwoSize + margin);
					}
				}
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			state = IusCLButtonState.bsUp;
		} else {
			state = IusCLButtonState.bsDisabled;
		}
		update();
	}

	protected void loadGraphic() {
		/* Images */
		if (!IusCLGraphUtils.isEmptyPicture(glyph.getUp())) {
			upGraphic = glyph.getUp().getGraphic();
		} else {
			upGraphic = null;
		}
		if (!IusCLGraphUtils.isEmptyPicture(glyph.getDown())) {
			downGraphic = glyph.getDown().getGraphic();
		} else {
			downGraphic = upGraphic;
		}
		if (!IusCLGraphUtils.isEmptyPicture(glyph.getClicked())) {
			clickedGraphic = glyph.getClicked().getGraphic();
		} else {
			clickedGraphic = upGraphic;
		}
		if (!IusCLGraphUtils.isEmptyPicture(glyph.getDisabled())) {
			disabledGraphic = glyph.getDisabled().getGraphic();
		} else {
			/* TODO Make the ugly disable */
			disabledGraphic = upGraphic;
		}
	}

	public IusCLGraphic getGraphic() {
		IusCLGraphic graphic = null;
		switch (state) {
		case bsClicked:
			graphic = clickedGraphic;
			break;
		case bsDisabled:
			graphic = disabledGraphic;
			break;
		case bsDown:
			graphic = downGraphic;
			break;
		case bsUp:
			graphic = upGraphic;
			break;
		}
		return graphic;
	}

	public void setGlyph(IusCLButtonGlyph glyph) {
		this.glyph = glyph;
		update();
	}

	public void setLayout(IusCLButtonLayout layout) {
		this.layout = layout;
		update();
	}

	public void update() {
		if (swtButton != null) {
			swtButton.redraw();
		}
	}

	@Override
	public void setCaption(String caption) {
		this.caption = caption;
		update();
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
		update();
	}

	public void setSpacing(Integer spacing) {
		this.spacing = spacing;
		update();
	}

	public IusCLFont getDisabledFont() {
		disabledFont.setNotify(this, "setDisabledFont");
		return disabledFont;
	}

	public void setDisabledFont(IusCLFont disabledFont) {
		this.disabledFont = disabledFont;
		update();
	}
}
