package com.arcadsoftware.mmk.lists.impl.xml;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IContentAction;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.ListHeader;
import com.arcadsoftware.mmk.lists.managers.AbstractContentManager;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class XmlContentManager extends AbstractContentManager
		implements IContentAction {

	private final IXmlLists xmlList;

	public XmlContentManager(final IXmlLists xmllist) {
		super(xmllist.getList());
		xmlList = xmllist;
		if (xmllist.getCashManager() != null) {
			cashManager = xmllist.getCashManager();
		} else {
			cashManager = new XmlCashManager(xmllist.getList());
		}
	}

	@Override
	public int addItems(final AbstractFiller filler, final boolean checkIfExists, final boolean replaceIfExists) {
		filler.setList(list);
		filler.toogleToAddMode(checkIfExists, replaceIfExists);
		return filler.populate();
	}

	@Override
	public int addItems(final AbstractArcadList list, final boolean checkIfExists, final boolean replaceIfExists) {
		return ((XmlCashManager) cashManager).addItems(list, checkIfExists, replaceIfExists);
	}

	@Override
	public int addItems(final StoreItem item, final boolean checkIfExists, final boolean replaceIfExists)
			throws ArcadException {
		return ((XmlCashManager) cashManager).addItems(item, checkIfExists, replaceIfExists);
	}

	@Override
	public int addItems(final StoreItem[] items, final boolean checkIfExists, final boolean replaceIfExists)
			throws ArcadException {
		return ((XmlCashManager) cashManager).addItems(items, checkIfExists, replaceIfExists);
	}

	@Override
	public int browse() throws ArcadException {
		return browse(null);
	}

	@Override
	public int browse(final String subquery) throws ArcadException {
		return ((XmlCashManager) cashManager).browse(subquery);
	}

	@Override
	public int clearItems() throws ArcadException {
		return ((XmlCashManager) cashManager).clearItems();
	}

	@Override
	public void compare(final AbstractArcadList opList,
			final AbstractArcadList addedList, final boolean adCheckIfExists, final boolean adReplaceIfExists,
			final AbstractArcadList commonList, final boolean cmCheckIfExists, final boolean cmReplaceIfExists,
			final AbstractArcadList deletedList, final boolean deCheckIfExists, final boolean deReplaceIfExists)
			throws ArcadException {

		if (addedList != null) {
			// Calcul des éléments de "oplist" n'appartenant pas à "list"
			opList.substract(list, addedList, adCheckIfExists, adReplaceIfExists);
		}
		if (deletedList != null) {
			// Calcul des éléments de "list" n'appartenant pas à "oplist"
			list.substract(opList, deletedList, deCheckIfExists, deReplaceIfExists);
		}
		if (commonList != null) {
			// Calcul des éléments de "list" appartenant pas à "oplist"
			list.intersect(opList, commonList, cmCheckIfExists, cmReplaceIfExists);
		}
	}

	@Override
	public int count(final String query) throws ArcadException {
		return ((XmlCashManager) cashManager).count(query);
	}

	@Override
	public void duplicate(final AbstractArcadList toList) throws ArcadException {
		if (toList instanceof IXmlLists) {
			// Duplication des fichiers Xml sous-jacent;
			final IXmlLists xlist = (IXmlLists) toList;
			if (xlist.getXmlFileName() != null) {
				try {
					FileUtils.copyFile(new File(xmlList.getXmlFileName()),
							new File(xlist.getXmlFileName()));
					final Date now = new Date();
					toList.getHeader().setCreatedThe(now);
					toList.getHeader().setCreatedBy(System.getProperty("user.name"));
					toList.getHeader().setLastModifiedThe(now);
					toList.getHeader().setLastModifiedBy(System.getProperty("user.name"));
					updateHeader(toList);
				} catch (final IOException e) {
					logError(AbstractArcadList.MODULE_NAME, e);
				}
			}
		}
	}

	@Override
	public boolean exists(final StoreItem item) throws ArcadException {
		return ((XmlCashManager) cashManager).exists(item);
	}

	@Override
	public int extractItems(final String extractQuery) throws ArcadException {
		return ((XmlCashManager) cashManager).extractItems(extractQuery);
	}

	@Override
	public int extractItems(final String extractQuery,
			final AbstractArcadList targetList,
			final boolean clearListBeforeAdding,
			final boolean checkIfExists,
			final boolean replaceIfExists) throws ArcadException {

		// Purge de la liste de réception
		if (clearListBeforeAdding) {
			targetList.clearItems();
		}
		final AbstractArcadList lst = targetList;
		int count;
		final IListBrowseListener l = item -> {
			if (clearListBeforeAdding) {
				lst.addItems(item, false, false);
			} else {
				lst.addItems(item, checkIfExists, replaceIfExists);
			}
		};
		list.addBrowseListener(l);
		try {
			final XmlCashManager cm = ((IXmlLists) targetList).getCashManager();
			cm.setFlushImmediat(false);
			count = browse(extractQuery);
			cm.setFlushImmediat(true);
			cm.flushRequest();
		} finally {
			list.removeBrowseListener(l);
		}
		return count;
	}

	@Override
	public int intersect(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) throws ArcadException {
		return intersect(opList, resList, checkIfExists, replaceIfExists, null);
	}

	@Override
	public int intersect(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists, final Hashtable<String, String> extendedQuery)
			throws ArcadException {
		int count = 0;
		// Création d'une liste temporaire
		final IXmlLists tempList = XmlUtils.createTempList(list);
		count = ((XmlCashManager) cashManager).intersect(opList, tempList, extendedQuery);
		// Si la liste résultat est la liste courante, on change juste
		// le nom de fichier sous-jacent
		if (resList == list) {
			XmlUtils.changeXmlFile(xmlList, tempList.getXmlFileName());
		} else {
			// sinon, on transfert le contenu dans la liste résultat.
			count = resList.addItems(tempList.getList(), checkIfExists, replaceIfExists);
		}
		return count;
	}

	@Override
	public void load(final boolean retrieveProcessInfo, final boolean metadataOnly) throws ArcadException {
		((XmlCashManager) cashManager).load(retrieveProcessInfo, metadataOnly);
	}

	@Override
	public int merge(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) throws ArcadException {
		int count = 0;
		if (resList == list) {
			return addItems(opList, checkIfExists, replaceIfExists);
		} else if (resList == opList) {
			return opList.addItems(list, checkIfExists, replaceIfExists);
		} else {
			count = resList.addItems(list, checkIfExists, replaceIfExists);
			count = count + resList.addItems(opList, checkIfExists, replaceIfExists);
			return count;
		}
	}

	@Override
	public int reinitializeValue(final String id, final String value) throws ArcadException {
		return ((XmlCashManager) cashManager).reinitializeValue(id, value);
	}

	@Override
	public int removeDuplicate(final String orderQuery) throws ArcadException {
		return ((XmlCashManager) cashManager).removeDuplicate(orderQuery);
	}

	@Override
	public int removeItems(final AbstractArcadList fromList) throws ArcadException {
		return ((XmlCashManager) cashManager).removeItems(fromList);
	}

	@Override
	public int removeItems(final StoreItem item) throws ArcadException {
		return ((XmlCashManager) cashManager).removeItems(item);
	}

	@Override
	public int removeItems(final StoreItem[] items) throws ArcadException {
		return ((XmlCashManager) cashManager).removeItems(items);
	}

	@Override
	public int removeItems(final String removeQuery) throws ArcadException {
		return ((XmlCashManager) cashManager).removeItems(removeQuery);
	}

	@Override
	public int substract(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) throws ArcadException {
		int count = 0;
		if (resList == list) {
			// Si la liste résultat est la liste courante, alors on supprime
			// simplement les éléments appartenant à la liste opérande.
			// Supression des éléments de opList dans la liste courante
			count = removeItems(opList);
		} else if (resList == opList) {
			// Si la liste résultat est la liste opérande, on fusionne
			// alors la liste courante dans la liste opérande.
			count = opList.merge(list, opList, checkIfExists, replaceIfExists);
		} else {
			// duplication de la liste courante dans une liste temporaire
			final IXmlLists temp = XmlUtils.createTempList(list);
			// Duplication de la liste courante
			duplicate(temp.getList());
			// Supression des éléments de la liste opérande
			temp.getList().removeItems(opList);
			// Ajout des éléments dans la liste résultat.
			count = resList.addItems(temp.getList(), checkIfExists, replaceIfExists);
		}
		return count;
	}

	public void updateHeader(final AbstractArcadList list) throws ArcadException {
		if (list instanceof IXmlLists) {
			final IXmlLists l = (IXmlLists) list;
			final ListHeader h = list.getHeader().clone();
			// String oldDescription = list.getHeader().getDescription();
			// String oldComment = list.getHeader().getComment();
			if (!l.getCashManager().isActive()) {
				list.load(false, false);
			}
			list.getHeader().assign(h);
			l.getCashManager().flushRequest();
		}
	}

	@Override
	public int updateItems(final AbstractArcadList fromList) throws ArcadException {
		return ((XmlCashManager) cashManager).updateItems(fromList);
	}

	@Override
	public int updateItems(final StoreItem item) throws ArcadException {
		return ((XmlCashManager) cashManager).updateItems(item);
	}

	@Override
	public int updateItems(final StoreItem[] items) throws ArcadException {
		return ((XmlCashManager) cashManager).updateItems(items);
	}

}
