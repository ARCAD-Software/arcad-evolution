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
 * 
 */
public abstract class AbstractSimpleTableManagerComposite extends AbstractArcadComposite {

	public class InternalTableViewer extends AbstractColumnedTableViewer {
		/**
		 * @param parent
		 * @param style
		 */
		public InternalTableViewer(Composite parent) {
			super(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL, false);
		}

		@Override
		@SuppressWarnings("hiding")
		public AbstractColumnedTableLabelProvider createTableLabelProvider(AbstractColumnedViewer viewer) {
			return getInternalLabelProvider(viewer);
		}

		@Override
		public String getValue(Object element, int columnIndex) {
			return getInternalValue(element, columnIndex);
		}

		public String getReferenceFileName() {
			return null;
		}

		@Override
		public ArcadColumns getReferenceColumns() {
			return getInternalReferenceColumns();
		}

		@Override
		public String getIdentifier() {
			return getInternalIdentifier();
		}

		@Override
		protected void doOnSelectionChange(IStructuredSelection selection) {
			AbstractSimpleTableManagerComposite.this.onSelection(selection);
		}
		
		/**
		 * @param parent
		 */
		public void initialize(Composite parent) {
			init();
		}

	}

	protected InternalTableViewer viewer;

	private Object selectedItem = null;
	private boolean updateButton = false;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractSimpleTableManagerComposite(Composite parent, int style, boolean updateButton) {
		super(parent, style);
		format();
		this.updateButton = updateButton;
	}

	private void format() {
		GridLayout grid = new GridLayout(3, false);
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		this.setLayout(grid);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		this.setLayoutData(gridData);
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

		Composite buttonBar = new Composite(this, SWT.BORDER);
		GridLayout layout;
		if (updateButton)
			layout = new GridLayout(3, false);
		else
			layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonBar.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		//gridData.heightHint = 30;
		gridData.horizontalSpan = 3;
		buttonBar.setLayoutData(gridData);

		// ((GridData)buttonBar.getLayoutData()).heightHint = 30;

		Button bdelete = new Button(buttonBar, SWT.PUSH);
		bdelete.setText(CoreUILabels.resString("Action.delete.text")); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.grabExcessHorizontalSpace=true;
		gridData.widthHint = 100;
		bdelete.setLayoutData(gridData);
		bdelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
			Button bupdate = new Button(buttonBar, SWT.PUSH);
			bupdate.setText(CoreUILabels.resString("action.update.text")); //$NON-NLS-1$
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.widthHint = 100;
			bupdate.setLayoutData(gridData);
			bupdate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
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

		Button badd = new Button(buttonBar, SWT.PUSH);
		badd.setText(CoreUILabels.resString("Action.add.text")); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		gridData.widthHint = 100;
		badd.setLayoutData(gridData);
		badd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (doOnAdd()) {
					refresh();
				}
			}
		});
	}

	public void setInput(Object input) {
		viewer.setInput(input);
	}

	public void refresh() {
		viewer.refresh();
	}

	public abstract String getInternalIdentifier();

	public abstract ArcadColumns getInternalReferenceColumns();

	public abstract String getInternalValue(Object element, int columnIndex);

	@SuppressWarnings("hiding")
	protected AbstractColumnedTableLabelProvider getInternalLabelProvider(AbstractColumnedViewer viewer) {
		return new ColumnedDefaultTableLabelProvider(viewer);
	}

	public void onSelection(IStructuredSelection selection) {
		if (!selection.isEmpty()) {
			setSelectedItem(selection.getFirstElement());
		} else
			setSelectedItem(null);
	}

	public abstract boolean isValidItem(Object item);

	protected boolean doOnAdd() {
		return true;
	}

	/**
	 * @param updated
	 */
	protected boolean doOnUpdate(Object updated) {
		return true;
	}

	/**
	 * @param deleted
	 */
	protected boolean doOnDelete(Object deleted) {
		return true;
	}

	public void setSelectedItem(Object selectedItem) {
		this.selectedItem = selectedItem;
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

}
