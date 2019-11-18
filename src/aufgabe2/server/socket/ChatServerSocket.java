package aufgabe2.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import aufgabe2.server.IChatServer;
import aufgabe2.server.chatroom.IChatRoom;

public class ChatServerSocket extends Thread implements Observer {

	private Socket client;

	private BufferedReader reader;

	private PrintWriter writer;

	private IChatServer server;

	private String userName;

	private IChatRoom chatRoom;

	public ChatServerSocket(Socket client, IChatServer server) throws IOException {
		this.client = client;
		this.server = server;

		InputStream inStream = client.getInputStream();
		OutputStream outStream = client.getOutputStream();

		if (inStream == null || outStream == null) {
			throw new IOException("No Connection");
		}

		reader = new BufferedReader(new InputStreamReader(inStream));
		writer = new PrintWriter(new OutputStreamWriter(outStream), true);
	}

	public void run() {
		String value;
		try {
			value = reader.readLine();
			System.out.println("HALLOOOOOOOO");
			System.out.println(value);
			if (!value.startsWith("HELLO")) {
				writer.println("Not the right Server, bye!");
				writer.flush();
				client.close();
			} else {
				writer.println("ROOMS");
				for (String room: server.getChatrooms()) {
					writer.println(room);
				}
				writer.println("ENDROOMS");
				writer.flush();
				value = reader.readLine();
				if (value.startsWith("LOGIN")) {
					String[] array = value.split(" ");
					userName = array[1].trim();
					chatRoom = server.connectToChatRoom(this, userName, array[2].trim());
					if (chatRoom == null) {
						writer.println("This Room is not accessible, please try again!");
						writer.flush();
						client.close();
					} else {
						writer.println("LOGGED-IN");
						writer.flush();
					}
				}
			}

			while (!client.isClosed()) {
				value = reader.readLine();
				if (value.startsWith("SND")) {
					chatRoom.sendMessage(userName, value.substring(4));
				} else if (value.startsWith("LOGOFF")) {
					chatRoom.disconnectFromRoom(userName);
					writer.println("LOGGED-OFF");
					writer.flush();
					client.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public IChatRoom getChatRoom() {
		return chatRoom;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof IChatRoom) {
			if (arg instanceof String) {
				writer.println((String) arg);
				writer.flush();
			}
		}

	}
}
