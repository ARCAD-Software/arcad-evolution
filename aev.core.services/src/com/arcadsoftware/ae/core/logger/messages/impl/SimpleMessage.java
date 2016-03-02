
package com.arcadsoftware.ae.core.logger.messages.impl;

import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;


/**
 * Classe repr‚sentant un message sans zone de donn‚es.<br>
 * @author MD
 */
public class SimpleMessage extends AbstractMessage {
        
    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service emetteur du message.
     * @param typeInfo String : Type de message (@link AbstractMessage Type de message)
     * @param message String : Texte principal du message.
     */
    public SimpleMessage(String serviceName,String typeInfo,String message) {
        super(serviceName,typeInfo,message);
    }

}
