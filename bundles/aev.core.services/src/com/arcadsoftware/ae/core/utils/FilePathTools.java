/*
 * Créé le 5 août 08
 *
 * TODO Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre - Préférences - Java - Style de code - Modèles de code
 */
package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class FilePathTools {
	public static final String ZIPEXT = ".zar"; //$NON-NLS-1$
	/**
	 * 
	 */
	public FilePathTools() {
	}
	
	/**
	 * Si "pathToSubstract" est égal à "path", on retourne une chaine vide.
	 * Si "pathToSubstract" et "path" sont différents alors : 
	 * - Si pathToSubstract ne se finit pas par "separator", on lui concatène "separator"
	 * - Si "path" commence par "pathToSubstract", on renvoit la partie différente entre "path" et "pathToSubstract"
	 * - sinon on renvoit "path" 
	 * @param path
	 * @param pathToSubstract
	 * @param separator
	 * @return
	 */
	public static final String substractFilePath(String path,
	                                             String pathToSubstract,
	                                             char separator){
		if ((!pathToSubstract.equals("")) &&  !(path.equals(""))) { //$NON-NLS-1$ //$NON-NLS-2$
			if (pathToSubstract.toUpperCase().equals(path.toUpperCase())) {
				return ""; //$NON-NLS-1$
			} else {
				if (pathToSubstract.charAt(pathToSubstract.length()-1)!=separator) {
					//pathToSubstract.concat(new Character(separator).toString());				
					pathToSubstract = pathToSubstract + separator;
				}
				
				if (path.toLowerCase().indexOf(pathToSubstract.toLowerCase())==0)
					return path.substring(pathToSubstract.length(),
										  path.length());
				else
					return path;					
			}
		} else
			return path;
	}
	
	public static String getExtension(String fileName) {
		int pos = fileName.lastIndexOf("."); //$NON-NLS-1$
		if (pos>-1)
			return fileName.substring(pos);
		else
			return "";  //$NON-NLS-1$
	}	
	public static String getShortExtension(String fileName) {
		int pos = fileName.lastIndexOf("."); //$NON-NLS-1$
		if ((pos>-1)&&(pos + 1 < fileName.length()))
			return fileName.substring(pos+1);
		else
			return "";  //$NON-NLS-1$
	}	
	
	//--------------------------------------------------------------------------
	public static final String extractLastPart(String path,char separator){  
  		int pos = 0;
  		if (path.length()>0) {
  			pos = path.lastIndexOf(new Character(separator).toString());
  			if (pos!=-1) 
  				return path.substring(pos+1,path.length());
  			else
  				return path;		
  		} else
  			return ""; //$NON-NLS-1$
	}
	//--------------------------------------------------------------------------
	public static final String extractFirstPart(String path,char separator){
		String result;  
		int pos = 0;		
		if (!path.equals("")){ //$NON-NLS-1$
			if (path.length()>0) {
				pos = path.lastIndexOf(new Character(separator).toString());
				if (pos!=-1) {
					result= path.substring(0,pos);
				}				
				else
					result=path;
				if (result.charAt(result.length()-1)==separator) 
					return result.substring(0,result.length()-1);
				else
					return result;
				
			} else
				return ""; //$NON-NLS-1$
		} else
			return ""; //$NON-NLS-1$
	}	
	//--------------------------------------------------------------------------
	public static final boolean isSubFile(String fileName,String path){
		return (fileName.toLowerCase().indexOf(path.toLowerCase())==0);
	}
	
	public static String findFile(String fileToSearch,String parentDirectory) {
		try {
			File f = new File(fileToSearch);
			if (f.isAbsolute()) {
				if (f.exists())
					return f.getCanonicalPath();
				else
					return null;
			} else {					
				f = new File(parentDirectory,fileToSearch);
				if (f.exists()) {
					return f.getCanonicalPath();
				} else {
					File dir = new File(parentDirectory);
					File[] dirs = dir.listFiles(
						new FileFilter(){
							public boolean accept(File file) {
							    return file.isDirectory(); 
							}	
						}
					);
					
					for (int i=0;i<dirs.length;i++){
						String result = findFile(fileToSearch,dirs[i].getCanonicalPath());
						if (result!=null)
							return result;
					}				
				}		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		 
		return null;
	}	
	
	public static  String findFileInPathes(String[] pathes,String fileToSearch) {
		for(int j=0;j<pathes.length;j++) {
			String path = pathes[j];
			String resultFile = findFile(fileToSearch,path);
			if (resultFile!=null)
				return resultFile;
		}		
		return null;
	}
	
}
