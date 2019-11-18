package aufgabe2.client.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import textFileLogger.TextFileLogger;

public class ChatClient {
	
	private TextFileLogger logger;

	private Socket client;

	private BufferedReader reader;

	private PrintWriter writer;

	private String userName;

	private String chatRoom;

	private List<String> chatRooms;

	public ChatClient(String userName, String serverAddress, int port) throws IOException {
		this.logger = new TextFileLogger("/home/students/acc120/TI_Labor/Rechnernetze/rnp/src/aufgabe2/client/socket/clientLog.txt");
		this.userName = userName;
		this.client = new Socket(InetAddress.getByName(serverAddress), port);
		chatRooms = new ArrayList<>();

		InputStream inStream = client.getInputStream();
		OutputStream outStream = client.getOutputStream();

		if (inStream == null || outStream == null) {
			throw new IOException("No Connection");
		}

		reader = new BufferedReader(new InputStreamReader(inStream));
		writer = new PrintWriter(new OutputStreamWriter(outStream), true);
		
		if (!client.isConnected()) {
			logger.log("Oh oh");
		} else {
			logger.log(client.getRemoteSocketAddress().toString());
			connect();
		}
	}

	public boolean connect() throws IOException {
		String message = "HELLO " + client.getInetAddress().getHostName();
		writer.println(message);
		writer.flush();
		logger.log(message);
		try {
			String value = reader.readLine();
			logger.log(value);
			if (value.equals("ROOMS")) {
				value = reader.readLine();
				logger.log(value);
				while (!value.equals("ENDROOMS")) {
					System.out.println("HALLOOOOOOOO");
					System.out.println(value);
					chatRooms.add(value);
					value = reader.readLine();
					logger.log(value);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean connectToChatRoom(String chatRoom) {
		try {
			if (chatRooms.contains(chatRoom)) {
				String message = "LOGIN " + userName + " " + chatRoom;
				writer.println(message);
				writer.flush();
				logger.log(message);
				message = reader.readLine();
				logger.log(message);
				if (message.equals("LOGGED-IN")) {
					this.chatRoom = chatRoom;
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getMessage() {
		try {
			String message = reader.readLine();
			logger.log(message);
			return message;
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public void sendMessage(String message) throws IOException {
		writer.println(message);
		writer.flush();
		logger.log(message);
	}

	public String getChatRoom() {
		return chatRoom;
	}
	
	public List<String> getChatRooms() {
		return chatRooms;
	}

	public String getUserName() {
		return userName;
	}
}
