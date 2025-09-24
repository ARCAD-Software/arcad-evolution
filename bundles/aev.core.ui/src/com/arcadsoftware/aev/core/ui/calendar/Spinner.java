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
/**
 *  <p>Spinner.java  - A spinner component<br>
 *  Mark Bryan Yu<br>
 *  swtcalendar.sourceforge.net</p>
 *
 *  <p>This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.</p>
 *
 *  <p>This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.</p>
 *
 *  <p>You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.</p>
 */
package com.arcadsoftware.aev.core.ui.calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TypedListener;

// [Compatibility note] GTK/WIN32 Compatible.

public class Spinner extends Composite {
	static final int BUTTON_WIDTH = 16;
	private final Font font;
	private int minimum, maximum;
	private boolean roll;
	private final Label text;
	private final Button up, down;

	public Spinner(final Composite parent, final int style) {
		super(parent, style);
		text = new Label(this, style | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);

		up.addListener(SWT.Selection, e -> up());

		down.addListener(SWT.Selection, e -> down());

		addListener(SWT.Resize, e -> resize());

		final Color oldBackground = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		font = new Font(getDisplay(), text.getFont().getFontData()[0].getName(),
				text.getFont().getFontData()[0].getHeight() + 2, text.getFont().getFontData()[0].getStyle());
		text.setFont(font);
		text.setBackground(oldBackground);

		minimum = 0;
		maximum = 9;
		roll = false;
		setSelection(minimum);

		addDisposeListener(event -> font.dispose());
	}

	public void addSelectionListener(final SelectionListener listener) {
		if (listener == null) {
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		}
		addListener(SWT.Selection, new TypedListener(listener));
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final GC gc = new GC(text);
		final Point textExtent = gc.textExtent(String.valueOf(maximum));
		gc.dispose();
		final Point pt = text.computeSize(textExtent.x, textExtent.y);
		int width = pt.x + BUTTON_WIDTH + 10;
		int height = pt.y;
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width, height);
	}

	void down() {
		setSelection(getSelection() - 1);
		notifyListeners(SWT.Selection, new Event());
	}

	public int getMaximum() {
		return maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public int getSelection() {
		return Integer.parseInt(text.getText());
	}

	/**
	 * @return
	 */
	public boolean isRoll() {
		return roll;
	}

	void resize() {
		final Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		final int textWidth = pt.x - BUTTON_WIDTH;
		final int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH, buttonHeight);
	}

	public void setMaximum(final int maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();
	}

	public void setMinimum(final int minimum) {
		this.minimum = minimum;
	}

	/**
	 * @param b
	 */
	public void setRoll(final boolean b) {
		roll = b;
	}

	public void setSelection(int selection) {
		if (selection < minimum) {
			if (roll) {
				selection = maximum;
			} else {
				selection = minimum;
			}
		} else if (selection > maximum) {
			if (roll) {
				selection = minimum;
			} else {
				selection = maximum;
			}
		}

		text.setText(String.valueOf(selection));
	}

	void up() {
		setSelection(getSelection() + 1);
		notifyListeners(SWT.Selection, new Event());
	}

}