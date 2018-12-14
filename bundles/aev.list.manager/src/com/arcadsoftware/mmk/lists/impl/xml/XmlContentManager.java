package com.arcadsoftware.mmk.lists.impl.xml;







import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.IContentAction;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.ListHeader;
import com.arcadsoftware.mmk.lists.managers.AbstractContentManager;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;
public class XmlContentManager extends AbstractContentManager 
implements IContentAction{


	
	private IXmlLists xmlList;

	
	public XmlContentManager(IXmlLists xmllist){
		super(xmllist.getList());
		this.xmlList=xmllist;
		if (xmllist.getCashManager()!=null)
			cashManager = xmllist.getCashManager();
		else
			cashManager = new XmlCashManager(xmllist.getList());
	}
	
	public int browse() throws ArcadException{
		return browse(null);
	}

	public int browse(String subquery) throws ArcadException {
		return ((XmlCashManager)cashManager).browse(subquery);		
	}
	public boolean exists(StoreItem item) throws ArcadException {
		return ((XmlCashManager)cashManager).exists(item);
	}
	

	public int addItems(AbstractFiller filler,boolean checkIfExists, boolean replaceIfExists) {
		filler.setList(list);
		filler.toogleToAddMode(checkIfExists,replaceIfExists);
		return filler.populate();
	}	
	
	public int addItems(StoreItem[] items,boolean checkIfExists, boolean replaceIfExists) throws ArcadException {
		return ((XmlCashManager)cashManager).addItems(items,checkIfExists,replaceIfExists);
	}

	public int addItems(StoreItem item, boolean checkIfExists, boolean replaceIfExists) throws ArcadException {
		return ((XmlCashManager)cashManager).addItems(item,checkIfExists,replaceIfExists);
	}	
	public int addItems(AbstractList list, boolean checkIfExists, boolean replaceIfExists) {
		return ((XmlCashManager)cashManager).addItems(list,checkIfExists,replaceIfExists);
	}

	public int removeItems(StoreItem[] items) throws ArcadException {
		return ((XmlCashManager)cashManager).removeItems(items);
	}

	public int removeItems(StoreItem item) throws ArcadException {
		return ((XmlCashManager)cashManager).removeItems(item);
	}
	public int removeItems(AbstractList fromList) throws ArcadException {
		return ((XmlCashManager)cashManager).removeItems(fromList);
	}	
	
	public int removeItems(String removeQuery) throws ArcadException {
		return ((XmlCashManager)cashManager).removeItems(removeQuery);
	}

	public int clearItems() throws ArcadException {
		return ((XmlCashManager)cashManager).clearItems();
	}
	
	
	public int updateItems(StoreItem[] items) throws ArcadException {
		return ((XmlCashManager)cashManager).updateItems(items);
	}

	public int updateItems(StoreItem item)  throws ArcadException{
		return ((XmlCashManager)cashManager).updateItems(item);
	}

	public int updateItems(AbstractList fromList) throws ArcadException {
		return ((XmlCashManager)cashManager).updateItems(fromList);
	}	
	
	
	public int reinitializeValue(String id, String value) throws ArcadException {
		return ((XmlCashManager)cashManager).reinitializeValue(id,value);
	}
	
	
	public int extractItems(String extractQuery) throws ArcadException {
		return ((XmlCashManager)cashManager).extractItems(extractQuery);
	}

	public int extractItems(String extractQuery, 
			                    AbstractList targetList, 
			                    final boolean clearListBeforeAdding, 
			                    final boolean checkIfExists, 
			                    final boolean replaceIfExists)  throws ArcadException {
				
		//Purge de la liste de r‚ception
		if (clearListBeforeAdding) {
			targetList.clearItems();
		}
		final AbstractList lst = targetList; 
		int count;
		IListBrowseListener l = new IListBrowseListener(){
			public void elementBrowsed(StoreItem item) {
				if (clearListBeforeAdding) {
					lst.addItems(item,false,false);
				} else {
					lst.addItems(item,checkIfExists,replaceIfExists);
				}
			}			
		};			
		list.addBrowseListener(l);
		try {
			XmlCashManager cm =((IXmlLists)targetList).getCashManager(); 
			cm.setFlushImmediat(false);
			count = browse(extractQuery);
			cm.setFlushImmediat(true);
			cm.flushRequest();
		} finally {
			list.removeBrowseListener(l);
		}				
		return count;
	}

	public int removeDuplicate(String orderQuery)  throws ArcadException{
		return ((XmlCashManager)cashManager).removeDuplicate(orderQuery);
	}


	public int merge(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists) throws ArcadException {
		int count = 0; 
		if (resList==list) {
			return addItems(opList,checkIfExists,replaceIfExists);
		} else if (resList==opList) {
			return opList.addItems(list,checkIfExists,replaceIfExists);
		} else {			
			count = resList.addItems(list,checkIfExists,replaceIfExists);			
			count = count +resList.addItems(opList,checkIfExists,replaceIfExists);
			return count;
		}
	}
	
	public int substract(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists)  throws ArcadException{
		int count = 0;		
		if (resList==list) {
			//Si la liste r‚sultat est la liste courante, alors on supprime
			//simplement les ‚l‚ments appartenant … la liste op‚rande.
			//Supression des ‚l‚ments de opList dans la liste courante
			count = removeItems(opList);				
		} else if (resList==opList ){
			//Si la liste r‚sultat est la liste op‚rande, on fusionne
			//alors la liste courante dans la liste op‚rande.			
			count = opList.merge(list,opList,checkIfExists,replaceIfExists);
		}else {
			//duplication de la liste courante dans une liste temporaire
			IXmlLists temp = XmlUtils.createTempList(list);
			//Duplication de la liste courante
			duplicate(temp.getList());
			//Supression des ‚l‚ments de la liste op‚rande
			temp.getList().removeItems(opList);
			//Ajout des ‚l‚ments dans la liste r‚sultat.
			count = resList.addItems(temp.getList(),checkIfExists,replaceIfExists);
		}
		return count;
	}

	public int intersect(AbstractList opList,AbstractList resList,
	         boolean checkIfExists, boolean replaceIfExists)  throws ArcadException{
		return intersect(opList,resList,checkIfExists,replaceIfExists,null);
	}	
	
	public int intersect(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists,Hashtable<String,String> extendedQuery)  throws ArcadException{
		int count = 0;
		//Cr‚ation d'une liste temporaire
		IXmlLists tempList = XmlUtils.createTempList(list);				
		count = ((XmlCashManager)cashManager).intersect(opList,tempList,extendedQuery);	
		//Si la liste r‚sultat est la liste courante, on change juste
		//le nom de fichier sous-jacent
		if (resList==list) { 
			XmlUtils.changeXmlFile(xmlList,tempList.getXmlFileName());				
		} else {
			//sinon, on transfert le contenu dans la liste r‚sultat.
			count = resList.addItems(tempList.getList(),checkIfExists,replaceIfExists);
		}
		return count;
	}	
	

	public void duplicate(AbstractList toList)  throws ArcadException{
		if (toList instanceof IXmlLists) {
			//Duplication des fichiers Xml sous-jacent;
			IXmlLists xlist = (IXmlLists)toList;
			if (xlist.getXmlFileName()!=null) {
				try {
					FileUtils.copyFile(new File(xmlList.getXmlFileName()),
							           new File(xlist.getXmlFileName()));
					Date now = new Date();
					toList.getHeader().setCreatedThe(now);					
					toList.getHeader().setCreatedBy(System.getProperty("user.name"));
					toList.getHeader().setLastModifiedThe(now);					
					toList.getHeader().setLastModifiedBy(System.getProperty("user.name"));
					updateHeader(toList);					
				} catch (IOException e) {
					logError(AbstractList.MODULE_NAME,e);
				}	
			}			
		}		
	}
	
	public void updateHeader(AbstractList list)  throws ArcadException{
		if (list instanceof IXmlLists) {
			IXmlLists l = (IXmlLists)list;
			ListHeader h = list.getHeader().clone();
			String oldDescription = list.getHeader().getDescription(); 
			String oldComment = list.getHeader().getComment();			
	        if (!l.getCashManager().isActive())
	        	list.load(false,false);
	        list.getHeader().assign(h);
			l.getCashManager().flushRequest();
		}
	}
	

	public void load(boolean retrieveProcessInfo,boolean metadataOnly) throws ArcadException {
		((XmlCashManager)cashManager).load(retrieveProcessInfo,metadataOnly);
	}

	public int count(String query) throws ArcadException {
		return ((XmlCashManager)cashManager).count(query);		
	}


	public void compare(AbstractList opList,
	           AbstractList addedList,boolean adCheckIfExists, boolean adReplaceIfExists,
	           AbstractList commonList,boolean cmCheckIfExists, boolean cmReplaceIfExists,
	           AbstractList deletedList,boolean deCheckIfExists, boolean deReplaceIfExists)
			   throws ArcadException {
		
		if (addedList!=null) {
			//Calcul des ‚l‚ments de "oplist" n'appartenant pas … "list"
			opList.substract(list,addedList,adCheckIfExists,adReplaceIfExists);
		}
		if (deletedList!=null) {
			//Calcul des ‚l‚ments de "list" n'appartenant pas … "oplist"		
			list.substract(opList,deletedList,deCheckIfExists,deReplaceIfExists);
		}
		if (commonList!=null) {
			//Calcul des ‚l‚ments de "list" appartenant pas … "oplist"		
			list.intersect(opList,commonList,cmCheckIfExists,cmReplaceIfExists);
		}
	}









}
