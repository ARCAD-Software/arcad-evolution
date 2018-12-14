/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;

import com.arcadsoftware.aev.core.ui.container.Container;
import com.arcadsoftware.aev.core.ui.container.ContainerProvider;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class ArcadActions {

	@SuppressWarnings("unchecked")
	private ArrayList actions = new ArrayList();
	private Container container;
	private ContainerProvider containerProvider;

	public ArcadActions() {
	
	}
	
	public ArcadActions(Container container) {
		this.container = container;
	}
	
	public ArcadAction[] getActions() {
		ArcadAction[] a = new ArcadAction[actions.size()];
		for (int i = 0; i < actions.size(); i++) {
			a[i] = (ArcadAction) actions.get(i);
		}
		return a;
	}

	public void mergeAction(ArcadActions actionToMerge) {
		ArcadAction[] a = actionToMerge.getActions();
		if (a.length > 0)
			addSeparator();
		for (int i = 0; i < a.length; i++) {
			addAction(a[i]);
		}
	}

	@SuppressWarnings("unchecked")
	public void addAction(ArcadAction a) {
		actions.add(a);
	}

	@SuppressWarnings("unchecked")
	public void addSeparator() {
		actions.add(new Separator());
	}

	public abstract void makeAction();

	public void fillMenuAction(IMenuManager manager) {
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i) instanceof ArcadAction){
				ArcadAction a = (ArcadAction) actions.get(i);
				if (match(a)) {
					manager.add(a);
				}
			} else if (actions.get(i) instanceof Separator) {
				manager.add((Separator) actions.get(i));
			}
		}
		manager.add(new Separator());
	}

	protected boolean match(ArcadAction a){
		return true;
	}
	
	public void fillToolbarAction(IToolBarManager manager) {
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i) instanceof ArcadAction)
				manager.add((ArcadAction) actions.get(i));
			else if (actions.get(i) instanceof Separator)
				manager.add((Separator) actions.get(i));
		}
		manager.add(new Separator());
	}

	public Container getContainer() {
		return container;
	}

	public ContainerProvider getContainerProvider() {
		return containerProvider;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public void setContainerProvider(ContainerProvider provider) {
		containerProvider = provider;
	}
}
