/*
 * Created on Jul 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcadsoftware.aev.core.ui.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

/**
 * Classe des actions agissant sur plusieurs entit�s.<br>
 * <p>
 * <b><u>Les �l�ments � trait�s</u></b><br>
 * La d�termination des �l�ments � traiter peut se fairte de plusieurs mani�res.
 * <br>
 * SI la valeur de <code>collection</code> n'est pas null ALORS<br>
 * - ce seront ces �l�ments qui seront trait�s<br>
 * SINON SI la valeur de <code>entity</code> n'est pas null ALORS<br>
 * - seule cette valeur sera trait�e<br>
 * SINON SI la valeur de <code>collectionProvider</code> n'est pas null ALORS<br>
 * - ce sont les �lements de la collection fournie par
 * <code>collectionProvider.getCollection()</code> qui seront trait�s.
 * </p>
 * <p>
 * <b><u>Traitement de chaque �l�ment</u></b><br>
 * Les �l�ments seront trait�s un par un grace � la m�thode
 * {@link #runOnEntity(ArcadEntity) <code>runOnEntity(ArcadEntity)</code>}.
 * </p>
 * <p>
 * <b><u>Etapes de traitement</u></b><br>
 * Vous pouvez controler les �tapes du traitement en surchargeant les m�thodes :
 * <ul>
 * <li>{@link AbstractMultiItemAction#doBeforeProcessing(ArcadCollection) <code>
 * doBeforeProcessing(ArcadCollection items)</code>} : m�thode ex�cut�e AVANT le
 * traitement globale</li>
 * <li>{@link AbstractMultiItemAction#doAfterProcessing(ArcadCollection) <code>
 * doAfterProcessing(ArcadCollection items)</code>} : m�thode ex�cut�e APRES le
 * traitement globale</li>
 * <li>{@link AbstractMultiItemAction#doAfterProcessingOnItem(ArcadEntity)
 * <code>doAfterProcessingOnItem(ArcadEntity item)</code>} : m�thode ex�cut�e
 * AVANT le traitement d'un �l�ment</li>
 * <li>{@link AbstractMultiItemAction#doBeforeProcessingOnItem(ArcadEntity)
 * <code>doBeforeProcessingOnItem(ArcadEntity item)</code>} : m�thode ex�cut�e
 * APRES le traitement d'un �l�ment</li>
 * </ul>
 * 
 * @author MD
 * 
 */
public abstract class AbstractMultiItemAction extends ArcadAction {

	protected ArcadActions containerActions = null;

	protected IArcadCollectionProvider collectionProvider;
	protected ArcadCollection collection = null;
	protected ArcadEntity entity = null;
	protected boolean multiEntity = false;

	public AbstractMultiItemAction() {
		super();
	}

	public AbstractMultiItemAction(ArcadActions containerActions) {
		super();
		this.containerActions = containerActions;
	}

	public AbstractMultiItemAction(IArcadCollectionProvider collectionProvider) {
		this.collectionProvider = collectionProvider;
	}

	public AbstractMultiItemAction(ArcadEntity entity) {
		super();
		this.entity = entity;
	}

	public AbstractMultiItemAction(ArcadCollection collection) {
		super();
		this.collection = collection;
	}

	/**
	 * M�thode permettant de tester si on peut ex�cuter l'action
	 * 
	 * @param object
	 * @return boolean
	 */
	public boolean checkBeforeProcessing(ArcadCollection items) {
		return true;
	}

	public void doBeforeProcessing(ArcadCollection items) {
		// Do nothing
	}

	public void doAfterProcessing(ArcadCollection items) {
		// Do nothing
	}

	public void doAfterProcessingOnItem(ArcadEntity item) {
		// Do nothing
	}

	public boolean doBeforeProcessingOnItem(ArcadEntity item) {
		return true;
	}

	/**
	 * Description de l'action pour le ProgressMonitor
	 */
	public abstract String getActionName();

	public class RunWithProgress implements IRunnableWithProgress {
		private boolean result = true;

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			boolean ok = false;
			ArcadCollection toProceed = getItemToProceed();
			if (toProceed == null)
				return;
			monitor.beginTask(getActionName(), toProceed.count());
			if (toProceed.count() > 0) {
				doBeforeProcessing(toProceed);
				multiEntity = (toProceed.count() > 1);
				for (int i = 0; i < toProceed.count(); i++) {
					monitor.worked(1);
					ArcadEntity ent = (ArcadEntity) toProceed.items(i);
					if (doBeforeProcessingOnItem(ent))
						ok = runOnEntity(ent);
					if (ok)
						doAfterProcessingOnItem(ent);
					result = result && ok;
				}
				doAfterProcessing(toProceed);
			}
			monitor.done();
		}

