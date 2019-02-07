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

	protected ArcadCollection lastParent;
	protected int level = 1;
	protected int tag = -1;
	protected String iconID = StringTools.EMPTY;
	protected boolean useTagEquality = false;

	public ArcadEntity() {
		super();
	}

	@Override
	public String getIconID() {
		return iconID;
	}

	@Override
	public String getOverlayID() {
		return null;
	}

	@Override
	public void setIconID(String string) {
		iconID = string;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int i) {
		level = i;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int data) {
		tag = data;
	}

	@Override
	public void setParent(ArcadCollection parent) {
		this.lastParent = parent;
	}

	@Override
	public ArcadCollection getParent() {
		return lastParent;
	}
	
	@Override
	public void unsetParent(ArcadCollection parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean hasChildren() {
		if (lastParent == null)
			return false;
		return lastParent.hasChildren(this);
	}

	@Override
	public IArcadCollectionItem duplicate() {
		System.out.println("Warning: call to ArcadEntity::duplicate() returning null.");
		return null;
	}

	@Override
	public boolean equalsItem(IArcadCollectionItem item) {
		return false;
	}

	@Override
	public boolean equalsWithLevel(IArcadCollectionItem item) {
		return false;
	}

	protected boolean read() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public boolean isUseTagEquality() {
		return useTagEquality;
	}

	@Override
	public void setUseTagEquality(boolean useTagEquality) {
		this.useTagEquality = useTagEquality;
	}
}
