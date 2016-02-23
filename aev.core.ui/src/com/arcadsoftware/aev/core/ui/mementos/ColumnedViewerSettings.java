/*
 * Créé le 4 déc. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author jbeauquis
 */
public class ColumnedViewerSettings extends ArcadSettings {
	
	private String viewerId;
	private ArcadColumns columns;
	
	/**
	 * @param serverName
	 * @param userName
	 */
	public ColumnedViewerSettings(String serverName, String userName) {
		super(serverName, userName);
	}
	
	public ColumnedViewerSettings(String serverName, String userName,String viewerId) {
		super(serverName, userName);
		this.viewerId = viewerId;
	}	
	
	public ColumnedViewerSettings(String viewerId, ArcadColumns cols) {
		this("*ALL", "*ALL",viewerId,cols); //$NON-NLS-1$ //$NON-NLS-2$
	}		
	
	/**
	 * @param serverName
	 * @param userName
	 * @param viewerId
	 * @param cols
	 */
	public ColumnedViewerSettings(String serverName, String userName,
			String viewerId, ArcadColumns cols) {
		this(serverName, userName,viewerId);
		this.columns = cols;
	}
	

	/**
	 * @param serverName
	 * @param userName
	 * @param absColViewer
	 */
	public ColumnedViewerSettings(String serverName, String userName, AbstractColumnedViewer absColViewer) {
		super(serverName, userName);
		this.viewerId=absColViewer.getIdentifier();
		this.columns=absColViewer.getDisplayedColumns();
	}
	
	public ArcadColumn getColumn(int index){
		return this.columns.items(index);
	}
	
	/**
	 * @return Renvoie columns.
	 */
	public ArcadColumns getColumns() {
		return columns;
	}
	/**
	 * @return Renvoie viewerId.
	 */
	public String getViewerId() {
		return viewerId;
	}
	/**
	 * @param columns columns à définir.
	 */
	public void setColumns(ArcadColumns columns) {
		this.columns = columns;
	}
}
