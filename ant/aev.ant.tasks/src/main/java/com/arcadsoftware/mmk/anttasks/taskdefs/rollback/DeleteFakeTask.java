package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Sort;
import org.apache.tools.ant.types.resources.comparators.FileSystem;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.apache.tools.ant.types.resources.comparators.Reverse;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

/**
 * ATTENTION : cette classe est une copie de la classe Delete.jave de la version 1.7 de Ant. La portée de la méthode
 * {@link #delete(File) delete} a été changée de private en protected.<br>
 * Des méthodes d'accès spécifiques ont été ajoutée
 *
 * @author md
 */
public abstract class DeleteFakeTask extends MatchingTask {

	private static class ReverseDirs implements ResourceCollection {
		static final Comparator REVERSE = (foo, bar) -> ((Comparable) foo).compareTo(bar) * -1;
		private final File basedir;
		private final String[] dirs;

		ReverseDirs(final File basedir, final String[] dirs) {
			this.basedir = basedir;
			this.dirs = dirs;
			Arrays.sort(this.dirs, REVERSE);
		}

		@Override
		public boolean isFilesystemOnly() {
			return true;
		}

		@Override
		public Iterator iterator() {
			return new FileResourceIterator(null, basedir, dirs);
		}

		@Override
		public int size() {
			return dirs.length;
		}
	}

	private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
	private static final ResourceSelector EXISTS = new Exists();

	private static final ResourceComparator REVERSE_FILESYSTEM = new Reverse(new FileSystem());

	public static int getRetryDelay() {
		return DELETE_RETRY_SLEEP_MILLIS;
	}

	private boolean deleteOnExit = false;
	protected File dir = null;
	private boolean failonerror = true;
	// CheckStyle:VisibilityModifier OFF - bc
	protected File file = null;

	protected List<FileSet> filesets = new ArrayList<>();
	// by default, remove matching empty dirs
	protected boolean includeEmpty = false;

	private boolean quiet = false;
	private Resources rcs = null;
	// CheckStyle:VisibilityModifier ON

	protected boolean usedMatchingTask = false;

	private int verbosity = Project.MSG_VERBOSE;

	/**
	 * add an arbitrary selector
	 *
	 * @param selector
	 *            the selector to be added
	 * @since Ant 1.6
	 */
	@Override
	public void add(final FileSelector selector) {
		usedMatchingTask = true;
		super.add(selector);
	}

	/**
	 * Add an arbitrary ResourceCollection to be deleted.
	 *
	 * @param rc
	 *            the filesystem-only ResourceCollection.
	 */
	public void add(final ResourceCollection rc) {
		if (rc == null) {
			return;
		}
		rcs = rcs == null ? new Resources() : rcs;
		rcs.add(rc);
	}

	/**
	 * add an "And" selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addAnd(final AndSelector selector) {
		usedMatchingTask = true;
		super.addAnd(selector);
	}

	/**
	 * add a contains selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addContains(final ContainsSelector selector) {
		usedMatchingTask = true;
		super.addContains(selector);
	}

	/**
	 * add a regular expression selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addContainsRegexp(final ContainsRegexpSelector selector) {
		usedMatchingTask = true;
		super.addContainsRegexp(selector);
	}

	/**
	 * add an extended selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addCustom(final ExtendSelector selector) {
		usedMatchingTask = true;
		super.addCustom(selector);
	}

	/**
	 * add a selector date entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addDate(final DateSelector selector) {
		usedMatchingTask = true;
		super.addDate(selector);
	}

	/**
	 * add a depends selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addDepend(final DependSelector selector) {
		usedMatchingTask = true;
		super.addDepend(selector);
	}

	/**
	 * add a depth selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addDepth(final DepthSelector selector) {
		usedMatchingTask = true;
		super.addDepth(selector);
	}

	/**
	 * add a selector filename entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addFilename(final FilenameSelector selector) {
		usedMatchingTask = true;
		super.addFilename(selector);
	}

	/**
	 * Adds a set of files to be deleted.
	 *
	 * @param set
	 *            the set of files to be deleted
	 */
	public void addFileset(final FileSet set) {
		filesets.add(set);
	}

	/**
	 * add a majority selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addMajority(final MajoritySelector selector) {
		usedMatchingTask = true;
		super.addMajority(selector);
	}

	/**
	 * add the modified selector
	 *
	 * @param selector
	 *            the selector to add
	 * @since ant 1.6
	 */
	@Override
	public void addModified(final ModifiedSelector selector) {
		usedMatchingTask = true;
		super.addModified(selector);
	}

