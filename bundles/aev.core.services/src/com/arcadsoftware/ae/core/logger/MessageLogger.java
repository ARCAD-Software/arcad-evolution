
package com.arcadsoftware.ae.core.logger;

import java.util.ArrayList;
import com.arcadsoftware.ae.core.logger.formatter.impl.XmlMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.ErrorMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.FailedMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.InfoMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.StatusMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.SucceedMessage;
import com.arcadsoftware.ae.core.logger.messages.impl.WarningMessage;
import com.arcadsoftware.ae.core.logger.router.AbstractMessageRouter;
import com.arcadsoftware.ae.core.utils.Utils;

/**
 * Classe en charge d'envoyer les messages.<br>
 * Cette classe est une classe d'aide permettant l'envoi de
 * message vers la sortie standard.
 */
public class MessageLogger {
	private static MessageLogger instance = null;			
	private ArrayList<AbstractMessageRouter> messageRouters = new ArrayList<AbstractMessageRouter>();
	private static boolean enabled = true;
	private static boolean loaded = false;	

    protected MessageLogger() {
        super();
    }

    public void setMessageRouters(ArrayList<AbstractMessageRouter> list) {
    	this.messageRouters = list;
    }
    
    
    
	public static MessageLogger getInstance(){
		if (instance==null) {
			instance = new MessageLogger();			
		}
		return instance;
	}
	
    public static void setInstance(MessageLogger instance) {
    	MessageLogger.instance = instance;
    	loaded = true;
    }	    
        
    
    /**
     * Envoi le message pass‚ en param‚tre.
     * @param message AbstractMessage : Message … envoyer.
     */
    public static void initializeMessage() {
    	for (int i=0;i<getInstance().messageRouters.size();i++){
    		AbstractMessageRouter mr = 
    			(AbstractMessageRouter)getInstance().messageRouters.get(i);
    		mr.initializeMessages();
    	}
    }     
    
    /**
     * Envoi le message pass‚ en param‚tre.
     * @param message AbstractMessage : Message … envoyer.
     */
    public static void sendMessage(AbstractMessage message) {
    	for (int i=0;i<getInstance().messageRouters.size();i++){
    		AbstractMessageRouter mr = (AbstractMessageRouter)getInstance().messageRouters.get(i);
    		mr.interceptMessage(message);
    	}
    }
    
    /**
     * Envoi le message pass‚ en param‚tre.
     * @param message AbstractMessage : Message … envoyer.
     */
    public static void finalizeMessage() {
    	for (int i=0;i<getInstance().messageRouters.size();i++){
    		AbstractMessageRouter mr = 
    			getInstance().messageRouters.get(i);
    		mr.finalizeMessages();
    	}
    }    
    
    /**
     * Envoi le message pass‚ en param‚tre.
     * @param message AbstractMessage : Message … envoyer.
     */
    public static String[] getData() {
    	String[] result = new String[getInstance().messageRouters.size()];
    	for (int i=0;i<getInstance().messageRouters.size();i++){
    		AbstractMessageRouter mr = 
    			getInstance().messageRouters.get(i);
    		result[i] = mr.getData();
    	}
    	return result;
    }       
    
    
    /**
     * Envoi un message de type erreur ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte principal du message. 
     */
    public static void sendErrorMessage(String serviceName,String message) {
        sendMessage(new ErrorMessage(serviceName,message));
    }    
    
    /**
     * Envoi un message de type erreur ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param e : Throwable Objet de type Throwable. 
     */
    public static void sendErrorMessage(String serviceName,Throwable e) {
        sendMessage(new ErrorMessage(serviceName,Utils.stackTrace(e)));
    }      
    
    /**
     * Envoi un message de type information ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte pricncipal du message. 
     */    
    public static void sendInfoMessage(String serviceName,String message) {
        sendMessage(new InfoMessage(serviceName,message));
    }   
    
    /**
     * Envoi un message de type information ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param e : Throwable Objet de type Throwable. 
     */
    public static void sendInfoMessage(String serviceName,Throwable e) {
        sendMessage(new InfoMessage(serviceName,Utils.stackTrace(e)));
    }       
    
    /**
     * Envoi un message de type Warning ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte pricncipal du message. 
     */    
    public static void sendWarningMessage(String serviceName,String message) {
        sendMessage(new WarningMessage(serviceName,message));
    }       
    
    /**
     * Envoi un message de type Warning ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param e : Throwable Objet de type Throwable. 
     */
    public static void sendWarningMessage(String serviceName,Throwable e) {
        sendMessage(new WarningMessage(serviceName,Utils.stackTrace(e)));
    }     
    
    /**
     * Envoi un message de type SUCCEED ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte pricncipal du message. 
     */    
    public static void sendSucceedMessage(String serviceName,String message) {
        sendMessage(new SucceedMessage(serviceName,message));
    } 
    
    /**
     * Envoi un message de type SUCCEED ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param e : Throwable Objet de type Throwable. 
     */
    public static void sendSucceedMessage(String serviceName,Throwable e) {
        sendMessage(new SucceedMessage(serviceName,Utils.stackTrace(e)));
    }        
    

	public static void sendStatusMessage(String serviceName, boolean b) {
		sendMessage(new StatusMessage(serviceName,b));		
	}        
    
    
    /**
     * Envoi un message de type FAILED ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param message : Texte pricncipal du message. 
     */    
    public static void sendFailedMessage(String serviceName,String message) {
        sendMessage(new FailedMessage(serviceName,message));
    }       
    /**
     * Envoi un message de type FAILED ayant pour texte 
     * principal <code>message</code>.
     * @param serviceName String : Nom du service ‚metteur.
     * @param e : Throwable Objet de type Throwable. 
     */
    public static void sendFailedMessage(String serviceName,Throwable e) {
        sendMessage(new FailedMessage(serviceName,Utils.stackTrace(e)));
    }        
     
	public synchronized static boolean isEnabled() {
		return enabled;
	}

	public synchronized static void setEnabled(boolean enabled) {
		MessageLogger.enabled = enabled;
	}

	public synchronized static boolean isLoaded() {
		return loaded;
	}	
	
	/**
	 * Renvoit 
	 * @return the messageRouters ArrayList<AbstractMessageRouter> : 
	 */
	public ArrayList<AbstractMessageRouter> getMessageRouters() {
		return messageRouters;
	}


	public static boolean isStatusOk(String message) {
		if (message.endsWith("."))
			message = message.substring(0,message.length()-1);
		return XmlMessageFormatter.isStatusOk(message,AbstractMessage.TYPE_MSG_STATUS);
	}
}
