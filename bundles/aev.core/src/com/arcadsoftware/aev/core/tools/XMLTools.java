package com.arcadsoftware.aev.core.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * @author MD, SJU
 *
 */
public class XMLTools {

	private XMLTools() {
		super();
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

	public static Document loadXMLFromString(String xml) {
		return loadXMLFromReader(new StringReader(resolveSpecialEntities(xml)));
	}

	public static Document loadXMLFromFile(File file) throws FileNotFoundException {
		return loadXMLFromReader(new FileReader(file));
	}

	public static Document loadXMLFromReader(Reader reader) {
		try(BufferedReader bufferedReader = new BufferedReader(reader)){			
			final DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
			final InputSource is = new InputSource(reader);
			return builder.parse(is);
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}

	protected static String resolveSpecialEntities(String xmlString) {
		return xmlString.replace("&sol;", "/");
	}

	public static void writeXMLDocumentToFile(Document document, File targetFile, String encoding) {
		try {
			DOMSource source = new DOMSource(document);
			StreamResult sortie = new StreamResult(targetFile);
			getStandardTransformer(true, encoding).transform(source, sortie);
		}
		catch (Exception e) {
			MessageManager.addAndPrintException(e);
		}
	}

	public static String outputXMLDocumentToString(final Document document, final String encoding) {
		try (StringWriter stringWriter = new StringWriter()){
			DOMSource source = new DOMSource(document);			
			StreamResult output = new StreamResult(stringWriter);
			getStandardTransformer(false, encoding).transform(source, output);

			return stringWriter.toString();
		}
		catch (Exception e) {
			MessageManager.addAndPrintException(e);
			return null;
		}
	}

	protected static Transformer getStandardTransformer(boolean includeXMLDeclaration, final String encoding) throws TransformerConfigurationException {
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

	public static String stripNonValidXMLCharacters(String xml) {
		StringBuilder out = new StringBuilder();
		char current;

		if (!StringTools.isEmpty(xml)) {
			for (int i = 0; i < xml.length(); i++) {
				current = xml.charAt(i);
				if (isValidXMLCharacter(current)) {
					out.append(current);
				} else {
					System.out.println("Invalid XML character 0x" + Integer.toHexString(current) + " at position " + i + " of \"" + xml + '"');
				}
			}
		}

		return out.toString();
	}

	public static boolean isValidXMLCharacter(char aChar) {
		return (aChar == 0x9) || (aChar == 0xA) || (aChar == 0xD) || ((aChar >= 0x20) && (aChar <= 0xD7FF))
				|| ((aChar >= 0xE000) && (aChar <= 0xFFFD)) || ((aChar >= 0x10000) && (aChar <= 0x10FFFF));
	}

	public static String getStringAttribute(NamedNodeMap attributes, String name) {
		String value = null;
		Node node = attributes.getNamedItem(name);
		if (node != null) {
			value = node.getNodeValue();
		}

		return value;
	}

	public static Integer getIntegerAttribute(NamedNodeMap attributes, String name) {
		String stringValue = getStringAttribute(attributes, name);
		try {
			return Integer.parseInt(stringValue);
		} catch (Exception e) {
		}

		return null;
	}	
}