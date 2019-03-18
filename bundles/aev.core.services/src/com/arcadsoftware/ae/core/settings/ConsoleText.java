package com.arcadsoftware.ae.core.settings;

/**
 * Console Section widget that represent a Text information.
 */
public class ConsoleText extends ConsoleField {

	public ConsoleText() {
		super();
	}
	
	public ConsoleText(String label, int icon, String help) {
		super();
		setLabel(label);
		setIcon(icon);
		setHelp(help);
	}

	public ConsoleText(String label, int icon) {
		this(label);
		setIcon(icon);
	}

	public ConsoleText(String label, String help) {
		this(label);
		setHelp(help);
	}

	public ConsoleText(String label) {
		super();
		setLabel(label);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleText(getLabel(), getIcon(), getHelp());
	}
	
}
