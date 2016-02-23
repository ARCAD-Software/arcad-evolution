/*
 * Created on 2 mars 2006
 *
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.dialogs.PropertyDialog;

import com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage;

/**
 * @author MD
 * 
 */
public class ArcadPropertyDialog extends PropertyDialog implements ISelectionChangedListener {

	/**
	 * @param parentShell
	 * @param mng
	 * @param selection
	 */
	@SuppressWarnings("restriction")
	public ArcadPropertyDialog(Shell parentShell, PreferenceManager mng, ISelection selection) {
		super(parentShell, mng, selection);
	}

	public void doAfterCreation() {
		this.getTreeViewer().addPostSelectionChangedListener(this);
		// Comme on n'enregistre cette page qu'après sa création, pour éviter
		// qu'elle ne prenne la taille de
		// sa liste à la création, elle n'a pas reçu l'évènement pour la 1ère
		// sélection (celle par défaut)
		// On lui envoie donc cet évènement.
		this.getTreeViewer().setSelection(this.getTreeViewer().getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		try {
			TreeViewer tv = this.getTreeViewer();
			StructuredSelection selection = (StructuredSelection) tv.getSelection();
			if (!selection.isEmpty()) {
				Object o = selection.getFirstElement();
				IPreferencePage p = ((IPreferenceNode) o).getPage();
				if (p instanceof ArcadPropertyPage)
					((ArcadPropertyPage) p).doAfterCreating();
			}
		} catch (Exception e) {
			// Do nothing
		}
	}
}