	/**
	 * add a "None" selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addNone(final NoneSelector selector) {
		usedMatchingTask = true;
		super.addNone(selector);
	}

	/**
	 * add a "Not" selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addNot(final NotSelector selector) {
		usedMatchingTask = true;
		super.addNot(selector);
	}

	/**
	 * add an "Or" selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addOr(final OrSelector selector) {
		usedMatchingTask = true;
		super.addOr(selector);
	}

	/**
	 * add a present selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addPresent(final PresentSelector selector) {
		usedMatchingTask = true;
		super.addPresent(selector);
	}

	/**
	 * add a "Select" selector entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addSelector(final SelectSelector selector) {
		usedMatchingTask = true;
		super.addSelector(selector);
	}

	/**
	 * add a selector size entry on the selector list
	 *
	 * @param selector
	 *            the selector to be added
	 */
	@Override
	public void addSize(final SizeSelector selector) {
		usedMatchingTask = true;
		super.addSize(selector);
	}

	/**
	 * add a name entry on the exclude list
	 *
	 * @return an NameEntry object to be configured
	 */
	@Override
	public PatternSet.NameEntry createExclude() {
		usedMatchingTask = true;
		return super.createExclude();
	}

	/**
	 * add a name entry on the include files list
	 *
	 * @return an NameEntry object to be configured
	 */
	@Override
	public PatternSet.NameEntry createExcludesFile() {
		usedMatchingTask = true;
		return super.createExcludesFile();
	}

	/**
	 * add a name entry on the include list
	 *
	 * @return a NameEntry object to be configured
	 */
	@Override
	public PatternSet.NameEntry createInclude() {
		usedMatchingTask = true;
		return super.createInclude();
	}

	/**
	 * add a name entry on the include files list
	 *
	 * @return an NameEntry object to be configured
	 */
	@Override
	public PatternSet.NameEntry createIncludesFile() {
		usedMatchingTask = true;
		return super.createIncludesFile();
	}

	/**
	 * add a set of patterns
	 *
	 * @return PatternSet object to be configured
	 */
	@Override
	public PatternSet createPatternSet() {
		usedMatchingTask = true;
		return super.createPatternSet();
	}

