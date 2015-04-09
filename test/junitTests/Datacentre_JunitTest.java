package junitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import resources.Datacenter;

public class Datacentre_JunitTest {

	int datacenterId = 1;

	// Construct datacenter object given parameters above
	Datacenter datacenter = new Datacenter(datacenterId);

	// Get datacenter data fields
	String name = datacenter.getName();
	String description = datacenter.getDescription();
	double latitude = datacenter.getLatitude();
	double longitude = datacenter.getLongitude();


	@Test
	/** Check that the correct name is found */
	public void testName() {
		assertEquals(name, "D1");
	}

	@Test
	/** Check that the correct description is found */
	public void testDescription() {
		assertEquals(description, "D1");
	}

	@Test
	/** Check that the correct latitude is found */
	public void testLatitude() {
		assertEquals(latitude, 0.0, 0);
	}

	@Test
	/** Check that the correct longitude is found */
	public void testLongitude() {
		assertEquals(longitude, 0.0, 0);
	}
}