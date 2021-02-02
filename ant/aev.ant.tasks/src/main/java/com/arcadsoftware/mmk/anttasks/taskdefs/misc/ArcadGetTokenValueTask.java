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

public class ArcadGetTokenValueTask extends AbstractArcadAntTask {

	private static final String ANY_CHARACTERS_STRING = "\\s*";
	private static final String APOSTROPHE = "\'";
	private static final String BLANK = " ";
	private static final int BUFFER_LENGTH = 4096;
	private static final String CARRIAGE_RETURN = "\r";

	private static final String CLOSING_TAG = ">";
	private static final String EQUAL = "=";
	private static final String FIRST = "*FIRST";
	private static final String LINE_BREAK = "\n";
	private static final String OPENING_TAG = "<";
	private static final String QUOTATION_MARK = "\"";
	private static final String STAR_ALL = "*ALL";
	String all = "";
	private boolean allOccurs = false;
	File file = null;
	private String filePath = null;
	String[] keys = null;
	private String occurs = null;
	private int[] occursRank = null;
	private boolean oneOccur = true;
	private String returnProperty;
	Scanner scanner = null;

	private String token = null;

	@Override
	public void doExecute() {
		processInit();
		processExecution();
	}

	public File getFile() {
		return file;
	}

	public void getFileContent() {
		final StringBuilder b = new StringBuilder();
		scanner.useDelimiter(CARRIAGE_RETURN);
		boolean first = true;
		String scannerToken = null;
		while (scanner.hasNext()) {
			if (!first) {
				b.append(CARRIAGE_RETURN);
			}
			scannerToken = scanner.next();
			b.append(scannerToken);
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

	public String getToken() {
		return token;
	}

	public String getValue(final String key) {
		scanner.reset();
		String oldValue = null;
		final StringBuilder b = new StringBuilder();
		b.append(ANY_CHARACTERS_STRING).append(key).append(ANY_CHARACTERS_STRING);
		final String delimiter = b.toString();
		scanner.useDelimiter(delimiter);
		int idxOccurs = 0;
		int occur = 0;
		while (scanner.hasNext()) {
			occur++;
			final String find = scanner.next();
			if (allOccurs || occursRank[idxOccurs] == occur) {
				// Search for quotation mark before old value
				int i = find.indexOf(QUOTATION_MARK);
				// Search for quotation mark after old value
				int j = find.indexOf(QUOTATION_MARK, i + 1);
				String keyAndValue = "";
				// There are '"' as delimiters for the value
				if (i == 0 && j > -1) {
					oldValue = find.substring(i + 1, j + 1);
					keyAndValue = key + QUOTATION_MARK + oldValue + QUOTATION_MARK;
					// Search for apostrophe delimiters
				} else {
					// Search for apostrophe before old value
					i = find.indexOf(APOSTROPHE);
					// Search for apostrophe after old value
					j = find.indexOf(APOSTROPHE, i + 1);
					if (i == 0 && j > -1) {
						oldValue = find.substring(i + 1, j + 1);
						keyAndValue = key + APOSTROPHE + oldValue + APOSTROPHE;

						// Search for blank or carriage return or line break delimiters
					} else {
						i = find.indexOf(BLANK);
						// Skip first blank if in position 0 after end of key
						if (i == 0) {
							i = find.indexOf(BLANK, i + 1);
						}
						// If no blank, search for closing tag
						if (i < 0) {
							i = find.indexOf(OPENING_TAG);
						}
						// If no closing tag, search for carriage return
						if (i < 0) {
							i = find.indexOf(CARRIAGE_RETURN);
						}
						// If no carriage return search for line break
						if (i < 0) {
							i = find.indexOf(LINE_BREAK);
						}
						if (i > 0) {
							oldValue = find.substring(0, i);
							keyAndValue = key + oldValue;
						}
					}
				}
				int x = -1;
				if (!keyAndValue.trim().equals("")) {
					x = all.indexOf(keyAndValue);
				}
				if (x > -1) {
					final String before = all.substring(0, x);
					final String after = all.substring(x + keyAndValue.length());
					all = before + keyAndValue + after;
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
		setReturnPropertyValue(oldValue);
		return oldValue;
	}

	public void initScanner() {
		try {
			scanner = new Scanner(file);
		} catch (final FileNotFoundException e) {
			throw new BuildException(e);
		}
	}

	public void processExecution() {
		String tokenBefore = "";
		for (final String key : keys) {
			if (!tokenBefore.equals(key)) {
				if (scanner != null) {
					scanner.close();
				}
				initScanner();
			}
			scanFile(key);
			tokenBefore = key;
		}
	}

	public void processInit() {
		final String[] occursArr = occurs.split(",");
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

		String key = "";
		if (token.indexOf(EQUAL) > -1) {
			key = token.substring(0, token.indexOf(EQUAL) + 1);
		} else if (token.indexOf(CLOSING_TAG) > -1) {
			key = token.substring(0, token.indexOf(CLOSING_TAG) + 1);
		} else {
			key = token;
		}
		keys[0] = key;

		initScanner();
		getFileContent();
	}

	public void scanFile(final String key) {
		log(getValue(key));
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

	public void setReturnProperty(final String property) {
		returnProperty = property;
	}

	protected final void setReturnPropertyValue(final String value) {
		if (value != null) {
			if (getProject().getUserProperty(returnProperty) == null) {
				getProject().setProperty(returnProperty, value);
			} else {
				getProject().setUserProperty(returnProperty, value);
			}
		}
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
		if (returnProperty == null) {
			throw new BuildException("You must specify a property to set.");
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
