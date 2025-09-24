/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
/*
 * Cr�� le 3 ao�t 07
 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

public class CustomExpandBar {
	public static final String MAIN = "main"; //$NON-NLS-1$

	ExpandBar infoBar;

	public CustomExpandBar(final Composite parent) {
		this(parent, SWT.NONE);
	}

	public CustomExpandBar(final Composite parent, final int style) {
		// Cr�ation de l'expandBar
		infoBar = new ExpandBar(parent, style);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 3;
		infoBar.setLayoutData(gridData);
		infoBar.addExpandListener(new ExpandListener() {
			@Override
			public void itemCollapsed(final ExpandEvent arg0) {
				Display.getDefault().asyncExec(() -> {
					final ExpandItem item = (ExpandItem) arg0.item;
					updateExpandItemHeight(item);
				});
			}

			@Override
			public void itemExpanded(final ExpandEvent arg0) {
				Display.getDefault().asyncExec(() -> {
					final ExpandItem item = (ExpandItem) arg0.item;
					updateExpandItemHeight(item);
				});
			}
		});
		parent.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				CustomExpandBar.this.redraw();
			}
		});
	}

	public ExpandBar getExpandBar() {
		return infoBar;
	}

	private int getGrabHeight(final ExpandItem item) {
		final ExpandBar bar = item.getParent();
		int grabHeight = bar.getClientArea().height;
		final int count = bar.getItemCount();
		for (int i = 0; i < count; i++) {
			final ExpandItem ei = bar.getItems()[i];
			grabHeight -= ei.getHeaderHeight();
			if (ei.getExpanded() && !isMain(ei)) {
				grabHeight -= ei.getHeight();
			}
		}
		grabHeight -= bar.getSpacing() * (count + 1);
		return grabHeight;
	}

	private ExpandItem getTheOnlyOneExpanded(final ExpandBar bar) {
		ExpandItem onlyOne = null;
		final int count = bar.getItemCount();
		for (int i = 0; i < count; i++) {
			final ExpandItem ei = bar.getItems()[i];
			if (ei.getExpanded()) {
				if (onlyOne == null) {
					onlyOne = ei;
				} else {
					return null;
				}
			}
		}
		return onlyOne;
	}

	public void initialize() {
		Display.getDefault().asyncExec(() -> {
			final ExpandItem item = infoBar.getItem(0);
			updateExpandItemHeight(item);
		});
	}

	private boolean isMain(final ExpandItem item) {
		if (item.getData() != null) {
			if (item.getData() instanceof String) {
				return ((String) item.getData()).equals(MAIN);
			}
		}
		return false;
	}

	public void redraw() {
		Display.getDefault().asyncExec(() -> {
			final ExpandItem item = infoBar.getItem(0);
			updateExpandItemHeight(item);
		});
	}

	void updateExpandItemHeight(final ExpandItem item) {
		final ExpandBar bar = item.getParent();
		final int count = bar.getItemCount();
		final ExpandItem onlyOneExpanded = getTheOnlyOneExpanded(bar);
		if (onlyOneExpanded != null) {
			int grabHeight = bar.getClientArea().height;
			grabHeight -= count * onlyOneExpanded.getHeaderHeight();
			grabHeight -= (count + 1) * bar.getSpacing();
			onlyOneExpanded.setHeight(grabHeight);
		} else {
			// Affectation des tailles
			for (int i = 0; i < count; i++) {
				final ExpandItem ei = bar.getItems()[i];
				if (!isMain(ei)) {
					ei.setHeight(ei.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				}
			}
			final int grabHeight = getGrabHeight(item);
			for (int i = 0; i < count; i++) {
				final ExpandItem ei = bar.getItems()[i];
				if (isMain(ei)) {
					ei.setHeight(grabHeight);
				}
			}

		}

		// if (!isMain(item)) {
		// for (int i=0;i<count;i++) {
		// ExpandItem ei = bar.getItems()[i];
		// if (isMain(ei) || isOnlyOneExpanded(ei)){
		// boolean expanded = ei.getExpanded();
		// if (expanded)
		// ei.setExpanded(false);
		// int grabHeight = getGrabHeight(item);
		// ei.setHeight(grabHeight);
		// if (expanded)
		// ei.setExpanded(true);
		// }
		// }
		// }
	}

}
