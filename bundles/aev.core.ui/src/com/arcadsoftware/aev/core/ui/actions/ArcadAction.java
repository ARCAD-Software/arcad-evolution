/*
 * Cree le 1 juin 04
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
 * Cette classe est une extension de la classe de Actions. Elle doit �tre utilis�e pour toute cr�ation d'action au
 * sein des plugin ARCAD.
 *
 * @author MD
 */
public class ArcadAction extends Action {

	private boolean cancelled = true;
	protected MonitorManager monitorManager = new MonitorManager();
	protected boolean runOk = true;

	/**
	 * Constructeur de la classe
	 */
	public ArcadAction() {
		super();
		setInterface();
	}

	/**
	 * Create an action with defined style
	 * 
	 * @param text
	 * @param style
	 */
	public ArcadAction(final String text, final int style) {
		super(text, style);
		setInterface();
	}

	/**
	 * @param text
	 * @param image
	 */
	public ArcadAction(final String text, final String tooltip, final ImageDescriptor image) {
		super(text, image);
		setToolTipText(tooltip);
	}

	/**
	 * Cette m�thode appel�e avant l'ex�cution (mais apr�s l'initialisation).<br>
	 * Si cette m�thode renvoit <b>true</b>, l'ex�cution est lanc�e sinon elle est abandonn�e.
	 *
	 * @return boolean : <b>True</b> si l'ex�cution est possible, <b>false</b> sinon.
	 */
	protected boolean canExecute() {
		return true;
	}

	/**
	 * M�thode appel�e � la fin d'une l'ex�cution reussi� ou non.<br>
	 * Surchargez cette m�thode si vous d�sirez effectuer un certain nombre d'op�rations � la fin de
	 * l'ex�cution. Si vous d�sirez connaitre si l'ex�cution s'est effectu�e correctement utilisez la m�thode
	 * {@link #isRunOk() isRunOk()}
	 */
	protected void doAfterRun() {
		// Do nothing
	}

	/**
	 * M�thode appel�e en tout premier lieu lors de l'ex�cution.<br>
	 * Surchargez cette m�thode si vous d�sirez effectuer un certain nombre d'op�ration avant m�me de commencer
	 * l'ex�cution.
	 */
	protected void doBeforeRun() {
		// Do nothing
	}

	protected void doInitialize() {
		// Do nothing
	}

	/**
	 * Cette m�thode contient le code r�el d'ex�cution.<br>
	 * Surchargez cette m�thode pour d�finir les actions � accomplir par votre action.
	 *
	 * @return boolean : <b>True</b> si l'ex�cution s'est d�roul�e correctement, <b>false</b> sinon.
	 */
	protected boolean execute() {

		return true;
	}

