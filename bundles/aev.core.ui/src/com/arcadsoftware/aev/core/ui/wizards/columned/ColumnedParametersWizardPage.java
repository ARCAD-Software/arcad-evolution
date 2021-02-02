package com.arcadsoftware.aev.core.ui.wizards.columned;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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

	private final static String DIALOG_NAME = "ColumnedParametersWizardPage"; //$NON-NLS-1$
	private static final String STORE_DISPLAYED = IDocProvider.ID + ".CLMCVSEXPORT_DISPLAYED";//$NON-NLS-1$
	private static final String STORE_FILEPATH = IDocProvider.ID + ".CLMCVSEXPORT_FILEPATH";//$NON-NLS-1$
	private static final String STORE_FILTERED = IDocProvider.ID + ".CLMCVSEXPORT_FILTERED";//$NON-NLS-1$
	private static final String STORE_HEADER = IDocProvider.ID + ".CLMCVSEXPORT_HEADER";//$NON-NLS-1$
	private static final String STORE_SEPARATOR = IDocProvider.ID + ".CLMCVSEXPORT_SEPARATOR";//$NON-NLS-1$
	private final String[] extensions = new String[] { "*.csv" }; //$NON-NLS-1$

	String filePath = " "; //$NON-NLS-1$
	boolean includeHeader = false;
	Button includeHeaderButton;
	boolean onlyDisplayedColumns = false;
	Button onlyDisplayedColumnsButton;
	boolean onlyDisplayedFilteredData = false;
	Button onlyDisplayedFilteredDataButton;
	IStructuredSelection selection;
	String separator = ","; //$NON-NLS-1$
	Text separatorText;
	private IDialogSettings settings;
	Text text;
	IWorkbench workbench;

	public ColumnedParametersWizardPage(final String pageName) {
		super(pageName);
		setPageComplete(true);
	}

	public ColumnedParametersWizardPage(final String pageName, final String title, final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		settings = EvolutionCoreUIPlugin.getSettings(getDialogName());
		if (settings.get(STORE_SEPARATOR) == null) {
			initWidgetValues();
		}
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite container = GuiFormatTools.createComposite(parent);

		includeHeaderButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.includeHeader"), //$NON-NLS-1$
				SWT.CENTER);
		includeHeaderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				includeHeader = includeHeaderButton.getSelection();
			}
		});

		onlyDisplayedColumnsButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.exportOnlyDisplayedColumns"), //$NON-NLS-1$
				SWT.CENTER);
		onlyDisplayedColumnsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				onlyDisplayedColumns = onlyDisplayedColumnsButton.getSelection();
			}
		});

		onlyDisplayedFilteredDataButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("wizardPage.text.exportOnlyFilteredData"), //$NON-NLS-1$
				SWT.CENTER);
		onlyDisplayedFilteredDataButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				onlyDisplayedFilteredData = onlyDisplayedFilteredDataButton
						.getSelection();
			}
		});

		separatorText = GuiFormatTools.createLabelledText(container, CoreUILabels
				.resString("wizardPage.label.separator")); //$NON-NLS-1$
		separatorText.setText(separator);
		separatorText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				separator = separatorText.getText();
			}
		});

		text = GuiFormatTools.createLabelledTextWithFileSelector(container, CoreUILabels
				.resString("wizardPage.text.labelFileSelector"), //$NON-NLS-1$
				false, CoreUILabels.resString("wizardPage.text.titleFileSelector"), //$NON-NLS-1$
				extensions);
		text.addModifyListener(e -> {
			filePath = text.getText();
			setPageComplete(isPageComplete());
		});
		setControl(parent);
		if (settings != null) {
			loadWidgetValues(settings);
		}
		setPageComplete(isPageComplete());
	}

	protected String getDialogName() {
		return DIALOG_NAME;
	}

	public String[] getExtensions() {
		return extensions;
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	protected String getPageHelpContextId() {
		return null;
	}

	public String getSeparator() {
		return separator;
	}

	public IDialogSettings getSettings() {
		return settings;
	}

	protected void initWidgetValues() {
		settings.put(STORE_HEADER, includeHeader);
		settings.put(STORE_DISPLAYED, onlyDisplayedColumns);
		settings.put(STORE_FILTERED, onlyDisplayedFilteredData);
		settings.put(STORE_SEPARATOR, separator);
		settings.put(STORE_FILEPATH, filePath);
	}

	public boolean isIncludeHeader() {
		return includeHeader;
	}

	public boolean isOnlyDisplayedColumns() {
		return onlyDisplayedColumns;
	}

	public boolean isOnlyDisplayedFilteredData() {
		return onlyDisplayedFilteredData;
	}

	@Override
	public boolean isPageComplete() {
		return text != null && !text.getText().equalsIgnoreCase(StringTools.EMPTY);
	}

	protected void loadWidgetValues(final IDialogSettings dialogSettings) {
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

	public void saveWidgetValues(final IDialogSettings dialogSettings) {
		dialogSettings.put(STORE_HEADER, includeHeaderButton.getSelection());
		dialogSettings.put(STORE_DISPLAYED, onlyDisplayedColumnsButton.getSelection());
		dialogSettings.put(STORE_FILTERED, onlyDisplayedFilteredDataButton.getSelection());
		dialogSettings.put(STORE_SEPARATOR, separatorText.getText());
		dialogSettings.put(STORE_FILEPATH, text.getText());
	}

	public void setOnlyDisplayedColumns(final boolean includeOnlyDisplayedColumns) {
		onlyDisplayedColumns = includeOnlyDisplayedColumns;
	}

	public void setOnlyDisplayedFilteredData(final boolean onlyDisplayedFilteredData) {
		this.onlyDisplayedFilteredData = onlyDisplayedFilteredData;
	}
}
