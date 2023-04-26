package com.arcadsoftware.aev.core.ui.monitors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.IMonitor;

/**
 * Monitor permettant de recevoir une notification de message 
 * � chaque changement d'�tape d'une Action et de renvoyer ces 
 * messages sur un fichier de log dont le chemin d'acc�s est 
 * pass� en param�tre.
 * 
 * @author dlelong
 *
 */
public class FileMonitor implements IMonitor {

	protected String actionDefinition;
	protected FileWriter writer;
	protected boolean verbose = true;

	/**
	 * Cr�ation d'un nouveau FileMonitor
	 * @param actionDefinition : Texte court expliquant ce que fait l'action �cout�e
	 * @param filename : Chemin d'acc�s au fichier dans lequel on va stocker les informations
	 * @param verbose : si 'true' �criture des messages de chaque �tape, sinon �criture de d�but et de fin d'action
	 */
	public FileMonitor(String actionDefinition, String filename, boolean verbose){
		super();
		this.actionDefinition = actionDefinition;
		this.verbose = verbose;
		init(filename);
	}
	
	//Par d�faut le monitor fonctionne en mode verbose
	public FileMonitor(String actionDefinition, String filename){
		this(actionDefinition, filename, true);
	}
	
	/**
	 * Initialisation du writer et cr�ation du fichier s'il n'existe pas
	 * @param filename
	 */
	protected void init(String filename){
		try {
			File f = new File(filename);
			if(!f.exists()){
				if(!f.createNewFile()) 
					return;
			}
			writer = new FileWriter(filename, true);		
		} catch (FileNotFoundException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		}	
	}

	public void afterExecute(String message, int numberOfStep) {
		if(verbose)
			write(message);
	}

	public void beforeExecute(String message, int numberOfStep) {
		if(verbose)
			write(message);
	}

	public void begin(String message, int totalNumberOfStep) {
		write(message);
	}

	public void end(String message) {
		write(message);
		dispose();
	}

	public void initialize(String message, int numberOfStep) {
		if(verbose)
			write(message);
	}

	public void progress(String message, int numberOfStep) {
		if(verbose)
			write(message);
	}

	/**
	 * Ajout � la fin du fichier de la d�finition de l'action suivi du message
	 * pass� en param�tre
	 * @param message
	 */
	protected void write(String message){
		try {
			String text = actionDefinition.concat(" : ").concat(message).concat("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			if(writer != null)
				writer.write(text,0,text.length());	
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		}
	}
	
	/**
	 * Lib�ration des ressources
	 */
	protected void dispose(){
		try {
			if(writer != null)
				writer.close();
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
		}
	}
}
