
package com.arcadsoftware.ae.core.logger.messages;



import java.util.ArrayList;




/**
 * Classe abstraite de base servant … d‚finir tous les messages.<br>
 * Un message affich‚ a le format suivant : <br>
 * <pre>
 * [&lt;service_name&gt;][&lt;message_type&gt;]&lt;message_text&gt;(&lt;data_area&gt;)
 * </pre>
 * dont les diff‚rentes zones sont d‚crites comme suit : <br>
 * <table border="1" width="100%">
 * <tr><td><b>Zone</b></td><td><b>Description</b></td></tr>
 * <tr><td>&lt;service_name&gt;</td><td>Repr‚sente le nom du service ayant envoy‚ le message</td></tr>
 * <tr><td>&lt;message_type&gt;</td><td>Indique le type de message. 
 *    Cette zone peut prendre les valeurs suivantes : (ERROR|WARNING|INFO)</td></tr>
 * <tr><td>&lt;message_text&gt;</td><td>Texte principal du message</td></tr>
 * <tr><td>&lt;data_area&gt;</td>
 * 	<td>Transporte les donn‚es du message. Ces donn‚es sont cod‚es 
 * 	par paire de la maniŠre suivante : &lt;key&gt;=&lt;value&gt; ;
 * </td>
 * </tr>   
 * </table>
 * @author MD
 *
 */
public abstract class AbstractMessage {
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message d'information (valeur="INFO")
     */
    public static final String TYPE_MSG_INFO = "INFO";
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message d'avertissement (valeur="WARNING")
     */  
    public static final String TYPE_MSG_WARNING = "WARNING";
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message d'erreur valeur="ERROR")
     */   
    public static final String TYPE_MSG_ERROR = "ERROR";
    
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message de r‚ussite valeur="SUCCESS")
     */   
    public static final String TYPE_MSG_SUCCEED = "SUCCEED";    
    
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message d'echec valeur="SUCCESS")
     */   
    public static final String TYPE_MSG_FAILED = "FAILED";      
    
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message d'echec valeur="SUCCESS")
     */   
    public static final String TYPE_MSG_RETURN = "RETURN";     
    
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message de type StatusMessage)
     */   
    public static final String TYPE_MSG_STATUS = "STATUS";       
    
    
    /**
     * Type de message utilis‚ pour lors de l'‚mission 
     * d'un message data;
     */   
    public static final String TYPE_MSG_DATA = "DATA";         
    
    private String serviceName;
    private String messageText;    
    private String messageType;    
    private ArrayList<MessageData> datas = new ArrayList<MessageData>();  
    
    
    
    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service emetteur du message.
     */
    public AbstractMessage(String serviceName) {
        super();
        this.serviceName = serviceName;
    }
    
    /**
     * Constructeur de classe.
     * @param serviceName String : Nom du service emetteur du message.
     * @param messageType String : Type de message (@link com.arcadsoftware.serviceprovider.service.IOptionModifiers)
     * @param messageText String : Texte principal du message.
     */
    public AbstractMessage(String serviceName,String messageType, String messageText) {
        super();
        this.serviceName = serviceName;
        this.messageText = messageText;
        this.messageType = messageType;        
    } 
    /**
     * M‚thode permettant l'ajout du paire de donn‚es au message.<br>
     * @param key String : Cl‚ de la donn‚es
     * @param value String : Valeur de la donn‚es
     */
    public void addData(String key,String value) {
        datas.add(new MessageData(key,value));
    }    
    
    /**
     * M‚thode permettant de retourner le MessageData situ‚ … la position
     * pass‚ en param‚tre.<br>
     * @param index : int : Position de relecture du MessageData
     * @return MessageData positionn‚ … l'index pass‚ en paramŠtre.
     */
    public MessageData messageDataAt(int index){
    	return datas.get(index);
    }
    
    /**
     * M‚thode permettant de retourner le nombre de MessageData .<br>
     * @return int : Nombre de MessageData .
     */
    public int getMessageCount(){
    	return datas.size();
    }    
    
	/**
	 * @return Renvoie datas.
	 */
	public ArrayList getDatas() {
		return datas;
	}
	/**
	 * @param datas datas … d‚finir.
	 */
	public void setDatas(ArrayList<MessageData> datas) {
		this.datas = datas;
	}
	/**
	 * @return Renvoie messageText.
	 */
	public String getMessageText() {
		return messageText;
	}
	/**
	 * @param messageText messageText … d‚finir.
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	/**
	 * @return Renvoie messageType.
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType messageType … d‚finir.
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return Renvoie serviceName.
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName serviceName … d‚finir.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public boolean isErrorMessage() {
		if ((messageType.equals(TYPE_MSG_ERROR)) ||
		   (messageType.equals(TYPE_MSG_FAILED)) )
		   return true;
		return false;
	}
	public boolean isWarningMessage() {
		return messageType.equals(TYPE_MSG_WARNING);
	}	
	public boolean isInfoMessage() {
		return messageType.equals(TYPE_MSG_INFO);
	}		
	
}
