/*
 * Created on 19 févr. 2007
 *
 */
package com.arcadsoftware.aev.core.ui.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public abstract class AbstractBasicTableManagerComposite extends AbstractArcadComposite {

	public class InternalTableViewer extends AbstractColumnedTableViewer {
		/**
		 * @param parent
		 * @param style
		 */
		public InternalTableViewer(final Composite parent) {
			super(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL, false);
		}

		@Override
		public AbstractColumnedTableLabelProvider createTableLabelProvider(final AbstractColumnedViewer viewer) {
			return getInternalLabelProvider(viewer);
		}

		@Override
		protected void doOnDoubleClick(final IStructuredSelection selection) {
			update();
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
		protected List<Action> getNextActions() {
			return getActions();
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

	protected InternalTableViewer viewer;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractBasicTableManagerComposite(final Composite parent, final int style) {
		super(parent, style);
		format();
	}

	public void add() {
		if (doOnAdd()) {
			refresh();
		}
	}

	public void createControl() {
		viewer = new InternalTableViewer(this);
		viewer.initialize(this);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		viewer.getTable().setLayoutData(gridData);
	}

	public void delete() {
		if (getSelectedItem() != null) {
			if (isValidItem(getSelectedItem())) {
				if (MessageDialog.openConfirm(EvolutionCoreUIPlugin.getShell(), getDeletionConfirmationTitle(),
						getDeletionConfirmationMessage())) {
					if (doOnDelete(getSelectedItem())) {
						setSelectedItem(null);
						refresh();
					}
				}
			}
		}
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

	protected ArrayList<Action> getActions() {
		return new ArrayList<>();
	}

	protected String getDeletionConfirmationMessage() {
		return CoreUILabels.resString("confirmDeletion.text");
	}

	protected String getDeletionConfirmationTitle() {
		return "ARCAD";
	}

	public Image getInternalActualImage(final Object element, final int columnIndex) {
		return null;
	}

	public abstract String getInternalIdentifier();

	protected AbstractColumnedTableLabelProvider getInternalLabelProvider(final AbstractColumnedViewer viewer) {
		return new ColumnedDefaultTableLabelProvider(viewer) {
			@Override
			protected Image getActualImage(final Object element, final int actualColumnIndex) {
				final Image im = getInternalActualImage(element, actualColumnIndex);
				if (im == null) {
					return super.getActualImage(element, actualColumnIndex);
				} else {
					return im;
				}
			}
		};
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

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if (viewer != null) {
			viewer.getViewer().getControl().setEnabled(enabled);
		}
	}

	public void setInput(final Object input) {
		viewer.setInput(input);
	}

	public void setSelectedItem(final Object selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	public void update() {
		if (getSelectedItem() != null) {
			if (isValidItem(getSelectedItem())) {
				if (doOnUpdate(getSelectedItem())) {
					refresh();
				}
			}
		}
	}

}
