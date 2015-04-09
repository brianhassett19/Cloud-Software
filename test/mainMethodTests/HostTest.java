package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;

import resources.Host;

/**
 * Tests methods associated with the Host resource
 */
public class HostTest {

	public static void main(String[] args) {
		// Set parameters to identify host
		int datacenterId = 1;
		int floorId = 1;
		int rackId = 1;
		int hostId = 1;

		// Construct host object given parameters above
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Get host data fields
		int modelGroupId = host.getModelGroupId();
		String name = host.getName();
		String description = host.getDescription();
		String hostType = host.getHostType();
		InetAddress ipAddress = host.getIpAddress();
		int processorCount = host.getProcessorCount();
		int vmCount = host.getVmCount();

		// Print host data fields
		System.out.println("Host ID: " + hostId);
		System.out.println("Rack ID: " + rackId);
		System.out.println("Model Group ID: " + modelGroupId);
		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("Type: " + hostType);
		System.out.println("IP Address: " + ipAddress);
		System.out.println("Processors: " + processorCount);
		System.out.println("VMs: " + vmCount);
		System.out.println();

		// Get power usage for last 1 hour
		int hours = 1;
		double[][] powerUsage = host.getPower(hours);
		System.out.println(powerUsage.length + " entries");
		for (int i = 0; i < powerUsage.length; i++) {
			System.out.println("[" + (long) powerUsage[i][0] + "]["
					+ powerUsage[i][1] + "]");
		}

	}
}