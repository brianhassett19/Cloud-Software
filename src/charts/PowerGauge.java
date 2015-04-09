package charts;


import static com.googlecode.charts4j.Color.BLACK;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.GoogleOMeter;

public class PowerGauge {
	
	/**
	 * Returns a Google-O-Meter with '0' and 'MAX' as the axis labels. An arrow 
	 * on the chart indicates the current power.
	 * 
	 * @param curPower
	 * @return A url representing the Google-O-Meter
	 */
	public static String getChartUrl(double curPower) {
		
		double curPowerRounded = roundPower(curPower, 2);
		
		int maxPower = 250; // This is currently hard coded, but will need to be input by user.
		
		String curPowerString = curPowerRounded + " Watts";
		
		GoogleOMeter chart = GCharts.newGoogleOMeter((curPowerRounded * 100) / maxPower,
								curPowerString,
								"",
								Color.newColor("1F77B4"),
								Color.newColor("AEC7E8"),
								Color.newColor("FFBB78"),
								Color.newColor("FF7F0E")); //Four colours, dark blue to dark orange
		
		chart.setTitle("Current Power Consumption", BLACK, 20);
		chart.setSize(350, 200);
		String url = chart.toURLString();

		// Add string to the end of the url to change the size of the arrow and to add the axis labels to the dial:
		url += "&chls=2,5,12|12&chxt=x,y&chxl=1:|0|+|MAX (" + maxPower + " W)";
		return url;
	}
	
	
	/**
	 * Returns a double rounded to a specified number of decimal places
	 * 
	 * @param curPower
	 * @param decimalPlaces
	 * @return The power figure passed rounded to two decimal places
	 */
	private static double roundPower(double curPower, int decimalPlaces) {
		
		// Multiply the number by the appropriate power of 10, for division later.
		curPower = curPower * Math.pow(10, decimalPlaces);
		
		// Cast to an int, then divide the number by the appropriate power of 10:
		double curPowerRounded = ((int) curPower) / Math.pow(10, decimalPlaces);
		
		return curPowerRounded;
	}
}