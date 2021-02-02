package com.arcadsoftware.ae.core.settings;

public class SectionId {

	String help;
	int icon;
	String id;
	String label;
	transient int order;

	public String getHelp() {
		return help;
	}

	public int getIcon() {
		return icon;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public int getOrder() {
		return order;
	}

	public void setHelp(final String help) {
		this.help = help;
	}

	public void setIcon(final int icon) {
		this.icon = icon;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setOrder(final int order) {
		this.order = order;
	}
}
