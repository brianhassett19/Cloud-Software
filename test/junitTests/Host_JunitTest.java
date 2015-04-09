package junitTests;

import static org.junit.Assert.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import resources.Host;

public class Host_JunitTest {

	int datacenterId = 1;
	int floorId = 1;
	int rackId = 1;
	int hostId = 1;

	@Test
	/** Check that the correct model group ID is found */
	public void testModelGroupId() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		int modelGroupId = host.getModelGroupId();
		assertEquals(modelGroupId, 1);
	}

	@Test
	/** Check that the correct name is found */
	public void testName() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String name = host.getName();
		assertEquals(name, "Host1");
	}

	@Test
	/** Check that the correct description is found */
	public void testDescription() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String description = host.getDescription();
		assertEquals(description, "Host1");
	}

	@Test
	/** Check that the correct host type is found */
	public void testType() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostType = host.getHostType();
		assertEquals(hostType, "SERVER");
	}

	@Test
	/** Check that the correct IP address is found */
	public void testIpAddress() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		InetAddress ipAddress = host.getIpAddress();
		assertEquals(ipAddress.toString(), "/127.0.0.1");
	}

	@Test
	/** Check that the correct processor count is found */
	public void testProcessorCount() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		int processorCount = host.getProcessorCount();
		assertEquals(processorCount, 1);
	}

	@Test
	/** Check that the correct VM count is found */
	public void testVmCount() {
		Host host = null;
		try {
			host = new Host(datacenterId, floorId, rackId, hostId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		int vmCount = host.getVmCount();
		assertEquals(vmCount, 0);
	}
}