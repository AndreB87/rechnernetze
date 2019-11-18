package aufgabe2.server;

import java.net.InetAddress;
import java.util.List;

import aufgabe2.server.chatroom.IChatRoom;
import aufgabe2.server.socket.ChatServerSocket;

public interface IChatServer {
	
	public List<String> getChatrooms() throws Exception;
	
	public IChatRoom connectToChatRoom(ChatServerSocket user, String userName, String chatRoom);
	
	public boolean addChatRoom(IChatRoom chatRoom);
	
	public boolean removeChatRoom(String chatRoom);
	
	public InetAddress getAddress();

	public void checkConnection();
}
