/*
 * Créé le 1 juin 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.propertypages;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class ArcadPropertyPage extends PropertyPage {

	private String pageName = "ArcadPropertyPage"; //$NON-NLS-1$

	public ArcadPropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse .swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(final Composite parent) {
		return null;
	}

	public void doAfterCreating() {
		// Do nothing
	}

	protected abstract void fillContents(Composite parent);

	public String getPageName() {
		return pageName;
	}

	public void setPageName(final String string) {
		pageName = string;
	}

	protected abstract void setValue();

}
