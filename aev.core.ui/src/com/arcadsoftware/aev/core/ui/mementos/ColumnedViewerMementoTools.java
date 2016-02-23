/*
 * Créé le 5 déc. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.File;
import java.io.IOException;

import org.eclipse.ui.IMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author jbeauquis
 */
public class ColumnedViewerMementoTools extends RootAndUserMementoTools {

	private String filterViewerId = StringTools.EMPTY;
	private static ColumnedViewerMementoTools instance;
	private String filename = null;

	private ColumnedViewerMementoTools() {
		super();
	}

	public static ColumnedViewerMementoTools getInstance() {
		if (instance == null) {
			instance = new ColumnedViewerMementoTools();
			instance.load();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#getFileName
	 * ()
	 */
	@Override
	protected String getFileName() {
		if (this.filename == null)
			filename = EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
					+ AbstractColumnedViewer.FILENAME;
		File directory = new File(EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
				+ AbstractColumnedViewer.DIRECTORYNAME);
		if (!directory.exists())
			directory.mkdir();
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"File : " + this.filename);//$NON-NLS-1$
			}
		}
		return this.filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#readKeys
	 * (java.lang.String, java.lang.String, org.eclipse.ui.IMemento)
	 */
	@Override
	public ArcadSettings readKeys(String serverName, String userName, IMemento user) {
		IMemento colViewer = user.getChild("columnedViewer");//$NON-NLS-1$
		String colViewerId = colViewer.getString("id");//$NON-NLS-1$
		IMemento[] cols = colViewer.getChildren("column");//$NON-NLS-1$
		ArcadColumns arcCols = new ArcadColumns();
		for (int i = 0; i < cols.length; i++) {
			arcCols.add(new ArcadColumn(cols[i].getString("id"), cols[i].getString("label"), //$NON-NLS-1$ //$NON-NLS-2$
					cols[i].getString("userLabel"), cols[i].getInteger("visible").intValue(), //$NON-NLS-1$ //$NON-NLS-2$
					cols[i].getInteger("position").intValue(), cols[i].getInteger("width").intValue()));//$NON-NLS-1$ //$NON-NLS-2$
		}
		return new ColumnedViewerSettings(serverName, userName, colViewerId, arcCols);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#saveKeys
	 * (org.eclipse.ui.IMemento,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public void saveKeys(IMemento user, ArcadSettings s) {
		if (s instanceof ColumnedViewerSettings) {
			ColumnedViewerSettings colViewerSettings = (ColumnedViewerSettings) s;
			IMemento colViewer = user.createChild("columnedViewer");//$NON-NLS-1$
			colViewer.putString("id", colViewerSettings.getViewerId());//$NON-NLS-1$
			for (int i = 0; i < colViewerSettings.getColumns().count(); i++) {
				IMemento aCol = colViewer.createChild("column");//$NON-NLS-1$
				aCol.putString("id", colViewerSettings.getColumn(i).getIdentifier());//$NON-NLS-1$
				aCol.putString("label", colViewerSettings.getColumn(i).getName());//$NON-NLS-1$
				aCol.putString("userLabel", colViewerSettings.getColumn(i).getUserName());//$NON-NLS-1$
				aCol.putInteger("visible", colViewerSettings.getColumn(i).getVisible());//$NON-NLS-1$
				aCol.putInteger("position", colViewerSettings.getColumn(i).getPosition());//$NON-NLS-1$
				aCol.putInteger("width", colViewerSettings.getColumn(i).getWidth());//$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keyEquals
	 * (com.arcadsoftware.aev.core.ui.mementos.ArcadSettings,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public boolean keyEquals(ArcadSettings ref, ArcadSettings toCompare) {
		if ((ref instanceof ColumnedViewerSettings) && (toCompare instanceof ColumnedViewerSettings)) {
			ColumnedViewerSettings r = (ColumnedViewerSettings) ref;
			ColumnedViewerSettings c = (ColumnedViewerSettings) toCompare;
			return r.getServerName().equalsIgnoreCase(c.getServerName())
					&& r.getUserName().equalsIgnoreCase(c.getUserName())
					&& r.getViewerId().equalsIgnoreCase(c.getViewerId());
		}
		return false;
	}

	public ColumnedViewerSettings getViewerSetting(String identifier) {
		ColumnedViewerSettings filter = new ColumnedViewerSettings("*ALL", "*ALL", identifier); //$NON-NLS-1$//$NON-NLS-2$
		return (ColumnedViewerSettings) getCurrentSettings(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keep(com
	 * .arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	protected boolean keep(ArcadSettings s) {
		// return
		// ((ColumnedViewerSettings)s).getViewerId().equals(filterViewerId);
		return true;
	}

	/**
	 * @return Renvoie filterViewerId.
	 */
	public String getFilterViewerId() {
		return filterViewerId;
	}

	/**
	 * @param filterViewerId
	 *            filterViewerId à définir.
	 */
	public void setFilterViewerId(String filterViewerId) {
		this.filterViewerId = filterViewerId;
	}
}
