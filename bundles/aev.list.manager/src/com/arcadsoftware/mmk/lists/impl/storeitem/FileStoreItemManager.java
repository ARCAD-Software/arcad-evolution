package com.arcadsoftware.mmk.lists.impl.storeitem;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDataType;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class FileStoreItemManager extends AbstractStoreItemManager {

	private static final String ID = "com.arcadsoftware.lists.filelist";
	private static final String VERSION = "1.0.0.0";

	public FileStoreItemManager(final AbstractArcadList list) {
		super(list);
	}

	@Override
	public void createMetadata(final ListMetaDatas metadatas) {
		metadatas.setId(ID);
		metadatas.setVersion(VERSION);
		metadatas.addColumnDef("prefix", "arcad.list.prefix", ListColumnDataType.STRING, true);
		metadatas.addColumnDef("path", "arcad.list.filePath", ListColumnDataType.STRING, true);
		metadatas.addColumnDef("name", "arcad.list.fileName", ListColumnDataType.STRING, true);
		metadatas.addColumnDef("ext", "arcad.list.fileext", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("last", "arcad.list.lastmodifiedthe", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("size", "arcad.list.filesize", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("type", "arcad.list.filetype", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("canRead", "arcad.list.canRead", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("canWrite", "arcad.list.canWrite", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("isHidden", "arcad.list.isHidden", ListColumnDataType.STRING, false);
		metadatas.addColumnDef("absname", "arcad.list.absfileName", ListColumnDataType.STRING, true);
		metadatas.addColumnDef("target", "arcad.list.target", ListColumnDataType.STRING, false);
	}

	@Override
	public StoreItem toStoreItem(final Object object) {
		if (object instanceof File) {
			final StoreItem item = new StoreItem(list.getMetadatas());
			final File f = (File) object;

			final String fileName = f.getAbsolutePath();
			item.setUserValue(
					new String[] {
							FilenameUtils.getPrefix(fileName),
							FilenameUtils.getPath(fileName),
							FilenameUtils.getName(fileName),
							FilenameUtils.getExtension(fileName),
							String.valueOf(f.lastModified()),
							String.valueOf(f.length()),
							f.isDirectory() ? "dir" : "file",
							f.canRead() ? "1" : "0",
							f.canWrite() ? "1" : "0",
							f.isHidden() ? "1" : "0",
							f.getAbsolutePath(),
							""
					});
			return item;
		}
		return null;
	}

}
