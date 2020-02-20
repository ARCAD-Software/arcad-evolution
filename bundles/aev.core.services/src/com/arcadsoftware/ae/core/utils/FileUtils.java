package com.arcadsoftware.ae.core.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileUtils {
	private static void keepModificationDate(File sourceFile, File destFile) throws IOException {
		long lm = (long) (Math.floor( (double) sourceFile.lastModified() / 1000.0) * 1000.0);
		sourceFile.setLastModified(lm);
		destFile.setLastModified(lm);	
	}	
	
	public static void getStructure(File root,ArrayList<String> structure,FileFilter filter) throws IOException{
		if (root.isDirectory()){
			File[] kids;
			if (filter!=null)
				 kids = root.listFiles(filter);
			else
				 kids = root.listFiles();
			for (int i=0;i<kids.length;i++){
				structure.add(kids[i].getPath());
			}
			for (int i=0;i<kids.length;i++){
				getStructure(kids[i],structure,filter);
			}
		}
	}
	
	public static boolean copyFile(String source, String destination,boolean keepModificationDate){
		File destFile = new File(destination);	
		File sourceFile = new File(source);
		return copyFile(sourceFile,destFile,keepModificationDate);
	}
	
	public static boolean copyFile(File sourceFile, File destFile,boolean keepModificationDate){
		boolean result;
		InputStream in = null;
		OutputStream out = null;
		try {			
			if (destFile.exists() && destFile.isFile()) {
				destFile.delete();
			}
			File parent = destFile.getParentFile();			
			if (!parent.exists()) {
				parent.mkdirs();
			}
			
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(destFile);
	
			byte[] buffer = new byte[64 * 1024];
			int count = 0;
			do {
				out.write(buffer, 0, count);
				count = in.read(buffer, 0, buffer.length);
			} while (count != -1);

			closeCloseable(in);
			closeCloseable(out);
						
			if (keepModificationDate){
				keepModificationDate(sourceFile, destFile);
			}			
			result = true;			
		} catch (IOException ioe){
			result = false;								
		}finally{
			closeCloseable(in);
			closeCloseable(out);
		}

		return result;		
	}
	
	private static void closeCloseable(Closeable closeable ){
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException e) {
		}		
	}
	
	
	public static boolean duplicate(String sourceFolder, String targetFolder,boolean structureOnly) {
		File targetDirectory = new File(targetFolder);
		File sourceDirectory = new File(sourceFolder);
		if (!targetDirectory.exists()) {
			if (!targetDirectory.mkdirs())
				return false;
		}
		

		FileFilter filter = null;
		if (structureOnly) {
			filter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}		
			};
		}
		
		try {
			ArrayList<String> list = new ArrayList<String>();
			getStructure(sourceDirectory, list, filter);
			for(int i =0;i<list.size();i++){
				String sourceFileName = list.get(i);
				String relativePart = FilePathTools.substractFilePath(sourceFileName,sourceFolder,File.separatorChar);
				String newFilename = targetFolder+File.separator+relativePart;
				File destFile = new File(newFilename);
				File sourceFile = new File(sourceFileName);
				if (structureOnly) {
					destFile.mkdirs();
				} else {
					if (sourceFile.isFile()) {
						copyFile(sourceFileName,newFilename, false);
					}
				}
			}
			return true;
		} catch (IOException e) {			
			return false;
		}
	
	}
	
	
}
