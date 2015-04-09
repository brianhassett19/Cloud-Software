package power;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import papillon.Api;
import xml.XmlParser;

/**
 * A non-instantiable class providing a range of methods to return power
 * readings for various resources.
 */
public class Power {
	/**
	 * Private constructor prevents class being instantiated.
	 */
	private Power() {

	}

	/**
	 * Returns a two-dimensional (time and power) array for a datacenter over a
	 * specified timespan
	 *
	 * @param datacenterId
	 *            the id of the datacenter
	 * @param hours
	 *            the number of hours for which power readings are required
	 * @return a two-dimensional array of time and power values
	 */
	public static double[][] getPower(int datacenterId, int hours) {
		// Construct a string identifying the datacenter resource
		String resourcePath = "" + datacenterId;
		// Return power for the resource for the timespan
		return getPower(resourcePath, hours);
	}

	/**
	 * Returns a two-dimensional (time and power) array for a floor over a
	 * specified timespan
	 * 
	 * @param datacenterId
	 *            the id of the datacenter in which the floor is located
	 * @param floorId
	 *            the id of the floor
	 * @param hours
	 *            the number of hours for which power readings are required
	 * @return a two-dimensional array of time and power values
	 */
	public static double[][] getPower(int datacenterId, int floorId, int hours) {
		// Construct a string identifying the floor resource
		String resourcePath = datacenterId + "/floors/" + floorId;
		// Return power for the resource for the timespan
		return getPower(resourcePath, hours);
	}

	/**
	 * Returns a two-dimensional (time and power) array for a rack over a
	 * specified timespan
	 *
	 * @param datacenterId
	 *            the id of the datacenter in which the rack is located
	 * @param floorId
	 *            the id of the floor on which the rack is located
	 * @param rackId
	 *            the id of the rack
	 * @param hours
	 *            the number of hours for which power readings are required
	 * @return a two-dimensional array of time and power values
	 */
	public static double[][] getPower(int datacenterId, int floorId,
			int rackId, int hours) {
		// Construct a string identifying the rack resource
		String resourcePath = datacenterId + "/floors/" + floorId + "/racks/"
				+ rackId;
		// Return power for the resource for the timespan
		return getPower(resourcePath, hours);
	}

	/**
	 * Returns a two-dimensional (time and power) array for a host over a
	 * specified timespan
	 *
	 * @param datacenterId
	 *            the id of the datacenter in which the host is located
	 * @param floorId
	 *            the id of the floor on which the host is located
	 * @param rackId
	 *            the id of the rack in which the host is located
	 * @param hostId
	 *            the id of the host
	 * @param hours
	 *            the number of hours for which power readings are required
	 * @return a two-dimensional array of time and power values
	 */
	public static double[][] getPower(int datacenterId, int floorId,
			int rackId, int hostId, int hours) {
		// Construct a string identifying the host resource
		String resourcePath = datacenterId + "/floors/" + floorId + "/racks/"
				+ rackId + "/hosts/" + hostId;
		// Return power for the resource for the timespan
		return getPower(resourcePath, hours);
	}

	/**
	 * Returns a two-dimensional (time and power) array for a resource over a
	 * specified timespan from an appropriate URL
	 *
	 * @param resourcePath
	 *            a string representing the path to the api data for the
	 *            resource
	 * @param hours
	 *            the number of hours for which power readings are required. If
	 *            0, the reading for the last minute is returned.
	 * @return a two-dimensional array of time and power values
	 */
	public static double[][] getPower(String resourcePath, int hours) {
		// Set start and end time in seconds (note 3600 seconds per hour)
		long endTime = System.currentTimeMillis() / 1000L;
		long startTime;
		if (hours == 0) {
			// Set startTime to one minute ago
			startTime = endTime - 60;
		} else {
			// Set startTime to specified no. of hours ago
			startTime = endTime - (3600 * hours);
		}

		// Construct URL for API call to retrieve power values between these
		// times
		String baseUri = Api.getBaseUri();
		String url = baseUri + resourcePath + "/power?starttime=" + startTime
				+ "&endtime=" + endTime;

		// Declare XPath expression to select only power nodes that are
		// immediate children of the root node (not other power nodes)
		String xPathExpression = "//powers/power";

		// Get power readings and store in array
		NodeList powers = XmlParser.getXPathElements(url, xPathExpression);
		double[][] readings = new double[powers.getLength()][2];
		for (int i = 0; i < powers.getLength(); i++) {
			Element reading = (Element) powers.item(i);
			// Add timestamp and power value to array
			readings[i][0] = XmlParser.getLongValue(reading, "timeStamp");
			readings[i][1] = XmlParser.getDoubleValue(reading, "power") * 60;
		}

		return readings;
	}
}