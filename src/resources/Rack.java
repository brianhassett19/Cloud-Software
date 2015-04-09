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
 * Represents a server rack in a datacenter
 */
public class Rack implements Resource {
	/** The ID of the rack */
	private int id;

	/** The ID of the datacenter in which the rack is located */
	private int datacenterId;

	/** The ID of the floor on which the rack is located */
	private int floorId;

	/** The name of the rack */
	private String name;

	/** A description of the rack */
	private String description;

	/** The power distribution units for the rack */
	private int pdu;

	/** The x-coordinate of the rack's location */
	private double xAxis;

	/** The y-coordinate of the rack's location */
	private double yAxis;

	/** URL of chart showing rack power profile for the last hour */
	private String oneHourLineChart;

	/** URL of chart showing rack power profile for the last twelve hours */
	private String twelveHourLineChart;

	/** URL of chart showing rack power profile for the last day */
	private String oneDayLineChart;

	/** List of hosts on the rack */
	private ArrayList<Host> hosts = new ArrayList<Host>();

	/** Base URI for API calls */
	private static String baseUri = Api.getBaseUri();

	/**
	 * Constructs a Rack object
	 * 
	 * @param datacenterId
	 *            the id of the datacenter in which the rack is located
	 * @param floorId
	 *            the id of the floor on which the rack is located
	 * @param rackId
	 *            the id of the rack
	 */
	public Rack(int datacenterId, int floorId, int rackId) {
		// Set IDs
		this.id = rackId;
		this.datacenterId = datacenterId;
		this.floorId = floorId;

		// Set other values
		setProperties();

		// Get hosts for rack
		String url = baseUri + datacenterId + "/floors/" + floorId + "/racks/"
				+ rackId + "/hosts";
		NodeList allHosts = XmlParser.getElements(url, "host");
		for (int i = 0; i < allHosts.getLength(); i++) {
			Element host = (Element) allHosts.item(i);
			try {
				hosts.add(new Host(this.datacenterId, this.floorId, this.id,
						XmlParser.getIntValue(host, "id")));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets properties of the rack object
	 */
	private void setProperties() {
		// Construct URL for API call
		String url = baseUri + datacenterId + "/floors/" + floorId + "/racks/"
				+ id;

		// Get rack data
		Element rack = XmlParser.getRootElement(url);

		// Set values
		this.name = XmlParser.getTextValue(rack, "name");
		this.description = XmlParser.getTextValue(rack, "description");
		this.pdu = XmlParser.getIntValue(rack, "pdu");
		this.xAxis = XmlParser.getDoubleValue(rack, "xAxis");
		this.yAxis = XmlParser.getDoubleValue(rack, "yAxis");

		// Get URL strings for power/time line charts
		this.oneHourLineChart = LineChart.getChartUrl(1, getPower(1));
		this.twelveHourLineChart = LineChart.getChartUrl(12, getPower(12));
		this.oneDayLineChart = LineChart.getChartUrl(24, getPower(24));
	}

	public void refreshData() {
		// Apply up-to-date rack information
		setProperties();

		// Get up-to-date list of hosts
		String url = baseUri + datacenterId + "/floors/" + floorId + "/racks/"
				+ id + "/hosts";
		NodeList hostsUpdated = XmlParser.getElements(url, "host");

		// Create array of host IDs
		int[] newIds = new int[hostsUpdated.getLength()];
		for (int i = 0; i < newIds.length; i++) {
			Element host = (Element) hostsUpdated.item(i);
			newIds[i] = XmlParser.getIntValue(host, "id");
		}

		// Delete from ArrayList any out-of-date hosts
		for (int i = 0; i < hosts.size(); i++) {
			// Set flag indicating whether object is still valid
			boolean stillValid = false;

			// For each rack object, check that ID is still in updated list
			int idForCheck = hosts.get(i).getId();
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
				hosts.remove(i);
			}
		}

		// Refresh data for remaining hosts
		for (int i = 0; i < hosts.size(); i++) {
			hosts.get(i).refreshData();
		}

		// Add any newly registered hosts to ArrayList
		for (int i = 0; i < newIds.length; i++) {
			// Set flag to indicate whether object found
			boolean found = false;

			// For each ID in the updated list, check whether there is already a
			// corresponding object
			for (int j = 0; j < hosts.size(); j++) {
				// If matching object found, update flag
				if (newIds[i] == hosts.get(j).getId()) {
					found = true;
					break;
				} else {
					continue;
				}
			}

			// If not found, add new object to list
			if (!found) {
				try {
					hosts.add(new Host(datacenterId, floorId, id, newIds[i]));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public double[][] getPower(int hours) {
		return Power.getPower(datacenterId, floorId, id, hours);
	}

	public double getCurrentPower() {
		double[][] power = getPower(0);
		return power[0][1];
	}

	public int getId() {
		return this.id;
	}

	/**
	 * Returns the id of the datacenter in which the rack is located
	 * 
	 * @return the id of the datacenter in which the rack is located
	 */
	public int getDatacenterId() {
		return this.datacenterId;
	}

	/**
	 * Returns the id of the floor on which the rack is located
	 * 
	 * @return the id of the floor on which the rack is located
	 */
	public int getFloorId() {
		return this.floorId;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the power distribution units for the rack
	 * 
	 * @return the power distribution units for the rack
	 */
	public int getPdu() {
		return this.pdu;
	}

	/**
	 * Returns the x-coordinate of the floor's location
	 * 
	 * @return the x-coordinate of the floor's location
	 */
	public double getXAxis() {
		return this.xAxis;
	}

	/**
	 * Returns the y-coordinate of the floor's location
	 * 
	 * @return the y-coordinate of the floor's location
	 */
	public double getYAxis() {
		return this.yAxis;
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
	 * Returns an array list of servers on the rack
	 * 
	 * @return an array list of servers on the rack
	 */
	public ArrayList<Host> getHosts() {
		return hosts;
	}

	/**
	 * Return a Host object based on its id
	 * 
	 * @param hostId
	 *            the id of the host to be returned
	 * @return the host associated with the id
	 */
	public Host getHostById(int hostId) {
		Host host = null;
		for (int i = 0; i < hosts.size(); i++) {
			if (hosts.get(i).getId() != hostId) {
				continue;
			}
			host = hosts.get(i);
			break;
		}
		return host;
	}

	public String toString() {
		return getName() + " [Datacenter " + datacenterId + " : Floor "
				+ floorId + " : Rack " + id + "]";
	}

}