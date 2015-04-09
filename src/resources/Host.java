package resources;

import interfaces.Resource;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import papillon.Api;
import power.Power;
import xml.XmlParser;
import charts.ActivityPieChart;
import charts.LineChart;

/**
 * Represents a server in a datacenter
 */
public class Host implements Resource {
	/** The ID of the host */
	private int id;

	/** The ID of the datacenter in which the host is located */
	private int datacenterId;

	/** The ID of the floor on which the host is located */
	private int floorId;

	/** The ID of the rack in which the host is located */
	private int rackId;

	/** The ID of the power model group for the host */
	private int modelGroupId;

	/** The name of the host */
	private String name;

	/** A description of the host */
	private String description;

	/** The type of the host */
	private String hostType;

	/** The IP address of the host */
	private InetAddress ipAddress;

	/** The number of processors in the host */
	private int processorCount;

	/** The number of virtual machines running on the host */
	private int vmCount;

	/** URL of chart showing host power profile for the last hour */
	private String oneHourLineChart;

	/** URL of chart showing host power profile for the last twelve hours */
	private String twelveHourLineChart;

	/** URL of chart showing host power profile for the last day */
	private String oneDayLineChart;

	/** URL of chart showing top three host applications for the last minute */
	private String activityChart;

	/** Base URI for API calls */
	private static String baseUri = Api.getBaseUri();

	/**
	 * Constructs a host object
	 * 
	 * @param datacenterId
	 *            the id of the datacenter in which the host is located
	 * @param floorId
	 *            the id of the foor on which the host is located
	 * @param rackId
	 *            the id of the rack in which the host is located
	 * @param hostId
	 *            the id of the host
	 * @throws UnknownHostException
	 */
	public Host(int datacenterId, int floorId, int rackId, int hostId)
			throws UnknownHostException {
		// Set IDs
		this.id = hostId;
		this.datacenterId = datacenterId;
		this.floorId = floorId;
		this.rackId = rackId;

		// Set other values
		setProperties();
	}

	/**
	 * Sets properties of the host object
	 * 
	 * @throws UnknownHostException
	 */
	private void setProperties() throws UnknownHostException {
		// Construct URL for API call
		String url = baseUri + datacenterId + "/floors/" + floorId + "/racks/"
				+ rackId + "/hosts/" + id;

		// Get host data
		Element host = XmlParser.getRootElement(url);

		// Set values
		this.modelGroupId = XmlParser.getIntValue(host, "modelGroupId");
		this.name = XmlParser.getTextValue(host, "name");
		this.description = XmlParser.getTextValue(host, "description");
		this.hostType = XmlParser.getTextValue(host, "hostType");
		this.ipAddress = InetAddress.getByName(XmlParser.getTextValue(host,
				"IPAddress"));
		this.processorCount = XmlParser.getIntValue(host, "processorCount");
		this.vmCount = XmlParser.getIntValue(host, "VMCount");

		// Get URL strings for charts
		this.oneHourLineChart = LineChart.getChartUrl(1, getPower(1));
		this.twelveHourLineChart = LineChart.getChartUrl(12, getPower(12));
		this.oneDayLineChart = LineChart.getChartUrl(24, getPower(24));
		double[][] power = getPower(1);
		long timestamp = (long) power[power.length - 1][0];
		this.activityChart = ActivityPieChart
				.getChartUrl(getActivity(timestamp));
	}

	public void refreshData() {
		// Get up-to-date host data
		try {
			setProperties();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public double[][] getPower(int hours) {
		return Power.getPower(datacenterId, floorId, rackId, id, hours);
	}

	public double getCurrentPower() {
		double[][] power = getPower(0);
		return power[0][1];
	}

	/**
	 * Returns a string array of the host's activity at a given timestamp for
	 * purposes of constructing pie chart
	 * 
	 * @param timestamp
	 *            a long value representing the time at which activity was
	 *            recorded
	 * @return a string array of the host's activity
	 */
	public String[] getActivity(long timestamp) {
		String[] activityData = new String[12];

		// Construct URL for API call to retrieve values
		String url = baseUri + datacenterId + "/floors/" + floorId + "/racks/"
				+ rackId + "/hosts/" + id + "/activity?starttime=" + timestamp
				+ "&endtime=" + (timestamp + 1); // endtime must be greater than
													// starttime, so add 1

		// Get activity
		NodeList activities = XmlParser.getElements(url, "activity");
		Element activity = (Element) activities.item(0);

		// Add activity data to array
		activityData[0] = XmlParser.getTextValue(activity, "id");
		activityData[1] = XmlParser.getTextValue(activity, "timeStamp");
		activityData[2] = XmlParser.getTextValue(activity, "allApps");

		// Get app data and add to array
		String xPathExpression = "//activities/activity/apps/app";
		NodeList apps = XmlParser.getXPathElements(url, xPathExpression);
		for (int i = 0; i < apps.getLength(); i++) {
			Element app = (Element) apps.item(i);
			activityData[3 * i + 3] = XmlParser.getTextValue(app, "appId");
			activityData[3 * i + 4] = XmlParser.getTextValue(app, "name");
			activityData[3 * i + 5] = XmlParser.getTextValue(app, "cpu");
		}

		return activityData;
	}

	public int getId() {
		return this.id;
	}

	/**
	 * Returns the id of the datacenter in which the host is located
	 * 
	 * @return the id of the datacenter in which the host is located
	 */
	public int getDatacenterId() {
		return this.datacenterId;
	}

	/**
	 * Returns the id of the floor on which the host is located
	 * 
	 * @return the id of the floor on which the host is located
	 */
	public int getFloorId() {
		return this.floorId;
	}

	/**
	 * Returns the id of the rack in which the host is located
	 * 
	 * @return the id of the rack in which the host is located
	 */
	public int getRackId() {
		return this.rackId;
	}

	/**
	 * Returns the id of the power model group for the host
	 * 
	 * @return the id of the power model group for the host
	 */
	public int getModelGroupId() {
		return this.modelGroupId;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the type of host
	 * 
	 * @return a string describing the type of host
	 */
	public String getHostType() {
		return this.hostType;
	}

	/**
	 * Returns the IP address of the host
	 * 
	 * @return the ip address of the host
	 */
	public InetAddress getIpAddress() {
		return this.ipAddress;
	}

	/**
	 * Returns the number of processors in the host
	 * 
	 * @return the number of processors in the host
	 */
	public int getProcessorCount() {
		return this.processorCount;
	}

	/**
	 * Returns the number of virtual machines running on the host
	 * 
	 * @return the number of virtual machines running on the host
	 */
	public int getVmCount() {
		return this.vmCount;
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
	 * Returns URL of chart showing top three host applications for the last
	 * minute
	 * 
	 * @return url of chart showing top three host applications for the last
	 *         minute
	 */
	public String getActivityChart() {
		return this.activityChart;
	}

	public String toString() {
		return getName() + " [Datacenter " + datacenterId + " : Floor "
				+ floorId + " : Rack " + rackId + " : Host " + id + "]";
	}

}