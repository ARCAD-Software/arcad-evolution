package com.arcadsoftware.mmk.anttasks.taskdefs.lists;



import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.AntFactory;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.EListConstants;


public abstract class AbstractXmlFileListTask extends AbstractListTask {
	protected AbstractXmlList list;
	private String filename;	
	protected String listType = EListConstants.LST_TYPE_GENERIC.getValue();
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if ((filename==null) || (filename.equals(""))) {		
			throw new BuildException("Filename attribute must be set!");
		} else {
			File f = new File(filename);
			if (!f.exists()) {
				throw new BuildException("List File Not Found!");
			}		
		}		
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#doExecute()
	 */
	@Override
	public void doExecute() {
  
		list = (AbstractXmlList)AntFactory.getInstance().getBean(listType);
    	list.setXmlFileName(getFilename());	
    	processExecution();
	}

	public void updateHeaderInfo(String description,String comment){
        if (!list.getCashManager().isActive())
        	list.load(false,false);

		if (description!=null) {
			list.getHeader().setDescription(description);
		}
		if (comment!=null) {
			list.getHeader().setComment(comment);
		}		
		list.getCashManager().flushRequest();
	}
	
	
	
	public abstract void processExecution() ;

	/**
	 * Renvoit 
	 * @return the filename String : 
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @param listType the listType to set
	 */
	public void setListType(String listType) {
		this.listType = listType;
	}

	
	

}
