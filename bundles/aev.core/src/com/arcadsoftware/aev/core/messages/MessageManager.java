/*
 * Cr�� le 5 mai 04
 *
 */
package com.arcadsoftware.aev.core.messages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.tools.XMLTools;

/**
 * Gestionnaire des messages. Cette classe statique est appel�e dans toutes les op�ration � risque pour controler le bon
 * d�roulement de l'op�ration.
 *
 * @author MD
 */
public class MessageManager implements IDiagnosisProvider {
	private static Logger logger = Logger.getLogger(MessageManager.class.getName());
	
	private static List<Object> contextPlugins = new ArrayList<>();
	private static int firstBlockMessage = 0;
	private static final MessageManager instance = new MessageManager();
	public static final int LEVEL_BETATESTING = 2;

	public static final int LEVEL_DELETED = 0;

	public static final int LEVEL_DEVELOPMENT = 1;
	public static final int LEVEL_PRODUCTION = 4;
	private static final List<IMessagesListener> listenerList = new ArrayList<>();
	private static final List<Message> messages = new ArrayList<>();

	public static final int SHOW_COMPLETION = MessageDetail.COMPLETION;

	public static final int SHOW_DIAGNOSTIC = MessageDetail.DIAGNOSTIC;

	public static final int SHOW_ERROR = MessageDetail.ERROR;

	public static final int SHOW_EXCEPTION = MessageDetail.EXCEPTION;

	public static final int SHOW_WARNING = MessageDetail.WARNING;

	public static final int SHOW_ALL = SHOW_COMPLETION | SHOW_DIAGNOSTIC | SHOW_WARNING | SHOW_ERROR | SHOW_EXCEPTION;

	public static final int SHOW_ANYERROR = SHOW_WARNING | SHOW_ERROR | SHOW_EXCEPTION;

	

	/**
	 * Log and print an exception at level production.
	 *
	 * @param e:
	 *            an exception
	 */
	public static Message addAndPrintException(final Throwable e) {
		return addAndPrintException(e, LEVEL_PRODUCTION);
	}

	public static Message addAndPrintException(final Throwable e, final int level) {
		logger.log(Level.INFO, e.getMessage(), e);
		return addException(e, level);
	}

	public static Message addErrorMessage(final String command) {
		return addErrorMessage(command, "");
	}

	public static Message addErrorMessage(final String command, final String description) {
		return addMessage(command, MessageDetail.ERROR, description);
	}

	// <MR number="2018/00421" version="10.09.11" date="Aug 30, 2018" type="Enh" user="ACL">
	public static Message addException(final Throwable e) {
		return addException(e, LEVEL_PRODUCTION);
	}
	// </MR>

