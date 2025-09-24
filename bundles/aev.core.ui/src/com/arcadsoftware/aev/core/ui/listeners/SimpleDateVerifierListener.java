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
/*
 * Created on 13 avr. 2006
 */
package com.arcadsoftware.aev.core.ui.listeners;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author md
 */
public class SimpleDateVerifierListener implements Listener {

	final Calendar calendar = Calendar.getInstance();
	int dStartPos = -1;
	String format = null;
	boolean ignore;
	int mStartPos = -1;

	ArrayList<String> sepList = new ArrayList<>();
	Text text;
	boolean valid = false;
	int yLength = 4;
	int yStartPos = -1;

	public SimpleDateVerifierListener(final Text text, final String format) {
		super();
		this.text = text;
		this.format = format;
		valid = analyseFormat(format);
	}

	private boolean analyseFormat(final String analyseFormat) {
		if (analyseFormat == null) {
			return false;
		}
		int pos = analyseFormat.indexOf("yyyy"); //$NON-NLS-1$
		if (pos > -1) {
			yStartPos = pos;
		}

		if (yStartPos == -1) {
			pos = analyseFormat.indexOf("yy"); //$NON-NLS-1$
			if (pos > -1) {
				yStartPos = pos;
				yLength = 2;
			}
		}

		pos = analyseFormat.indexOf("MM"); //$NON-NLS-1$
		if (pos > -1) {
			mStartPos = pos;
		}
		pos = analyseFormat.indexOf("dd"); //$NON-NLS-1$
		if (pos > -1) {
			dStartPos = pos;
		}

		for (int i = 0; i < analyseFormat.length(); i++) {
			if (analyseFormat.charAt(i) == '/') {
				sepList.add(String.valueOf(i));
			}
		}

		return yStartPos != -1 && mStartPos != -1 && dStartPos != -1;
	}

	private boolean checkDate(final StringBuilder date) {
		calendar.set(Calendar.YEAR, 1901);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		final String yyyy = getCenturyPart(date);
		if (yyyy.indexOf('y') == -1) {
			final int year = Integer.parseInt(yyyy);
			calendar.set(Calendar.YEAR, year);
		}
		final String mm = getMonthPart(date);
		if (mm.indexOf('M') == -1) {
			final int month = Integer.parseInt(mm) - 1;
			final int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
			if (0 > month || month > maxMonth) {
				return false;
			}
			calendar.set(Calendar.MONTH, month);
		}
		final String dd = getDayPart(date);
		if (dd.indexOf('d') == -1) {
			final int day = Integer.parseInt(dd);
			final int maxDay = calendar.getActualMaximum(Calendar.DATE);
			if (1 > day || day > maxDay) {
				return false;
			}
			calendar.set(Calendar.DATE, day);
		} else {
			if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
				final char firstChar = date.charAt(8);
				if (firstChar != 'd' && '2' < firstChar) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkInput(final Event e) {
		final StringBuilder buffer = new StringBuilder(e.text);
		final char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		final int start = e.start;
		if (start > format.length() - 1) {
			return false;
		}
		int index = 0;
		for (final char c : chars) {
			if (isSeparatorPosition(start + index)) {
				if (c == '/') {
					index++;
					continue;
				}
				buffer.insert(index++, '/');
			}
			if (c < '0' || '9' < c) {
				return false;
			}
			if (start + index == getMonthFirstDigitPosition() && '1' < c) {
				return false; /* [M]M */
			}
			if (start + index == getDayFirstDigitPosition() && '3' < c) {
				return false; /* [D]D */
			}
			index++;
		}
		return true;
	}

	private String getCenturyPart(final StringBuilder value) {
		return value.substring(yStartPos, yStartPos + yLength);
	}

	private int getDayFirstDigitPosition() {
		return dStartPos;
	}

	private String getDayPart(final StringBuilder value) {
		return value.substring(dStartPos, dStartPos + 2);
	}

	private int getMonthFirstDigitPosition() {
		return mStartPos;
	}

	private String getMonthPart(final StringBuilder value) {
		return value.substring(mStartPos, mStartPos + 2);
	}

	@Override
	public void handleEvent(final Event e) {
		if (!valid) {
			return;
		}
		if (ignore) {
			return;
		}
		e.doit = false;
		final StringBuilder buffer = new StringBuilder(e.text);
		final char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		// Gestion du caract�re d'effacement
		if (processBackspace(e)) {
			return;
		}
		// Controle de saisie (num�rique, domaine, etc...)
		if (!checkInput(e)) {
			return;
		}
		// Cr�ation d'un texte de sumulation pour controle de validit�
		final String newText = buffer.toString();
		final int length = newText.length();
		final StringBuilder date = new StringBuilder(text.getText());
		date.replace(e.start, e.start + length, newText);
		// Controle de validit� de la date
		if (!checkDate(date)) {
			return;
		}
		// Si tous les controle sont OK, on int�gre la modification
		// dans le texte du controle
		text.setSelection(e.start, e.start + length);
		ignore = true;
		text.insert(newText);
		ignore = false;

	}

	private boolean isSeparatorPosition(final int position) {
		return sepList.indexOf(String.valueOf(position)) != -1;
	}

	private boolean processBackspace(final Event e) {
		final StringBuilder buffer = new StringBuilder(e.text);
		final char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);

		if (e.character == '\b') {
			for (int i = e.start; i < e.end; i++) {
				if (i < format.length() && i > -1) {
					buffer.append(format.charAt(i));
				} else {
					return true;
				}
			}
			text.setSelection(e.start, e.start + buffer.length());
			ignore = true;
			text.insert(buffer.toString());
			ignore = false;
			text.setSelection(e.start, e.start);
			return true;
		}
		return false;
	}

}
