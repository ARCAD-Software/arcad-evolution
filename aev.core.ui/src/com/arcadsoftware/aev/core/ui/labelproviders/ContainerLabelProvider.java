/*
 * Créé le 13 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.labelproviders;

import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.ui.container.IContainer;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author MD
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ContainerLabelProvider extends AbstractColumnedTreeLabelProvider {

	public ContainerLabelProvider(AbstractColumnedViewer viewer) {
		super(viewer);
	}

	@Override
	protected Image getActualImage(Object element, int actualColumnIndex) {
		if(actualColumnIndex == 0 && element instanceof IContainer)
			return ((IContainer)element).getImage();
		return super.getActualImage(element, actualColumnIndex);
	}
}
