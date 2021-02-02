package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;

public class DeleteFileHelper extends AbstractRollbackableTaskHelper {

	private static final String TYPE_DIR = "dir";
	private static final String TYPE_FILE = "file";

	protected ArrayList<String> deletedDir = new ArrayList<>();
	protected ArrayList<String> deletedfiles = new ArrayList<>();

	public DeleteFileHelper() {
		super();
	}

	public DeleteFileHelper(final IRollbackableTask task) {
		super(task);
	}

	private void addDirectory(final String directory) {
		if (deletedDir.indexOf(directory) == -1) {
			deletedDir.add(directory);
		}
	}

	public void backup(final File fileToDelete) {
		if (fileToDelete.isFile()) {
			try {
				final String[] pathes = FileUtils.getFileUtils().dissect(fileToDelete.getAbsolutePath());
				final String backupFileName = dataDirectory + File.separator + pathes[1];
				final File backupFile = new File(backupFileName);
				if (!backupFile.exists()) {
					FileUtils.getFileUtils().createNewFile(backupFile, true);
				}
				FileUtils.getFileUtils().copyFile(fileToDelete, backupFile, null, true);

				deletedfiles.add(fileToDelete.getAbsolutePath());
				addDirectory(fileToDelete.getParent());
			} catch (final IOException e) {
				throw new BuildException("Unable to create backup file!", e, task.getTask().getLocation());
			}
		} else if (fileToDelete.isDirectory()) {
			addDirectory(fileToDelete.getAbsolutePath());
		}
	}

	@Override
	public Element createRollbackData(final Element e) {
		final Element action = super.createRollbackData(e);
		// Traitement des fichiers supprimés
		for (final String deletedfile : deletedfiles) {
			insertDeleteElement(action, deletedfile, TYPE_FILE);
		}
		// Traitement des directory supprimés
		for (final String element : deletedDir) {
			insertDeleteElement(action, element, TYPE_DIR);
		}
		return action;
	}

	private void insertDeleteElement(final Element action, final String fileName, final String type) {
		final Element deleteAction = XMLUtils.addElement(document, action, "backup");
		deleteAction.setAttribute("src", fileName);
		final String path = FileUtils.getFileUtils().removeLeadingPath(
				new File(getBackupRoot()),
				new File(dataDirectory));
		deleteAction.setAttribute("datadir", path);
		deleteAction.setAttribute("type", type);
	}

	public void restore(final String dataDir, final String fileNameToRestore, final String type) {
		if (type.equals(TYPE_FILE)) {
			final String[] pathes = FileUtils.getFileUtils().dissect(fileNameToRestore);
			final String backedupFileName = getBackupRoot() + File.separator + dataDir + File.separator + pathes[1];
			final File backedupFile = new File(backedupFileName);
			if (backedupFile.exists()) {
				try {
					final File fileToRestore = new File(fileNameToRestore);
					if (!fileToRestore.exists()) {
						FileUtils.getFileUtils().createNewFile(fileToRestore, true);
					}
					FileUtils.getFileUtils().copyFile(backedupFile, fileToRestore, null, true);
				} catch (final IOException e) {
					throw new BuildException("Unable to restore file!", e, task.getTask().getLocation());
				}
			}
		} else if (type.equals(TYPE_DIR)) {
			final File directoryToCreate = new File(fileNameToRestore);
			directoryToCreate.mkdirs();
		}
	}

	@Override
	public boolean rollback(final ArcadRollbackTask rollbackTask, final Element e) {
		final NodeList backupActions = e.getElementsByTagName("backup");
		for (int i = 0; i < backupActions.getLength(); i++) {
			final Element backupAction = (Element) backupActions.item(i);
			final String fileName = backupAction.getAttribute("src");
			final String datadir = backupAction.getAttribute("datadir");
			final String type = backupAction.getAttribute("type");
			if (fileName != null) {
				restore(datadir, fileName, type);
			} else {
				throw new BuildException("Invalid Rollback Data!", task.getTask().getLocation());
			}

		}
		return true;
	}
}
