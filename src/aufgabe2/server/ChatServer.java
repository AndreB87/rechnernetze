package aufgabe2.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aufgabe2.server.chatroom.IChatRoom;
import aufgabe2.server.socket.ChatServerSocket;

public class ChatServer implements IChatServer {

	/**
	 * Adresse des Servers
	 */
	private InetAddress serverAddress;

	/**
	 * Socket
	 */
	private ServerSocket serverSocket;

	/**
	 * Liste der Chat-Räume
	 */
	private Map<String, IChatRoom> chatRooms;

	public ChatServer(int port) throws IOException {
		this.serverAddress = InetAddress.getLocalHost();
		this.serverSocket = new ServerSocket(port);
		this.chatRooms = new HashMap<>();
	}

	public ChatServer(String internetAddress, int port) throws IOException {
		this.serverAddress = InetAddress.getByName(internetAddress);
		this.serverSocket = new ServerSocket(port);
		this.chatRooms = new HashMap<>();
	}

	public void checkConnection() {
		while (true) {
			Socket client = null;
			try {
				client = serverSocket.accept();
				System.out.println("Client gefunden");
				System.out.println(client);
				ChatServerSocket socket = new ChatServerSocket(client, this);
				System.out.println(socket);
				socket.start();
				
			} catch (IOException e) {
				// Nothing to do here
			}
		}
	}

	/**
	 * Liefert eine Liste mit den Namen der Chat-Räume, die sich auf dem Server
	 * befinden, zurück
	 */
	@Override
	public List<String> getChatrooms() throws Exception {
		if (chatRooms != null && !chatRooms.isEmpty()) {
			return new ArrayList<>(chatRooms.keySet());
		} else {
			throw new Exception("Auf diesem Server existieren keine Chat-Räume!");
		}
	}

	@Override
	public IChatRoom connectToChatRoom(ChatServerSocket user, String userName, String chatRoom) {
		if (chatRooms.containsKey(chatRoom) && chatRooms.get(chatRoom).connectToRoom(user, userName)) {
			return chatRooms.get(chatRoom);
		}
		return null;
	}
	
	@Override
	public boolean addChatRoom(IChatRoom chatRoom) {
		if (!chatRooms.containsKey(chatRoom.getName())) {
			chatRooms.put(chatRoom.getName(), chatRoom);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeChatRoom(String chatRoom) {
		if (chatRooms.containsKey(chatRoom)) {
			chatRooms.remove(chatRoom);
			return true;
		}
		return false;
	}

	@Override
	public InetAddress getAddress() {
		return serverAddress;
	}
}