	/**
	 * Notifie l'ensemble des IMonitor de la fin de l'ex�cution r�elle de l'action
	 *
	 * @param message
	 *            : libell� de fin de l'ex�cution de l'action
	 * @param numberOfStep
	 *            : nombre d'�tape de l'action
	 */
	private void fireAfterExecute(final String message, final int numberOfStep) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.afterExecute(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor du d�but de l'ex�cution r�elle de l'action
	 *
	 * @param message
	 *            : libell� de d�marrage de l'ex�cution de l'action
	 * @param numberOfStep
	 *            : nombre d'�tape de l'action
	 */
	private void fireBeforeExecute(final String message, final int numberOfStep) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.beforeExecute(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor du d�but de l'action
	 *
	 * @param message
	 *            : libell� de d�marrage de l'action
	 * @param totalNumberOfStep
	 *            : nombre d'�tape du doBeforeRun
	 */
	private void fireBegin(final String message, final int totalNumberOfStep) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.begin(message, totalNumberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor de la fin de l'action
	 */
	private void fireEnd(final String message) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.end(message);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor du d�but de l'initialisation de l'action
	 *
	 * @param message
	 *            : libell� d'initialisation de l'action
	 * @param numberOfStep
	 *            : nombre d'�tape de l'initialisation
	 */
	private void fireInitialize(final String message, final int numberOfStep) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.initialize(message, numberOfStep);
		}
	}

	/**
	 * Notifie l'ensemble des IMonitor d'un changement d'�tape
	 *
	 * @param message
	 *            : libell� de l'�tape en cours
	 * @param numberOfStep
	 *            : nombre d'�tape franchie
	 */
	protected void fireProgress(final String message, final int numberOfStep) {
		final Iterator<?> it = monitorManager.iterator();
		while (it.hasNext()) {
			final IMonitor monitor = (IMonitor) it.next();
			monitor.progress(message, numberOfStep);
		}
	}

	/**
	 * @return Libell� de fin d'ex�cution de l'action dans les IMonitor associ�s
	 */
	public String getAfterExecuteMessage() {
		return CoreUILabels.resString("ArcadAction.AfterExecuteMessage"); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre d'�tape de fin d'ex�cution
	 */
	public int getAfterExecuteStepNumber() {
		return 1;
	}

	/**
	 * @return Libell� de d�but d'ex�cution de l'action dans les IMonitor associ�s
	 */
	public String getBeforeExecuteMessage() {
		return CoreUILabels.resString("ArcadAction.BeforeExecuteMessage"); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre d'�tape du d�but d'ex�cution
	 */
	public int getBeforeExecuteStepNumber() {
		return 1;
	}

	/**
	 * @return Libell� du d�marrage de l'action dans les IMonitor associ�s
	 */
	public String getBeginMessage() {
		return CoreUILabels.resString("ArcadAction.BeginMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libell� de fin de l'action dans les IMonitor associ�s
	 */
	public String getEndMessage() {
		return CoreUILabels.resString("ArcadAction.EndMessage"); //$NON-NLS-1$
	}

	/**
	 * @return Libell� de l'initialisation de l'action dans les IMonitor associ�s
	 */
	public String getInitializeMessage() {
		return CoreUILabels.resString("ArcadAction.InitializeMessage"); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre d'�tape de l'initialisation
	 */
	public int getInitializeStepNumber() {
		return 1;
	}

	public MonitorManager getMonitorManager() {
		return monitorManager;
	}

	/**
	 * Retourne le nombre total d'�tape de votre action. Ceci afin de notifier les IMonitor de l'avancement du
	 * traitement (ex. : faire progresser la barre d'avancement du ProgressMonitor)
	 */
	public int getTotalStepNumber() {
		return 5;
	}

	/**
	 * @return boolean
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isNotSeparator() {
		return true;
	}

	/**
	 * M�thode retournant le code d'ex�cution.<br>
	 *
	 * @return boolean : <b>True</b> si l'ex�cution s'est effectu�e correctement, <b>false</b> sinon.
	 */
	public boolean isRunOk() {
		return runOk;
	}

	/**
	 * M�thode d'ex�cution de la classe.<br>
	 * Cette m�thode prend en charge le cycle d'ex�cution de l'action.<br>
	 * <b><u>Cycle d'ex�cution</u></b><br>
	 * 1 - Appel � la m�thode {@link #doBeforeRun() doBeforeRun()}<br>
	 * 2 - SI l'appel � la m�thode {@link #canExecute() canExecute()} renvoit VRAI ALORS<br>
	 * 3 - &nbsp;&nbsp;&nbsp;&nbsp;Appel � la m�thode {@link #execute() execute()} <br>
	 * 4 - &nbsp;&nbsp;&nbsp;&nbsp;Appel � la m�thode {@link #doAfterRun() doAfterRun()} <br>
	 * 5 - FIN SI<br>
	 * Le code retour de l'ex�cution est affect� � la variable d'instance runOk accessible via la m�thode
	 * {@link #isRunOk() isRunOk()}
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
	 * @param b
	 */
	public void setCancelled(final boolean b) {
		cancelled = b;
	}

	/**
	 * M�thode de d�finition de l'interface utilisateur de la classe.<br>
	 * Surchargez cette m�thode pour d�finir les �l�ments d'interface de l'action (text, tooltip, image, etc.)
	 */
	protected void setInterface() {
		// Do nothing
	}

	/**
	 * @param b
	 */
	public void setRunOk(final boolean b) {
		runOk = b;
	}
}
