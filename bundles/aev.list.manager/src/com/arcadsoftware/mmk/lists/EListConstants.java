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
package com.arcadsoftware.mmk.lists;

public enum EListConstants {
	LST_ATT_ROWID("id"), LST_TAG_COL("col"), LST_TAG_COLUMNDEF("columndef"), LST_TAG_COMMENT(
			"comment"), LST_TAG_CONTENT("content"), LST_TAG_DESCRIPTION("description"), LST_TAG_HEADER(
					"header"), LST_TAG_LIST("arcad.list"), LST_TAG_METADATAS("metadatas"), LST_TAG_ROW(
							"row"), LST_TYPE_FILE("fileList"), LST_TYPE_GENERIC("genericList");

	private String value = null;

	private EListConstants(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
