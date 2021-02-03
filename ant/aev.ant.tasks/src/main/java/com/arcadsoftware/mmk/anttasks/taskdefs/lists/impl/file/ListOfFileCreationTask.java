package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractListFillTask;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;

public class ListOfFileCreationTask extends AbstractListFillTask {

	private boolean checkDuplication = false;
	private String comment = null;

	private String listDescription = null;
	private boolean replaceFileIfExists = false;

	@Override
	public int populate() {
		// Traitement de l'ajout à partir d'une liste
		int count1 = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final FileList fromlist = (FileList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			if (checkDuplication) {
				list.addItems(fromlist, checkIfExists, replaceIfExists);
			} else {
				fromlist.duplicate(list);
				count1 = list.count("");
			}
		}
		int count2 = 0;
		// Si aucun élement n'a été ajouté par la duplication
		// on execute un populate simple
		if (!checkDuplication) {
			count2 = list.populate();
			// sinon on ajoute les nouveaux éléments
		} else {
			count2 = list.addItems(filler, checkIfExists, replaceIfExists);
		}

		updateHeaderInfo(listDescription, comment);

		return count1 + count2;
	}

	/**
	 * @param checkDuplication
	 *            the checkDuplication to set
	 */
	public void setCheckDuplication(final boolean checkDuplication) {
		this.checkDuplication = checkDuplication;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Override
	public void setDescription(final String description) {
		this.listDescription = description;
	}

	/**
	 * @param replaceFileIfExists
	 *            the replaceFileIfExists to set
	 */
	public void setReplaceFileIfExists(final boolean replaceFileIfExists) {
		this.replaceFileIfExists = replaceFileIfExists;
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
		if (f.exists()) {
			// Si l'utilisateur n'a pas demandé le remplacement
			// on envoit un exception
			if (!replaceFileIfExists) {
				throw new BuildException("File already exists!");
			} else {
				// Sinon on supprime le fichier
				try {
					Files.delete(f.toPath());
				}
				catch(IOException e) {
					throw new BuildException("File Replacement failed!", e);
				}
			}
		}
	}

}
