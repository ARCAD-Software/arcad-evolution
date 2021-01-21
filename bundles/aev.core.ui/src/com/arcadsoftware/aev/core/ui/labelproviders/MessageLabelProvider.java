/*
 * Copyright 2005 ARCAD-Software.
 * Créé par mlafon
 * Créé le 23 juin 05
 */
package com.arcadsoftware.aev.core.ui.labelproviders;

import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.ColumnedDefaultTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.messages.MessageIconHelper;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

public class MessageLabelProvider extends ColumnedDefaultTreeLabelProvider {

	public MessageLabelProvider(AbstractColumnedViewer viewer) {
		super(viewer);
	}

	@Override
	protected Image getActualImage(Object element, int actualColumnIndex) {
		if (actualColumnIndex == 0) {
			if (element instanceof Message) {
				return MessageIconHelper.getMessageIcon((Message) element).image();
			} else if (element instanceof MessageDetail) {
				return MessageIconHelper.getMessageDetailIcon((MessageDetail) element).image();
			}
		}
		return null;
	}
}