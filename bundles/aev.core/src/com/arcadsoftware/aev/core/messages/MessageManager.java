/*
 * Cr�� le 5 mai 04
 *
 */
package com.arcadsoftware.aev.core.messages;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Gestionnaire des messages. Cette classe statique est appel�e dans
 * toutes les op�ration � risque pour controler le bon d�roulement de l'op�ration.
 * 
 * @author MD
 */
public class MessageManager implements IDiagnosisProvider {
	
	public static final int LEVEL_PRODUCTION = 4;
	public static final int LEVEL_BETATESTING = 2;
	public static final int LEVEL_DEVELOPMENT = 1;
	public static final int LEVEL_DELETED = 0;

	public static int level_PluginsStatus = LEVEL_BETATESTING;

	/** 
	 * Affiche les d�tails de messages de type COMPLETION.<br>
	 * Equivalent � MessageDetail.COMPLETION, peut �tre utilis� en tant
	 * que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_COMPLETION = MessageDetail.COMPLETION;
	/** 
	 * Affiche les d�tails de messages de type DIAGNOSTIC.<br>
	 * Equivalent � MessageDetail.DIAGNOSTIC, peut �tre utilis� en tant
	 * que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_DIAGNOSTIC = MessageDetail.DIAGNOSTIC;
	/** 
	 * Affiche les d�tails de messages de type WARNING.<br>
	 * Equivalent � MessageDetail.WARNING, peut �tre utilis� en tant
	 * que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_WARNING = MessageDetail.WARNING;
	/** 
	 * Affiche les d�tails de messages de type ERROR.<br>
	 * Equivalent � MessageDetail.ERROR, peut �tre utilis� en tant
	 * que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_ERROR = MessageDetail.ERROR;
	/** 
	 * Affiche les d�tails de messages de type EXCEPTION.<br>
	 * Equivalent � MessageDetail.EXCEPTION, peut �tre utilis� en tant
	 * que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_EXCEPTION = MessageDetail.EXCEPTION;
	/** 
	 * Affiche tous les d�tails de messages de type erreur (Warning, erreur, ou exception).<br>
	 * Ne peut pas �tre utilis� en tant que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_ANYERROR = SHOW_WARNING | SHOW_ERROR | SHOW_EXCEPTION;
	/** 
	 * Affiche tous les d�tails de messages.<br>
	 * Ne peut pas �tre utilis� en tant que param�tre des methodes d'ajout de messages et d�tails.
	 */
	public static final int SHOW_ALL = SHOW_COMPLETION | SHOW_DIAGNOSTIC | SHOW_WARNING | SHOW_ERROR | SHOW_EXCEPTION;
	


	// Compter de blocks d'update...
	private static ArrayList<Object> contextPlugins = new ArrayList<Object>();
	private static final ArrayList<IMessagesListener> listenerList = new ArrayList<IMessagesListener>();
	
	private static final ArrayList<Message> messages = new ArrayList<Message>();
	private static final MessageManager instance = new MessageManager();
	private static int firstBlockMessage = 0;

	
//	/**
//	 * Listener utilis� pour r�cup�rer les log d'un plugin (durant un block process).
//	 */
//	private static ILogListener arcadLogListener = new ILogListener() {
//		public void logging(IStatus status, String plugin) {
//
//			// Mapping entre s�verit� et message type.
//			Message message = addMessage(plugin,status.getSeverity() << 1,level_PluginsStatus,status.getMessage());
//
//			IStatus[] children = status.getChildren();
//			for (int i=0; i < children.length;i++) {
//				message.addDetail(children[i].getSeverity() << 1,children[i].getMessage());
//			}
//		}
//	}; 
//	
	/**
	 * 
	 */
	public MessageManager() {
		super();
	}
	
	public static MessageManager getDefault() {
		return instance;
	}
	
	public static void addListener(IMessagesListener listener) {
		if (listenerList.indexOf(listener) == -1)
			listenerList.add(listener);
	}

	public static void removeListener(IMessagesListener listener) {
		int i = listenerList.indexOf(listener); 
		if (i != -1)
			listenerList.remove(i);
	}
	
