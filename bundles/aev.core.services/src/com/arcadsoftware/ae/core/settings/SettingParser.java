package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.arcadsoftware.ae.core.utils.IXMLContentParser;

public class SettingParser implements IXMLContentParser {
	
	
	Setting setting;
	
	public SettingParser(){
		setting = new Setting();
	}
	
	public String getRootName() {
		return "configuration";
	}

	/**
	 * Returns the value of the attribute defined by <code>attributeName</code>.<br>
	 * If the attribute doesn't exist, returns an empty string
	 * @param node the parent node
	 * @param attributeName The attribute Name
	 * @return the content of the attribute or an empty string
	 */
	protected String getAttributeValue(Element node,String attributeName) {
    	Attribute attribute = 
    		node.attribute(attributeName);
    	if (attribute!=null) {
    		return attribute.getStringValue();
    	}
    	return ""; //$NON-NLS-1$
	}
	
	public boolean parse(Element node) {
		String category = getAttributeValue(node, "category");
		Category c = setting.getCategoryByName(category);
		if (c==null) {
			c = new Category();
			c.setLabel(category);
			setting.addCategory(c);
		}
		SectionId section = new SectionId();		
		String sectionId = getAttributeValue(node, "sid");		
		String label = getAttributeValue(node, "label");
		String help = getAttributeValue(node, "help");
		section.setId(sectionId);
		section.setLabel(label);
		section.setHelp(help);		
		ArrayList<ConsoleField> fields = new ArrayList<ConsoleField>();
        for ( Iterator i = node.elementIterator(); i.hasNext(); ) {
        	Element prop = (Element) i.next();
        	ConsoleField field = null;
    		String id = getAttributeValue(prop, "id");		
    		String pLabel = getAttributeValue(prop, "label");
    		String pHelp = getAttributeValue(prop, "help");
    		String pDefault = getAttributeValue(prop, "default");
    		String pType = getAttributeValue(prop, "type");
    		
        	if (prop.getName().equalsIgnoreCase("property")) {
	    		field = new ConsoleProperty();
	    		ConsoleProperty cp = (ConsoleProperty)field;
	    		String data= prop.getText(); 
	    		cp.setId(id);
	    		cp.setDefaultvalue(pDefault);
	    		cp.setValue(data);
	            for ( Iterator j = prop.elementIterator("item"); j.hasNext(); ) {
	            	Element item = (Element) j.next();
	            	String itemValue = item.getText();
	            	cp.getList().add(itemValue);
	            }
        	} else if (prop.getName().equalsIgnoreCase("text")) {
        		field = new ConsoleText();
        	} else if (prop.getName().equalsIgnoreCase("set")) {
        		field = new ConsoleSet();        		
        	}
        	if (field!=null){
        		field.setLabel(pLabel);
        		field.setHelp(pHelp);
        		fields.add(field);
        	}
        }	
        Form form = new Form(section,fields);
        c.getList().add(form);
		return true;
	}

	
	public Setting getSetting() {
		return setting;
	}
}
