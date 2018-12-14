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
 * @author MD
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface IContainer extends IAdaptable{
	String getLabel();
	Image getImage();	
	IContainer getParent();
	Object[] getChildren();
	boolean hasChildren();
	String getUniqueKey();	
	public boolean isDragable();
	public boolean valideDrop(Object source);
	public boolean valideDrop(TransferData source);	
	public boolean performDrop(IContainer source);	
	public void manageMenuAction(IMenuManager manager);	
	public void manageToolbarAction(IToolBarManager manager);	
	public boolean isPropertyMenuVisible();
	public StructuredViewer getViewer();
}
