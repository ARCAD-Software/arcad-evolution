
package com.arcadsoftware.ae.core.logger.messages.impl;



public class ErrorMessage extends StatusMessage {
    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service émetteur.
     * @param message : Texte principal du message.
     */
    public ErrorMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_ERROR, message,false);
    }

}
