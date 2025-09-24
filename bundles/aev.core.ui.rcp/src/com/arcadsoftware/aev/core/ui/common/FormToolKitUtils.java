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
package com.arcadsoftware.aev.core.ui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class FormToolKitUtils {

	public static void paintBordersFor(final FormToolkit toolkit, final Composite composite) {
		toolkit.paintBordersFor(composite);
	}

	public static void setOrientationRightToLeft(final FormToolkit toolkit) {
		toolkit.setOrientation(SWT.RIGHT_TO_LEFT);
	}

}
