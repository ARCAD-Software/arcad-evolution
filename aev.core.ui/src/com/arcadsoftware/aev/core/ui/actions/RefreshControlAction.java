/*
 * Créé le 27 mai 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author mlafon
 * @version 1.0.0
 * 
 * 
 */
public class RefreshControlAction extends Action {

	private IRefreshControl control = null;

	protected RefreshControlAction(String text) {
		super(text);
	}

	protected RefreshControlAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	protected RefreshControlAction(String text, int style) {
		super(text, style);
	}

	/**
	 * @param control
	 *            l'élément cabable de se raffraichir.
	 * @param image
	 */
	public RefreshControlAction(IRefreshControl control, String longText) {
		super();
		this.control = control;
		setText(CoreUILabels.resString("RefreshControlAction.Refresh")); //$NON-NLS-1$
		setToolTipText(longText);
		setImageDescriptor(CoreUILabels.getImageDescriptor(EvolutionCoreUIPlugin.ACT_REFRESH));
	}

	@Override
	public void run() {
		if (control != null)
			control.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return (control != null) && super.isEnabled();
	}

	/**
	 * @return IRefreshControl
	 */
	public IRefreshControl getControl() {
		return control;
	}

	/**
	 * @param control
	 *            le controle à raffraihir.
	 */
	public void setControl(IRefreshControl control) {
		this.control = control;
	}

}
