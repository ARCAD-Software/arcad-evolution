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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.calendar.SWTCalendarDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class CalendarEdit extends ButtonEdit {

	private final Button clearButton;
	private final SWTCalendarDialog dialog;
	private SimpleDateFormat formatter;

	/**
	 * @param parent
	 * @param editable
	 * @param password
	 */
	public CalendarEdit(final Composite parent) {
		super(parent, false, false);
		setLayout(new GridLayout(4, false));
		dialog = new SWTCalendarDialog(getShell());
		setDateFormatPattern(CoreUILabels.resString("CalendarEdit.Format_Date")); //$NON-NLS-1$
		getButton().setImage(Icon.CALENDAR_SELECT_DAY.image());
		clearButton = new Button(this, SWT.PUSH);
		clearButton.setImage(Icon.CLEANUP.image());
		clearButton.setToolTipText(CoreUILabels.resString("CalendarEdit.ClearButtton.Tooltips")); //$NON-NLS-1$
		clearButton.setLayoutData(new GridData());
		clearButton.addListener(SWT.Selection, event -> {
			setDate(new Date());
			setText(StringTools.EMPTY);
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.widgets.ButtonEdit#getButtonLabel()
	 */
	@Override
	protected String getButtonLabel() {
		return StringTools.EMPTY;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getCalendar() {
		return dialog.getCalendar();
	}

	/**
	 * @return Buuton
	 */
	public Button getClearButton() {
		return clearButton;
	}

	public Date getDate() {
		if (StringUtils.isEmpty(getText())) {
			return null;
		}
		try {
			return formatter.parse(getText());
		} catch (final ParseException e) {
			MessageManager.addException(e, MessageManager.LEVEL_DEVELOPMENT);
			return null;
		}
	}

	/**
	 * @return SimpleDateFormat
	 */
	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.widgets.ButtonEdit#openDialog(java.lang .String)
	 */
	@Override
	protected String openDialog(final String defaultText) {
		if (dialog.open() == 0) {
			return formatter.format(dialog.getCalendar().getTime());
		}
		return null;
	}

	/**
	 * @param calendar
	 */
	public void setCalendar(final Calendar calendar) {
		if (calendar != null) {
			dialog.setCalendar(calendar);
			// FM 2006/00226 FA 2006/00258
			initText(formatter.format(calendar.getTime()));

		}
	}

	public void setDate(final Date date) {
		if (date != null) {
			dialog.getCalendar().setTime(date);
			initText(formatter.format(date));
		}
	}

	public void setDateFormatPattern(final String pattern) {
		formatter = new SimpleDateFormat(pattern);
		resizeText(formatter.format(new Date(0)) + "xxx"); //$NON-NLS-1$
	}

	public void setDateFromString(final String format, final String date) {
		if (date != null) {
			final Date dt = GuiFormatTools.getDateFromString(format, date);
			dialog.getCalendar().setTime(dt);
			initText(formatter.format(dt));
		}
	}

	/**
	 * @param format
	 */
	public void setFormatter(final SimpleDateFormat format) {
		if (format != null) {
			formatter = format;
		}
	}

}
