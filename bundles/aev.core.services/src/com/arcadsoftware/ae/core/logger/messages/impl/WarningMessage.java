package com.arcadsoftware.ae.core.logger.messages.impl;



/**
 * Classe repr‚sentant un message d'avertissement.<br>
 * Celle classe d‚finit un message dont le type est fix‚
 * … {@link com.arcadsoftware.serviceprovider.message.AbstractMessage#TYPE_MSG_WARNING TYPE_MSG_WARNING}
 * @author MD
 */
public class WarningMessage extends SimpleMessage {

    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte principal du message.
     */
    public WarningMessage(String serviceName,  String message) {
        super(serviceName, TYPE_MSG_WARNING, message);
    }

}
