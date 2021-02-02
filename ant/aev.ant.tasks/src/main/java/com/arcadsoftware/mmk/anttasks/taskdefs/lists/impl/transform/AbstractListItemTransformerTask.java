package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public abstract class AbstractListItemTransformerTask extends AbstractArcadAntTask {

	private StoreItem item = null;

	private AbstractXmlList parentList = null;
	private String resultProperty = null;

	private String resultValue = null;
	private int sourceIndex = -1;

	private String sourceProperty = null;
	private String sourceValue = null;

	private int targetIndex = -1;
	private boolean verbose = false;

	@Override
	public void doExecute() {
		if (parentList == null) {
			throw new BuildException("List is required!");
		}
		if (item == null) {
			throw new BuildException("Item is required!");
		}
		sourceIndex = validPropertyId(sourceProperty);
		if (sourceIndex == -1) {
			throw new BuildException("Invalid Source Property Identifier");
		}
		targetIndex = validPropertyId(resultProperty);
		if (targetIndex == -1) {
			throw new BuildException("Invalid Result Property Identifier");
		}
		sourceValue = item.getValue(sourceProperty);
		if (verbose) {
			System.out.println("Source Value = " + sourceValue);
		}
		resultValue = transform(sourceValue);
		if (verbose) {
			System.out.println("Result Value = " + resultValue);
		}

		item.setValue(targetIndex, resultValue);
		parentList.updateItems(item);
	}

	public StoreItem getItem() {
		return item;
	}

	public AbstractXmlList getParentList() {
		return parentList;
	}

	public String getResultProperty() {
		return resultProperty;
	}

	public String getSourceProperty() {
		return sourceProperty;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setItem(final StoreItem item) {
		this.item = item;
	}

	public void setParentList(final AbstractXmlList parentList) {
		this.parentList = parentList;
	}

	public void setResultProperty(final String resultProperty) {
		this.resultProperty = resultProperty;
	}

	public void setSourceProperty(final String sourceProperty) {
		this.sourceProperty = sourceProperty;
	}

	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	public abstract String transform(String sourceValue);

	@Override
	public void validateAttributes() {
		if (sourceProperty == null || sourceProperty.equals("")) {
			throw new BuildException("Source property is required!");
		}
		if (resultProperty == null || resultProperty.equals("")) {
			resultProperty = sourceProperty;
		}
	}

	public int validPropertyId(final String property) {
		final ListMetaDatas md = item.getMetadatas();
		for (int i = 0; i < md.count(); i++) {
			final ListColumnDef cd = md.getColumnDefAt(i);
			if (cd.getId().equalsIgnoreCase(property)) {
				return i;
			}
		}
		return -1;
	}

}
