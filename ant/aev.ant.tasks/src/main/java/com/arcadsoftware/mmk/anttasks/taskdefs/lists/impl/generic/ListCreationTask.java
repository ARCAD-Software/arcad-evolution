package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListWithItem;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.Item;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.ItemValue;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListCreationTask extends AbstractXmlFileListWithItem {

	public class ItemDef {
		ListColumnDef cd;

		public ItemDef() {
			cd = new ListColumnDef();
		}

		public ListColumnDef getColumnDef() {
			return cd;
		}

		public void setDataType(final String datatype) {
			cd.setDatatypeFromText(datatype);
		}

		public void setId(final String name) {
			cd.setId(name);
		}

		public void setKey(final String key) {
			cd.setKey(key.equalsIgnoreCase("true"));
		}

		public void setPropertyName(final String propertyName) {
			cd.setPropertyName(propertyName);
		}
	}

	public class Metadata {
		Vector<ItemDef> defs = new Vector<>();

		public Metadata() {
			super();
		}

		public ItemDef createItemDef() {
			final ItemDef itemDef = new ItemDef();
			defs.add(itemDef);
			return itemDef;
		}

		/**
		 * Renvoit
		 * 
		 * @return the cols ListMetaDatas :
		 */
		public ListMetaDatas getCols() {
			final ListMetaDatas cols = new ListMetaDatas();
			for (int i = 0; i < defs.size(); i++) {
				cols.addColumnDef(defs.elementAt(i).getColumnDef());
			}
			return cols;
		}

	}

	private boolean checkDuplication = false;
	private String comment = null;

	private String description = null;

	Vector<Metadata> metadatas = new Vector<>();

	private boolean replaceFileIfExists = false;

	public Metadata createMetadata() {
		final Metadata metadata = new Metadata();
		metadatas.add(metadata);
		return metadata;
	}

	@Override
	public int processExecutionWithCount() {
		if (fromListFileName == null || fromListFileName.equals("")) {
			final Metadata m = metadatas.elementAt(0);
			list.setMetadatas(m.getCols());
		}
		// Traitement de l'ajout à partir d'une liste
		int count = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final GenericList fromlist = (GenericList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			fromlist.load(false, true);
			list.setMetadatas(fromlist.getMetadatas());
			list.initStoreItem();
			if (checkDuplication) {
				count = list.addItems(fromlist, checkIfExists, replaceIfExists);
			} else {
				fromlist.duplicate(list);
				count = list.count("");
			}
		}
		int count2 = 0;
		// Ajout des données
		for (final Item c : items) {
			final Vector<ItemValue> v = c.getValues();
			final StoreItem storeItem = new StoreItem();
			storeItem.setMetadatas(list.getMetadatas());
			boolean toadd = false;
			for (final ItemValue val : v) {
				if (list.getMetadatas() != null) {
					final ListColumnDef cd = list.getMetadatas().getColumnFromId(val.getId());
					if (cd != null) {
						storeItem.setValue(cd.getId(), val.getValue());
						toadd = true;
					}
				}
			}
			if (toadd) {
				final int i = list.addItems(storeItem, checkIfExists, replaceIfExists);
				count2 += i;
			}
		}
		updateHeaderInfo(description, comment);

		return count + count2;
	}

	/**
	 * @param checkDuplication
	 *            the checkDuplication to set
	 */
	public void setCheckDuplication(final boolean checkDuplication) {
		this.checkDuplication = checkDuplication;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param replaceFileIfExists
	 *            the replaceFileIfExists to set
	 */
	public void setReplaceFileIfExists(final boolean replaceFileIfExists) {
		this.replaceFileIfExists = replaceFileIfExists;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		// Le controle de saisie est réalisé dans le super
		final File f = new File(getFilename());
		if (f.exists()) {
			// Si l'utilisateur n'a pas demandé le remplacement
			// on envoit un exception
			if (!replaceFileIfExists) {
				throw new BuildException("File already exists!");
			} else {
				// Sinon on supprime le fichier
				try {
					Files.delete(f.toPath());
				}
				catch(IOException e) {
					throw new BuildException("File Replacement failed!", e);
				}
			}
		}

		// Si une liste est passé en paramètre, les métadatas ne seront
		// prises dans la définition de la liste.
		if (fromListFileName == null || fromListFileName.equals("")) {
			switch (metadatas.size()) {
			case 0:
				throw new BuildException("Metadata Information must be set!");
			case 1:
				for (final Metadata md : metadatas) {
					final ListMetaDatas cols = md.getCols();
					if (cols.count() == 0) {
						throw new BuildException("At least one ItemDef must be set!");
					} else {
						try {
							cols.valid();
						} catch (final ArcadException e) {
							throw new BuildException(e.getMessage());
						}
					}
				}
				break;
			default:
				throw new BuildException("Only one Metadata Tag could be used!");
			}
		} else {

		}

	}

}
