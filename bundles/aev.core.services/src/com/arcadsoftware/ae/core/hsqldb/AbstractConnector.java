package com.arcadsoftware.ae.core.hsqldb;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractConnector {

	protected HSQLDBConnector dbconnector;
	protected SimpleDateFormat df = null;

	public AbstractConnector() {
		super();
		dbconnector = createDbConnector();
		df = new SimpleDateFormat(dbconnector.getDbConfig().getDbFilterDateFormat());
	}

	public void close() {
		dbconnector.close();
	}

	protected String convertDate(final Date d) {
		return convertDate(d, df);
	}

	protected String convertDate(final Date d, final SimpleDateFormat format) {
		return d == null ? "null" : format.format(d);
	}

	public abstract HSQLDBConnector createDbConnector();

	protected String nullString(final String s) {
		return s == null ? "" : s;
	}
}
