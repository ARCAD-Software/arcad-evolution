package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;

import com.arcadsoftware.ae.core.utils.Utils;

public class ResourceResolverHelper {
	   static final File NULL_FILE_PLACEHOLDER = new File("/NULL_FILE");
		
		
	    protected Vector rcs;
	    protected ArrayList<String> fileList = new ArrayList<String>();
	    protected ArrayList<String> dirCopyMap = new ArrayList<String>();    
	    
	    /**
	     * Either returns its argument or a plaeholder if the argument is null.
	     */
	    private static File getKeyFile(File f) {
	        return f == null ? NULL_FILE_PLACEHOLDER : f;
	    }
	    
	    /**
	     * Adds the given strings to a list contained in the given map.
	     * The file is the key into the map.
	     */
	    private static void add(File baseDir, String[] names, Map m) {
	        if (names != null) {
	            baseDir = getKeyFile(baseDir);
	            List l = (List) m.get(baseDir);
	            if (l == null) {
	                l = new ArrayList(names.length);
	                m.put(baseDir, l);
	            }
	            l.addAll(java.util.Arrays.asList(names));
	        }
	    }

	    /**
	     * Adds the given string to a list contained in the given map.
	     * The file is the key into the map.
	     */
	    private static void add(File baseDir, String name, Map m) {
	        if (name != null) {
	            add(baseDir, new String[] {name}, m);
	        }
	    }    
	    
	    /**
	     * Add a set of files to copy.
	     * @param set a set of files to copy.
	     */
	    public void addFileset(FileSet set) {
	        add(set);
	    }

	    /**
	     * Add a collection of files to copy.
	     * @param res a resource collection to copy.
	     * @since Ant 1.7
	     */
	    public void add(ResourceCollection res) {
	        rcs.add(res);
	    }
	    
	    protected boolean supportsNonFileResources() {
	        return false;
	    }
	    
	    /**
	     * Compares source files to destination files to see if they should be
	     * copied.
	     *
	     * @param fromDir  The source directory.
	     * @param toDir    The destination directory.
	     * @param files    A list of files to copy.
	     * @param dirs     A list of directories to copy.
	     */
	    protected void scan(File fromDir, String[] files,
	                        String[] dirs) {
	        //FileNameMapper mapper = getMapper();
	    	FileNameMapper mapper = new IdentityMapper();
	        buildMap(fromDir, fromDir, files, mapper, fileList);
	        buildMap(fromDir, fromDir, dirs, mapper, dirCopyMap);
	    }

	    /**
	     * Add to a map of files/directories to copy.
	     *
	     * @param fromDir the source directory.
	     * @param toDir   the destination directory.
	     * @param names   a list of filenames.
	     * @param mapper  a <code>FileNameMapper</code> value.
	     * @param map     a map of source file to array of destination files.
	     */
	    protected void buildMap(File fromDir, File toDir, String[] names,
	                            FileNameMapper mapper, ArrayList<String> map) {
	        for (int i = 0; i < names.length; i++) {
	        	File src = new File(fromDir, names[i]);
	        	map.add(src.getAbsolutePath());
	        }
	    }


	    public void getElements(Project project,Vector rcs){
	    	this.rcs = rcs;
	        HashMap filesByBasedir = new HashMap();
	        HashMap dirsByBasedir = new HashMap();
	        HashSet baseDirs = new HashSet();
	        ArrayList nonFileResources = new ArrayList();
	        for (int i = 0; i < rcs.size(); i++) {
	            ResourceCollection rc = (ResourceCollection) rcs.elementAt(i);
	            // Step (1) - beware of the ZipFileSet
	            if (rc instanceof FileSet && rc.isFilesystemOnly()) {
	                FileSet fs = (FileSet) rc;
	                DirectoryScanner ds = null;
	                try {
	                    ds = fs.getDirectoryScanner(project);
	                } catch (BuildException e) {
	                	System.out.println(e.getLocalizedMessage());
	                	System.out.println(Utils.stackTrace(e));
//	                    if (failonerror
//	                        || !getMessage(e).endsWith(" not found.")) {
//	                        throw e;
//	                    } else {
//	                        log("Warning: " + getMessage(e), Project.MSG_ERR);
//	                        continue;
//	                    }
	                }
	                File fromDir = fs.getDir(project);

	                String[] srcFiles = ds.getIncludedFiles();
	                String[] srcDirs = ds.getIncludedDirectories();
	                add(fromDir, srcFiles, filesByBasedir);
	                add(fromDir, srcDirs, dirsByBasedir);
	                baseDirs.add(fromDir);
	            } else { // not a fileset or contains non-file resources

	                if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
	                    throw new BuildException(
	                               "Only FileSystem resources are supported.");
	                }

	                Iterator resources = rc.iterator();
	                while (resources.hasNext()) {
	                    Resource r = (Resource) resources.next();
	                    if (!r.isExists()) {
	                        continue;
	                    }

	                    File baseDir = NULL_FILE_PLACEHOLDER;
	                    String name = r.getName();
	                    if (r instanceof FileResource) {
	                        FileResource fr = (FileResource) r;
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

	        Iterator iter = baseDirs.iterator();
	        while (iter.hasNext()) {
	            File f = (File) iter.next();
	            List files = (List) filesByBasedir.get(f);
	            List dirs = (List) dirsByBasedir.get(f);

	            String[] srcFiles = new String[0];
	            if (files != null) {
	                srcFiles = (String[]) files.toArray(srcFiles);
	            }
	            String[] srcDirs = new String[0];
	            if (dirs != null) {
	                srcDirs = (String[]) dirs.toArray(srcDirs);
	            }
	            scan(f == NULL_FILE_PLACEHOLDER ? null : f, srcFiles,srcDirs);
	        }

	        DirectoryScanner ds = new DirectoryScanner();
	    	for(int i=0;i<dirCopyMap.size();i++) {
	    		String toFile = dirCopyMap.get(i);    		
	    		File dirToScan = new File(toFile); 
	    		ds.setBasedir(dirToScan);
	    		ds.scan();
	    		String[] fileToAdd = ds.getIncludedFiles();
	    		for (int k=0;k<fileToAdd.length;k++){
	    			File f = new File(dirToScan,fileToAdd[k]);
	    			if (fileList.indexOf(f.getAbsolutePath())==-1) {
	    				fileList.add(f.getAbsolutePath());
	    			}
	    		}
	    		
	    	}        	
	    }

		/**
		 * Renvoit 
		 * @return the fileList ArrayList<String> : 
		 */
		public ArrayList<String> getFileList() {
			return fileList;
		}
}
