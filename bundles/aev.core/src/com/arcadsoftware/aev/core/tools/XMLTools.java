package com.arcadsoftware.aev.core.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * @author MD, SJU
 *
 */
public class XMLTools {

	public static Element addElement(final Node node, final String tagName){
		final Element element = node.getOwnerDocument().createElement(tagName);
		node.appendChild(element);
		return element;
	}
	
	public static Document createNewXMLDocument() throws ParserConfigurationException {
		return getDocumentBuilderFactory().newDocumentBuilder().newDocument();
	}

	public static List<Node> getChildNodes(final Node node) {
		final List<Node> childNodes = new ArrayList<>();
		final NodeList nodeList = node.getChildNodes();
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				childNodes.add(nodeList.item(i));
			}
		}
		return childNodes;
	}

	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		return factory;
	}

	public static Integer getIntegerAttribute(final NamedNodeMap attributes, final String name) {
		final String stringValue = getStringAttribute(attributes, name);
		try {
			return Integer.parseInt(stringValue);
		} catch (final Exception e) {
		}

		return null;
	}

	public static <T> List<T> getNodes(final Node node, final String elementName, final Class<T> nodeClass) {
		return getNodesNamed(node, elementName) //
				.stream() //
				.filter(nodeClass::isInstance) //
				.map(nodeClass::cast) //
				.collect(Collectors.toList());
	}

	public static List<Node> getNodesNamed(final Node node, final String elementName) {
		return getChildNodes(node) //
				.stream() //
				.filter(n -> n.getNodeName().equals(elementName)) //
				.collect(Collectors.toList()); //
	}

	public static Element getRoot(final Document document) {
		return getChildNodes(document) //
				.stream() //
				.filter(Element.class::isInstance) //
				.map(Element.class::cast) //
				.findFirst() //
				.orElse(null);
	}

	protected static Transformer getStandardTransformer(final boolean includeXMLDeclaration, final String encoding)
			throws TransformerConfigurationException {
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

	public static String getStringAttribute(final NamedNodeMap attributes, final String name) {
		String value = null;
		final Node node = attributes.getNamedItem(name);
		if (node != null) {
			value = node.getNodeValue();
		}

		return value;
	}

	public static boolean isValidXMLCharacter(final char aChar) {
		return aChar == 0x9 || aChar == 0xA || aChar == 0xD || aChar >= 0x20 && aChar <= 0xD7FF
				|| aChar >= 0xE000 && aChar <= 0xFFFD || aChar >= 0x10000 && aChar <= 0x10FFFF;
	}

	public static Document loadXMLFromFile(final File file) throws FileNotFoundException {
		return loadXMLFromReader(new FileReader(file));
	}

	public static Document loadXMLFromReader(final Reader reader) {
		try (BufferedReader bufferedReader = new BufferedReader(reader)) {
			final DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
			final InputSource is = new InputSource(reader);
			return builder.parse(is);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document loadXMLFromString(final String xml) {
		return loadXMLFromReader(new StringReader(resolveSpecialEntities(xml)));
	}

	public static String outputXMLDocumentToString(final Document document, final String encoding) {
		try (StringWriter stringWriter = new StringWriter()) {
			final DOMSource source = new DOMSource(document);
			final StreamResult output = new StreamResult(stringWriter);
			getStandardTransformer(false, encoding).transform(source, output);

			return stringWriter.toString();
		} catch (final Exception e) {
			MessageManager.addAndPrintException(e);
			return null;
		}
	}

	protected static String resolveSpecialEntities(final String xmlString) {
		return xmlString.replace("&sol;", "/");
	}

	public static String stripNonValidXMLCharacters(final String xml) {
		final StringBuilder out = new StringBuilder();
		char current;

		if (!StringTools.isEmpty(xml)) {
			for (int i = 0; i < xml.length(); i++) {
				current = xml.charAt(i);
				if (isValidXMLCharacter(current)) {
					out.append(current);
				} else {
					System.out.println("Invalid XML character 0x" + Integer.toHexString(current) + " at position " + i
							+ " of \"" + xml + '"');
				}
			}
		}

		return out.toString();
	}

	public static void writeXMLDocumentToFile(final Document document, final File targetFile, final String encoding) {
		try {
			final DOMSource source = new DOMSource(document);
			final StreamResult sortie = new StreamResult(targetFile);
			getStandardTransformer(true, encoding).transform(source, sortie);
		} catch (final Exception e) {
			MessageManager.addAndPrintException(e);
		}
	}

	private XMLTools() {
		super();
	}
}