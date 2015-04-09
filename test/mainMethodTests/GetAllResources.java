package tests;

import java.io.IOException;
import java.util.ArrayList;

import organization.Organization;

import resources.Datacenter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests the Organization.getAllDatacenters method
 */
public class GetAllResources {

	public static void main(String[] args) {
		// Get all datacenters
		ArrayList<Datacenter> datacenters = Organization.getAllDatacenters();

		// Get JSON object mapper
		ObjectMapper mapper = new ObjectMapper();

		// Convert ArrayList to JSON object and print
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(System.out,
					datacenters);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}