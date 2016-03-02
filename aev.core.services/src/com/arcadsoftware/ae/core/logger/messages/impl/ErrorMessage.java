
package com.arcadsoftware.ae.core.logger.messages.impl;



/**
 * Classe repr‚sentant un message d'erreur.<br>
 * Celle classe d‚finit un message dont le type est fix‚
 * … {@link com.arcadsoftware.serviceprovider.message.AbstractMessage#TYPE_MSG_ERROR TYPE_MSG_ERROR}
 * @author MD
 */
public class ErrorMessage extends StatusMessage {
    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte principal du message.
     */
    public ErrorMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_ERROR, message,false);
    }

}
