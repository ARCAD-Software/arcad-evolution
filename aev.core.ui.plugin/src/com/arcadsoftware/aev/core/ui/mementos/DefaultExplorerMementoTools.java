package com.arcadsoftware.aev.core.ui.mementos;

import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.tools.StringTools;

public class DefaultExplorerMementoTools extends ExplorerMementoTools {

	public DefaultExplorerMementoTools(String viewId) {
		super(viewId);
	}

	@Override
	protected ExplorerSettings createNewExplorerSettings() {
		return null;
	}

	@Override
	protected void deleteOldValues() {
		// Do nothing
	}

	@Override
	protected boolean isGoodExplorerSettings(ExplorerSettings explorerSettings) {
		return false;
	}

	@Override
	protected void readAll(XMLMemento root) {
		// Do nothing
	}

	@Override
	protected void saveExplorerSettings(XMLMemento root, ExplorerSettings es) {
		// Do nothing
	}

	@Override
	protected String getFilename() {
		return StringTools.EMPTY;
	}
}
