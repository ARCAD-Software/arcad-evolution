package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_ACTIONCODE_COPY;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_ACTIONCODE_DELETE;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_PROP_DIR;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_PROP_ID;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_SETTING_FILENAME;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_ACTION;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_CODE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.DeleteFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

public class ArcadRollbackTask extends AbstractArcadAntTask {

	private Document document;
	private String rollbackDir;
	private String rollbackId;

	private final ArrayList<String> rootDirectories = new ArrayList<>();
	private String rootDirectory;

	@Override
	public void doExecute() {
		// TODO [ANT] Trier le traitement en fonction +récent -> +vieux
		Collections.sort(rootDirectories);

		for (final String rootDirectorie : rootDirectories) {
			rootDirectory = rootDirectorie;
			final String[] segments = FileUtils.getPathStack(rootDirectory);
			rollbackId = segments[segments.length - 1];
			// Chargement du document contenant les informations
			loadDocument();
			restoreAction();
		}
	}

	public String getRollbackDir() {
		return rollbackDir;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	private String getSettingFileName() {
		return rootDirectory + File.separator + RB_SETTING_FILENAME.getValue();
	}

	private boolean loadDocument() {
		final File f = new File(getSettingFileName());
		try {
			document = XMLUtils.loadXMLFromFile(f);
			return true;
		} catch (final Exception e) {
			throw new BuildException(e.getMessage(), e, getLocation());
		}
	}

	private boolean restoreAction() {
		final Element root = XMLUtils.getRoot(document);
		// iterate through child elements of root with element name "foo"
		final NodeList nodes = root.getElementsByTagName(RB_TAG_ACTION.getValue());
		for (int i = 0; i < nodes.getLength(); i++) {
			final Element action = (Element) nodes.item(i);
			final String code = action.getAttribute(RB_TAG_CODE.getValue());
			if (code.equals(RB_ACTIONCODE_COPY.getValue())) {
				restoreCopy(action);
			} else if (code.equals(RB_ACTIONCODE_DELETE.getValue())) {
				restoreDelete(action);
			}
		}
		return true;
	}

	private void restoreCopy(final Element e) {
		final CopyFileHelper helper = new CopyFileHelper(null);
		helper.setRollbackDir(rollbackDir);
		helper.setRollbackId(rollbackId);
		helper.rollback(this, e);
	}

	private void restoreDelete(final Element e) {
		final DeleteFileHelper helper = new DeleteFileHelper(null);
		helper.setRollbackDir(rollbackDir);
		helper.setRollbackId(rollbackId);
		helper.rollback(this, e);
	}

	public void setRollbackDir(final String rollbackDir) {
		this.rollbackDir = rollbackDir;
	}

	public void setRollbackId(final String rollbackId) {
		this.rollbackId = rollbackId;
	}

	@Override
	public void validateAttributes() {
		if (rollbackId == null || rollbackId.equals("")) {
			// Si la valeur passée dans la tache est vide, on recherche dans
			// le projet
			rollbackId = getProject().getProperty(RB_PROP_ID.getValue());
		}
		if (rollbackDir == null || rollbackDir.equals("")) {
			// Si la valeur passée dans la tache est vide, on recherche dans
			// le projet
			rollbackDir = getProject().getProperty(RB_PROP_DIR.getValue());
			if (rollbackDir == null || rollbackDir.equals("")) {
				// Si aucune valeur n'est passé, on recherche si il n'y a pas
				// de définition globale
				final String value = RollbackSettings.getInstance().getBackupEnvironment();
				if (value == null || value.equals("")) {
					throw new BuildException("Rollback Directory is required!");
				} else {
					rollbackDir = value;
				}
			}
		}

		final File f = new File(rollbackDir);
		if (!f.exists()) {
			throw new BuildException("Rollback Directory:" + rollbackDir + " not found!");
		} else {
			// Si le rollbackId est valide
			if (rollbackId != null && !rollbackId.equals("")) {
				final File fid = new File(rollbackDir, rollbackId);
				if (!fid.exists()) {
					throw new BuildException("Transaction Directory:" + rollbackId + " not found!");
				} else {
					rootDirectories.add(rollbackDir + File.separator + rollbackId);
				}
			} else {
				/*
				 * Si le rollbakid n'est pas passé, on scanne tous répertoires a la recherche d'un fichier
				 * "rollback-settings.xml" et on enregistre le répertoire pre comme étant un root directory
				 */
				final DirectoryScanner ds = new DirectoryScanner();

				ds.setBasedir(rollbackDir);
				// Parcours recursif pour trouver tous les jars
				ds.setIncludes(new String[] { "**\\" + RB_SETTING_FILENAME.getValue() });
				ds.setCaseSensitive(false);
				ds.setFollowSymlinks(true);
				ds.scan();
				final String[] files = ds.getIncludedFiles();
				for (final String file : files) {
					final File settingFile = new File(rollbackDir + File.separator + file);
					rootDirectories.add(settingFile.getParent());
				}

			}
		}

	}

}
