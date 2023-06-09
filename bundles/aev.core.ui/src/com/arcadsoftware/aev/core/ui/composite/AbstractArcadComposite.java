/*
 * Created on 21 f�vr. 2007
 *
 */
package com.arcadsoftware.aev.core.ui.composite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author MD
 */
public class AbstractArcadComposite extends Composite {
	public static Shell getCurrentShell() {
		return EvolutionCoreUIPlugin.getShell();
	}

	public static void showError(final String message) {
		MessageDialog.openError(getCurrentShell(),
				CoreUILabels.resString("msg.commonTitle"), //$NON-NLS-1$
				message);
	}

	protected String errorMessage = null;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractArcadComposite(final Composite parent, final int style) {
		super(parent, style);
	}

	public void dataDateToWidgetDate(final Text widget, String data) {
		final String format = (String) widget.getData();
		final SimpleDateFormat f = new SimpleDateFormat(format);
		if (data == null || data.equals("")) { //$NON-NLS-1$
			data = f.format(new Date());
		} else {
			final SimpleDateFormat nativeFormat = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
			try {
				final Date date = nativeFormat.parse(data);
				data = f.format(date);
			} catch (final ParseException e) {
				data = f.format(new Date());
			}
		}
		widget.setText(data);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	protected boolean setMessage() {
		if (errorMessage == null) {
			return true;
		} else {
			return false;
		}
	}
}
