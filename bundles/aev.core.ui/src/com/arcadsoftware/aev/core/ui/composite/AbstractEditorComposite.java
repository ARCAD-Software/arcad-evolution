package com.arcadsoftware.aev.core.ui.composite;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.editors.AbstractArcadEditorPart;
import com.arcadsoftware.aev.core.ui.listeners.IDirtyListener;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

public abstract class AbstractEditorComposite extends AbstractArcadComposite
		implements ModifyListener, SelectionListener {

	private class DirtyListener {
		private final ArcadListenerList contentChangedListeners = new ArcadListenerList(3);

		/**
		 *
		 */
		public DirtyListener() {
			super();
		}

		public void addDirtyListener(final IDirtyListener listener) {
			contentChangedListeners.add(listener);
		}

		public void fireContentChanged() {
			final Object[] listeners = contentChangedListeners.getListeners();
			for (final Object listener : listeners) {
				final IDirtyListener l = (IDirtyListener) listener;
				try {
					l.dirtyEvent(dirty);
				} catch (final RuntimeException e1) {
					removeDirtyListener(l);
				}
			}
		}

		/*
		 * (non-Javadoc) Method declared on ISelectionProvider.
		 */
		public void removeDirtyListener(final IDirtyListener listener) {
			contentChangedListeners.remove(listener);
		}
	}

	ArrayList<AbstractEditorComposite> childs = new ArrayList<>();
	boolean dirty = false;

	Object edited;
	DirtyListener l = new DirtyListener();

	private final AbstractArcadEditorPart parentEditorPart;

	public AbstractEditorComposite(final Composite parent, final int style, final Object edited,
			final AbstractArcadEditorPart editor) {
		this(parent, style, edited, true, editor);
	}

	public AbstractEditorComposite(final Composite parent, final int style, final Object edited, final boolean withinit,
			final AbstractArcadEditorPart editor) {
		super(parent, style);
		this.edited = edited;
		parentEditorPart = editor;
		format();
		if (withinit) {
			createContent();
		}
	}

	public void addChangeListener(final IDirtyListener listener) {
		l.addDirtyListener(listener);
	}

	protected boolean checkData(final Object edited) {
		return true;
	}

	public abstract void createContent();

	protected Label createInformationLabel(final Composite parent) {
		final Composite userInfoBar = new Composite(parent, SWT.BORDER);
		final GridLayout l = new GridLayout(1, true);
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop = 1;
		l.marginLeft = 5;
		userInfoBar.setLayout(l);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 18;
		userInfoBar.setLayoutData(gridData);
		final Label infoLabel = GuiFormatTools.createLabel(userInfoBar, ""); //$NON-NLS-1$
		return infoLabel;
	}

	public boolean editedCheckData(final Object edited) {
		return recursiveCheckData(this, edited);
	}

	public void editedFromScreen(final Object edited) {
		recursiveFromScreen(this, edited);
		fromScreen(edited);
	}

	public void editedToScreen(final Object edited) {
		recursiveToScreen(this, edited);
		toScreen(edited);
	}

	public void fireDirty() {
		dirty = true;
		l.fireContentChanged();
	}

	public void format() {
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		setLayoutData(gridData);
	}

	protected void fromScreen(final Object edited) {

	}

	private AbstractEditorComposite getAncestor(final AbstractEditorComposite c) {
		AbstractEditorComposite result = c;
		Composite composite = c;
		while (composite.getParent() != null) {
			if (composite.getParent() instanceof AbstractEditorComposite) {
				result = (AbstractEditorComposite) composite.getParent();
			}
			composite = composite.getParent();
		}
		return result;
	}

	public ArrayList<AbstractEditorComposite> getChilds() {
		return childs;
	}

	public Object getEdited() {
		return edited;
	}

	public AbstractArcadEditorPart getParentEditorPart() {
		return parentEditorPart;
	}

	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void modifyText(final ModifyEvent arg0) {
		dirty = true;
		l.fireContentChanged();
	}

	private boolean recursiveCheckData(final AbstractEditorComposite c, final Object edited) {
		final ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (final AbstractEditorComposite kid : kids) {
			if (kid.checkData(edited)) {
				recursiveFromScreen(kid, edited);
			} else {
				return false;
			}
		}
		return c.checkData(edited);
	}

	private void recursiveFromScreen(final AbstractEditorComposite c, final Object edited) {
		final ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (final AbstractEditorComposite kid : kids) {
			kid.fromScreen(edited);
			recursiveFromScreen(kid, edited);
		}
	}

	private void recursiveToScreen(final AbstractEditorComposite c, final Object edited) {
		final ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (final AbstractEditorComposite kid : kids) {
			kid.toScreen(edited);
			recursiveToScreen(kid, edited);
		}
	}

	public void registerControl(final Control c) {
		final AbstractEditorComposite registry = getAncestor(this);
		if (registry != null) {
			if (c instanceof Text) {
				((Text) c).addModifyListener(registry);
			}
			if (c instanceof Button) {
				((Button) c).addSelectionListener(registry);
			}
			if (c instanceof Combo) {
				((Combo) c).addSelectionListener(registry);
				((Combo) c).addModifyListener(registry);
			}
			if (c instanceof DoubleSpinner) {
				((DoubleSpinner) c).addSelectionListener(registry);
			}
		}

	}

	public void removeChangeListener(final IDirtyListener listener) {
		l.removeDirtyListener(listener);
	}

	public void setDirty(final boolean dirty) {
		this.dirty = dirty;
	}

	protected void toScreen(final Object edited) {

	}

	@Override
	public void widgetDefaultSelected(final SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(final SelectionEvent arg0) {
		dirty = true;
		l.fireContentChanged();
	}

}
