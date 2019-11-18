package aufgabe1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

public class MailFile {

	/**
	 * regex for valid email-address
	 */
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * End-String for commands to server
	 */
	private static final String end = "\r\n";

	/**
	 * File with user-information
	 */
	private static final String userFile = "/home/andre/OldEclipse/RNP/src/aufgabe1/config/user.conf";
	/**
	 * File for logging
	 */
	private static final String logFile = "/home/andre/OldEclipse/RNP/src/aufgabe1/logging.log";

	/**
	 * User properties 
	 */
	private Properties user;

	/**
	 * Socket
	 */
	private Socket socket;

	/**
	 * INet Address to MailHost
	 */
	private InetAddress mailHost;

	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * FileHandler for Logger
	 */
	private Handler fileHandler;

	public MailFile() throws SecurityException, IOException {
		user = MailProperty.getMailUser(userFile);
		mailHost = InetAddress.getByName(user.getProperty(MailProperty.HOSTNAME));
		logger = Logger.getLogger(MailFile.class.getName());
		fileHandler = new FileHandler(logFile);
		logger.addHandler(fileHandler);
		socket = SSLSocketFactory.getDefault().createSocket(mailHost,
				Port.valueOf(user.getProperty(MailProperty.PORT)).getPortNumber());
	}
	
	/**
	 * checks if the given email-address is valid
	 * @param email
	 * @return
	 */
	public boolean validateEMail(String email) {
		Matcher mat = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return mat.matches();
	}

	/**
	 * Prints out the help menu
	 */
	public void printHelp() {
		System.out.println("Verwendung: java –cp . MailFile [options] <recipient mail address> <file path>");
		System.out.println("wobei options folgendes umfasst:");
		System.out.println("\t-h\t Druckt diesen Text aus");
		System.out.println("\t-n\t Neuen User anlegen");
	}

	/**
	 * Menu part to create a new user
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void createUser() throws FileNotFoundException, IOException {
		Scanner s = new Scanner(System.in);
		String uname = "";
		String email = "";
		boolean validEmail = false;
		String hostname = "";
		int portN = 0;
		while (uname.equals("")) {
			System.out.println("Bitte geben Sie einen Benutzernamen an:");
			uname = s.nextLine();
		}
		while (!validEmail) {
			System.out.println("Bitte geben Sie Ihre E-Mail Adresse an:");
			email = s.nextLine();
			validEmail = validateEMail(email);
		}
		while (hostname.equals("Bitte geben Sie den Hostnamen an")) {
			System.out.println();
			hostname = s.nextLine();
		}
		while (!((portN == 1) || (portN == 2))) {
			System.out.println("Bitte wählen Sie einen Port aus.\n" + "1: SMTP (25)\n" + "2: SMTPS (465)");
			try {
				portN = s.nextInt();
			} catch (InputMismatchException e) {
				portN = 0;
			}
		}
		Port port;
		if (portN == 1) {
			port = Port.SMTP;
		} else {
			port = Port.SMTPS;
		}
		MailProperty.setMailUser(userFile, uname, email, hostname, port);
		s.close();
		System.out.println("Benutzer wurde angelegt.");
	}

	/**
	 * Reads the password from console
	 * 
	 * @return
	 */
	public String getPassword() {
		String password;
		if (System.console() != null) {
			password = new String(System.console().readPassword("Password: "));
		} else {
			Scanner s = new Scanner(System.in);
			System.out.print("Password: ");
			password = s.nextLine();
			s.close();
		}
		return password;
	}

	/**
	 * encode file to Base64
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private String encodeFile(File file) throws IOException {
		int length = (int) file.length();
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[length];
		reader.read(bytes, 0, length);
		reader.close();
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * Send Email with attached file to the recipient
	 *  
	 * @param recipient
	 * @param password
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public boolean sendEMail(String recipient, String password, File file) throws IOException {
		String boundary = "xyzzy_0123456789_xyzzy";
		if (socket.isConnected()) {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
			if (inStream == null || outStream == null) {
				return false;
			}
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outStream), true);
			
			String userName = user.getProperty(MailProperty.USERNAME);
			String auth = Base64.getEncoder().encodeToString((userName + "\0" + userName + "\0" + password).getBytes());

			// Say Hello to server
			System.out.println(reader.readLine());
			writer.write("HELO " + user.getProperty(MailProperty.HOSTNAME) + end);
			writer.flush();
			System.out.println(reader.readLine());
			writer.write("AUTH PLAIN " + auth + end);
			writer.flush();
			System.out.println(reader.readLine());

			//Send Information about EMail
			writer.write("MAIL FROM: " + user.getProperty(MailProperty.EMAIL) + end);
			writer.flush();
			System.out.println(reader.readLine());
			writer.write("RCPT TO: " + recipient + end);
			writer.flush();
			System.out.println(reader.readLine());
			writer.write("DATA" + end);
			writer.flush();
			System.out.println(reader.readLine());

			// Header
			writer.write("From: " + user.getProperty(MailProperty.EMAIL) + " " + end);
			logger.log(Level.INFO, "From: " + user.getProperty(MailProperty.EMAIL) + " " + end);
			writer.write("To: " + recipient + " " + end);
			writer.write("Subject: " + user.getProperty(MailProperty.SUBJECT) + " " + end);

			// Body
			writer.write("MIME-Version: 1.0 " + end);
			writer.write("Content-Type: multipart/mixed; boundary= " + boundary + " " + end);
			writer.write(end);
			writer.write("--" + boundary + end);

			// Text
			writer.write("Content-Type: text/plain; charset=ISO-8859-1 " + end);
			writer.write(end);
			writer.write(user.getProperty(MailProperty.BODY) + end);
			writer.write(end);
			writer.write("--" + boundary + end);

			// Attached file
			String fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1,
					file.getAbsolutePath().length() - 1);
			writer.write("Content-Type: application/octet-stream " + end);
			writer.write("Content-Transfer-Encoding: base64 " + end);
			writer.write("Content-Disposition: attachment;" + end + " filename=" + fileName + end);
			writer.write(end);
			writer.write(encodeFile(file));
			writer.write(end);
			
			// Send ending information
			writer.write("--" + boundary + "--" + end);
			writer.write(end + "." + end);
			writer.write("QUIT");
			writer.flush();
			String finalServerReaction = reader.readLine();
			System.out.println(finalServerReaction);

			// Close all
			writer.close();
			reader.close();
			inStream.close();
			outStream.close();
			socket.close();

			// Check if answer was positive
			if (finalServerReaction.contains("Ok")) {
				logger.fine("Gesendet von: " + user.getProperty(MailProperty.EMAIL) + "an: " + recipient + "Betreff: "
						+ user.getProperty(MailProperty.SUBJECT));
				return true;
			}
		}
		return false;

	}

	/**
	 * Main Function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MailFile mf;
			mf = new MailFile();
			if (args.length > 0 && args[0].equals("-n")) {
				mf.createUser();
			} else if (args.length > 1 && mf.validateEMail(args[0])) {
				String reciever = args[0];
				File file = new File(args[1]);
				if (!file.exists()) {
					throw new IllegalArgumentException("Die Datei existiert nicht!");
				}
				String password = mf.getPassword();
				if (mf.sendEMail(reciever, password, file)) {
					System.out.println("Die Email wurde versandt.");
				} else {
					System.out.println("Das Senden der EMail schlug fehl.");
				}
			} else {
				mf.printHelp();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
