/*
 * Créé le 23 juin 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.viewers.sorters;

import java.text.Collator;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.IArcadTableLabelProvider;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ArcadTableViewerColumnSorter extends ViewerSorter {

	private int currentColumnIndex = 0;
	boolean reversed = false;
	private ViewerComparator viewerComparator = new ViewerComparator();

	/**
	 * 
	 */
	public ArcadTableViewerColumnSorter() {
		super();
	}

	public ArcadTableViewerColumnSorter(int currentColumnIndex) {
		this();
		this.currentColumnIndex = currentColumnIndex;
	}

	/**
	 * @param collator
	 */
	public ArcadTableViewerColumnSorter(Collator collator) {
		super(collator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers .Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int cat1 = category(e1);
		int cat2 = category(e2);

		if (cat1 != cat2)
			return cat1 - cat2;

		// cat1 == cat2

		String name1 = null;
		String name2 = null;

		if (viewer == null || !(viewer instanceof ContentViewer)) {
			name1 = e1.toString();
			name2 = e2.toString();
		} else {
			IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
			if (prov instanceof IArcadTableLabelProvider) {
				IArcadTableLabelProvider lprov = (IArcadTableLabelProvider) prov;
				Object value1 = lprov.getColumnValue(e1, currentColumnIndex);
				Object value2 = lprov.getColumnValue(e2, currentColumnIndex);
				if (value1 != null && value1 instanceof Comparable && value2 != null && value2 instanceof Comparable
						&& value1.getClass().equals(value2.getClass())) {
					if (isReversed())
						return ((Comparable) value2).compareTo(value1);
					else
						return ((Comparable) value1).compareTo(value2);
				} else {
					name1 = lprov.getColumnText(e1, currentColumnIndex);
					name2 = lprov.getColumnText(e2, currentColumnIndex);
				}
			} else {
				name1 = e1.toString();
				name2 = e2.toString();
			}
		}
		if (name1 == null)
			name1 = StringTools.EMPTY;
		if (name2 == null)
			name2 = StringTools.EMPTY;
		if (isReversed())
			return viewerComparator.compare(viewer, name2, name1);
		return viewerComparator.compare(viewer, name1, name2);
	}

	/**
	 * @return int
	 */
	public int getCurrentColumnIndex() {
		return currentColumnIndex;
	}

	/**
	 * @param i
	 */
	public void setCurrentColumnIndex(int i) {
		currentColumnIndex = i;
	}

	/**
	 * @return boolean
	 */
	public boolean isReversed() {
		return reversed;
	}

	/**
	 * @param b
	 */
	public void setReversed(boolean b) {
		reversed = b;
	}

}
