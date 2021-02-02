package com.arcadsoftware.ae.core.logger.router.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.arcadsoftware.ae.core.exceptions.ArcadRuntimeException;
import com.arcadsoftware.ae.core.utils.Utils;

public class FileMessageRouter extends MessageRouterAdapter {

	private String filename;

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.router.AbstractMessageRouter#doFinalize()
	 */
	@Override
	protected void doFinalize() {
		final String data = getData(true);
		writeToFile(data);
	}

	/**
	 * @return Renvoie le nom du fichier de sortie.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Méthode permettant de spécifier le nom du fichier de sortie.
	 *
	 * @param filename
	 *            String : Nom du fichier de sorrtie.
	 */
	public void setFilename(String filename) {
		final String arcad_home = Utils.getHomeDirectory();
		if (arcad_home != null) {
			filename = Utils.substitute(filename, "${arcad_home}", arcad_home);
		}
		this.filename = filename;
	}

	private void writeToFile(final String data) {

		final File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}

		try (FileOutputStream file = new FileOutputStream(filename, false)) {
			file.write(data.getBytes());
		} catch (final IOException e) {
			throw new ArcadRuntimeException("Could not write to " + filename, e);
		}
	}
}
