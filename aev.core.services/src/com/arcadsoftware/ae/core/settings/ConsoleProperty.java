package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;



public class ConsoleProperty extends ConsoleField {

	public static final String TYPE_STRING = "string"; //$NON-NLS-1$
	public static final String TYPE_INTEGER = "number"; //$NON-NLS-1$
	public static final String TYPE_ENUM = "enum"; //$NON-NLS-1$
	public static final String TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$
	public static final String TYPE_PASSWORD = "password"; //$NON-NLS-1$
	// New types [02/2012]:
	public static final String TYPE_FLOAT = "float"; //$NON-NLS-1$
	public static final String TYPE_DATE = "date"; //$NON-NLS-1$
	public static final String TYPE_POSITIVEINTEGER = "positive"; //$NON-NLS-1$
	public static final String TYPE_PATH = "path"; //$NON-NLS-1$
	public static final String TYPE_URL = "url"; //$NON-NLS-1$
	// New Types [04/2012]:
	public static final String TYPE_TEXT = "text"; //$NON-NLS-1$
	
	private String id;
	private String defaultvalue;
	private String password;
	private ArrayList<String> list;
	private boolean readonly;
	private boolean hidden;
	private String type;

	
	private String value;
	
	public ConsoleProperty() {
		super();
	}
	
	public ConsoleProperty(String id, String label, String defaultvalue, boolean readonly) {
		this();
		this.id = id;
		setDefaultvalue(defaultvalue);
		this.readonly = readonly;
		setLabel(label);
	}
	
	public ConsoleProperty(String id, String label, int icon, String defaultvalue, String password, ArrayList<String> list, boolean readonly,
			boolean hidden, String help) {
		this(id, label, defaultvalue, readonly);
		this.password = password;
		this.list = list;
		this.hidden = hidden;
		if (password != null) {
			this.type = TYPE_PASSWORD;
		} else if ((list != null) && (list.size() > 0)) {
			this.type = TYPE_ENUM;
		}
		setIcon(icon);
		setHelp(help);
	}

	public ConsoleProperty(String id, String label, String defaultValue) {
		this();
		this.id = id;
		setDefaultvalue(defaultValue);
		setLabel(label);
	}

	public ConsoleProperty(String id, String label) {
		this();
		this.id = id;
		setDefaultvalue(""); //$NON-NLS-1$
		setLabel(label);
	}

	public String getId() {
		return id;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
		if (type == null) {
			// Guess Type from default Value.
			if ((defaultvalue == null) || (defaultvalue.length() == 0)) {
				type = TYPE_STRING;
			} else if ("true".equalsIgnoreCase(defaultvalue) || "false".equalsIgnoreCase(defaultvalue)) { //$NON-NLS-1$ //$NON-NLS-2$
				type = TYPE_BOOLEAN;
			} else if (ISODateFormater.mayIsoDate(defaultvalue)) {
				type = TYPE_DATE;
			} else {
				try {
					Integer.parseInt(defaultvalue);
					type = TYPE_INTEGER;
				} catch (NumberFormatException e) {
					type = TYPE_STRING;
				}
			}
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(ArrayList<String> list) {
		if (list == null) {
			list = new ArrayList<String>();
		}
		this.list = list;
	}

	/**
	 * @return the list
	 */
	public ArrayList<String> getList() {
		return list;
	}

	/**
	 * @param readonly
	 *            the readonly to set
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleProperty(id, getLabel(), getIcon(), defaultvalue, password, list, readonly, hidden, getHelp());
	}

	/**
	 * @return true is this property field if a password String.
	 */
	public boolean isPassword() {
		return "true".equalsIgnoreCase(password) || TYPE_PASSWORD.equalsIgnoreCase(type); //$NON-NLS-1$
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	public boolean isString() {
		return TYPE_STRING.equalsIgnoreCase(type);
	}
	
	public boolean isInteger() {
		return TYPE_INTEGER.equalsIgnoreCase(type);
	}
	
	public boolean isDate() {
		return TYPE_DATE.equalsIgnoreCase(type);
	}
	
	public boolean isBoolean() {
		return TYPE_BOOLEAN.equalsIgnoreCase(type);
	}
	
	public boolean isEnumeration() {
		return TYPE_ENUM.equalsIgnoreCase(type);
	}
	
	public boolean isFloat() {
		return TYPE_FLOAT.equalsIgnoreCase(type);
	}
	
	public boolean isPath() {
		return TYPE_PATH.equalsIgnoreCase(type);
	}
	
	public boolean isURL() {
		return TYPE_URL.equalsIgnoreCase(type);
	}
	
	public boolean isPositiveInteger() {
		return TYPE_POSITIVEINTEGER.equalsIgnoreCase(type);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
}
