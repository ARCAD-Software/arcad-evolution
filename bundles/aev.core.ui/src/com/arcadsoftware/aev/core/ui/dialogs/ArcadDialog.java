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
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author mlafon
 * @version 1.0.0
 * 
 *          Ajoute quelques méthode d'assistance à a la création de controles
 *          dans la fenêtre...
 * 
 *          <i> Copryright 2004, Arcad-Software</i>.
 */
public abstract class ArcadDialog extends Dialog {
	boolean okButtonOnly = false;

	protected ArcadDialog(Shell parentShell, boolean okButtonOnly) {
		super(parentShell);
		setBlockOnOpen(true);
		this.okButtonOnly = okButtonOnly;
	}

	protected ArcadDialog(Shell parentShell) {
		this(parentShell, false);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID,DialogConstantProvider.getInstance().OK_LABEL , true);
		if (!okButtonOnly) {
			createButton(parent, IDialogConstants.CANCEL_ID,
					DialogConstantProvider.getInstance().CANCEL_LABEL,false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_ARCAD));
	}

	protected static Shell getPluginShell() {
		return EvolutionCoreUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	protected IPreferenceStore getPreferences() {
		return EvolutionCoreUIPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * Méthode permettant d'ajouter un ModifyListener déclenchant la validation
	 * de la données saisie pour un widget de type Text.
	 * 
	 * @param c
	 *            Text : Text à mettre sous contrôle
	 */
	protected void addCheckDataListeners(Text t) {
		t.addModifyListener(new ModifyListener() {
			@SuppressWarnings("synthetic-access")
			public void modifyText(ModifyEvent e) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * Méthode permettant d'ajouter un iSelectionListener déclenchant la
	 * validation de la données saisie pour un widget de type Button.
	 * 
	 * @param b
	 *            Button : Button à mettre sous contrôle
	 */
	protected void addCheckDataListeners(Button b) {
		b.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent event) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * Méthode permettant d'ajouter un iSelectionListener et un ModifyListener
	 * déclenchant la validation de la données saisie pour une liste Combo.
	 * 
	 * @param c
	 *            Combo : Combo à mettre sous contrôle
	 */

	protected void addCheckDataListeners(Combo c) {
		c.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent event) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
		c.addModifyListener(new ModifyListener() {
			@SuppressWarnings("synthetic-access")
			public void modifyText(ModifyEvent e) {
				getButton(IDialogConstants.OK_ID).setEnabled(checkDataFromListeners());
			}
		});
	}

	/**
	 * Méthode permettant la validation des informations saisies.<br>
	 * La surcharge de cette méthode permet de déclarer vos règles de validation
	 * de saisie.<br>
	 * Pour intégrer l'appel de cette fonction à vos contrôle de saisie, vous
	 * pouvez utiliser les méthodes "addCheckDataListeners()" disponible sur les
	 * Combo et les Text.
	 * 
	 * @return boolean : <b>True</b> si les informations saisies sont valides,
	 *         <b>false</b> sinon.
	 */
	protected boolean checkDataFromListeners() {
		return true;
	}

}