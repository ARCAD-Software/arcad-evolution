package com.arcadsoftware.mmk.lists.impl.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParserException;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

import static com.arcadsoftware.mmk.lists.EListConstants.*;

public class XmlParseList extends AbstractLoggedObject{
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");	
	
	
	IXmlLists xmlList;
	public XmlParseList(IXmlLists xmlList) {
		this.xmlList = xmlList;
	}
	
	private Date getDate(String stringDate) {
		try {
			Date d = sd.parse(stringDate);
			return d;
		} catch (ParseException e) {
			return new Date();
		}
	}		
	
	
	private void parseHeaderTag(MXParser parser,String name) throws XmlPullParserException, IOException{
		if (name.equals(LST_TAG_HEADER.getValue())) {
			//Traitement des attributs
			int count = parser.getAttributeCount();
			for (int i=0;i<count;i++) {
				String atName = parser.getAttributeName(i);
				String atValue = parser.getAttributeValue(i);				
				if (atName.equals("createdThe")) {
					xmlList.getList().getHeader().setCreatedThe(getDate(atValue));
				}else if (atName.equals("createdBy")) {
					xmlList.getList().getHeader().setCreatedBy(atValue);
				}else if (atName.equals("lastModifiedThe")) {
					xmlList.getList().getHeader().setLastModifiedThe(getDate(atValue));
				}else if (atName.equals("lastModifiedBy")) {
					xmlList.getList().getHeader().setLastModifiedBy(atValue);
				}
			}
			int eventType = parser.nextTag();
			while(true) {		                    
	            String tag = parser.getName();
	            if(eventType == MXParser.START_TAG) {
	            	if (tag.equals("description")) {
	            		xmlList.getList().getHeader().setDescription(parser.nextText());
	            	} else if (tag.equals("comment")) {
	            		xmlList.getList().getHeader().setComment(parser.nextText());
	            	}
	            } else if(eventType == MXParser.END_TAG) {	            	
	            	if (tag.equals(LST_TAG_HEADER.getValue()))
	            		break;
	            }
	            eventType = parser.nextTag();
	         }	
		}
	}	
	
	private ListMetaDatas parseMetaTag(MXParser parser,String name) throws XmlPullParserException, IOException{
		if (name.equals(LST_TAG_METADATAS.getValue())) {
			//xmlList.getList().getMetadatas().clear();
			ListMetaDatas storedMetadata = new ListMetaDatas();
			storedMetadata.clear();
			int eventType = parser.nextTag();
			while(true) {		                    
	            String tag = parser.getName();
	            if(eventType == MXParser.START_TAG) {	  
	            	if (tag.equals(LST_TAG_COLUMNDEF.getValue())) {
	            		ListColumnDef cd = new ListColumnDef();
	        			int count = parser.getAttributeCount();
	        			for (int i=0;i<count;i++) {
	        				String atName = parser.getAttributeName(i);
	        				String atValue = parser.getAttributeValue(i);				
                            if (atName.equals("id")) {
	        					cd.setId(atValue);
	        				}else if (atName.equals("propertyName")) {
	        					cd.setPropertyName(atValue);
	        				}else if (atName.equals("datatype")) {
	        					cd.setDatatypeFromText(atValue);	        					
	        				}else if (atName.equals("key")) {
	        					cd.setKey(atValue.equalsIgnoreCase("true"));
	        				}
	        			}
	        			storedMetadata.addColumnDef(cd);
	            	}
	            } else if(eventType == MXParser.END_TAG) {
	            	if (tag.equals(LST_TAG_METADATAS.getValue())) {
	            		//TODO [LM] c'est le bon endroit pour valider les
	            		//métadatas stockées en terme d'Id et de version.
	            		if (xmlList.getList().getMetadatas().count() ==
	            			xmlList.getList().getMetadatas().getFixedMetadataCount()) {
	            			xmlList.getList().setMetadatas(storedMetadata);
	            		}
	            		break;
	            	}
	            }
	            eventType = parser.nextTag();
	         }	
			return storedMetadata;
		}
		return null;		
	}	
	
