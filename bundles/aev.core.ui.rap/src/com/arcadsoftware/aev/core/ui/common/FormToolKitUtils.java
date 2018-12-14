package com.arcadsoftware.aev.core.ui.common;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class FormToolKitUtils {

	public static void paintBordersFor(FormToolkit toolkit, Composite composite) {
		//toolkit.paintBordersFor(composite);
		//there is nothing to do when using RAP.
	}
	
	public static void setOrientationRightToLeft(FormToolkit toolkit) {
		// Not supported by RAP !
		//toolkit.setOrientation(SWT.RIGHT_TO_LEFT);
	}
}