	protected static void fireAddMessage(Message message, Throwable e) {
		for (int i = 0; i < listenerList.size(); i++) {
			((IMessagesListener)listenerList.get(i)).newMessageAdded(message, e);
		}
	}

	protected static void fireMessageDeleted(Message message) {
		for (int i = 0; i < listenerList.size(); i++) {
			((IMessagesListener)listenerList.get(i)).messageDeleted(message);
		}
	}

	public static void fireMessageChanged(Message message) {
		for (int i = 0; i < listenerList.size(); i++) {
			((IMessagesListener)listenerList.get(i)).messageChanged(message);
		}
	}
	
	/**
	 * Supprime tous les messages du gestionnaire.
	 * Attention cette suppression entraine la perte 
	 * de toutes les informations de d�bugage.
	 * 
	 */
	public static void clear() {

		messages.clear();
		contextPlugins.clear();
		fireMessageDeleted(null);
	}
	
	/**
	 * D�bute un block de messages.
	 * La cha�ne blockContext peut d�finir un Message qui sera ajout� an ent�te du block, si 
	 * cette cha�ne est nulle alors aucun message n'est ajout�.
	 * 
	 * @param plugin le plugin responsable du block de message (peut �tre null). 
	 * @param blockContext message englobant les messages jusqu'� terminaison du block. 
	 */
	public static Message beginMessageBlock(String blockContext) {
		if (contextPlugins.isEmpty())
			firstBlockMessage = messages.size();

		if (blockContext != null) {
			return addMessage(blockContext,MessageDetail.COMPLETION,LEVEL_BETATESTING,
					CoreLabels.resString("MessageManager.ProcessBegin")); //$NON-NLS-1$
		}
		return null;
	}
	
	/**
	 * Termine un block et retourne la liste des messages � afficher...
	 * 
	 * @param dialogParentShell 
	 */
	public static ArrayList<Message> endMessageBlock(int showParam) {
		if (contextPlugins.isEmpty() && (messages.size() > firstBlockMessage)) {
			
			ArrayList<Message> filteredMessages = new ArrayList<Message>(messages.size() - firstBlockMessage);

			for(int i = firstBlockMessage;i < messages.size(); i++) {
				Message message = messages.get(i);
				//SJU: retour homologation defect 1377 (affichage intempestif du dialog)
				//if (message.isVisibleTo(showParam)) {
				if ((showParam & message.getMaxDetailsType()) != 0 ) {
					filteredMessages.add(message);
				}
			}
			if (filteredMessages.size() == 0)
				return null;
			return filteredMessages;
		} else {
			return null;
		}
	}
	
	/**
	 * Ajoute un message dans le gestionnaire.
	 * (Message visible dans tous les modes d'ex�cution).
	 * @param command : Commande ex�cut�e
	 * @param type : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description : Texte de l'erreur
	 */
	public static Message addMessage(String command,int type,String description){
		return addMessage(command,type,LEVEL_PRODUCTION,description);
	}
	
	public static Message addErrorMessage(String command, String description){
		return addMessage(command, MessageDetail.ERROR, description);
	}
	public static Message addErrorMessage(String command){
		return addErrorMessage(command, "");
	}

	/**
	 * Ajoute un message dans le gestionnaire. (Message visible dans tous les modes d'ex�cution).
	 * Le type du message est fixe, donc l'ajout de d�tails n'influera pas sur le type affich� du message.
	 * @param command : Commande ex�cut�e
	 * @param type : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description : Texte de l'erreur
	 */
	public static Message addFixedTypeMessage(String command, int type, String description){
		return addMessage(command,type,LEVEL_PRODUCTION,description,true);
	}
	
	/**
	 * Ajoute un message dans le gestionnaire.
	 * (Message visible uniquement dans les modes betatest et d�veloppement).
	 * @param command : Commande ex�cut�e
	 * @param type : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description : Texte de l'erreur
	 */
	public static Message addMessageBeta(String command,int type,String description){
		return addMessage(command,type,LEVEL_BETATESTING,description);
	}

