package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;



import java.text.SimpleDateFormat;


import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;

public class ListInfoTask extends AbstractXmlFileListTask {
	 
	private static final String OPTS_ALL = "all";	
	private static final String OPTS_KEYONLY = "keyonly";	
	
	private boolean showHeader = true;
	private boolean showMetadata = true;
	private String  metaOptions = OPTS_ALL;	
	
	
	private boolean showProcessInfo = true;	
	
	
	private void printMetadata(AbstractXmlList list){
		ListMetaDatas md = list.getMetadatas();
		StringBuffer info = new StringBuffer();
		info.append("*** Metadata ***").append("\n");
		for (int i=0;i<md.count();i++) {
			ListColumnDef cd = md.getColumnDefAt(i);			
			boolean keep = (metaOptions.equals(OPTS_KEYONLY) && cd.isKey()) ||
			metaOptions.equals(OPTS_ALL);
			if (keep) {
				info.append("-> column ").append(i).append(" ***").append("\n");
				info.append("id=").append(cd.getId()).append("\n");
				info.append("property=").append(cd.getPropertyName()).append("\n");
				if (cd.isKey())
					info.append("key=true").append("\n");
				else
					info.append("key=false").append("\n");
			}
		}		
		if (getProject()!=null)
			getProject().log(this,info.toString(),2);		
	}
	
	private void printHeader(AbstractXmlList list){
		SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		StringBuffer info = new StringBuffer();
		info.append("*** header ***").append("\n");
		info.append("description=").append(list.getHeader().getDescription()).append("\n");
		info.append("comment=").append(list.getHeader().getComment()).append("\n");		
		info.append("createdThe=").append(fd.format(list.getHeader().getCreatedThe())).append("\n");
		info.append("createdBy=").append(list.getHeader().getCreatedBy()).append("\n");
		info.append("lastModifiedThe=").append(fd.format(list.getHeader().getLastModifiedThe())).append("\n");
		info.append("lastModifiedBy=").append(list.getHeader().getLastModifiedBy()).append("\n");
		if (getProject()!=null)
			getProject().log(this,info.toString(),2);		
	}	
	private void printProcessInformation(AbstractXmlList list){
		StringBuffer info = new StringBuffer();
		//info.append("->Affichage des informations de traitement");
		info.append("*** Process information ***").append("\n");		
		info.append("elementCount=").append(list.getElementCount()).append("\n");
		info.append("succeedElementCount=").append(list.getSucceedElementCount()).append("\n");
		info.append("failedElementCount=").append(list.getFailedElementCount()).append("\n");
		info.append("processedElementCount=").append(list.getProcessedElementCount()).append("\n");
		if (getProject()!=null)
			getProject().log(this,info.toString(),2);		
	}		
	

	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractListTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (showMetadata) {
			if ((metaOptions==null) || (metaOptions.equals(""))) {
				metaOptions=OPTS_ALL;
			} else {
				if ( !metaOptions.trim().toLowerCase().equals(OPTS_ALL) &&
					 !metaOptions.trim().toLowerCase().equals(OPTS_KEYONLY)
				)			
					throw new BuildException("Invalid value for metaOptions attribute!");		
			}			
		}
	}

	@Override
	public void processExecution() throws BuildException {		
		list.load(showProcessInfo,true);		
		if (showHeader)
			printHeader(list);
		if (showMetadata)
			printMetadata(list);
		if (showProcessInfo)
			printProcessInformation(list);
		
	}	
	
	
	/**
	 * @param showHeader the showHeader to set
	 */
	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	/**
	 * @param showMetadata the showMetadata to set
	 */
	public void setShowMetadata(boolean showMetadata) {
		this.showMetadata = showMetadata;
	}

	/**
	 * @param showProcessInfo the showProcessInfo to set
	 */
	public void setShowProcessInfo(boolean showProcessInfo) {
		this.showProcessInfo = showProcessInfo;
	}

	/**
	 * @param metadataOptions the metadataOptions to set
	 */
	public void setMetaOptions(String metaOptions) {
		this.metaOptions = metaOptions;
	}



}
