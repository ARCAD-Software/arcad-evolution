package com.arcadsoftware.aev.core.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * Collection of file tools to centralize handling/logging of File related methods.
 * 
 * @author ACL
 *
 */
public class FileTools {

	private FileTools() {}
	
	public static boolean deleteFile(File file) {
		try {
			Files.delete(file.toPath());
			return true;
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
		}
		return false;
	}
	
	public static boolean createNewFile(File file) throws IOException {
		boolean created = file.createNewFile();
		// This is only false if the file is not created, otherwise it should throw an error...
		if (!created)
			MessageManager.logDiagnostic("File " + file.getName() + " already exists");
		return created;
	}
	
	public static boolean renameTo(File file, File dest) {
		boolean renamed = file.renameTo(dest);
		if (!renamed)
			MessageManager.logDiagnostic("Could not rename file " + file.getName() + " to " + dest.getName());
		return renamed;
	}
	
	public static boolean setReadOnly(File file) {
		boolean readOnly = file.setReadOnly();
		if (!readOnly)
			MessageManager.logDiagnostic("Could not set " + file.getName() + " to Read Only");
		return readOnly;
	}
	
}
