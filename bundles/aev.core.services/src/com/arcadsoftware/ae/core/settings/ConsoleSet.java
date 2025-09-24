/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.ae.core.settings;

public class ConsoleSet extends ConsoleField {

	private int anchor;

	public ConsoleSet() {
		super();
	}

	public ConsoleSet(final String label) {
		this();
		setLabel(label);
	}

	public ConsoleSet(final String label, final int icon, final String help) {
		this(label, help);
		setIcon(icon);
	}

	public ConsoleSet(final String label, final int icon, final String help, final int anchor) {
		this(label, icon, help);
		this.anchor = anchor;
	}

	public ConsoleSet(final String label, final String help) {
		this(label);
		setHelp(help);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ConsoleSet(getLabel(), getIcon(), getHelp());
	}

	/**
	 * The anchor define a id used to reclace this set content from following action's form.
	 * <p>
	 * Into a form returned from an action. If a Set with the same anchor exist into the previous form then it must be
	 * replaced by the current one. If it does not then it must be added to the current actions form.
	 *
	 * @return the anchor of this set.
	 */
	public int getAnchor() {
		return anchor;
	}

	/**
	 * @param anchor
	 *            the anchor of this set
	 */
	public void setAnchor(final int anchor) {
		this.anchor = anchor;
	}

}
