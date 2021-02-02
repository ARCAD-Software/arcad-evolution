package com.arcadsoftware.mmk.anttasks.taskdefs.misc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;

public class ArcadReplaceTokenValueTask extends AbstractArcadAntTask {

	private static final String ANY_CHARACTERS_OR_BLANKS_STRING = "[\\s\\S]*?";
	private static final String ANY_CHARACTERS_STRING = "\\s*";
	private static final String APOSTROPHE = "\'";
	private static final String BLANK = " ";
	private static final int BUFFER_LENGTH = 4096;
	private static final String CARRIAGE_RETURN = "\r";
	private static final String CLOSING_TAG = ">";

	private static final String COMMA = ",";
	private static final String EQUAL = "=";
	private static final String FIRST = "*FIRST";
	private static final String LINE_BREAK = "\n";
	private static final String OPENING_TAG = "<";
	private static final String QUOTATION_MARK = "\"";
	private static final String REPLACE_ERROR = "Something goes wrong in the attempt of replacing the value! Replacement does not occur. Check coherence between file and declared token.";
	private static final String STAR_ALL = "*ALL";
	String all = "";
	private boolean allOccurs = false;
	File file = null;
	private String filePath = null;
	private boolean isXmlTagDelimited = false;

	String[] keys = null;
	private String occurs = null;
	private int[] occursRank = null;
	private boolean oneOccur = true;
	Scanner scanner = null;
	private String separator = "";
	private String token = null;
	String[] valuesToReplace = null;

	@Override
	public void doExecute() {
		processInit();
		processExecution();
		writeFileContent();
	}

	public File getFile() {
		return file;
	}

	public void getFileContent() {
		final StringBuilder b = new StringBuilder();
		scanner.useDelimiter(CARRIAGE_RETURN);
		boolean first = true;
		while (scanner.hasNext()) {
			if (!first) {
				b.append(CARRIAGE_RETURN);
			}
			b.append(scanner.next());
			first = false;
		}
		all = b.toString();
	}

	public String getFilePath() {
		return filePath;
	}

	public String getOccurs() {
		return occurs;
	}

	public String getSeparator() {
		return separator;
	}

	public String getToken() {
		return token;
	}

	public void initScanner() {
		scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (final FileNotFoundException e) {
			throw new BuildException(e);
		}
	}

	public void processExecution() {
		String tokenBefore = "";
		for (int i = 0; i < keys.length; i++) {
			if (!tokenBefore.equals(keys[i])) {
				if (scanner != null) {
					scanner.close();
				}
				initScanner();
			}
			scanFile(keys[i], valuesToReplace[i]);
			tokenBefore = keys[i];
		}
	}

	public void processInit() {
		final String[] occursArr = occurs.split(COMMA);
		occursRank = new int[occursArr.length];
		if (occursArr.length == 1 && occursArr[0].equals(STAR_ALL)) {
			oneOccur = false;
			allOccurs = true;
		} else if (occursArr.length == 1 && occursArr[0].equals(FIRST)) {
			oneOccur = true;
			allOccurs = false;
			occursRank[0] = 1;
		} else if (occursArr.length == 1) {
			oneOccur = true;
			allOccurs = false;
			occursRank[0] = Integer.parseInt(occursArr[0]);
		} else if (occursArr.length > 1) {
			oneOccur = false;
			allOccurs = false;
			for (int i = 0; i < occursArr.length; i++) {
				occursRank[i] = Integer.parseInt(occursArr[i]);
			}
		}

		keys = new String[1];
		valuesToReplace = new String[1];

		String key = "";
		String valueToReplace = "";
		if (token.indexOf(EQUAL) > -1) {
			key = token.substring(0, token.indexOf(EQUAL));
			valueToReplace = token.substring(token.indexOf(EQUAL) + 1);
			separator = EQUAL;
		} else if (token.indexOf(CLOSING_TAG) > -1) {
			key = token.substring(0, token.indexOf(CLOSING_TAG));
			valueToReplace = token.substring(token.indexOf(CLOSING_TAG) + 1);
			separator = CLOSING_TAG;
			isXmlTagDelimited = true;
		} else {
			throw new BuildException(
					"Invalid token! the token must represent either an xml attribute or tag, either a propertie key. Must contain a separator '>' or '='.");
		}
		// Trim ahead and trailing blanks in the replacement value
		// Trim quotation marks or apostrophes in the replacement value
		int index = valueToReplace.indexOf(QUOTATION_MARK);
		if (index > -1) {
			valueToReplace = valueToReplace.substring(index + 1);
			index = valueToReplace.lastIndexOf(QUOTATION_MARK);
			if (index > -1) {
				valueToReplace = valueToReplace.substring(0, index);
			}
		} else {
			index = valueToReplace.indexOf(APOSTROPHE);
			if (index > -1) {
				valueToReplace = valueToReplace.substring(index + 1);
				index = valueToReplace.lastIndexOf(APOSTROPHE);
				if (index > -1) {
					valueToReplace = valueToReplace.substring(0, index);
				}
			}
		}
		keys[0] = key;
		valuesToReplace[0] = valueToReplace;

		initScanner();
		getFileContent();
	}

