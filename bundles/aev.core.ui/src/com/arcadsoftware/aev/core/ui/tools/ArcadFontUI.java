package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class ArcadFontUI {

	private FontData fontData;
	private RGB color;

	public ArcadFontUI(ArcadFont font) {
		super();
		if (font != null) {
			if (font.getFontColor() != null) {
				color = new ArcadColorUI(font.getFontColor()).getColor();
			}
			fontData = new FontData();
			fontData.setName(font.getFontName());
			fontData.setHeight(font.getFontSize());
			fontData.setStyle((font.isBold() ? SWT.BOLD : SWT.NORMAL) | (font.isItalic() ? SWT.ITALIC : SWT.NORMAL));
		}
	}

	public ArcadFontUI() {
		super();
	}

	public ArcadFont getArcadFont() {
		if (fontData != null) {
			ArcadFont arcadFont = new ArcadFont();
			arcadFont.setBold((fontData.getStyle() == SWT.BOLD) || (fontData.getStyle() == SWT.BOLD + SWT.ITALIC));
			arcadFont.setFontColor(new ArcadColorUI(color).getArcadColor());
			arcadFont.setFontName(fontData.getName());
			arcadFont.setFontSize(fontData.getHeight());
			arcadFont.setItalic((fontData.getStyle() == SWT.ITALIC) || (fontData.getStyle() == SWT.BOLD + SWT.ITALIC));
			// arcadFont.setScript("" + fontData.data.lfCharSet);
			// arcadFont.setStrike(fontData.data.lfStrikeOut == 1);
			// arcadFont.setUnderline(fontData.data.lfUnderline == 1);
			return arcadFont;
		}
		return null;
	}

	public FontData getFontData() {
		return fontData;
	}

	public void setFontData(FontData fontData) {
		this.fontData = fontData;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

}
