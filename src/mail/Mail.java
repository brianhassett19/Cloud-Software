package mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Non-instantiable class containing method for sending a breach notification
 * e-mail.
 */
public class Mail {
	/**
	 * Private constructor prevents class being instantiated
	 */
	private Mail() {

	}

	/**
	 * Sends an e-mail from alarms.appillon@gmail.com to notify recipient of a
	 * power threshold breach with respect to a specified resource (rack or
	 * server).
	 * 
	 * @param resource
	 *            a string representation of the resource
	 * @param power
	 *            a double value representing the power reading that breached
	 *            the threshold
	 * @param to
	 *            a string representing the e-mail address to which to send the
	 *            notification
	 */
	public static void sendBreachMail(String resource, double power, String to) {
		// Sender's email account details
		String from = "alarms.appillon@gmail.com";
		final String username = "alarms.appillon";
		final String password = "appillon";

		// Sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Papillon Threshold Breach Notification");

			// Set message body
			message.setText("Power threshold exceeded for " + resource + "\n\n"
					+ "Latest reading " + Math.round(power) + " Watts");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			System.out.println("Failed to send message...");
		}

	}
}