/*
 * Créé le 3 août 07
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
	ExpandBar infoBar;

	public static final String MAIN = "main"; //$NON-NLS-1$

	public CustomExpandBar(Composite parent) {
		this(parent, SWT.NONE);
	}

	public CustomExpandBar(Composite parent, int style) {
		// Création de l'expandBar
		infoBar = new ExpandBar(parent, style);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 3;
		infoBar.setLayoutData(gridData);
		infoBar.addExpandListener(new ExpandListener() {
			public void itemExpanded(final ExpandEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						ExpandItem item = (ExpandItem) arg0.item;
						updateExpandItemHeight(item);
					}
				});
			}

			public void itemCollapsed(final ExpandEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						ExpandItem item = (ExpandItem) arg0.item;
						updateExpandItemHeight(item);
					}
				});
			}
		});
		parent.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				CustomExpandBar.this.redraw();
			}
		});
	}

	private int getGrabHeight(ExpandItem item) {
		ExpandBar bar = item.getParent();
		int grabHeight = bar.getClientArea().height;
		int count = bar.getItemCount();
		for (int i = 0; i < count; i++) {
			ExpandItem ei = bar.getItems()[i];
			grabHeight -= ei.getHeaderHeight();
			if (ei.getExpanded() && (!isMain(ei)))
				grabHeight -= ei.getHeight();
		}
		grabHeight -= (bar.getSpacing() * (count + 1));
		return grabHeight;
	}

	private boolean isMain(ExpandItem item) {
		if (item.getData() != null) {
			if (item.getData() instanceof String) {
				return (((String) item.getData()).equals(MAIN));
			}
		}
		return false;
	}

	private ExpandItem getTheOnlyOneExpanded(ExpandBar bar) {
		ExpandItem onlyOne = null;
		int count = bar.getItemCount();
		for (int i = 0; i < count; i++) {
			ExpandItem ei = bar.getItems()[i];
			if (ei.getExpanded()) {
				if (onlyOne == null)
					onlyOne = ei;
				else
					return null;
			}
		}
		return onlyOne;
	}

	void updateExpandItemHeight(ExpandItem item) {
		ExpandBar bar = item.getParent();
		int count = bar.getItemCount();
		ExpandItem onlyOneExpanded = getTheOnlyOneExpanded(bar);
		if (onlyOneExpanded != null) {
			int grabHeight = bar.getClientArea().height;
			grabHeight -= count * onlyOneExpanded.getHeaderHeight();
			grabHeight -= (count + 1) * bar.getSpacing();
			onlyOneExpanded.setHeight(grabHeight);
		} else {
			// Affectation des tailles
			for (int i = 0; i < count; i++) {
				ExpandItem ei = bar.getItems()[i];
				if (!isMain(ei))
					ei.setHeight(ei.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			}
			int grabHeight = getGrabHeight(item);
			for (int i = 0; i < count; i++) {
				ExpandItem ei = bar.getItems()[i];
				if (isMain(ei))
					ei.setHeight(grabHeight);
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

	public ExpandBar getExpandBar() {
		return infoBar;
	}

	public void initialize() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ExpandItem item = infoBar.getItem(0);
				updateExpandItemHeight(item);
			}
		});
	}

	public void redraw() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ExpandItem item = infoBar.getItem(0);
				updateExpandItemHeight(item);
			}
		});
	}

}
