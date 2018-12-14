/*
 * Copyright 2005 ARCAD-Software.
 * Créé par mlafon
 * Créé le 23 juin 05
 */
package com.arcadsoftware.aev.core.ui.labelproviders;

import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.ColumnedDefaultTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

public class MessageLabelProvider extends ColumnedDefaultTreeLabelProvider {

	public MessageLabelProvider(AbstractColumnedViewer viewer) {
		super(viewer);
	}

	@Override
	protected Image getActualImage(Object element, int actualColumnIndex) {
		if (actualColumnIndex == 0) {
			if (element instanceof Message) {
				switch (((Message) element).getMaxDetailsType()) {
				case MessageDetail.COMPLETION:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_COMPL);
				case MessageDetail.DIAGNOSTIC:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_DIAG);
				case MessageDetail.ERROR:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_ERROR);
				case MessageDetail.WARNING:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_WARN);
				default/* case MessageDetail.EXCEPTION */:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_EXCEP);
				}
			} else if (element instanceof MessageDetail) {
				switch (((MessageDetail) element).getType()) {
				case MessageDetail.COMPLETION:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_COMPL);
				case MessageDetail.DIAGNOSTIC:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_DIAG);
				case MessageDetail.ERROR:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_ERROR);
				case MessageDetail.WARNING:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_WARN);
				default/* case MessageDetail.EXCEPTION */:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_EXCEP);
				}
			}
		}
		return null;
	}
}