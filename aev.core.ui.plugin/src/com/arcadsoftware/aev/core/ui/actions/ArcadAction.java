/*
 * Créé le 1 juin 04
 */
package com.arcadsoftware.aev.core.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.model.MonitorManager;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * Classe mere de toutes les actions ARCAD.<br>
 * Cette classe est une extension de la classe de Actions. Elle doit être
 * utilisée pour toute création d'action au sein des plugin ARCAD.
 * 
 * @author MD
 * 
 */
public class ArcadAction extends Action {

	protected boolean runOk = true;
	private boolean cancelled = true;
	protected MonitorManager monitorManager = new MonitorManager();

	/**
	 * Constructeur de la classe
	 */
	public ArcadAction() {
		super();
		setInterface();
	}

	/**
	 * @param text
	 * @param image
	 */
	public ArcadAction(String text, String tooltip, ImageDescriptor image) {
		super(text, image);
		this.setToolTipText(tooltip);
	}

	/**
	 * Méthode de définition de l'interface utilisateur de la classe.<br>
	 * Surchargez cette méthode pour définir les éléments d'interface de
	 * l'action (text, tooltip, image, etc.)
	 */
	protected void setInterface() {
		// Do nothing
	}

	protected void doInitialize() {
		// Do nothing
	}

	/**
	 * Méthode d'exécution de la classe.<br>
	 * Cette méthode prend en charge le cycle d'exécution de l'action.<br>
	 * <b><u>Cycle d'exécution</u></b><br>
	 * 1 - Appel à la méthode {@link #doBeforeRun() doBeforeRun()}<br>
	 * 2 - SI l'appel à la méthode {@link #canExecute() canExecute()} renvoit
	 * VRAI ALORS<br>
	 * 3 - &nbsp;&nbsp;&nbsp;&nbsp;Appel à la méthode {@link #execute()
	 * execute()} <br>
	 * 4 - &nbsp;&nbsp;&nbsp;&nbsp;Appel à la méthode {@link #doAfterRun()
	 * doAfterRun()} <br>
	 * 5 - FIN SI<br>
	 * Le code retour de l'exécution est affecté à la variable d'instance runOk
	 * accessible via la méthode {@link #isRunOk() isRunOk()}
	 */
	@Override
	public void run() {
		fireBegin(getBeginMessage(), getTotalStepNumber());
		doBeforeRun();
		fireInitialize(getInitializeMessage(), getInitializeStepNumber());
		doInitialize();
		if (canExecute()) {
			fireBeforeExecute(getBeforeExecuteMessage(), getBeforeExecuteStepNumber());
			runOk = execute();
			fireAfterExecute(getAfterExecuteMessage(), getAfterExecuteStepNumber());
			doAfterRun();
			fireEnd(getEndMessage());
		}
	}

	/**
	 * Méthode appelée en tout premier lieu lors de l'exécution.<br>
	 * Surchargez cette méthode si vous désirez effectuer un certain nombre
	 * d'opération avant même de commencer l'exécution.
	 */
	protected void doBeforeRun() {
		// Do nothing
	}

	/**
	 * Méthode appelée à la fin d'une l'exécution reussié ou non.<br>
	 * Surchargez cette méthode si vous désirez effectuer un certain nombre
	 * d'opérations à la fin de l'exécution. Si vous désirez connaitre si
	 * l'exécution s'est effectuée correctement utilisez la méthode
	 * {@link #isRunOk() isRunOk()}
	 */
	protected void doAfterRun() {
		// Do nothing
	}

	/**
	 * Cette méthode appelée avant l'exécution (mais après l'initialisation).<br>
	 * Si cette méthode renvoit <b>true</b>, l'exécution est lancée sinon elle
	 * est abandonnée.
	 * 
	 * @return boolean : <b>True</b> si l'exécution est possible, <b>false</b>
	 *         sinon.
	 */
	protected boolean canExecute() {
		return true;
	}

	/**
	 * Cette méthode contient le code réel d'exécution.<br>
	 * Surchargez cette méthode pour définir les actions à accomplir par votre
	 * action.
	 * 
	 * @return boolean : <b>True</b> si l'exécution s'est déroulée correctement,
	 *         <b>false</b> sinon.
	 */
	protected boolean execute() {

		return true;
	}

