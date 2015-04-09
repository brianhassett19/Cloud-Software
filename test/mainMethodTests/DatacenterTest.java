package tests;

import resources.Datacenter;

/**
 * Tests methods associated with the Datacenter resource
 */
public class DatacenterTest {

	public static void main(String[] args) {
		// Set parameters to identify datacenter
		int datacenterId = 1;

		// Construct datacenter object given parameters above
		Datacenter datacenter = new Datacenter(datacenterId);

		// Get datacenter data fields
		String name = datacenter.getName();
		String description = datacenter.getDescription();
		double latitude = datacenter.getLatitude();
		double longitude = datacenter.getLongitude();

		// Print datacenter data fields
		System.out.println("Datacenter ID: " + datacenterId);
		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("Latitude: " + latitude);
		System.out.println("Longitude: " + longitude);
		System.out.println();

		// Get power usage for last 1 hour
		int hours = 1;
		double[][] powerUsage = datacenter.getPower(hours);
		System.out.println(powerUsage.length + " entries");
		for (int i = 0; i < powerUsage.length; i++) {
			System.out.println("[" + (long) powerUsage[i][0] + "]["
					+ powerUsage[i][1] + "]");
		}
	}

}