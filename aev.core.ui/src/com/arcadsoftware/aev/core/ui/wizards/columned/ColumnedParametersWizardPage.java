package com.arcadsoftware.aev.core.ui.wizards.columned;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage;

/**
 * @author dlelong
 */
public class ColumnedParametersWizardPage extends ArcadWizardPage {

	IWorkbench workbench;
	IStructuredSelection selection;
	Text separatorText;
	Button includeHeaderButton;
	Button onlyDisplayedColumnsButton;
	Button onlyDisplayedFilteredDataButton;
	Text text;

	private final static String DIALOG_NAME = "ColumnedParametersWizardPage"; //$NON-NLS-1$
	boolean includeHeader = false;
	boolean onlyDisplayedColumns = false;
	boolean onlyDisplayedFilteredData = false;
	String separator = ","; //$NON-NLS-1$
	String filePath = " "; //$NON-NLS-1$
	private String[] extensions = new String[] { "*.csv" }; //$NON-NLS-1$
	private static final String STORE_HEADER = IDocProvider.ID + ".CLMCVSEXPORT_HEADER";//$NON-NLS-1$
	private static final String STORE_DISPLAYED = IDocProvider.ID + ".CLMCVSEXPORT_DISPLAYED";//$NON-NLS-1$
	private static final String STORE_FILTERED = IDocProvider.ID + ".CLMCVSEXPORT_FILTERED";//$NON-NLS-1$
	private static final String STORE_SEPARATOR = IDocProvider.ID + ".CLMCVSEXPORT_SEPARATOR";//$NON-NLS-1$
	private static final String STORE_FILEPATH = IDocProvider.ID + ".CLMCVSEXPORT_FILEPATH";//$NON-NLS-1$
	private IDialogSettings settings;

	public ColumnedParametersWizardPage(String pageName) {
		super(pageName);
		setPageComplete(true);
	}

	public ColumnedParametersWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		settings = EvolutionCoreUIPlugin.getSettings(getDialogName());
		if (settings.get(STORE_SEPARATOR) == null)
			initWidgetValues();
	}

	protected String getDialogName() {
		return DIALOG_NAME;
	}

	public void createControl(Composite parent) {
		Composite container = GuiFormatTools.createComposite(parent);

		includeHeaderButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.includeHeader"), //$NON-NLS-1$
				SWT.CENTER);
		includeHeaderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColumnedParametersWizardPage.this.includeHeader = includeHeaderButton.getSelection();
			}
		});

		onlyDisplayedColumnsButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.exportOnlyDisplayedColumns"), //$NON-NLS-1$
				SWT.CENTER);
		onlyDisplayedColumnsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColumnedParametersWizardPage.this.onlyDisplayedColumns = onlyDisplayedColumnsButton.getSelection();
			}
		});

		onlyDisplayedFilteredDataButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.exportOnlyFilteredData"), //$NON-NLS-1$
				SWT.CENTER);
		onlyDisplayedFilteredDataButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColumnedParametersWizardPage.this.onlyDisplayedFilteredData = onlyDisplayedFilteredDataButton
						.getSelection();
			}
		});

		separatorText = GuiFormatTools.createLabelledText(container, CoreUILabels
				.resString("wizardPage.label.separator")); //$NON-NLS-1$
		separatorText.setText(separator);
		separatorText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ColumnedParametersWizardPage.this.separator = separatorText.getText();
			}
		});

		text = GuiFormatTools.createLabelledTextWithFileSelector(container, CoreUILabels
				.resString("wizardPage.text.labelFileSelector"), //$NON-NLS-1$
				false, CoreUILabels.resString("wizardPage.text.titleFileSelector"), //$NON-NLS-1$
				extensions);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ColumnedParametersWizardPage.this.filePath = text.getText();
				setPageComplete(isPageComplete());
			}
		});
		setControl(parent);
		if (settings != null)
			loadWidgetValues(settings);
		setPageComplete(isPageComplete());
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean isIncludeHeader() {
		return includeHeader;
	}

	public String getSeparator() {
		return separator;
	}

	public boolean isOnlyDisplayedColumns() {
		return onlyDisplayedColumns;
	}

	public void setOnlyDisplayedColumns(boolean includeOnlyDisplayedColumns) {
		this.onlyDisplayedColumns = includeOnlyDisplayedColumns;
	}

	@Override
	protected String getPageHelpContextId() {
		return null;
	}

	public String[] getExtensions() {
		return extensions;
	}

	public boolean isOnlyDisplayedFilteredData() {
		return onlyDisplayedFilteredData;
	}

	public void setOnlyDisplayedFilteredData(boolean onlyDisplayedFilteredData) {
		this.onlyDisplayedFilteredData = onlyDisplayedFilteredData;
	}

	protected void initWidgetValues() {
		settings.put(STORE_HEADER, includeHeader);
		settings.put(STORE_DISPLAYED, onlyDisplayedColumns);
		settings.put(STORE_FILTERED, onlyDisplayedFilteredData);
		settings.put(STORE_SEPARATOR, separator);
		settings.put(STORE_FILEPATH, filePath);
	}

	protected void loadWidgetValues(IDialogSettings dialogSettings) {
		includeHeader = dialogSettings.getBoolean(STORE_HEADER);
		onlyDisplayedColumns = dialogSettings.getBoolean(STORE_DISPLAYED);
		onlyDisplayedFilteredData = dialogSettings.getBoolean(STORE_FILTERED);
		separator = dialogSettings.get(STORE_SEPARATOR);
		filePath = dialogSettings.get(STORE_FILEPATH);

		includeHeaderButton.setSelection(dialogSettings.getBoolean(STORE_HEADER));
		onlyDisplayedColumnsButton.setSelection(dialogSettings.getBoolean(STORE_DISPLAYED));
		onlyDisplayedFilteredDataButton.setSelection(dialogSettings.getBoolean(STORE_FILTERED));
		separatorText.setText(dialogSettings.get(STORE_SEPARATOR));
		text.setText(dialogSettings.get(STORE_FILEPATH));
	}

	@Override
	public boolean isPageComplete() {
		return text != null && !text.getText().equalsIgnoreCase(StringTools.EMPTY);
	}

	public void saveWidgetValues(IDialogSettings dialogSettings) {
		dialogSettings.put(STORE_HEADER, includeHeaderButton.getSelection());
		dialogSettings.put(STORE_DISPLAYED, onlyDisplayedColumnsButton.getSelection());
		dialogSettings.put(STORE_FILTERED, onlyDisplayedFilteredDataButton.getSelection());
		dialogSettings.put(STORE_SEPARATOR, separatorText.getText());
		dialogSettings.put(STORE_FILEPATH, text.getText());
	}

	public IDialogSettings getSettings() {
		return settings;
	}
}