	/**
	 * Méthode retournant le code d'exécution.<br>
	 * 
	 * @return boolean : <b>True</b> si l'exécution s'est effectuée
	 *         correctement, <b>false</b> sinon.
	 */
	public boolean isRunOk() {
		return runOk;
	}

	/**
	 * @param b
	 */
	public void setRunOk(boolean b) {
		runOk = b;
	}

	/**
	 * @return boolean
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param b
	 */
	public void setCancelled(boolean b) {
		cancelled = b;
	}

	/**
	 * @return Libellé du démarrage de l'action dans les IMonitor associés
	 */
	public String getBeginMessage() {
		return CoreUILabels.resString("ArcadAction.BeginMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libellé de l'initialisation de l'action dans les IMonitor
	 *         associés
	 */
	public String getInitializeMessage() {
		return CoreUILabels.resString("ArcadAction.InitializeMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libellé de début d'exécution de l'action dans les IMonitor
	 *         associés
	 */
	public String getBeforeExecuteMessage() {
		return CoreUILabels.resString("ArcadAction.BeforeExecuteMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libellé de fin d'exécution de l'action dans les IMonitor associés
	 */
	public String getAfterExecuteMessage() {
		return CoreUILabels.resString("ArcadAction.AfterExecuteMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libellé de fin de l'action dans les IMonitor associés
	 */
	public String getEndMessage() {
		return CoreUILabels.resString("ArcadAction.EndMessage"); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre total d'étape de votre action. Ceci afin de notifier
	 * les IMonitor de l'avancement du traitement (ex. : faire progresser la
	 * barre d'avancement du ProgressMonitor)
	 */
	public int getTotalStepNumber() {
		return 5;
	}

	/**
	 * Retourne le nombre d'étape de l'initialisation
	 */
	public int getInitializeStepNumber() {
		return 1;
	}

	/**
	 * Retourne le nombre d'étape du début d'exécution
	 */
	public int getBeforeExecuteStepNumber() {
		return 1;
	}

	/**
	 * Retourne le nombre d'étape de fin d'exécution
	 */
	public int getAfterExecuteStepNumber() {
		return 1;
	}

	/**
	 * Notifie l'ensemble des IMonitor du début de l'action
	 * 
	 * @param message
	 *            : libellé de démarrage de l'action
	 * @param totalNumberOfStep
	 *            : nombre d'étape du doBeforeRun
	 */
	@SuppressWarnings("unchecked")
	private void fireBegin(String message, int totalNumberOfStep) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.begin(message, totalNumberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor du début de l'initialisation de l'action
	 * 
	 * @param message
	 *            : libellé d'initialisation de l'action
	 * @param numberOfStep
	 *            : nombre d'étape de l'initialisation
	 */
	@SuppressWarnings("unchecked")
	private void fireInitialize(String message, int numberOfStep) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.initialize(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor du début de l'exécution réelle de
	 * l'action
	 * 
	 * @param message
	 *            : libellé de démarrage de l'exécution de l'action
	 * @param numberOfStep
	 *            : nombre d'étape de l'action
	 */
	@SuppressWarnings("unchecked")
	private void fireBeforeExecute(String message, int numberOfStep) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.beforeExecute(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor d'un changement d'étape
	 * 
	 * @param message
	 *            : libellé de l'étape en cours
	 * @param numberOfStep
	 *            : nombre d'étape franchie
	 */
	@SuppressWarnings("unchecked")
	protected void fireProgress(String message, int numberOfStep) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.progress(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor de la fin de l'exécution réelle de
	 * l'action
	 * 
	 * @param message
	 *            : libellé de fin de l'exécution de l'action
	 * @param numberOfStep
	 *            : nombre d'étape de l'action
	 */
	@SuppressWarnings("unchecked")
	private void fireAfterExecute(String message, int numberOfStep) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.afterExecute(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor de la fin de l'action
	 */
	@SuppressWarnings("unchecked")
	private void fireEnd(String message) {
		Iterator it = monitorManager.iterator();
		while (it.hasNext()) {
			IMonitor monitor = (IMonitor) it.next();
			monitor.end(message);
		}
	}

	public MonitorManager getMonitorManager() {
		return monitorManager;
	}

}
