/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.ui.labelproviders;

import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.ColumnedDefaultTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.messages.MessageIconHelper;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

public class MessageLabelProvider extends ColumnedDefaultTreeLabelProvider {

	public MessageLabelProvider(final AbstractColumnedViewer viewer) {
		super(viewer);
	}

	@Override
	protected Image getActualImage(final Object element, final int actualColumnIndex) {
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