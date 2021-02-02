package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedTask;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListExecuteTask extends AbstractXmlFileListCountedTask
		implements IListBrowseListener, TaskContainer {

	private boolean failonerror = false;
	private String filter = null;
	private boolean holdIfFailed = false;
	private boolean initStatusBeforeProcessing = false;

	int itemCount = 0;
	protected Vector<Task> nestedTasks = new Vector<>();
	int nokCount = 0;

	private final String nokCountProperty = null;

	int okCount = 0;
	private final String okCountProperty = null;
	private boolean searchingForLastProcessedElement = false;

	private boolean startOnLastProcessedElement = false;

	@Override
	public void addTask(final Task task) {
		nestedTasks.addElement(task);
	}

	@Override
	public void elementBrowsed(final StoreItem item) {
		itemCount++;
		final String status = item.getValues()[0];
		if (searchingForLastProcessedElement) {
			// Si l'élément a un status NOK et qu'il n'est pas suspendu
			if (status.equalsIgnoreCase(StoreItem.STATUS_NOK) &&
					!status.equals(StoreItem.STATUS_HOLD)) {
				searchingForLastProcessedElement = false;
			} else {
				return;
			}
		}
		if (!status.equals(StoreItem.STATUS_HOLD)) {
			// Affectation des valeurs du projet
			final ListMetaDatas md = item.getMetadatas();
			for (int i = 0; i < md.count(); i++) {
				final ListColumnDef cd = md.getColumnDefAt(i);
				getProject().setProperty(cd.getPropertyName(), item.getValue(i));
			}
			getProject().setProperty("arcad.item.count", String.valueOf(itemCount));
			try {
				executeTasks(item);
				okCount++;
				item.setValue(0, StoreItem.STATUS_OK);
				list.updateItems(item);
			} catch (final Exception e) {
				nokCount++;
				if (holdIfFailed) {
					item.setValue(0, StoreItem.STATUS_HOLD);
				} else {
					item.setValue(0, StoreItem.STATUS_NOK);
				}
				list.updateItems(item);
				if (failonerror) {
					list.setFollowBrowsing(false);
				}
				getProject().log(this, e.getMessage(), Project.MSG_ERR);
			}
		}
	}

	public void executeTasks(final StoreItem item) {
		for (final Object element : nestedTasks) {
			final Task nestedTask = (Task) element;
			nestedTask.perform();
		}
	}

	@Override
	public int processExecutionWithCount() {
		list.addBrowseListener(this);
		try {
			if (initStatusBeforeProcessing) {
				list.reinitializeValue("status", StoreItem.STATUS_NONE);
			}
			if (startOnLastProcessedElement) {
				searchingForLastProcessedElement = true;
			}
			list.load(false, true);
			final int result = list.browse(filter);
			if (okCountProperty != null && !okCountProperty.equals("")) {
				getProject().setNewProperty(okCountProperty, String.valueOf(okCount));
			}
			// Mise à jour des propriétés de retour
			if (nokCountProperty != null && !nokCountProperty.equals("")) {
				getProject().setNewProperty(nokCountProperty, String.valueOf(nokCount));
			}
			return result;
		} finally {
			list.removeBrowseListener(this);
		}
	}

	/**
	 * @param failonerror
	 *            the failonerror to set
	 */
	public void setFailonerror(final boolean failonerror) {
		this.failonerror = failonerror;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final String filter) {
		this.filter = filter;
	}

	/**
	 * @param holdIfFailed
	 *            the holdIfFailed to set
	 */
	public void setHoldIfFailed(final boolean holdIfFailed) {
		this.holdIfFailed = holdIfFailed;
	}

	/**
	 * @param initStatusBeforeProcessing
	 *            the initStatusBeforeProcessing to set
	 */
	public void setInitStatusBeforeProcessing(final boolean initStatusBeforeProcessing) {
		this.initStatusBeforeProcessing = initStatusBeforeProcessing;
	}

	/**
	 * @param startOnLastProcessedElement
	 *            the startOnLastProcessedElement to set
	 */
	public void setStartOnLastProcessedElement(final boolean startOnLastProcessedElement) {
		this.startOnLastProcessedElement = startOnLastProcessedElement;
	}

}
