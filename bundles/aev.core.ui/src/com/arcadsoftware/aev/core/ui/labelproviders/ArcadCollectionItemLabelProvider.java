/*
 * Créé le 28 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.labelproviders;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.collections.IArcadDisplayable;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ArcadCollectionItemLabelProvider implements ILabelProvider {

	public ArcadCollectionItemLabelProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse. jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(final ILabelProviderListener listener) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Do nothing
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas dans Core UI
	 *
	 * @param key
	 * @return
	 */
	protected Image getCompositeImage(final String key, final String decoKey) {
		return CoreUILabels.getCompositeImage(key, decoKey);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(final Object element) {
		if (element instanceof IArcadDisplayable) {
			final IArcadDisplayable e = (IArcadDisplayable) element;
			final String overlay = e.getOverlayID();
			// ATTENTION : il est nécessaire de surcharger les méthodes getImage
			// et getCompositeImage si vos icônes
			// ne sont pas dans Core UI
			if (overlay != null) {
				return getCompositeImage(e.getIconID(), overlay);
			}
			return getImage(((IArcadCollectionItem) element).getIconID());
		}
		return null;
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas dans Core UI
	 *
	 * @param key
	 * @return
	 */
	protected Image getImage(final String key) {
		return CoreUILabels.getImage(key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(final Object element) {
		if (element instanceof IArcadDisplayable) {
			return ((IArcadDisplayable) element).getLabel();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang .Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// Do nothing
	}

}
