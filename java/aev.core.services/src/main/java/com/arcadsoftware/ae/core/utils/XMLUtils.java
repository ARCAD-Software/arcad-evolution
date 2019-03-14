package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.arcadsoftware.ae.core.logger.MessageLogger;

public class XMLUtils {

	
	public static boolean createXmlDocument(String xmlFilename,IXMLContentProvider content) throws IOException {
		Document document = DocumentHelper.createDocument();
		document.addDocType(content.getRootName(),null,null);
		//logDocument.setXMLEncoding("ISO-8859-1"); //$NON-NLS-1$
		document.setXMLEncoding(content.getEncoding()); //$NON-NLS-1$
		Element rootNode = document.addElement(content.getRootName());
		content.provide(rootNode);
        XMLWriter writer;		
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter(new FileOutputStream(xmlFilename), format);
        writer.write(document);	
        writer.flush();
        writer.close();	        
        return true;			
	}	
	
	public static boolean loadXmlDocument(String xmlFilename, IXMLContentParser parser) 
	throws IOException, DocumentException, FileNotFoundException {
        return loadXmlDocument(xmlFilename,parser,false);
	}		
	
	public static boolean loadXmlDocument(String xmlFilename, IXMLContentParser parser,boolean parseRoot) 
	throws IOException, DocumentException, FileNotFoundException {
		File f = new File(xmlFilename);
		if (!f.exists()) {
			throw new FileNotFoundException(xmlFilename);
		}		
        SAXReader reader = new SAXReader();

		Document document = reader.read(xmlFilename);
		Element root = document.getRootElement();	
		if (parseRoot) {
			return parser.parse(root); 
		} else {
	        for ( Iterator i = root.elementIterator(parser.getRootName()); i.hasNext(); ) {
	        	Element instElement = (Element) i.next();
	        	if (!parser.parse(instElement)){
	        		return false;
	        	}	        	
	        }
		}
        return true;
	}	
	
	
	
	public static boolean loadXml(String xml, IXMLContentParser parser) 
	throws DocumentException  {
        return loadXml(xml, parser,false);
	}	
	
	public static boolean loadXml(String xml, IXMLContentParser parser,boolean parseRoot) 
	throws DocumentException  {
    		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();	
		if (parseRoot) {
			return parser.parse(root); 
		} else {
	        for ( Iterator i = root.elementIterator(parser.getRootName()); i.hasNext(); ) {
	        	Element instElement = (Element) i.next();
	        	if (!parser.parse(instElement)){
	        		return false;
	        	}	        	
	        }
		}
        return true;
	}	
	
	public static String createXml(IXMLContentProvider content)  {
		Document document = DocumentHelper.createDocument();
		document.addDocType(content.getRootName(),null,null);
		//logDocument.setXMLEncoding("ISO-8859-1"); //$NON-NLS-1$
		document.setXMLEncoding(content.getEncoding()); //$NON-NLS-1$
		Element rootNode = document.addElement(content.getRootName());
		content.provide(rootNode);
		return document.asXML();
	}		
	
	public static Document createEmptyDocument(String fileName,String rootName) throws IOException {
		Document document = DocumentHelper.createDocument();
		document.addDocType(rootName,null,null);
		document.setXMLEncoding("ISO-8859-1"); //$NON-NLS-1$
		Element root = document.addElement(rootName);
        XMLWriter writer;
		writer = new XMLWriter(new FileWriter( fileName));
        writer.write(document);
        writer.close();	
        return document;			
	}
	
	public static String getTemfile(String filename){		
		String arcadHome  = Utils.getHomeDirectory();	 
		if (arcadHome!=null) {
			StringBuffer extensionPath= new StringBuffer(arcadHome);
			extensionPath.append(File.separator)
			             .append("integrater").append(File.separator) //$NON-NLS-1$
			             .append("tmp").append(File.separator) //$NON-NLS-1$		
			             .append(filename);
			return extensionPath.toString();
		}
	    return filename; 
	}
	
	public static File xslTransformation(String xmlFilename, String xlsFilename,boolean deleteSourcefile){
		String outFilename = null;
		if (xlsFilename.equals("")) { //$NON-NLS-1$
			//No transformation
			return new File(xmlFilename);
		} else {
			outFilename = getTemfile("trs"+Utils.computeId()+".xml");  //$NON-NLS-1$//$NON-NLS-2$
			File f = new File(outFilename);
			f.getParentFile().mkdirs();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				transformer = tFactory.newTransformer(new StreamSource(xlsFilename));
				transformer.transform(
						new StreamSource(xmlFilename), 
						new StreamResult(new FileOutputStream(outFilename)));
				if (deleteSourcefile){
					f = new File(xmlFilename);
					f.delete();
				}
				return f;				
			} catch (TransformerConfigurationException e) {
				MessageLogger.sendErrorMessage("XSL-Transformation",Utils.stackTrace(e)); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				MessageLogger.sendErrorMessage("XSL-Transformation",Utils.stackTrace(e));//$NON-NLS-1$
			} catch (TransformerException e) {
				MessageLogger.sendErrorMessage("XSL-Transformation",Utils.stackTrace(e));//$NON-NLS-1$
			}
			return null;
		}
	}	
	
	public static String getAttributeValue(Element e, String name) {
    	Attribute attribute = e.attribute(name);
    	if (attribute!=null) 
    		return attribute.getStringValue();
    	return ""; //$NON-NLS-1$
	}		
	
	public static String getElementValue(Element e, String name) {
    	Element element = e.element(name);
    	if (element!=null) 
    		return element.getText();
    	return ""; //$NON-NLS-1$
	}
}
