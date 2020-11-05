package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Separator;

/**
 * Dummy class to mimic a {@link Separator}
 * 
 * @author ARCAD Software
 */
public class ArcadSeparator extends ArcadAction {
	@Override
	protected boolean canExecute() {
		return false;
	}

	@Override
	public boolean isNotSeparator() {
		return false;
	}
}
