package com.arcadsoftware.mmk.lists;

public enum EListConstants {
	LST_TAG_LIST("arcad.list"),
	LST_TAG_HEADER("header"),
	LST_TAG_DESCRIPTION("description"),	
	LST_TAG_COMMENT("comment"),	
	LST_TAG_METADATAS("metadatas"),
	LST_TAG_COL("col"),
	LST_TAG_ROW("row"),
	LST_TAG_CONTENT("content"),	
	LST_TAG_COLUMNDEF("columndef"),
	LST_ATT_ROWID("id"),	
	LST_TYPE_FILE("fileList"),
	LST_TYPE_GENERIC("genericList")	
	;
	
	private String value =null;
	private EListConstants(String value){
		this.value = value;		
	}
	
	public String getValue() {
		return value;
	}
}

