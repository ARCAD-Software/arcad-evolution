/*
 * Created on 19 févr. 2007
 *
 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.ColumnedDefaultTableLabelProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author MD
 */
public abstract class AbstractSimpleTableManagerComposite extends AbstractArcadComposite {

	public class InternalTableViewer extends AbstractColumnedTableViewer {
		/**
		 * @param parent
		 * @param style
		 */
		public InternalTableViewer(final Composite parent) {
			super(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL, false);
		}

		@Override
		@SuppressWarnings("hiding")
		public AbstractColumnedTableLabelProvider createTableLabelProvider(final AbstractColumnedViewer viewer) {
			return getInternalLabelProvider(viewer);
		}

		@Override
		protected void doOnSelectionChange(final IStructuredSelection selection) {
			onSelection(selection);
		}

		@Override
		public String getIdentifier() {
			return getInternalIdentifier();
		}

		@Override
		public ArcadColumns getReferenceColumns() {
			return getInternalReferenceColumns();
		}

		public String getReferenceFileName() {
			return null;
		}

		@Override
		public String getValue(final Object element, final int columnIndex) {
			return getInternalValue(element, columnIndex);
		}

		/**
		 * @param parent
		 */
		public void initialize(final Composite parent) {
			init();
		}

	}

	private Object selectedItem = null;

	private boolean updateButton = false;
	protected InternalTableViewer viewer;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractSimpleTableManagerComposite(final Composite parent, final int style, final boolean updateButton) {
		super(parent, style);
		format();
		this.updateButton = updateButton;
	}

	public void createControl() {
		viewer = new InternalTableViewer(this);
		viewer.initialize(this);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		viewer.getTable().setLayoutData(gridData);

		final Composite buttonBar = new Composite(this, SWT.BORDER);
		GridLayout layout;
		if (updateButton) {
			layout = new GridLayout(3, false);
		} else {
			layout = new GridLayout(2, false);
		}
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonBar.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		// gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		buttonBar.setLayoutData(gridData);

		// ((GridData)buttonBar.getLayoutData()).heightHint = 30;

		final Button bdelete = new Button(buttonBar, SWT.PUSH);
		bdelete.setText(CoreUILabels.resString("Action.delete.text")); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 100;
		bdelete.setLayoutData(gridData);
		bdelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (getSelectedItem() != null) {
					if (isValidItem(getSelectedItem())) {
						if (MessageDialog.openConfirm(EvolutionCoreUIPlugin.getShell(), "ARCAD", //$NON-NLS-1$
								CoreUILabels.resString("confirmDeletion.text"))) { //$NON-NLS-1$
							if (doOnDelete(getSelectedItem())) {
								setSelectedItem(null);
								refresh();
							}
						}
					}
				}
			}
		});

		if (updateButton) {
			final Button bupdate = new Button(buttonBar, SWT.PUSH);
			bupdate.setText(CoreUILabels.resString("action.update.text")); //$NON-NLS-1$
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.widthHint = 100;
			bupdate.setLayoutData(gridData);
			bupdate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (getSelectedItem() != null) {
						if (isValidItem(getSelectedItem())) {
							if (doOnUpdate(getSelectedItem())) {
								refresh();
							}
						}
					}
				}
			});
		}

		final Button badd = new Button(buttonBar, SWT.PUSH);
		badd.setText(CoreUILabels.resString("Action.add.text")); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		gridData.widthHint = 100;
		badd.setLayoutData(gridData);
		badd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (doOnAdd()) {
					refresh();
				}
			}
		});
	}

	protected boolean doOnAdd() {
		return true;
	}

	/**
	 * @param deleted
	 */
	protected boolean doOnDelete(final Object deleted) {
		return true;
	}

	/**
	 * @param updated
	 */
	protected boolean doOnUpdate(final Object updated) {
		return true;
	}

	private void format() {
		final GridLayout grid = new GridLayout(3, false);
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		setLayout(grid);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		setLayoutData(gridData);
	}

	public abstract String getInternalIdentifier();

	@SuppressWarnings("hiding")
	protected AbstractColumnedTableLabelProvider getInternalLabelProvider(final AbstractColumnedViewer viewer) {
		return new ColumnedDefaultTableLabelProvider(viewer);
	}

	public abstract ArcadColumns getInternalReferenceColumns();

	public abstract String getInternalValue(Object element, int columnIndex);

	public Object getSelectedItem() {
		return selectedItem;
	}

	public abstract boolean isValidItem(Object item);

	public void onSelection(final IStructuredSelection selection) {
		if (!selection.isEmpty()) {
			setSelectedItem(selection.getFirstElement());
		} else {
			setSelectedItem(null);
		}
	}

	public void refresh() {
		viewer.refresh();
	}

	public void setInput(final Object input) {
		viewer.setInput(input);
	}

	public void setSelectedItem(final Object selectedItem) {
		this.selectedItem = selectedItem;
	}

}
