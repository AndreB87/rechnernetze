package aufgabe2.server;

import java.io.IOException;
import java.net.InetAddress;

import aufgabe2.server.chatroom.ChatRoom;

public class ServerMain {

	public static void main(String... args) {
		IChatServer server;
		try {
			System.out.println(InetAddress.getLocalHost());
			server = new ChatServer(1234);
			server.addChatRoom(new ChatRoom("ChatRoom1"));
			server.addChatRoom(new ChatRoom("ChatRoom2"));
			server.checkConnection();
			System.out.println(server.getAddress().getHostName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