	/**
	 * Ajout d'un message li� au d�clenchement d'un exception.
	 * 
	 * @param e
	 * @param level
	 */
	public static Message addException(final Throwable e, final int level) {
		final String EXCEPTION_THROWN = CoreLabels.resString("MessageManager.ExceptionThrown");
		String m = e.getLocalizedMessage();
		if (m == null) {
			m = e.getMessage();
		}
		if (m == null) {
			m = EXCEPTION_THROWN;
		}
		final Message message = new Message(m, SHOW_EXCEPTION, level, EXCEPTION_THROWN);
		// <FM number="2011/00454" version="08.12.02" date="9 d�c. 2011" user="MLAFON">
		messages.add(message);
		fireAddMessage(message, e);
		// </FM>
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try (PrintWriter writer = new PrintWriter(out, true)) {
				e.printStackTrace(writer);
			}

			final String s = out.toString();
			if (s != null && s.length() > 0) {
				message.addDetail(SHOW_EXCEPTION, s);
			}
		} catch (final IOException e1) {
			message.addDetail(SHOW_EXCEPTION, e1.getLocalizedMessage());
		}
		if (level <= LEVEL_BETATESTING) {
			message.addDate();
		}
		fireMessageChanged(message);
		return message;
	}

	/**
	 * Ajoute un message d�crivant l'exception. (Message visible uniquement en mode BetaTest ou Developpement).
	 *
	 * @param e
	 *            l'exception associ�e au message.
	 * @return le messgae qui vient d'�tre ajout�.
	 */
	public static Message addExceptionBeta(final Throwable e) {
		return addException(e, LEVEL_BETATESTING);
	}

	/**
	 * Ajoute un message dans le gestionnaire. (Message visible dans tous les modes d'ex�cution). Le type du message est
	 * fixe, donc l'ajout de d�tails n'influera pas sur le type affich� du message.
	 * 
	 * @param command
	 *            : Commande ex�cut�e
	 * @param type
	 *            : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description
	 *            : Texte de l'erreur
	 */
	public static Message addFixedTypeMessage(final String command, final int type, final String description) {
		return addMessage(command, type, LEVEL_PRODUCTION, description, true);
	}

	public static void addListener(final IMessagesListener listener) {
		if (listenerList.indexOf(listener) == -1) {
			listenerList.add(listener);
		}
	}

	/**
	 * Ajout d'un message pr�cr��.
	 *
	 * @param msg
	 *            Le message � ajouter.
	 * @return
	 */
	public static Message addMessage(final Message msg) {
		messages.add(msg);
		fireAddMessage(msg, null);
		return msg;
	}

	/**
	 * Ajout d'un message avec un premier d�tail. (Seule op�ration cr�ant v�ritablement un objet Message).
	 *
	 * @param command
	 *            Le texte de la commande utilis� ou titre du message.
	 * @param type
	 *            Type du premier d�tail du message (MEssageDetail.*)
	 * @param level
	 *            Niveau lis� au niveau d'ex�cution de la machine LEVEL_*
	 * @param description
	 *            Texte du premier d�tail.
	 * @return le message cr��.
	 */
	public static Message addMessage(final String command, final int type, final int level, final String description) {
		return addMessage(command, type, level, description, false);
	}

	public static Message addMessage(final String command, final int type, final int level, final String description,
			final boolean fixedType) {
		return addMessage(new Message(command, type, level, description, fixedType));
	}

	/**
	 * Ajoute un message dans le gestionnaire. (Message visible dans tous les modes d'ex�cution).
	 * 
	 * @param command
	 *            : Commande ex�cut�e
	 * @param type
	 *            : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description
	 *            : Texte de l'erreur
	 */
	public static Message addMessage(final String command, final int type, final String description) {
		return addMessage(command, type, LEVEL_PRODUCTION, description);
	}

	/**
	 * Ajoute un message dans le gestionnaire. (Message visible uniquement dans les modes betatest et d�veloppement).
	 * 
	 * @param command
	 *            : Commande ex�cut�e
	 * @param type
	 *            : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description
	 *            : Texte de l'erreur
	 */
	public static Message addMessageBeta(final String command, final int type, final String description) {
		return addMessage(command, type, LEVEL_BETATESTING, description);
	}

	/**
	 * Ajoute un message dans le gestionnaire. (Message visible uniquement dans le mode d'ex�cution d�veloppement).
	 * 
	 * @param command
	 *            : Commande ex�cut�e
	 * @param type
	 *            : Type d'erreur g�n�r�e (voir MessageDetail)
	 * @param description
	 *            : Texte de l'erreur
	 */
	public static Message addMessageDev(final String command, final int type, final String description) {
		return addMessage(command, type, LEVEL_DEVELOPMENT, description);
	}

	/**
	 * D�bute un block de messages. La cha�ne blockContext peut d�finir un Message qui sera ajout� an ent�te du block,
	 * si cette cha�ne est nulle alors aucun message n'est ajout�.
	 *
	 * @param plugin
	 *            le plugin responsable du block de message (peut �tre null).
	 * @param blockContext
	 *            message englobant les messages jusqu'� terminaison du block.
	 */
	public static Message beginMessageBlock(final String blockContext) {
		if (contextPlugins.isEmpty()) {
			firstBlockMessage = messages.size();
		}

		if (blockContext != null) {
			return addMessage(blockContext, MessageDetail.COMPLETION, LEVEL_BETATESTING,
					CoreLabels.resString("MessageManager.ProcessBegin")); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Supprime tous les messages du gestionnaire. Attention cette suppression entraine la perte de toutes les
	 * informations de d�bugage.
	 */
	public static void clear() {

		messages.clear();
		contextPlugins.clear();
		fireMessageDeleted(null);
	}

	/**
	 * Termine un block et retourne la liste des messages � afficher...
	 *
	 * @param dialogParentShell
	 */
	public static List<Message> endMessageBlock(final int showParam) {
		if (contextPlugins.isEmpty() && messages.size() > firstBlockMessage) {
			final List<Message> filteredMessages = new ArrayList<>(messages.size() - firstBlockMessage);

			for (int i = firstBlockMessage; i < messages.size(); i++) {
				final Message message = messages.get(i);
				if ((showParam & message.getMaxDetailsType()) != 0) {
					filteredMessages.add(message);
				}
			}
			if (!filteredMessages.isEmpty()) {
				return filteredMessages;
			}
		}
		// return Collections.emptyList();
		return null;
	}

	/**
	 * Exporte les messages contenus dans la liste sous la forme d'un fichier XML.
	 *
	 * @param fileName
	 *            Nom du fichier XML.
	 * @param messages
	 *            Liste des messages � exporter (si cette liste est nulle c'est la liste compl�te qui est utilis�e).
	 */
	public static void exportMessagesToXMLFile(final String fileName, final List<Message> messageList) {
		List<Message> msgs = messageList;
		if (messageList == null) {
			msgs = messages;
		}
		final File file = new File(fileName);
		try (final FileOutputStream fileOutput = FileUtils.openOutputStream(file)) {
			// This will create the parent path and try to open the file
		} catch (final Exception e) {
			addException(e);
			return;
		}

		try {
			final Document document = XMLTools.createNewXMLDocument();
			final Element root = document.createElement("messageList");

			for (final Message message : msgs) {
				final Element msg = document.createElement("message");
				msg.setAttribute("command", message.getCommand()); //$NON-NLS-1$
				final String level;
				switch (message.getLevel()) {
				case LEVEL_PRODUCTION:
					level = "production"; //$NON-NLS-1$
					break;

				case LEVEL_BETATESTING:
					level = "betatest"; //$NON-NLS-1$
					break;

				case LEVEL_DEVELOPMENT:
					level = "development"; //$NON-NLS-1$
					break;

				default:
					level = "unknown"; //$NON-NLS-1$

				}
				msg.setAttribute("level", level); //$NON-NLS-1$

				for (final MessageDetail detail : message.getDetails()) {
					final Element dtl = document.createElement("detail");
					final String type;
					switch (detail.getType()) {
					case MessageDetail.COMPLETION:
						type = "completion"; //$NON-NLS-1$
						break;

					case MessageDetail.DIAGNOSTIC:
						type = "diagnostic"; //$NON-NLS-1$
						break;

					case MessageDetail.ERROR:
						type = "error"; //$NON-NLS-1$
						break;

					case MessageDetail.EXCEPTION:
						type = "exception"; //$NON-NLS-1$
						break;

					case MessageDetail.WARNING:
						type = "warning"; //$NON-NLS-1$
						break;

					default:
						type = "unknown"; //$NON-NLS-1$
					}
					dtl.setAttribute("type", type); //$NON-NLS-1$
					if (!StringTools.isEmpty(detail.getDescription())) {
						final CDATASection desc = document.createCDATASection(detail.getDescription());
						dtl.appendChild(desc);
					}
					msg.appendChild(dtl);
				}
				root.appendChild(msg);
			}
			document.appendChild(root);
			XMLTools.writeXMLDocumentToFile(document, file, StandardCharsets.UTF_8.name());
		} catch (final Exception e) {
			addExceptionBeta(e);
		}
	}

	protected static void fireAddMessage(final Message message, final Throwable e) {
		for (final IMessagesListener element : listenerList) {
			element.newMessageAdded(message, e);
		}
	}

	public static void fireMessageChanged(final Message message) {
		for (final IMessagesListener element : listenerList) {
			element.messageChanged(message);
		}
	}

	protected static void fireMessageDeleted(final Message message) {
		for (final IMessagesListener element : listenerList) {
			element.messageDeleted(message);
		}
	}

	public static MessageManager getDefault() {
		return instance;
	}

	/**
	 * @param index
	 *            : position du message
	 * @return Renvoit le message situ� � la position "index". Si index est inf�rieur � 0 ou si index est sup�rieur � la
	 *         taille du gestionnaire la valeur null est retourn�e.
	 */
	public static Message getMessageAt(final int index) {
		if (index > -1 && index < messages.size()) {
			return messages.get(index);
		}
		return null;
	}

	/**
	 * @return
	 */
	public static List<Message> getMessagesList() {
		return messages;
	}

	/**
	 * @return Le nombre de message du gestionnaire
	 */
	public static int messageCount() {
		return messages.size();
	}

	public static void print() {
		for (int i = 0; i < messageCount(); i++) {
			Optional.ofNullable(getMessageAt(i)).ifPresent(Message::print);
		}
	}

	public static void removeListener(final IMessagesListener listener) {
		final int i = listenerList.indexOf(listener);
		if (i != -1) {
			listenerList.remove(i);
		}
	}

	/**
	 * Supprime le message situ� � la position "index".Si index est inf�rieur � 0 ou si index est sup�rieur � la taille
	 * du gestionnaire aucune suppression n'est effectu�e
	 * 
	 * @param index
	 *            : position du message � supprimer
	 */
	public static void removeMessageAt(final int index) {
		if (index > -1 && index < messages.size()) {
			final Message message = messages.remove(index);
			fireMessageDeleted(message);
		}
	}

	public MessageManager() {
		super();
	}

	@Override
	public String getDiagnosisContent() {
		final char ln = Character.LINE_SEPARATOR;
		final StringBuilder content = new StringBuilder();

		// Copy the array to avoir concurrent modification exception
		for (final Message message : messages) {
			content.append(message.toString())
					.append(ln).append(ln);
		}

		return content.toString();
	}

	@Override
	public String getDiagnosisFileName() {
		return "Message_manager.log";
	}
}
