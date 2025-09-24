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
package com.arcadsoftware.aev.core.osgi;

public class ServiceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7895535899805707572L;

	public ServiceNotFoundException(final Class<?> serviceClass) {
		super("Implementation of service " + serviceClass + " could not be found.");
	}
	
	public ServiceNotFoundException(final Class<?> serviceClass, final String name) {
		super("Implementation of service " + serviceClass + " with name " + name + " could not be found.");
	}
}
