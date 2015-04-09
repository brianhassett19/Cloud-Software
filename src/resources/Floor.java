package resources;

import interfaces.Resource;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import charts.LineChart;
import papillon.Api;
import power.Power;
import xml.XmlParser;

/**
 * Represents a floor in a datacenter
 */
public class Floor implements Resource {
	/** The id of the floor */
	private int id;

	/** The id of the datacenter in which the floor is located */
	private int datacenterId;

	/** The name of the floor */
	private String name;

	/** A description of the floor */
	private String description;

	/** The x-coordinate of the floor's location */
	private double xAxis;

	/** The y-coordinate of the floor's location */
	private double yAxis;

	/** URL of chart showing floor power profile for the last hour */
	private String oneHourLineChart;

	/** URL of chart showing floor power profile for the last twelve hours */
	private String twelveHourLineChart;

	/** URL of chart showing floor power profile for the last day */
	private String oneDayLineChart;

	/** List of racks on the floor */
	private ArrayList<Rack> racks = new ArrayList<Rack>();

	/** Base URI for API calls */
	private static String baseUri = Api.getBaseUri();

	/**
	 * Constructs a floor object
	 * 
	 * @param datacenterId
	 *            the id of the datacenter in which the floor is located
	 * @param floorId
	 *            the id of the floor
	 */
	public Floor(int datacenterId, int floorId) {
		// Set IDs
		this.id = floorId;
		this.datacenterId = datacenterId;

		// Set other values
		setProperties();

		// Get racks for floor
		String url = baseUri + datacenterId + "/floors/" + id + "/racks";
		NodeList allRacks = XmlParser.getElements(url, "rack");
		for (int i = 0; i < allRacks.getLength(); i++) {
			Element rack = (Element) allRacks.item(i);
			racks.add(new Rack(this.datacenterId, this.id, XmlParser
					.getIntValue(rack, "id")));
		}
	}

	/**
	 * Sets properties of the floor object
	 */
	private void setProperties() {
		// Construct URL for API call to retrieve other values
		String url = baseUri + datacenterId + "/floors/" + id;

		// Get data
		Element floor = XmlParser.getRootElement(url);

		// Set values
		this.name = XmlParser.getTextValue(floor, "name");
		this.description = XmlParser.getTextValue(floor, "description");
		this.xAxis = XmlParser.getDoubleValue(floor, "xAxis");
		this.yAxis = XmlParser.getDoubleValue(floor, "yAxis");

		// Get URL strings for power/time line charts
		this.oneHourLineChart = LineChart.getChartUrl(1, getPower(1));
		this.twelveHourLineChart = LineChart.getChartUrl(12, getPower(12));
		this.oneDayLineChart = LineChart.getChartUrl(24, getPower(24));
	}

	public void refreshData() {
		// Apply up-to-date floor information
		setProperties();

		// Get up-to-date list of racks
		String url = baseUri + datacenterId + "/floors/" + id + "/racks";
		NodeList racksUpdated = XmlParser.getElements(url, "rack");

		// Create array of rack IDs
		int[] newIds = new int[racksUpdated.getLength()];
		for (int i = 0; i < newIds.length; i++) {
			Element rack = (Element) racksUpdated.item(i);
			newIds[i] = XmlParser.getIntValue(rack, "id");
		}

		// Delete from ArrayList any out-of-date racks
		for (int i = 0; i < racks.size(); i++) {
			// Set flag indicating whether object is still valid
			boolean stillValid = false;

			// For each rack object, check that ID is still in updated list
			int idForCheck = racks.get(i).getId();
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
				racks.remove(i);
			}
		}

		// Refresh data for remaining racks
		for (int i = 0; i < racks.size(); i++) {
			racks.get(i).refreshData();
		}

		// Add any newly registered racks to ArrayList
		for (int i = 0; i < newIds.length; i++) {
			// Set flag to indicate whether object found
			boolean found = false;

			// For each ID in the updated list, check whether there is already a
			// corresponding object
			for (int j = 0; j < racks.size(); j++) {
				// If matching object found, update flag
				if (newIds[i] == racks.get(j).getId()) {
					found = true;
					break;
				} else {
					continue;
				}
			}

			// If not found, add new object to list
			if (!found) {
				racks.add(new Rack(datacenterId, id, newIds[i]));
			}
		}
	}

	public double[][] getPower(int hours) {
		return Power.getPower(datacenterId, id, hours);
	}

	public double getCurrentPower() {
		double[][] power = getPower(0);
		return power[0][1];
	}

	public int getId() {
		return this.id;
	}

	/**
	 * Returns the id of the datacenter in which the floor is located
	 * 
	 * @return the id of the datacenter in which the floor is located
	 */
	public int getDatacenterId() {
		return this.datacenterId;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
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
	 * Returns an arraylist of racks on the floor
	 * 
	 * @return an arraylist of racks on the floor
	 */
	public ArrayList<Rack> getRacks() {
		return racks;
	}

	/**
	 * Returns a Rack object based on its ID
	 * 
	 * @param rackId the id of the rack to be returned
	 * @return the rack associated with the id
	 */
	public Rack getRackById(int rackId) {
		Rack rack = null;
		for (int i = 0; i < racks.size(); i++) {
			if (racks.get(i).getId() != rackId) {
				continue;
			}
			rack = racks.get(i);
			break;
		}
		return rack;
	}

	public String toString() {
		return getName() + " [Datacenter " + datacenterId + " : Floor " + id
				+ "]";
	}

}