package charts;

import static com.googlecode.charts4j.Color.BLACK;

import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;
import com.googlecode.charts4j.LegendPosition;

public class ActivityPieChart {
	
	/**
	 * Returns a pie chart with the legend on the bottom, total size: 350x200
	 * 
	 * @param activity
	 * @return A url representing the pie chart for the current top three activities
	 */
	public static String getChartUrl(String[] activity) {
		// Define the four slice sizes:
		double slice1Size = 100 * (100 * Double.parseDouble(activity[5])) / Double.parseDouble(activity[2]);
		double slice2Size = 100 * (100 * Double.parseDouble(activity[8])) / Double.parseDouble(activity[2]);
		double slice3Size = 100 * (100 * Double.parseDouble(activity[11])) / Double.parseDouble(activity[2]);
		double slice4Size = (10000 - (((int) slice1Size) + ((int) slice2Size) + ((int) slice3Size)));

		// Create a Slice object for each value:
		Slice s1 = Slice.newSlice((int) (slice1Size / 100), Color.newColor("1F77B4"), activity[4], activity[4] + " (" + ((int) slice1Size) / 100.0 + "%)");
		Slice s2 = Slice.newSlice((int) (slice2Size / 100), Color.newColor("AEC7E8"), activity[7], activity[7] + " (" + ((int) slice2Size) / 100.0 + "%)");
		Slice s3 = Slice.newSlice((int) (slice3Size / 100), Color.newColor("FF7F0E"), activity[10], activity[10] + " (" + ((int) slice3Size) / 100.0 + "%)");
		Slice s4 = Slice.newSlice((int) (slice4Size / 100), Color.newColor("FFBB78"), "Other", "Other" + " (" + ((int) slice4Size) / 100.0 + "%)");

		// Create a PieChart object:
		PieChart chart = GCharts.newPieChart(s1, s2, s3, s4);
		chart.setOrientation((-1) * Math.PI / 2); // Rotate chart 90 degrees anti-clockwise to match the Papillon GUI.
		chart.setTitle("Top 3 Apps", BLACK, 16);
		chart.setSize(350, 200);
		chart.setLegendPosition(LegendPosition.BOTTOM);
		String url = chart.toURLString();

		return url;
	}
}