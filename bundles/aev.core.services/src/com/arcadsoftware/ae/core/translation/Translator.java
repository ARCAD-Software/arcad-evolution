package com.arcadsoftware.ae.core.translation;

public class Translator {
	private static Translator instance = new Translator();

	private static ITranslator translatorInstance = null;

	public static Translator getInstance() {
		return instance;
	}

	public static String resString(final String key) {
		if (translatorInstance != null) {
			return translatorInstance.resString(key);
		}
		return "";
	}

	public static String resString(final String key, final String[] substitionMessages) {
		String message = resString(key);
		if (!message.equals("")) {
			for (int i = 0; i < substitionMessages.length; i++) {
				message = message.replace("$" + i, substitionMessages[i]);
			}
		}
		return message;
	}

	private Translator() {
		super();
	}

	public static void setTranslator(final ITranslator t) {
		translatorInstance = t;
	}

}
