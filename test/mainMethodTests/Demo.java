package tests;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import papillon.Api;
import xml.XmlParser;

/**
 * Tests basic API call and parsing XML
 */
public class Demo {

	/**
	 * A test method to demonstrate the use of Api.getBaseUri() for making API
	 * calls and custom functions for parsing the XML returned
	 */
	public static void main(String[] args) {
		// Get datacenters
		String url = Api.getBaseUri();
		NodeList datacenters = XmlParser.getElements(url, "datacenter");

		// For each datacenter, get power for last minute
		for (int i = 0; i < datacenters.getLength(); i++) {
			Element datacenter = (Element) datacenters.item(i);
			int datacenterID = XmlParser.getIntValue(datacenter, "id");
			long endTime = System.currentTimeMillis() / 1000L;
			long startTime = endTime - 60;
			url = Api.getBaseUri() + datacenterID + "/power?starttime="
					+ startTime + "&endtime=" + endTime;
			NodeList powers = XmlParser.getElements(url, "power");
			Element power = (Element) powers.item(0);
			double powerVal = XmlParser.getDoubleValue(power, "power");
			System.out.println("Last power reading for "
					+ XmlParser.getTextValue(datacenter, "name") + " = " + powerVal);
		}

	}

}