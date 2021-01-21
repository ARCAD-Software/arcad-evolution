/*
 * Created on 28 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.dialogs.PropertyPageContributorManager;
import org.eclipse.ui.internal.dialogs.PropertyPageManager;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadPropertyDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author MD
 */
public class ShowPropertyDialogAction extends ArcadAction {
	protected Shell shell;
	protected IAdaptable element = null;

	public ShowPropertyDialogAction(IAdaptable element) {
		super();
		this.shell = EvolutionCoreUIPlugin.getShell();
		this.element = element;
		setInterface();
	}

	/**
	 * @param shell
	 * @param provider
	 */
	public ShowPropertyDialogAction() {
		this(null);
	}

	@Override
	protected void setInterface() {
		setText(CoreUILabels.resString("action.properties.text")); //$NON-NLS-1$
		setToolTipText(CoreUILabels.resString("action.properties.tooltip")); //$NON-NLS-1$		
		setImageDescriptor(Icon.PROPERTIES.imageDescriptor());
	}

	protected IAdaptable defineElement() {
		return null;
	}

	protected String defineName() {
		return StringTools.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		boolean isnull = false;
		PropertyPageManager pageManager = new PropertyPageManager();
		String title = StringTools.EMPTY;
		if (element == null) {
			element = defineElement();
			isnull = true;
		}
		if (element != null) {
			PropertyPageContributorManager.getManager().contribute(pageManager, element);
			// testing if there are pages in the manager
			Iterator<?> pages = pageManager.getElements(PreferenceManager.PRE_ORDER).iterator();
			String name = defineName();
			if (!pages.hasNext()) {
				MessageDialog.openInformation(shell, CoreUILabels.resString("msg.commonTitle"), //$NON-NLS-1$
						CoreUILabels.resString("msg.noPropertyPage")); //$NON-NLS-1$
				return;
			}
			//
			StringBuffer sb = new StringBuffer(CoreUILabels.resString("msg.propertyPageTitle"));//$NON-NLS-1$ 
			sb.append(name);
			title = sb.toString();

			ArcadPropertyDialog propertyDialog = new ArcadPropertyDialog(shell, pageManager, new StructuredSelection(
					new Object[] { element }));
			propertyDialog.create();
			propertyDialog.doAfterCreation();
			propertyDialog.getShell().setText(title);
			propertyDialog.open();
		}
		if (isnull)
			element = null;
	}
}