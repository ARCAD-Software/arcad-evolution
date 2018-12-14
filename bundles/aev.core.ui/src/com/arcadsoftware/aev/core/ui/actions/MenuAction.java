package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * 
 * @author dlelong
 * 
 */
public class MenuAction extends Action implements IMenuCreator {

	private MenuManager menuManager;

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public MenuAction(MenuManager menuManager) {
		this.menuManager = menuManager;
		setMenuCreator(this);
	}

	public MenuAction(String text, int style, MenuManager menuManager) {
		super(text, style);
		this.menuManager = menuManager;
		setMenuCreator(this);
	}

	public Menu getMenu(Control parent) {
		return menuManager.createContextMenu(parent);
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	public void dispose() {
		menuManager.dispose();
	}

}
