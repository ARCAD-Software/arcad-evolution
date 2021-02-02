package com.arcadsoftware.aev.core.ui.columned.model;

/**
 * Extended version of ArcadColumn that allows tooltip text for userName
 *
 * @author ACL
 */
public class ArcadTableColumn extends ArcadColumn {

	private String tooltipText = null;

	public ArcadTableColumn(final String property, final String name, final int visible, final int position,
			final int width) {
		super(property, name, name, visible, position, width, position); // pass name/userName, position/actualIndex
																			// properties
	}

	public ArcadTableColumn(final String identifier, final String name, final String userName, final int visible,
			final int position, final int width,
			final int actualIndex) {
		super(identifier, name, userName, visible, position, width, actualIndex);
		setTooltipText(name);
	}

	@Override
	public void assignTo(final ArcadColumn target) {
		super.assignTo(target);
		if (target instanceof ArcadTableColumn) {
			((ArcadTableColumn) target).setTooltipText(tooltipText);
		}
	}

	@Override
	public ArcadColumn duplicate() {
		final ArcadTableColumn column = new ArcadTableColumn(identifier, name, userName, visible, position, width,
				actualIndex);
		column.setTooltipText(tooltipText);
		return column;
	}

	@Override
	public String getTooltipText() {
		return tooltipText;
	}

	public void setTooltipText(final String tooltipText) {
		this.tooltipText = tooltipText;
	}

}
