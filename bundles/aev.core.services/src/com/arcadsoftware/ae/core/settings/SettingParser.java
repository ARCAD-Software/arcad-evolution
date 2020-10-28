package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.IXMLContentParser;

public class SettingParser implements IXMLContentParser {
	
	private Setting setting;
	
	public SettingParser(){
		setting = new Setting();
	}
	
	public String getRootName() {
		return "configuration";
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
		final List<ConsoleField> fields = new ArrayList<>();
		final NodeList nodes = node.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
        	final Node child = nodes.item(i);
        	ConsoleField field = null;
    		String id = getAttributeValue(child, "id");		
    		String pLabel = getAttributeValue(child, "label");
    		String pHelp = getAttributeValue(child, "help");
    		String pDefault = getAttributeValue(child, "default");    		
    		
    		final String nodeName = child.getNodeName();
        	if (nodeName.equalsIgnoreCase("property")) {
	    		field = new ConsoleProperty();
	    		ConsoleProperty cp = (ConsoleProperty)field;
	    		String data= child.getTextContent(); 
	    		cp.setId(id);
	    		cp.setDefaultvalue(pDefault);
	    		cp.setValue(data);
	    		final NodeList items = child.getChildNodes();
	            for ( int j = 0; j < items.getLength(); i++ ) {
	            	final Node item = items.item(j);
	            	if(item.getNodeName().equalsIgnoreCase("item")) {
	            		cp.getList().add(item.getTextContent());	
	            	}
	            }	            
        	} else if (nodeName.equalsIgnoreCase("text")) {
        		field = new ConsoleText();
        	} else if (nodeName.equalsIgnoreCase("set")) {
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