	private void parseColTag(MXParser parser,String name,int initialEventType) throws XmlPullParserException, IOException{
		int eventType= initialEventType;
		//if (name.equals("col")) {
			//int eventType = parser.nextTag();
			if (!xmlList.getList().isStoreItemInitialized())
				xmlList.getList().initStoreItem();
			while(true) {		                    
	            String tag = parser.getName();
	            if(eventType == MXParser.START_TAG) {
	    			//int count = parser.getAttributeCount();
	    			String atId = parser.getAttributeValue(0);	    				    			
    				String atValue = parser.getAttributeValue(1);
    				xmlList.getList().getStoreItem().setValue(atId,atValue);	 	    			
	            } else if(eventType == MXParser.END_TAG) {
	            	
	            	if (tag.equals("col"))
	            		break;
	            }
	            eventType = parser.nextTag();
	         }	
		//}
	}	
	
	protected void fireElementBrowsed(StoreItem item) {
		
	}
	
	
	private void parseRowTag(MXParser parser,String name) throws XmlPullParserException, IOException{
		if (name.equals(LST_TAG_ROW.getValue())) {
			int eventType = parser.nextTag();
			while(true) {		                    
	            String tag = parser.getName();
	            if(eventType == MXParser.START_TAG) {	            	
	            	parseColTag(parser,tag,eventType);
	            } else if(eventType == MXParser.END_TAG) {	            	
	            	if (tag.equals(LST_TAG_ROW.getValue())){
	            		fireElementBrowsed(xmlList.getList().getStoreItem());
	            		break;
	            	}
	            }
	            eventType = parser.nextTag();
	         }	
		}
	}	

	public void parse(){		
		String fileName = xmlList.getXmlFileName();
		//xmlList.getList().initStoreItem();
		File f = new File(fileName);
		if (f.exists()) {			
			MXParser parser = new MXParser();
			try {
				FileReader reader = new FileReader(f);	 
				parser.setInput(reader);
				int eventType = parser.getEventType();
				do {						
	                while(true) {
	                    String tag = parser.getName();
	                    if(eventType == MXParser.START_TAG) {		                    	
	                    	parseHeaderTag(parser,tag);
	                    	parseMetaTag(parser,tag);
	                    	parseRowTag(parser,tag);		                    			                    			                    	
	                    } else if(eventType == MXParser.END_TAG) {
	                    	break;
	                    }
	                    eventType = parser.nextTag();
	                 }	
	                eventType = parser.next();
				} while (eventType != MXParser.END_DOCUMENT);
				reader.close();
			} catch (FileNotFoundException e1) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e1);
			} catch (XmlPullParserException e1) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e1);
			} catch (IOException e) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e);
			}
		}
	}
	
	public void parseInfoOnly(){
		String fileName = xmlList.getXmlFileName();
		//xmlList.getList().initStoreItem();
		File f = new File(fileName);
		if (f.exists()) {
			MXParser parser = new MXParser();			
			try {
				FileReader reader = new FileReader(f);	 
				parser.setInput(reader);
				int eventType = parser.getEventType();
				boolean found = false;
				do {						
	                while(true) {		                    
	                    String tag = parser.getName();
	                    if(eventType == MXParser.START_TAG) {
	                    	parseHeaderTag(parser,tag);
	                    	ListMetaDatas md = parseMetaTag(parser,tag);
	                    	if (md!=null) {
	                    		xmlList.getList().setMetadatas(md);
	                    		xmlList.getList().initStoreItem();
	                    	}
	                    } else if(eventType == MXParser.END_TAG) {
	                		if (tag.equals(LST_TAG_METADATAS.getValue())) {
	                			found = true;
	                		}
	                    	break;
	                    }
	                    eventType = parser.nextTag();
	                 }	
	                eventType = parser.next();
				} while ((eventType != MXParser.END_DOCUMENT) && !found);
				reader.close();
			} catch (FileNotFoundException e1) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e1);
			} catch (XmlPullParserException e1) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e1);
			} catch (IOException e) {
				logError(AbstractList.MODULE_NAME+"::XmlParseList",e);
			}			
		}
	}	
	
	
	
}
