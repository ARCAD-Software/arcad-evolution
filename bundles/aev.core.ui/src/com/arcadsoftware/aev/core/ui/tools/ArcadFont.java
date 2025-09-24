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

import java.util.Objects;

public class ArcadFont {

	private boolean bold = false;
	private ArcadColor fontColor;
	private String fontName;
	private int fontSize;
	private boolean italic = false;

	@Override
	public boolean equals(final Object o) {
		if (o instanceof ArcadFont) {
			final ArcadFont font = (ArcadFont) o;
			return font.getFontColor().equals(fontColor) &&
					font.getFontName().equals(fontName) &&
					font.getFontSize() == fontSize &&
					font.isBold() == bold &&
					font.isItalic() == italic;
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fontColor, fontName, fontSize, bold, italic);
	}
	
	public ArcadColor getFontColor() {
		return fontColor;
	}

	public String getFontName() {
		return fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setBold(final boolean bold) {
		this.bold = bold;
	}

	public void setFontColor(final ArcadColor fontColor) {
		this.fontColor = fontColor;
	}

	public void setFontName(final String fontName) {
		this.fontName = fontName;
	}

	public void setFontSize(final int fontSize) {
		this.fontSize = fontSize;
	}

	public void setItalic(final boolean italic) {
		this.italic = italic;
	}
}