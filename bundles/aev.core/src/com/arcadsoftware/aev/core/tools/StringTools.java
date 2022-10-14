/*
 * Cr�� le 26 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.google.re2j.Pattern;

public class StringTools {

	public static final FastDateFormat arcadSimpleDateFormat = FastDateFormat.getInstance("yyyyMMdd"); //$NON-NLS-1$
	private static final char DATESEP = '/';
	public static final String EMPTY = ""; //$NON-NLS-1$
	private static final char TIMESEP = ':';
	private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[A-Z]{1}[A-Z_0-9]*");

	/**
	 * Transforme une date Arcad en Calendar Java. <br>
	 *
	 * @param arcadDate
	 *            la chaine de caract�re repr�sentant la date
	 * @return
	 */
	public static Calendar arcadDateToCalendar(final String arcadDate) {
		final Calendar result = new GregorianCalendar();
		try {
			result.setTime(arcadSimpleDateFormat.parse(arcadDate));
		} catch (final ParseException e) {
			return new GregorianCalendar();
		}
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result;
	}

	/**
	 * Transforme un Calendar Java en date Arcad. <br>
	 * Ignore les heure, minutes et secondes.
	 *
	 * @param calendar
	 *            la date � transformer
	 * @return
	 */
	public static String calendarToArcadDate(final Calendar calendar) {
		return arcadSimpleDateFormat.format(calendar.getTime());
	}

	// --------------------------------------------------------------------------
	/*
	 * Transforme un caract�re en une chaine cod�e unicode de 4 car XXYY
	 */
	public static String charToUnicodeCode(final char c) {
		final StringBuilder sb = new StringBuilder(Integer.toHexString(c));
		while (sb.length() < 4) {
			sb.insert(0, "0"); //$NON-NLS-1$
		}
		return sb.toString().toUpperCase();
	}

	public static String completion(final String s, final char c, final int size, final boolean left) {
		final StringBuilder sb = new StringBuilder(""); //$NON-NLS-1$
		if (s.length() >= size) {
			sb.append(s);
		} else {
			if (left) {
				sb.append(replicate(c, size - s.length())).append(s);
			} else {
				sb.append(s).append(replicate(c, size - s.length()));
			}
		}
		return sb.toString();
	}

	public static boolean containsAny(final String aString, final CharSequence charSequence) {
		return StringUtils.containsAny(aString, charSequence);
	}

	// --------------------------------------------------------------------------
	public static String deleteCharToString(final String s, final char c) {
		final StringBuilder sb = deleteCharToStringBuilder(s, c);
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	public static StringBuilder deleteCharToStringBuilder(final String s, final char c) {
		final List<Integer> vec = new ArrayList<>();
		final StringBuilder sb = new StringBuilder(s);
		int size = s.length();
		for (int i = 0; i < size; i++) {
			if (sb.charAt(i) == c) {
				vec.add(i);
			}
		}
		if (!vec.isEmpty()) {
			size = vec.size() - 1;
			for (int i = size; i >= 0; i--) {
				sb.deleteCharAt(vec.get(i));
			}
		}
		return sb;
	}

	// --------------------------------------------------------------------------
	public static List<String> explode(final String s, final int size) {
		final List<String> list = new ArrayList<>();
		// Suppression des caractere Newline
		String token;
		String value;
		String keep;
		final StringBuilder sb = deleteCharToStringBuilder(s, '\n');
		final StringTokenizer st = new StringTokenizer(sb.toString(), "\r", false);//$NON-NLS-1$
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			do {
				if (token.length() > size) {
					value = token.substring(1, size);
					keep = token.substring(size + 1, token.length());
				} else {
					value = token;
					keep = "";//$NON-NLS-1$
				}
				list.add(value);
			} while (!keep.equals(""));//$NON-NLS-1$
		}
		return list;

	}

	// --------------------------------------------------------------------------
	public static String formatChronoDate(final String chrono) {
		final StringBuilder sb = new StringBuilder();
		if (chrono.length() == 8) {
			sb.append(chrono.substring(7, 8)).append(DATESEP);
			sb.append(chrono.substring(5, 6)).append(DATESEP);
			sb.append(chrono.substring(1, 4)).append(DATESEP);
		} else {
			sb.append("00")//$NON-NLS-1$
					.append(DATESEP)
					.append("00")//$NON-NLS-1$
					.append(DATESEP)
					.append("0000"); //$NON-NLS-1$
		}
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	public static String formatTime(final String chrono) {
		final StringBuilder sb = new StringBuilder();
		if (chrono.length() == 6) {
			sb.append(chrono.substring(1, 2)).append(TIMESEP);
			sb.append(chrono.substring(3, 4)).append(TIMESEP);
			sb.append(chrono.substring(5, 6)).append(TIMESEP);
		} else {
			sb.append("00")//$NON-NLS-1$
					.append(TIMESEP)
					.append("00")//$NON-NLS-1$
					.append(TIMESEP)
					.append("00"); //$NON-NLS-1$
		}
		return sb.toString();
	}

	public static String[] intToStringArray(final Integer[] ints) {
		final String[] strings = new String[ints.length];
		for (int i = 0; i < ints.length; i++) {
			strings[i] = String.valueOf(ints[i]);
		}

		return strings;
	}

	public static String[] intToStringArray(final List<Integer> ints) {
		return intToStringArray(ints.toArray(new Integer[ints.size()]));
	}

	public static boolean isBlank(final CharSequence arg0) {
		return StringUtils.isBlank(arg0);
	}

	public static boolean isBlank(final String s) {
		return StringUtils.isBlank(s);
	}

	public static boolean isEmpty(final String s) {
		return StringUtils.isEmpty(s);
	}

	/**
	 * M�thode qui permet de savoir si le nom est valide. Le nom doit etre en majuscule, commenc� par une lettre et les
	 * autres caract�res autoris�s sont: [A-Z], [0-9] et '_'
	 *
	 * @param name
	 * @return <b>null</b> si le nom est valid un message d'erreur sinon
	 */
	public static String isNameValid(final String name) {
		return VALID_NAME_PATTERN.matches(name) ? null : CoreLabels.resString("Name.Notvalid.Message");
	}

	/**
	 * M�thode qui permet de savoir si une chaine de caracteres <param>num</param> est num�rique. Le test porte sur le
	 * parsing en tant que Double de <param>num</param> qui soul�ve une exception.
	 *
	 * @param num
	 * @return <b>true</b> si
	 */
	public static boolean isNumeric(final String num) {
		try {
			Double.parseDouble(num);
			return true;
		} catch (final NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * @return true if the value can be used as a pattern e.g. LIKE '%PATTERN%'
	 */
	public static boolean isPattern(final String value) {
		return containsAny(value, "%_");
	}

	// --------------------------------------------------------------------------
	public static final String lpad(final String s, final int length, final char paddedChar) {
		final StringBuilder sb = new StringBuilder(s);
		if (s.length() < length) {
			while (sb.length() < length) {
				sb.insert(0, paddedChar);
			}
			return sb.toString();
		}
		return sb.toString().substring(0, length);
	}

	// --------------------------------------------------------------------------
	public static final String pad(final String s, final int length) {
		final int sLength = s.length();
		if (sLength >= length) {
			return s;
		}

		final char[] padBuffer = new char[length];
		s.getChars(0, sLength, padBuffer, 0);
		Arrays.fill(padBuffer, sLength, length, ' ');
		return new String(padBuffer);
	}

	// --------------------------------------------------------------------------
	/**
	 * @param s
	 * @return The string <code>s</code> with every single quote ' replaced with two single quotes ''
	 */
	public static String quotedString(final String s) {
		if (s.indexOf('\'') < 0) {
			return s;
		}
		final int size = s.length();
		final StringBuilder result = new StringBuilder(size + 1);
		for (int i = 0; i < size; i++) {
			final char c = s.charAt(i);
			if (c == '\'') {
				result.append("''");//$NON-NLS-1$
			} else {
				result.append(c);
			}
		}

		return result.toString();
	}

	public static String replicate(final char c, final int size) {
		final StringBuilder s = replicateToStringBuilder(c, size);
		return s.toString();
	}

	public static StringBuilder replicateToStringBuilder(final char c, final int size) {
		final StringBuilder s = new StringBuilder("");//$NON-NLS-1$
		for (int i = 1; i <= size; i++) {
			s.append(c);
		}
		return s;
	}

	public static String reverse(final String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}

	public static final String[] split(final String s, final int size) {
		String value;
		final List<String> list = new ArrayList<>();
		final StringTokenizer st = new StringTokenizer(s, "\r\n", false); //$NON-NLS-1$
		while (st.hasMoreElements()) {
			String line = (String) st.nextElement();
			do {
				if (line.length() > size) {
					value = line.substring(0, size - 1);
					line = line.substring(size - 1);
				} else {
					value = line;
					line = ""; //$NON-NLS-1$
				}
				list.add(value);
			} while (!line.equals("")); //$NON-NLS-1$
		}
		final String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			a[i] = list.get(i);
		}
		return a;
	}

	public static void string2File(final File file, final String data) throws IOException {
		//<JIRA number="RDSKIPPER-180" version="13.1.10" date="May 12, 2021" type="Bug" user="ACL">
		// Not correctly closing BufferedWriter (only closing FileWriter) so written contents were blank
		try (FileWriter thisFile = new FileWriter(file)) {
			try (BufferedWriter bw = new BufferedWriter(thisFile)) {
				bw.write(data);
			}
		}
		//</JIRA>
	}

	public static int[] stringToIntArray(final List<String> strings) {
		return stringToIntArray(strings.toArray(new String[strings.size()]));
	}

	public static int[] stringToIntArray(final String[] strings) {
		final int[] ints = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			ints[i] = NumberUtils.toInt(strings[i]);
		}

		return ints;
	}

	// ----------------------------------------------------------------------------------------------------------------------
	public static String stringToUnicodeString(final String s, final char breakChar) {
		final String VALIDCHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //$NON-NLS-1$
		final StringBuilder res = new StringBuilder("");//$NON-NLS-1$
		String search;
		for (int i = 0; i < s.length(); i++) {
			search = Character.toString(s.charAt(i));
			if (VALIDCHAR.indexOf(search) != -1) {
				res.append(s.charAt(i));
			} else {
				res.append(breakChar).append(charToUnicodeCode(s.charAt(i)));
			}
		}
		return res.toString();
	}

	// <MR number="2019/00069" version="11.00.04" date="Feb 27, 2019" type="Bug" user="ACL">
	public static String stripEnd(final String str, final String stripChars) {
		return StringUtils.stripEnd(str, stripChars);
	}
	// </MR>

	// --------------------------------------------------------------------------
	public static String substitute(final String s,
			final String oldPattern,
			final String newPattern) {
		if (s != null) {
			// <FM number="2010/00134" version="08.09.02" date="25 mars 2010 user="md">
			if (oldPattern == null || newPattern == null || oldPattern.equalsIgnoreCase("")) {
				return s;
			}
			// </FM>
			String s1 = s;
			int st = 0;
			int start = 0;
			int pos = s1.indexOf(oldPattern, start);
			while (pos != -1) {
				start = pos + newPattern.length();
				final StringBuilder sb = new StringBuilder(s1.substring(0, pos));
				sb.append(newPattern);
				st = pos + oldPattern.length();
				sb.append(s1.substring(st, s1.length()));
				s1 = sb.toString();
				pos = s1.indexOf(oldPattern, start); // Ajout...
			}
			return s1;
		}
		return s;
	}

	// --------------------------------------------------------------------------
	public static String substitute(final String s,
			final String oldPattern,
			final String newPattern,
			final int nbocc) {
		String s1 = s;
		int i = 0;
		int /* ln=0, */ st = 0;
		int start = 0;
		int pos = s1.indexOf(oldPattern, start);
		while (pos != -1 && i < nbocc) {
			i++;
			start = pos + newPattern.length();
			final StringBuilder sb = new StringBuilder(s1.substring(0, pos));
			sb.append(newPattern);
			st = pos + oldPattern.length();
			sb.append(s1.substring(st, s1.length()));
			s1 = sb.toString();

			pos = s1.indexOf(oldPattern, start); // Ajout...
		}
		return s1;
	}

	/**
	 * @return the trimmed, upper-case version of
	 *         <code>name</name> if non-null and unquoted, otherwise <code>name</code>
	 */
	public static String toIbmiName(final String name) {
		if (name == null || name.length() > 2 && name.startsWith("\"") && name.endsWith("\"")) {
			return name;
		}
		return name.trim().toUpperCase();
	}

	private StringTools() {

	}
}
