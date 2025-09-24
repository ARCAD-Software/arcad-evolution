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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class ArcadColorUI {

	private RGB color;

	public ArcadColorUI(final ArcadColor color) {
		this.color = new RGB(color.getRed(), color.getGreen(), color.getBlue());
	}

	public ArcadColorUI(final int red, final int green, final int blue) {
		color = new RGB(red, green, blue);
	}

	public ArcadColorUI(final RGB color) {
		this.color = color;
	}

	public ArcadColor getArcadColor() {
		if (color != null) {
			return new ArcadColor(color.red, color.green, color.blue);
		}
		return null;
	}

	public RGB getColor() {
		return color;
	}

	public Color getSWTColor(final Device device) {
		return new Color(device, color);
	}

	public void setColor(final RGB color) {
		this.color = color;
	}
}
