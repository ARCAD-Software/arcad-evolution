package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.file;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractListFillTask;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;

public class ListOfFileAdditionTask extends AbstractListFillTask {

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.ListFillTask#doBeforePopulating()
	 */
	@Override
	public int populate() {
		// Traitement de l'ajout à partir d'une liste
		int count1 = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final FileList fromlist = (FileList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count1 = list.addItems(fromlist, checkIfExists, replaceIfExists);
		}
		// Traitement d'ajout à partir de ressources
		int count2 = 0;
		count2 = list.addItems(filler, checkIfExists, replaceIfExists);
		return count1 + count2;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		// Le controle de saisie est réaliser dans le super
		final File f = new File(getFilename());
		if (!f.exists()) {
			throw new BuildException("File not found!");
		}
	}

}
