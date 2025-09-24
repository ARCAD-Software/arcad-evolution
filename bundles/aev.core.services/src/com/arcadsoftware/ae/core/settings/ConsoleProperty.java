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
package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class ConsoleProperty extends ConsoleField {

	public static final String TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$
	public static final String TYPE_DATE = "date"; //$NON-NLS-1$
	public static final String TYPE_ENUM = "enum"; //$NON-NLS-1$
	// New types [02/2012]:
	public static final String TYPE_FLOAT = "float"; //$NON-NLS-1$
	public static final String TYPE_INTEGER = "number"; //$NON-NLS-1$
	public static final String TYPE_PASSWORD = "password"; //$NON-NLS-1$
	public static final String TYPE_PATH = "path"; //$NON-NLS-1$
	public static final String TYPE_POSITIVEINTEGER = "positive"; //$NON-NLS-1$
	public static final String TYPE_STRING = "string"; //$NON-NLS-1$
	// New Types [04/2012]:
	public static final String TYPE_TEXT = "text"; //$NON-NLS-1$
	public static final String TYPE_URL = "url"; //$NON-NLS-1$

	private String defaultvalue;
	private boolean hidden;
	private String id;
	private ArrayList<String> list;
	private String password;
	private boolean readonly;
	private String type;

	private String value;

	public ConsoleProperty() {
		super();
	}

	public ConsoleProperty(final String id, final String label) {
		this();
		this.id = id;
		setDefaultvalue(""); //$NON-NLS-1$
		setLabel(label);
	}

	public ConsoleProperty(final String id, final String label, final int icon, final String defaultvalue,
			final String password, final ArrayList<String> list, final boolean readonly,
			final boolean hidden, final String help) {
		this(id, label, defaultvalue, readonly);
		this.password = password;
		this.list = list;
		this.hidden = hidden;
		if (password != null) {
			type = TYPE_PASSWORD;
		} else if (list != null && list.size() > 0) {
			type = TYPE_ENUM;
		}
		setIcon(icon);
		setHelp(help);
	}

	public ConsoleProperty(final String id, final String label, final String defaultValue) {
		this();
		this.id = id;
		setDefaultvalue(defaultValue);
		setLabel(label);
	}

	public ConsoleProperty(final String id, final String label, final String defaultvalue, final boolean readonly) {
		this();
		this.id = id;
		setDefaultvalue(defaultvalue);
		this.readonly = readonly;
		setLabel(label);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleProperty(id, getLabel(), getIcon(), defaultvalue, password, list, readonly, hidden,
				getHelp());
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return the list
	 */
	public ArrayList<String> getList() {
		return list;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public boolean isBoolean() {
		return TYPE_BOOLEAN.equalsIgnoreCase(type);
	}

	public boolean isDate() {
		return TYPE_DATE.equalsIgnoreCase(type);
	}

	public boolean isEnumeration() {
		return TYPE_ENUM.equalsIgnoreCase(type);
	}

	public boolean isFloat() {
		return TYPE_FLOAT.equalsIgnoreCase(type);
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	public boolean isInteger() {
		return TYPE_INTEGER.equalsIgnoreCase(type);
	}

	/**
	 * @return true is this property field if a password String.
	 */
	public boolean isPassword() {
		return "true".equalsIgnoreCase(password) || TYPE_PASSWORD.equalsIgnoreCase(type); //$NON-NLS-1$
	}

	public boolean isPath() {
		return TYPE_PATH.equalsIgnoreCase(type);
	}

	public boolean isPositiveInteger() {
		return TYPE_POSITIVEINTEGER.equalsIgnoreCase(type);
	}

	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	public boolean isString() {
		return TYPE_STRING.equalsIgnoreCase(type);
	}

	public boolean isURL() {
		return TYPE_URL.equalsIgnoreCase(type);
	}

	public void setDefaultvalue(final String defaultvalue) {
		this.defaultvalue = defaultvalue;
		if (type == null) {
			// Guess Type from default Value.
			if (defaultvalue == null || defaultvalue.length() == 0) {
				type = TYPE_STRING;
			} else if ("true".equalsIgnoreCase(defaultvalue) || "false".equalsIgnoreCase(defaultvalue)) { //$NON-NLS-1$ //$NON-NLS-2$
				type = TYPE_BOOLEAN;
			} else if (ISODateFormater.mayIsoDate(defaultvalue)) {
				type = TYPE_DATE;
			} else {
				try {
					Integer.parseInt(defaultvalue);
					type = TYPE_INTEGER;
				} catch (final NumberFormatException e) {
					type = TYPE_STRING;
				}
			}
		}
	}

	/**
	 * @param hidden
	 *            the hidden to set
	 */
	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(ArrayList<String> list) {
		if (list == null) {
			list = new ArrayList<>();
		}
		this.list = list;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @param readonly
	 *            the readonly to set
	 */
	public void setReadonly(final boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}
