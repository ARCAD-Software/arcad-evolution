package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform;

import org.apache.tools.ant.BuildException;

public class directoryConvertTask extends AbstractListItemTransformerTask {

	public String originDirectory = null;
	public String targetDirectory = null;

	public String getOriginDirectory() {
		return originDirectory;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setOriginDirectory(final String originDirectory) {
		this.originDirectory = originDirectory;
	}

	public void setTargetDirectory(final String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	@Override
	public String transform(final String sourceValue) {
		if (sourceValue.equals(originDirectory)) {
			return targetDirectory;
		}
		if (sourceValue.startsWith(originDirectory)) {
			final String upperPart = sourceValue.substring(originDirectory.length());
			return targetDirectory + upperPart;
		}
		return sourceValue;
	}

	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (originDirectory == null || originDirectory.equals("")) {
			throw new BuildException("Origin Directory is required!");
		}
		if (targetDirectory == null || targetDirectory.equals("")) {
			throw new BuildException("Target Directory is required!");
		}
	}

}
