
package com.arcadsoftware.ae.core.logger.messages.impl;



/**
 * Classe repr‚sentant un message d'information.<br>
 * Celle classe d‚finit un message dont le type est fix‚
 * … {@link com.arcadsoftware.serviceprovider.message.AbstractMessage#TYPE_MSG_INFO TYPE_MSG_INFO}
 * @author MD
 */
public class InfoMessage extends SimpleMessage {

     /**
      * Constructeur de classe.
      * @param serviceName String : Nom du service ‚metteur.
      * @param message : Texte principal du message.
      */
    public InfoMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_INFO, message);
    }

}
