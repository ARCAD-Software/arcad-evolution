package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;

public abstract class AbstractArcadCopyTask extends Copy {
	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public abstract void doAfterExecuting();

	public abstract void doBeforeCopying(String fromFile, String toFile, boolean overwrite);

	public abstract void doBeforeExecuting();

	/**
	 * Actually does the file (and possibly empty directory) copies. This is a good method for subclasses to override.
	 */
	@Override
	protected void doFileOperations() {
		if (fileCopyMap.size() > 0) {
			log("Copying " + fileCopyMap.size()
					+ " file" + (fileCopyMap.size() == 1 ? "" : "s")
					+ " to " + destDir.getAbsolutePath());

			final Enumeration<String> e = fileCopyMap.keys();
			while (e.hasMoreElements()) {
				final String fromFile = e.nextElement();
				final String[] toFiles = fileCopyMap.get(fromFile);

				for (final String toFile : toFiles) {
					if (fromFile.equals(toFile)) {
						log("Skipping self-copy of " + fromFile, verbosity);
						continue;
					}
					try {
						log("Copying " + fromFile + " to " + toFile, verbosity);

						final FilterSetCollection executionFilters = new FilterSetCollection();
						if (filtering) {
							executionFilters
									.addFilterSet(getProject().getGlobalFilterSet());
						}
						for (final Object element : getFilterSets()) {
							executionFilters
									.addFilterSet((FilterSet) element);
						}

						doBeforeCopying(fromFile, toFile, forceOverwrite);

						fileUtils.copyFile(fromFile, toFile, executionFilters,
								getFilterChains(), forceOverwrite,
								preserveLastModified, getEncoding(),
								getOutputEncoding(), getProject());

					} catch (final IOException ioe) {
						String msg = "Failed to copy " + fromFile + " to " + toFile
								+ " due to " + getCause(ioe);
						final File targetFile = new File(toFile);
						if (targetFile.exists() && !targetFile.delete()) {
							msg += " and I couldn't delete the corrupt " + toFile;
						}
						if (failonerror) {
							throw new BuildException(msg, ioe, getLocation());
						}
						log(msg, Project.MSG_ERR);
					}
				}
			}
		}
		if (includeEmpty) {
			final Enumeration<String[]> e = dirCopyMap.elements();
			int createCount = 0;
			while (e.hasMoreElements()) {
				final String[] dirs = e.nextElement();
				for (final String dir : dirs) {
					final File d = new File(dir);
					if (!d.exists()) {
						if (!d.mkdirs()) {
							log("Unable to create directory "
									+ d.getAbsolutePath(), Project.MSG_ERR);
						} else {
							createCount++;
						}
					}
				}
			}
			if (createCount > 0) {
				log("Copied " + dirCopyMap.size()
						+ " empty director"
						+ (dirCopyMap.size() == 1 ? "y" : "ies")
						+ " to " + createCount
						+ " empty director"
						+ (createCount == 1 ? "y" : "ies") + " under "
						+ destDir.getAbsolutePath());
			}
		}
	}

	@Override
	public void execute() {
		doBeforeExecuting();
		try {
			super.execute();
		} finally {
			doAfterExecuting();
		}
	}

	/**
	 * Returns a reason for failure based on the exception thrown. If the exception is not IOException output the class
	 * name, output the message if the exception is MalformedInput add a little note.
	 */
	protected String getCause(final Exception ex) {
		final boolean baseIOException = ex.getClass() == IOException.class;
		final StringBuilder message = new StringBuilder();
		if (!baseIOException || ex.getMessage() == null) {
			message.append(ex.getClass().getName());
		}
		if (ex.getMessage() != null) {
			if (!baseIOException) {
				message.append(" ");
			}
			message.append(ex.getMessage());
		}
		if (ex.getClass().getName().indexOf("MalformedInput") != -1) {
			message.append(LINE_SEPARATOR);
			message.append(
					"This is normally due to the input file containing invalid");
			message.append(LINE_SEPARATOR);
			message.append("bytes for the character encoding used : ");
			message.append(
					getEncoding() == null
							? fileUtils.getDefaultEncoding()
							: getEncoding());
			message.append(LINE_SEPARATOR);
		}
		return message.toString();
	}

}
