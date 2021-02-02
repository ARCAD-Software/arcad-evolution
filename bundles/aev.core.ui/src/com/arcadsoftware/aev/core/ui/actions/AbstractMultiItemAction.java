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
 * Classe des actions agissant sur plusieurs entités.<br>
 * <p>
 * <b><u>Les éléments à traités</u></b><br>
 * La détermination des éléments à traiter peut se fairte de plusieurs manières. <br>
 * SI la valeur de <code>collection</code> n'est pas null ALORS<br>
 * - ce seront ces éléments qui seront traités<br>
 * SINON SI la valeur de <code>entity</code> n'est pas null ALORS<br>
 * - seule cette valeur sera traitée<br>
 * SINON SI la valeur de <code>collectionProvider</code> n'est pas null ALORS<br>
 * - ce sont les élements de la collection fournie par <code>collectionProvider.getCollection()</code> qui seront
 * traités.
 * </p>
 * <p>
 * <b><u>Traitement de chaque élément</u></b><br>
 * Les éléments seront traités un par un grace à la méthode {@link #runOnEntity(ArcadEntity)
 * <code>runOnEntity(ArcadEntity)</code>}.
 * </p>
 * <p>
 * <b><u>Etapes de traitement</u></b><br>
 * Vous pouvez controler les étapes du traitement en surchargeant les méthodes :
 * <ul>
 * <li>{@link AbstractMultiItemAction#doBeforeProcessing(ArcadCollection) <code>
 * doBeforeProcessing(ArcadCollection items)</code>} : méthode exécutée AVANT le traitement globale</li>
 * <li>{@link AbstractMultiItemAction#doAfterProcessing(ArcadCollection) <code>
 * doAfterProcessing(ArcadCollection items)</code>} : méthode exécutée APRES le traitement globale</li>
 * <li>{@link AbstractMultiItemAction#doAfterProcessingOnItem(ArcadEntity)
 * <code>doAfterProcessingOnItem(ArcadEntity item)</code>} : méthode exécutée AVANT le traitement d'un élément</li>
 * <li>{@link AbstractMultiItemAction#doBeforeProcessingOnItem(ArcadEntity)
 * <code>doBeforeProcessingOnItem(ArcadEntity item)</code>} : méthode exécutée APRES le traitement d'un élément</li>
 * </ul>
 *
 * @author MD
 */
public abstract class AbstractMultiItemAction extends ArcadAction {

	public class RunWithProgress implements IRunnableWithProgress {
		private boolean result = true;

