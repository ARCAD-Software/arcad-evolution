package com.arcadsoftware.mmk.lists.impl.xml;

import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_COL;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_COLUMNDEF;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_COMMENT;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_CONTENT;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_DESCRIPTION;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_HEADER;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_LIST;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_METADATAS;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_ROW;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;

public class XmlStoreManager extends AbstractStoreManager {	

	private XmlSerializer serializer;
	private final IXmlLists xmlList;
	private final SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
	private FileWriter fileWriter;	
	
	public XmlStoreManager(final IXmlLists xmlList) {
		super(xmlList.getList());
		this.xmlList = xmlList;
	}

	private void createHeaderSection(final XmlSerializer serializer) throws IOException {
		serializer.startTag(null, LST_TAG_HEADER.getValue());
		serializer.attribute(null, "createdThe", sd.format(list.getHeader().getCreatedThe()));
		serializer.attribute(null, "createdBy", list.getHeader().getCreatedBy());
		// Modification des informations
		final Date now = new Date();
		list.getHeader().setLastModifiedThe(now);
		list.getHeader().setLastModifiedBy(System.getProperty("user.name"));
		serializer.attribute(null, "lastModifiedThe", sd.format(list.getHeader().getLastModifiedThe()));
		serializer.attribute(null, "lastModifiedBy", list.getHeader().getLastModifiedBy());

		serializer.startTag(null, LST_TAG_DESCRIPTION.getValue());
		serializer.text(list.getHeader().getDescription());
		serializer.endTag(null, LST_TAG_DESCRIPTION.getValue());

		serializer.startTag(null, LST_TAG_COMMENT.getValue());
		serializer.text(list.getHeader().getComment());
		serializer.endTag(null, LST_TAG_COMMENT.getValue());

		serializer.endTag(null, LST_TAG_HEADER.getValue());
	}

	private void createItemSection(final XmlSerializer serializer) throws IOException {
		serializer.startTag(null, LST_TAG_ROW.getValue());
		final int count = list.getMetadatas().count();
		for (int i = 0; i < count; i++) {
			final ListColumnDef coldef = list.getMetadatas().getColumnDefAt(i);
			final String id = coldef.getId();
			serializer.startTag(null, LST_TAG_COL.getValue());
			final String value = list.getStoreItem().getValue(id);
			serializer.attribute(null, "id", coldef.getId());
			serializer.attribute(null, "value", value);
			serializer.endTag(null, LST_TAG_COL.getValue());
		}
		serializer.endTag(null, LST_TAG_ROW.getValue());
	}

	private void createMetadataSection(final XmlSerializer serializer) throws IOException {
		serializer.startTag(null, LST_TAG_METADATAS.getValue());
		serializer.attribute(null, "id", list.getMetadatas().getId());
		serializer.attribute(null, "version", list.getMetadatas().getVersion());

		final int count = list.getMetadatas().count();
		for (int i = 0; i < count; i++) {
			final ListColumnDef colDef = list.getMetadatas().getColumnDefAt(i);
			serializer.startTag(null, LST_TAG_COLUMNDEF.getValue());
			serializer.attribute(null, "id", colDef.getId());
			serializer.attribute(null, "propertyName", colDef.getPropertyName());
			serializer.attribute(null, "datatype", colDef.getDatatype().getValue());
			serializer.attribute(null, "key", String.valueOf(colDef.isKey()));
			serializer.endTag(null, LST_TAG_COLUMNDEF.getValue());
		}
		serializer.endTag(null, LST_TAG_METADATAS.getValue());
	}

	private void createSerializer() throws ArcadException {		
		try {
			this.fileWriter = new FileWriter(xmlList.getXmlFileName());
			this.serializer = XmlPullParserFactory.newInstance().newSerializer();
			this.serializer.setOutput(fileWriter);
		} catch (final Exception e) {
			throw new ArcadException(e.getMessage(), e);
		}
	}

	@Override
	protected boolean finalization() throws ArcadException {
		try {
			serializer.endTag(null, LST_TAG_CONTENT.getValue());
			serializer.endTag(null, LST_TAG_LIST.getValue());
			serializer.endDocument();
			// Le fichier sous-jacent n'était par fermé !
			// Cela pouvait générait une erreur si tentative de remplacement
			// du fichier dnas une même session JVM
			serializer.flush();
			fileWriter.close();
			return true;
		} catch (final IOException e) {
			throw new ArcadException(e.getMessage(), e);
		}
	}

	@Override
	protected boolean initialization() throws ArcadException {
		createSerializer();
		try {
			// Creation de l'entête
			serializer.startDocument("ISO-8859-1", true);
			serializer.startTag(null, LST_TAG_LIST.getValue());
			createHeaderSection(serializer);
			createMetadataSection(serializer);
			// Création du tag de contenu
			serializer.startTag(null, LST_TAG_CONTENT.getValue());
			return true;
		} catch (final IOException e) {
			throw new ArcadException(e.getMessage(), e);
		}
	}

	@Override
	public boolean saveItem() throws ArcadException {
		try {
			createItemSection(serializer);
			return true;
		} catch (final IOException e) {
			throw new ArcadException(e.getMessage(), e);
		}
	}

}
