package com.arcadsoftware.ae.core.translation;


public class Translator {
	private static Translator instance = new Translator();
		
	private static ITranslator translator = null;
	
	private Translator(){
		super();
	}

	public static Translator getInstance() {
		return instance;
	}

	public void setTranslator(ITranslator t){
		translator = t; 
	}
	
	public static String resString(String key) {
		if (translator!=null) {
			return translator.resString(key);
		}
		return "";
	}	
	
	public static String resString(String key,String[] substitionMessages) {
		String message = resString(key);
		if (!message.equals("")){
			for (int i=0;i<substitionMessages.length;i++){			
				message.replace("$"+i,substitionMessages[i]);
			}
		}
		return message;
	}	
	
	
}
