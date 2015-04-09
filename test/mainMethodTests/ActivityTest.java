package tests;

import java.net.UnknownHostException;

import resources.Host;

/**
 * Tests the Host.getActivity method
 */
public class ActivityTest {
	public static void main(String[] args) {
		// Define host
		int datacenterId = 1;
		int floorId = 1;
		int rackId = 1;
		int hostId = 1;

		// Construct host object
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// Get timestamp of last activity for host
		double[][] powers = host.getPower(1);
		double maxTimestamp = powers[0][0];
		for (int i = 1; i < powers.length; i++) {
			if (powers[i][0] > maxTimestamp) {
				maxTimestamp = powers[i][0];
			}
		}
		long timestamp = (long) maxTimestamp;
		
		// Get activity for host at timestamp
		String[] activity = host.getActivity(timestamp);
		
		// Display
		System.out.println("[0]  Activity ID: " + activity[0]);
		System.out.println("[1]  Timestamp: " + activity[1]);
		System.out.println("[2]  All apps: " + activity[2]);
		System.out.println("[3]  App 1 ID: " + activity[3]);
		System.out.println("[4]  App 1 Name: " + activity[4]);
		System.out.println("[5]  App 1 CPU: " + activity[5]);
		System.out.println("[6]  App 2 ID: " + activity[6]);
		System.out.println("[7]  App 2 Name: " + activity[7]);
		System.out.println("[8]  App 2 CPU: " + activity[8]);
		System.out.println("[9]  App 3 ID: " + activity[9]);
		System.out.println("[10] App 3 Name: " + activity[10]);
		System.out.println("[11] App 3 CPU: " + activity[11]);
	}

}