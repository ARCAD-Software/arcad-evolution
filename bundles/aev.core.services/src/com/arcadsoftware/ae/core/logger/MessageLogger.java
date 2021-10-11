
package com.arcadsoftware.ae.core.logger;

import java.util.ArrayList;
import java.util.List;

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
 * Cette classe est une classe d'aide permettant l'envoi de message vers la sortie standard.
 */
public class MessageLogger {
	private static boolean enabled = true;
	private static MessageLogger instance = null;
	private static boolean loaded = false;

	/**
	 * Envoi le message passé en paramétre.
	 *
	 * @param message
	 *            AbstractMessage : Message é envoyer.
	 */
	public static void finalizeMessage() {
		for (final AbstractMessageRouter mr : getInstance().messageRouters) {
			mr.finalizeMessages();
		}
	}

	/**
	 * Envoi le message passé en paramétre.
	 *
	 * @param message
	 *            AbstractMessage : Message é envoyer.
	 */
	public static String[] getData() {
		final String[] result = new String[getInstance().messageRouters.size()];
		for (int i = 0; i < getInstance().messageRouters.size(); i++) {
			final AbstractMessageRouter mr = getInstance().messageRouters.get(i);
			result[i] = mr.getData();
		}
		return result;
	}

	public static MessageLogger getInstance() {
		if (instance == null) {
			instance = new MessageLogger();
		}
		return instance;
	}

	/**
	 * Envoi le message passé en paramétre.
	 *
	 * @param message
	 *            AbstractMessage : Message é envoyer.
	 */
	public static void initializeMessage() {
		for (final AbstractMessageRouter element : getInstance().messageRouters) {
			final AbstractMessageRouter mr = element;
			mr.initializeMessages();
		}
	}

	public static synchronized boolean isEnabled() {
		return enabled;
	}

	public static synchronized boolean isLoaded() {
		return loaded;
	}

	public static boolean isStatusOk(String message) {
		if (message.endsWith(".")) {
			message = message.substring(0, message.length() - 1);
		}
		return XmlMessageFormatter.isStatusOk(message, AbstractMessage.TYPE_MSG_STATUS);
	}

	/**
	 * Envoi un message de type erreur ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte principal du message.
	 */
	public static void sendErrorMessage(final String serviceName, final String message) {
		sendMessage(new ErrorMessage(serviceName, message));
	}

	/**
	 * Envoi un message de type erreur ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param e
	 *            : Throwable Objet de type Throwable.
	 */
	public static void sendErrorMessage(final String serviceName, final Throwable e) {
		sendMessage(new ErrorMessage(serviceName, Utils.stackTrace(e)));
	}

	/**
	 * Envoi un message de type FAILED ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte pricncipal du message.
	 */
	public static void sendFailedMessage(final String serviceName, final String message) {
		sendMessage(new FailedMessage(serviceName, message));
	}

	/**
	 * Envoi un message de type FAILED ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param e
	 *            : Throwable Objet de type Throwable.
	 */
	public static void sendFailedMessage(final String serviceName, final Throwable e) {
		sendMessage(new FailedMessage(serviceName, Utils.stackTrace(e)));
	}

	/**
	 * Envoi un message de type information ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte pricncipal du message.
	 */
	public static void sendInfoMessage(final String serviceName, final String message) {
		sendMessage(new InfoMessage(serviceName, message));
	}

	/**
	 * Envoi un message de type information ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param e
	 *            : Throwable Objet de type Throwable.
	 */
	public static void sendInfoMessage(final String serviceName, final Throwable e) {
		sendMessage(new InfoMessage(serviceName, Utils.stackTrace(e)));
	}

	/**
	 * Envoi le message passé en paramétre.
	 *
	 * @param message
	 *            AbstractMessage : Message é envoyer.
	 */
	public static void sendMessage(final AbstractMessage message) {
		for (final AbstractMessageRouter element : getInstance().messageRouters) {
			final AbstractMessageRouter mr = element;
			mr.interceptMessage(message);
		}
	}

	public static void sendStatusMessage(final String serviceName, final boolean b) {
		sendMessage(new StatusMessage(serviceName, b));
	}

	/**
	 * Envoi un message de type SUCCEED ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte pricncipal du message.
	 */
	public static void sendSucceedMessage(final String serviceName, final String message) {
		sendMessage(new SucceedMessage(serviceName, message));
	}

	/**
	 * Envoi un message de type SUCCEED ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param e
	 *            : Throwable Objet de type Throwable.
	 */
	public static void sendSucceedMessage(final String serviceName, final Throwable e) {
		sendMessage(new SucceedMessage(serviceName, Utils.stackTrace(e)));
	}

	/**
	 * Envoi un message de type Warning ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte pricncipal du message.
	 */
	public static void sendWarningMessage(final String serviceName, final String message) {
		sendMessage(new WarningMessage(serviceName, message));
	}

	/**
	 * Envoi un message de type Warning ayant pour texte principal <code>message</code>.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param e
	 *            : Throwable Objet de type Throwable.
	 */
	public static void sendWarningMessage(final String serviceName, final Throwable e) {
		sendMessage(new WarningMessage(serviceName, Utils.stackTrace(e)));
	}

	public static synchronized void setEnabled(final boolean enabled) {
		MessageLogger.enabled = enabled;
	}

	public static void setInstance(final MessageLogger instance) {
		MessageLogger.instance = instance;
		loaded = true;
	}

	private List<AbstractMessageRouter> messageRouters = new ArrayList<>();

	protected MessageLogger() {
		super();
	}

	/**
	 * Renvoit
	 *
	 * @return the messageRouters ArrayList<AbstractMessageRouter> :
	 */
	public List<AbstractMessageRouter> getMessageRouters() {
		return messageRouters;
	}

	public void setMessageRouters(final List<AbstractMessageRouter> list) {
		messageRouters = list;
	}
}
