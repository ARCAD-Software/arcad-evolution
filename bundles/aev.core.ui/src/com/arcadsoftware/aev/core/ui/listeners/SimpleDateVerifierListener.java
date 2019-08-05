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

	boolean ignore;
	int yStartPos = -1;
	int yLength = 4;
	int mStartPos = -1;
	int dStartPos = -1;

	Text text;
	String format = null;
	ArrayList<String> sepList = new ArrayList<String>();
	boolean valid = false;
	final Calendar calendar = Calendar.getInstance();

	private int getMonthFirstDigitPosition() {
		return mStartPos;
	}

	private int getDayFirstDigitPosition() {
		return dStartPos;
	}

	private boolean isSeparatorPosition(int position) {
		return (sepList.indexOf(String.valueOf(position)) != -1);
	}

	public SimpleDateVerifierListener(Text text, String format) {
		super();
		this.text = text;
		this.format = format;
		valid = analyseFormat(format);
	}

	private String getCenturyPart(StringBuffer value) {
		return value.substring(yStartPos, yStartPos + yLength);
	}

	private String getMonthPart(StringBuffer value) {
		return value.substring(mStartPos, mStartPos + 2);
	}

	private String getDayPart(StringBuffer value) {
		return value.substring(dStartPos, dStartPos + 2);
	}

	private boolean analyseFormat(String analyseFormat) {
		if (analyseFormat == null)
			return false;
		int pos = analyseFormat.indexOf("yyyy"); //$NON-NLS-1$
		if (pos > -1)
			yStartPos = pos;

		if (yStartPos == -1) {
			pos = analyseFormat.indexOf("yy"); //$NON-NLS-1$
			if (pos > -1) {
				yStartPos = pos;
				yLength = 2;
			}
		}

		pos = analyseFormat.indexOf("MM"); //$NON-NLS-1$
		if (pos > -1)
			mStartPos = pos;
		pos = analyseFormat.indexOf("dd"); //$NON-NLS-1$
		if (pos > -1)
			dStartPos = pos;

		for (int i = 0; i < analyseFormat.length(); i++) {
			if (analyseFormat.charAt(i) == '/') {
				sepList.add(String.valueOf(i));
			}
		}

		return (yStartPos != -1) && (mStartPos != -1) && (dStartPos != -1);
	}

	private boolean processBackspace(Event e) {
		StringBuffer buffer = new StringBuffer(e.text);
		char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);

		if (e.character == '\b') {
			for (int i = e.start; i < e.end; i++) {
				if ((i < format.length()) && (i > -1)) {
					buffer.append(format.charAt(i));
				} else
					return true;
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

	private boolean checkInput(Event e) {
		StringBuffer buffer = new StringBuffer(e.text);
		char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		int start = e.start;
		if (start > format.length() - 1)
			return false;
		int index = 0;
		for (int i = 0; i < chars.length; i++) {
			if (isSeparatorPosition(start + index)) {
				if (chars[i] == '/') {
					index++;
					continue;
				}
				buffer.insert(index++, '/');
			}
			if (chars[i] < '0' || '9' < chars[i])
				return false;
			if (start + index == getMonthFirstDigitPosition() && '1' < chars[i])
				return false; /* [M]M */
			if (start + index == getDayFirstDigitPosition() && '3' < chars[i])
				return false; /* [D]D */
			index++;
		}
		return true;
	}

	private boolean checkDate(StringBuffer date) {
		calendar.set(Calendar.YEAR, 1901);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		String yyyy = getCenturyPart(date);
		if (yyyy.indexOf('y') == -1) {
			int year = Integer.parseInt(yyyy);
			calendar.set(Calendar.YEAR, year);
		}
		String mm = getMonthPart(date);
		if (mm.indexOf('M') == -1) {
			int month = Integer.parseInt(mm) - 1;
			int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
			if (0 > month || month > maxMonth)
				return false;
			calendar.set(Calendar.MONTH, month);
		}
		String dd = getDayPart(date);
		if (dd.indexOf('d') == -1) {
			int day = Integer.parseInt(dd);
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			if (1 > day || day > maxDay)
				return false;
			calendar.set(Calendar.DATE, day);
		} else {
			if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
				char firstChar = date.charAt(8);
				if (firstChar != 'd' && '2' < firstChar)
					return false;
			}
		}
		return true;
	}

	public void handleEvent(Event e) {
		if (!valid)
			return;
		if (ignore)
			return;
		e.doit = false;
		StringBuffer buffer = new StringBuffer(e.text);
		char[] chars = new char[buffer.length()];
		buffer.getChars(0, chars.length, chars, 0);
		// Gestion du caractère d'effacement
		if (processBackspace(e))
			return;
		// Controle de saisie (numérique, domaine, etc...)
		if (!checkInput(e))
			return;
		// Création d'un texte de sumulation pour controle de validité
		String newText = buffer.toString();
		int length = newText.length();
		StringBuffer date = new StringBuffer(text.getText());
		date.replace(e.start, e.start + length, newText);
		// Controle de validité de la date
		if (!checkDate(date))
			return;
		// Si tous les controle sont OK, on intégre la modification
		// dans le texte du controle
		text.setSelection(e.start, e.start + length);
		ignore = true;
		text.insert(newText);
		ignore = false;

	}

}
