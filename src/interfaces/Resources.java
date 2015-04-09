package interfaces;

/**
 * Defines methods common to all resources (Datacenter, Floor, Rack, Host)
 */
public interface Resource {
	/**
	 * Returns the ID of the resource
	 * 
	 * @return the id of the resource
	 */
	public int getId();

	/**
	 * Returns the name of the resource
	 * 
	 * @return the name of the resource
	 */
	public String getName();

	/**
	 * Returns a description of the resource
	 * 
	 * @return a string describing the resource
	 */
	public String getDescription();

	/**
	 * Returns a string representation of the resource
	 * 
	 * @return a string representation of the resource
	 */
	public String toString();

	/**
	 * Returns a two-dimensional array of power and time values for the resource
	 * for a specified number of hours
	 * 
	 * @param hours
	 *            the number of hours for which power readings are required
	 * @return a two-dimensional array of power and time values
	 * 
	 */
	public double[][] getPower(int hours);

	/**
	 * Returns power for the resource for the last minute.
	 * 
	 * @return the power value for the resource for the last minute
	 * 
	 */
	public double getCurrentPower();

	/**
	 * Updates data for the resource.
	 */
	public void refreshData();

	/**
	 * Returns URL of chart showing resource power profile for the last hour
	 * 
	 * @return URL of chart showing resource power profile for the last hour
	 */
	public String getOneHourLineChart();

	/**
	 * Returns URL of chart showing resource power profile for the last twelve
	 * hours
	 * 
	 * @return URL of chart showing resource power profile for the last twelve
	 *         hours
	 */
	public String getTwelveHourLineChart();

	/**
	 * Returns URL of chart showing resource power profile for the last day
	 * 
	 * @return URL of chart showing resource power profile for the last day
	 */
	public String getOneDayLineChart();

}