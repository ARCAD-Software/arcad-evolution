/*
 * Created on 27 juin 2006
 */
package com.arcadsoftware.aev.core.ui.propertypages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.arcadsoftware.aev.core.model.ArcadEntity;

/**
 * @author MD
 */
public abstract class AbstractPropertyPage extends ArcadPropertyPage {

	private boolean enabled = true;
	ArcadEntity item = null;

	public AbstractPropertyPage() {
		super();
	}

	public abstract ArcadEntity affectElement(Object o);

	public abstract boolean checkElement(Object o);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse .swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(final Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(gridData);

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout grid = new GridLayout(3, false);
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		composite.setLayout(grid);
		gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		fillContents(composite);
		setValue();

		enabled = isEditable(item);

		if (!enabled) {
			// La page n'est pas éditable dans sa globalité.
			setCompositeNotEditable(composite);
		} else {
			// La gestion de l'édition des contrôles se fait de manière plus
			// fine.
			final Control[] c = doRightManagement();
			setControlsNotEditable(c);
		}
		return composite;
	}

	protected void doApply() {
		// Do nothing
	}

	/**
	 * Cette méthode est à surcharger pour choisir de manière plus fine (en fonction de droits, par exemple) les
	 * contrôles qui ne doivent pas être "éditables".
	 *
	 * @return Control[] : tableau des contrôles qui ne doivent pas être éditables.
	 */
	protected Control[] doRightManagement() {
		return new Control[0];
	}

	protected abstract void fillWithContent();

	protected abstract void fillWithItem(ArcadEntity itemToFill);

	/**
	 * Cette méthode est à surcharger lorsque l'on ne veut pas que la page soit éditable (mode consultation).
	 *
	 * @param itemEditable
	 * @return booelan : si la page est éditable dans sa globalité ou non.
	 */
	protected boolean isEditable(final ArcadEntity itemEditable) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply() {
		if (enabled) {
			if (checkElement(getElement())) {
				fillWithContent();
				doApply();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	public abstract boolean readElement(Object o);

	private void setCompositeNotEditable(final Composite composite) {
		composite.setEnabled(false);
		noDefaultAndApplyButton();
		final Control[] ctrls = composite.getChildren();
		for (int i = 0; i < ctrls.length; i++) {
			if (!(ctrls[i] instanceof Label)) {
				ctrls[i].setEnabled(false);
			}
		}

	}

	private void setControlsNotEditable(final Control[] control) {
		for (final Control element : control) {
			element.setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage#setValue()
	 */
	@Override
	protected void setValue() {
		final Object o = getElement();
		if (checkElement(o)) {
			item = affectElement(o);
			if (readElement(o)) {
				fillWithItem(item);
			}
		}
	}

}