	public String replaceValue(final String key, final String valueToReplace) {
		scanner.reset();
		String oldValue = null;
		final StringBuilder b = new StringBuilder();
		b.append(ANY_CHARACTERS_STRING).append(key).append(ANY_CHARACTERS_OR_BLANKS_STRING);
		final String delimiter = b.toString();
		scanner.useDelimiter(delimiter);
		int idxOccurs = 0;
		int occur = 0;
		while (scanner.hasNext()) {
			occur++;
			String find = scanner.next();
			if (allOccurs || occursRank[idxOccurs] == occur) {
				// Search for equal operator
				int i = find.indexOf(EQUAL);
				if (i > -1) {
					find = find.substring(i + 1);
				} else {
					// Search for closing tag
					i = find.indexOf(CLOSING_TAG);
					if (i > -1) {
						find = find.substring(i + 1);
					}
				}
				final String work = find.trim();
				// Search for quotation mark before old value
				i = work.indexOf(QUOTATION_MARK);
				// Search for quotation mark after old value
				int j = work.indexOf(QUOTATION_MARK, i + 1);
				String keyAndValue = "";
				String keyAndValueToReplace = "";
				// There are '"' as delimiters for the value
				if (i == 0 && j > -1) {
					oldValue = work.substring(i + 1, j + 1);

					StringBuilder buf = new StringBuilder();

					buf.append(QUOTATION_MARK).append(oldValue).append(QUOTATION_MARK);
					keyAndValue = buf.toString();

					buf = new StringBuilder();
					buf.append(QUOTATION_MARK).append(valueToReplace).append(QUOTATION_MARK);
					keyAndValueToReplace = buf.toString();
					// Search for apostrophe delimiters
				} else {
					// Search for apostrophe before old value
					i = work.indexOf(APOSTROPHE);
					// Search for apostrophe after old value
					j = work.indexOf(APOSTROPHE, i + 1);
					if (i == 0 && j > -1) {
						oldValue = work.substring(i + 1, j + 1);
						StringBuilder buf = new StringBuilder();
						buf.append(APOSTROPHE).append(oldValue).append(APOSTROPHE);
						keyAndValue = buf.toString();
						buf = new StringBuilder();
						buf.append(APOSTROPHE).append(valueToReplace).append(APOSTROPHE);
						keyAndValueToReplace = buf.toString();
						// Search for blank or carriage return or line break delimiters
					} else {
						i = work.indexOf(BLANK);
						j = find.indexOf(OPENING_TAG);
						// If no closing tag, search for carriage return
						if (i < 0 && j < 0) {
							i = find.indexOf(CARRIAGE_RETURN);
						}
						// If no carriage return search for line break
						if (i < 0 && j < 0) {
							i = find.indexOf(LINE_BREAK);
						}
						if (isXmlTagDelimited && j > 0) {
							oldValue = find.substring(0, j);
						} else if (i > 0) {
							oldValue = work.substring(0, Math.min(i, work.length()));
						} else {
							throw new BuildException(REPLACE_ERROR);
						}
						keyAndValue = oldValue;
						keyAndValueToReplace = valueToReplace;
					}
				}
				int x = -1;
				if (!keyAndValue.trim().equals("")) {
					x = all.indexOf(keyAndValue);
				}
				if (x > -1) {
					final String before = all.substring(0, x);
					final String after = all.substring(x + keyAndValue.length());
					all = before + keyAndValueToReplace + after;
				} else {
					throw new BuildException(REPLACE_ERROR);
				}
				if (oneOccur) {
					break;
				}
				if (allOccurs) {
					continue;
				}
				if (idxOccurs + 1 < occursRank.length) {
					idxOccurs++;
				} else {
					break;
				}
			}
		}
		if (oldValue == null) {
			throw new BuildException(REPLACE_ERROR);
		}
		return oldValue;
	}

	public void scanFile(final String key, final String valueToReplace) {
		log(replaceValue(key, valueToReplace));
	}

	private void setFile(final File file) {
		this.file = file;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
		setFile(new File(filePath));
	}

	public void setOccurs(final String occurs) {
		this.occurs = occurs;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	@Override
	public void validateAttributes() {
		if (occurs == null || occurs.equals("")) {
			occurs = getProject().getProperty("token.occurs");
			if (occurs == null || occurs.equals("")) {
				throw new BuildException("Parameter 'occurs' required");
			}
		}
		if (filePath == null || filePath.equals("")) {
			filePath = getProject().getProperty("file.path");
			if (filePath == null || filePath.equals("")) {
				throw new BuildException("Parameter 'filePath' required");
			}
		}
		if (token == null || token.equals("")) {
			token = getProject().getProperty("token.string");
			if (token == null || token.equals("")) {
				throw new BuildException("Parameter 'token' required");
			}
		}
	}

	public void writeFileContent() {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("");
		} catch (final IOException e) {
			throw new BuildException(e);
		}

		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true))) {
			int i = 0;
			int length = 0;
			if (all.length() >= BUFFER_LENGTH) {
				length = BUFFER_LENGTH;
			} else {
				length = all.length();
			}

			final byte[] allBuff = all.getBytes();
			int remain = all.length();
			while (length > 0) {
				final byte[] buffer = new byte[BUFFER_LENGTH];
				System.arraycopy(allBuff, i, buffer, 0, length);
				bos.write(buffer, 0, length);
				i += length;
				remain = remain - length;
				if (remain > BUFFER_LENGTH) {
					length = BUFFER_LENGTH;
				} else {
					length = remain;
				}
			}
		} catch (final IOException e) {
			throw new BuildException(e);
		}
	}

}
