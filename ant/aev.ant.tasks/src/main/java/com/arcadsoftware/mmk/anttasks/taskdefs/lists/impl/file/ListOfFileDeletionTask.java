package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.file;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListFromResourcesTask;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListOfFileDeletionTask extends AbstractXmlFileListFromResourcesTask {

	private String deleteQuery = null;

	@Override
	public int process() {
		// Suppression à partir d'une liste
		int count1 = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final FileList fromlist = (FileList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count1 = list.removeItems(fromlist);
		}
		// Supression à partir d'une requete
		int count2 = 0;
		if (deleteQuery != null && !deleteQuery.equals("")) {
			count2 = list.removeItems(deleteQuery);
		}
		int count3 = 0;
		// Supression à partir d'élément ressources
		final StoreItem[] items = new StoreItem[files.size()];
		for (int i = 0; i < files.size(); i++) {
			final String filename = files.get(i);
			items[i] = list.toStoreItem(new File(filename));
		}
		count3 = list.removeItems(items);
		return count1 + count2 + count3;
	}

	/**
	 * @param deleteQuery
	 *            the deleteQuery to set
	 */
	public void setDeleteQuery(final String deleteQuery) {
		this.deleteQuery = deleteQuery;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListFromResourcesTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (getFilename() == null || getFilename().equals("")) {
			throw new BuildException("Filename attribute must be set!");
		}
		// Le controle de saisie est réalisé dans le super
		final File f = new File(getFilename());
		if (!f.exists()) {
			throw new BuildException("File not found!");
		}

	}

}
