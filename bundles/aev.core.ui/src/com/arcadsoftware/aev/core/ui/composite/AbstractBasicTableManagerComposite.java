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
 * 
 */
public abstract class AbstractBasicTableManagerComposite extends AbstractArcadComposite {

	public class InternalTableViewer extends AbstractColumnedTableViewer {
		/**
		 * @param parent
		 * @param style
		 */
		public InternalTableViewer(Composite parent) {
			super(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL, false);
		}

		@Override
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
			AbstractBasicTableManagerComposite.this.onSelection(selection);
		}
		
		/**
		 * @param parent
		 */
		public void initialize(Composite parent) {
			init();
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		protected List getNextActions() {
			return AbstractBasicTableManagerComposite.this.getActions();
		}

		@Override
		protected void doOnDoubleClick(IStructuredSelection selection) {		
			update();
		}
		
	}

	protected InternalTableViewer viewer;

	private Object selectedItem = null;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractBasicTableManagerComposite(Composite parent, int style) {
		super(parent, style);
		format();
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
	
	public Image getInternalActualImage(Object element, int columnIndex) {
		return null;
	}

	
	protected AbstractColumnedTableLabelProvider getInternalLabelProvider(AbstractColumnedViewer viewer) {
		return new ColumnedDefaultTableLabelProvider(viewer){
			@Override
			protected Image getActualImage(Object element, int actualColumnIndex) {
				Image im = getInternalActualImage(element,actualColumnIndex);
				if (im==null) {
					return super.getActualImage(element, actualColumnIndex);
				} else {
					return im;
				}				
			};
		};
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

	
	public void add(){
		if (doOnAdd()) {
			refresh();
		}
	}
	
	@Override
	public void update(){
		if (getSelectedItem() != null) {
			if (isValidItem(getSelectedItem())) {
				if (doOnUpdate(getSelectedItem())) {
					refresh();
				}
			}
		}		
	}
	
	
	protected String getDeletionConfirmationMessage(){
		return CoreUILabels.resString("confirmDeletion.text");
	}
	
	protected String getDeletionConfirmationTitle(){
		return "ARCAD";
	}
	
	public void delete(){
		if (getSelectedItem() != null) {
			if (isValidItem(getSelectedItem())) {
				if (MessageDialog.openConfirm(EvolutionCoreUIPlugin.getShell(), getDeletionConfirmationTitle(), //$NON-NLS-1$
						getDeletionConfirmationMessage())) { //$NON-NLS-1$
					if (doOnDelete(getSelectedItem())) {
						setSelectedItem(null);
						refresh();
					}
				}
			}
		}		
	}
	
	protected ArrayList<Action> getActions() {
		return new ArrayList<Action>();
	}	
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (viewer!=null) {
			viewer.getViewer().getControl().setEnabled(enabled);
		}
	}
	
}
