package com.arcadsoftware.aev.core.ui.columned.model;

/**
 * Extended version of ArcadColumn that allows tooltip text for userName
 * 
 * @author ACL
 */
public class ArcadTableColumn extends ArcadColumn {

	private String tooltipText = null;

	public ArcadTableColumn(String identifier, String name, String userName, int visible, int position, int width,
			int actualIndex) {
		super(identifier, name, userName, visible, position, width, actualIndex);
		setTooltipText(name);
	}
	
	public ArcadTableColumn(String property, String name, int visible, int position, int width) {
		super(property, name, name, visible, position, width, position); // pass name/userName, position/actualIndex properties
	}
	
	public String getTooltipText() {
		return tooltipText;
	}

	public void setTooltipText(String tooltipText) {
		this.tooltipText = tooltipText;
	}

	@Override
	public ArcadColumn duplicate() {
		ArcadTableColumn column = new ArcadTableColumn(identifier, name, userName, visible, position, width, actualIndex);
		column.setTooltipText(tooltipText);
		return column;
	}

	@Override
	public void assignTo(ArcadColumn target) {
		super.assignTo(target);
		if (target instanceof ArcadTableColumn)
			((ArcadTableColumn)target).setTooltipText(tooltipText);
	}

}
