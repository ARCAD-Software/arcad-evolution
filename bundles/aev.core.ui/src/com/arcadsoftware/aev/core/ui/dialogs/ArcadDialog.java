/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
/*
 * Created on 11 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author mlafon
 * @version 1.0.0 Ajoute quelques m�thode d'assistance � a la cr�ation de controles dans la fen�tre... <i> Copryright
 *          2004, Arcad-Software</i>.
 */
public abstract class ArcadDialog extends Dialog {
	protected static Shell getPluginShell() {
		return EvolutionCoreUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	boolean okButtonOnly = false;

	protected ArcadDialog(final Shell parentShell) {
		this(parentShell, false);
	}

	protected ArcadDialog(final Shell parentShell, final boolean okButtonOnly) {
		super(parentShell);
		setBlockOnOpen(true);
		this.okButtonOnly = okButtonOnly;
	}

	/**
	 * M�thode permettant d'ajouter un iSelectionListener d�clenchant la validation de la donn�es saisie pour un widget
	 * de type Button.
	 *
	 * @param b
	 *            Button : Button � mettre sous contr�le
	 */
	protected void addCheckDataListeners(final Button b) {
		b.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent event) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * M�thode permettant d'ajouter un iSelectionListener et un ModifyListener d�clenchant la validation de la donn�es
	 * saisie pour une liste Combo.
	 *
	 * @param c
	 *            Combo : Combo � mettre sous contr�le
	 */

	protected void addCheckDataListeners(final Combo c) {
		c.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent event) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
		c.addModifyListener(new ModifyListener() {
			@Override
			@SuppressWarnings("synthetic-access")
			public void modifyText(final ModifyEvent e) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * M�thode permettant d'ajouter un ModifyListener d�clenchant la validation de la donn�es saisie pour un widget de
	 * type Text.
	 *
	 * @param c
	 *            Text : Text � mettre sous contr�le
	 */
	protected void addCheckDataListeners(final Text t) {
		t.addModifyListener(new ModifyListener() {
			@Override
			@SuppressWarnings("synthetic-access")
			public void modifyText(final ModifyEvent e) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * M�thode permettant la validation des informations saisies.<br>
	 * La surcharge de cette m�thode permet de d�clarer vos r�gles de validation de saisie.<br>
	 * Pour int�grer l'appel de cette fonction � vos contr�le de saisie, vous pouvez utiliser les m�thodes
	 * "addCheckDataListeners()" disponible sur les Combo et les Text.
	 *
	 * @return boolean : <b>True</b> si les informations saisies sont valides, <b>false</b> sinon.
	 */
	protected boolean checkDataFromListeners() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Icon.ARCAD.image());
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, DialogConstantProvider.getInstance().OK_LABEL, true);
		if (!okButtonOnly) {
			createButton(parent, IDialogConstants.CANCEL_ID,
					DialogConstantProvider.getInstance().CANCEL_LABEL, false);
		}
	}

	protected IPreferenceStore getPreferences() {
		return EvolutionCoreUIPlugin.getDefault().getPreferenceStore();
	}

}