/*
 * Created on 12 avr. 2006
 *
 */
package com.arcadsoftware.aev.core.ui.actions;


/**
 * Classe des actions n'agissant que sur une seule entité ARCAD par envoi
 * d'une commande sur le serveur iSeries.<br>
 * @author MD
 *
 */
public abstract class AbstractSimpleItemAction extends ArcadAction {
	protected ArcadActions containerActions = null;
			
	/**
	 * Constructeur
	 */
	public AbstractSimpleItemAction() {
		super();
	}		
	/**
	 * Constructeur
	 * @param containerActions
	 */
	public AbstractSimpleItemAction(ArcadActions containerActions) {
		super();
		this.containerActions = containerActions;
	}
	
}
