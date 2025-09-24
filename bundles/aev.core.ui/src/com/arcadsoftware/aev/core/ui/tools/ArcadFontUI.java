/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class ArcadFontUI {

	private RGB color;
	private FontData fontData;

	public ArcadFontUI() {
		super();
	}

	public ArcadFontUI(final ArcadFont font) {
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

	public ArcadFont getArcadFont() {
		if (fontData != null) {
			final ArcadFont arcadFont = new ArcadFont();
			arcadFont.setBold(fontData.getStyle() == SWT.BOLD || fontData.getStyle() == SWT.BOLD + SWT.ITALIC);
			arcadFont.setFontColor(new ArcadColorUI(color).getArcadColor());
			arcadFont.setFontName(fontData.getName());
			arcadFont.setFontSize(fontData.getHeight());
			arcadFont.setItalic(fontData.getStyle() == SWT.ITALIC || fontData.getStyle() == SWT.BOLD + SWT.ITALIC);
			// arcadFont.setScript("" + fontData.data.lfCharSet);
			// arcadFont.setStrike(fontData.data.lfStrikeOut == 1);
			// arcadFont.setUnderline(fontData.data.lfUnderline == 1);
			return arcadFont;
		}
		return null;
	}

	public RGB getColor() {
		return color;
	}

	public FontData getFontData() {
		return fontData;
	}

	public void setColor(final RGB color) {
		this.color = color;
	}

	public void setFontData(final FontData fontData) {
		this.fontData = fontData;
	}

}
