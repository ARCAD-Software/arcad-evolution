package com.arcadsoftware.mmk.lists.impl.xml;

import static com.arcadsoftware.mmk.lists.EListConstants.*;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.xmlpull.mxp1_serializer.MXSerializer;
import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;

public class XmlStoreManager extends AbstractStoreManager {
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
	
	IXmlLists xmlList;
	MXSerializer serializer = null;
	
	public XmlStoreManager(IXmlLists xmlList) {
		super(xmlList.getList());
		this.xmlList = xmlList;
	}
	
	private void createHeaderSection(MXSerializer serializer) throws IOException {		
		serializer.startTag(null,LST_TAG_HEADER.getValue());
		serializer.attribute(null,"createdThe",sd.format(list.getHeader().getCreatedThe()));
		serializer.attribute(null,"createdBy",list.getHeader().getCreatedBy());
		//Modification des informations
		Date now = new Date();
		list.getHeader().setLastModifiedThe(now);
		list.getHeader().setLastModifiedBy(System.getProperty("user.name"));				
		serializer.attribute(null,"lastModifiedThe",sd.format(list.getHeader().getLastModifiedThe()));		
		serializer.attribute(null,"lastModifiedBy",list.getHeader().getLastModifiedBy());	
		
		
		serializer.startTag(null,LST_TAG_DESCRIPTION.getValue());
		serializer.text(list.getHeader().getDescription());
		serializer.endTag(null,LST_TAG_DESCRIPTION.getValue());
		
		serializer.startTag(null,LST_TAG_COMMENT.getValue());
		serializer.text(list.getHeader().getComment());
		serializer.endTag(null,LST_TAG_COMMENT.getValue());	
		
		serializer.endTag(null,LST_TAG_HEADER.getValue());
	}	
	
	private void createMetadataSection(MXSerializer serializer) throws IOException {			
		serializer.startTag(null,LST_TAG_METADATAS.getValue());	
		serializer.attribute(null,"id",list.getMetadatas().getId());
		serializer.attribute(null,"version",list.getMetadatas().getVersion());		
	
		int count = list.getMetadatas().count();
		for (int i=0;i<count;i++) {
			ListColumnDef colDef = list.getMetadatas().getColumnDefAt(i);
			//Element column = metadataNode.addElement(LST_TAG_COLUMNDEF.getValue());
			serializer.startTag(null,LST_TAG_COLUMNDEF.getValue());
			serializer.attribute(null,"id",colDef.getId());			
			serializer.attribute(null,"propertyName",colDef.getPropertyName());			
			serializer.attribute(null,"datatype",colDef.getDatatype().getValue());			
			serializer.attribute(null,"key",String.valueOf(colDef.isKey()));			
			serializer.endTag(null,LST_TAG_COLUMNDEF.getValue());
		}
		serializer.endTag(null,LST_TAG_METADATAS.getValue());
	}	
	
	private void createItemSection(MXSerializer serializer) throws IOException {	
		serializer.startTag(null,LST_TAG_ROW.getValue());
		int count = list.getMetadatas().count();
		for (int i=0;i<count;i++) {
			ListColumnDef coldef = list.getMetadatas().getColumnDefAt(i);
			String id = coldef.getId();
			serializer.startTag(null,LST_TAG_COL.getValue());
			String value = list.getStoreItem().getValue(id);
			serializer.attribute(null,"id",coldef.getId());			
			serializer.attribute(null,"value",value);
			serializer.endTag(null,LST_TAG_COL.getValue());
		}
		serializer.endTag(null,LST_TAG_ROW.getValue());
	}	
	
	
	
	private boolean createSerializer() throws ArcadException{
		String fileName = xmlList.getXmlFileName();
		//xmlList.getList().initStoreItem();
		File f = new File(fileName);

		serializer = new MXSerializer();			
		FileWriter writer;
		try {
			writer = new FileWriter(f);
	        serializer.setOutput(writer);	
	        return true;
		} catch (IOException e) {
			throw new ArcadException(e.getMessage(),e);
		}
	}

	protected boolean initialization()throws ArcadException{
		if (createSerializer()){
	        try {
	        	//Creation de l'entête
				serializer.startDocument("ISO-8859-1", true);
				serializer.startTag(null,LST_TAG_LIST.getValue());
				createHeaderSection(serializer);
				createMetadataSection(serializer);
				//Création du tag de contenu
				serializer.startTag(null,LST_TAG_CONTENT.getValue());				
				return true;
			} catch (IOException e) {
				throw new ArcadException(e.getMessage(),e);
			}			
		}
		return false;
	};
	protected boolean finalization() throws ArcadException{
        try {
			serializer.endTag(null,LST_TAG_CONTENT.getValue());
			serializer.endTag(null,LST_TAG_LIST.getValue());
			serializer.endDocument();
			//Le fichier sous-jacent n'était par fermé !
			//Cela pouvait générait une erreur si tentative de remplacement
			//du fichier dnas une même session JVM
			//System.out.println("close-1");
			serializer.getWriter().flush();
			serializer.getWriter().close();
			//System.out.println("close-2");
			return true;
		} catch (IOException e) {
			throw new ArcadException(e.getMessage(),e);
		}			
	};		
	
	@Override
	public boolean saveItem() throws ArcadException{
		try {
			createItemSection(serializer);
			return true;
		} catch (IOException e) {
			throw new ArcadException(e.getMessage(),e);
		}
	}
	
}
