package com.arcadsoftware.ae.core.logger.router.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.arcadsoftware.ae.core.utils.FilePathTools;
import com.arcadsoftware.ae.core.utils.Utils;

public class FileInstantMessageRouter extends MessageRouterAdapter {
	private String filename;
	private boolean rollingFile = false;
	private long rollingFileMaxSize = 1024000;
	private long rollingFileMaxIndex = 99999;	
	private boolean resetOnOpen = false;
	
	
	public void renameLogFile(){
		File f = new File(filename);
		File dir = f.getParentFile();
		final String osfilename = f.getName();
		String[] files = dir.list(new FilenameFilter(){
			public boolean accept(File dir, String name) { 
				return name.matches(osfilename+"\\.[0-9]+");
			}			
		});		
		Arrays.sort(files);
		Collections.reverse(Arrays.asList(files));
		
		for (int i=0;i<files.length;i++) {
			String fn = files[i];
			String ext = FilePathTools.getExtension(fn);
			int extInt = new Integer(ext.substring(1)).intValue();
			extInt++;
			File fsrc = new File(dir.getAbsolutePath()+File.separator+fn);
			if ((rollingFileMaxIndex>0) && (extInt > rollingFileMaxIndex)){
				fsrc.delete();
			} else {
				String suffix = Utils.lpad(String.valueOf(extInt),5,'0');				
				File ftrg = new File(filename+"."+suffix);
				fsrc.renameTo(ftrg);
			}
		}
		File fsrc = new File(filename);
		String suffix = Utils.lpad("1",5,'0');
		File ftrg = new File(filename+"."+suffix);
		fsrc.renameTo(ftrg);		
	}
	
	private void writeToFile(String data, boolean append) {
		
		if (rollingFile){
			File f = new File(filename);
			if (f.length()>=rollingFileMaxSize) {
				renameLogFile();
			}					
		}
		
		FileOutputStream file;
		File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try {
			file = new FileOutputStream(filename,append);
			
			if (data!=null) {
				file.write(data.getBytes());
			}
			file.close();				
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}		
	}
	
	protected void doInitialize() {
		writeToFile("-------------------- "+new Date()+" -------------------- \n",!resetOnOpen);
	}	
	
	protected void doFinalize() {
		writeToFile("******************** "+new Date()+" ******************** \n",true);
	}	

	protected void doIntercept() {
		String data = getData(true);
		writeToFile(data,true);
		messages.clear();
	}	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		String arcad_home = Utils.getHomeDirectory();
		if (arcad_home!=null) {
			filename = Utils.substitute(filename,"${arcad_home}",arcad_home);
		}
		this.filename = filename;
	}


	public boolean isRollingFile() {
		return rollingFile;
	}


	public void setRollingFile(boolean rollingFile) {
		this.rollingFile = rollingFile;
	}
	public void setResetOnOpen(boolean resetOnOpen) {
		this.resetOnOpen = resetOnOpen;
	}

	public long getRollingFileMaxSize() {
		return rollingFileMaxSize;
	}

	public long getRollingFileMaxIndex() {
		return rollingFileMaxIndex;
	}	
	
	public void setRollingFileMaxSize(long rollingFileMaxSize) {
		this.rollingFileMaxSize = rollingFileMaxSize;
	}


	public void setRollingFileMaxIndex(long rollingFileMaxIndex) {
		if (rollingFileMaxIndex>99999)
			rollingFileMaxIndex = 99999;		
		this.rollingFileMaxIndex = rollingFileMaxIndex;
	}
}
