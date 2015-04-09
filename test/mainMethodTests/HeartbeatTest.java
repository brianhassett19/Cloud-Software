package tests;

import java.util.ArrayList;

import resources.Datacenter;
import resources.Host;

/**
 * Tests the Datacenter.heartbeat method
 */
public class HeartbeatTest {

	public static void main(String[] args) {
		// Create Datacenter object
		Datacenter datacenter = new Datacenter(1);
		
		// Get list of inactive hosts
		ArrayList<Host> inactive = datacenter.heartbeat();
		
		// Display inactive hosts
		System.out.println(inactive.size() + " inactive hosts:");
		System.out.println(inactive.toString());

	}

}