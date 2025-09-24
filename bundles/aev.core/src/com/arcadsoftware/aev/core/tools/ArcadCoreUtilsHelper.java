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

import com.arcadsoftware.aev.core.EvolutionCorePlugin;

/**
 * @author MD
 */
public class ArcadCoreUtilsHelper implements IHelper {
	public ArcadCoreUtilsHelper() {
		super();
	}

	@Override
	public void beginAction() {
		// Do nothing
	}

	@Override
	public void endAction() {
		// Do nothing
	}

	@Override
	public String getBasedLocation() {
		return EvolutionCorePlugin.getDefault().getStateLocation().toString();
	}

	@Override
	public String getCompliantFileName() {
		return EvolutionCorePlugin.getCompliantFileName();
	}
}
