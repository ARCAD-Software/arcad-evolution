package com.arcadsoftware.aev.core.ui.mementos;

import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.tools.StringTools;

public class DefaultExplorerMementoTools extends ExplorerMementoTools {

	public DefaultExplorerMementoTools(final String viewId) {
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
	protected String getFilename() {
		return StringTools.EMPTY;
	}

	@Override
	protected boolean isGoodExplorerSettings(final ExplorerSettings explorerSettings) {
		return false;
	}

	@Override
	protected void readAll(final XMLMemento root) {
		// Do nothing
	}

	@Override
	protected void saveExplorerSettings(final XMLMemento root, final ExplorerSettings es) {
		// Do nothing
	}
}
