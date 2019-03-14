package com.arcadsoftware.mmk.lists.impl.fillers;

import java.io.File;

import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;

public class DirectoryScanFiller extends AbstractFiller {
	
	private String directoryToScan;

	public DirectoryScanFiller(FileList list,String directoryToScan) {
		super(list);
		this.directoryToScan = directoryToScan;
	}

	private int getFiles(File basedir) {
		if (basedir.exists()) {
			int count = 0;
			File[] files = basedir.listFiles();
			for (int i= 0;i<files.length;i++){
				if (files[i].isDirectory()){
					getFiles(files[i]);
				} else {										
					saveItem(getList().toStoreItem(files[i]));
					count++;
				}
			}
			return count;
		} 
		return -1;
	}	
	
	@Override
	public int fill() {
		File directoryToScanFile = new File(directoryToScan);
		if (directoryToScanFile.exists() && directoryToScanFile.isDirectory()) {
			return getFiles(directoryToScanFile);
		}
		return -1;
	}
	

	
}
