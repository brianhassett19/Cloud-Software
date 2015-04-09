package papillon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A non-instantiable class providing a single method to access the base URI for
 * Papillon API calls.
 */
public class Api {

	/**
	 * Private constructor prevents class being instantiated
	 */
	private Api() {

	}

	/**
	 * Returns base URI for Papillon API calls, read from a file. For purposes
	 * of the mobile app, this URI takes the form:
	 * http://server[:port]/papillonserver/rest/datacenters/
	 * 
	 * @return a string representing the base uri for papillon api calls
	 */
	public static String getBaseUri() {
		String baseURI;
		// Base URI stored in external file for ease of modification
		File uriFile = new File("baseURI.txt");

		// Create scanner from file, if found
		Scanner input = null;
		try {
			input = new Scanner(uriFile);
		} catch (FileNotFoundException e) {
			System.out.println("Base URI file not found");
		}

		// Read base URI and return to caller
		baseURI = input.nextLine();
		input.close();

		return baseURI;

	}

}