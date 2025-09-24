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
package com.arcadsoftware.ae.core.settings;

import java.util.List;

public class Form {
	List<ConsoleField> fields;
	SectionId section;

	public Form(final SectionId section, final List<ConsoleField> fields) {
		this.fields = fields;
		this.section = section;
	}

	public List<ConsoleField> getFields() {
		return fields;
	}

	public SectionId getSection() {
		return section;
	}

}
