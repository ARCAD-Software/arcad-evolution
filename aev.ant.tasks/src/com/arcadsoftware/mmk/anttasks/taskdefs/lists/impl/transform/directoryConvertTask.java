package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform;

import org.apache.tools.ant.BuildException;




public class directoryConvertTask extends AbstractListItemTransformerTask {

	public String originDirectory = null;
	public String targetDirectory = null;
	


	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if ((originDirectory==null) || (originDirectory.equals(""))) {		
			throw new BuildException("Origin Directory is required!");
		} 		
		if ((targetDirectory==null) || (targetDirectory.equals(""))) {		
			throw new BuildException("Target Directory is required!");
		} 		
	}

	@Override
	public String transform(String sourceValue) {
		if (sourceValue.equals(originDirectory)){
			return targetDirectory;
		}
		if (sourceValue.startsWith(originDirectory)){
			String upperPart = sourceValue.substring(originDirectory.length());
			return targetDirectory+upperPart;
		}
		return sourceValue;
	}

	public String getOriginDirectory() {
		return originDirectory;
	}

	public void setOriginDirectory(String originDirectory) {
		this.originDirectory = originDirectory;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

}
