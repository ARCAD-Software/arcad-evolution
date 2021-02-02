/*
 * Created on 13 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.columned.model;

import org.eclipse.swt.SWT;

/**
 * @author MD
 */
public class ArcadColumn {

	public static final int HIDDEN = 1;
	public static final int VISIBLE = 0;

	protected int actualIndex = 0;
	protected String identifier = null;
	protected String name = null;
	protected int position = 0;
	private int style = SWT.LEFT;
	protected String userName = null;
	protected int visible;

	protected int width = 100;

	public ArcadColumn() {
		super();
	}

	public ArcadColumn(final String property, final String name, final int visible, final int position,
			final int width) {
		this(property, name, name, visible, position, width, position); // pass name/userName, position/actualIndex
																		// properties
	}

	public ArcadColumn(final String property, final String name, final int visible, final int position, final int width,
			final int actualIndex) {
		this(property, name, name, visible, position, width, actualIndex); // pass name for both name and userName
																			// property
	}

	public ArcadColumn(final String identifier, final String name, final String userName, final int visible,
			final int position, final int width) {
		this(identifier, name, userName, visible, position, width, position); // pass position for both position and
																				// actualIndex property
	}

	/**
	 * Constructeur d'un colonne
	 *
	 * @param identifier
	 *            (aka property) : Identificateur de la colonne
	 * @param name
	 *            : Libellé de la colonne
	 * @param visible
	 *            : Indicateur de visibilité de la colonne
	 * @param position
	 *            Position de la colonne dans la liste
	 */
	public ArcadColumn(final String identifier, final String name, final String userName, final int visible,
			final int position, final int width,
			final int actualIndex) {
		super();
		this.identifier = identifier;
		this.name = name;
		this.userName = userName;
		this.visible = visible;
		this.position = position;
		this.width = width;
		this.actualIndex = actualIndex;
	}

	public void assignTo(final ArcadColumn target) {
		target.setIdentifier(identifier);
		target.setName(name);
		target.setUserName(userName);
		target.setVisible(visible);
		target.setPosition(position);
		target.setWidth(width);
	}

	public ArcadColumn duplicate() {
		return new ArcadColumn(identifier, name, userName, visible, position, width, actualIndex);
	}

	/**
	 * @return Returns the actualIndex.
	 */
	public int getActualIndex() {
		return actualIndex;
	}

	/**
	 * @return Returns the property.
	 */
	public String getIdentifier() {
		return identifier;
	}

	public String getLabel() {
		final StringBuilder buf = new StringBuilder(getUserName());
		return buf.append(" [").append(getName()).append(']').toString(); //$NON-NLS-1$
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the style
	 */
	public int getStyle() {
		return style;
	}

	public String getTooltipText() {
		return null;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return Returns the visible.
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param actualIndex
	 *            The actualIndex to set.
	 */
	public void setActualIndex(final int actualIndex) {
		this.actualIndex = actualIndex;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setIdentifier(final String property) {
		identifier = property;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param position
	 *            The position to set.
	 */
	public void setPosition(final int position) {
		this.position = position;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(final int style) {
		this.style = style;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @param visible
	 *            The visible to set.
	 */
	public void setVisible(final int visible) {
		this.visible = visible;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(final int width) {
		this.width = width;
	}
}
