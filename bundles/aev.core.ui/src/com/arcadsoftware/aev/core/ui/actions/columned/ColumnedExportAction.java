package com.arcadsoftware.aev.core.ui.actions.columned;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;

import com.arcadsoftware.aev.core.osgi.GlobalLogService;
import com.arcadsoftware.aev.core.osgi.ServiceRegistry;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.actions.ArcadAction;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTreeViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTreeViewer;
import com.arcadsoftware.aev.core.ui.wizards.ArcadWizardDialog;
import com.arcadsoftware.aev.core.ui.wizards.IWizardBranding;
import com.arcadsoftware.aev.core.ui.wizards.columned.ColumnedCSVExportWizard;
import com.arcadsoftware.aev.core.ui.wizards.columned.ColumnedParametersWizardPage;
import com.arcadsoftware.documentation.brands.Brand;

/**
 * @author dlelong
 */
public class ColumnedExportAction extends ArcadAction {

	private static final String WIZARD_PAGE_TITLE = "action.columned.wizardPage.title";
	private ColumnedParametersWizardPage parametersPage;
	private final AbstractColumnedViewer viewer;

	public ColumnedExportAction(final AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
	}

	private ArrayList<Object> completeElementListOfRoot(final TreeItem treeItem, final ArrayList<Object> list) {
		final Object o = treeItem.getData();
		list.add(o);
		final TreeItem[] items = treeItem.getItems();
		for (final TreeItem item : items) {
			completeElementListOfRoot(item, list);
		}
		return list;
	}

	public ColumnedCSVExportWizard createWizard() {
		return new ColumnedCSVExportWizard(this, CoreUILabels.resString(WIZARD_PAGE_TITLE)); //$NON-NLS-1$
	}

	@Override
	public boolean execute() {
		final ColumnedCSVExportWizard wizard = createWizard();
		if (wizard != null) {
			final ArcadWizardDialog dialog = new ArcadWizardDialog(EvolutionCoreUIPlugin.getShell(), wizard);
			dialog.setPageSize(150, 140);
			dialog.setBlockOnOpen(true);
			dialog.create();
			dialog.open();
			return true;
		}
		return false;
	}

	public WizardPage[] getPages() {
		parametersPage = new ColumnedParametersWizardPage(CoreUILabels.resString(WIZARD_PAGE_TITLE), //$NON-NLS-1$
				CoreUILabels.resString(WIZARD_PAGE_TITLE), //$NON-NLS-1$
				getWizardImage());
		return new WizardPage[] { parametersPage };
	}

	private TreeItem getTreeItem(final Object element) {
		if (viewer != null && viewer.getViewer() instanceof ColumnedTreeViewer) {
			return ((ColumnedTreeViewer) viewer.getViewer()).findTreeItem(element);			
		}
		return null;
	}

	private ImageDescriptor getWizardImage() {
		return ServiceRegistry.lookup(IWizardBranding.class) //
				.map(IWizardBranding::getBrandingImage) //
				.orElseGet(Brand.ARCAD_LOGO_64::imageDescriptor);
	}

	public String getWizardTitle() {
		return CoreUILabels.resString(WIZARD_PAGE_TITLE); //$NON-NLS-1$
	}

	public boolean process() {
		try {
			final StringBuilder data = new StringBuilder();
			ArrayList<Object> elements = new ArrayList<>();

			final Item[] items = viewer instanceof AbstractColumnedTableViewer ? ((AbstractColumnedTableViewer) viewer)
					.getTable().getItems() : ((AbstractColumnedTreeViewer) viewer).getTree().getItems();
			for (final Item item : items) {
				elements.add(item.getData());
			}

			final ArcadColumns columns = viewer.getDisplayedColumns().duplicate();
			final ArrayList<String> list = new ArrayList<>();
			String separator = parametersPage.getSeparator();
			if (separator.equals("\\t")) {
				separator = "	"; //$NON-NLS-1$
			} else if (separator.equals(StringTools.EMPTY)) {
				MessageDialog.openError(EvolutionCoreUIPlugin.getDefault().getPluginShell(), CoreUILabels
						.resString("messageBox.clm.exportSeparatorVideText"), //$NON-NLS-1$
						CoreUILabels.resString("messageBox.clm.exportSeparatorVideMessage")); //$NON-NLS-1$
				return false;
			}

			if (parametersPage.isOnlyDisplayedColumns()) {
				for (int i = 0; i < columns.count(); i++) {
					final ArcadColumn column = columns.items(i);
					if (column.getVisible() == ArcadColumn.HIDDEN) {
						list.add(column.getIdentifier());
					}
				}
				for (final String element : list) {
					columns.getList().remove(columns.items(element));
				}
			}

			if (parametersPage.isIncludeHeader()) {
				final Iterator<?> iterator = columns.getList().iterator();
				data.append(((ArcadColumn) iterator.next()).getUserName());
				while (iterator.hasNext()) {
					final ArcadColumn column = (ArcadColumn) iterator.next();
					data.append(separator);
					data.append(column.getUserName());
				}
				data.append('\n'); // $NON-NLS-1$
			}

			if (viewer.isFiltered() && parametersPage.isOnlyDisplayedFilteredData()) {
				final ArrayList<Object> filteredElements = new ArrayList<>();
				for (int i = 0; i < elements.size(); i++) {
					final boolean inFilteredData = viewer.getFilter().select(viewer.getViewer(), elements,
							elements.get(i));
					if (inFilteredData) {
						filteredElements.add(elements.get(i));
					}
				}
				elements = filteredElements;
			}

			// Attention le viewer.getInput() ne donne que les éléments de
			// premier niveau pour les arbres...
			if (viewer instanceof AbstractColumnedTreeViewer) {
				final ArrayList<Object> treeElements = new ArrayList<>();
				for (final Object element : elements) {
					completeElementListOfRoot(getTreeItem(element), treeElements);
				}
				elements = treeElements;
			}
			// Pour chaque élément, on va récupérer sa valeur textuelle pour la
			// concaténer au buffer final
			for (final Object element : elements) {
				boolean isFirstData = true;
				for (int j = 0; j < columns.count(); j++) {
					if (!isFirstData) {
						data.append(separator);
					} else {
						isFirstData = false;
					}
					String value = viewer.getValue(element, columns.items(j).getPosition());
					// replace any line return with space since line return is special items separator
					if (value != null) {
						value = value.replaceAll("\r\n", " ");
						value = value.replaceAll("\n", " ");
					}
					data.append(value);
				}
				data.append('\n'); // $NON-NLS-1$
			}

			String filePath = parametersPage.getFilePath();
			if (filePath != null) {
				for (int i = 0; i < parametersPage.getExtensions().length; i++) {
					if (!filePath.endsWith(parametersPage.getExtensions()[i].substring(1))) {
						filePath += parametersPage.getExtensions()[i].substring(1);
					}
				}
				boolean ok = true;
				final File file = new File(filePath);
				if (!file.exists()) {					
					ok = file.createNewFile();					
				}
				if (ok) {
					StringTools.string2File(file, new String(data));
					parametersPage.saveWidgetValues(parametersPage.getSettings());
				} 
				return ok;
			}
			return false;
		} catch (final Exception e) {
			ServiceRegistry.lookupOrDie(GlobalLogService.class).debug(e);
			return false;
		}
	}
}
