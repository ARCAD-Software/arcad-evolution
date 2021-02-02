/*
 * Cr�� le 26 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.model;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public abstract class ArcadEntity implements IArcadCollectionItem, ITagable {

	protected String iconID = StringTools.EMPTY;
	protected ArcadCollection lastParent;
	protected int level = 1;
	protected int tag = -1;
	protected boolean useTagEquality = false;

	public ArcadEntity() {
		super();
	}

	@Override
	public IArcadCollectionItem duplicate() {
		System.out.println("Warning: call to ArcadEntity::duplicate() returning null.");
		return null;
	}

	@Override
	public boolean equalsItem(final IArcadCollectionItem item) {
		return false;
	}

	@Override
	public boolean equalsWithLevel(final IArcadCollectionItem item) {
		return false;
	}

	@Override
	public String getIconID() {
		return iconID;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public String getOverlayID() {
		return null;
	}

	@Override
	public ArcadCollection getParent() {
		return lastParent;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public boolean hasChildren() {
		if (lastParent == null) {
			return false;
		}
		return lastParent.hasChildren(this);
	}

	@Override
	public boolean isUseTagEquality() {
		return useTagEquality;
	}

	protected boolean read() {
		return false;
	}

	@Override
	public void setIconID(final String string) {
		iconID = string;
	}

	@Override
	public void setLevel(final int i) {
		level = i;
	}

	@Override
	public void setParent(final ArcadCollection parent) {
		lastParent = parent;
	}

	@Override
	public void setTag(final int data) {
		tag = data;
	}

	@Override
	public void setUseTagEquality(final boolean useTagEquality) {
		this.useTagEquality = useTagEquality;
	}

	@Override
	public void unsetParent(final ArcadCollection parent) {
		// TODO Auto-generated method stub
	}
}
