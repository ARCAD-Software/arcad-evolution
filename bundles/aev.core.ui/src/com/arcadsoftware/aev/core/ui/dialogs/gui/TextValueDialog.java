/*
 * Created on 15 janv. 2007
 */
package com.arcadsoftware.aev.core.ui.dialogs.gui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadCenteredDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * Dialogue permettant la saisie d'une valeur texte.
 * 
 * @author MD
 * 
 */
public class TextValueDialog extends ArcadCenteredDialog {
	protected static int DIALOG_WIDTH = 350;
	protected static int DIALOG_HEIGHT = 200;
	Text valueText = null;
	private int limit = 0;
	private String label = StringTools.EMPTY;
	private String defaultValue = StringTools.EMPTY;
	private String value = StringTools.EMPTY;

	/**
	 * @param parentShell
	 */
	public TextValueDialog(Shell parentShell, String title, String label, int limit, String defaultText) {
		super(parentShell, DIALOG_WIDTH, DIALOG_HEIGHT, CoreUILabels.resString(title));
		this.label = label;
		this.limit = limit;
		this.defaultValue = defaultText;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		valueText = GuiFormatTools.createLabelledText(composite, label, defaultValue, limit);
		return composite;
	}

	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		value = valueText.getText();
		super.buttonPressed(buttonId);
	}

	/**
	 * 
	 * @param parentShell
	 * @return String
	 */
	public static String open(Shell parentShell, String title, String label, int limit, String defaultText) {
		TextValueDialog dialog = new TextValueDialog(parentShell, title, label, limit, defaultText);
		if (dialog.open() == 0)
			return dialog.getValue();
		return StringTools.EMPTY;
	}

	/**
	 * 
	 * @param parentShell
	 * @return String
	 */
	public static String open(Shell parentShell, String title, String label, String defaultText) {
		return open(parentShell, title, label, -1, defaultText);
	}

	/**
	 * 
	 * @param parentShell
	 * @return String
	 */
	public static String open(Shell parentShell, String title, String label) {
		return open(parentShell, title, label, -1, StringTools.EMPTY);
	}

}
