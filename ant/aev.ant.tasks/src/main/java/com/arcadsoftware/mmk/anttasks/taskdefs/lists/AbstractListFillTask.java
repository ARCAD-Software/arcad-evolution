package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;
import java.util.List;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public abstract class AbstractListFillTask extends AbstractXmlFileListFromResourcesTask {
	public class MapFiller extends AbstractFiller {
		private final List<String> files;

		public MapFiller(final AbstractArcadList list, final List<String> files) {
			super(list);
			this.files = files;
		}

		@Override
		public int fill() {
			int count = 0;
			for (final String toFile : files) {
				final int j = processFile(this, toFile);
				count += j;
			}
			return count;
		}
	}

	protected boolean checkIfExists = false;
	protected MapFiller filler;

	protected boolean replaceIfExists = false;

	public abstract int populate();

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListFromResourcesTask#process()
	 */
	@Override
	public int process() {
		// Remplissage de la liste
		filler = new MapFiller(list, files);
		list.setFiller(filler);
		return populate();

	}

	public int processFile(final AbstractFiller filler, final String file) {
		final StoreItem storeItem = filler.getList().toStoreItem(new File(file));
		return filler.saveItem(storeItem);
	}

	/**
	 * @param checkIfExists
	 *            the checkIfExists to set
	 */
	public void setCheckIfExists(final boolean checkIfExists) {
		this.checkIfExists = checkIfExists;
	}

	/**
	 * @param replaceIfExists
	 *            the replaceIfExists to set
	 */
	public void setReplaceIfExists(final boolean replaceIfExists) {
		this.replaceIfExists = replaceIfExists;
	}

}
