/*
 * Created on 12 avr. 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
 * <b><u>Création des contrôles de saisie</u></b><br>
 * Pour créer les contrôles de saisie de votre page : <br>
 * - Surchargez la méthode {@link #createControlPage(Composite)
 * createControlPage(Composite) }<br>
 * - Surchargez la méthode {@link #setDefaultValue() setDefaultValue()}
 * </p>
 * <p>
 * <b><u>Validation de la saisie</u></b><br>
 * Pour valider les informations saisies, surchargez la méthode
 * {@link #checkData() checkData()} pour y inscrire les règles de validation de
 * votre saisie. Tant que cette méthode renvoit "false", la page n'est pas
 * considérée comme validée.<br>
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
	public AbstractSimpleItemWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public AbstractSimpleItemWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public AbstractSimpleItemWizardPage(String pageName, String title, String description) {
		super(pageName, title);
		this.setDescription(description);
	}

	public AbstractSimpleItemWizardPage(String pageName, String title, String description, ArcadEntity item) {
		this(pageName, title, description);
		this.item = item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage#getPageHelpContextId
	 * ()
	 */
	@Override
	protected String getPageHelpContextId() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		composite.setLayout(grid);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		createControlPage(composite);
		setControl(composite);
		setDefaultValue();
	}

	/**
	 * Méthode permettant la définition de l'interface de saisie de la page.<br>
	 * Cette méthode pertmet de définir les éléments graphiques de saisie de la
	 * page.<br>
	 * Le composite passé en paramètre doit être le parent des controles
	 * principaux de la page. Il est formaté à l'aide d'un layout de type
	 * GridLayout de 3 colonnes.<br>
	 * <b>NOTE : </b><br>
	 * Pour des raison d'homogénéité, il est vivement recommandé d'utiliser la
	 * classe GuiFormatTools pour créer vos controles de saisie.
	 * 
	 * @param parent
	 *            Composite : Nom du composant sur lesquels vous devez ajouter
	 *            vos contrôles.
	 */
	protected abstract void createControlPage(Composite parent);

	/**
	 * Méthode permettant l'affectation initiale des valeurs des controles de
	 * saisie.<br>
	 * Surclassez cette méthode pour spécifier les valeurs par défaut des
	 * contrôles que vous avez créé sur votre page.
	 */
	protected void setDefaultValue() {
		// Do nothing
	}

	/**
	 * Méthode permettant la validation des informations saisies.<br>
	 * La surcharge de cette méthode permet de déclarer vos règles de validation
	 * de saisie.<br>
	 * Pour intégrer l'appel de cette fonction à vos contrôle de saisie, vous
	 * pouvez utiliser les méthodes "addCheckDataListeners()" disponible sur les
	 * Combo et les Text.
	 * 
	 * @return boolean : <b>True</b> si les informations saisies sont valides,
	 *         <b>false</b> sinon.
	 */
	protected boolean checkData() {
		return true;
	}

	/**
	 * Méthode permettant d'ajouter un iSelectionListener et un ModifyListener
	 * déclenchant la validation de la données saisie pour une liste Combo.
	 * 
	 * @param c
	 *            Combo : Combo à mettre sous contrôle
	 */

	protected void addCheckDataListeners(Combo c) {
		c.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				setPageComplete(checkData());
			}
		});
		c.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(checkData());
			}
		});
	}

	/**
	 * Méthode permettant d'ajouter un ModifyListener déclenchant la validation
	 * de la données saisie pour un widget de type Text.
	 * 
	 * @param c
	 *            Text : Text à mettre sous contrôle
	 */
	protected void addCheckDataListeners(Text t) {
		t.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(checkData());
			}
		});
	}

	/**
	 * Méthode permettant d'ajouter un iSelectionListener déclenchant la
	 * validation de la données saisie pour un widget de type Button.
	 * 
	 * @param b
	 *            Button : Button à mettre sous contrôle
	 */
	protected void addCheckDataListeners(Button b) {
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				setPageComplete(checkData());
			}
		});
	}

	/**
	 * @return Returns the nextPageName.
	 */
	public String getNextPageName() {
		return nextPageName;
	}

	/**
	 * @param nextPageName
	 *            The nextPageName to set.
	 */
	public void setNextPageName(String nextPageName) {
		this.nextPageName = nextPageName;
	}

	/**
	 * Méthode à surcharger le cas échéant, pour préparer, créer les objets,
	 * commandes ou instructions à partir des renseignements de cette page.
	 */
	public void makePageData() {
		setPageComplete(checkData());
	}
}