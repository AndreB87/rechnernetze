package aufgabe2.server.chatroom;

import java.util.List;

import aufgabe2.server.socket.ChatServerSocket;


public interface IChatRoom {
	
	public boolean connectToRoom(ChatServerSocket user, String userName);
	
	public boolean disconnectFromRoom(String userName);
	
	public boolean sendMessage(String userName, String message);
	
	public String getName();
	
	public List<String> getUsers();

}
