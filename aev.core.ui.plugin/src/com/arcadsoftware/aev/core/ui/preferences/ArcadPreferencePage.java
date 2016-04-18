/*
 * Created on 27 avr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.arcadsoftware.aev.core.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author mlafon
 */
public abstract class ArcadPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Création d'une page de préférences.<br>
	 * A dériver dans un autre plugin que le Core UI Plugin.<br>
	 * Dois affecter le PreferenceStore du plugin conserné.
	 * 
	 * @param store
	 *            stockage des propriétés à éditer.
	 */
	protected ArcadPreferencePage(IPreferenceStore store) {
		super(GRID);
		setPreferenceStore(store);
		String description = getPageDescription();
		if (description != null)
			setDescription(description);
	}

	/**
	 * A utiliser uniquement dans le Core UIPlugin.
	 * 
	 */
	public ArcadPreferencePage() {
		this(EvolutionCoreUIPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Method getPageHelpContextId must be overridden by subclasses to provide
	 * the help context ID of the page. Return null for no page F1 help.
	 * 
	 * @return String
	 */
	protected abstract String getPageHelpContextId();

	/**
	 * Method getPageDescription must be overridden by subclasses to provide the
	 * description of the page. Return null for no description.
	 * 
	 * @return String
	 */
	protected abstract String getPageDescription();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	/**
	 * Ajoute un champ de saisie de couleur.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addColorField(String key, String label) {
		addField(new ColorFieldEditor(key, label, getFieldEditorParent()));
	}

	/**
	 * Ajoute un champ de saisie booléen.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addBooleanField(String key, String label) {
		addField(new BooleanFieldEditor(key, label, getFieldEditorParent()));
	}

	/**
	 * Ajoute un champ de saisie de chaine de caractère.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addStringField(String key, String label) {
		addField(new StringFieldEditor(key, label, getFieldEditorParent()));
	}

	/**
	 * Ajoute un champ de saisie de chaine de caractère.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addStringField(String key, String label, int validateStrategy) {
		addField(new StringFieldEditor(key, label, StringFieldEditor.UNLIMITED, validateStrategy,
				getFieldEditorParent()));
	}

	/**
	 * Ajoute un champ de saisie entier.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addIntegerField(String key, String label) {
		addField(new IntegerFieldEditor(key, label, getFieldEditorParent()));
	}

	/**
	 * Ajoute un champ de saisie de chaine de mots de passe.
	 * 
	 * @param key
	 * @param label
	 */
	protected void addPwdStringField(String key, String label) {
		StringFieldEditor pwdFieldEditor = new StringFieldEditor(key, label, getFieldEditorParent());
		pwdFieldEditor.getTextControl(getFieldEditorParent()).setEchoChar(GuiFormatTools.PWDChar);
		addField(pwdFieldEditor);
	}

	/**
	 * Ajoute un champ de saisie pour un type énumération.
	 * 
	 * @param key
	 * @param label
	 * @param labelAndValues
	 *            tableau de la forme {{text,val}, ...}
	 */
	protected void addEnumerationField(String key, String label, String[][] labelAndValues) {
		addField(new RadioGroupFieldEditor(key, label, 1, labelAndValues, getFieldEditorParent(), true));
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
		Control control = super.createContents(parent);
		String helpID = getPageHelpContextId();
		if (helpID != null)
			GuiFormatTools.setHelp(control, helpID);
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		// Permet de mettre à jour la référence de l'aide directement, dès
		// que la page est sélectionnée.
		if (visible || !getShell().isDisposed()) {
			String helpID = getPageHelpContextId();
			if (helpID != null)
				GuiFormatTools.setHelp(getControl(), helpID);
		}
	}

}
