package tests;

import java.net.UnknownHostException;

import resources.Host;
import charts.PowerGauge;

/**
 * Tests the PowerGauge class
 */
public class PowerGaugeTest {
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
		
		// Get current power for host
		double[][] powers = host.getPower(1);
		double curPower = powers[powers.length - 1][1];
		
		System.out.println("curPower = " + curPower);		
		System.out.println(PowerGauge.getChartUrl(curPower));
	}
}