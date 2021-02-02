/*
 * Créé le 17 mai 2004
 * Projet : org.vafada.swtcalendar
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.calendar;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class SWTTimeChooser extends Composite {

	private final Spinner hourChooser;
	private final Spinner minutChooser;
	private final Spinner secondChooser;

	/**
	 * @param parent
	 * @param style
	 */
	public SWTTimeChooser(final Composite parent, final String caption) {
		super(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		if (caption == null || caption.equals("")) { //$NON-NLS-1$
			layout.numColumns = 3;
		} else {
			layout.numColumns = 4;
		}
		setLayout(layout);
		final GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		setLayoutData(data);
		if (!"".equals(caption)) { //$NON-NLS-1$
			final Label label = new Label(this, SWT.LEFT);
			label.setText(caption);
			label.setLayoutData(new GridData());
		}
		hourChooser = new Spinner(this, SWT.NONE);
		hourChooser.setMaximum(23);
		hourChooser.setRoll(true);
		minutChooser = new Spinner(this, SWT.NONE);
		minutChooser.setMaximum(59);
		minutChooser.setRoll(true);
		secondChooser = new Spinner(this, SWT.NONE);
		secondChooser.setMaximum(59);
		secondChooser.setRoll(true);
	}

	public Calendar getTime(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, hourChooser.getSelection());
		calendar.set(Calendar.MINUTE, minutChooser.getSelection());
		calendar.set(Calendar.SECOND, secondChooser.getSelection());
		return calendar;
	}

	public void setTime(final Calendar calendar) {
		hourChooser.setSelection(calendar.get(Calendar.HOUR_OF_DAY));
		minutChooser.setSelection(calendar.get(Calendar.MINUTE));
		secondChooser.setSelection(calendar.get(Calendar.SECOND));
	}

}
