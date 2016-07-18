package com.arcadsoftware.aev.core.ui.rap.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;

import com.arcadsoftware.aev.core.ui.dialogs.DialogConstantProvider;
import com.arcadsoftware.aev.core.ui.dialogs.IDialogConstantFiller;

public class RAPDialogConstantFiller implements IDialogConstantFiller {

	public void fill(DialogConstantProvider provider) {
		provider.OK_LABEL = IDialogConstants.get().OK_LABEL;
		provider.CANCEL_LABEL = IDialogConstants.get().CANCEL_LABEL;
		provider.YES_LABEL = IDialogConstants.get().YES_LABEL;
		provider.NO_LABEL = IDialogConstants.get().NO_LABEL;
		provider.NO_TO_ALL_LABEL = IDialogConstants.get().NO_TO_ALL_LABEL;
		provider.YES_TO_ALL_LABEL = IDialogConstants.get().YES_TO_ALL_LABEL;
		provider.SKIP_LABEL = IDialogConstants.get().SKIP_LABEL;
		provider.STOP_LABEL = IDialogConstants.get().STOP_LABEL;
		provider.ABORT_LABEL = IDialogConstants.get().ABORT_LABEL;
		provider.RETRY_LABEL = IDialogConstants.get().RETRY_LABEL;
		
		provider.IGNORE_LABEL = IDialogConstants.get().IGNORE_LABEL;
		provider.PROCEED_LABEL = IDialogConstants.get().PROCEED_LABEL;
		provider.OPEN_LABEL = IDialogConstants.get().OPEN_LABEL;
		provider.CLOSE_LABEL = IDialogConstants.get().CLOSE_LABEL;
		provider.SHOW_DETAILS_LABEL = IDialogConstants.get().SHOW_DETAILS_LABEL;
		provider.HIDE_DETAILS_LABEL = IDialogConstants.get().HIDE_DETAILS_LABEL;
		
		provider.BACK_LABEL = IDialogConstants.get().BACK_LABEL;
		provider.NEXT_LABEL = IDialogConstants.get().NEXT_LABEL;
		provider.FINISH_LABEL = IDialogConstants.get().FINISH_LABEL;
		provider.HELP_LABEL = IDialogConstants.get().HELP_LABEL;

	}

}
