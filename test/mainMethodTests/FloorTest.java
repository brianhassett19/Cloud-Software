package tests;

import resources.Floor;

/**
 * Tests methods associated with the Flor resource
 */
public class FloorTest {

	public static void main(String[] args) {
		// Set parameters to identify floor
		int datacenterId = 1;
		int floorId = 1;

		// Construct floor object given parameters above
		Floor floor = new Floor(datacenterId, floorId);

		// Get floor data fields
		String name = floor.getName();
		String description = floor.getDescription();
		double xAxis = floor.getXAxis();
		double yAxis = floor.getYAxis();

		// Print floor data fields
		System.out.println("Floor ID: " + floorId);
		System.out.println("Datacenter ID: " + datacenterId);
		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("xAxis: " + xAxis);
		System.out.println("yAxis: " + yAxis);
		System.out.println();

		// Get power usage for last 1 hour
		int hours = 1;
		double[][] powerUsage = floor.getPower(hours);
		System.out.println(powerUsage.length + " entries");
		for (int i = 0; i < powerUsage.length; i++) {
			System.out.println("[" + (long) powerUsage[i][0] + "]["
					+ powerUsage[i][1] + "]");
		}

	}

}