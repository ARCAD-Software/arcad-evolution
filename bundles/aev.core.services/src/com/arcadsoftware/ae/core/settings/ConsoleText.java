package com.arcadsoftware.ae.core.settings;

/**
 * Console Section widget that represent a Text information.
 */
public class ConsoleText extends ConsoleField {

	public ConsoleText() {
		super();
	}

	public ConsoleText(final String label) {
		super();
		setLabel(label);
	}

	public ConsoleText(final String label, final int icon) {
		this(label);
		setIcon(icon);
	}

	public ConsoleText(final String label, final int icon, final String help) {
		super();
		setLabel(label);
		setIcon(icon);
		setHelp(help);
	}

	public ConsoleText(final String label, final String help) {
		this(label);
		setHelp(help);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleText(getLabel(), getIcon(), getHelp());
	}

}
