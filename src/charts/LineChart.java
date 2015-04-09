package charts;

import static com.googlecode.charts4j.Color.BLACK;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.ScatterPlotData;
import com.googlecode.charts4j.XYLineChart;

public class LineChart {
	
	/**
	 * Takes an int representing the number of hours to be represented, and an array
	 * representing the power values to be graphed. The number of hours required is taken
	 * as a separate parameter so that the data can be processed to ensure a consistent
	 * line chart. 
	 * 
	 * Note to reviewer: as we used a single VM to develop the code, we had some issues
	 * regarding missing readings. We have some additional code below to cater for this.
	 * 
	 * @param hours
	 * @param readings
	 * @return a url representing a line chart for the time range specified
	 */
	public static String getChartUrl(int hours, double[][] readings) {
		
		// Pad the data to ensure it fits as required. If the data is already ok then
		// no processing is required.
		double[][] paddedReadings = pad(hours, readings);
		
		if(hours > 1) {
			paddedReadings = scaleData(paddedReadings);
		}
		
		// Take arrays representing the times and power values for use in the LineChart methods:
		double[] times = new double[paddedReadings.length];
		double[] powers = new double[paddedReadings.length];

		for (int i = 0; i < paddedReadings.length; i++) {
			times[i] = paddedReadings[i][0];
			powers[i] = paddedReadings[i][1];
		}

		// Define required chart parameters:
		double min_x = getMin(times);
		double max_x = getMax(times);
		double min_y = getMin(powers);
		double max_y = getMax(powers);

		// Scale the data as required:
		Data d1 = DataUtil.scaleWithinRange(min_x, max_x, times); // removed buffer to improve accuracy of grid lines
		Data d2 = DataUtil.scaleWithinRange(min_y - 1, max_y + 1, powers);

		// A ScatterPlot is used as it suited requirements better than the other line charts:
		ScatterPlotData data = Plots.newScatterPlotData(d1, d2);

		Color shapeColor = Color.newColor("4682B4");
		data.setColor(shapeColor);

		// Set up axes:
		AxisLabels axisLabels_x1 = AxisLabelsFactory.newAxisLabels("Date/Time", 50.0); // new
		axisLabels_x1.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 13, 
				AxisTextAlignment.CENTER)); // new
		
		AxisLabels axisLabels_x2 = AxisLabelsFactory.newAxisLabels(getTimeAxisLabels(min_x, max_x));
		axisLabels_x2.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 13, 
				AxisTextAlignment.CENTER)); // new
		
		AxisLabels axisLabels_y1 = AxisLabelsFactory.newAxisLabels("Power (Watts)", 50.0); // new
		axisLabels_y1.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 13,
				AxisTextAlignment.CENTER)); // new
		
		AxisLabels axisLabels_y2 = AxisLabelsFactory.newNumericRangeAxisLabels(
				min_y - 1, max_y + 1);
		axisLabels_y2.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 13,
				AxisTextAlignment.CENTER));

		
		// Prepare the chart properties:
		XYLineChart xyLineChart = GCharts.newXYLineChart(data);
		xyLineChart.setSize(750, 400);
		xyLineChart.setGrid(25, 25, 3, 2);
		xyLineChart.setTitle("Power Consumption", BLACK, 16);
		xyLineChart.setBackgroundFill(Fills.newSolidFill(Color
				.newColor("FFFFFF")));
		xyLineChart.addXAxisLabels(axisLabels_x1);
		xyLineChart.addYAxisLabels(axisLabels_y2);
		xyLineChart.addXAxisLabels(axisLabels_x2);// new
		xyLineChart.addYAxisLabels(axisLabels_y1);// new

		return xyLineChart.toURLString();
	}

	/**
	 * In case of partial data for the chosen time range, this method will pad the
	 * array with leading power values of zero to represent the time the server was 
	 * powered off.
	 * 
	 * Note to reviewer: this method should only be required in rare cases such as a new server install
	 * or a server malfunction. However as we developed the code using a single VM, there were many
	 * instances where we had only partial data and needed to add the zero values to represent the
	 * time the machine was off. 
	 * 
	 * @param hours
	 * @param array
	 * @return Am array containing leading zeroes for power if the initial readings were partial.
	 */
	private static double[][] pad(int hours, double[][] array)
	{
		// As the data is not scaled for one hour, no need to pad:
		if(hours == 1) {
			return array;
		}
		
		// The required number of points for the time range chosen:
		int numPoints = hours * 60;
		
		// Create an array to hold the padded readings:
		double[][] paddedArray = new double[numPoints][2];
		
		// Add existing values to the padded array:
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j <= 1; j++){
				paddedArray[(numPoints - array.length) + i][j] = array[i][j]; // The current values fit in at the end of the padded array
			}
		}

		// Add time values at the start:
		for(int i = numPoints - array.length - 1; i >= 0; i--) {
			paddedArray[i][0] = paddedArray[i + 1][0] - 60; // add the time stamp, power is already initialised to zero
		}
		
		return paddedArray;
	}
	
	/**
	 * Returns the minimum value in a double array
	 * 
	 * @param values
	 * @return the minimum value
	 */
	private static double getMin(double[] values) {
		double min = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] < min) {
				min = values[i];
			}
		}
		return min;
	}

	/**
	 * Returns the maximum value in a double array
	 * 
	 * @param values
	 * @return the maximum value
	 */
	private static double getMax(double[] values) {
		double max = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		return max;
	}
	
	
	/**
	 * Creates a List representing the time axis. The range is set using the min and
	 * max values. All Unix time stamps are converted to a string representation.
	 * 
	 * @param min_x
	 * @param max_x
	 * @return a time axis for a line chart
	 */
	private static List<String> getTimeAxisLabels(double min_x, double max_x) {
		// Create a List to hold the values:
		List<String> timesList = new LinkedList<String>();
		
		// Add a string representation of the minimum for the range
		String s = getDateString((long)min_x);
		timesList.add(s);
		
		// The range is divided in four, and string representations of each point are added:
		s = getDateString((long)(min_x + (max_x - min_x) / 4));
		timesList.add(s);
		s = getDateString((long)(min_x + 2 * (max_x - min_x) / 4));
		timesList.add(s);
		s = getDateString((long)(min_x + 3 * (max_x - min_x) / 4));
		timesList.add(s);
		
		// Add a string representation of the maximum for the range
		s = getDateString((long)max_x);
		timesList.add(s);
		
		return timesList;
	}
	
	/**
	 * takes a Unix time stamp and converts it to a string
	 * 
	 * @param unixTime
	 * @return a string representation of unixTime
	 */
	private static String getDateString(long unixTime) {
		java.util.Date dateTime = new java.util.Date((long)unixTime * 1000);
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String s = formatter.format(dateTime);
		return s;
	}
	
	
	/**
	 * For improved visuals of the graphs, this method is used to scale down the data.
	 * Specifically, 360 data points are retained for the 12 hour and 24 hour line charts
	 * for display in the user interface.
	 * 
	 * @param array
	 * @return A scaled down version of array containing 360 data points
	 */
	private static double[][] scaleData(double[][] array)
	{
		int factor = array.length / 360;

		// If the number of points is already 360 or less return the same array:
		if(factor <= 1) {
			return array;
		}

		// Create an array of size 360 to hold the scaled data.
		double[][] scaledArray = new double[360][2];

		for(int i = 0; i < 360; i++) {
			// set the time
			if(factor == 2) {
				scaledArray[i][0] = array[i * factor][0]; // if the factor is 2, then the general formula below won't work.
			}
			else {
				scaledArray[i][0] = array[(factor / 2) +  (i * factor)][0];
			}

			// Add the average power values corresponding to the time at index i:
			scaledArray[i][1] = getAverageData(i, factor, array);
		}

		return scaledArray;
	}


	/**
	 * Returns the average of a number of values. The values are taken starting at 'index' in
	 * the array. The number of values depends on factor.
	 * 
	 * @param index
	 * @param factor
	 * @param array
	 * @return the average of the values after pivoting on the value at index
	 */
	private static double getAverageData(int index, int factor, double[][] array) {
		// Declare variables:
		double sum = 0;
		double count = 0;
		double average = 0;

		// Sum the required number of values:
		for(int i = index * factor; i < index * factor + factor; i++)
		{
			sum += array[i][1];
			count++;
		}
		
		// Compute the average:
		average = sum / count;
		
		return average;
	}
}