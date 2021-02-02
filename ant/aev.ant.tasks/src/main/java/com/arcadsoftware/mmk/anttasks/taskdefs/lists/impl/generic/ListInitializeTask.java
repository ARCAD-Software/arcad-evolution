package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedTask;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;

public class ListInitializeTask extends AbstractXmlFileListCountedTask {

	private String id = null;
	private String value = null;

	@Override
	public int processExecutionWithCount() {
		validateListAttributes();
		return list.reinitializeValue(id, value);
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public void validateAttributes() {
		super.validateAttributes();
	}

	public void validateListAttributes() {
		super.validateAttributes();
		if (id == null || id.equals("")) {
			throw new BuildException("Id attribute must be set!");
		} else {
			final ListColumnDef cd = list.getMetadatas().getColumnFromId(id);
			if (cd == null) {
				throw new BuildException("Id attribute is not a valid id!");
			} else if (cd.isKey()) {
				throw new BuildException("Id attribute must not be set to an key id!");
			}
		}
		// if ((value==null) || (value.equals(""))) {
		// throw new BuildException("Value attribute must be set!");
		// }
		if (value == null) {
			throw new BuildException("Value attribute must be set!");
		}

	}

}
