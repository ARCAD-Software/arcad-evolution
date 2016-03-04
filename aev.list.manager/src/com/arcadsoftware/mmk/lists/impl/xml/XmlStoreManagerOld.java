package com.arcadsoftware.mmk.lists.impl.xml;

//import static com.arcadsoftware.mmk.lists.EListConstants.*;


//import java.io.FileWriter;
//import java.io.IOException;
import java.text.SimpleDateFormat;
//import java.util.Date;

//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.XMLWriter;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;
//import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;

public class XmlStoreManagerOld extends AbstractStoreManager {
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
	
//	Document document;
//	Element rootNode;
//	Element headerNode;
//	Element metadataNode;	
//	Element contentNode;	
//	XMLWriter writer;
	
	private int rowId = 0;
	IXmlLists xmllist;
	
	public XmlStoreManagerOld(IXmlLists list) {
		super(list.getList());
		this.xmllist = list;
	}
	
//	private void createDocument(){
//		document = DocumentHelper.createDocument();
//		document.addDocType(LST_TAG_LIST.getValue(),null,null);			
//	}
//	
//	private void createHeaderSection() {
//		headerNode = rootNode.addElement(LST_TAG_HEADER.getValue());
//		headerNode.addAttribute("createdThe",sd.format(list.getHeader().getCreatedThe()));
//		headerNode.addAttribute("createdBy",list.getHeader().getCreatedBy());	
//		//Modification des informations
//		Date now = new Date();
//		list.getHeader().setLastModifiedThe(now);
//		list.getHeader().setLastModifiedBy(System.getProperty("user.name"));		
//		
//		headerNode.addAttribute("lastModifiedThe",sd.format(list.getHeader().getLastModifiedThe()));		
//		headerNode.addAttribute("lastModifiedBy",list.getHeader().getLastModifiedBy());		
//		
//		Element description = headerNode.addElement(LST_TAG_DESCRIPTION.getValue());
//		description.setText(list.getHeader().getDescription());
//		Element comment = headerNode.addElement(LST_TAG_COMMENT.getValue());
//		comment.setText(list.getHeader().getComment());
//	}
//	
//	private void createMetadataSection(){
//		metadataNode = rootNode.addElement(LST_TAG_METADATAS.getValue());
//		metadataNode.addAttribute("id",list.getMetadatas().getId());
//		metadataNode.addAttribute("version",list.getMetadatas().getVersion());		
//		int count = list.getMetadatas().count();
//		for (int i=0;i<count;i++) {
//			ListColumnDef colDef = list.getMetadatas().getColumnDefAt(i);
//			Element column = metadataNode.addElement(LST_TAG_COLUMNDEF.getValue());
//			column.addAttribute("position",String.valueOf(colDef.getPosition()));
//			column.addAttribute("id",colDef.getId());			
//			column.addAttribute("label",colDef.getLabel());
//			column.addAttribute("propertyName",colDef.getPropertyName());			
//			column.addAttribute("key",String.valueOf(colDef.isKey()));			
//		}
//	}
//	
//	private void createContentSection(){
//		contentNode = rootNode.addElement(LST_TAG_CONTENT.getValue());
//	}	
//	
//	private Element createItemSection(){
//		rowId++;
//		Element row = contentNode.addElement(LST_TAG_ROW.getValue());
//		int count = list.getMetadatas().count();
//		for (int i=0;i<count;i++) {
//			ListColumnDef coldef = list.getMetadatas().getColumnDefAt(i);
//			int index = coldef.getPosition();
//			Element col = row.addElement(LST_TAG_COL.getValue());
//			String value = list.getStoreItem().getValue(index);
//			col.addAttribute("id",coldef.getId());			
//			col.addAttribute("value",value);
//		}
//		return row;
//	}	
//	
//	public boolean openDocument() throws IOException {	
//		OutputFormat format = new OutputFormat();
//		//format.setEncoding("UTF-8");
//		format.setEncoding("ISO-8859-1");
//        writer = new XMLWriter(new FileWriter( xmllist.getXmlFileName()),format);
//        
//	    writer.write( document );	
//	    return true;		
//	}
	
	@Override	
	protected boolean initialization() throws ArcadException {
		return super.initialization();
//		createDocument();		
//		try {
//			openDocument();
//			//Cr‚ation du noeud root
//			rootNode = document.addElement(LST_TAG_LIST.getValue());				
//			writer.writeOpen(rootNode);
//			//Enregistrement du header de lists
//			createHeaderSection();
//			writer.write(headerNode);
//			//Enregistrement de la section metadatas
//			createMetadataSection();
//			writer.write(metadataNode);
//			createContentSection();
//			writer.writeOpen(contentNode);	
//			rowId = 0;
//			
//			return true;
//		} catch (IOException e) {
//			throw new ArcadException(e.getMessage(),e);			
//		}
	}
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.lists.AbstractStoreManager#saveFinalize()
	 */
	@Override
	protected boolean finalization() throws ArcadException {
		return super.finalization();		
//		try {
//			writer.writeClose(contentNode);			
//			writer.writeClose(rootNode);
//			writer.close();
//			return true;
//		} catch (IOException e) {
//			throw new ArcadException(e.getMessage(),e);	
//		}			
	}	

	
	@Override
	public boolean saveItem()  throws ArcadException {	
		return true;
//		try {
//			Element rowNode = createItemSection();
//			writer.write(rowNode);
//			return true;
//		} catch (IOException e) {
//			throw new ArcadException(e.getMessage(),e);	
//		}
	}







}

