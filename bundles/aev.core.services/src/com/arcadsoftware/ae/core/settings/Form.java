package com.arcadsoftware.ae.core.settings;

import java.util.List;




public class Form {
	SectionId section;
	List<ConsoleField> fields; 

	public Form(SectionId section,List<ConsoleField> fields){
		this.fields = fields;
		this.section = section;
	}
	
	public List<ConsoleField> getFields(){
		return fields;
	}
	
	public SectionId getSection(){
		return section;
	}
	
}
