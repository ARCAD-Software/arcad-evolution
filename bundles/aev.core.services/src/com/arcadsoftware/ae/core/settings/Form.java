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
