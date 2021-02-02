/*
 * Créé le 27 mai 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class RefreshControlAction extends Action {

	private IRefreshControl control = null;

	/**
	 * @param control
	 *            l'élément cabable de se raffraichir.
	 * @param image
	 */
	public RefreshControlAction(final IRefreshControl control, final String longText) {
		super();
		this.control = control;
		setText(CoreUILabels.resString("RefreshControlAction.Refresh")); //$NON-NLS-1$
		setToolTipText(longText);
		setImageDescriptor(Icon.REFRESH.imageDescriptor());
	}

	protected RefreshControlAction(final String text) {
		super(text);
	}

	protected RefreshControlAction(final String text, final ImageDescriptor image) {
		super(text, image);
	}

	protected RefreshControlAction(final String text, final int style) {
		super(text, style);
	}

	/**
	 * @return IRefreshControl
	 */
	public IRefreshControl getControl() {
		return control;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return control != null && super.isEnabled();
	}

	@Override
	public void run() {
		if (control != null) {
			control.refresh();
		}
	}

	/**
	 * @param control
	 *            le controle à raffraihir.
	 */
	public void setControl(final IRefreshControl control) {
		this.control = control;
	}

}
