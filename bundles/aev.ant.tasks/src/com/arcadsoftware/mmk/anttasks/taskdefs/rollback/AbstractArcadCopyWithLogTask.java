package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;

import com.arcadsoftware.serviceprovider.transactionlogger.logger.ArcadPCTransactionLogger;
import com.arcadsoftware.serviceprovider.transactionlogger.logger.ArcadTransactionLogger;



public abstract class AbstractArcadCopyWithLogTask extends AbstractArcadCopyTask {
	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	protected boolean copyWithLog = false;
	protected ArcadTransactionLogger logger;
	protected ArcadPCTransactionLogger h2Logger;
	protected String host= "";
	protected String user= "";
	protected String pwd= "";
	protected String instance = "AD";
	protected String port= "8230";
	protected String localUser= "";
	protected String localPwd= "";
	protected String localPort= "";
	

	   /**
     * Actually does the file (and possibly empty directory) copies.
     * This is a good method for subclasses to override.
     */
    protected void doFileOperations() {
        if (fileCopyMap.size() > 0) {
            log("Copying " + fileCopyMap.size()
                + " file" + (fileCopyMap.size() == 1 ? "" : "s")
                + " to " + destDir.getAbsolutePath());

    		if (copyWithLog) {
    			logger = new ArcadTransactionLogger();
    			logger.setServer(host);
    			logger.setUser(user);
    			//logger.setPassword(Base64.encode(pwd.getBytes())); // password is coded Base64
    			//logger.setPort(port);
    			log("local password : " + localPwd);
    			logger.setPassword(pwd);
    			logger.setInstance(instance);
    	        String logPath = "${arcad.path.home}/logs/logSP.log";
    	        String dbLocation = "file:${arcad.path.home}/exec_agent/data/transaction_offline";
    	        String dbUser = "sa";
    	        String dbPwd = "";
    	        String dbFilterDateFormat = "yyyy-MM-dd HH:mm";
    	        h2Logger = new ArcadPCTransactionLogger(logPath, dbLocation, dbUser, dbPwd, dbFilterDateFormat);
    		}
            
            Enumeration e = fileCopyMap.keys();
            while (e.hasMoreElements()) {
                String fromFile = (String) e.nextElement();
                String[] toFiles = (String[]) fileCopyMap.get(fromFile);

                for (int i = 0; i < toFiles.length; i++) {
                    String toFile = toFiles[i];

                    if (fromFile.equals(toFile)) {
                        log("Skipping self-copy of " + fromFile, verbosity);
                        continue;
                    }
                    try {
                        log("Copying " + fromFile + " to " + toFile, verbosity);

                        FilterSetCollection executionFilters =
                            new FilterSetCollection();
                        if (filtering) {
                            executionFilters
                                .addFilterSet(getProject().getGlobalFilterSet());
                        }
                        //original code -> for (Enumeration filterEnum = filterSets.elements();
                        for (Enumeration filterEnum = getFilterSets().elements();                        
                            filterEnum.hasMoreElements();) {
                            executionFilters
                                .addFilterSet((FilterSet) filterEnum.nextElement());
                        }
                        	log("Logging " 
                                + " file " + fromFile);
                        	doBeforeCopying(fromFile,toFile,forceOverwrite);
             
                        /*
	                        <original>
	                        fileUtils.copyFile(fromFile, toFile, executionFilters,
	                                filterChains, forceOverwrite,
	                                preserveLastModified, inputEncoding,
	                                outputEncoding, getProject());
	                        </original>
                        */
                        	
                        //<newcode>
                        fileUtils.copyFile(fromFile, toFile, executionFilters,
                                getFilterChains(), forceOverwrite,
                                preserveLastModified, getEncoding(),
                                getOutputEncoding(), getProject());
                        //</newcode>
                        
                        if (copyWithLog) {
                        	doAfterCopying(fromFile,toFile); 
                        }
                        
                    } catch (IOException ioe) {
                        String msg = "Failed to copy " + fromFile + " to " + toFile
                            + " due to " + getDueTo(ioe);
                        File targetFile = new File(toFile);
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
            h2Logger.destroy();
        }
        if (includeEmpty) {
            Enumeration e = dirCopyMap.elements();
            int createCount = 0;
            while (e.hasMoreElements()) {
                String[] dirs = (String[]) e.nextElement();
                for (int i = 0; i < dirs.length; i++) {
                    File d = new File(dirs[i]);
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
    
    public abstract void doAfterCopying(String fromFile,String toFile);
    
}
