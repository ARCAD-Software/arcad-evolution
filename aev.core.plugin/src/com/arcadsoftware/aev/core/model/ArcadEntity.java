/*
 * Créé le 26 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class ArcadEntity implements IArcadCollectionItem, IAdaptable, ITagable {

	private ArcadCollection parent;
	private int level = 1;
	private int tag = -1;
	private String iconID = StringTools.EMPTY;
	boolean useTagEquality = false;

	public ArcadEntity() {
		super();
	}

	public String getIconID() {
		return iconID;
	}

	public String getOverlayID() {
		return null;
	}

	public void setIconID(String string) {
		iconID = string;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int i) {
		level = i;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int data) {
		tag = data;
	}

	public void setParent(ArcadCollection parent) {
		this.parent = parent;
	}

	public ArcadCollection getParent() {
		return parent;
	}

	public boolean hasChildren() {
		if (parent == null)
			return false;
		return parent.hasChildren(this);
	}

	public IArcadCollectionItem duplicate() {
		return null;
	}

	public boolean equalsItem(IArcadCollectionItem item) {
		return false;
	}

	public boolean equalsWithLevel(IArcadCollectionItem item) {
		return false;
	}

	protected boolean read() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean isUseTagEquality() {
		return useTagEquality;
	}

	public void setUseTagEquality(boolean useTagEquality) {
		this.useTagEquality = useTagEquality;
	}
}
