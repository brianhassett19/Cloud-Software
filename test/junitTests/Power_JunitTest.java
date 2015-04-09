package junitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import resources.Datacenter;
import tests.LineChartTest;

public class Power_JunitTest {

	private int datacentreId = 1;
	private int numHours = 1;
	Datacenter datacenter = new Datacenter(datacentreId);
	double[][] powerUsage = datacenter.getPower(numHours);


	@Test
	/** Check that all power values are positive */
	public void testPositivePower() {
		assertTrue(validPowers(powerUsage));
	}

	@Test
	/** Check that all time values fall in the expected range */
	public void testValidTimes() {
		assertTrue(validTimes(powerUsage));
	}

	@Test
	/** Check that there are 60 readings per hour */
	public void testNumReadings() {
		assertEquals(powerUsage.length, numHours * 60);
	}

	@Test
	/** Check that there are 60 readings per hour */
	public void testIncrementalTimes() {
		assertTrue(incrementalTimes(powerUsage));
	}

	
	/**
	 * Check that all time values increment
	 * 
	 * @param powerUsage
	 * @return a boolean indicating that the times are valid
	 */
	private boolean incrementalTimes(double[][] powerUsage) {
		double curTime = powerUsage[0][0];
		for(int i = 1; i < powerUsage.length; i++)
		{
			if(powerUsage[i][1] > curTime)
			{
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Check that all power values are positive
	 * 
	 * @param powerUsage
	 * @return a boolean indicating that all power values are positive
	 */
	private  boolean validPowers(double[][] powerUsage) {
		for(int i = 0; i < powerUsage.length; i++)
		{
			if(powerUsage[i][1] < 0)
			{
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Check that all time values fall in the expected range
	 * 
	 * @param powerUsage
	 * @return a boolean indicating that all time values fall within the required range
	 */
	private boolean validTimes(double[][] powerUsage) {
		long endTime = System.currentTimeMillis() / 1000;
		long startTime = endTime - (numHours * 3600);

		for(int i = 0; i < powerUsage.length; i++)
		{
			if(powerUsage[i][0] > endTime || powerUsage[i][0] < startTime)
			{
				return false;
			}
		}
		return true;
	}
}