		public boolean getResult() {
			return result;
		}

	}

	public RunWithProgress getRunnableAction() {
		return new RunWithProgress();
	}

	@Override
	protected boolean execute() {
		IRunnableWithProgress runContainer = getRunnableAction();
		try {
			new ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell()).run(false, false,
					runContainer);
			return ((RunWithProgress) runContainer).getResult();
		} catch (InvocationTargetException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
		} catch (InterruptedException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
		}
		return false;
	}

	/**
	 * @return Returns the collection.
	 */
	public ArcadCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            The collection to set.
	 */
	public void setCollection(ArcadCollection collection) {
		this.collection = collection;
	}

	/**
	 * @return Returns the collectionProvider.
	 */
	public IArcadCollectionProvider getCollectionProvider() {
		return collectionProvider;
	}

	/**
	 * @param collectionProvider
	 *            The collectionProvider to set.
	 */
	public void setCollectionProvider(IArcadCollectionProvider collectionProvider) {
		this.collectionProvider = collectionProvider;
	}

	/**
	 * @return Returns the containerActions.
	 */
	public ArcadActions getContainerActions() {
		return containerActions;
	}

	/**
	 * @param containerActions
	 *            The containerActions to set.
	 */
	public void setContainerActions(ArcadActions containerActions) {
		this.containerActions = containerActions;
	}

	/**
	 * @return Returns the entity.
	 */
	public ArcadEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            The entity to set.
	 */
	public void setEntity(ArcadEntity entity) {
		this.entity = entity;
	}

	/**
	 * @return Returns the multiEntity.
	 */
	public boolean isMultiEntity() {
		return multiEntity;
	}

	/**
	 * @param multiEntity
	 *            The multiEntity to set.
	 */
	public void setMultiEntity(boolean multiEntity) {
		this.multiEntity = multiEntity;
	}

	public ArcadCollection getItemToProceed() {
		ArcadCollection toProceed = null;
		// Pr�paration de la collection � traiter
		if (collection != null)
			toProceed = collection;
		else if (entity != null) {
			toProceed = createCollection();
			toProceed.copyAndAdd(entity);
		} else if (collectionProvider != null) {
			toProceed = collectionProvider.getCollection();
		}
		extendToProceed(toProceed);
		return toProceed;
	}

	/**
	 * M�thode permettant de modifier le contenu de la collection des �l�ments �
	 * traiter.<br>
	 * Surchargez cette m�thode si vous d�rirez modifer le contenu de la
	 * collection des �l�ments � traiter juste apr�s que la classe n'est
	 * r�alis�e son remplissage automatique en fonction des param�tres pass�s.
	 * G�n�ralement, cette m�thode est utilis�e pour ajouter dans la collection
	 * des �l�ments d�duits des �l�ments principaux.
	 * 
	 * @param toProceed
	 *            ArcadCollection : Collection des �l�ments � traiter telle
	 *            quelle existe juste apr�s le remplissage r�alis� pa la m�thode
	 *            {@link #getItemToProceed() <code>getItemToProceed()</code>}
	 */
	public void extendToProceed(ArcadCollection toProceed) {
		// Do nothing
	}

	/**
	 * M�thode permettant de cr�er la collection d�sir�e servant de conteneur
	 * aux �l�ments � traiter.<br>
	 * C'est dans cette m�thode que vous allez cr�eer la collection sp�cialis�e
	 * contenant les �l�ments � traiter.
	 * 
	 * @return ArcadCollection : Objet de t'un type descendant d'ArcadCollection
	 */
	public abstract ArcadCollection createCollection();

	/**
	 * M�thode permettant de d�finir l'action � r�aliser sur l'entit� ARCAD
	 * pass�e en param�tre.<br>
	 * C'est grace � cette m�thode que vous allez r�ellement d�finir l'ex�cution
	 * de l'action entit� par entit�.
	 * 
	 * @param newEntity
	 *            ArcadEntity : Entit� sur laquelle vous allez d�finir le
	 *            traitement.
	 * @return boolean : renvoit vrai si 'laction s'est correctement d�roul�e.
	 */
	public abstract boolean runOnEntity(ArcadEntity newEntity);

}
