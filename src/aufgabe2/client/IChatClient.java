package aufgabe2.client;

public interface IChatClient {
	
	public boolean connectToServer(String serverAddress);
	
	public boolean connectToChatRoom(String serverName);
	
	public boolean sendMessage(String message);
	
	public void recieveMessage(String message);

}