	/**
	 * Accommodate Windows bug encountered in both Sun and IBM JDKs. Others possible. If the delete does not work, call
	 * System.gc(), wait a little and try again.
	 */
	/*
	 * CHANGEMENT DE LA PORTEE de private en protected
	 */
	protected boolean delete(final File f) {
		if (!f.delete()) {			
			try {
				Thread.sleep(DELETE_RETRY_SLEEP_MILLIS);
			} catch (final InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (!f.delete()) {
				if (deleteOnExit) {
					final int level = quiet ? Project.MSG_VERBOSE : Project.MSG_INFO;
					log("Failed to delete " + f + ", calling deleteOnExit."
							+ " This attempts to delete the file when the Ant jvm"
							+ " has exited and might not succeed.", level);
					f.deleteOnExit();
					return true;
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * Delete the file(s).
	 *
	 * @exception BuildException
	 *                if an error occurs
	 */
	@Override
	public void execute() {
		if (usedMatchingTask) {
			log("DEPRECATED - Use of the implicit FileSet is deprecated.  "
					+ "Use a nested fileset element instead.", quiet ? Project.MSG_VERBOSE : verbosity);
		}

		if (file == null && dir == null && filesets.isEmpty() && rcs == null) {
			throw new BuildException("At least one of the file or dir "
					+ "attributes, or a nested resource collection, "
					+ "must be set.");
		}

		if (quiet && failonerror) {
			throw new BuildException("quiet and failonerror cannot both be "
					+ "set to true", getLocation());
		}

		// delete the single file
		if (file != null) {
			if (file.exists()) {
				if (file.isDirectory()) {
					log("Directory " + file.getAbsolutePath()
							+ " cannot be removed using the file attribute.  "
							+ "Use dir instead.", quiet ? Project.MSG_VERBOSE : verbosity);
				} else {
					log("Deleting: " + file.getAbsolutePath());

					if (!delete(file)) {
						handle("Unable to delete file " + file.getAbsolutePath());
					}
				}
			} else {
				log("Could not find file " + file.getAbsolutePath()
						+ " to delete.", quiet ? Project.MSG_VERBOSE : verbosity);
			}
		}

		// delete the directory
		if (dir != null && dir.exists() && dir.isDirectory()
				&& !usedMatchingTask) {
			/*
			 * If verbosity is MSG_VERBOSE, that mean we are doing regular logging (backwards as that sounds). In that
			 * case, we want to print one message about deleting the top of the directory tree. Otherwise, the removeDir
			 * method will handle messages for _all_ directories.
			 */
			if (verbosity == Project.MSG_VERBOSE) {
				log("Deleting directory " + dir.getAbsolutePath());
			}
			removeDir(dir);
		}
		final Resources resourcesToDelete = new Resources();
		resourcesToDelete.setProject(getProject());
		final Resources filesetDirs = new Resources();
		filesetDirs.setProject(getProject());
		FileSet implicit = null;
		if (usedMatchingTask && dir != null && dir.isDirectory()) {
			// add the files from the default fileset:
			implicit = getImplicitFileSet();
			implicit.setProject(getProject());
			filesets.add(implicit);
		}

		for (final FileSet fileset2 : filesets) {
			FileSet fs = fileset2;
			if (fs.getProject() == null) {
				log("Deleting fileset with no project specified;"
						+ " assuming executing project", Project.MSG_VERBOSE);
				fs = (FileSet) fs.clone();
				fs.setProject(getProject());
			}
			if (!fs.getDir().isDirectory()) {
				handle("Directory does not exist:" + fs.getDir());
			} else {
				resourcesToDelete.add(fs);
				if (includeEmpty) {
					filesetDirs.add(new ReverseDirs(fs.getDir(), fs
							.getDirectoryScanner().getIncludedDirectories()));
				}
			}
		}
		resourcesToDelete.add(filesetDirs);
		if (rcs != null) {
			// sort first to files, then dirs
			final Restrict exists = new Restrict();
			exists.add(EXISTS);
			exists.add(rcs);
			final Sort s = new Sort();
			s.add(REVERSE_FILESYSTEM);
			s.add(exists);
			resourcesToDelete.add(s);
		}
		try {
			if (resourcesToDelete.isFilesystemOnly()) {
				for (final Object element : resourcesToDelete) {
					final FileResource r = (FileResource) element;
					// nonexistent resources could only occur if we already
					// deleted something from a fileset:
					if (!r.isExists()) {
						continue;
					}
					if (!r.isDirectory() || r.getFile().list().length == 0) {
						log("Deleting " + r, verbosity);
						if (!delete(r.getFile()) && failonerror) {
							handle("Unable to delete "
									+ (r.isDirectory() ? "directory " : "file ") + r);
						}
					}
				}
			} else {
				handle(getTaskName() + " handles only filesystem resources");
			}
		} catch (final Exception e) {
			handle(e);
		} finally {
			if (implicit != null) {
				filesets.remove(implicit);
			}
		}
	}

	private void handle(final Exception e) {
		if (failonerror) {
			throw e instanceof BuildException
					? (BuildException) e
					: new BuildException(e);
		}
		log(e, quiet ? Project.MSG_VERBOSE : verbosity);
	}

	private void handle(final String msg) {
		handle(new BuildException(msg));
	}

	public boolean isDeleteOnExit() {
		return deleteOnExit;
	}

	public boolean isQuiet() {
		return quiet;
	}

	/**
	 * Delete a directory
	 *
	 * @param d
	 *            the directory to delete
	 */
	protected void removeDir(final File d) {
		String[] list = d.list();
		if (list == null) {
			list = new String[0];
		}
		for (final String s : list) {
			final File f = new File(d, s);
			if (f.isDirectory()) {
				removeDir(f);
			} else {
				log("Deleting " + f.getAbsolutePath(), quiet ? Project.MSG_VERBOSE : verbosity);
				if (!delete(f)) {
					handle("Unable to delete file " + f.getAbsolutePath());
				}
			}
		}
		log("Deleting directory " + d.getAbsolutePath(), verbosity);
		if (!delete(d)) {
			handle("Unable to delete directory " + dir.getAbsolutePath());
		}
	}

	/**
	 * remove an array of files in a directory, and a list of subdirectories which will only be deleted if
	 * 'includeEmpty' is true
	 *
	 * @param d
	 *            directory to work from
	 * @param files
	 *            array of files to delete; can be of zero length
	 * @param dirs
	 *            array of directories to delete; can of zero length
	 */
	protected void removeFiles(final File d, final String[] files, final String[] dirs) {
		if (files.length > 0) {
			log("Deleting " + files.length + " files from "
					+ d.getAbsolutePath(), quiet ? Project.MSG_VERBOSE : verbosity);
			for (final String file2 : files) {
				final File f = new File(d, file2);
				log("Deleting " + f.getAbsolutePath(),
						quiet ? Project.MSG_VERBOSE : verbosity);
				if (!delete(f)) {
					handle("Unable to delete file " + f.getAbsolutePath());
				}
			}
		}

		if (dirs.length > 0 && includeEmpty) {
			int dirCount = 0;
			for (int j = dirs.length - 1; j >= 0; j--) {
				final File currDir = new File(d, dirs[j]);
				final String[] dirFiles = currDir.list();
				if (dirFiles == null || dirFiles.length == 0) {
					log("Deleting " + currDir.getAbsolutePath(),
							quiet ? Project.MSG_VERBOSE : verbosity);
					if (!delete(currDir)) {
						handle("Unable to delete directory "
								+ currDir.getAbsolutePath());
					} else {
						dirCount++;
					}
				}
			}

			if (dirCount > 0) {
				log("Deleted "
						+ dirCount
						+ " director" + (dirCount == 1 ? "y" : "ies")
						+ " form " + d.getAbsolutePath(),
						quiet ? Project.MSG_VERBOSE : verbosity);
			}
		}
	}

	/**
	 * Sets case sensitivity of the file system
	 *
	 * @param isCaseSensitive
	 *            "true"|"on"|"yes" if file system is case sensitive, "false"|"off"|"no" when not.
	 */
	@Override
	public void setCaseSensitive(final boolean isCaseSensitive) {
		usedMatchingTask = true;
		super.setCaseSensitive(isCaseSensitive);
	}

	/**
	 * Sets whether default exclusions should be used or not.
	 *
	 * @param useDefaultExcludes
	 *            "true"|"on"|"yes" when default exclusions should be used, "false"|"off"|"no" when they shouldn't be
	 *            used.
	 */
	@Override
	public void setDefaultexcludes(final boolean useDefaultExcludes) {
		usedMatchingTask = true;
		super.setDefaultexcludes(useDefaultExcludes);
	}

	/**
	 * If true, on failure to delete, note the error and set the deleteonexit flag, and continue
	 *
	 * @param deleteOnExit
	 *            true or false
	 */
	public void setDeleteOnExit(final boolean deleteOnExit) {
		this.deleteOnExit = deleteOnExit;
	}

	/**
	 * Set the directory from which files are to be deleted
	 *
	 * @param dir
	 *            the directory path.
	 */
	public void setDir(final File dir) {
		this.dir = dir;
		getImplicitFileSet().setDir(dir);
	}

	/**
	 * Sets the set of exclude patterns. Patterns may be separated by a comma or a space.
	 *
	 * @param excludes
	 *            the string containing the exclude patterns
	 */
	@Override
	public void setExcludes(final String excludes) {
		usedMatchingTask = true;
		super.setExcludes(excludes);
	}

	/**
	 * Sets the name of the file containing the includes patterns.
	 *
	 * @param excludesfile
	 *            A string containing the filename to fetch the include patterns from.
	 */
	@Override
	public void setExcludesfile(final File excludesfile) {
		usedMatchingTask = true;
		super.setExcludesfile(excludesfile);
	}

	// ************************************************************************
	// protected and private methods
	// ************************************************************************

	/**
	 * If false, note errors but continue.
	 *
	 * @param failonerror
	 *            true or false
	 */
	public void setFailOnError(final boolean failonerror) {
		this.failonerror = failonerror;
	}

	/**
	 * Set the name of a single file to be removed.
	 *
	 * @param file
	 *            the file to be deleted
	 */
	public void setFile(final File file) {
		this.file = file;
	}

	/**
	 * Sets whether or not symbolic links should be followed.
	 *
	 * @param followSymlinks
	 *            whether or not symbolic links should be followed
	 */
	@Override
	public void setFollowSymlinks(final boolean followSymlinks) {
		usedMatchingTask = true;
		super.setFollowSymlinks(followSymlinks);
	}

	/**
	 * If true, delete empty directories.
	 *
	 * @param includeEmpty
	 *            if true delete empty directories (only for filesets). Default is false.
	 */
	public void setIncludeEmptyDirs(final boolean includeEmpty) {
		this.includeEmpty = includeEmpty;
	}

	/**
	 * Sets the set of include patterns. Patterns may be separated by a comma or a space.
	 *
	 * @param includes
	 *            the string containing the include patterns
	 */
	@Override
	public void setIncludes(final String includes) {
		usedMatchingTask = true;
		super.setIncludes(includes);
	}

	/**
	 * Sets the name of the file containing the includes patterns.
	 *
	 * @param includesfile
	 *            A string containing the filename to fetch the include patterns from.
	 */
	@Override
	public void setIncludesfile(final File includesfile) {
		usedMatchingTask = true;
		super.setIncludesfile(includesfile);
	}

	/**
	 * If true and the file does not exist, do not display a diagnostic message or modify the exit status to reflect an
	 * error. This means that if a file or directory cannot be deleted, then no error is reported. This setting emulates
	 * the -f option to the Unix &quot;rm&quot; command. Default is false meaning things are &quot;noisy&quot;
	 *
	 * @param quiet
	 *            "true" or "on"
	 */
	public void setQuiet(final boolean quiet) {
		this.quiet = quiet;
		if (quiet) {
			failonerror = false;
		}
	}

	/**
	 * If true, list all names of deleted files.
	 *
	 * @param verbose
	 *            "true" or "on"
	 */
	public void setVerbose(final boolean verbose) {
		if (verbose) {
			verbosity = Project.MSG_INFO;
		} else {
			verbosity = Project.MSG_VERBOSE;
		}
	}

}
