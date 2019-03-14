package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractList;

public abstract class AbstractCashManager  extends AbstractLoggedObject{
	protected AbstractList list;
	
	private boolean flushImmediat = true;
	protected boolean active = false;
	
	public AbstractCashManager(AbstractList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}
	
	public void flushRequest() {
		if (flushImmediat)
			flush();
	}	
	
	/**
	 * Renvoit 
	 * @return the active boolean : 
	 */
	public boolean isActive() {
		return active;
	}	
	
	/**
	 * Renvoit 
	 * @return the flushImmediat boolean : 
	 */
	public boolean isFlushImmediat() {
		return flushImmediat;
	}

	/**
	 * @param flushImmediat the flushImmediat to set
	 */
	public void setFlushImmediat(boolean flushImmediat) {
		this.flushImmediat = flushImmediat;
	}
	
	public abstract int flush();
	
	
}
