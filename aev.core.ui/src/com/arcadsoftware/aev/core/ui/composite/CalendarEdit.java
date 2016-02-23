/*
 * Créé le 14 mai 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.ui.composite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.calendar.SWTCalendarDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author mlafon
 * @version 1.0.0
 * 
 * 
 */
public class CalendarEdit extends ButtonEdit {

	private SWTCalendarDialog dialog;
	private SimpleDateFormat formatter;
	private Button clearButton;

	/**
	 * @param parent
	 * @param editable
	 * @param password
	 */
	public CalendarEdit(Composite parent) {
		super(parent, false, false);
		setLayout(new GridLayout(4, false));
		dialog = new SWTCalendarDialog(getShell());
		setDateFormatPattern(CoreUILabels.resString("CalendarEdit.Format_Date")); //$NON-NLS-1$
		getButton().setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.CALENDAR_ICON));
		clearButton = new Button(this, SWT.PUSH);
		clearButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ACT_ERASE));
		clearButton.setToolTipText(CoreUILabels.resString("CalendarEdit.ClearButtton.Tooltips")); //$NON-NLS-1$
		clearButton.setLayoutData(new GridData());
		clearButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				setDate(new Date());
				setText(StringTools.EMPTY);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.widgets.ButtonEdit#openDialog(java.lang
	 * .String)
	 */
	@Override
	protected String openDialog(String defaultText) {
		if (dialog.open() == 0)
			return formatter.format(dialog.getCalendar().getTime());
		return null;
	}

	/**
	 * @return Buuton
	 */
	public Button getClearButton() {
		return clearButton;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getCalendar() {
		return dialog.getCalendar();
	}

	/**
	 * @return SimpleDateFormat
	 */
	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public void setDateFormatPattern(String pattern) {
		formatter = new SimpleDateFormat(pattern);
		resizeText(formatter.format(new Date(0)) + "xxx"); //$NON-NLS-1$
		// FM 2006/00226 FA 2006/00258
		// initText(formatter.format(dialog.getCalendar().getTime()));
	}

	/**
	 * @param calendar
	 */
	public void setCalendar(Calendar calendar) {
		if (calendar != null) {
			dialog.setCalendar(calendar);
			// FM 2006/00226 FA 2006/00258
			initText(formatter.format(calendar.getTime()));

		}
	}

	public Date getDate() {
		if (getText() == StringTools.EMPTY)
			return null;
		try {
			return formatter.parse(getText());
		} catch (ParseException e) {
			MessageManager.addException(e, MessageManager.LEVEL_DEVELOPMENT);
			return null;
		}
	}

	public void setDate(Date date) {
		if (date != null) {
			dialog.getCalendar().setTime(date);
			initText(formatter.format(date));
		}
	}

	public void setDateFromString(String format, String date) {
		if (date != null) {
			Date dt = GuiFormatTools.getDateFromString(format, date);
			dialog.getCalendar().setTime(dt);
			initText(formatter.format(dt));
		}
	}

	/**
	 * @param format
	 */
	public void setFormatter(SimpleDateFormat format) {
		if (format != null) {
			formatter = format;
			// FM 2006/00226 FA 2006/00258
			// initText(formatter.format(dialog.getCalendar().getTime()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.ui.widgets.ButtonEdit#getButtonLabel()
	 */
	@Override
	protected String getButtonLabel() {
		return StringTools.EMPTY;
	}

}
