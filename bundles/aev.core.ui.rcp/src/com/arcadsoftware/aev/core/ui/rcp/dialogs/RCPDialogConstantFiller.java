package com.arcadsoftware.aev.core.ui.rcp.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.osgi.service.component.annotations.Component;

import com.arcadsoftware.aev.core.ui.dialogs.DialogConstantProvider;
import com.arcadsoftware.aev.core.ui.dialogs.IDialogConstantFiller;

@Component( service = IDialogConstantFiller.class, immediate = true )
public class RCPDialogConstantFiller implements IDialogConstantFiller {
	public void fill(DialogConstantProvider provider) {
		provider.OK_LABEL = IDialogConstants.OK_LABEL;
		provider.CANCEL_LABEL = IDialogConstants.CANCEL_LABEL;
		provider.YES_LABEL = IDialogConstants.YES_LABEL;
		provider.NO_LABEL = IDialogConstants.NO_LABEL;
		provider.NO_TO_ALL_LABEL = IDialogConstants.NO_TO_ALL_LABEL;
		provider.YES_TO_ALL_LABEL = IDialogConstants.YES_TO_ALL_LABEL;
		provider.SKIP_LABEL = IDialogConstants.SKIP_LABEL;
		provider.STOP_LABEL = IDialogConstants.STOP_LABEL;
		provider.ABORT_LABEL = IDialogConstants.ABORT_LABEL;
		provider.RETRY_LABEL = IDialogConstants.RETRY_LABEL;
		
		provider.IGNORE_LABEL = IDialogConstants.IGNORE_LABEL;
		provider.PROCEED_LABEL = IDialogConstants.PROCEED_LABEL;
		provider.OPEN_LABEL = IDialogConstants.OPEN_LABEL;
		provider.CLOSE_LABEL = IDialogConstants.CLOSE_LABEL;
		provider.SHOW_DETAILS_LABEL = IDialogConstants.SHOW_DETAILS_LABEL;
		provider.HIDE_DETAILS_LABEL = IDialogConstants.HIDE_DETAILS_LABEL;
		
		provider.BACK_LABEL = IDialogConstants.BACK_LABEL;
		provider.NEXT_LABEL = IDialogConstants.NEXT_LABEL;
		provider.FINISH_LABEL = IDialogConstants.FINISH_LABEL;
		provider.HELP_LABEL = IDialogConstants.HELP_LABEL;

	}

}
