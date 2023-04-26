/*
 * Cr�� le 13 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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
