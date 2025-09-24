/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
/*
 * Created on 12 avr. 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.model.ArcadEntity;

/**
 * <p>
 * <b><u>Cr�ation des contr�les de saisie</u></b><br>
 * Pour cr�er les contr�les de saisie de votre page : <br>
 * - Surchargez la m�thode {@link #createControlPage(Composite) createControlPage(Composite) }<br>
 * - Surchargez la m�thode {@link #setDefaultValue() setDefaultValue()}
 * </p>
 * <p>
 * <b><u>Validation de la saisie</u></b><br>
 * Pour valider les informations saisies, surchargez la m�thode {@link #checkData() checkData()} pour y inscrire les
 * r�gles de validation de votre saisie. Tant que cette m�thode renvoit "false", la page n'est pas consid�r�e
 * comme valid�e.<br>
 * </p>
 *
 * @author MD
 */
public abstract class AbstractSimpleItemWizardPage extends ArcadWizardPage {

	protected ArcadEntity item = null;
	private String nextPageName = null;

	/**
	 * @param pageName
	 */
	public AbstractSimpleItemWizardPage(final String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public AbstractSimpleItemWizardPage(final String pageName, final String title, final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public AbstractSimpleItemWizardPage(final String pageName, final String title, final String description) {
		super(pageName, title);
		setDescription(description);
	}

	public AbstractSimpleItemWizardPage(final String pageName, final String title, final String description,
			final ArcadEntity item) {
		this(pageName, title, description);
		this.item = item;
	}

	/**
	 * M�thode permettant d'ajouter un iSelectionListener d�clenchant la validation de la donn�es saisie pour un
	 * widget de type Button.
	 *
	 * @param b
	 *            Button : Button � mettre sous contr�le
	 */
	protected void addCheckDataListeners(final Button b) {
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				setPageComplete(checkData());
			}
		});
	}

	/**
	 * M�thode permettant d'ajouter un iSelectionListener et un ModifyListener d�clenchant la validation de la
	 * donn�es saisie pour une liste Combo.
	 *
	 * @param c
	 *            Combo : Combo � mettre sous contr�le
	 */

	protected void addCheckDataListeners(final Combo c) {
		c.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				setPageComplete(checkData());
			}
		});
		c.addModifyListener(e -> setPageComplete(checkData()));
	}

	/**
	 * M�thode permettant d'ajouter un ModifyListener d�clenchant la validation de la donn�es saisie pour un
	 * widget de type Text.
	 *
	 * @param c
	 *            Text : Text � mettre sous contr�le
	 */
	protected void addCheckDataListeners(final Text t) {
		t.addModifyListener(e -> setPageComplete(checkData()));
	}

	/**
	 * M�thode permettant la validation des informations saisies.<br>
	 * La surcharge de cette m�thode permet de d�clarer vos r�gles de validation de saisie.<br>
	 * Pour int�grer l'appel de cette fonction � vos contr�le de saisie, vous pouvez utiliser les m�thodes
	 * "addCheckDataListeners()" disponible sur les Combo et les Text.
	 *
	 * @return boolean : <b>True</b> si les informations saisies sont valides, <b>false</b> sinon.
	 */
	protected boolean checkData() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	public void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		composite.setLayout(grid);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.verticalAlignment = GridData.FILL;
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessVerticalSpace = true;
			gridData.grabExcessHorizontalSpace = true;
			composite.setLayoutData(gridData);
		}
		createControlPage(composite);
		setControl(composite);
		setDefaultValue();
	}

	/**
	 * M�thode permettant la d�finition de l'interface de saisie de la page.<br>
	 * Cette m�thode pertmet de d�finir les �l�ments graphiques de saisie de la page.<br>
	 * Le composite pass� en param�tre doit �tre le parent des controles principaux de la page. Il est format�
	 * � l'aide d'un layout de type GridLayout de 3 colonnes.<br>
	 * <b>NOTE : </b><br>
	 * Pour des raison d'homog�n�it�, il est vivement recommand� d'utiliser la classe GuiFormatTools pour
	 * cr�er vos controles de saisie.
	 *
	 * @param parent
	 *            Composite : Nom du composant sur lesquels vous devez ajouter vos contr�les.
	 */
	protected abstract void createControlPage(Composite parent);

	/**
	 * @return Returns the nextPageName.
	 */
	public String getNextPageName() {
		return nextPageName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage#getPageHelpContextId ()
	 */
	@Override
	protected String getPageHelpContextId() {
		return null;
	}

	/**
	 * M�thode � surcharger le cas �ch�ant, pour pr�parer, cr�er les objets, commandes ou instructions �
	 * partir des renseignements de cette page.
	 */
	public void makePageData() {
		setPageComplete(checkData());
	}

	/**
	 * M�thode permettant l'affectation initiale des valeurs des controles de saisie.<br>
	 * Surclassez cette m�thode pour sp�cifier les valeurs par d�faut des contr�les que vous avez cr�� sur
	 * votre page.
	 */
	protected void setDefaultValue() {
		// Do nothing
	}

	/**
	 * @param nextPageName
	 *            The nextPageName to set.
	 */
	public void setNextPageName(final String nextPageName) {
		this.nextPageName = nextPageName;
	}
}