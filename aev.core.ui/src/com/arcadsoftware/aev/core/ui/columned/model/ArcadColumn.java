/*
 * Created on 13 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.columned.model;

import org.eclipse.swt.SWT;

/**
 * @author MD
 */
public class ArcadColumn {

	public static final int VISIBLE = 0;
	public static final int HIDDEN = 1;

	protected String identifier = null;
	protected String name = null;
	protected String userName = null;
	protected int visible;
	protected int position = 0;
	protected int width = 100;
	protected int actualIndex = 0;

	private int style = SWT.LEFT;


	public ArcadColumn() {
		super();
	}

	/**
	 * Constructeur d'un colonne
	 * 
	 * @param identifier (aka property)
	 *            : Identificateur de la colonne
	 * @param name
	 *            : Libellé de la colonne
	 * @param visible
	 *            : Indicateur de visibilité de la colonne
	 * @param position
	 *            Position de la colonne dans la liste
	 */
	public ArcadColumn(String identifier, String name, String userName, int visible, int position, int width,
			int actualIndex) {
		super();
		this.identifier = identifier;
		this.name = name;
		this.userName = userName;
		this.visible = visible;
		this.position = position;
		this.width = width;
		this.actualIndex = actualIndex;
	}
	public ArcadColumn(String identifier, String name, String userName, int visible, int position, int width) {
		this(identifier, name, userName, visible, position, width, position); // pass position for both position and actualIndex property
	}
	
	public ArcadColumn(String property, String name, int visible, int position, int width, int actualIndex) {
		this(property, name, name, visible, position, width, actualIndex); // pass name for both name and userName property
	}
	
	public ArcadColumn(String property, String name, int visible, int position, int width) {
		this(property, name, name, visible, position, width, position); // pass name/userName, position/actualIndex properties
	}
	public ArcadColumn duplicate() {
		return new ArcadColumn(identifier, name, userName, visible, position, width, actualIndex);
	}

	public void assignTo(ArcadColumn target) {
		target.setIdentifier(identifier);
		target.setName(name);
		target.setUserName(userName);
		target.setVisible(visible);
		target.setPosition(position);
		target.setWidth(width);
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return Returns the property.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setIdentifier(String property) {
		this.identifier = property;
	}

	/**
	 * @return Returns the visible.
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            The visible to set.
	 */
	public void setVisible(int visible) {
		this.visible = visible;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return Returns the actualIndex.
	 */
	public int getActualIndex() {
		return actualIndex;
	}

	/**
	 * @param actualIndex
	 *            The actualIndex to set.
	 */
	public void setActualIndex(int actualIndex) {
		this.actualIndex = actualIndex;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLabel() {
		StringBuffer buf = new StringBuffer(getUserName());
		return buf.append(" [").append(getName()).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return the style
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	public String getTooltipText() {
		return null;
	}
}
