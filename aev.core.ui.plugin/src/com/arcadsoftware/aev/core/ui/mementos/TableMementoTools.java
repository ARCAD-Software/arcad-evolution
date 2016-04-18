/*
 * Created on 13 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;

/**
 * @author MD
 */
public class TableMementoTools {

	public static final TableMementoTools instance = new TableMementoTools();

	/**
	 * 
	 */
	private TableMementoTools() {
		super();
	}

	public static boolean getViewerDef(String filename, ArcadColumns columns) {
		try {
			columns.clear();
			XMLMemento x = XMLMemento.createReadRoot(new FileReader(filename));
			IMemento[] columnDefs = x.getChildren("columnDef"); //$NON-NLS-1$
			for (int j = 0; j < columnDefs.length; j++) {
				IMemento cd = columnDefs[j];
				String property = cd.getString("property"); //$NON-NLS-1$
				String name = cd.getString("label"); //$NON-NLS-1$
				int visible = cd.getInteger("visible").intValue(); //$NON-NLS-1$
				int width = cd.getInteger("width").intValue(); //$NON-NLS-1$						
				int position = cd.getInteger("position").intValue(); //$NON-NLS-1$
				int actualIndex = cd.getInteger("actualPosition").intValue(); //$NON-NLS-1$				
				ArcadColumn c = new ArcadColumn(property, name, visible, position, width, actualIndex);
				columns.add(c);
			}
		} catch (WorkbenchException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"TableMementoTools.getViewerDef");//$NON-NLS-1$
		} catch (FileNotFoundException e) {
			// Do nothing
		}
		return true;
	}

	public static boolean setViewerDef(String filename, ArcadColumns columns) {
		// Enregistrement du fichier
		XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (int i = 0; i < columns.count(); i++) {
			ArcadColumn c = columns.items(i);
			IMemento columnDef = x.createChild("columnDef"); //$NON-NLS-1$
			columnDef.putString("property", c.getIdentifier());//$NON-NLS-1$
			columnDef.putString("label", c.getIdentifier());//$NON-NLS-1$			
			columnDef.putInteger("visible", c.getVisible());//$NON-NLS-1$
			columnDef.putInteger("width", c.getWidth()); //$NON-NLS-1$		
			columnDef.putInteger("position", c.getPosition());//$NON-NLS-1$
		}
		try {
			x.save(new FileWriter(filename));
			return true;
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + filename);//$NON-NLS-1$
		}
		return false;
	}

}
