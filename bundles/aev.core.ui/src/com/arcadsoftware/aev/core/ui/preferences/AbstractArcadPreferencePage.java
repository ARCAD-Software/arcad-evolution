/*
 * Created on 16 déc. 2006
 */
package com.arcadsoftware.aev.core.ui.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author MD
 * 
 */
public abstract class AbstractArcadPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/**
     * 
     */
	public AbstractArcadPreferencePage() {
		super();
	}

	/**
	 * @param title
	 */
	public AbstractArcadPreferencePage(String title) {
		super(title);
	}

	/**
	 * @param title
	 * @param image
	 */
	public AbstractArcadPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	public void showError(String message) {
		MessageDialog.openError(EvolutionCoreUIPlugin.getShell(), CoreUILabels.resString("msg.commonTitle"), //$NON-NLS-1$
				message);
	}
}
