/*
 * Cr�� le 23 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author MD
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ArcadWizardDialog extends WizardDialog {

	/**
	 * @param parentShell
	 * @param newWizard
	 */
	public ArcadWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		this.setPageSize(565,350);
		this.updateDialog();		
	}
	
	public void updateDialog(){
		updateSize(getCurrentPage());
	}
	
	public void perforFinish(){
		finishPressed();
	}
	

}
