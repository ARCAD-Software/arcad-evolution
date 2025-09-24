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
package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.IOException;

public class FilePathTools {
	public static final String ZIPEXT = ".zar"; //$NON-NLS-1$
	// --------------------------------------------------------------------------

	public static final String extractFirstPart(final String path, final char separator) {
		String result;
		int pos = 0;
		if (!path.equals("")) { //$NON-NLS-1$
			if (path.length() > 0) {
				pos = path.lastIndexOf(Character.toString(separator));
				if (pos != -1) {
					result = path.substring(0, pos);
				} else {
					result = path;
				}
				if (result.charAt(result.length() - 1) == separator) {
					return result.substring(0, result.length() - 1);
				} else {
					return result;
				}

			} else {
				return ""; //$NON-NLS-1$
			}
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	// --------------------------------------------------------------------------
	public static final String extractLastPart(final String path, final char separator) {
		int pos = 0;
		if (path.length() > 0) {
			pos = path.lastIndexOf(Character.toString(separator));
			if (pos != -1) {
				return path.substring(pos + 1, path.length());
			} else {
				return path;
			}
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public static String findFile(final String fileToSearch, final String parentDirectory) {
		try {
			File f = new File(fileToSearch);
			if (f.isAbsolute()) {
				return f.exists() ? f.getCanonicalPath() : null;
			} else {
				f = new File(parentDirectory, fileToSearch);
				if (f.exists()) {
					return f.getCanonicalPath();
				} else {
					final File dir = new File(parentDirectory);
					final File[] dirs = dir.listFiles(File::isDirectory);

					for (final File dir2 : dirs) {
						final String result = findFile(fileToSearch, dir2.getCanonicalPath());
						if (result != null) {
							return result;
						}
					}
				}
			}
		} catch (final IOException e) {
			return null;
		}
		return null;
	}

	public static String findFileInPathes(final String[] pathes, final String fileToSearch) {
		for (final String path : pathes) {
			final String resultFile = findFile(fileToSearch, path);
			if (resultFile != null) {
				return resultFile;
			}
		}
		return null;
	}

	public static String getExtension(final String fileName) {
		final int pos = fileName.lastIndexOf('.'); //$NON-NLS-1$
		if (pos > -1) {
			return fileName.substring(pos);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public static String getShortExtension(final String fileName) {
		final int pos = fileName.lastIndexOf('.'); //$NON-NLS-1$
		if (pos > -1 && pos + 1 < fileName.length()) {
			return fileName.substring(pos + 1);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	// --------------------------------------------------------------------------
	public static final boolean isSubFile(final String fileName, final String path) {
		return fileName.toLowerCase().indexOf(path.toLowerCase()) == 0;
	}

	/**
	 * Si "pathToSubstract" est égal à "path", on retourne une chaine vide. Si "pathToSubstract" et "path" sont
	 * différents alors : - Si pathToSubstract ne se finit pas par "separator", on lui concatène "separator" - Si "path"
	 * commence par "pathToSubstract", on renvoit la partie différente entre "path" et "pathToSubstract" - sinon on
	 * renvoit "path"
	 *
	 * @param path
	 * @param pathToSubstract
	 * @param separator
	 * @return
	 */
	public static final String substractFilePath(final String path,
			String pathToSubstract,
			final char separator) {
		if (!pathToSubstract.equals("") && !path.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (pathToSubstract.equalsIgnoreCase(path)) {
				return ""; //$NON-NLS-1$
			} else {
				if (pathToSubstract.charAt(pathToSubstract.length() - 1) != separator) {
					pathToSubstract = pathToSubstract + separator;
				}

				if (path.toLowerCase().indexOf(pathToSubstract.toLowerCase()) == 0) {
					return path.substring(pathToSubstract.length(),
							path.length());
				} else {
					return path;
				}
			}
		} else {
			return path;
		}
	}

	private FilePathTools() {
		
	}

}