	/**
	 * Ajoute un message dans le gestionnaire.
	 * (Message visible uniquement dans le mode d'ex�cution d�veloppement).
	 * @param command : Commande ex�cut�e
	 * @param type : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description : Texte de l'erreur
	 */
	public static Message addMessageDev(String command,int type,String description){
		return addMessage(command,type,LEVEL_DEVELOPMENT,description);
	}

	/**
	 * Ajoute un message d�crivant l'exception.
	 * (Message visible uniquement en mode BetaTest ou Developpement).
	 * 
	 * @param e l'exception associ�e au message.
	 * @return le messgae qui vient d'�tre ajout�.
	 */
	public static Message addExceptionBeta(Throwable e) {
		return addException(e,LEVEL_BETATESTING);
	}

	//<MR number="2018/00421" version="10.09.11" date="Aug 30, 2018" type="Enh" user="ACL">
	public static Message addException(Throwable e) {
		return addException(e,LEVEL_PRODUCTION);
	}
	//</MR>

	/**
	 * Ajout d'un message li� au d�clenchement d'un exception.
	 * @param e
	 * @param level
	 */
	public static Message addException(Throwable e,int level) {
		final String EXCEPTION_THROWN = CoreLabels.resString("MessageManager.ExceptionThrown");
		String m = e.getLocalizedMessage();
		if (m == null)
			m = e.getMessage();
		if (m == null)
			m = EXCEPTION_THROWN;
		Message message = new Message(m,SHOW_EXCEPTION, level, EXCEPTION_THROWN);
		//<FM number="2011/00454" version="08.12.02" date="9 d�c. 2011" user="MLAFON">
		messages.add(message);
		fireAddMessage(message,e);
		//</FM>
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(out,true));
			out.flush();
			String s = out.toString();
			out.close();
			if ((s != null) && (s.length() > 0)) {
				message.addDetail(SHOW_EXCEPTION,s);
			}
		} catch (IOException e1) {
			message.addDetail(SHOW_EXCEPTION,e1.getLocalizedMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (level <= LEVEL_BETATESTING) {
			message.addDate();
		}
		fireMessageChanged(message);
		return message;
	}
	
	/**
	 * Log and print an exception at level production.
	 * 
	 * @param e: an exception
	 */
	public static Message addAndPrintException(Throwable e){
		return addAndPrintException(e, LEVEL_PRODUCTION);
	}
	
	public static Message addAndPrintException(Throwable e, int level){		
		e.printStackTrace();
		return addException(e, level);
	}
	
	/**
	 * Ajout d'un message avec un premier d�tail.
	 * (Seule op�ration cr�ant v�ritablement un objet Message). 
	 * 
	 * @param command Le texte de la commande utilis� ou titre du message. 
	 * @param type Type du premier d�tail du message (MEssageDetail.*)
	 * @param level Niveau lis� au niveau d'ex�cution de la machine LEVEL_*
	 * @param description Texte du premier d�tail.
	 * @return le message cr��.
	 */	
	public static Message addMessage(String command,int type,int level,String description){
		return addMessage(command, type, level, description, false);
	}

	public static Message addMessage(String command,int type,int level,String description, boolean fixedType){
		return addMessage(new Message(command,type,level,description,fixedType));
	}
	
	/**
	 * Ajout d'un message pr�cr��.
	 * 
	 * @param msg Le message � ajouter.
	 * @return
	 */
	public static Message addMessage(Message msg) {
		//ML: suppression du filtre en dur...
		//if (level <= ArcadCorePlugin.getDefault().getMessagesLevel()) {
			messages.add(msg);
			fireAddMessage(msg, null);
		//}
		return msg;
	}

	/**
	 *  
	 * @param index : position du message
	 * @return Renvoit le message situ� � la position "index". Si index est inf�rieur � 0 ou si index est sup�rieur � la taille du 
	 * gestionnaire la valeur null est retourn�e.  
	 */
	public static Message getMessageAt(int index) {
		if ((index>-1) && (index<messages.size())) {		
			return (Message)messages.get(index);
		}
		return null;
	}	
	
	/**
	 * Supprime le message situ� � la position "index".Si index est inf�rieur 
	 * � 0 ou si index est sup�rieur � la taille du gestionnaire aucune
	 * suppression n'est effectu�e  
	 * @param index : position du message � supprimer
	 */
	public static void removeMessageAt(int index) {
		if ((index>-1) && (index<messages.size())) {		
			Message message = (Message)messages.remove(index);
			fireMessageDeleted(message);
		}
	}	
	/** 
	 * @return Le nombre de message du gestionnaire
	 */
	public static int messageCount() {
		return messages.size();
	}	
	
	public static void print() {
		for (int i = 0; i < messageCount(); i++) {
			getMessageAt(i).print();	
		}
	}

	/**
	 * @return
	 */
	public static ArrayList<Message> getMessagesList() {
		return messages;
	}

	/**
	 * Exporte les messages contenus dans la liste sous la forme d'un fichier
	 * XML.
	 * 
	 * @param fileName
	 *            Nom du fichier XML.
	 * @param messages
	 *            Liste des messages � exporter (si cette liste est nulle c'est
	 *            la liste compl�te qui est utilis�e).
	 */
	public static void exportMessagesToXMLFile(String fileName, AbstractList<Message> messageList) {
		AbstractList<Message> msgs = messageList;
		if (messageList == null)
			msgs = messages;

		// TODO [DL] voir pourquoi file n'est jamais utilis�
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			addExceptionBeta(e);
			return;
		}

		Document document = null;
		try {
			document = DocumentHelper.parseText("<messageList></messageList>"); //$NON-NLS-1$

			document.addDocType("messageList", "SYSTEM", "http://eclipse.arcadsoftware.com/xml/messagesexport.dtd"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			Element root = document.getRootElement();

			for (int i = 0; i < msgs.size(); i++) {
				if (msgs.get(i) instanceof Message) {
					Message message = (Message) msgs.get(i);
					Element msg = root.addElement("message"); //$NON-NLS-1$
					msg.addAttribute("command", message.getCommand()); //$NON-NLS-1$
					String level = StringTools.EMPTY;
					switch (message.getLevel()) {
					case LEVEL_PRODUCTION: {
						level = "production"; //$NON-NLS-1$
						break;
					}
					case LEVEL_BETATESTING: {
						level = "betatest"; //$NON-NLS-1$
						break;
					}
					case LEVEL_DEVELOPMENT: {
						level = "development"; //$NON-NLS-1$
						break;
					}
					default: {
						level = "unknown"; //$NON-NLS-1$
					}
					}
					msg.addAttribute("level", level); //$NON-NLS-1$

					for (int j = 0; j < message.detailCount(); j++) {
						MessageDetail detail = message.getDetailAt(j);
						Element dtl = msg.addElement("detail"); //$NON-NLS-1$
						String type = StringTools.EMPTY;
						switch (detail.getType()) {
						case MessageDetail.COMPLETION: {
							type = "completion"; //$NON-NLS-1$
							break;
						}
						case MessageDetail.DIAGNOSTIC: {
							type = "diagnostic"; //$NON-NLS-1$
							break;
						}
						case MessageDetail.ERROR: {
							type = "error"; //$NON-NLS-1$
							break;
						}
						case MessageDetail.EXCEPTION: {
							type = "exception"; //$NON-NLS-1$
							break;
						}
						case MessageDetail.WARNING: {
							type = "warning"; //$NON-NLS-1$
							break;
						}
						default:
							type = "unknown"; //$NON-NLS-1$
						}
						dtl.addAttribute("type", type); //$NON-NLS-1$
						msg.addCDATA((detail.getDescription() != null ? detail.getDescription() : StringTools.EMPTY)
								.concat("\n")); //$NON-NLS-1$ 
					}
				}
			}
		} catch (DocumentException e) {
			addExceptionBeta(e);
		}
		if (document != null) {
			try {
				file.write(document.asXML().getBytes());
				file.close();
			} catch (IOException e) {
				addExceptionBeta(e);
			}
		}
	}

	// public static void exportMessagesToXMLFile(String fileName,ArrayList
	// messageList) {

	// if (messageList == null)
	// messageList = messages;

	// // TODO [Optionnel] Ajouter le support XML, incompatible avec la jt400
	// utilis�e et la jvm 1.3.

	// FileOutputStream file;
	// try {
	// file = new FileOutputStream(fileName);
	// } catch (FileNotFoundException e) {
	// addExceptionBeta(e);
	// return;
	// }

	// try {
	//	file.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n".getBytes()); //$NON-NLS-1$
	//	file.write("<?xml-stylesheet type=\"text/xsl\" href=\"http://eclipse.arcadsoftware.com/xml/messagesexport.xsl\"?>\n".getBytes()); //$NON-NLS-1$
	//	file.write("<!DOCTYPE messageList SYSTEM \"http://eclipse.arcadsoftware.com/xml/messagesexport.dtd\">".getBytes()); //$NON-NLS-1$
	//	file.write("<messageList>\n".getBytes()); //$NON-NLS-1$

	// for (int i = 0; i < messageList.size(); i++) {
	// if (messageList.get(i) instanceof Message) {
	// Message message = (Message) messageList.get(i);
	//	file.write("<message command=\"".getBytes());  //$NON-NLS-1$
	// file.write(message.getCommand().getBytes());
	//	file.write("\" level=\"".getBytes()); //$NON-NLS-1$
	// switch (message.getLevel()) {
	// case LEVEL_PRODUCTION : {
	//	file.write("production".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case LEVEL_BETATESTING : {
	//	file.write("betatest".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case LEVEL_DEVELOPMENT : {
	//	file.write("development".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// default: {
	//	file.write("unknown".getBytes()); //$NON-NLS-1$
	// }
	// }
	//	file.write("\">\n".getBytes()); //$NON-NLS-1$
	// for(int j = 0; j < message.detailCount(); j++) {
	// MessageDetail detail = message.getDetailAt(j);
	//	file.write("<detail type=\"".getBytes()); //$NON-NLS-1$
	// switch (detail.getType()) {
	// case MessageDetail.COMPLETION : {
	//	file.write("completion".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case MessageDetail.DIAGNOSTIC : {
	//	file.write("diagnostic".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case MessageDetail.ERROR : {
	//	file.write("error".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case MessageDetail.EXCEPTION : {
	//	file.write("exception".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// case MessageDetail.WARNING : {
	//	file.write("warning".getBytes()); //$NON-NLS-1$
	// break;
	// }
	// default :
	//	file.write("unknown".getBytes()); //$NON-NLS-1$
	// }
	//	file.write("\"><![CDATA[".getBytes()); //$NON-NLS-1$
	// if (detail.getDescription() != null)
	// file.write(detail.getDescription().getBytes());
	//	file.write("]]></detail>\n".getBytes()); //$NON-NLS-1$
	// }
	//	file.write("</message>\n".getBytes()); //$NON-NLS-1$
	// }
	// }
	//	file.write("</messageList>".getBytes()); //$NON-NLS-1$
	// } catch (IOException e2) {
	// addExceptionBeta(e2);
	// }

	// try {
	// file.close();
	// } catch (IOException e1) {
	// addExceptionBeta(e1);
	// }
	// // VERSION PROPRE :
	// // Probl�me d'accessibilit� du parser xml dans la jdk1.3 ... il faut
	// mettre en ligne J2EE.jar.

	// // DocumentBuilderFactory dFactory =
	// DocumentBuilderFactory.newInstance();
	// // DocumentBuilder dBuilder;
	// // try {
	// // dBuilder = dFactory.newDocumentBuilder();
	// // } catch (ParserConfigurationException e3) {
	// // addExceptionBeta(e3);
	// // return;
	// // }
	// // Document doc = dBuilder.newDocument();

	// // // Attache la fiche de mise en forme d'arcad.
	////	ProcessingInstruction stylesheet = doc.createProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"http://eclipse.arcadsoftware.com/xml/messagesexport.xsl\""); //$NON-NLS-1$ //$NON-NLS-2$
	// // doc.appendChild(stylesheet);

	////	Element mesListElement = doc.createElement("messageList"); //$NON-NLS-1$
	// // doc.appendChild(mesListElement);

	// // for (int i = 0; i < messageList.size(); i++) {
	// // if (messageList.get(i) instanceof Message) {
	// // Message message = (Message) messageList.get(i);
	////	Element mesElement = doc.createElement("message"); //$NON-NLS-1$
	////	mesElement.setAttribute("command",message.getCommand()); //$NON-NLS-1$
	// // mesListElement.appendChild(mesElement);
	// // for(int j = 0; j < message.detailCount(); j++) {
	// // MessageDetail detail = message.getDetailAt(j);
	////	Element detElement = doc.createElement("detail"); //$NON-NLS-1$
	// // switch (detail.getType()) {
	// // case MessageDetail.COMPLETION : {
	////	detElement.setAttribute("type","completion"); //$NON-NLS-1$ //$NON-NLS-2$
	// // break;
	// // }
	// // case MessageDetail.DIAGNOSTIC : {
	////	detElement.setAttribute("type","diagnostic"); //$NON-NLS-1$ //$NON-NLS-2$
	// // break;
	// // }
	// // case MessageDetail.ERROR : {
	////	detElement.setAttribute("type","error"); //$NON-NLS-1$ //$NON-NLS-2$
	// // break;
	// // }
	// // case MessageDetail.EXCEPTION : {
	////	detElement.setAttribute("type","exception"); //$NON-NLS-1$ //$NON-NLS-2$
	// // break;
	// // }
	// // case MessageDetail.WARNING : {
	////	detElement.setAttribute("type","warning"); //$NON-NLS-1$ //$NON-NLS-2$
	// // break;
	// // }
	////	default : detElement.setAttribute("type","unknown"); //$NON-NLS-1$ //$NON-NLS-2$
	// // }
	// // mesElement.appendChild(detElement);
	// //
	// detElement.appendChild(doc.createCDATASection(detail.getDescription()));
	// // }
	// // }
	// // }

	// //Create a transformer to write the file out
	// // TransformerFactory tFactory = TransformerFactory.newInstance();
	// // Transformer transformer;
	// // try {
	// // transformer = tFactory.newTransformer();
	////	transformer.setOutputProperty(OutputKeys.ENCODING,"iso-8859-1"); //$NON-NLS-1$
	////	transformer.setOutputProperty(OutputKeys.VERSION,"1.0"); //$NON-NLS-1$
	////	transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://eclipse.arcadsoftware.com/xml/messagesexport.dtd"); //$NON-NLS-1$
	////	transformer.setOutputProperty(OutputKeys.INDENT,"yes"); //$NON-NLS-1$
	// // } catch (TransformerConfigurationException e) {
	// // addExceptionBeta(e);
	// // return;
	// // }

	// // StreamResult result;
	// // try {
	// // result = new StreamResult(new FileOutputStream(fileName));
	// // } catch (FileNotFoundException e1) {
	// // addExceptionBeta(e1);
	// // return;
	// // }
	// // try {
	// // transformer.transform(new DOMSource(doc), result);
	// // } catch (TransformerException e2) {
	// // addExceptionBeta(e2);
	// // return;
	// // }
	// // try {
	// // result.getOutputStream().close();
	// // } catch (IOException e4) {
	// // addExceptionBeta(e4);
	// // }
	// }
	

	@Override
	public String getDiagnosisFileName() {
		return "Message_manager.log";
	}

	@Override
	public String getDiagnosisContent() {
		char ln = Character.LINE_SEPARATOR;
		StringBuffer content = new StringBuffer();
		
		//Copy the array to avoir concurrent modification exception
		for(Message message : messages){
			content	.append(message.toString())
					.append(ln).append(ln); 
		}
		
		return content.toString();
	}	


}