		public boolean getResult() {
			return result;
		}

		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			boolean ok = false;
			final ArcadCollection toProceed = getItemToProceed();
			if (toProceed == null) {
				return;
			}
			monitor.beginTask(getActionName(), toProceed.count());
			if (toProceed.count() > 0) {
				doBeforeProcessing(toProceed);
				multiEntity = toProceed.count() > 1;
				for (int i = 0; i < toProceed.count(); i++) {
					monitor.worked(1);
					final ArcadEntity ent = (ArcadEntity) toProceed.items(i);
					if (doBeforeProcessingOnItem(ent)) {
						ok = runOnEntity(ent);
					}
					if (ok) {
						doAfterProcessingOnItem(ent);
					}
					result = result && ok;
				}
				doAfterProcessing(toProceed);
			}
			monitor.done();
		}

	}

	protected ArcadCollection collection = null;
	protected IArcadCollectionProvider collectionProvider;
	protected ArcadActions containerActions = null;
	protected ArcadEntity entity = null;

	protected boolean multiEntity = false;

	public AbstractMultiItemAction() {
		super();
	}

	public AbstractMultiItemAction(final ArcadActions containerActions) {
		super();
		this.containerActions = containerActions;
	}

	public AbstractMultiItemAction(final ArcadCollection collection) {
		super();
		this.collection = collection;
	}

	public AbstractMultiItemAction(final ArcadEntity entity) {
		super();
		this.entity = entity;
	}

	public AbstractMultiItemAction(final IArcadCollectionProvider collectionProvider) {
		this.collectionProvider = collectionProvider;
	}

	/**
	 * Méthode permettant de tester si on peut exécuter l'action
	 *
	 * @param object
	 * @return boolean
	 */
	public boolean checkBeforeProcessing(final ArcadCollection items) {
		return true;
	}

	/**
	 * Méthode permettant de créer la collection désirée servant de conteneur aux éléments à traiter.<br>
	 * C'est dans cette méthode que vous allez créeer la collection spécialisée contenant les éléments à traiter.
	 *
	 * @return ArcadCollection : Objet de t'un type descendant d'ArcadCollection
	 */
	public abstract ArcadCollection createCollection();

	public void doAfterProcessing(final ArcadCollection items) {
		// Do nothing
	}

	public void doAfterProcessingOnItem(final ArcadEntity item) {
		// Do nothing
	}

	public void doBeforeProcessing(final ArcadCollection items) {
		// Do nothing
	}

	public boolean doBeforeProcessingOnItem(final ArcadEntity item) {
		return true;
	}

	@Override
	protected boolean execute() {
		final IRunnableWithProgress runContainer = getRunnableAction();
		try {
			new ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell()).run(false, false,
					runContainer);
			return ((RunWithProgress) runContainer).getResult();
		} catch (final InvocationTargetException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
		} catch (final InterruptedException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
			Thread.currentThread().interrupt();
		}
		return false;
	}

	/**
	 * Méthode permettant de modifier le contenu de la collection des éléments à traiter.<br>
	 * Surchargez cette méthode si vous dérirez modifer le contenu de la collection des éléments à traiter juste aprés
	 * que la classe n'est réalisée son remplissage automatique en fonction des paramétres passés. Généralement, cette
	 * méthode est utilisée pour ajouter dans la collection des éléments déduits des éléments principaux.
	 *
	 * @param toProceed
	 *            ArcadCollection : Collection des éléments à traiter telle quelle existe juste aprés le remplissage
	 *            réalisé pa la méthode {@link #getItemToProceed() <code>getItemToProceed()</code>}
	 */
	public void extendToProceed(final ArcadCollection toProceed) {
		// Do nothing
	}

	/**
	 * Description de l'action pour le ProgressMonitor
	 */
	public abstract String getActionName();

	/**
	 * @return Returns the collection.
	 */
	public ArcadCollection getCollection() {
		return collection;
	}

	/**
	 * @return Returns the collectionProvider.
	 */
	public IArcadCollectionProvider getCollectionProvider() {
		return collectionProvider;
	}

	/**
	 * @return Returns the containerActions.
	 */
	public ArcadActions getContainerActions() {
		return containerActions;
	}

	/**
	 * @return Returns the entity.
	 */
	public ArcadEntity getEntity() {
		return entity;
	}

	public ArcadCollection getItemToProceed() {
		ArcadCollection toProceed = null;
		// Préparation de la collection à traiter
		if (collection != null) {
			toProceed = collection;
		} else if (entity != null) {
			toProceed = createCollection();
			toProceed.copyAndAdd(entity);
		} else if (collectionProvider != null) {
			toProceed = collectionProvider.getCollection();
		}
		extendToProceed(toProceed);
		return toProceed;
	}

	public RunWithProgress getRunnableAction() {
		return new RunWithProgress();
	}

	/**
	 * @return Returns the multiEntity.
	 */
	public boolean isMultiEntity() {
		return multiEntity;
	}

	/**
	 * Méthode permettant de définir l'action à réaliser sur l'entité ARCAD passée en paramétre.<br>
	 * C'est grace à cette méthode que vous allez réellement définir l'exécution de l'action entité par entité.
	 *
	 * @param newEntity
	 *            ArcadEntity : Entité sur laquelle vous allez définir le traitement.
	 * @return boolean : renvoit vrai si 'laction s'est correctement déroulée.
	 */
	public abstract boolean runOnEntity(ArcadEntity newEntity);

	/**
	 * @param collection
	 *            The collection to set.
	 */
	public void setCollection(final ArcadCollection collection) {
		this.collection = collection;
	}

	/**
	 * @param collectionProvider
	 *            The collectionProvider to set.
	 */
	public void setCollectionProvider(final IArcadCollectionProvider collectionProvider) {
		this.collectionProvider = collectionProvider;
	}

	/**
	 * @param containerActions
	 *            The containerActions to set.
	 */
	public void setContainerActions(final ArcadActions containerActions) {
		this.containerActions = containerActions;
	}

	/**
	 * @param entity
	 *            The entity to set.
	 */
	public void setEntity(final ArcadEntity entity) {
		this.entity = entity;
	}

	/**
	 * @param multiEntity
	 *            The multiEntity to set.
	 */
	public void setMultiEntity(final boolean multiEntity) {
		this.multiEntity = multiEntity;
	}

}
