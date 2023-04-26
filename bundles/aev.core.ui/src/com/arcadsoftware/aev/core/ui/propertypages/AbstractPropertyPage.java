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

	public abstract boolean checkElement(Object o);

	public abstract ArcadEntity affectElement(Object o);

	public abstract boolean readElement(Object o);

	protected abstract void fillWithItem(ArcadEntity itemToFill);

	protected abstract void fillWithContent();

	protected void doApply() {
		// Do nothing
	}

	/**
	 * Cette m�thode est � surcharger lorsque l'on ne veut pas que la page soit
	 * �ditable (mode consultation).
	 * 
	 * @param itemEditable
	 * @return booelan : si la page est �ditable dans sa globalit� ou non.
	 */
	protected boolean isEditable(ArcadEntity itemEditable) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage#setValue()
	 */
	@Override
	protected void setValue() {
		Object o = getElement();
		if (checkElement(o)) {
			item = affectElement(o);
			if (readElement(o))
				fillWithItem(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(gridData);

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout(3, false);
		grid.marginHeight = 0;
		grid.marginWidth = 0;
		composite.setLayout(grid);
		gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		fillContents(composite);
		setValue();

		enabled = isEditable(item);

		if (!enabled) {
			// La page n'est pas �ditable dans sa globalit�.
			setCompositeNotEditable(composite);
		} else {
			// La gestion de l'�dition des contr�les se fait de mani�re plus
			// fine.
			Control[] c = doRightManagement();
			setControlsNotEditable(c);
		}
		return composite;
	}

	private void setControlsNotEditable(Control[] control) {
		for (int i = 0; i < control.length; i++) {
			control[i].setEnabled(false);
		}
	}

	/**
	 * Cette m�thode est � surcharger pour choisir de mani�re plus fine (en
	 * fonction de droits, par exemple) les contr�les qui ne doivent pas �tre
	 * "�ditables".
	 * 
	 * @return Control[] : tableau des contr�les qui ne doivent pas �tre
	 *         �ditables.
	 */
	protected Control[] doRightManagement() {
		return new Control[0];
	}

	private void setCompositeNotEditable(Composite composite) {
		composite.setEnabled(false);
		this.noDefaultAndApplyButton();
		Control[] ctrls = composite.getChildren();
		for (int i = 0; i < ctrls.length; i++) {
			if (!(ctrls[i] instanceof Label))
				ctrls[i].setEnabled(false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}

}
