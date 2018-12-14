/*
 * Créé le 26 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.tools;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class StringTools {

	public static SimpleDateFormat arcadSimpleDateFormat = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$

	private static final int _LANG = 0;
	private static final char _DATESEP = '/';
	private static final char _TIMESEP = ':';
	public static final String EMPTY = ""; //$NON-NLS-1$

	/**
	 * 
	 */
	public StringTools() {
		super();
	}

	public static String getStackTrace(Throwable e) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(out, true));
		try {
			out.flush();
			String s = out.toString();
			out.close();
			return s;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static final String[] split(String s, int size) {
		// Découpage selon #13#10}
		// - On supprime les #10}
		String value;
		ArrayList list = new ArrayList();
		StringTokenizer st = new StringTokenizer(s, "\r\n", false); //$NON-NLS-1$
		while (st.hasMoreElements()) {
			String line = (String) st.nextElement();
			do {
				if (line.length() > size) {
					value = line.substring(0, size - 1);
					line = line.substring(size);
				} else {
					value = line;
					line = StringTools.EMPTY;
				}
				list.add(value);
			} while (!line.equals(StringTools.EMPTY));
		}
		String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			a[i] = (String) list.get(i);
		}
		return a;
	}

	// --------------------------------------------------------------------------
	public static final String pad(String s, int length) {
		StringBuffer sb = new StringBuffer(s);
		if (s.length() < length) {
			int l = length - s.length();
			for (int i = 0; i < l; i++) {
				sb.append(' ');//$NON-NLS-1$ 
			}
		}
		return sb.toString();

	}

	// --------------------------------------------------------------------------
	public static final String lpad(String s, int length, char paddedChar) {
		StringBuffer sb = new StringBuffer(s);
		if (s.length() < length) {
			while (sb.length() < length)
				sb.insert(0, paddedChar);
			return sb.toString();
		}
		return sb.toString().substring(0, length);
	}

	// --------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public static StringBuffer deleteCharToStringBuffer(String s, char c) {
		Vector vec = new Vector();
		StringBuffer sb = new StringBuffer(s);
		int size = s.length();
		for (int i = 0; i < size; i++) {
			if (sb.charAt(i) == c) {
				vec.add(new Integer(i));
			}
		}
		if (vec.size() > 0) {
			size = vec.size() - 1;
			for (int i = size; i >= 0; i--) {
				sb.deleteCharAt(((Integer) vec.get(i)).intValue());
			}
		}
		return sb;
	}

	// --------------------------------------------------------------------------
	public static String deleteCharToString(String s, char c) {
		StringBuffer sb = deleteCharToStringBuffer(s, c);
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public static ArrayList explode(String s, int size) {
		ArrayList list = new ArrayList();
		// Suppression des caractere Newline
		String token, value, keep;
		StringBuffer sb = deleteCharToStringBuffer(s, '\n');
		StringTokenizer st = new StringTokenizer(sb.toString(), "\r", false);//$NON-NLS-1$ 
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			do {
				if (token.length() > size) {
					value = token.substring(1, size);
					keep = token.substring(size + 1, token.length());
				} else {
					value = token;
					keep = StringTools.EMPTY;
				}
				list.add(value);
			} while (!(keep.equals(StringTools.EMPTY)));
		}
		return list;

	}

	public static boolean isEmpty(String s) {
		if (s == null || s.equals(StringTools.EMPTY))
			return true;
		return false;
	}

	// --------------------------------------------------------------------------
	public static String quotedString(String s) {
		StringBuffer sb = new StringBuffer(s);
		StringBuffer result = new StringBuffer();
		int size = s.length();
		for (int i = 0; i < size; i++) {
			if (sb.charAt(i) == '\'') {
				result.append("''");//$NON-NLS-1$ 
			} else {
				result.append(sb.charAt(i));
			}
		}
		return result.toString();
	}

	// --------------------------------------------------------------------------
	public static String formatChronoDate(String chrono) {
		StringBuffer sb = new StringBuffer();
		if (chrono.length() == 8) {
			if (_LANG == 0) {
				sb.append(chrono.substring(7, 8)).append(_DATESEP);
				sb.append(chrono.substring(5, 6)).append(_DATESEP);
				sb.append(chrono.substring(1, 4)).append(_DATESEP);
			} else {
				sb.append(chrono.substring(5, 6)).append(_DATESEP);
				sb.append(chrono.substring(7, 8)).append(_DATESEP);
				sb.append(chrono.substring(1, 4)).append(_DATESEP);
			}
		} else {
			sb.append("00")//$NON-NLS-1$ 
					.append(_DATESEP).append("00")//$NON-NLS-1$ 
					.append(_DATESEP).append("0000"); //$NON-NLS-1$ 
		}
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	public static String formatTime(String chrono) {
		StringBuffer sb = new StringBuffer();
		if (chrono.length() == 6) {
			sb.append(chrono.substring(1, 2)).append(_TIMESEP);
			sb.append(chrono.substring(3, 4)).append(_TIMESEP);
			sb.append(chrono.substring(5, 6)).append(_TIMESEP);
		} else {
			sb.append("00")//$NON-NLS-1$ 
					.append(_TIMESEP).append("00")//$NON-NLS-1$ 
					.append(_TIMESEP).append("00"); //$NON-NLS-1$ 
		}
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	/*
	 * Transforme un caractére en une chaine codée unicode de 4 car XXYY
	 */
	public static String charToUnicodeCode(char c) {
		StringBuffer sb = new StringBuffer(Integer.toHexString(c));
		while (sb.length() < 4) {
			sb.insert(0, "0");} //$NON-NLS-1$  		  		
		return sb.toString().toUpperCase();
	}

	// ----------------------------------------------------------------------------------------------------------------------
	public static String stringToUnicodeString(String s, char breakChar) {
		final String VALIDCHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //$NON-NLS-1$  				  	
		StringBuffer res = new StringBuffer(StringTools.EMPTY);
		String search;
		for (int i = 0; i < s.length(); i++) {
			search = new Character(s.charAt(i)).toString();
			if (VALIDCHAR.indexOf(search) != -1) {
				res.append(s.charAt(i));
			} else
				res.append(breakChar).append(charToUnicodeCode(s.charAt(i)));
		}
		return res.toString();
	}

	// --------------------------------------------------------------------------
	public static String substitute(String s, String oldPattern, String newPattern) {
		if (s != null) {
			String s1 = new String(s);
			int st = 0;
			int start = 0;
			int pos = s1.indexOf(oldPattern, start);
			while (pos != -1) {
				if (newPattern != null) {
					start = pos + newPattern.length();
					StringBuffer sb = new StringBuffer(s1.substring(0, pos));
					sb.append(newPattern);
					st = pos + oldPattern.length();
					sb.append(s1.substring(st, s1.length()));
					s1 = sb.toString();
					pos = s1.indexOf(oldPattern, start); // Ajout...
				}
			}
			return s1;
		}
		return s;
	}

	// --------------------------------------------------------------------------
	public static String substitute(String s, String oldPattern, String newPattern, int nbocc) {
		String s1 = new String(s);
		int i = 0;
		int /* ln=0, */st = 0;
		int start = 0;
		int pos = s1.indexOf(oldPattern, start);
		while ((pos != -1) && (i < nbocc)) {
			i++;
			// pos = s1.indexOf(oldPattern,start);
			// if (pos!=-1) {
			start = pos + newPattern.length();
			StringBuffer sb = new StringBuffer(s1.substring(0, pos));
			sb.append(newPattern);
			// ln = ((s1.length()-pos)-oldPattern.length());
			st = pos + oldPattern.length();
			sb.append(s1.substring(st, s1.length()));
			s1 = sb.toString();
			// }
			pos = s1.indexOf(oldPattern, start); // Ajout...
		}
		return s1;
	}

	public static StringBuffer replicateToStringBuffer(char c, int size) {
		StringBuffer s = new StringBuffer(StringTools.EMPTY);
		for (int i = 1; i <= size; i++) {
			s.append(c);
		}
		return s;
	}

	public static String replicate(char c, int size) {
		StringBuffer s = replicateToStringBuffer(c, size);
		return s.toString();
	}

	public static String reverse(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = s.length() - 1; i >= 0; i--)
			sb.append(s.charAt(i));
		return sb.toString();
	}

	public static String completion(String s, char c, int size, boolean left) {
		StringBuffer sb = new StringBuffer(StringTools.EMPTY);
		if (s.length() >= size)
			sb.append(s);
		else {
			if (left)
				sb.append(replicate(c, size - s.length())).append(s);
			else
				sb.append(s).append(replicate(c, size - s.length()));
		}
		return sb.toString();
	}

	/**
	 * Transforme une date Arcad en Calendar Java. <br>
	 * 
	 * @param arcadDate
	 *            la chaine de caractère représentant la date
	 * @return Calendar
	 */
	public static Calendar arcadDateToCalendar(String arcadDate) {
		Calendar result = new GregorianCalendar();
		try {
			result.setTime(arcadSimpleDateFormat.parse(arcadDate));
		} catch (ParseException e) {
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
	 *            la date à transformer
	 * @return String
	 */
	public static String CalendarToArcadDate(Calendar calendar) {
		return arcadSimpleDateFormat.format(calendar.getTime());
	}

	public static String getFormattedDate(String dateToFormat) {
		String entryFormat;
		if (dateToFormat.length() == 8)
			entryFormat = "yyyyMMdd"; //$NON-NLS-1$
		else
			entryFormat = "yyMMdd"; //$NON-NLS-1$

		SimpleDateFormat dfreader = new SimpleDateFormat(entryFormat);
		SimpleDateFormat dfwriter = new SimpleDateFormat(CoreLabels.resString("format.dateWriter")); //$NON-NLS-1$
		try {
			Date d = dfreader.parse(dateToFormat);
			return dfwriter.format(d);
		} catch (ParseException e) {
			return StringTools.EMPTY;
		}
	}

	public static String getFormattedTime(String timeToFormat) {
		SimpleDateFormat dfreader = new SimpleDateFormat("hhmmss");//$NON-NLS-1$
		SimpleDateFormat dfwriter = new SimpleDateFormat("hh:mm:ss");//$NON-NLS-1$
		try {
			Date d = dfreader.parse(timeToFormat);
			return dfwriter.format(d);
		} catch (ParseException e) {
			return StringTools.EMPTY;
		}
	}

	public static void string2File(File file, String data) {
		try {
			FileWriter thisFile = new FileWriter(file);
			BufferedWriter thisFileBuffer = new BufferedWriter(thisFile);

			thisFileBuffer.write(data);
			thisFileBuffer.close();
		} catch (Exception e) {
			// Do nothing
		}
	}

	/**
	 * Méthode qui permet de savoir si une chaine de caracteres
	 * <param>num</param> est numérique. Le test porte sur le parsing en tant
	 * que Double de <param>num</param> qui soulève une exception.
	 * 
	 * @param num
	 * @return <b>true</b> si
	 */
	public static boolean isNumeric(String num) {
		try {
			Double.parseDouble(num);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Méthode qui permet de savoir si le nom est valide. Le nom doit etre en
	 * majuscule, commencé par une lettre et les autres caractères autorisés
	 * sont: [A-Z], [0-9] et '_'
	 * 
	 * @param name
	 * @return <b>null</b> si le nom est valid un message d'erreur sinon
	 */
	public static String isNameValid(String name) {
		if (name.matches("[A-Z]{1}[A-Z_0-9]*")) //$NON-NLS-1$
			return null;
		return CoreLabels.resString("Name.Notvalid.Message"); //$NON-NLS-1$
	}
	
	public static boolean containsAny(String aString, CharSequence charSequence){
		return StringUtils.containsAny(aString, charSequence);
	}
	
	public static boolean isBlank(CharSequence arg0) {
		return StringUtils.isBlank(arg0);
	}
}
