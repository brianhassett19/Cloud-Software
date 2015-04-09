package output;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mail.Mail;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import organization.Organization;

import resources.Datacenter;
import resources.Host;
import resources.Rack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import database.ConnectionParameters;

/**
 * Contains the main method for the application.
 */
public class GetPowerProfile {
	/**
	 * Private constructor prevents class being instantiated
	 */
	private GetPowerProfile() {

	}

	/**
	 * Posts data for all datacenters to remote database every minute, making it
	 * available for web app to display; also checks against database whether
	 * rack and server power thresholds have been breached and if so notifies
	 * specified e-mail contact accordingly.
	 * 
	 * @param args
	 *            a string array
	 * 
	 */
	public static void main(String[] args) {
		// Get all datacenter objects
		final ArrayList<Datacenter> datacenters = Organization
				.getAllDatacenters();

		// Create JSON object mapper
		final ObjectMapper mapper = new ObjectMapper();

		// Create timer
		Timer timer = new Timer();

		// Schedule task to run every minute
		long delay = 60000;
		timer.scheduleAtFixedRate(new TimerTask() {

			// Create HashMap to track sending of mails for particular
			// resources. Keys are string representations of racks and servers;
			// values are dates (times) at which mails were sent
			HashMap<String, Date> alarmTracker = new HashMap<String, Date>();

			@Override
			public void run() {
				// Get organization name
				String orgName = Organization.getOrganizationName();

				// Refresh Organization datacenters data
				Organization.refreshData();

				// Get date
				Date date = new Date(System.currentTimeMillis());

				// Create HttpClient and HttpPost
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost httpPost = new HttpPost(
						"http://cloud.danielmaher.me/putjson.php");

				// Create JSON string to post to database
				String jsonString = null;
				try {
					// Convert ArrayList to JSON and embed in jsonString
					jsonString = "{\n\"datacenter\" : "
							+ mapper.writeValueAsString(datacenters) + "\n}";
				} catch (JsonProcessingException e2) {
					e2.printStackTrace();
				}

				try {
					// Post data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("username",
							orgName));
					nameValuePairs.add(new BasicNameValuePair("json",
							jsonString));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							"UTF-8"));
					HttpResponse httpResponse = httpClient.execute(httpPost);

					if (httpResponse != null) {
						// Write confirmation to console
						System.out.println("Output posted at " + date);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Declare database ConnectionParameters
				ConnectionParameters connectionParams = null;

				// Create database connection parameters object from JSON file
				try {
					connectionParams = mapper.readValue(new File("db.json"),
							ConnectionParameters.class);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					// Load JDBC driver
					Class.forName("com.mysql.jdbc.Driver");

					// Connect to database
					Connection connection = DriverManager.getConnection(
							connectionParams.getUrl(),
							connectionParams.getUser(),
							connectionParams.getPassword());

					// Create a statement to issue SQL queries
					Statement statement = connection.createStatement();

					// Select alarm details from rackThresholds table
					ResultSet rackResultSet = statement
							.executeQuery("SELECT * FROM rackThresholds");

					// For each rack for which an alarm has been set, check that
					// power has not exceeded threshold
					while (rackResultSet.next()) {
						Rack rack = Organization
								.getDatacenterById(
										rackResultSet.getInt("datacenterId"))
								.getFloorById(rackResultSet.getInt("floorId"))
								.getRackById(rackResultSet.getInt("rackId"));
						double currentPower = rack.getCurrentPower();
						if (currentPower > rackResultSet
								.getDouble("powerThreshold")) {
							System.out
									.println("Power threshold exceeded for rack "
											+ rack.getName() + " at " + date);
							// Check whether mail has been sent for rack in last
							// 15 mins; if not, send mail and add/update tracker
							// entry
							if (alarmTracker.get(rack.toString()) == null
									|| date.after(new Date(alarmTracker.get(
											rack.toString()).getTime() + 900000))) {
								Mail.sendBreachMail(rack.toString(),
										currentPower,
										rackResultSet.getString("email"));
								alarmTracker.put(rack.toString(), date);
							}
						} else {
							// If power is within threshold, no action
							System.out.println(rack.getName() + " OK");
						}
					}

					// Select alarm details from hostThresholds table
					ResultSet hostResultSet = statement
							.executeQuery("SELECT * FROM hostThresholds");

					// For each host for which an alarm has been set, check that
					// power has not exceeded threshold
					while (hostResultSet.next()) {
						Host host = Organization
								.getDatacenterById(
										hostResultSet.getInt("datacenterId"))
								.getFloorById(hostResultSet.getInt("floorId"))
								.getRackById(hostResultSet.getInt("rackId"))
								.getHostById(hostResultSet.getInt("hostId"));
						double currentPower = host.getCurrentPower();
						if (currentPower > hostResultSet
								.getDouble("powerThreshold")) {
							System.out
									.println("Power threshold exceeded for host "
											+ host.getName() + " at " + date);
							// Check whether mail has been sent for host in last
							// 15 mins; if not, send mail and add/update tracker
							// entry
							if (alarmTracker.get(host.toString()) == null
									|| date.after(new Date(alarmTracker.get(
											host.toString()).getTime() + 900000))) {
								Mail.sendBreachMail(host.toString(),
										currentPower,
										hostResultSet.getString("email"));
								alarmTracker.put(host.toString(), date);
							}
						} else {
							System.out.println(host.getName() + " OK");
						}
					}

					// Close result set, statement and connection
					hostResultSet.close(); // rackResultSet implicitly closed
					statement.close();
					connection.close();

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			// Sets the start time (now) and interval at which to run
		}, new Date(System.currentTimeMillis()), delay);

	}
}