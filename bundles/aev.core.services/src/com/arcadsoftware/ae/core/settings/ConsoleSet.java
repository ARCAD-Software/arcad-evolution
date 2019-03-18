package com.arcadsoftware.ae.core.settings;

public class ConsoleSet extends ConsoleField {
	
	private int anchor;
	
	public ConsoleSet() {
		super();
	}

	public ConsoleSet(String label) {
		this();
		setLabel(label);
	}
	
	public ConsoleSet(String label, String help) {
		this(label);
		setHelp(help);
	}
	
	public ConsoleSet(String label, int icon, String help) {
		this(label, help);
		setIcon(icon);
	}
	
	public ConsoleSet(String label, int icon, String help, int anchor) {
		this(label, icon, help);
		this.anchor = anchor;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleSet(getLabel(), getIcon(), getHelp());
	}

	/**
	 * @param anchor the anchor of this set
	 */
	public void setAnchor(int anchor) {
		this.anchor = anchor;
	}

	/**
	 * The anchor define a id used to reclace this set content from following action's form.
	 * 
	 * <p> Into a form returned from an action. If a Set with the same anchor exist into the previous form then it must be
	 * replaced by the current one. If it does not then it must be added to the current actions form.
	 * 
	 * @return the anchor of this set.
	 */
	public int getAnchor() {
		return anchor;
	}
	
}
