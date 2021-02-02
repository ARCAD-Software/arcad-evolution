package com.arcadsoftware.ae.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author MD
 */
public class Utils {
	protected static final String ARCAD_HOME = "ARCAD_HOME";
	private static final Class<?>[] parameters = new Class[] { URL.class };

	public static void addFile(final File f) throws IOException {
		addURL(f.toURI().toURL());
	}

	public static void addFile(final String s) throws IOException {
		final File f = new File(s);
		addFile(f);
	}

	public static void addURL(final URL u) throws IOException {
		final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			final Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, u);
		} catch (final Exception t) {
			throw new IOException("Error, could not add URL to system classloader");
		}
	}

	public static synchronized String computeId() {
		final SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return fd.format(new Date());
	}

	/**
	 * Méthode permettant de créer un tableau de File associé é chaque fichier contenu dans un répertoire spécifique
	 * passé en paramétre.
	 *
	 * @param libFolder
	 *            : File : Répertoire de base
	 * @param recursive
	 *            : Analyse récursive
	 * @return Tableau des URL
	 */
	public static File[] getFiles(final File libFolder, final boolean recursive) {
		if (libFolder.exists()) {
			final File[] list = libFolder.listFiles();
			final List<File> l = new ArrayList<>();
			for (final File element : list) {
				if (element.isDirectory() && recursive) {
					Collections.addAll(l, getFiles(element, recursive));
				} else {
					l.add(element);
				}
			}
			final File[] result = new File[l.size()];
			for (int i = 0; i < l.size(); i++) {
				result[i] = l.get(i);
			}
			return result;
		}
		return new File[0];
	}

	public static String getHomeDirectory() {
		String result = System.getProperty(ARCAD_HOME);

		if (isEmpty(result)) {
			result = System.getenv(ARCAD_HOME);
		}

		if (!isEmpty(result)) {
			System.setProperty(ARCAD_HOME, result);
		}

		return result;
	}

	public static String getTemporaryFolder() {
		final String folder = System.getProperty("java.io.tmpdir", "");
		if (folder.equals("")) {
			final String userhome = System.getProperty("user.home", "");
			if (!userhome.equals("")) {
				return userhome + File.pathSeparator + "tmp";
			} else {
				return null;
			}
		} else {
			return folder;
		}
	}

	/**
	 * Méthode permettant de créer un tableau d'URL associé é chaque fichier contenu dans un répertoire spécifique passé
	 * en paramétre.
	 *
	 * @param libFolder
	 *            : File : Répertoire de base
	 * @param recursive
	 *            : Analyse récursive
	 * @return Tableau des URL
	 * @throws MalformedURLException
	 */
	public static URL[] getURLs(final File libFolder, final boolean recursive) throws MalformedURLException {
		final File[] files = getFiles(libFolder, recursive);
		final List<URL> l = new ArrayList<>(files.length);
		for (final File file : files) {
			final URL u = file.toURI().toURL();
			l.add(u);
		}
		final URL[] result = new URL[l.size()];
		for (int i = 0; i < l.size(); i++) {
			result[i] = l.get(i);
		}
		return result;
	}

	/**
	 * Check that the given String is neither <code>null nor of length 0. Note: Will return <code>true for a String that
	 * purely consists of whitespace.
	 * <p>
	 * StringUtils.hasLength(null) = false StringUtils.hasLength("") = false StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 *
	 * @param str
	 *            the String to check (may be <code>null)
	 * @return <code>true if the String is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(final String str) {
		return str != null && str.length() > 0;
	}

	public static boolean isEmpty(final String string) {
		return string == null || string.equals("");
	}

	// --------------------------------------------------------------------------
	public static final String lpad(final String s, final int length) {
		return lpad(s, length, ' ');
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
		return rpad(s, length, ' ');
	}

	public static String quotedString(final String s) {
		if (s == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(s);
		final StringBuilder result = new StringBuilder();
		final int size = s.length();
		for (int i = 0; i < size; i++) {
			if (sb.charAt(i) == '\'') {
				result.append("''");//$NON-NLS-1$
			} else {
				result.append(sb.charAt(i));
			}
		}
		return result.toString();
	}

	public static String readTextFile(final String fileName) {
		final StringBuilder sb = new StringBuilder();
		try (final BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str).append('\n');
			}
		} catch (final IOException e) {
			// fail quietly
		}
		return sb.toString();
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

	// --------------------------------------------------------------------------
	public static final String rpad(final String s, final int length) {
		return rpad(s, length, ' ');
	}

	// --------------------------------------------------------------------------
	public static final String rpad(final String s, final int length, final char paddedChar) {
		final StringBuilder sb = new StringBuilder(s);
		if (s.length() < length) {
			while (sb.length() < length) {
				sb.append(paddedChar);
			}
			return sb.toString();
		}
		return sb.toString().substring(0, length);
	}

	public static void setUrls(final String folder) throws IOException {
		// Récupération des URL représentant les fichiers stockés dans
		// le répertoire des URL
		final URL[] urls = Utils.getURLs(new File(folder), true);
		for (final URL url : urls) {
			Utils.addURL(url);
		}
	}

	public static void sleepForAWhile(final long millisecond) {
		try {
			Thread.sleep(millisecond);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static String stackTrace(final Throwable e) {
		final StringBuilder sb = new StringBuilder();
		sb.append(e.getMessage()).append('\n');
		final StackTraceElement[] ste = e.getStackTrace();
		for (final StackTraceElement st : ste) {
			sb.append(st.toString()).append('\n');
		}
		return sb.toString();
	}

	// --------------------------------------------------------------------------
	public static String substitute(final String s,
			final String oldPattern,
			final String newPattern) {
		return substitute(s, oldPattern, newPattern, -1);
	}

	// --------------------------------------------------------------------------
	public static String substitute(final String s,
			final String oldPattern,
			final String newPattern,
			final int nbocc) {
		String s1 = s;
		int i = 0;
		int st = 0;
		int start = 0;
		int pos = s1.indexOf(oldPattern, start);
		boolean follow = true;
		while (pos != -1 && follow) {
			i++;
			start = pos + newPattern.length();
			final StringBuilder sb = new StringBuilder(s1.substring(0, pos));
			sb.append(newPattern);
			st = pos + oldPattern.length();
			sb.append(s1.substring(st, s1.length()));
			s1 = sb.toString();
			pos = s1.indexOf(oldPattern, start); // Ajout...
			if (nbocc != -1) {
				follow = i < nbocc;
			}
		}
		return s1;
	}

	public static String substituteProperty(String value, final String propertyName) {
		if (System.getProperty(propertyName) != null) {
			final StringBuilder toReplace = new StringBuilder();
			toReplace.append("${").append(propertyName).append('}');
			value = Utils.substitute(value, toReplace.toString(), System.getProperty(propertyName), -1);
		}
		return value;
	}

	public static String[] toSubstitute(final String value) {
		final List<String> l = new ArrayList<>();
		// Recherche d'une propriété
		final String s = value;
		final int start = s.indexOf("${", 0);
		if (start > -1) {
			final int end = s.indexOf('}', start + 2);
			if (end > -1) {
				final String sub = s.substring(start + 2, end);
				l.add(sub);
			}
		}
		final String[] result = new String[l.size()];
		for (int i = 0; i < l.size(); i++) {
			result[i] = l.get(i);
		}
		return result;
	}

	/**
	 * Trim all occurences of the supplied leading character from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @param leadingCharacter
	 *            the leading character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimLeadingCharacter(final String str, final char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		final StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied trailing character from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @param trailingCharacter
	 *            the trailing character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimTrailingCharacter(final String str, final char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		final StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static void writeToFile(final String filename, final String data, final boolean append) throws IOException {
		final File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try (FileOutputStream file = new FileOutputStream(filename, append)) {
			file.write(data.getBytes());
		}
	}

	private Utils() {

	}
}
