package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

/**
 * @author MD
 */
public interface IColumnedSearcher {
	boolean match(Object element, IColumnResolver resolver);
}
