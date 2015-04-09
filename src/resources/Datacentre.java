package resources;

import interfaces.Resource;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import charts.LineChart;
import papillon.Api;
import power.Power;
import xml.XmlParser;

/**
 * Represents a datacenter
 */
public class Datacenter implements Resource {
	/** The id of the datacenter */
	private int id;

	/** The name of the datacenter */
	private String name;

	/** A description of the datacenter */
	private String description;

	/** The latitude of the datacenter location */
	private double latitude;

	/** The longitude of the datacenter location */
	private double longitude;

	/** URL of chart showing datacenter power profile for the last hour */
	private String oneHourLineChart;

	/** URL of chart showing datacenter power profile for the last twelve hours */
	private String twelveHourLineChart;

	/** URL of chart showing datacenter power profile for the last day */
	private String oneDayLineChart;

	/** List of floors in datacenter */
	private ArrayList<Floor> floors = new ArrayList<Floor>();

	/** Base URI for API calls */
	private static String baseUri = Api.getBaseUri();

	/**
	 * Constructs a datacenter object
	 * 
	 * @param datacenterId
	 *            the id of the datacenter
	 */
	public Datacenter(int datacenterId) {
		// Set ID
		this.id = datacenterId;

		// Set other values
		setProperties();

		// Get floors
		String url = baseUri + id + "/allfloors";
		NodeList allFloors = XmlParser.getElements(url, "floorExtended");
		for (int i = 0; i < allFloors.getLength(); i++) {
			Element floor = (Element) allFloors.item(i);
			floors.add(new Floor(id, XmlParser.getIntValue(floor, "floorId")));
		}

	}

	/**
	 * Sets properties of the datacenter object
	 */
	private void setProperties() {
		// Construct URL for API call
		String url = baseUri + id;

		// Get data
		Element datacenter = XmlParser.getRootElement(url);

		// Set values
		this.name = XmlParser.getTextValue(datacenter, "name");
		this.description = XmlParser.getTextValue(datacenter, "description");
		this.latitude = XmlParser.getDoubleValue(datacenter, "latitude");
		this.longitude = XmlParser.getDoubleValue(datacenter, "longitude");

		// Get URL strings for power/time line charts
		this.oneHourLineChart = LineChart.getChartUrl(1, getPower(1));
		this.twelveHourLineChart = LineChart.getChartUrl(12, getPower(12));
		this.oneDayLineChart = LineChart.getChartUrl(24, getPower(24));
	}

	public void refreshData() {
		// Apply up-to-date datacenter information
		setProperties();

		// Get up-to-date list of floors
		String url = baseUri + id + "/allfloors";
		NodeList floorsUpdated = XmlParser.getElements(url, "floorExtended");

		// Create array of floor IDs
		int[] newIds = new int[floorsUpdated.getLength()];
		for (int i = 0; i < newIds.length; i++) {
			Element floor = (Element) floorsUpdated.item(i);
			newIds[i] = XmlParser.getIntValue(floor, "floorId");
		}

		// Delete from ArrayList any out-of-date floors
		for (int i = 0; i < floors.size(); i++) {
			// Set flag indicating whether object is still valid
			boolean stillValid = false;

			// For each floor object, check that ID is still in updated list
			int idForCheck = floors.get(i).getId();
			for (int j = 0; j < newIds.length; j++) {
				// If object's ID found in new IDs list, update flag
				if (idForCheck == newIds[j]) {
					stillValid = true;
					break;
				} else {
					continue;
				}
			}

			// If no longer valid, delete object
			if (!stillValid) {
				floors.remove(i);
			}
		}

		// Refresh data for remaining floors
		for (int i = 0; i < floors.size(); i++) {
			floors.get(i).refreshData();
		}

		// Add any newly registered floors to ArrayList
		for (int i = 0; i < newIds.length; i++) {
			// Set flag to indicate whether object found
			boolean found = false;

			// For each ID in the updated list, check whether there is already a
			// corresponding object
			for (int j = 0; j < floors.size(); j++) {
				// If matching object found, update flag
				if (newIds[i] == floors.get(j).getId()) {
					found = true;
					break;
				} else {
					continue;
				}
			}

			// If not found, add new object to list
			if (!found) {
				floors.add(new Floor(id, newIds[i]));
			}
		}
	}

	public double[][] getPower(int hours) {
		// Pass parameters to common method and return map
		return Power.getPower(id, hours);
	}

	public double getCurrentPower() {
		double[][] power = getPower(0);
		return power[0][1];
	}

	/**
	 * Returns an arraylist of objects representing hosts in the datacenter that
	 * have not communicated to Papillon master server in last 10 minutes
	 * 
	 * @return an arraylist of ojects representing non-communicating hosts
	 */
	public ArrayList<Host> heartbeat() {
		// Create ArrayList to hold inactive Host objects
		ArrayList<Host> inactiveHosts = new ArrayList<Host>();

		// Construct URL for API call
		String url = Api.getBaseUri() + id + "/heartbeat";

		// Get inactive hosts
		NodeList inactive = XmlParser.getElements(url, "host");

		// Getting floor ID of inactive hosts requires a further call (to
		// allhosts) as not included in heartbeat XML
		url = Api.getBaseUri() + id + "/allhosts";
		NodeList allHosts = XmlParser.getElements(url, "hostExtended");

		// Add inactive hosts to ArrayList
		for (int i = 0; i < inactive.getLength(); i++) {
			Element host = (Element) inactive.item(i);

			// Get IDs to identify host
			int rackId = XmlParser.getIntValue(host, "rackId");
			int hostId = XmlParser.getIntValue(host, "id");

			// Get floor ID of host, identified by its IP address
			int floorId = 0;
			for (int j = 0; j < allHosts.getLength(); j++) {
				Element extendedHost = (Element) allHosts.item(j);
				if (!XmlParser.getTextValue(host, "IPAddress").equals(
						XmlParser.getTextValue(extendedHost, "IPAddress"))) {
					continue;
				}
				floorId = XmlParser.getIntValue(extendedHost, "floorId");
				break;
			}

			// Add Host object
			try {
				inactiveHosts.add(new Host(id, floorId, rackId, hostId));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		return inactiveHosts;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the latitude of the datacenter location
	 * 
	 * @return the latitude of the datacenter location
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * Returns the longitude of the datacenter location
	 * 
	 * @return the longitude of the datacenter location
	 */
	public double getLongitude() {
		return this.longitude;
	}

	public String getOneHourLineChart() {
		return this.oneHourLineChart;
	}

	public String getTwelveHourLineChart() {
		return this.twelveHourLineChart;
	}

	public String getOneDayLineChart() {
		return this.oneDayLineChart;
	}

	/**
	 * Returns an arraylist of floors in the datacenter
	 * 
	 * @return an arraylist of floors in the datacenter
	 */
	public ArrayList<Floor> getFloors() {
		return floors;
	}

	/**
	 * Returns a Floor object based on its ID
	 * 
	 * @param floorId
	 *            the id of the floor to be returned
	 * @return the floor associated with the id
	 */
	public Floor getFloorById(int floorId) {
		Floor floor = null;
		for (int i = 0; i < floors.size(); i++) {
			if (floors.get(i).getId() != floorId) {
				continue;
			}
			floor = floors.get(i);
			break;
		}
		return floor;
	}

	public String toString() {
		return getName() + " [Datacenter " + id + "]";
	}

}