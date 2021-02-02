package com.arcadsoftware.ae.core.logger.router.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.arcadsoftware.ae.core.exceptions.ArcadRuntimeException;
import com.arcadsoftware.ae.core.utils.FilePathTools;
import com.arcadsoftware.ae.core.utils.Utils;

public class FileInstantMessageRouter extends MessageRouterAdapter {
	private String filename;
	private boolean resetOnOpen = false;
	private boolean rollingFile = false;
	private long rollingFileMaxIndex = 99999;
	private long rollingFileMaxSize = 1024000;

	@Override
	protected void doFinalize() {
		writeToFile("******************** " + new Date() + " ******************** \n", true);
	}

	@Override
	protected void doInitialize() {
		writeToFile("-------------------- " + new Date() + " -------------------- \n", !resetOnOpen);
	}

	@Override
	protected void doIntercept() {
		final String data = getData(true);
		writeToFile(data, true);
		messages.clear();
	}

	public String getFilename() {
		return filename;
	}

	public long getRollingFileMaxIndex() {
		return rollingFileMaxIndex;
	}

	public long getRollingFileMaxSize() {
		return rollingFileMaxSize;
	}

	public boolean isRollingFile() {
		return rollingFile;
	}

	public void renameLogFile() {
		final File f = new File(filename);
		final File dir = f.getParentFile();
		final String osfilename = f.getName();
		final String[] files = dir.list((file, name) -> name.matches(osfilename + "\\.[0-9]+"));
		Arrays.sort(files);
		Collections.reverse(Arrays.asList(files));

		for (final String fn : files) {
			final String ext = FilePathTools.getExtension(fn);
			int extInt = Integer.parseInt(ext.substring(1));
			extInt++;
			final File fsrc = new File(dir.getAbsolutePath() + File.separator + fn);
			if (rollingFileMaxIndex > 0 && extInt > rollingFileMaxIndex) {
				fsrc.delete();
			} else {
				final String suffix = Utils.lpad(String.valueOf(extInt), 5, '0');
				final File ftrg = new File(filename + "." + suffix);
				fsrc.renameTo(ftrg);
			}
		}
		final File fsrc = new File(filename);
		final String suffix = Utils.lpad("1", 5, '0');
		final File ftrg = new File(filename + "." + suffix);
		fsrc.renameTo(ftrg);
	}

	public void setFilename(String filename) {
		final String arcad_home = Utils.getHomeDirectory();
		if (arcad_home != null) {
			filename = Utils.substitute(filename, "${arcad_home}", arcad_home);
		}
		this.filename = filename;
	}

	public void setResetOnOpen(final boolean resetOnOpen) {
		this.resetOnOpen = resetOnOpen;
	}

	public void setRollingFile(final boolean rollingFile) {
		this.rollingFile = rollingFile;
	}

	public void setRollingFileMaxIndex(long rollingFileMaxIndex) {
		if (rollingFileMaxIndex > 99999) {
			rollingFileMaxIndex = 99999;
		}
		this.rollingFileMaxIndex = rollingFileMaxIndex;
	}

	public void setRollingFileMaxSize(final long rollingFileMaxSize) {
		this.rollingFileMaxSize = rollingFileMaxSize;
	}

	private void writeToFile(final String data, final boolean append) {
		if (rollingFile) {
			final File f = new File(filename);
			if (f.length() >= rollingFileMaxSize) {
				renameLogFile();
			}
		}

		final File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try (FileOutputStream file = new FileOutputStream(filename, append)) {
			if (data != null) {
				file.write(data.getBytes());
			}
		} catch (final IOException e) {
			throw new ArcadRuntimeException("Could not write to file " + filename, e);
		}
	}
}
