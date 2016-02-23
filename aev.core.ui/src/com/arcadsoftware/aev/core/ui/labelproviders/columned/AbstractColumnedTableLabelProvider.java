package com.arcadsoftware.aev.core.ui.labelproviders.columned;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.collections.IArcadDisplayable;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSearcher;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnResolver;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnedSearcher;

/**
 * @author MD
 * 
 */
public abstract class AbstractColumnedTableLabelProvider extends AbstractColumnedLabelProviderAdapter implements
		IColumnResolver {

	protected AbstractColumnedViewer viewer;

	public AbstractColumnedTableLabelProvider(AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
	 * .Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		int actualIndex = viewer.getActualIndexFromDisplayedIndex(columnIndex);
		if (actualIndex == -1)
			return null;
		Image result = getActualImage(element, actualIndex);
		if (result == null) {
			if (columnIndex == 0) {
				if (element instanceof IArcadDisplayable) {
					IArcadDisplayable e = (IArcadDisplayable) element;
					String overlay = e.getOverlayID();
					// ATTENTION : il est nécessaire de surcharger les méthodes
					// getImage et getCompositeImage si vos icônes
					// ne sont pas dans Core UI
					if (overlay != null)
						return getCompositeImage(e.getIconID(), overlay);
					return getImage(((IArcadCollectionItem) element).getIconID());
				}
			}
		} else
			return result;
		return null;
	}

	public Color getMatchColor() {
		return new Color(Display.getCurrent(), 255, 230, 230);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
	 * .Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		IColumnedSearcher searcher = viewer.getSearcher();
		if (searcher != null) {
			TableItem ti = getTableItem(element);
			if (ti != null) {
				if (searcher.match(element, this) && ((ColumnedSearcher) searcher).getCriteriaList().getSize() > 0) {
					ti.setBackground(getMatchColor());
				} else
					ti.setBackground(((ColumnedTableViewer) viewer.getViewer()).getTable().getBackground());
			}
		}
		// Transcryption de l'index de la colonne affichée en index réel
		// parmi les colonnes de références.
		int actualIndex = viewer.getActualIndexFromDisplayedIndex(columnIndex);
		if (actualIndex == -1)
			return StringTools.EMPTY;
		return getValue(element, actualIndex);
	}

	@Override
	public Object getColumnValue(Object element, int columnIndex) {
		return geTypedValue(element, columnIndex);
	}
	
	/**
	 * @param element
	 * @param actualColumnIndex
	 */
	protected Image getActualImage(Object element, int actualColumnIndex) {
		return null;
	}

	public TableItem getTableItem(Object element) {
		if (viewer != null) {
			if (viewer.getViewer() instanceof ColumnedTableViewer)
				return ((ColumnedTableViewer) viewer.getViewer()).findTableItem(element);
		}
		return null;
	}

	public String getValue(Object element, int columnIndex) {
		return viewer.getValue(element, columnIndex);
	}
	
	public Object geTypedValue(Object element, int columnIndex) {
		return viewer.getTypedValue(element, columnIndex);
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas
	 * dans Core UI
	 * 
	 * @param key
	 * @return
	 */
	protected Image getImage(String key) {
		return CoreUILabels.getImage(key);
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas
	 * dans Core UI
	 * 
	 * @param key
	 * @return
	 */
	protected Image getCompositeImage(String key, String decoKey) {
		return CoreUILabels.getCompositeImage(key, decoKey);
	}

}
