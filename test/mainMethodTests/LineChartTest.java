package tests;

import charts.LineChart;
import resources.Datacenter;

/**
 * Tests the LineChart class
 */
public class LineChartTest {
	public static void main(String[] args) {
		int datacenterId = 1;

		Datacenter datacenter = new Datacenter(datacenterId);

		double[][] powerUsage = datacenter.getPower(1);

		String url = LineChart.getChartUrl(1, powerUsage);
		System.out.println(url);
	}

}