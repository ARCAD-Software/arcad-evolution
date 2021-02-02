/*
 * Created on 13 nov. 2006
 */
package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author MD
 */
public class TestTableViewer extends AbstractColumnedTableViewer {

	public class TestItem extends ArcadEntity {
		private String val1 = StringTools.EMPTY;
		private String val2 = StringTools.EMPTY;
		private String val3 = StringTools.EMPTY;

		public TestItem(final String v1, final String v2, final String v3) {
			super();
			val1 = v1;
			val2 = v2;
			val3 = v3;
		}

		@Override
		public String getLabel() {
			return StringTools.EMPTY;
		}

		/**
		 * @return Returns the val1.
		 */
		public String getVal1() {
			return val1;
		}

		/**
		 * @return Returns the val2.
		 */
		public String getVal2() {
			return val2;
		}

		/**
		 * @return Returns the val3.
		 */
		public String getVal3() {
			return val3;
		}
	}

	ArcadCollection elements = new ArcadCollection();

	/**
	 * @param parent
	 * @param style
	 */
	public TestTableViewer(final Composite parent, final int style) {
		super(parent, style);

		elements.add(new TestItem("L1C1", "L1C2", "L1C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L2C1", "L2C2", "L2C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L3C1", "L3C2", "L3C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L4C1", "L4C2", "L4C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L5C1", "L5C2", "L5C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L2C1", "L4C2", "L1C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		elements.add(new TestItem("L2C1", "L1C2", "L3C3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setInput(elements);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTableViewer
	 * #createTableLabelProvider(com.arcadsoftware.aev.core.ui.viewers.columned. AbstractColumnedViewer)
	 */
	@Override
	public AbstractColumnedTableLabelProvider createTableLabelProvider(final AbstractColumnedViewer viewer) {
		return new AbstractColumnedTableLabelProvider(viewer) {
			// Do nothing
		};
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #doOnDoubleClick(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected void doOnDoubleClick(final IStructuredSelection selection) {
		getDisplayedColumns().delete(1);
		refreshColumns();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return "com.arcadsoftware.aev.core.ui.viewers.columned.impl.testTableViewer"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getReferenceColumns()
	 */
	@Override
	public ArcadColumns getReferenceColumns() {
		final ArcadColumns cols = new ArcadColumns();
		cols.add(new ArcadColumn("col1", "InternalHeader1", "InternalHeader1", ArcadColumn.VISIBLE, 0, 100));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		cols.add(new ArcadColumn("col2", "InternalHeader2", "InternalHeader2", ArcadColumn.VISIBLE, 1, 100));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		cols.add(new ArcadColumn("col3", "InternalHeader3", "InternalHeader3", ArcadColumn.VISIBLE, 2, 100));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setSortOnColumn(true);
		return cols;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getValue(java.lang.Object, int)
	 */
	@Override
	public String getValue(final Object element, final int columnIndex) {
		if (element instanceof TestItem) {
			final TestItem i = (TestItem) element;
			switch (columnIndex) {
			case 0:
				return i.getVal1();
			case 1:
				return i.getVal2();
			case 2:
				return i.getVal3();
			}
		}
		return StringTools.EMPTY;
	}
}
