package com.arcadsoftware.aev.core.ui.actions.columned;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;

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
import com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage;
import com.arcadsoftware.aev.core.ui.wizards.columned.ColumnedCSVExportWizard;
import com.arcadsoftware.aev.core.ui.wizards.columned.ColumnedParametersWizardPage;

/**
 * @author dlelong
 */
public class ColumnedExportAction extends ArcadAction {

	private ColumnedParametersWizardPage parametersPage;
	private AbstractColumnedViewer viewer;

	public ColumnedExportAction(AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
	}

	public WizardPage[] getPages() {
		parametersPage = new ColumnedParametersWizardPage(CoreUILabels.resString("action.columned.wizardPage.title"), //$NON-NLS-1$
				CoreUILabels.resString("action.columned.wizardPage.title"), //$NON-NLS-1$
				ArcadWizardPage.ARCAD_IMDDESCRIPTOR_WIZARD);
		return new WizardPage[] { parametersPage };
	}

	public ColumnedCSVExportWizard createWizard() {
		return new ColumnedCSVExportWizard(this, CoreUILabels.resString("action.columned.wizardPage.title")); //$NON-NLS-1$
	}

	public String getWizardTitle() {
		return CoreUILabels.resString("action.columned.wizardPage.title"); //$NON-NLS-1$
	}

	@Override
	public boolean execute() {
		ColumnedCSVExportWizard wizard = createWizard();
		if (wizard != null) {
			ArcadWizardDialog dialog = new ArcadWizardDialog(EvolutionCoreUIPlugin.getShell(), wizard);
			dialog.setPageSize(150, 140);
			dialog.setBlockOnOpen(true);
			dialog.create();
			dialog.open();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean process() {
		try {
			StringBuffer data = new StringBuffer();
			ArrayList elements = new ArrayList();

			Item[] items = viewer instanceof AbstractColumnedTableViewer ? ((AbstractColumnedTableViewer) viewer)
					.getTable().getItems() : ((AbstractColumnedTreeViewer) viewer).getTree().getItems();
			for (int i = 0; i < items.length; i++) {
				elements.add(items[i].getData());
			}

			ArcadColumns columns = viewer.getDisplayedColumns().duplicate();
			ArrayList list = new ArrayList();
			String separator = parametersPage.getSeparator();
			if (separator.equals("\\t")) //$NON-NLS-1$
				separator = "	"; //$NON-NLS-1$
			else if (separator.equals(StringTools.EMPTY)) {
				MessageDialog.openError(EvolutionCoreUIPlugin.getDefault().getPluginShell(), CoreUILabels
						.resString("messageBox.clm.exportSeparatorVideText"), //$NON-NLS-1$
						CoreUILabels.resString("messageBox.clm.exportSeparatorVideMessage")); //$NON-NLS-1$
				return false;
			}

			if (parametersPage.isOnlyDisplayedColumns()) {
				for (int i = 0; i < columns.count(); i++) {
					ArcadColumn column = columns.items(i);
					if (column.getVisible() == ArcadColumn.HIDDEN)
						list.add(column.getIdentifier());
				}
				for (int i = 0; i < list.size(); i++) {
					columns.getList().remove(columns.items((String) list.get(i)));
				}
			}

			if (parametersPage.isIncludeHeader()) {
				// String[] userNameValues = columns.getUserNameValues();
				Iterator iterator = columns.getList().iterator();
				data.append(((ArcadColumn) iterator.next()).getUserName());
				while (iterator.hasNext()) {
					ArcadColumn column = (ArcadColumn) iterator.next();
					data.append(separator);
					data.append(column.getUserName());
				}
				data.append('\n'); //$NON-NLS-1$
			}

			if (viewer.isFiltered() && parametersPage.isOnlyDisplayedFilteredData()) {
				ArrayList filteredElements = new ArrayList();
				for (int i = 0; i < elements.size(); i++) {
					boolean inFilteredData = viewer.getFilter().select(viewer.getViewer(), elements, elements.get(i));
					if (inFilteredData)
						filteredElements.add(elements.get(i));
				}
				elements = filteredElements;
			}

			// Attention le viewer.getInput() ne donne que les éléments de
			// premier niveau pour les arbres...
			if (viewer instanceof AbstractColumnedTreeViewer) {
				ArrayList treeElements = new ArrayList();
				for (int i = 0; i < elements.size(); i++) {
					completeElementListOfRoot(getTreeItem(elements.get(i)), treeElements);
				}
				elements = treeElements;
			}
			// Pour chaque élément, on va récupérer sa valeur textuelle pour la
			// concaténer au buffer final
			for (int i = 0; i < elements.size(); i++) {
				boolean isFirstData = true;
				for (int j = 0; j < columns.count(); j++) {
					if (!isFirstData)
						data.append(separator);
					else
						isFirstData = false;
					String value = viewer.getValue(elements.get(i), columns.items(j).getPosition());
					// replace any line return with space since line return is special items separator
					if (value != null){
						value = value.replaceAll("\r\n", " ");
						value = value.replaceAll("\n", " ");
					}
					data.append(value);
				}
				data.append('\n'); //$NON-NLS-1$
			}

			String filePath = parametersPage.getFilePath();
			if (filePath != null) {
				for (int i = 0; i < parametersPage.getExtensions().length; i++) {
					if (!filePath.endsWith((parametersPage.getExtensions()[i]).substring(1)))
						filePath += (parametersPage.getExtensions()[i]).substring(1);
				}
				File file = new File(filePath);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						// Do nothing
					}
				}
				StringTools.string2File(file, new String(data));
				parametersPage.saveWidgetValues(parametersPage.getSettings());
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private TreeItem getTreeItem(Object element) {
		if (viewer != null) {
			if (viewer.getViewer() instanceof ColumnedTreeViewer)
				return ((ColumnedTreeViewer) viewer.getViewer()).findTreeItem(element);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private ArrayList completeElementListOfRoot(TreeItem treeItem, ArrayList list) {
		Object o = treeItem.getData();
		list.add(o);
		TreeItem[] items = treeItem.getItems();
		for (int i = 0; i < items.length; i++) {
			completeElementListOfRoot(items[i], list);
		}
		return list;
	}
}
