package database;

/**
 * Represents the parameters required for a database connection. Instantiated
 * using values read from a JSON file.
 */
public class ConnectionParameters {
	private String url;
	private String user;
	private String password;

	/**
	 * Returns the database URL.
	 * 
	 * @return the database url
	 * 
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the username used to connect the database.
	 * 
	 * @return the username used to connect to the database
	 * 
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Returns the password used to connect to the database.
	 * 
	 * @return the password used to connect to the database
	 * 
	 */
	public String getPassword() {
		return password;
	}
}