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
