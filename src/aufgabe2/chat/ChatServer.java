import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.HashMap;

public class ChatServer {

  private final int PORT = 9001;
  private HashSet<User> users = new HashSet<User>();
  private HashMap<String,ChatRoom> rooms = new HashMap<String,ChatRoom>();

  public static void main(String[] args) throws Exception {
    ChatServer server = new ChatServer();
    server.run();
  }
  
  public void run() throws Exception {
    
    System.out.println("The chat server is running.");
    rooms.put("default2",new ChatRoom("default1"));
    rooms.put("default1",new ChatRoom("default2"));
    ServerSocket listener = new ServerSocket(PORT);
    
    try {
      while (true) {
        Socket socket = listener.accept();
        User user = new User(socket,this);
        users.add(user);
        user.start();
        System.out.println("A new user joined.");
      }
    } finally {
      listener.close();
    }
    
  }
  
  public ChatRoom getRoom(String room) {
    return rooms.get(room);
  }
}