package aufgabe2.server.chatroom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import aufgabe2.server.socket.ChatServerSocket;

public class ChatRoom extends Observable implements IChatRoom  {
	
	private String name;
	
	private Map<String, ChatServerSocket> users;
	
	private List<String> messages;
	
	public ChatRoom(String name) {
		this.name = name;
		this.users = new HashMap<String, ChatServerSocket>();
		this.messages = new ArrayList<String>();
	}

	@Override
	public boolean connectToRoom(ChatServerSocket user, String userName) {
		if (!users.containsKey(userName)) {
			users.put(userName, user);
			return true;
		}
		return false;
	}

	@Override
	public boolean disconnectFromRoom(String userName) {
		if (users.containsKey(userName)) {
			users.remove(userName);
			return true;
		}
		return false;
	}

	@Override
	public boolean sendMessage(String username, String message) {
		if (users.containsKey(username)) {
			String chatMessage = username + " " + LocalDateTime.now() + ": " + message;
			messages.add(chatMessage);
			this.setChanged();
			this.notifyObservers(chatMessage);
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public List<String> getUsers() {
		return new ArrayList<>(users.keySet());
	}

}
