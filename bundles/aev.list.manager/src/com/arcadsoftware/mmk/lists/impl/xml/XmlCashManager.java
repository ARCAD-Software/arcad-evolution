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
import com.arcadsoftware.mmk.lists.AbstractList;
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
	
	public static final String ROOT_PATH = "/"+EListConstants.LST_TAG_LIST.getValue();	 
	public static final String HEADER_PATH = ROOT_PATH+"/"+EListConstants.LST_TAG_HEADER.getValue();
	public static final String META_PATH = ROOT_PATH+"/"
	                                       +EListConstants.LST_TAG_METADATAS.getValue()+"/"
    									   +EListConstants.LST_TAG_COLUMNDEF.getValue();
	public static final String DATA_PATH = ROOT_PATH+"/content/row";	
	
	private static final String IDROW = "internalid";
	

	IXmlLists xmllist;	
	

	private String cashId;	
	
	private String insertOrder = null;
	private String updateOrder = null;	
	private String deleteOrder = null;	
	private String orderByOrder = null;	
	private String tableCreationOrder = null;	
	private String indexCreationOrder = null;	
		
	

	

	
	
	public XmlCashManager(AbstractList list){	
		super(list);
		if (list instanceof IXmlLists) {
			this.xmllist = (IXmlLists)list;
		}

	}
	
	private void createSQLOrders(){
		
		createCreationOrder();		
		createInsertOrder();			
		createUpdateOrder();
		createDeleteOder();		
		createOrderByOrder();		
	}
	
	
	public void createCash() throws ArcadException {
		DBConnector.getInstance().execute(tableCreationOrder);
		if (indexCreationOrder!=null) {
			DBConnector.getInstance().execute(indexCreationOrder);
		}
		active = true;
	}

	private void putInCash(){
		putInCash(true);
	}	
	
	private void putInCash(boolean createTable){
		if (!this.isActive()) {
			if (createTable){
				XmlParseList xmlParser = new XmlParseList(xmllist);			
				xmlParser.setLogger(logger);
				xmlParser.parseInfoOnly();	
				createSQLOrders();
				try {							
					this.createCash();
				} catch (ArcadException e) {					
					logError(AbstractList.MODULE_NAME,e);
				}
			}
			// Il faut purger la table pour éviter les valeurs parasites.
			XmlParseList pl = new XmlParseList(xmllist) {
				protected void fireElementBrowsed(StoreItem item) {
					elementBrowsed(item);
				}
			};			
			pl.setLogger(logger);
			pl.parse();			
			//TODO [BLINDAGE] Etre sur que le nom du fichier est affecté
		}
	}		
	
	
	private class FlushFiller extends AbstractFiller{
		public FlushFiller() {
			super(list);
		}

		@Override
		public int fill() {
			StringBuffer log = new StringBuffer("+ Fill The list: ");
			log.append(xmllist.getXmlFileName());
			boolean execution = true;
			try{
				StringBuffer query = new StringBuffer();
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
						while(rs.next()) {		
							log.append("\n  -> Load the Current Item #").append(count);
							for (int i=0;i<list.getMetadatas().count();i++){
								String value = rs.getString(i+2);
								log.append("\n    : value #").append(i).append(" : "+value);
								list.getStoreItem().setValue(i,value);					
							}	
							log.append("\n    -> Get The current Item");
							StoreItem item = list.getStoreItem(); 
							log.append("\n    -> Save the Item into the list");
							saveItem(item);							
							count++;
						}
						log.append("\n  -> Close the result set");
						rs.close();
						log.append("\n  -> Return the processed items count");
						return count;
					} catch (SQLException e) {
						log.append("\n  : an error occurred during the SQL Execution " + Utils.stackTrace(e));
						ArcadException ae = new ArcadException(
									Translator.resString("error.cash.executionFailed", new String[]{"FlushFiller::fill"}), 
									e);
						logError(AbstractList.MODULE_NAME,ae);
						execution = false;
						return -1;
					}				
				} catch (ArcadException e1) {
					log.append("\n  : an error occurred during saving the Item " + Utils.stackTrace(e1));
					execution = false;
					logError(AbstractList.MODULE_NAME,e1);
					return -1;
				}
			} finally{
				if (!execution) {
					logError(AbstractList.MODULE_NAME,log.toString());
				} else {
					logVerbose(AbstractList.MODULE_NAME,"+ Fill The list: "+xmllist.getXmlFileName()+" : OK");
				}
			}
		}		
	}	
	
	private class EmptyFiller extends AbstractFiller{
		public EmptyFiller() {
			super(list);
		}

		@Override
		public int fill() {	
			return 0;	
		}		
	}	
	
	
	
	public int flush() {		
		if (this.isActive()) {
			xmllist.getList().setFiller(new FlushFiller());
			return xmllist.getList().populate();			
		} 
		else {
			xmllist.getList().setFiller(new EmptyFiller());
			return xmllist.getList().populate();
		}
	}		
	
	
	
	private void createInsertOrder(){
		StringBuffer insert = new StringBuffer();
		insert.append("insert into t").append(cashId).append(" (");
		
		for (int i=0;i<list.getMetadatas().count();i++){
			ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			insert.append(cd.getId());
			if (i<list.getMetadatas().count()-1) insert.append(',');				
		}			
		insert.append(") values (");
		for (int i=0;i<list.getMetadatas().count();i++){
			insert.append('?');
			if (i<list.getMetadatas().count()-1) insert.append(',');								
		}
		insert.append(')');	
		insertOrder = insert.toString();
	}
	
	private void createUpdateOrder(){
		StringBuffer update = new StringBuffer();
		update.append("update t").append(cashId).append(" set ");
		
		for (int i=0;i<list.getMetadatas().count();i++){
			ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (!cd.isKey()) {
				update.append(cd.getId()).append("=?,");
			}
		}					
		update.deleteCharAt(update.length()-1);
		update.append(makeWhereClause());
		updateOrder = update.toString();
	}	
	
	private void createOrderByOrder(){
		StringBuffer order = new StringBuffer();
		order.append(" order by ");		
		for (int i=0;i<list.getMetadatas().count();i++){
			ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (cd.isKey()) {
				order.append(cd.getId()).append(',');
			}
		}					
		order.deleteCharAt(order.length()-1);
		orderByOrder = order.toString();
	}	
	
	private void createDeleteOder(){
		StringBuffer delete = new StringBuffer();
		delete.append("delete from t").append(cashId).append(' ');
		delete.append(makeWhereClause());
		deleteOrder = delete.toString();
	}	
	
	
	private synchronized String getTableName(){
		/*SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Object o = new Object();
		try {
			synchronized(o) { o.wait(5);}
			
		} catch (InterruptedException e) {}	
		return fd.format(new Date());*/
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}	
	
	private void createCreationOrder(){
		//Creation de la table des données
		StringBuffer table = new StringBuffer();
		StringBuffer columns = new StringBuffer();		
		StringBuffer indexes = new StringBuffer();		
		StringBuffer createindex = new StringBuffer();		
		cashId = getTableName();
		table.append("create table t").append(cashId).append(" (");
		columns.append(IDROW).append(" INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY");
		for (int i=0;i<list.getMetadatas().count();i++){
			ListColumnDef cd = list.getMetadatas().getColumnDefAt(i);
			if (!columns.toString().equals(""))
				columns.append(',');
			switch (cd.getDatatype()) {
			case INTEGER: columns.append(cd.getId()).append(" integer");break;
			case STRING: columns.append(cd.getId()).append(" varchar(4000)");break;
			default: break;
			}
			if (cd.isKey()) {
				if (!indexes.toString().equals(""))
					indexes.append(',');
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
	

	
	
	public void elementBrowsed(StoreItem item) {
		if (active){
			try {
				DBConnector.getInstance()
				           .executePrepareStatement(insertOrder,item.getValues());
			} catch (ArcadException e) {
				logError(AbstractList.MODULE_NAME,e);
			}
		}
	}	
	
	private String makeWhereClause(){
		//TODO [DOC] Documentation la gestion du case sensititve
		boolean caseSensitive = ListSettings.getInstance().isCaseSensitive();
		StringBuffer result = new StringBuffer();		
		ListMetaDatas metadata = list.getMetadatas();
		for (int i=0;i<metadata.count();i++) {
			ListColumnDef cd = metadata.getColumnDefAt(i);
			if (cd.isKey()) {
				if (!result.toString().equals(""))
					result.append(" and ");
				if (caseSensitive)
					result.append(cd.getId()).append("= ?");
				else
					result.append("UCASE(").append(cd.getId()).append(") ").append("= UCASE(?)");
			}
		}
		if (!result.toString().equals(""))
			return new StringBuffer(" where ").append(result).toString();
		return "";		
	}
	

	

	
	private int add(StoreItem item) throws ArcadException {
		int count = DBConnector.getInstance()
						.executePrepareStatement(insertOrder,item.getValues());
		return count;
	}
	
	private int update(StoreItem item) throws ArcadException {
		int count = DBConnector.getInstance()
						.executePrepareStatement(updateOrder,item.getUpdateValues());
		return count;
	}	
	
	private int update(String id,String value) throws ArcadException {
		StringBuffer query = new StringBuffer();
		query.append("update t").append(cashId)
		     .append(" set ").append(id).append("=?");
		int count = DBConnector.getInstance()
						.executePrepareStatement(query.toString(),new String[]{value});
		return count;
	}		
	
	
	
	private int delete(StoreItem item) throws ArcadException {
		int count = DBConnector.getInstance()
        			.executePrepareStatement(deleteOrder,item.getKeyValues());
		return count;
	}		
	private int delete(String removeQuery) throws ArcadException {
		return delete(removeQuery,false);
	}		
	
	private int delete(String removeQuery,boolean keep) throws ArcadException {
		StringBuffer query = new StringBuffer();
		query.append("delete from t").append(cashId);
		if ((removeQuery!=null) && (!removeQuery.equals(""))){
			if (keep) { 
				query.append(" where ").append(IDROW)
					 .append(" not in (")
					 		.append("select ").append(IDROW)
					 		.append(" from t").append(cashId)
					 		.append(" where ").append(removeQuery)
					 .append(" )");
			} else
				query.append(" where ").append(removeQuery);
		}
		int count = DBConnector.getInstance()
        			.execute(query.toString());
		return count;
	}	
	
	private int selectCount(String selectQuery) throws ArcadException {
		StringBuffer query = new StringBuffer();
		query.append("select count(*) as nb from t").append(cashId);
		if ((selectQuery!=null) && (!selectQuery.equals(""))){
			query.append(" where ").append(selectQuery);
		}		
		ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			while(rs.next()) {
				return rs.getInt("nb");
			}
			rs.close();
		} catch (SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[]{"selectCount"}), 
					e);
		}			
		return -1;
	}	
	
	

	private int removeDuplicatItems(String orderQuery) throws ArcadException{
		
		IXmlLists tempList = XmlUtils.createTempList(this.list);
		tempList.getCashManager().setFlushImmediat(false);
		

		StringBuffer query = new StringBuffer();
		query.append("select * from t").append(cashId);
		query.append(orderByOrder);
		if ((orderQuery!=null) && (!orderQuery.equals(""))){
			query.append(',').append(orderQuery);
		}
		int count = 0;
		ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			String key = "";
			while(rs.next()) {
				for (int i=0;i<list.getMetadatas().count();i++){
					//décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					//la colonne _id
					list.getStoreItem().setValue(i,rs.getString(i+2));					
				}
				String currentKey = list.getStoreItem().getKey();
				if (!currentKey.equals(key)) {
					tempList.getList().addItems(list.getStoreItem(),false,false);
					key = currentKey;					
				} else {
					//On ne comptabilise que les éléments que l'on ne retient pas.
					count++;
				}
			}
			rs.close();
		} catch (SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[]{"removeDuplicatItems"}), 
					e);
		}			
		tempList.getCashManager().setFlushImmediat(true);
		tempList.getCashManager().flushRequest();
		XmlUtils.changeXmlFile(xmllist,tempList.getXmlFileName());
		active = false;
		putInCash(false);
		return count;		
	}
	
	public boolean find(StoreItem item) throws ArcadException {
		putInCash();
		StringBuffer query = new StringBuffer();
		query.append("select * from t").append(cashId);
		query.append(makeWhereClause());
		ResultSet rs = 
			DBConnector.getInstance().executePrepareQuery(query.toString(),item.getKeyValues());
		try {
			boolean result = rs.next();
			rs.close();
			return result;
		} catch (SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[]{"find"}), 
					e);
		}
	}	
	/* -----------------------------------------------------------------
	 *             Gestion du parcours du contenu des listes   
	 -------------------------------------------------------------------*/
	public int browse() throws ArcadException {		
		return browse(null);		
	}

	public int browse(String subquery) throws ArcadException {
		putInCash();
		int count = 0;
		StringBuffer query = new StringBuffer();
		query.append("select * from t").append(cashId);
		if ((subquery!=null) && (!subquery.equals(""))){
			query.append(" where ").append(subquery);
		}
		ResultSet rs = DBConnector.getInstance().executeQuery(query.toString());
		try {
			while(rs.next()) {
				for (int i=0;i<list.getMetadatas().count();i++){
					//décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					//la colonne _id
					list.getStoreItem().setValue(i,rs.getString(i+2));					
				}	
				count++;
				list.fireBrowseEvent();	
				if (!list.isFollowBrowsing())
					break;
			}	
			rs.close();
		} catch (SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[]{"browse"}), 
					e);
		}	
		return count;
	}
	
	public boolean exists(StoreItem item) throws ArcadException {
		return find(item);
	}
	/* -----------------------------------------------------------------
	 *             Gestion des ajouts des items de listes
	 -------------------------------------------------------------------*/
	public int addItems(StoreItem[] items, boolean checkIfExists, boolean replaceIfExists) 
	throws ArcadException {
		putInCash();
		int count = 0;
		for (int i=0;i<items.length;i++) {
			if (checkIfExists) {
				if (find(items[i])){
					if (replaceIfExists) 
						count = count + update(items[i]);
				} else 
					count = count + add(items[i]);
			} else {
				count = count + add(items[i]);
			}
		}		
		flushRequest();
		return count;
	}

	public int addItems(StoreItem item, boolean checkIfExists, boolean replaceIfExists) 
	throws ArcadException {
		return addItems(new StoreItem[]{item},checkIfExists,replaceIfExists);
	}
	
	public int addItems(AbstractList fromList, final boolean checkIfExists, final boolean replaceIfExists) {		 
		int count;		
		IListBrowseListener l = new IListBrowseListener(){
			public void elementBrowsed(StoreItem item) {
				try {
					addItems(item,checkIfExists,replaceIfExists);
				} catch (ArcadException e) {
					logError(AbstractList.MODULE_NAME,e);
				}
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
	
	/* -----------------------------------------------------------------
	 *             Gestion des suppressions des items de listes
	 -------------------------------------------------------------------*/
	public int removeItems(StoreItem[] items) throws ArcadException {
		putInCash();
		int count =0;
		for (int i=0;i<items.length;i++) {			
			count = count+ delete(items[i]);
		}		
		//TODO [LM] mise en place d'une stratégie de sauvegarde du cache;
		flushRequest();
		return count;
	}

	public int removeItems(StoreItem item) throws ArcadException {
		return removeItems(new StoreItem[]{item});
	}

	public int removeItems(String removeQuery) throws ArcadException {
		putInCash();
		int count = delete(removeQuery);
		flushRequest();	
		return count;
	}		
	public int clearItems() throws ArcadException {
		return removeItems("");
	}
	
	public int removeItems(AbstractList fromList) 
	throws ArcadException {
		putInCash();
		//Chargement des caches
		((IXmlLists)fromList).getCashManager().putInCash();
		String opCashId = ((IXmlLists)fromList).getCashManager().getCashId();		
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from t").append(cashId).append(" as t1 ")
		   .append("where exists (")
		   .append("select * from t").append(opCashId).append(" as t2 ")
		   .append("where ");		
		ListColumnDef[] keys = list.getMetadatas().getKeys(); 
		for (int i=0;i<keys.length;i++) {
			ListColumnDef cd = keys[i];
			String f = cd.getId();
			sql.append("t1.").append(f).append(" = ").append("t2.").append(f);
			if (i<keys.length-1)
				sql.append(" and ");
		}
		sql.append(')');		
		int count = DBConnector.getInstance().execute(sql.toString());
		flushRequest();
		return count;
	}

	/* -----------------------------------------------------------------
	 *             Gestion des modifications des items de listes
	 -------------------------------------------------------------------*/
	public int updateItems(StoreItem[] items) 
	throws ArcadException {
		putInCash();
		int count = 0;
		for (int i=0;i<items.length;i++) {
			count = count+update(items[i]);
		}		
		flushRequest();
		return count;
	}

	public int updateItems(StoreItem item) 
	throws ArcadException {
		return updateItems(new StoreItem[]{item});
	}

	public int updateItems(AbstractList fromList) {
		int count;		
		IListBrowseListener l = new IListBrowseListener(){
			public void elementBrowsed(StoreItem item) {
				try {
					updateItems(item);
				} catch (ArcadException e) {
					logError(AbstractList.MODULE_NAME,e);
				}
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
	/* -----------------------------------------------------------------
	 *             Gestion des extractions  des items de listes
	 -------------------------------------------------------------------*/
	
	public int extractItems(String extractQuery) 
	throws ArcadException {
		putInCash();
		int count=delete(extractQuery,true);
		flushRequest();
		return count;
	}

	/* -----------------------------------------------------------------
	 *             Gestion d'actions diverses sur une liste 
	 -------------------------------------------------------------------*/
	public int removeDuplicate(String orderQuery) 
	throws ArcadException {
		putInCash();
		int count = removeDuplicatItems(orderQuery);
		flushRequest();
		return count;				
	}	
	
	public int reinitializeValue(String id, String value) 
	throws ArcadException {
		load(false,false);
		//putInCash();
		int count = update(id,value);
		flushRequest();
		return count;				
	}	
	
	public int count(String query) 
	throws ArcadException {
		putInCash();		
		return selectCount(query);		
	}	
	
	
	public void load(boolean retrieveProcessInfo,boolean metadataOnly) 
	throws ArcadException {
		XmlParseList p = new XmlParseList(xmllist);
		p.setLogger(getLogger());
		p.parseInfoOnly();
		if (!metadataOnly || retrieveProcessInfo) {
			putInCash();
		}
		if (retrieveProcessInfo) {
			list.setElementCount(selectCount(""));
			list.setSucceedElementCount(selectCount("status='"+StoreItem.STATUS_OK+"'"));			
			list.setFailedElementCount(selectCount("status='"+StoreItem.STATUS_NOK+"'"));						
		}
	}		
	
	
	/* -----------------------------------------------------------------
	 *             Gestion des opérations ensemblistes 
	 -------------------------------------------------------------------*/
	public int intersect(AbstractList opList,IXmlLists tempList)
	throws ArcadException {
		return intersect(opList,tempList,null);
	}
	
	public int intersect(AbstractList opList,IXmlLists tempList,Hashtable<String,String> extendedQuery) 
	throws ArcadException {
		int count=0;
		((IXmlLists)opList).getCashManager().putInCash();
		putInCash();
		tempList.getCashManager().setFlushImmediat(false);
		
		String opCashId = ((IXmlLists)opList).getCashManager().getCashId();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t").append(cashId).append(" as t1 ")
		   .append("where exists (")
		   .append("select * from t").append(opCashId).append(" as t2 ")
		   .append("where ");
		
		ListColumnDef[] keys = list.getMetadatas().getKeys(); 
		for (int i=0;i<keys.length;i++) {
			ListColumnDef cd = keys[i];
			String f = cd.getId();
			sql.append("t1.").append(f).append(" = ").append("t2.").append(f);
			if (i<keys.length-1)
				sql.append(" and ");
		}
		if ((extendedQuery!=null) && !extendedQuery.isEmpty()) { 
			Iterator<String> iterator = extendedQuery.keySet().iterator();
			while(iterator.hasNext()) {
				String id = iterator.next();
				String op = extendedQuery.get(id);
				sql.append(" and (t1.").append(id).append(op).append("t2.").append(id).append(") ");
			}
			//sql.append(query);
		}
		sql.append(')');	
		
		ResultSet rs = DBConnector.getInstance().executeQuery(sql.toString());
		try {
			while(rs.next()) {
				for (int i=0;i<list.getMetadatas().count();i++){
					//décalage de 2 du rs.getString() car commence à 1
					// et on ne tient pas compte de la valeur de
					//la colonne _id
					list.getStoreItem().setValue(i,rs.getString(i+2));					
				}
				tempList.getList().addItems(list.getStoreItem(),false,false);
				count++;								
			}		
			rs.close();
			tempList.getCashManager().setFlushImmediat(true);
			tempList.getCashManager().flushRequest();
		} catch (SQLException e) {
			throw new ArcadException(
					Translator.resString("error.cash.executionFailed", new String[]{"intersect"}), 
					e);
		}	
		return count;		
	}


	
	
	
	/**
	 * Renvoit 
	 * @return the cashId String : 
	 */
	public String getCashId() {
		return cashId;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}



	
	









	
	
	
}
