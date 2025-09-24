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
package com.arcadsoftware.aev.core.ui.dialogs;

import com.arcadsoftware.aev.core.osgi.ServiceNotFoundException;
import com.arcadsoftware.aev.core.osgi.ServiceRegistry;

public class DialogConstantProvider {

	private static DialogConstantProvider instance = new DialogConstantProvider();

	public static DialogConstantProvider getInstance() {
		return instance;
	}

	public String ABORT_LABEL;
	public String BACK_LABEL;
	public String CANCEL_LABEL;
	public String CLOSE_LABEL;
	public String FINISH_LABEL;
	public String HELP_LABEL;
	public String HIDE_DETAILS_LABEL;
	public String IGNORE_LABEL;
	public String NEXT_LABEL;
	public String NO_LABEL;
	public String NO_TO_ALL_LABEL;
	public String OK_LABEL;
	public String OPEN_LABEL;
	public String PROCEED_LABEL;
	public String RETRY_LABEL;
	public String SHOW_DETAILS_LABEL;
	public String SKIP_LABEL;
	public String STOP_LABEL;

	public String YES_LABEL;

	public String YES_TO_ALL_LABEL;

	private DialogConstantProvider() {
		ServiceRegistry
				.lookup(IDialogConstantFiller.class)
				.orElseThrow(() -> new ServiceNotFoundException(IDialogConstantFiller.class))
				.fill(this);
	}
}
