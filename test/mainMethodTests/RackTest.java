package tests;

import resources.Rack;

/**
 * Tests methods associated with the Rack resource
 */
public class RackTest {

	public static void main(String[] args) {
		// Set parameters to identify rack
		int datacenterId = 1;
		int floorId = 1;
		int rackId = 1;
		
		// Construct rack object given parameters above
		Rack rack = new Rack(datacenterId, floorId, rackId);
		
		// Get rack data fields
		String name = rack.getName();
		String description = rack.getDescription();
		int pdu = rack.getPdu();
		double xAxis = rack.getXAxis();
		double yAxis = rack.getYAxis();
				
		// Print rack data fields
		System.out.println("Rack ID: " + rackId);
		System.out.println("Floor ID: " + floorId);
		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("PDU: " + pdu);
		System.out.println("xAxis: " + xAxis);
		System.out.println("yAxis: " + yAxis);
		System.out.println();
		
		// Get power usage for last 1 hour
		int hours = 1;
		double[][] powerUsage = rack.getPower(hours);
		System.out.println(powerUsage.length + " entries");
		for (int i = 0; i < powerUsage.length; i++) {
			System.out.println("[" + (long) powerUsage[i][0] + "]["
					+ powerUsage[i][1] + "]");
		}

	}

}