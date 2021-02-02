package com.arcadsoftware.ae.core.settings;

public abstract class ConsoleField {

	private String help;
	private int icon;
	private String label;

	/**
	 * @return the help
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param help
	 *            the help to set
	 */
	public void setHelp(final String help) {
		this.help = help;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(final int icon) {
		this.icon = icon;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

}
