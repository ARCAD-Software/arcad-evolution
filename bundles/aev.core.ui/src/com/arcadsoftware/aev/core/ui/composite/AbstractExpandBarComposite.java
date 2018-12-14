/*
 * Créé le 27 avr. 2007
 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author MD
 */
public abstract class AbstractExpandBarComposite extends Composite {

	public static final int ORIENTATION_VERTICAL = 0;
	public static final int ORIENTATION_HORIZONTAL = 1;
	protected static int OFFSET = 6;

	int headerHeihght = 50;
	int bodyHeight = 100;
	protected Label titleLabel;

	String labelTitle;

	Composite userArea = null;

	boolean expanded = true;
	String imageId = EvolutionCoreUIPlugin.ARROW_EXPANDED;

	Label moreParameter = null;

	int orientation = ORIENTATION_HORIZONTAL;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractExpandBarComposite(Composite parent, int style, String title, int headerSize, int bodySize,
			int orientation) {
		super(parent, style);
		this.bodyHeight = bodySize;
		this.headerHeihght = headerSize;
		this.labelTitle = title;
		this.orientation = orientation;

	}

	public void initialize() {
		createContent();
		createBodyAreaComposite();
		doOnExpand(userArea);
		GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
	}

	public abstract void createBodyAreaComposite();

	void collapse() {
		userArea.dispose();
		imageId = getCollapsedIconKey();
		GridData gridData = (GridData) this.getLayoutData();
		if (orientation == ORIENTATION_HORIZONTAL)
			gridData.heightHint = headerHeihght + OFFSET;
		else
			gridData.widthHint = headerHeihght + OFFSET;
		doOnCollapse();

	}

	void expand() {
		createBodyAreaComposite();
		doOnExpand(userArea);
		imageId = getExpandedIconKey();
		GridData gridData = (GridData) this.getLayoutData();
		if (orientation == ORIENTATION_HORIZONTAL)
			gridData.heightHint = headerHeihght + bodyHeight + OFFSET;
		else
			gridData.widthHint = headerHeihght + bodyHeight + OFFSET;
	}

	public abstract void formatComposite();

	public abstract void formatExpander();

	public Composite getBarParent() {
		return this;
	}

	/**
	 * @param text
	 */
	public void formatTitle(String text) {
		// Do nothing
	}

	private void createContent() {
		formatComposite();
		moreParameter = new Label(getBarParent(), SWT.NONE);
		formatExpander();
		moreParameter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// Si le controle est déployé
				if (expanded)
					collapse();
				else
					expand();
				expanded = !expanded;
				moreParameter.setImage(CoreUILabels.getImage(imageId));
				GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
			}
		});
		moreParameter.setImage(CoreUILabels.getImage(getExpandedIconKey()));

		formatTitle(labelTitle);
	}

	/**
	 * Méthode permettant de définir les contrôles à ajouter à la barre.
	 * 
	 * @param parent
	 *            Composite : Composite sur lequel vous allez ajouter vos
	 *            contrôles.
	 */
	public abstract void doOnExpand(Composite parent);

	/**
	 * Methode permettant de déclencher des actions lors de la contraction de la
	 * barre.
	 * 
	 */
	public void doOnCollapse() {
		// Do nothing
	}

	public String getCollapsedIconKey() {
		return EvolutionCoreUIPlugin.ARROW_COLLAPSED;
	}

	public String getExpandedIconKey() {
		return EvolutionCoreUIPlugin.ARROW_EXPANDED;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean flag) {
		if (flag == expanded)
			return;
		// Si le controle est déployé
		if (flag)
			expand();
		else
			collapse();
		this.expanded = flag;
		moreParameter.setImage(CoreUILabels.getImage(imageId));
		GuiFormatTools.allLayout(AbstractExpandBarComposite.this);
	}
}
