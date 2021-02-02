package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;

public class ResourceResolverHelper {
	static final File NULL_FILE_PLACEHOLDER = new File("NULL_FILE");

	/**
	 * Adds the given string to a list contained in the given map. The file is the key into the map.
	 */
	private static void add(final File baseDir, final String name, final Map<File, List<String>> m) {
		if (name != null) {
			add(baseDir, new String[] { name }, m);
		}
	}

	/**
	 * Adds the given strings to a list contained in the given map. The file is the key into the map.
	 */
	private static void add(File baseDir, final String[] names, final Map<File, List<String>> m) {
		if (names != null) {
			baseDir = getKeyFile(baseDir);
			final List<String> l = m.computeIfAbsent(baseDir, f -> new ArrayList<>());
			l.addAll(java.util.Arrays.asList(names));
		}
	}

	/**
	 * Either returns its argument or a plaeholder if the argument is null.
	 */
	private static File getKeyFile(final File f) {
		return f == null ? NULL_FILE_PLACEHOLDER : f;
	}

	protected List<String> dirCopyMap = new ArrayList<>();

	protected List<String> fileList = new ArrayList<>();

	protected List<ResourceCollection> rcs;

	/**
	 * Add a collection of files to copy.
	 *
	 * @param res
	 *            a resource collection to copy.
	 * @since Ant 1.7
	 */
	public void add(final ResourceCollection res) {
		rcs.add(res);
	}

	/**
	 * Add a set of files to copy.
	 *
	 * @param set
	 *            a set of files to copy.
	 */
	public void addFileset(final FileSet set) {
		add(set);
	}

	/**
	 * Add to a map of files/directories to copy.
	 *
	 * @param fromDir
	 *            the source directory.
	 * @param toDir
	 *            the destination directory.
	 * @param names
	 *            a list of filenames.
	 * @param mapper
	 *            a <code>FileNameMapper</code> value.
	 * @param map
	 *            a map of source file to array of destination files.
	 */
	protected void buildMap(final File fromDir, final File toDir, final String[] names,
			final FileNameMapper mapper, final List<String> map) {
		for (final String name : names) {
			final File src = new File(fromDir, name);
			map.add(src.getAbsolutePath());
		}
	}

	public void getElements(final Project project, final List<ResourceCollection> rcs) {
		this.rcs = rcs;
		final Map<File, List<String>> filesByBasedir = new HashMap<>();
		final Map<File, List<String>> dirsByBasedir = new HashMap<>();
		final Set<File> baseDirs = new HashSet<>();
		final List<Resource> nonFileResources = new ArrayList<>();
		for (final ResourceCollection rc : rcs) {
			// Step (1) - beware of the ZipFileSet
			if (rc instanceof FileSet && rc.isFilesystemOnly()) {
				final FileSet fs = (FileSet) rc;
				DirectoryScanner ds = null;
				ds = fs.getDirectoryScanner(project);
				final File fromDir = fs.getDir(project);

				final String[] srcFiles = ds.getIncludedFiles();
				final String[] srcDirs = ds.getIncludedDirectories();
				add(fromDir, srcFiles, filesByBasedir);
				add(fromDir, srcDirs, dirsByBasedir);
				baseDirs.add(fromDir);
			} else { // not a fileset or contains non-file resources

				if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
					throw new BuildException(
							"Only FileSystem resources are supported.");
				}

				final Iterator<Resource> resources = rc.iterator();
				while (resources.hasNext()) {
					final Resource r = resources.next();
					if (!r.isExists()) {
						continue;
					}

					File baseDir = NULL_FILE_PLACEHOLDER;
					String name = r.getName();
					if (r instanceof FileResource) {
						final FileResource fr = (FileResource) r;
						baseDir = getKeyFile(fr.getBaseDir());
						if (fr.getBaseDir() == null) {
							name = fr.getFile().getAbsolutePath();
						}
					}

					// copying of dirs is trivial and can be done
					// for non-file resources as well as for real
					// files.
					if (r.isDirectory() || r instanceof FileResource) {
						add(baseDir, name,
								r.isDirectory() ? dirsByBasedir
										: filesByBasedir);
						baseDirs.add(baseDir);
					} else { // a not-directory file resource
						// needs special treatment
						nonFileResources.add(r);
					}
				}
			}
		}

		final Iterator<File> iter = baseDirs.iterator();
		while (iter.hasNext()) {
			final File f = iter.next();
			final List<String> files = filesByBasedir.get(f);
			final List<String> dirs = dirsByBasedir.get(f);

			String[] srcFiles = new String[0];
			if (files != null) {
				srcFiles = files.toArray(srcFiles);
			}
			String[] srcDirs = new String[0];
			if (dirs != null) {
				srcDirs = dirs.toArray(srcDirs);
			}
			scan(f == NULL_FILE_PLACEHOLDER ? null : f, srcFiles, srcDirs);
		}

		final DirectoryScanner ds = new DirectoryScanner();
		for (int i = 0; i < dirCopyMap.size(); i++) {
			final String toFile = dirCopyMap.get(i);
			final File dirToScan = new File(toFile);
			ds.setBasedir(dirToScan);
			ds.scan();
			final String[] fileToAdd = ds.getIncludedFiles();
			for (final String element : fileToAdd) {
				final File f = new File(dirToScan, element);
				if (fileList.indexOf(f.getAbsolutePath()) == -1) {
					fileList.add(f.getAbsolutePath());
				}
			}

		}
	}

	/**
	 * Renvoit
	 *
	 * @return the fileList ArrayList<String> :
	 */
	public List<String> getFileList() {
		return fileList;
	}

	/**
	 * Compares source files to destination files to see if they should be copied.
	 *
	 * @param fromDir
	 *            The source directory.
	 * @param toDir
	 *            The destination directory.
	 * @param files
	 *            A list of files to copy.
	 * @param dirs
	 *            A list of directories to copy.
	 */
	protected void scan(final File fromDir, final String[] files,
			final String[] dirs) {
		final FileNameMapper mapper = new IdentityMapper();
		buildMap(fromDir, fromDir, files, mapper, fileList);
		buildMap(fromDir, fromDir, dirs, mapper, dirCopyMap);
	}

	protected boolean supportsNonFileResources() {
		return false;
	}
}
