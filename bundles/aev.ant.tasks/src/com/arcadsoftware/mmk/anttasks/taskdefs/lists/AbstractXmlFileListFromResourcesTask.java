package com.arcadsoftware.mmk.anttasks.taskdefs.lists;


import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import com.arcadsoftware.mmk.lists.EListConstants;

public abstract class AbstractXmlFileListFromResourcesTask 
extends AbstractXmlFileListCountedTask {
	protected ResourceResolverHelper helper;
	protected Vector rcs = new Vector();  


	protected ArrayList<String> files;
	protected String fromListFileName=null;	

	
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

	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		if ((getFilename()==null) || (getFilename().equals(""))) {		
			throw new BuildException("Filename attribute must be set!");
		}	
		if ((fromListFileName!=null) && !fromListFileName.equals("")){
			File f2 = new File(fromListFileName);
			if (!f2.exists()) {
				throw new BuildException("FromList File not found!");
			}			
		}			
	}  	        
    
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListWithProcessTask#processExecutionWithCount()
	 */
	@Override
	public int processExecutionWithCount() {
		//Recherche des ‚l‚ments … traiter
		helper = new ResourceResolverHelper();
		helper.getElements(getProject(),rcs);
		files = helper.getFileList();	
		//D‚claration de la liste        
    	int count = process();	
    	return count;
	}

	public abstract int process();


	/**
	 * @param fromList the fromList to set
	 */
	public void setFromListFileName(String fromListFileName) {
		this.fromListFileName = fromListFileName;
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#doBeforeExecuting()
	 */
	@Override
	protected void doBeforeExecuting() {
		super.doBeforeExecuting();
		listType = EListConstants.LST_TYPE_FILE.getValue();
	}	
	
	
    

}
