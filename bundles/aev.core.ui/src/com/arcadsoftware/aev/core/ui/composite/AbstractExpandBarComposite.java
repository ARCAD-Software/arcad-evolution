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
/*
 * Cr�� le 27 avr. 2007
 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author MD
 */
public abstract class AbstractExpandBarComposite extends Composite {

	protected static int OFFSET = 6;
	public static final int ORIENTATION_HORIZONTAL = 1;
	public static final int ORIENTATION_VERTICAL = 0;

	int bodyHeight = 100;
	boolean expanded = true;
	int headerHeihght = 50;

	String labelTitle;

	Label moreParameter = null;

	int orientation = ORIENTATION_HORIZONTAL;

	protected Label titleLabel;

	Composite userArea = null;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractExpandBarComposite(final Composite parent, final int style, final String title, final int headerSize,
			final int bodySize,
			final int orientation) {
		super(parent, style);
		bodyHeight = bodySize;
		headerHeihght = headerSize;
		labelTitle = title;
		this.orientation = orientation;

	}

	void collapse() {
		userArea.dispose();
		final GridData gridData = (GridData) getLayoutData();
		if (orientation == ORIENTATION_HORIZONTAL) {
			gridData.heightHint = headerHeihght + OFFSET;
		} else {
			gridData.widthHint = headerHeihght + OFFSET;
		}
		doOnCollapse();

	}

	public abstract void createBodyAreaComposite();

	private void createContent() {
		formatComposite();
		moreParameter = new Label(getBarParent(), SWT.NONE);
		formatExpander();
		moreParameter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				// Si le controle est d�ploy�
				if (expanded) {
					collapse();
				} else {
					expand();
				}
				setImage();
				expanded = !expanded;

				GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
			}

		});
		moreParameter.setImage(Icon.COLLAPSE.image());

		formatTitle(labelTitle);
	}

	/**
	 * Methode permettant de d�clencher des actions lors de la contraction de la barre.
	 */
	public void doOnCollapse() {
		// Do nothing
	}

	/**
	 * M�thode permettant de d�finir les contr�les � ajouter � la barre.
	 *
	 * @param parent
	 *            Composite : Composite sur lequel vous allez ajouter vos contr�les.
	 */
	public abstract void doOnExpand(Composite parent);

	void expand() {
		createBodyAreaComposite();
		doOnExpand(userArea);
		final GridData gridData = (GridData) getLayoutData();
		if (orientation == ORIENTATION_HORIZONTAL) {
			gridData.heightHint = headerHeihght + bodyHeight + OFFSET;
		} else {
			gridData.widthHint = headerHeihght + bodyHeight + OFFSET;
		}
	}

	public abstract void formatComposite();

	public abstract void formatExpander();

	/**
	 * @param text
	 */
	public void formatTitle(final String text) {
		// Do nothing
	}

	public Composite getBarParent() {
		return this;
	}

	public void initialize() {
		createContent();
		createBodyAreaComposite();
		doOnExpand(userArea);
		GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(final boolean flag) {
		if (flag == expanded) {
			return;
		}
		// Si le controle est d�ploy�
		if (flag) {
			expand();
		} else {
			collapse();
		}
		expanded = flag;
		setImage();
		GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
	}

	private void setImage() {
		moreParameter.setImage(expanded ? Icon.COLLAPSE.image() : Icon.EXPAND.image());
	}
}
