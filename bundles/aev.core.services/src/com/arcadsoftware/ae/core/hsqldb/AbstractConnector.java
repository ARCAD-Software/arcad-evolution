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
