/*
 * Créé le 13 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface IContainer extends IAdaptable {
	Object[] getChildren();

	Image getImage();

	String getLabel();

	IContainer getParent();

	String getUniqueKey();

	StructuredViewer getViewer();

	boolean hasChildren();

	boolean isDragable();

	boolean isPropertyMenuVisible();

	void manageMenuAction(IMenuManager manager);

	void manageToolbarAction(IToolBarManager manager);

	boolean performDrop(IContainer source);

	boolean valideDrop(Object source);

	boolean valideDrop(TransferData source);
}
