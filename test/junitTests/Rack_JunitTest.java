package junitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import resources.Rack;

public class Rack_JunitTest {

	int datacenterId = 1;
	int floorId = 1;
	int rackId = 1;
	
	// Construct rack object given parameters above
	Rack rack = new Rack(datacenterId, floorId, rackId);
	
	// Get rack data fields
	String name = rack.getName();
	String description = rack.getDescription();
	int pdu = rack.getPdu();
	double xAxis = rack.getXAxis();
	double yAxis = rack.getYAxis();

	@Test
	/** Check that the correct name is found */
	public void testName() {
		assertEquals(name, "R1");
	}
	
	@Test
	/** Check that the correct description is found */
	public void testDescription() {
		assertEquals(description, "R1");
	}
	
	@Test
	/** Check that the correct description is found */
	public void testPDU() {
		assertEquals(pdu, 10);
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