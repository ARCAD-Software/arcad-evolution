package com.arcadsoftware.aev.core.ui.editors;

import java.util.ArrayList;

import org.eclipse.ui.part.EditorPart;

import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.ui.listeners.IDirtyListener;
import com.arcadsoftware.aev.core.ui.listeners.IEntitySaved;

public abstract class AbstractArcadEditorPart extends EditorPart
		implements IDirtyListener {

	private boolean dirtyPart = false;

	String errorMessage = null;

	String initialName = ""; //$NON-NLS-1$

	public final int LABEL_MAXLENGTH = 30;

	ArrayList<IEntitySaved> savedListeners = new ArrayList<>();

	public void addSavedListener(final IEntitySaved listener) {
		savedListeners.add(listener);
	}

	public abstract boolean checkData();

	@Override
	public void dirtyEvent(final boolean dirty) {
		dirtyPart = dirty;
		firePropertyChange(PROP_DIRTY);
	}

	public boolean doBeforeSaving() {
		if (!checkData()) {
			if (errorMessage != null) {
				// EvolutionCoreUIPlugin.openError(errorMessage);
			}
			return false;
		}
		return true;
	}

	@Override
	public void doSaveAs() {
	}

	public void fireSaved(final ArcadEntity entity) {
		for (final IEntitySaved l : savedListeners) {
			try {
				l.doAfterSaving(entity);
			} catch (final RuntimeException e1) {
				removeSavedListener(l);
			}
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getInitialName() {
		return initialName;
	}

	@Override
	public boolean isDirty() {
		return dirtyPart;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void removeSavedListener(final IEntitySaved listener) {
		savedListeners.remove(listener);
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public void setFocus() {
	}

	public void setInitialName(final String initialName) {
		this.initialName = initialName;
	}

	protected String truncateName(final String label) {
		if (label.length() > LABEL_MAXLENGTH) {
			return label.substring(0, LABEL_MAXLENGTH - 3) + "..."; //$NON-NLS-1$
		}
		return label;

	}

}
