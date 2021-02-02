/*
 * Cr�� le 29 mars 2007
 *
 */
package com.arcadsoftware.aev.core.model;

/**
 * @author MD
 */
public class ArcadListenerList {
	/**
	 * The empty array singleton instance, returned by getListeners() when size == 0.
	 */
	private static final Object[] EmptyArray = new Object[0];

	/**
	 * The initial capacity of the list. Always >= 1.
	 */
	private int capacity;

	/**
	 * The list of listeners. Initially <code>null</code> but initialized to an array of size capacity the first time a
	 * listener is added. Maintains invariant: listeners != null IFF size != 0
	 */
	private Object[] listeners = null;

	/**
	 * The current number of listeners. Maintains invariant: 0 <= size <= listeners.length.
	 */
	private int size;

	/**
	 * Creates a listener list with an initial capacity of 1.
	 */
	public ArcadListenerList() {
		this(1);
	}

	/**
	 * Creates a listener list with the given initial capacity.
	 *
	 * @param capacity
	 *            the number of listeners which this list can initially accept without growing its internal
	 *            representation; must be at least 1
	 */
	public ArcadListenerList(final int capacity) {
		if (capacity >= 1) {
			this.capacity = capacity;
		}
	}

	/**
	 * Adds the given listener to this list. Has no effect if an identical listener is already registered.
	 *
	 * @param listener
	 *            the listener
	 */
	public void add(final Object listener) {
		if (listener != null) {
			if (size == 0) {
				listeners = new Object[capacity];
			} else {
				// check for duplicates using identity
				for (int i = 0; i < size; ++i) {
					if (listeners[i] == listener) {
						return;
					}
				}
				// grow array if necessary
				if (size == listeners.length) {
					System.arraycopy(listeners, 0, listeners = new Object[size * 2 + 1], 0, size);
				}
			}

			listeners[size] = listener;
			size++;
		}
	}

	/**
	 * Removes all listeners from this list.
	 */
	public void clear() {
		size = 0;
		listeners = null;
	}

	/**
	 * Returns an array containing all the registered listeners, in the order in which they were added.
	 * <p>
	 * The resulting array is unaffected by subsequent adds or removes. If there are no listeners registered, the result
	 * is an empty array singleton instance (no garbage is created). Use this method when notifying listeners, so that
	 * any modifications to the listener list during the notification will have no effect on the notification itself.
	 * </p>
	 *
	 * @return the list of registered listeners
	 */
	public Object[] getListeners() {
		if (size == 0) {
			return EmptyArray;
		}
		final Object[] result = new Object[size];
		System.arraycopy(listeners, 0, result, 0, size);
		return result;
	}

	/**
	 * Returns whether this listener list is empty.
	 *
	 * @return <code>true</code> if there are no registered listeners, and <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes the given listener from this list. Has no effect if an identical listener was not already registered.
	 *
	 * @param listener
	 *            the listener
	 */
	public void remove(final Object listener) {
		if (listener != null) {
			for (int i = 0; i < size; ++i) {
				if (listeners[i] == listener) {
					if (size == 1) {
						listeners = null;
						size = 0;
					} else {
						System.arraycopy(listeners, i + 1, listeners, i, --size - i);
						listeners[size] = null;
					}
					return;
				}
			}
		}
	}

	/**
	 * Returns the number of registered listeners.
	 *
	 * @return the number of registered listeners
	 */
	public int size() {
		return size;
	}
}
