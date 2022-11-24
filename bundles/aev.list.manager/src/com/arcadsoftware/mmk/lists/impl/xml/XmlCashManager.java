package com.arcadsoftware.mmk.lists.impl.xml;

//import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_COMMENT;

//import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_DESCRIPTION;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.ae.core.translation.Translator;
import com.arcadsoftware.ae.core.utils.Utils;
import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.EListConstants;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.ListSettings;
import com.arcadsoftware.mmk.lists.db.DBConnector;
import com.arcadsoftware.mmk.lists.managers.AbstractCashManager;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class XmlCashManager extends AbstractCashManager
		implements IListBrowseListener {

	private class EmptyFiller extends AbstractFiller {
		public EmptyFiller() {
			super(list);
		}

		@Override
		public int fill() {
			return 0;
		}
	}

	private class FlushFiller extends AbstractFiller {
		public FlushFiller() {
			super(list);
		}

		@Override
		public int fill() {
			final StringBuilder log = new StringBuilder("+ Fill The list: ");
			log.append(xmllist.getXmlFileName());
			boolean execution = true;
			try {
				final StringBuilder query = new StringBuilder();
				log.append("\n  SQL Query Creation");
				query.append("select * from t").append(cashId);
				log.append("\n  Query: ").append(query.toString());
				ResultSet rs;
				try {
					log.append("\n  -> Query execution");
					rs = DBConnector.getInstance().executeQuery(query.toString());
					try {
						int count = 0;
						log.append("\n  -> Browsing the result set");
						while (rs.next()) {
							log.append("\n  -> Load the Current Item #").append(count);
							for (int i = 0; i < list.getMetadatas().count(); i++) {
								final String value = rs.getString(i + 2);
								log.append("\n    : value #").append(i).append(" : " + value);
								list.getStoreItem().setValue(i, value);
							}
							log.append("\n    -> Get The current Item");
							final StoreItem item = list.getStoreItem();
							log.append("\n    -> Save the Item into the list");
							saveItem(item);
							count++;
						}
						log.append("\n  -> Close the result set");
						rs.close();
						log.append("\n  -> Return the processed items count");
						return count;
					} catch (final SQLException e) {
						log.append("\n  : an error occurred during the SQL Execution " + Utils.stackTrace(e));
						final ArcadException ae = new ArcadException(
								Translator.resString("error.cash.executionFailed",
										new String[] { "FlushFiller::fill" }),
								e);
						logError(AbstractArcadList.MODULE_NAME, ae);
						execution = false;
						return -1;
					}
				} catch (final ArcadException e1) {
					log.append("\n  : an error occurred during saving the Item " + Utils.stackTrace(e1));
					execution = false;
					logError(AbstractArcadList.MODULE_NAME, e1);
					return -1;
				}
			} finally {
				if (!execution) {
					logError(AbstractArcadList.MODULE_NAME, log.toString());
				} else {
					logVerbose(AbstractArcadList.MODULE_NAME, "+ Fill The list: " + xmllist.getXmlFileName() + " : OK");
				}
			}
		}
	}
	
	public static final String ROOT_PATH = "/" + EListConstants.LST_TAG_LIST.getValue();
	public static final String DATA_PATH = ROOT_PATH + "/content/row";
	public static final String HEADER_PATH = ROOT_PATH + "/" + EListConstants.LST_TAG_HEADER.getValue();

	private static final String IDROW = "internalid";

	public static final String META_PATH = ROOT_PATH + "/"
			+ EListConstants.LST_TAG_METADATAS.getValue() + "/"
			+ EListConstants.LST_TAG_COLUMNDEF.getValue();


	private String cashId;
	private String deleteOrder = null;
	private String indexCreationOrder = null;
	private String insertOrder = null;
	private String orderByOrder = null;
	private String tableCreationOrder = null;

	private String updateOrder = null;

	IXmlLists xmllist;

	public XmlCashManager(final AbstractArcadList list) {
		super(list);
		if (list instanceof IXmlLists) {
			xmllist = (IXmlLists) list;
		}

	}

	private int add(final StoreItem item) throws ArcadException {
		return DBConnector.getInstance()
				.executePrepareStatement(insertOrder, item.getValues());
	}

	public int addItems(final AbstractArcadList fromList, final boolean checkIfExists, final boolean replaceIfExists) {
		int count;
		final IListBrowseListener l = item -> {
			try {
				addItems(item, checkIfExists, replaceIfExists);
			} catch (final ArcadException e) {
				logError(AbstractArcadList.MODULE_NAME, e);
			}
		};
		fromList.addBrowseListener(l);
		try {
			setFlushImmediat(false);
			count = fromList.browse();
			setFlushImmediat(true);
			flushRequest();
		} finally {
			fromList.removeBrowseListener(l);
		}
		return count;
	}

	public int addItems(final StoreItem item, final boolean checkIfExists, final boolean replaceIfExists)
			throws ArcadException {
		return addItems(new StoreItem[] { item }, checkIfExists, replaceIfExists);
	}

	/*
	 * ----------------------------------------------------------------- Gestion des ajouts des items de listes
	 * -------------------------------------------------------------------
	 */
	public int addItems(final StoreItem[] items, final boolean checkIfExists, final boolean replaceIfExists)
			throws ArcadException {
		putInCash();
		int count = 0;
		for (final StoreItem item : items) {
			if (checkIfExists) {
				if (find(item)) {
					if (replaceIfExists) {
						count = count + update(item);
					}
				} else {
					count = count + add(item);
				}
			} else {
				count = count + add(item);
			}
		}
		flushRequest();
		return count;
	}

	/*
	 * ----------------------------------------------------------------- Gestion du parcours du contenu des listes
	 * -------------------------------------------------------------------
	 */
	public int browse() throws ArcadException {
		return browse(null);
	}

	public int browse(final String subquery) throws ArcadException {
		putInCash();
		int count = 0;
		final StringBuilder query = new StringBuilder();
		query.append("select * from t").append(cashId);
		if (subquery != null && !subquery.equals("")) {
			query.append(" where ").append(subquery);
		}
		final ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			while (rs.next()) {
				for (int i = 0; i < list.getMetadatas().count(); i++) {
					// décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					// la colonne _id
					list.getStoreItem().setValue(i, rs.getString(i + 2));
				}
				count++;
				list.fireBrowseEvent();
				if (!list.isFollowBrowsing()) {
					break;
				}
			}
			rs.close();
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[] { "browse" }),
					e);
		}
		return count;
	}

	public int clearItems() throws ArcadException {
		return removeItems("");
	}

	public int count(final String query)
			throws ArcadException {
		putInCash();
		return selectCount(query);
	}

	public void createCash() throws ArcadException {
		DBConnector.getInstance().execute(tableCreationOrder);
		if (indexCreationOrder != null) {
			DBConnector.getInstance().execute(indexCreationOrder);
		}
		active = true;
	}

	private void createCreationOrder() {
		// Creation de la table des données
		final StringBuilder table = new StringBuilder();
		final StringBuilder columns = new StringBuilder();
		final StringBuilder indexes = new StringBuilder();
		final StringBuilder createindex = new StringBuilder();
		cashId = getTableName();
		table.append("create table t").append(cashId).append(" (");
		columns.append(IDROW).append(" INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY");
		for (int i = 0; i < list.getMetadatas().count(); i++) {
			final ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (!columns.toString().equals("")) {
				columns.append(',');
			}
			switch (cd.getDatatype()) {
			case INTEGER:
				columns.append(cd.getId()).append(" integer");
				break;
			case STRING:
				columns.append(cd.getId()).append(" varchar(4000)");
				break;
			default:
				break;
			}
			if (cd.isKey()) {
				if (!indexes.toString().equals("")) {
					indexes.append(',');
				}
				indexes.append(cd.getId());
			}
		}
		if (!columns.toString().equals("")) {
			table.append(columns.toString()).append(')');
			tableCreationOrder = table.toString();
		}
		if (!indexes.toString().equals("")) {
			createindex.append("create index i").append(cashId)
					.append(" on t").append(cashId).append(" (")
					.append(indexes.toString()).append(')');
			indexCreationOrder = createindex.toString();
		}

	}

	private void createDeleteOder() {
		final StringBuilder delete = new StringBuilder();
		delete.append("delete from t").append(cashId).append(' ');
		delete.append(makeWhereClause());
		deleteOrder = delete.toString();
	}

	private void createInsertOrder() {
		final StringBuilder insert = new StringBuilder();
		insert.append("insert into t").append(cashId).append(" (");

		for (int i = 0; i < list.getMetadatas().count(); i++) {
			final ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			insert.append(cd.getId());
			if (i < list.getMetadatas().count() - 1) {
				insert.append(',');
			}
		}
		insert.append(") values (");
		for (int i = 0; i < list.getMetadatas().count(); i++) {
			insert.append('?');
			if (i < list.getMetadatas().count() - 1) {
				insert.append(',');
			}
		}
		insert.append(')');
		insertOrder = insert.toString();
	}

	private void createOrderByOrder() {
		final StringBuilder order = new StringBuilder();
		order.append(" order by ");
		for (int i = 0; i < list.getMetadatas().count(); i++) {
			final ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (cd.isKey()) {
				order.append(cd.getId()).append(',');
			}
		}
		order.deleteCharAt(order.length() - 1);
		orderByOrder = order.toString();
	}

	private void createSQLOrders() {

		createCreationOrder();
		createInsertOrder();
		createUpdateOrder();
		createDeleteOder();
		createOrderByOrder();
	}

	private void createUpdateOrder() {
		final StringBuilder update = new StringBuilder();
		update.append("update t").append(cashId).append(" set ");

		for (int i = 0; i < list.getMetadatas().count(); i++) {
			final ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (!cd.isKey()) {
				update.append(cd.getId()).append("=?,");
			}
		}
		update.deleteCharAt(update.length() - 1);
		update.append(makeWhereClause());
		updateOrder = update.toString();
	}

	private int delete(final StoreItem item) throws ArcadException {
		return DBConnector.getInstance()
				.executePrepareStatement(deleteOrder, item.getKeyValues());
	}

	private int delete(final String removeQuery) throws ArcadException {
		return delete(removeQuery, false);
	}

	private int delete(final String removeQuery, final boolean keep) throws ArcadException {
		final StringBuilder query = new StringBuilder();
		query.append("delete from t").append(cashId);
		if (removeQuery != null && !removeQuery.equals("")) {
			if (keep) {
				query.append(" where ").append(IDROW)
						.append(" not in (")
						.append("select ").append(IDROW)
						.append(" from t").append(cashId)
						.append(" where ").append(removeQuery)
						.append(" )");
			} else {
				query.append(" where ").append(removeQuery);
			}
		}
		return DBConnector.getInstance()
				.execute(query.toString());
	}

	@Override
	public void elementBrowsed(final StoreItem item) {
		if (active) {
			try {
				DBConnector.getInstance()
						.executePrepareStatement(insertOrder, item.getValues());
			} catch (final ArcadException e) {
				logError(AbstractArcadList.MODULE_NAME, e);
			}
		}
	}

	public boolean exists(final StoreItem item) throws ArcadException {
		return find(item);
	}

	public int extractItems(final String extractQuery)
			throws ArcadException {
		putInCash();
		final int count = delete(extractQuery, true);
		flushRequest();
		return count;
	}

	public boolean find(final StoreItem item) throws ArcadException {
		putInCash();
		final StringBuilder query = new StringBuilder();
		query.append("select * from t").append(cashId);
		query.append(makeWhereClause());
		final ResultSet rs = DBConnector.getInstance().executePrepareQuery(query.toString(), item.getKeyValues());
		try {
			final boolean result = rs.next();
			rs.close();
			return result;
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[] { "find" }),
					e);
		}
	}

	@Override
	public int flush() {
		if (isActive()) {
			xmllist.getList().setFiller(new FlushFiller());
			return xmllist.getList().populate();
		} else {
			xmllist.getList().setFiller(new EmptyFiller());
			return xmllist.getList().populate();
		}
	}

	/**
	 * Renvoit
	 * 
	 * @return the cashId String :
	 */
	public String getCashId() {
		return cashId;
	}

	private synchronized String getTableName() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/*
	 * ----------------------------------------------------------------- Gestion des opérations ensemblistes
	 * -------------------------------------------------------------------
	 */
	public int intersect(final AbstractArcadList opList, final IXmlLists tempList)
			throws ArcadException {
		return intersect(opList, tempList, null);
	}

	public int intersect(final AbstractArcadList opList, final IXmlLists tempList,
			final Hashtable<String, String> extendedQuery)
			throws ArcadException {
		int count = 0;
		((IXmlLists) opList).getCashManager().putInCash();
		putInCash();
		tempList.getCashManager().setFlushImmediat(false);

		final String opCashId = ((IXmlLists) opList).getCashManager().getCashId();
		final StringBuilder sql = new StringBuilder();
		sql.append("select * from t").append(cashId).append(" as t1 ")
				.append("where exists (")
				.append("select * from t").append(opCashId).append(" as t2 ")
				.append("where ");

		final ListColumnDef[] keys = list.getMetadatas().getKeys();
		for (int i = 0; i < keys.length; i++) {
			final ListColumnDef cd = keys[i];
			final String f = cd.getId();
			sql.append("t1.").append(f).append(" = ").append("t2.").append(f);
			if (i < keys.length - 1) {
				sql.append(" and ");
			}
		}
		if (extendedQuery != null && !extendedQuery.isEmpty()) {
			final Iterator<String> iterator = extendedQuery.keySet().iterator();
			while (iterator.hasNext()) {
				final String id = iterator.next();
				final String op = extendedQuery.get(id);
				sql.append(" and (t1.").append(id).append(op).append("t2.").append(id).append(") ");
			}
		}
		sql.append(')');

		final ResultSet rs = DBConnector.getInstance().executeQuery(sql.toString());
		try {
			while (rs.next()) {
				for (int i = 0; i < list.getMetadatas().count(); i++) {
					// décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					// la colonne _id
					list.getStoreItem().setValue(i, rs.getString(i + 2));
				}
				tempList.getList().addItems(list.getStoreItem(), false, false);
				count++;
			}
			rs.close();
			tempList.getCashManager().setFlushImmediat(true);
			tempList.getCashManager().flushRequest();
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[] { "intersect" }),
					e);
		}
		return count;
	}

	public void load(final boolean retrieveProcessInfo, final boolean metadataOnly)
			throws ArcadException {
		final XmlParseList p = new XmlParseList(xmllist);
		p.setLogger(getLogger());
		p.parseInfoOnly();
		if (!metadataOnly || retrieveProcessInfo) {
			putInCash();
		}
		if (retrieveProcessInfo) {
			list.setElementCount(selectCount(""));
			list.setSucceedElementCount(selectCount("status='" + StoreItem.STATUS_OK + "'"));
			list.setFailedElementCount(selectCount("status='" + StoreItem.STATUS_NOK + "'"));
		}
	}

	private String makeWhereClause() {
		final boolean caseSensitive = ListSettings.getInstance().isCaseSensitive();
		final StringBuilder result = new StringBuilder();
		final ListMetaDatas metadata = list.getMetadatas();
		for (int i = 0; i < metadata.count(); i++) {
			final ListColumnDef cd = metadata.getColumnDefAt(i);
			if (cd.isKey()) {
				if (!result.toString().equals("")) {
					result.append(" and ");
				}
				if (caseSensitive) {
					result.append("CAST(").append(cd.getId()).append(" as VARCHAR(4000))").append(" = ? ");

				} else {
					result.append("UCASE(CAST(").append(cd.getId()).append(" as VARCHAR(4000))").append(") = UCASE(?) ");
				}
			}
		}
		if (!result.toString().equals("")) {
			return new StringBuilder(" where ").append(result).toString();
		}
		return "";
	}

	private void putInCash() {
		putInCash(true);
	}

	private void putInCash(final boolean createTable) {
		if (!isActive()) {
			if (createTable) {
				final XmlParseList xmlParser = new XmlParseList(xmllist);
				xmlParser.setLogger(logger);
				xmlParser.parseInfoOnly();
				createSQLOrders();
				try {
					createCash();
				} catch (final ArcadException e) {
					logError(AbstractArcadList.MODULE_NAME, e);
				}
			}
			// Il faut purger la table pour éviter les valeurs parasites.
			final XmlParseList pl = new XmlParseList(xmllist) {
				@Override
				protected void fireElementBrowsed(final StoreItem item) {
					elementBrowsed(item);
				}
			};
			pl.setLogger(logger);
			pl.parse();
		}
	}

	public int reinitializeValue(final String id, final String value)
			throws ArcadException {
		load(false, false);
		final int count = update(id, value);
		flushRequest();
		return count;
	}

	/*
	 * ----------------------------------------------------------------- Gestion d'actions diverses sur une liste
	 * -------------------------------------------------------------------
	 */
	public int removeDuplicate(final String orderQuery)
			throws ArcadException {
		putInCash();
		final int count = removeDuplicatItems(orderQuery);
		flushRequest();
		return count;
	}

	private int removeDuplicatItems(final String orderQuery) throws ArcadException {

		final IXmlLists tempList = XmlUtils.createTempList(list);
		tempList.getCashManager().setFlushImmediat(false);

		final StringBuilder query = new StringBuilder();
		query.append("select * from t").append(cashId);
		query.append(orderByOrder);
		if (orderQuery != null && !orderQuery.equals("")) {
			query.append(',').append(orderQuery);
		}
		int count = 0;
		final ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			String key = "";
			while (rs.next()) {
				for (int i = 0; i < list.getMetadatas().count(); i++) {
					// décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					// la colonne _id
					list.getStoreItem().setValue(i, rs.getString(i + 2));
				}
				final String currentKey = list.getStoreItem().getKey();
				if (!currentKey.equals(key)) {
					tempList.getList().addItems(list.getStoreItem(), false, false);
					key = currentKey;
				} else {
					// On ne comptabilise que les éléments que l'on ne retient pas.
					count++;
				}
			}
			rs.close();
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[] { "removeDuplicatItems" }),
					e);
		}
		tempList.getCashManager().setFlushImmediat(true);
		tempList.getCashManager().flushRequest();
		XmlUtils.changeXmlFile(xmllist, tempList.getXmlFileName());
		active = false;
		putInCash(false);
		return count;
	}

	public int removeItems(final AbstractArcadList fromList)
			throws ArcadException {
		putInCash();
		// Chargement des caches
		((IXmlLists) fromList).getCashManager().putInCash();
		final String opCashId = ((IXmlLists) fromList).getCashManager().getCashId();

		final StringBuilder sql = new StringBuilder();
		sql.append("delete from t").append(cashId).append(" as t1 ")
				.append("where exists (")
				.append("select * from t").append(opCashId).append(" as t2 ")
				.append("where ");
		final ListColumnDef[] keys = list.getMetadatas().getKeys();
		for (int i = 0; i < keys.length; i++) {
			final ListColumnDef cd = keys[i];
			final String f = cd.getId();
			sql.append("t1.").append(f).append(" = ").append("t2.").append(f);
			if (i < keys.length - 1) {
				sql.append(" and ");
			}
		}
		sql.append(')');
		final int count = DBConnector.getInstance().execute(sql.toString());
		flushRequest();
		return count;
	}

	public int removeItems(final StoreItem item) throws ArcadException {
		return removeItems(new StoreItem[] { item });
	}

	/*
	 * ----------------------------------------------------------------- Gestion des suppressions des items de listes
	 * -------------------------------------------------------------------
	 */
	public int removeItems(final StoreItem[] items) throws ArcadException {
		putInCash();
		int count = 0;
		for (final StoreItem item : items) {
			count = count + delete(item);
		}
		flushRequest();
		return count;
	}

	public int removeItems(final String removeQuery) throws ArcadException {
		putInCash();
		final int count = delete(removeQuery);
		flushRequest();
		return count;
	}

	private int selectCount(final String selectQuery) throws ArcadException {
		final StringBuilder query = new StringBuilder();
		query.append("select count(*) as nb from t").append(cashId);
		if (selectQuery != null && !selectQuery.equals("")) {
			query.append(" where ").append(selectQuery);
		}
		final ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			while (rs.next()) {
				return rs.getInt("nb");
			}
			rs.close();
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[] { "selectCount" }),
					e);
		}
		return -1;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	private int update(final StoreItem item) throws ArcadException {
		return DBConnector.getInstance()
				.executePrepareStatement(updateOrder, item.getUpdateValues());
	}

	private int update(final String id, final String value) throws ArcadException {
		final StringBuilder query = new StringBuilder();
		query.append("update t").append(cashId)
				.append(" set ").append(id).append("=?");
		return DBConnector.getInstance()
				.executePrepareStatement(query.toString(), new String[] { value });
	}

	public int updateItems(final AbstractArcadList fromList) {
		int count;
		final IListBrowseListener l = item -> {
			try {
				updateItems(item);
			} catch (final ArcadException e) {
				logError(AbstractArcadList.MODULE_NAME, e);
			}
		};
		fromList.addBrowseListener(l);
		try {
			setFlushImmediat(false);
			count = fromList.browse();
			setFlushImmediat(true);
			flushRequest();
		} finally {
			fromList.removeBrowseListener(l);
		}
		return count;
	}
	/*
	 * ----------------------------------------------------------------- Gestion des extractions des items de listes
	 * -------------------------------------------------------------------
	 */

	public int updateItems(final StoreItem item)
			throws ArcadException {
		return updateItems(new StoreItem[] { item });
	}

	/*
	 * ----------------------------------------------------------------- Gestion des modifications des items de listes
	 * -------------------------------------------------------------------
	 */
	public int updateItems(final StoreItem[] items)
			throws ArcadException {
		putInCash();
		int count = 0;
		for (final StoreItem item : items) {
			count = count + update(item);
		}
		flushRequest();
		return count;
	}

}
