/*
 * Créé le 20 nov. 08
 *
 * TODO Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre - Préférences - Java - Style de code - Modèles de code
 */
package com.arcadsoftware.ae.core.ant;


import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Target;


import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;


public class ArcadAntExecutor {
	
	public static final int MSG_ERROR = 0;
	public static final int MSG_OK = 1;	

	public ArcadAntExecutor() {
		super();		
	}
	
	private class ProgressListener implements BuildListener {
		
		int targetLevel = 0;
		int taskLevel = 0;		
		
		private String formatEvent(BuildEvent arg0) {
			StringBuffer b = new StringBuffer();
			Target t = arg0.getTarget();
			if (t!=null) {
				String tname =t.getName(); 
				if (tname.equals("")) //$NON-NLS-1$
					tname = "_Initialization";//$NON-NLS-1$
				b.append(tname);				
			}			
			if (arg0.getTask()!=null) {
				b.append("::").append(arg0.getTask().getTaskName());//$NON-NLS-1$
			}
			if (arg0.getException()!=null) {
				b.append('\n').append(arg0.getException().getMessage()).append('\n');//$NON-NLS-1$ //$NON-NLS-2$
			}	
			b.append('\n').append(arg0.getMessage());//$NON-NLS-1$
			if (b.toString().equals(""))//$NON-NLS-1$
				return "";//$NON-NLS-1$
			return b.toString();
		}

		
		
		private void sendMessage(String message) {
			if (message!=null){ 
				ArcadAntExecutor.this.sendInfoMessage(message);
			}
		}
		
		public void buildStarted(BuildEvent arg0) {
		}

		public void buildFinished(BuildEvent arg0) {
		}

		public void targetStarted(BuildEvent arg0) {
			sendMessage(formatEvent(arg0));
		}
		public void targetFinished(BuildEvent arg0) {
			//sendMessage(formatEvent(arg0));
		}

		public void taskStarted(BuildEvent arg0) {
			sendMessage(formatEvent(arg0));
		}

		public void taskFinished(BuildEvent arg0) {
			//sendMessage(formatEvent(arg0));
		}

		public void messageLogged(BuildEvent arg0) {
			sendMessage(formatEvent(arg0));
//			if (arg0.getException()!=null)
//				sendMessage(Utils.replicate(' ',targetLevel*2+taskLevel*2) +arg0.getException().getMessage()+" Priority : "+arg0.getPriority());
		}
		
	}		

	
	private void setSystemProperties(Properties sysProperties){
	    // Loop through the property map
	    Set systemPropertyNames = sysProperties.keySet();
	    Iterator iter = systemPropertyNames.iterator();
	    while (iter.hasNext()) {
	        String propertyName =  (String) iter.next();
	        String propertyValue = (String) sysProperties.get(propertyName);
	        if (propertyValue == null) continue;
	        System.setProperty(propertyName,propertyValue);
	    }			
	}	
	
	
	public int execute(String buildFile,
			           String basedir,
			           String target,
			           Properties sysProperties,
			           Properties projectProperties,
			           Properties returnProperties,			           
			           String extendedAntLibraryPath) throws ArcadAntException {
			ArcadAntRunner runner = new ArcadAntRunner(); 
			try {
				//Mise à jour des propriétés system
				setSystemProperties(sysProperties);
				//Ajout d'un listener de progression
				runner.getProject().addBuildListener(new ProgressListener());	
				runner.setLibraryFolder(extendedAntLibraryPath);
				//Définition des propriétés				
				runner.setProperties(projectProperties,true);			
				//Initiallisation du projet				
				runner.init(buildFile,basedir);
				//Execution
				runner.runTarget(target);	
				//Récupération des propriétés du projet				
				runner.getReturnProperties(returnProperties);			
				
				return MSG_OK;
			} catch (ArcadAntException e) {
				sendErrorMessage(Utils.stackTrace(e));
			}
		return MSG_ERROR;
	}
	
	public void sendInfoMessage(String message) {
		MessageLogger.sendInfoMessage("ANT-EXECUTOR",message);
	}
	public void sendErrorMessage(String message) {
		MessageLogger.sendErrorMessage("ANT-EXECUTOR",message);
	}	
	
	
}
