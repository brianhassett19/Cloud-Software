package xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A non-instantiable class providing methods to parse XML returned from the
 * Papillon API
 */
public class XmlParser {
	/**
	 * Private constructor prevents class being instantiated
	 */
	private XmlParser() {

	}

	/**
	 * Parses an XML resource and return a document.
	 * 
	 * @param url
	 *            a string representing the url of the xml resource to be parsed
	 * @return a document representation of the xml resource
	 */
	private static Document parse(String url) {
		// Get document builder factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		Document document = null;

		try {
			// Get document builder
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		try {
			// Parse XML to get DOM representation
			document = builder.parse(url);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * Returns the root element of an XML resource
	 * 
	 * @param url
	 *            a string representing the url of the xml resource to be parsed
	 * @return the root element of the resource
	 */
	public static Element getRootElement(String url) {
		// Get DOM representation of XML resource
		Document document = parse(url);

		// Return root element
		return document.getDocumentElement();
	}

	/**
	 * Returns a list of elements from an XML resource
	 * 
	 * @param url
	 *            a string representing the url of the xml resource to be parsed
	 * @param tagName
	 *            a string representing a tag in the xml resource
	 * @return a nodelist of elements from the xml resource
	 */
	public static NodeList getElements(String url, String tagName) {
		return getRootElement(url).getElementsByTagName(tagName);
	}

	/**
	 * Returns a list of elements from an XML resource using XPath
	 * 
	 * @param url
	 *            a string representing the url of the xml resource to be parsed
	 * @param xPathExpression
	 *            a string representing a path used to select xml nodes
	 * @return a nodelist of elements from the xml resource
	 */
	public static NodeList getXPathElements(String url, String xPathExpression) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xPath = xPathfactory.newXPath();
		NodeList nodes = null;
		try {
			nodes = (NodeList) xPath.compile(xPathExpression).evaluate(
					parse(url), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return nodes;
	}

	/**
	 * Returns the text content associated with an element and tag name
	 * 
	 * @param element
	 *            an element in an xml document
	 * @param tag
	 *            a string representing an xml tag
	 * @return the string value associated with the tag
	 */
	public static String getTextValue(Element element, String tag) {
		String textVal = null;
		NodeList nodes = element.getElementsByTagName(tag);
		Element e = (Element) nodes.item(0);
		textVal = e.getFirstChild().getNodeValue();
		return textVal;
	}

	/**
	 * Returns an int value associated with an element and tag name
	 * 
	 * @param element
	 *            an element in an xml document
	 * @param tag
	 *            a string representing an xml tag
	 * @return the value associated with the tag
	 */
	public static int getIntValue(Element element, String tag) {
		return Integer.parseInt(getTextValue(element, tag));
	}

	/**
	 * Returns a long value associated with an element and tag name
	 * 
	 * @param element
	 *            an element in an xml document
	 * @param tag
	 *            a string representing an xml tag
	 * @return the long value associated with the tag
	 */
	public static long getLongValue(Element element, String tag) {
		return Long.parseLong(getTextValue(element, tag));
	}

	/**
	 * Returns a double value associated with an element and tag name
	 * 
	 * @param element
	 *            an element in an xml document
	 * @param tag
	 *            a string representing an xml tag
	 * @return the double value associated with the tag
	 */
	public static double getDoubleValue(Element element, String tag) {
		return Double.parseDouble(getTextValue(element, tag));
	}

}