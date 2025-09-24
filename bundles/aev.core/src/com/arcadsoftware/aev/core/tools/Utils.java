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
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class Utils {

	private static Utils instance;

	public static void beginAction() {
		getInstance().getHelper().beginAction();
	}

	// private static Cursor cursor;

	public static void endAction() {
		getInstance().getHelper().endAction();
	}

	public static String getBasedLocation() {
		return getInstance().getHelper().getBasedLocation();
	}

	public static String getCompliantFileName() {
		return getInstance().getHelper().getCompliantFileName();
	}

	public static Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	private IHelper _helper = null;

	/**
	 * 
	 */
	public Utils() {
		super();
	}

	public IHelper getHelper() {
		if (_helper == null) {
			_helper = new DefaultUtilsHelper();
		}
		return _helper;
	}

	public void setHelper(final IHelper helper) {
		_helper = helper;
	}
}
