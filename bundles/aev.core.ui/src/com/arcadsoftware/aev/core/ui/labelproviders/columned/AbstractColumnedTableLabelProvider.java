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
 */
public abstract class AbstractColumnedTableLabelProvider extends AbstractColumnedLabelProviderAdapter implements
		IColumnResolver {

	protected AbstractColumnedViewer viewer;

	public AbstractColumnedTableLabelProvider(final AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
	}

	/**
	 * @param element
	 * @param actualColumnIndex
	 */
	protected Image getActualImage(final Object element, final int actualColumnIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang .Object, int)
	 */
	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		final int actualIndex = viewer.getActualIndexFromDisplayedIndex(columnIndex);
		if (actualIndex == -1) {
			return null;
		}
		final Image result = getActualImage(element, actualIndex);
		if (result == null) {
			if (columnIndex == 0) {
				if (element instanceof IArcadDisplayable) {
					final IArcadDisplayable e = (IArcadDisplayable) element;
					final String overlay = e.getOverlayID();
					// ATTENTION : il est nécessaire de surcharger les méthodes
					// getImage et getCompositeImage si vos icônes
					// ne sont pas dans Core UI
					if (overlay != null) {
						return getCompositeImage(e.getIconID(), overlay);
					}
					return getImage(((IArcadCollectionItem) element).getIconID());
				}
			}
		} else {
			return result;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang .Object, int)
	 */
	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		final IColumnedSearcher searcher = viewer.getSearcher();
		if (searcher != null) {
			final TableItem ti = getTableItem(element);
			if (ti != null) {
				if (searcher.match(element, this) && ((ColumnedSearcher) searcher).getCriteriaList().getSize() > 0) {
					ti.setBackground(getMatchColor());
				} else {
					ti.setBackground(((ColumnedTableViewer) viewer.getViewer()).getTable().getBackground());
				}
			}
		}
		// Transcryption de l'index de la colonne affichée en index réel
		// parmi les colonnes de références.
		final int actualIndex = viewer.getActualIndexFromDisplayedIndex(columnIndex);
		if (actualIndex == -1) {
			return StringTools.EMPTY;
		}
		return getValue(element, actualIndex);
	}

	@Override
	public Object getColumnValue(final Object element, final int columnIndex) {
		return geTypedValue(element, columnIndex);
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas dans Core UI
	 *
	 * @param key
	 * @return
	 */
	protected Image getCompositeImage(final String key, final String decoKey) {
		return CoreUILabels.getCompositeImage(key, decoKey);
	}

	/**
	 * Il est nécessaire de surcharger cette méthode si votre icône n'est pas dans Core UI
	 *
	 * @param key
	 * @return
	 */
	protected Image getImage(final String key) {
		return CoreUILabels.getImage(key);
	}

	public Color getMatchColor() {
		return new Color(Display.getCurrent(), 255, 230, 230);
	}

	public TableItem getTableItem(final Object element) {
		if (viewer != null) {
			if (viewer.getViewer() instanceof ColumnedTableViewer) {
				return ((ColumnedTableViewer) viewer.getViewer()).findTableItem(element);
			}
		}
		return null;
	}

	@Override
	public String getValue(final Object element, final int columnIndex) {
		return viewer.getValue(element, columnIndex);
	}

	public Object geTypedValue(final Object element, final int columnIndex) {
		return viewer.getTypedValue(element, columnIndex);
	}

}
