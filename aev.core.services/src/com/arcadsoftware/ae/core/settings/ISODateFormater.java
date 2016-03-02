/**
 * Copyright (c) Arcad-Software (2008). All Rights Reserved.
 * 
 * Creation date: 10 September 2008
 * @author Marc Lafon
 * 
 */
package com.arcadsoftware.ae.core.settings;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



/**
 * A helper static class to convert a Date to and from String in the ISO format :
 * 
 * <code>yyyy-mm-ddThh:mn:ss</code>
 * 
 * 
 */
public final class ISODateFormater {

	public static final Date EPOCH = new Date(0);
	
	/**
	 * Test the string without parsing it to state if it may represent an ISO formated date. 
	 * 
	 * If this method return <code>false</code> then the sting is NOT an ISO Date.
	 * If it return <code>true</code> then the string may stil NOT represent an ISO Date.
	 * 
	 * @param string an ISO date string formated.
	 * @return true if the string may be a ISO Date.
	 */
	static public boolean mayIsoDate(String string) {
		return  (string != null) &&
				(string.length() == 19) &&
				(string.charAt(4) == '-') &&
				(string.charAt(10) == 'T') &&
				(string.charAt(16) == ':');
	}

	/**
	 * Convert a string representing an ISO Date format to a Calendar Object
	 * 
	 * @param string
	 * @return a GregorianCalendar representing the date in the GMT timezone.
	 * @throws ParseException If the string can not be parsed as an ISO Date Format.
	 */
	static public Calendar toCalendar(String string) throws ParseException {
		if (string == null) {
			return null;
		}
		if (string.length() != 19) {
			throw new ParseException("InvalidLength",string.length()); //$NON-NLS-1$
		}
		Calendar result = new GregorianCalendar(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		result.set(Calendar.MILLISECOND, 0);
		int i = 0;
		try {
			result.set(Calendar.YEAR, Integer.parseInt(string.substring(0,4)));
			i = 5;
			result.set(Calendar.MONTH, Integer.parseInt(string.substring(5,7)) - 1);
			i = 8;
			result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(string.substring(8,10)));
			i = 11;
			result.set(Calendar.HOUR_OF_DAY, Integer.parseInt(string.substring(11,13)));
			i = 14;
			result.set(Calendar.MINUTE, Integer.parseInt(string.substring(14,16)));
			i = 17;
			result.set(Calendar.SECOND, Integer.parseInt(string.substring(17,19)));
			//result.set(Calendar.ZONE_OFFSET, 0);
		} catch (NumberFormatException e) {
			throw new ParseException(e.getMessage(), i);
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException(e.getMessage(), i);
		}
		return result;
	}
	

	/**
	 * Convert a string representing an ISO Date format to a Date Object
	 * 
	 * @param string
	 * @return a Date.
	 * @throws ParseException If the string can not be parsed as an ISO Date Format.
	 */
	static public Date toDate(String string) throws ParseException {
		if (string == null) {
			return null;
		}
		Calendar cal = toCalendar(string);
		if (cal == null) {
			return null;
		}
		return cal.getTime();
	}

	/**
	 * Convert the Date to a GMT, ISO date formated string.
	 * 
	 * @param date
	 * @return
	 */
	static public String toString(Date date) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		cal.setTime(date);
		return toString(cal);
	}
	
	/**
	 * Convert the Calendar to a GMT, ISO date formated string.
	 *  
	 * @param calendar
	 * @return
	 */
	static public String toString(Calendar calendar) {
		calendar.set(Calendar.ZONE_OFFSET, 0);
		return String.format("%04d-%02d-%02dT%02d:%02d:%02d", //$NON-NLS-1$
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
	}
	
}