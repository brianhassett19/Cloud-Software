package junitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import resources.Floor;

public class Floor_JunitTest {

	// Set parameters to identify floor
	int datacenterId = 1;
	int floorId = 1;

	// Construct floor object given parameters above
	Floor floor = new Floor(datacenterId, floorId);

	// Get floor data fields
	String name = floor.getName();
	String description = floor.getDescription();
	double xAxis = floor.getXAxis();
	double yAxis = floor.getYAxis();

	@Test
	/** Check that the correct name is found */
	public void testName() {
		assertEquals(name, "F1");
	}

	@Test
	/** Check that the correct description is found */
	public void testDescription() {
		assertEquals(description, "F1");
	}

	@Test
	/** Check that the correct xAxis is found */
	public void testXaxis() {
		assertEquals(xAxis, 0.0, 0);
	}

	@Test
	/** Check that the correct yAxis is found */
	public void testYaxis() {
		assertEquals(yAxis, 0.0, 0);
	}
}