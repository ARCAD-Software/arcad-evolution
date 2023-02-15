package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class CopyFileHelper extends AbstractRollbackableTaskHelper {

	private static final String CREATED = "created";
	private static final String UPDATED = "updated";

	protected ArrayList<String> createdfiles = new ArrayList<>();
	protected ArrayList<String> updatedfiles = new ArrayList<>();

	public CopyFileHelper() {
		super();
	}

	public CopyFileHelper(final IRollbackableTask task) {
		super(task);
	}

	public void backupFile(final File f) {
		backupFile(null, f.getAbsolutePath(), true);
	}

	public void backupFile(final String source, final String fileToBackup, final boolean overwrite) {
		final File fileToBackupFile = new File(fileToBackup);
		if (fileToBackupFile.exists()) {
			// Creation d'une copie de secours dans le répertoire de
			// sauvegarde
			try {
				// Traitement du mode overwrite.
				// Si le fichier de destination existe et qu'il est
				// plus recent que le fichier source, on ne fait rien
				if (source != null && !overwrite) {
					final File sourceFile = new File(source);
					final long slm = sourceFile.lastModified();
					if (slm != 0 && fileToBackupFile.lastModified() > slm) {
						return;
					}
				}
				final String[] pathes = FileUtils.getFileUtils().dissect(fileToBackup);
				final String backupFile = dataDirectory + File.separator + pathes[1];
				final File f = new File(backupFile);
				if (!f.exists()) {
					FileUtils.getFileUtils().createNewFile(f, true);
				}
				FileUtils.getFileUtils().copyFile(fileToBackup, backupFile, null, true);

				// Enregistrement du fichier dans les ifnormations de rollback
				updatedfiles.add(fileToBackup);
			} catch (final IOException e) {
				throw new BuildException("Unable to create backup file!", e, task.getTask().getLocation());
			}
		} else {
			// Si le répertoire n'existe pas, il sera créé lors de la copie, donc il faudra l'effacer
			// et tout son contenu avec
			pathWillBeCreated(fileToBackupFile);
			createdfiles.add(fileToBackup);
		}
	}

	@Override
	public Element createRollbackData(final Element e) {
		final Element action = super.createRollbackData(e);
		// Traitement des fichiers modifiés
		for (final String updatedfile : updatedfiles) {
			final Element copyAction = XMLUtils.addElement(document, action, "backup");
			copyAction.setAttribute("src", updatedfile);
			final String path = FileUtils.getFileUtils().removeLeadingPath(
					new File(getBackupRoot()),
					new File(dataDirectory));
			copyAction.setAttribute("datadir", path);
			copyAction.setAttribute("status", UPDATED);
		}
		// Traitement des fichiers ajoutés
		for (final String createdfile : createdfiles) {
			final Element copyAction = XMLUtils.addElement(document, action, "backup");
			copyAction.setAttribute("src", createdfile);
			copyAction.setAttribute("datadir", "");
			copyAction.setAttribute("status", CREATED);
		}
		return action;
	}

	protected boolean pathWillBeCreated(final File aFile) {
		boolean created = false;

		final File parentDirectory = aFile.getParentFile();
		if (parentDirectory != null && !parentDirectory.exists()) {
			created = true;
			if (!pathWillBeCreated(parentDirectory) && !createdfiles.contains(parentDirectory.getAbsolutePath())) {
				createdfiles.add(parentDirectory.getAbsolutePath());
			}
		}

		return created;
	}

	public void restoreFile(final String dataDir, final String fileToRestore, final String status) {
		final String[] pathes = FileUtils.getFileUtils().dissect(fileToRestore);
		final String backedupFile = getBackupRoot() + File.separator + dataDir + File.separator + pathes[1];
		if (status.equals(UPDATED)) {
			if (new File(backedupFile).exists()) {
				try {
					final File f = new File(fileToRestore);
					if (!f.exists()) {
						FileUtils.getFileUtils().createNewFile(f, true);
					}
					FileUtils.getFileUtils().copyFile(backedupFile, fileToRestore, null, true);
				} catch (final IOException e) {
					throw new BuildException("Unable to restore file!", e, task.getTask().getLocation());
				}
			}
		} else if (status.equals(CREATED)) {
			// Si le fichier a restaurer existe mais qu'aucun fichier correspondant
			// n'existe dans le repertoire de backup, on supprime le fichier de destination.
			final File file = new File(fileToRestore);
			if (file.exists()) {
				try {
					Files.delete(file.toPath());
				} catch (final IOException e) {
					throw new BuildException("Unable to delete file!", e, task.getTask().getLocation());
				}
			}
		}
	}

	@Override
	public boolean rollback(final ArcadRollbackTask rollbackTask, final Element e) {
		final NodeList backupActions = e.getElementsByTagName("backup");
		// Iterate through rollback elements in reverse to prevent
		// the deletion failure of non empty directory
		IntStream
				.iterate(backupActions.getLength() - 1, i -> i - 1)
				.limit(backupActions.getLength())
				.mapToObj(backupActions::item)
				.forEach(node -> {
					final Element backupAction = (Element) node;
					final String fileName = backupAction.getAttribute("src");
					final String status = backupAction.getAttribute("status");
					final String datadir = backupAction.getAttribute("datadir");
					if (!fileName.isEmpty()) {
						restoreFile(datadir, fileName, status);
					}
				});
		return true;
	}
}
