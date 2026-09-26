/*
IusCL - http://iuscl.org

This software is distributed under the terms of:
Eclipse Public License v1.0 - http://www.eclipse.org/org/documents/epl-v10.html
*/

package org.iuscl.comctrls;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.iuscl.classes.IusCLComponent;
import org.iuscl.classes.IusCLStrings;
import org.iuscl.comctrls.IusCLParagraphAttributes.IusCLNumberingStyle;
import org.iuscl.comctrls.IusCLParagraphAttributes.IusCLParagraphAlignment;
import org.iuscl.dialogs.IusCLDialogs;
import org.iuscl.graphics.IusCLColor;
import org.iuscl.graphics.IusCLFont;
import org.iuscl.stdctrls.IusCLMultiLineEditControl;
import org.iuscl.sysutils.IusCLStrUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IusCLRichEdit extends IusCLMultiLineEditControl {

	private static final String STYLE = "style";

	StyledText swtStyledText = null;

	IusCLParagraphAttributes paragraphAttributes = new IusCLParagraphAttributes();
	IusCLTextAttributes textAttributes = new IusCLTextAttributes();

	public IusCLRichEdit(IusCLComponent aOwner) {
		super(aOwner);

		IusCLFont.removeFontProperties(IusCLRichEdit.this, "Font");
		removeProperty("ParentFont");

		defineProperty("ParagraphAttributes.Alignment", IusCLPropertyType.ptEnum, "paLeftJustify", IusCLParagraphAlignment.paLeftJustify);

		defineProperty("ParagraphAttributes.Justified", IusCLPropertyType.ptBoolean, "false");

		defineProperty("ParagraphAttributes.FirstIndent", IusCLPropertyType.ptInteger, "0");
		defineProperty("ParagraphAttributes.LeftIndent", IusCLPropertyType.ptInteger, "0");
		defineProperty("ParagraphAttributes.RightIndent", IusCLPropertyType.ptInteger, "0");

		defineProperty("ParagraphAttributes.Numbering", IusCLPropertyType.ptEnum, "nsNone", IusCLNumberingStyle.nsNone);

		defineProperty("TextAttributes.Name", IusCLPropertyType.ptFontName, textAttributes.getName());
		defineProperty("TextAttributes.Size", IusCLPropertyType.ptInteger, textAttributes.getSize().toString());
		defineProperty("TextAttributes.Height", IusCLPropertyType.ptInteger, textAttributes.getHeight().toString());

		defineProperty("TextAttributes.Bold", IusCLPropertyType.ptBoolean, "false");
		defineProperty("TextAttributes.Italic", IusCLPropertyType.ptBoolean, "false");
		defineProperty("TextAttributes.Underline", IusCLPropertyType.ptBoolean, "false");
		defineProperty("TextAttributes.StrikeOut", IusCLPropertyType.ptBoolean, "false");

		defineProperty("TextAttributes.Color", IusCLPropertyType.ptColor, textAttributes.getColor().getAsString(),
				textAttributes.getColor().getAsStandardColor());
		defineProperty("TextAttributes.HighlightColor", IusCLPropertyType.ptColor, textAttributes.getHighlightColor().getAsString(),
				textAttributes.getHighlightColor().getAsStandardColor());
		defineProperty("TextAttributes.UnderlineColor", IusCLPropertyType.ptColor, textAttributes.getUnderlineColor().getAsString(),
				textAttributes.getUnderlineColor().getAsStandardColor());
		defineProperty("TextAttributes.StrikeOutColor", IusCLPropertyType.ptColor, textAttributes.getStrikeOutColor().getAsString(),
				textAttributes.getStrikeOutColor().getAsStandardColor());

		defineProperty("TextAttributes.UnderlineStyle", IusCLPropertyType.ptEnum, textAttributes.getUnderlineStyle().toString(),
				textAttributes.getUnderlineStyle());

		createWnd(createSwtControl());
	}

	@Override
	protected Control createSwtControl() {
		int swtCreateParams = SWT.H_SCROLL | SWT.V_SCROLL;

		if (this.getBorderStyle() == IusCLBorderStyle.bsSingle) {
			swtCreateParams = swtCreateParams | SWT.BORDER;
		}

		swtStyledText = new StyledText(this.getFormSwtComposite(), swtCreateParams);
		swtVerticalScrollBar = swtStyledText.getVerticalBar();
		swtHorizontalScrollBar = swtStyledText.getHorizontalBar();

		return swtStyledText;
	}

	@Override
	public IusCLFont getFont() {
		return this.getTextAttributes().getAsFont();
	}

	@Override
	public void setFont(IusCLFont font) {
		IusCLTextAttributes newTextAttributes = new IusCLTextAttributes();
		newTextAttributes.loadFromTextAttributes(getTextAttributes());
		newTextAttributes.loadFromFont(font, false);
		setTextAttributes(newTextAttributes);
	}

	@Override
	public boolean getParentFont() {
		return false;
	}

	@Override
	public void setParentFont(boolean parentFont) {
		/* Intentionally nothing */
	}

	@Override
	public IusCLStrings getLines() {
		IusCLStrings lines = new IusCLStrings();
		lines.setText(swtStyledText.getText());
		lines.setNotify(this, "setLines");
		return lines;
	}

	@Override
	public void setLines(IusCLStrings lines) {
		swtStyledText.setText(lines.getText());
	}

	@Override
	public String getText() {
		return swtStyledText.getText();
	}

	@Override
	public void setText(String text) {
		swtStyledText.setText(text);
	}

	@Override
	public void setMaxLength(Integer maxLength) {
		super.setMaxLength(maxLength);

		if (maxLength == 0) {
			maxLength = -1;
		}
		swtStyledText.setTextLimit(maxLength);
	}

	@Override
	public void setWordWrap(boolean wordWrap) {
		super.setWordWrap(wordWrap);
		swtStyledText.setWordWrap(wordWrap);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		swtStyledText.setEditable(!readOnly);
	}

	@Override
	public Integer getSelStart() {
		return swtStyledText.getSelectionRange().x;
	}

	@Override
	public void setSelStart(Integer selStart) {
		int selEnd = getSelStart() + getSelLength();
		swtStyledText.setSelectionRange(selStart, selEnd - selStart);
		swtStyledText.showSelection();
	}

	@Override
	public Integer getSelLength() {
		return swtStyledText.getSelectionRange().y;
	}

	@Override
	public void setSelLength(Integer selLength) {
		int start = getSelStart();
		swtStyledText.setSelectionRange(start, selLength);
		swtStyledText.showSelection();
	}

	@Override
	public String getSelText() {
		return swtStyledText.getSelectionText();
	}

	@Override
	public void setSelText(String selText) {
		int start = getSelStart();
		swtStyledText.replaceTextRange(start, getSelLength(), selText);
		setSelStart(start);
		setSelLength(selText.length());
		swtStyledText.showSelection();
	}

	public IusCLTextAttributes getTextAttributes() {
		textAttributes.setNotify(this, "setTextAttributes");

		return textAttributes;
	}

	public IusCLTextAttributes getSelTextAttributes() {
		return getSelTextAttributes(getSelStart());
	}

	public IusCLTextAttributes getSelTextAttributes(Integer position) {
		IusCLTextAttributes selTextAttributes = new IusCLTextAttributes();
		selTextAttributes.loadFromTextAttributes(this.textAttributes);

		StyleRange styleRange = null;
		if (position > 0) {
			styleRange = swtStyledText.getStyleRangeAtOffset(position - 1);
		}

		if (styleRange != null) {
			boolean bold = false;
			boolean italic = false;

			if (styleRange.font != null) {
				Font swtFont = styleRange.font;

				FontData fontData = swtFont.getFontData()[0];

				String fontName = fontData.getName();
				selTextAttributes.setName(fontName);

				int fontSize = fontData.getHeight();
				selTextAttributes.setSize(fontSize);

				FontData[] fds = swtFont.getFontData();
				for (int i = 0; i < fds.length; i++) {
					int fontStyle = fds[i].getStyle();
					if (!bold && (fontStyle & SWT.BOLD) != 0)
						bold = true;
					if (!italic && (fontStyle & SWT.ITALIC) != 0)
						italic = true;
				}
			} else {
				bold = (styleRange.fontStyle & SWT.BOLD) != 0;
				italic = (styleRange.fontStyle & SWT.ITALIC) != 0;
			}
			selTextAttributes.setBold(bold);
			selTextAttributes.setItalic(italic);

			selTextAttributes.setUnderline(styleRange.underline);
			selTextAttributes.setStrikeOut(styleRange.strikeout);

			if (styleRange.foreground != null) {
				IusCLColor color = new IusCLColor();
				color.loadFromSwtColor(styleRange.foreground);
				selTextAttributes.setColor(color);
			}
			if (styleRange.background != null) {
				IusCLColor highlightColor = new IusCLColor();
				highlightColor.loadFromSwtColor(styleRange.background);
				selTextAttributes.setHighlightColor(highlightColor);
			}
			if (styleRange.underlineColor != null) {
				IusCLColor underlineColor = new IusCLColor();
				underlineColor.loadFromSwtColor(styleRange.underlineColor);
				selTextAttributes.setUnderlineColor(underlineColor);
			}
			if (styleRange.strikeoutColor != null) {
				IusCLColor strikeOutColor = new IusCLColor();
				strikeOutColor.loadFromSwtColor(styleRange.strikeoutColor);
				selTextAttributes.setStrikeOutColor(strikeOutColor);
			}

			return selTextAttributes;
		}

		return selTextAttributes;
	}

	public void setTextAttributes(IusCLTextAttributes textAttributes) {
		this.textAttributes = textAttributes;

		IusCLFont font = textAttributes.getAsFont();

		swtStyledText.setFont(font.getSwtFont());

		swtStyledText.setForeground(textAttributes.getColor().getAsSwtColor());
		swtStyledText.setBackground(textAttributes.getHighlightColor().getAsSwtColor());
	}

	public void setSelTextAttributes(IusCLTextAttributes textAttributes) {
		setSelTextAttributes(textAttributes, getSelStart(), getSelLength());
	}

	public void setSelTextAttributes(IusCLTextAttributes textAttributes, Integer start, Integer length) {
		if (length == 0) {
			return;
		}

		/*
		 * Create new style range, all null and 0
		 */
		StyleRange newStyleRange = new StyleRange();

		Boolean bold = textAttributes.getBold();
		Boolean italic = textAttributes.getItalic();
		Boolean strikeOut = textAttributes.getStrikeOut();
		Boolean underline = textAttributes.getUnderline();

		IusCLColor color = textAttributes.getColor();
		IusCLColor highlightColor = textAttributes.getHighlightColor();
		IusCLColor underlineColor = textAttributes.getUnderlineColor();
		IusCLColor strikeOutColor = textAttributes.getStrikeOutColor();

		// IusCLUnderlineStyle underlineStyle = textAttributes.getUnderlineStyle();

		IusCLFont newFont = textAttributes.getAsFont(getTextAttributes().getAsFont());

		newStyleRange.font = newFont.getSwtFont();

		if ((bold != null) || (italic != null)) {
			newStyleRange.fontStyle = SWT.NORMAL;

			if (bold != null && bold) {
				newStyleRange.fontStyle = newStyleRange.fontStyle | SWT.BOLD;
			}

			if (italic != null && italic) {
				newStyleRange.fontStyle = newStyleRange.fontStyle | SWT.ITALIC;
			}
		}

		if (underline != null) {
			newStyleRange.underline = underline;
		}
		if (strikeOut != null) {
			newStyleRange.strikeout = strikeOut;
		}

		if (color != null) {
			newStyleRange.foreground = color.getAsSwtColor();
		}
		if (highlightColor != null) {
			newStyleRange.background = highlightColor.getAsSwtColor();
		}
		if (underlineColor != null) {
			newStyleRange.underlineColor = underlineColor.getAsSwtColor();
		}
		if (strikeOutColor != null) {
			newStyleRange.strikeoutColor = strikeOutColor.getAsSwtColor();
		}

		/* New range all selection */
		int newRangeStart = start;
		int newRangeLength = length;
		int[] oldRanges = swtStyledText.getRanges(start, length);
		StyleRange[] oldStyleRanges = swtStyledText.getStyleRanges(start, length, false);

		int maxCount = oldRanges.length * 2 + 2;
		int[] newRanges = new int[maxCount];
		StyleRange[] newStyleRanges = new StyleRange[maxCount / 2];

		int count = 0;
		for (int index = 0; index < oldRanges.length; index += 2) {
			int currentOldRangeStart = oldRanges[index];
			int currentOldRangeLength = oldRanges[index + 1];
			StyleRange currentOldStyleRange = oldStyleRanges[index / 2];

			if (currentOldRangeStart > newRangeStart) {
				/* Before the beginning of the current existing range */
				newRangeLength = currentOldRangeStart - newRangeStart;
				newRanges[count] = newRangeStart;
				newRanges[count + 1] = newRangeLength;
				newStyleRanges[count / 2] = newStyleRange;
				count += 2;
			}
			/* New range remaining selection after the end of current existing range */
			newRangeStart = currentOldRangeStart + currentOldRangeLength;
			newRangeLength = (start + length) - newRangeStart;

			/*
			 * Create merged style range
			 */
			StyleRange currentMergedStyleRange = new StyleRange(currentOldStyleRange);
			/* Note: fontStyle is not copied by the constructor */
			currentMergedStyleRange.fontStyle = currentOldStyleRange.fontStyle;

			/* Start merging with newStyleRange */
			IusCLFont mergedFont = new IusCLFont();

			if (currentMergedStyleRange.font != null) {
				mergedFont.setSwtFont(currentMergedStyleRange.font);
			} else {
				mergedFont.setSwtFont(getTextAttributes().getAsFont().getSwtFont());
			}

			IusCLFont newMergedFont = textAttributes.getAsFont(mergedFont);
			currentMergedStyleRange.font = newMergedFont.getSwtFont();

			if ((bold != null) || (italic != null)) {
				if (bold != null) {
					if (bold) {
						currentMergedStyleRange.fontStyle |= SWT.BOLD;
					} else {
						currentMergedStyleRange.fontStyle &= ~(SWT.BOLD);
					}
				}
				if (italic != null) {
					if (italic) {
						currentMergedStyleRange.fontStyle |= SWT.ITALIC;
					} else {
						currentMergedStyleRange.fontStyle &= ~(SWT.ITALIC);
					}
				}
			}

			if (underline != null) {
				currentMergedStyleRange.underline = underline;
			}
			if (strikeOut != null) {
				currentMergedStyleRange.strikeout = strikeOut;
			}

			if (color != null) {
				currentMergedStyleRange.foreground = color.getAsSwtColor();
			}
			if (highlightColor != null) {
				currentMergedStyleRange.background = highlightColor.getAsSwtColor();
			}
			if (underlineColor != null) {
				currentMergedStyleRange.underlineColor = underlineColor.getAsSwtColor();
			}
			if (strikeOutColor != null) {
				currentMergedStyleRange.strikeoutColor = strikeOutColor.getAsSwtColor();
			}

			/* Add the merged range */
			newRanges[count] = currentOldRangeStart;
			newRanges[count + 1] = currentOldRangeLength;
			newStyleRanges[count / 2] = currentMergedStyleRange;
			count += 2;
		}

		/* After the end of the last existing range */
		if (newRangeLength > 0) {
			newRanges[count] = newRangeStart;
			newRanges[count + 1] = newRangeLength;
			newStyleRanges[count / 2] = newStyleRange;
			count += 2;
		}

		/* Compact the ranges */
		if ((0 < count) && (count < maxCount)) {
			int[] tmpRanges = new int[count];
			StyleRange[] tmpStyles = new StyleRange[count / 2];
			System.arraycopy(newRanges, 0, tmpRanges, 0, count);
			System.arraycopy(newStyleRanges, 0, tmpStyles, 0, count / 2);
			newRanges = tmpRanges;
			newStyleRanges = tmpStyles;
		}

		swtStyledText.setStyleRanges(start, length, newRanges, newStyleRanges);
	}

	public IusCLParagraphAttributes getParagraphAttributes() {
		paragraphAttributes.setNotify(this, "setParagraphAttributes");

		return paragraphAttributes;
	}

	public IusCLParagraphAttributes getSelParagraphAttributes() {
		return getSelParagraphAttributes(getSelStart());
	}

	public IusCLParagraphAttributes getSelParagraphAttributes(Integer position) {
		IusCLParagraphAttributes selParagraphAttributes = new IusCLParagraphAttributes();

		int lineIndex = swtStyledText.getLineAtOffset(position);
		int alignment = swtStyledText.getLineAlignment(lineIndex);

		if ((alignment & SWT.LEFT) != 0) {
			selParagraphAttributes.setAlignment(IusCLParagraphAlignment.paLeftJustify);
		}
		if ((alignment & SWT.CENTER) != 0) {
			selParagraphAttributes.setAlignment(IusCLParagraphAlignment.paCenter);
		}
		if ((alignment & SWT.RIGHT) != 0) {
			selParagraphAttributes.setAlignment(IusCLParagraphAlignment.paRightJustify);
		}

		boolean justify = swtStyledText.getLineJustify(lineIndex);
		selParagraphAttributes.setJustified(justify);

		int firstIndent = swtStyledText.getLineIndent(lineIndex);
		selParagraphAttributes.setFirstIndent(firstIndent);

		int leftIndent = swtStyledText.getLineWrapIndent(lineIndex);
		selParagraphAttributes.setLeftIndent(leftIndent);

		return selParagraphAttributes;
	}

	public void setParagraphAttributes(IusCLParagraphAttributes paragraphAttributes) {
		this.paragraphAttributes = paragraphAttributes;

		switch (paragraphAttributes.getAlignment()) {
		case paCenter:
			swtStyledText.setAlignment(SWT.CENTER);
			break;
		case paLeftJustify:
			swtStyledText.setAlignment(SWT.LEFT);
			break;
		case paRightJustify:
			swtStyledText.setAlignment(SWT.RIGHT);
			break;
		}

		swtStyledText.setJustify(paragraphAttributes.getJustified());

		swtStyledText.setIndent(paragraphAttributes.getFirstIndent());
		swtStyledText.setWrapIndent(paragraphAttributes.getLeftIndent());
	}

	public void setSelParagraphAttributes(IusCLParagraphAttributes paragraphAttributes) {
		setSelParagraphAttributes(paragraphAttributes, getSelStart(), getSelLength());
	}

	public void setSelParagraphAttributes(IusCLParagraphAttributes paragraphAttributes, Integer start, Integer length) {
		int lineStart = swtStyledText.getLineAtOffset(start);
		int lineEnd = swtStyledText.getLineAtOffset(start + length);

		if (paragraphAttributes.getAlignment() != null) {
			switch (paragraphAttributes.getAlignment()) {
			case paCenter:
				swtStyledText.setLineAlignment(lineStart, lineEnd - lineStart + 1, SWT.CENTER);
				break;
			case paLeftJustify:
				swtStyledText.setLineAlignment(lineStart, lineEnd - lineStart + 1, SWT.LEFT);
				break;
			case paRightJustify:
				swtStyledText.setLineAlignment(lineStart, lineEnd - lineStart + 1, SWT.RIGHT);
				break;
			}
		}

		if (paragraphAttributes.getJustified() != null) {
			swtStyledText.setLineJustify(lineStart, lineEnd - lineStart + 1, paragraphAttributes.getJustified());
		}

		if (paragraphAttributes.getFirstIndent() != null) {
			swtStyledText.setLineIndent(lineStart, lineEnd - lineStart + 1, paragraphAttributes.getFirstIndent());
		}

		if (paragraphAttributes.getLeftIndent() != null) {
			swtStyledText.setLineWrapIndent(lineStart, lineEnd - lineStart + 1, paragraphAttributes.getLeftIndent());
		}
	}

	private String getCssStyleDifferenceFromTextAttributes(IusCLTextAttributes textAttributes, IusCLTextAttributes anotherTextAttributes) {
		/* Font */
		String cssStyle = "";

		if ((anotherTextAttributes == null) || (!IusCLStrUtils.equalValues(textAttributes.getName(), anotherTextAttributes.getName()))) {
			cssStyle = "font-family: '" + textAttributes.getName() + "'; ";
		}

		if ((anotherTextAttributes == null) || (!Objects.equals(textAttributes.getSize(), anotherTextAttributes.getSize()))) {
			cssStyle = cssStyle + "font-size: " + textAttributes.getSize() + "pt; ";
		}

		if ((anotherTextAttributes == null) || (!Objects.equals(textAttributes.getItalic(), anotherTextAttributes.getItalic()))) {
			if (Boolean.TRUE.equals(textAttributes.getItalic())) {
				cssStyle = cssStyle + "font-style: italic; ";
			} else {
				cssStyle = cssStyle + "font-style: normal; ";
			}
		}

		if ((anotherTextAttributes == null) || (!Objects.equals(textAttributes.getBold(), anotherTextAttributes.getBold()))) {
			if (Boolean.TRUE.equals(textAttributes.getBold())) {
				cssStyle = cssStyle + "font-weight: bold; ";
			} else {
				cssStyle = cssStyle + "font-weight: normal; ";
			}
		}

		String cssTextDecoration = "";

		if ((anotherTextAttributes == null) || (!Objects.equals(textAttributes.getUnderline(), anotherTextAttributes.getUnderline()))) {
			if (Boolean.TRUE.equals(textAttributes.getUnderline())) {
				cssTextDecoration = cssTextDecoration + " underline";
			} else {
				cssTextDecoration = cssTextDecoration + " normal";
			}
		}

		if ((anotherTextAttributes == null) || (!Objects.equals(textAttributes.getStrikeOut(), anotherTextAttributes.getStrikeOut()))) {
			if (Boolean.TRUE.equals(textAttributes.getStrikeOut())) {
				cssTextDecoration = cssTextDecoration + " line-through";
			} else {
				cssTextDecoration = cssTextDecoration + " normal";
			}
		}

		if (IusCLStrUtils.isNotNullNotEmpty(cssTextDecoration)) {
			cssStyle = cssStyle + "text-decoration:" + cssTextDecoration;

			// if ((anotherTextAttributes == null) ||
			// (textAttributes.getStrikeOutColor().equalValue(
			// anotherTextAttributes.getStrikeOutColor()) == false)) {
			//
			// cssStyle = cssStyle + " color: " +
			// textAttributes.getStrikeOutColor().getAsHTMLColor();
			// }

			/*
			 * TODO
			 * 
			 * For text-decoration color, another span is needed containing the font style
			 * span
			 */
			cssStyle = cssStyle + "; ";

		}

		if ((anotherTextAttributes == null) || (!textAttributes.getColor().equalValue(anotherTextAttributes.getColor()))) {
			cssStyle = cssStyle + "color: " + textAttributes.getColor().getAsHTMLColor() + "; ";
		}

		if ((anotherTextAttributes == null) || (!textAttributes.getHighlightColor().equalValue(anotherTextAttributes.getHighlightColor()))) {
			cssStyle = cssStyle + "background-color: " + textAttributes.getHighlightColor().getAsHTMLColor() + "; ";
		}

		return cssStyle;
	}

	private String getCssStyleDifferenceFromParagraphAttributes(IusCLParagraphAttributes paragraphAttributes,
			IusCLParagraphAttributes anotherParagraphAttributes) {
		String cssStyle = "";

		/* Paragraph */

		boolean justified = paragraphAttributes.getJustified();
		if ((justified) && ((anotherParagraphAttributes == null) || (justified != anotherParagraphAttributes.getJustified()))) {
			cssStyle = cssStyle + "text-align: justify; ";

			if ((anotherParagraphAttributes == null) || (paragraphAttributes.getAlignment() != anotherParagraphAttributes.getAlignment())) {
				switch (paragraphAttributes.getAlignment()) {
				case paCenter:
					cssStyle = cssStyle + "text-align-last: center; ";
					break;
				case paLeftJustify:
					cssStyle = cssStyle + "text-align-last: left; ";
					break;
				case paRightJustify:
					cssStyle = cssStyle + "text-align-last: right; ";
					break;
				}
			}
		} else {
			if ((anotherParagraphAttributes == null) || (paragraphAttributes.getAlignment() != anotherParagraphAttributes.getAlignment())) {
				switch (paragraphAttributes.getAlignment()) {
				case paCenter:
					cssStyle = cssStyle + "text-align: center; ";
					break;
				case paLeftJustify:
					cssStyle = cssStyle + "text-align: left; ";
					break;
				case paRightJustify:
					cssStyle = cssStyle + "text-align: right; ";
					break;
				}
			}
		}

		Integer firstIndent = paragraphAttributes.getFirstIndent();
		if (firstIndent != 0
				&& ((anotherParagraphAttributes == null) || (!Objects.equals(firstIndent, anotherParagraphAttributes.getFirstIndent())))) {
			cssStyle = cssStyle + "text-indent: " + firstIndent + "px; ";
		}

		Integer leftIndent = paragraphAttributes.getLeftIndent();
		if (leftIndent != 0 && ((anotherParagraphAttributes == null) || (!Objects.equals(leftIndent, anotherParagraphAttributes.getLeftIndent())))) {
			cssStyle = cssStyle + "margin-left: " + leftIndent + "px; ";
		}

		Integer rightIndent = paragraphAttributes.getRightIndent();
		if (rightIndent != 0
				&& ((anotherParagraphAttributes == null) || (!Objects.equals(rightIndent, anotherParagraphAttributes.getRightIndent())))) {
			cssStyle = cssStyle + "margin-right: " + rightIndent + "px; ";
		}

		return cssStyle;
	}

	public IusCLStrings getAsHtmlLines() {
		return getAsHtmlLines(0, swtStyledText.getCharCount());
	}

	public IusCLStrings getAsHtmlLines(Integer start, Integer length) {
		Element jdomDiv = new Element("div");

		String htmlDivStyle = getCssStyleDifferenceFromParagraphAttributes(paragraphAttributes, null);
		htmlDivStyle = htmlDivStyle + getCssStyleDifferenceFromTextAttributes(textAttributes, null);

		Attribute jdomDivStyle = new Attribute(STYLE, htmlDivStyle);
		jdomDiv.setAttribute(jdomDivStyle);

		StyledTextContent swtStyledTextContent = swtStyledText.getContent();
		int end = start + length;

		int startLine = swtStyledTextContent.getLineAtOffset(start);
		int endLine = swtStyledTextContent.getLineAtOffset(end);

		for (int lineIndex = startLine; lineIndex <= endLine; lineIndex++) {
			String line = swtStyledTextContent.getLine(lineIndex);
			int lineOffset = swtStyledTextContent.getOffsetAtLine(lineIndex);

			Element jdomP = new Element("p");

			String htmlPStyle = "margin-bottom: 0; margin-top: 0; ";
			htmlPStyle = htmlPStyle + getCssStyleDifferenceFromParagraphAttributes(getSelParagraphAttributes(lineOffset), paragraphAttributes);

			Attribute jdomPStyle = new Attribute(STYLE, htmlPStyle);
			jdomP.setAttribute(jdomPStyle);

			int[] lineRanges = swtStyledText.getRanges(lineOffset, line.length());

//			StyleRange[] lineStyleRanges = swtStyledText.getStyleRanges(lineOffset, line.length(), false);
//			if (lineStyleRanges == null) {
//				lineStyleRanges = new StyleRange[0];
//			}

			int pos = 0;

			for (int index = 0; index < lineRanges.length; index += 2) {
				int currentLineRangeStart = lineRanges[index] - lineOffset;
				int currentLineRangeLength = lineRanges[index + 1];

				if (pos < currentLineRangeStart) {
					jdomP.addContent(line.substring(pos, currentLineRangeStart));
				}

				Element jdomSpan = new Element("span");
				jdomSpan.addContent(line.substring(currentLineRangeStart, currentLineRangeStart + currentLineRangeLength));

				String htmlSpanStyle = getCssStyleDifferenceFromTextAttributes(getSelTextAttributes(lineOffset + currentLineRangeStart + 1),
						textAttributes);
				Attribute jdomSpanStyle = new Attribute(STYLE, htmlSpanStyle);
				jdomSpan.setAttribute(jdomSpanStyle);

				jdomP.addContent(jdomSpan);

				pos = currentLineRangeStart + currentLineRangeLength;
			}

			if (pos < line.length()) {
				jdomP.addContent(line.substring(pos));
			}

			if (line.isEmpty()) {
				Element jdomBr = new Element("br");
				jdomP.addContent(jdomBr);
			}

			jdomDiv.addContent(jdomP);
		}

		/*
		 * TODO
		 * 
		 * Concatenate line spans which have identical style
		 */

		String ls = IusCLStrUtils.sLineBreak();

		Document jdomDocument = new Document(jdomDiv);
		Format jdomSerializeFormat = Format.getRawFormat();
		// jdomSerializeFormat.setIndent(" ");
		// jdomSerializeFormat.setLineSeparator(IusCLStrUtils.sLineBreak());
		XMLOutputter jdomSerializer = new XMLOutputter(jdomSerializeFormat);
		String jdomSerialized = jdomSerializer.outputString(jdomDocument);

		jdomSerialized = jdomSerialized.replace("<p", "<p" + ls);
		jdomSerialized = jdomSerialized.replace("</p", "</p" + ls);
		jdomSerialized = jdomSerialized.replace("<span", "<span" + ls);
		jdomSerialized = jdomSerialized.replace("</span", "</span" + ls);
		IusCLStrings htmlLines = new IusCLStrings();
		htmlLines.setText(jdomSerialized);

		return htmlLines;
	}

	public void setFromHtmlLines(IusCLStrings htmlLines) {
		setFromHtmlLines(htmlLines, 0, swtStyledText.getCharCount());
	}

	public void setFromHtmlLines(IusCLStrings htmlLines, Integer start, Integer length) {
		/*
		 * TODO
		 * 
		 * Load from html, reverse operation:
		 * 
		 * div --> whole text p --> line span --> styleRange
		 */

		IusCLDialogs.showError("IusCLRichEdit - setFromHtmlLines, not implemented");
	}
}
