package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * @author dlelong
 */
public class MenuAction extends Action implements IMenuCreator {

	private final MenuManager menuManager;

	public MenuAction(final MenuManager menuManager) {
		this.menuManager = menuManager;
		setMenuCreator(this);
	}

	public MenuAction(final String text, final int style, final MenuManager menuManager) {
		super(text, style);
		this.menuManager = menuManager;
		setMenuCreator(this);
	}

	@Override
	public void dispose() {
		menuManager.dispose();
	}

	@Override
	public Menu getMenu(final Control parent) {
		return menuManager.createContextMenu(parent);
	}

	@Override
	public Menu getMenu(final Menu parent) {
		return null;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

}
