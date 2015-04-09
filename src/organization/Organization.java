package organization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import papillon.Api;
import resources.Datacenter;
import xml.XmlParser;

/**
 * A non-instantiable class providing access to information about all of an
 * organization's datacenters.
 */
public class Organization {
	private static ArrayList<Datacenter> datacenters;

	/**
	 * Private constructor prevents class being instantiated
	 */
	private Organization() {
	}
	
	/**
	 * Returns the name of the organization, read from a file
	 * 
	 * @return the name of the organization
	 * 
	 */
	public static String getOrganizationName() {
		String orgName;
		// Organization name stored in external file for ease of modification
		File orgFile = new File("orgName.txt");

		// Create scanner from file, if found
		Scanner input = null;
		try {
			input = new Scanner(orgFile);
		} catch (FileNotFoundException e) {
			System.out.println("File orgName.txt not found");
		}

		// Read base URI and return to caller
		orgName = input.nextLine();
		input.close();

		return orgName;
	}

	/**
	 * Returns ArrayList of objects representing the organization's datacenters
	 * 
	 * @return an array list of datacenters
	 * 
	 */
	public static ArrayList<Datacenter> getAllDatacenters() {
		NodeList nodes = XmlParser.getElements(Api.getBaseUri(), "datacenter");

		// Add datacenters to array list
		datacenters = new ArrayList<Datacenter>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Element datacenter = (Element) nodes.item(i);
			datacenters.add(new Datacenter(XmlParser.getIntValue(datacenter,
					"id")));
		}

		return datacenters;
	}

	/**
	 * Updates organization's datacenter information
	 */
	public static void refreshData() {
		// Get up-to-date list of all datacenters
		NodeList nodes = XmlParser.getElements(Api.getBaseUri(), "datacenter");

		// Create array of datacenter IDs
		int[] newIds = new int[nodes.getLength()];
		for (int i = 0; i < newIds.length; i++) {
			Element datacenter = (Element) nodes.item(i);
			newIds[i] = XmlParser.getIntValue(datacenter, "id");
		}

		// Delete from ArrayList any out-of-date datacenters
		for (int i = 0; i < datacenters.size(); i++) {
			// Set flag indicating whether object is still valid
			boolean stillValid = false;

			// For each datacenter object, check that ID is still in updated
			// list
			int idForCheck = datacenters.get(i).getId();
			for (int j = 0; j < newIds.length; j++) {
				// If object's ID found in new IDs list, update flag
				if (idForCheck == newIds[j]) {
					stillValid = true;
					break;
				} else {
					continue;
				}
			}

			// If no longer valid, delete object
			if (!stillValid) {
				datacenters.remove(i);
			}
		}

		// Refresh data for remaining datacenters
		for (int i = 0; i < datacenters.size(); i++) {
			datacenters.get(i).refreshData();
		}

		// Add any newly registered datacenters to ArrayList
		for (int i = 0; i < newIds.length; i++) {
			// Set flag to indicate whether object found
			boolean found = false;

			// For each ID in the updated list, check whether there is already a
			// corresponding object
			for (int j = 0; j < datacenters.size(); j++) {
				// If matching object found, update flag
				if (newIds[i] == datacenters.get(j).getId()) {
					found = true;
					break;
				} else {
					continue;
				}
			}

			// If not found, add new object to list
			if (!found) {
				datacenters.add(new Datacenter(newIds[i]));
			}
		}

	}

	/**
	 * Returns a Datacenter object based on its ID
	 * 
	 * @param datacenterId
	 *            the id of the datacenter to be returned
	 * @return the datacenter object associated with the id
	 * 
	 */
	public static Datacenter getDatacenterById(int datacenterId) {
		Datacenter datacenter = null;
		for (int i = 0; i < datacenters.size(); i++) {
			if (datacenters.get(i).getId() != datacenterId) {
				continue;
			}
			datacenter = datacenters.get(i);
			break;
		}
		return datacenter;
	}

}