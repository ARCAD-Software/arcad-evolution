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

	String all = "";
	String[] keys = null;
	String[] valuesToReplace = null;
	File file = null;
	Scanner scanner = null;
	final String CARRIAGE_RETURN = "\r";
	final String LINE_BREAK = "\n";
	final String BLANK = " ";
	final String COMMA = ",";
	final String SEMICOLON = ";";
	final String QUOTATION_MARK = "\"";
	final String APOSTROPHE = "\'";
	final String ANY_CHARACTERS_STRING = "\\s*";
	final String ANY_CHARACTERS_OR_BLANKS_STRING = "[\\s\\S]*?";
	final String EQUAL = "=";
	final String CLOSING_TAG = ">";
	final String OPENING_TAG = "<";
	final String ALL = "*ALL";
	final String FIRST = "*FIRST";
	boolean oneOccur = true;
	boolean allOccurs = false;
	int[] occursRank = null;
	String occurs = null;
	String token = null;
	String filePath = null;
	boolean isXmlTagDelimited = false;
	String separator = "";
	
	/*
	 * must receive 3 arguments :
	 * 
	 * file path
	 * 
	 * occurrence rank : if several occurrences to replace, enumerate occurrences ranks with ',' as separator (eg : 1,5,18); special values available : *FIRST, *ALL
	 * 
	 * token to replace
	 * 
	 * for example with a token : 'program name="test1"'; 
	 * the search is run with token 'program name='; 
	 * when token found, old value after '=' is replaced by 'test1';
	 */
	@Override
	public void validateAttributes() {
		System.out.println("Attributes validation");
		if ((occurs==null) || (occurs.equals(""))) {
			occurs = getProject().getProperty("token.occurs");
			if ((occurs==null) || (occurs.equals(""))) {
				throw new BuildException("Parameter 'occurs' required");
			}
		}
		if ((filePath==null) || (filePath.equals(""))) {
			filePath = getProject().getProperty("file.path");
			if ((filePath==null) || (filePath.equals(""))) {
				throw new BuildException("Parameter 'filePath' required");
			}
		}
		if ((token==null) || (token.equals(""))) {
			token = getProject().getProperty("token.string");
			if ((token==null) || (token.equals(""))) {
				throw new BuildException("Parameter 'token' required");
			}
		}
	}

	@Override
	public void doExecute() throws BuildException {
		processInit();
		processExecution();
		writeFileContent();
	}
	
	public void processExecution() throws BuildException {		
		String tokenBefore = "";
		for (int i = 0; i < keys.length; i++) {
			if (!tokenBefore.equals(keys[i])) {
				if (scanner != null)
					scanner.close();
				initScanner();
			}
			scanFile(keys[i], valuesToReplace[i]);
			tokenBefore = keys[i];
		}
	}
	
	public void processInit() {
		String[] occursArr = occurs.split(COMMA);
		occursRank = new int[occursArr.length];
		if (occursArr.length == 1 && occursArr[0].equals(ALL)) {
			oneOccur = false;
			allOccurs = true;
		} else if (occursArr.length == 1 && occursArr[0].equals(FIRST)) {
			oneOccur = true;
			allOccurs = false;
			occursRank[0] = 1;
		} else if (occursArr.length == 1) {
			oneOccur = true;
			allOccurs = false;
			occursRank[0] = new Integer(occursArr[0]).intValue();
		} else if (occursArr.length > 1) {
			oneOccur = false;
			allOccurs = false;
			for (int i = 0; i < occursArr.length; i++) {
				occursRank[i] = new Integer(occursArr[i]).intValue();
			}
		}
		
		keys = new String[1];
		valuesToReplace = new String[1];

		String key = "";
		String valueToReplace = "";
		if (token.indexOf(EQUAL) > -1) {
			key = token.substring(0, token.indexOf(EQUAL));
			valueToReplace = token.substring(token.indexOf(EQUAL)+1);
			separator = EQUAL;
		} else if (token.indexOf(CLOSING_TAG) > -1) {
			key = token.substring(0, token.indexOf(CLOSING_TAG));
			valueToReplace = token.substring(token.indexOf(CLOSING_TAG)+1);
			separator = CLOSING_TAG;
			isXmlTagDelimited = true;
		} else {
			throw new BuildException("Invalid token! the token must represent either an xml attribute or tag, either a propertie key. Must contain a separator '>' or '='.");
		}
		// Trim ahead and trailing blanks in the replacement value
		//valueToReplace = valueToReplace.trim();
		// Trim quotation marks or apostrophes in the replacement value
		int index = valueToReplace.indexOf(QUOTATION_MARK);
		if (index > -1) {
			valueToReplace = valueToReplace.substring(index+1);
			index = valueToReplace.lastIndexOf(QUOTATION_MARK);
			if (index > -1) {
				valueToReplace = valueToReplace.substring(0, index);
			}
		} else {
			index = valueToReplace.indexOf(APOSTROPHE);
			if (index > -1) {
				valueToReplace = valueToReplace.substring(index+1);
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
	
	public void getFileContent() {
		StringBuffer b = new StringBuffer();
		scanner.useDelimiter(CARRIAGE_RETURN);
		boolean first = true;
		String token = null;
		while(scanner.hasNext()) {
			if (!first)
				b.append(CARRIAGE_RETURN);
			token = scanner.next();
			b.append(token);
			first = false;
		}
		all = b.toString();
		System.out.println(all);
	}
	
	public void initScanner() {
		scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void writeFileContent() {
		int BUFFER_LENGTH = 4096;
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file,true));
			int i = 0;
			int length = 0;
			if (all.length() >= BUFFER_LENGTH) 
				length = BUFFER_LENGTH;
			else 
				length = all.length();
				
			byte[] allBuff = all.getBytes();
			int remain = all.length();
			while (length > 0) {
				byte[] buffer = new byte[BUFFER_LENGTH];
				System.arraycopy(allBuff, i, buffer, 0, length);
				bos.write(buffer,0,length);
				i += length;
				remain = remain - length;
				if (remain > BUFFER_LENGTH)
					length = BUFFER_LENGTH;
				else
					length = remain;
			}
			bos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				bos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void scanFile(String key, String valueToReplace) {
		String value = replaceValue(key, valueToReplace);
		System.out.println(value);	
	}
	
	
	public String replaceValue(String key, String valueToReplace) {
		scanner.reset();
		String oldValue = null;
		StringBuffer b =new StringBuffer();
		b.append(ANY_CHARACTERS_STRING).append(key).append(ANY_CHARACTERS_OR_BLANKS_STRING);
		String delimiter = b.toString();
		scanner.useDelimiter(delimiter);
		String find = scanner.next();
		int idxOccurs = 0;
		int occur = 0;
		while(scanner.hasNext()) {
			occur++;
			find = scanner.next();
			if (allOccurs ||occursRank[idxOccurs] == occur) {
				// Search for equal operator
				int i = find.indexOf(EQUAL);
				if (i > -1) {
					find = find.substring(i+1);
				} else {
				// Search for closing tag
					i = find.indexOf(CLOSING_TAG);
					if (i > -1) {
						find = find.substring(i+1);
					}
				}
				String work = find.trim();
				// Search for quotation mark before old value
				i = work.indexOf(QUOTATION_MARK);
				// Search for quotation mark after old value
				int j = work.substring(i+1).indexOf(QUOTATION_MARK);
				String keyAndValue = "";
				String keyAndValueToReplace = "";
				// There are '"' as delimiters for the value
				if (i == 0 && j > -1) {
					oldValue = work.substring(i+1,j+1);
					System.out.println(oldValue);
					i = find.indexOf(QUOTATION_MARK);
					StringBuffer buf = new StringBuffer();
					//buf.append(key);
					//buf.append(separator);
					//buf.append(find.substring(0,i));
					buf.append(QUOTATION_MARK).append(oldValue).append(QUOTATION_MARK);
					keyAndValue = buf.toString();
					
					buf = new StringBuffer();
					//buf.append(key);
					//buf.append(separator);
					//buf.append(find.substring(0,i));
					buf.append(QUOTATION_MARK).append(valueToReplace).append(QUOTATION_MARK);
					keyAndValueToReplace = buf.toString();
				// Search for apostrophe delimiters
				} else {
					// Search for apostrophe before old value
					i = work.indexOf(APOSTROPHE);
					// Search for apostrophe after old value
					j = work.substring(i+1).indexOf(APOSTROPHE);
					if (i == 0 && j > -1) {
						oldValue = work.substring(i+1,j+1);
						System.out.println(oldValue);
						StringBuffer buf = new StringBuffer();
						//buf.append(key);
						//buf.append(separator);
						//buf.append(find.substring(0,i));
						buf.append(APOSTROPHE).append(oldValue).append(APOSTROPHE);
						keyAndValue = buf.toString();
						buf = new StringBuffer();
						//buf.append(key);
						//buf.append(separator);
						//buf.append(find.substring(0,i));
						buf.append(APOSTROPHE).append(valueToReplace).append(APOSTROPHE);
						keyAndValueToReplace = buf.toString();
					// Search for blank or carriage return or line break delimiters
					} else {
						i = work.indexOf(BLANK);
						// Skip first blank if in position 0 after end of key
						//if (i == 0) 
							//i = find.substring(i+1).indexOf(BLANK);
						// If no blank, search for opening tag
						//if (i < 0) 
							j = find.indexOf(OPENING_TAG);
						// If no closing tag, search for carriage return
						if (i < 0 && j < 0) 
							i = find.indexOf(CARRIAGE_RETURN);
						// If no carriage return search for line break
						if (i < 0 && j < 0) 
							i = find.indexOf(LINE_BREAK);
						if (isXmlTagDelimited && j > 0) {
							oldValue = find.substring(0,j);
						} else if (i > 0){
							oldValue = work.substring(0,Math.min(i, work.length()));
						} else {
							throw new BuildException("Something goes wrong in the attempt of replacing the value! Replacement does not occur. Check coherence between file and declared token.");
						}
						System.out.println(oldValue);
						keyAndValue = oldValue;
						keyAndValueToReplace = valueToReplace;
					}
				}
				int x = -1;
				if (!keyAndValue.trim().equals("")) {
					x = all.indexOf(keyAndValue);
				}
				if (x > -1) {
					String before = all.substring(0, x);
					String after = all.substring(x+(keyAndValue).length());
					all = before + keyAndValueToReplace + after;
					//System.out.println(all);
				} else {
					throw new BuildException("Something goes wrong in the attempt of replacing the value! Replacement does not occur. Check coherence between file and declared token.");
				}
				if (oneOccur)
					break;
				if (allOccurs)
					continue;
				if (idxOccurs + 1 < occursRank.length )
					idxOccurs++;
				else 
					break;
			}
		}
		if(oldValue == null)
			throw new BuildException("Something goes wrong in the attempt of replacing the value! Replacement does not occur. Check coherence between file and declared token.");
		return oldValue;
	}

	public File getFile() {
		return file;
	}

	private void setFile(File file) {
		this.file = file;
	}

	public String getOccurs() {
		return occurs;
	}

	public void setOccurs(String occurs) {
		this.occurs = occurs;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		setFile(new File(filePath));
	}

}
