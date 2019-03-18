package com.arcadsoftware.ae.core.logger.router.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.arcadsoftware.ae.core.utils.Utils;

public class FileMessageRouter extends MessageRouterAdapter {

	private String filename;
	
	private void writeToFile(String data) {		
		
		File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		
		FileOutputStream file;
		try {			
			//String convertData = new String(data.getBytes(),"ISO-8859-1");
			file = new FileOutputStream(filename,false);
			file.write(data.getBytes());
			file.close();				
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.router.AbstractMessageRouter#doFinalize()
	 */
	protected void doFinalize() {
		String data = getData(true);
		writeToFile(data);
	}

	/**
	 * @return Renvoie le nom du fichier de sortie.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * Méthode permettant de spécifier le nom du 
	 * fichier de sortie.
	 * @param filename String : Nom du fichier de sorrtie.
	 */
	public void setFilename(String filename) {
		String arcad_home = Utils.getHomeDirectory();
		if (arcad_home!=null) {
			filename = Utils.substitute(filename,"${arcad_home}",arcad_home);
		}
		this.filename = filename;
	}
}
