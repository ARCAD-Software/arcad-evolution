package com.arcadsoftware.ae.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.arcadsoftware.ae.core.logger.MessageLogger;

public class XMLUtils {
	private XMLUtils() {
		
	}
	
	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		
		return factory;
	}
	
	public static Document createNewXMLDocument() throws ParserConfigurationException {		
		return getDocumentBuilderFactory().newDocumentBuilder().newDocument();
	}

	public static Document loadXMLFromString(String xml) throws IOException, ParserConfigurationException, SAXException {
		return loadXMLFromReader(new StringReader(xml));
	}

	public static Document loadXMLFromFile(File file) throws IOException, ParserConfigurationException, SAXException {
		return loadXMLFromReader(new FileReader(file));
	}

	public static Document loadXMLFromReader(Reader reader) throws IOException, ParserConfigurationException, SAXException {
		try(BufferedReader bufferedReader = new BufferedReader(reader)){			
			final DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
			final InputSource is = new InputSource(reader);
			return builder.parse(is);
		}
	}

	protected static String resolveSpecialEntities(String xmlString) {
		return xmlString.replace("&sol;", "/");
	}

	public static void writeXMLDocumentToFile(Document document, File targetFile, String encoding)
	throws TransformerException {
		final DOMSource source = new DOMSource(document);
		final StreamResult sortie = new StreamResult(targetFile);
		getStandardTransformer(true, encoding).transform(source, sortie);
	}

	public static String outputXMLDocumentToString(final Document document, final String encoding)
	throws IOException, TransformerException {
		try (StringWriter stringWriter = new StringWriter()){
			DOMSource source = new DOMSource(document);			
			StreamResult output = new StreamResult(stringWriter);
			getStandardTransformer(false, encoding).transform(source, output);

			return stringWriter.toString();
		}
	}

	private static Transformer getStandardTransformer(boolean includeXMLDeclaration, final String encoding) throws TransformerConfigurationException {
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, includeXMLDeclaration ? "no" : "yes");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

		return transformer;
	}
	
	public static boolean createXmlDocument(String xmlFilename, IXMLContentProvider content)
	throws ParserConfigurationException, TransformerException {
		final Document document = createNewXMLDocument();
		final Element rootNode = document.createElement(content.getRootName());
		content.provide(document, rootNode);
		document.appendChild(rootNode);
		writeXMLDocumentToFile(document, new File(xmlFilename), content.getEncoding());        	        
        return true;			
	}	
	
	public static boolean loadXmlDocument(String xmlFilename, IXMLContentParser parser)
	throws IOException, ParserConfigurationException, SAXException {
        return loadXmlDocument(xmlFilename, parser, false);
	}		
	
	public static boolean loadXmlDocument(String xmlFilename, IXMLContentParser parser, boolean parseRoot) 
	throws IOException, ParserConfigurationException, SAXException {
		File f = new File(xmlFilename);
		if (!f.exists()) {
			throw new FileNotFoundException(xmlFilename);
		}		
        
		final Document document = loadXMLFromFile(f);		
		return parseDocument(document, parser, parseRoot);
	}

	public static boolean loadXml(String xml, IXMLContentParser parser)
	throws IOException, ParserConfigurationException, SAXException {
        return loadXml(xml, parser, false);
	}	
	
	public static boolean loadXml(String xml, IXMLContentParser parser,boolean parseRoot)
	throws IOException, ParserConfigurationException, SAXException {
    	final Document document = loadXMLFromString(xml);
    	return parseDocument(document, parser, parseRoot);
	}	
	
	private static boolean parseDocument(final Document document, final IXMLContentParser parser, final boolean parseRoot) {
		final NodeList nodes = document.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);
			if(node instanceof Element) {
				if (parseRoot) {
					return parser.parse((Element)node);
				}
				else if(node.getNodeName().equalsIgnoreCase(parser.getRootName()) && !parser.parse((Element)node)) {
					return false;
				}
			}
		}
        return true;
	}
	
	public static String createXml(IXMLContentProvider content)
	throws IOException, TransformerException, ParserConfigurationException  {
		final Document document = createNewXMLDocument();		
		final Element rootNode = document.createElement(content.getRootName());
		content.provide(document, rootNode);
		document.appendChild(rootNode);
		return outputXMLDocumentToString(document, content.getEncoding());
	}		
	
	public static Document createEmptyDocument(String fileName, String rootName)
	throws ParserConfigurationException, TransformerException {
		final Document document = createNewXMLDocument();		
		final Element rootNode = document.createElement(rootName);
		document.appendChild(rootNode);
		writeXMLDocumentToFile(document, new File(fileName), StandardCharsets.UTF_8.name());	
        return document;			
	}
	
	public static String getTempfile(String filename){		
		String arcadHome  = Utils.getHomeDirectory();	 
		if (arcadHome != null) {
			return Paths.get(arcadHome, "integrater", "tmp", filename).toString();
		}
	    return filename; 
	}
	
	public static File xslTransformation(String xmlFilename, String xlsFilename,boolean deleteSourcefile){
		String outFilename = null;
		if (xlsFilename.equals("")) { //$NON-NLS-1$
			//No transformation
			return new File(xmlFilename);
		} else {
			outFilename = getTempfile("trs"+Utils.computeId()+".xml");  //$NON-NLS-1$//$NON-NLS-2$
			File f = new File(outFilename);
			f.getParentFile().mkdirs();
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			Transformer transformer;
			try {
				transformer = transformerFactory.newTransformer(new StreamSource(xlsFilename));
				transformer.transform(
						new StreamSource(xmlFilename), 
						new StreamResult(new FileOutputStream(outFilename)));
				if (deleteSourcefile){
					f = new File(xmlFilename);
					f.delete();
				}
				return f;				
			}
			catch (Exception e) {
				MessageLogger.sendErrorMessage("XSL-Transformation",Utils.stackTrace(e));//$NON-NLS-1$
			}
			return null;
		}
	}
	
	public static Element getRoot(final Document document) {
		final NodeList nodes = document.getChildNodes();
		if(nodes != null) {
			for(int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if(node instanceof Element) {
					return (Element) node;
				}
			}
		}
		return null;
	}
	
	public static Element addElement(final Document document, final Node node, final String elementName) {
		final Element newElement = document.createElement(elementName);
		node.appendChild(newElement);
		return newElement;
	}
}
