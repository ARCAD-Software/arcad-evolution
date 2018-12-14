/*
 * Créé le 11 juin 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.contentproviders;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class HierarchicTreeContentProvider implements ITreeContentProvider {
	protected static final Object[] EMPTYARRAY = new Object[0];
	private int firstDisplayLevel = 1;

	/**
	 * 
	 */
	public HierarchicTreeContentProvider() {
		this(1);
	}

	public HierarchicTreeContentProvider(int firstDisplayLevel) {
		super();
		this.firstDisplayLevel = firstDisplayLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IArcadCollectionItem) {
			IArcadCollectionItem i = (IArcadCollectionItem) parentElement;
			if (i.getParent() == null)
				return EMPTYARRAY;
			return i.getParent().getChildren(i);
		}
		return EMPTYARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
	 * )
	 */
	public Object getParent(Object element) {
		if (element instanceof IArcadCollectionItem) {
			IArcadCollectionItem i = (IArcadCollectionItem) element;
			if (i.getParent() == null)
				return null;
			return i.getParent().getParent(i);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof IArcadCollectionItem) {
			IArcadCollectionItem i = (IArcadCollectionItem) element;
			if (i.getParent() == null)
				return false;
			return i.getParent().hasChildren(i);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ArcadCollection) {
			ArcadCollection c = (ArcadCollection) inputElement;
			if (c.count() > 0) {
				Object[] o = c.getElementsByLevel(firstDisplayLevel);
				return o;
			}
			return EMPTYARRAY;
		}
		return EMPTYARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Do nothing
	}

}
