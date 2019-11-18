import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class User extends Thread {

  private String name;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private ChatServer server;
  private ChatRoom room;

  public User(Socket socket, ChatServer server) {
    this.socket = socket;
    this.server = server;
  }
   
  public PrintWriter getWriter() {
    return this.out;
  }

  public void run() {
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);

      String input;
      
      input = in.readLine();
      joinRoom(input);
      
      do {
        input = in.readLine();
        room.broadcast(input);
      } while (input != null);
           
      socket.close();
           
    } catch (IOException e) {
      System.out.println(e);
    }
  }
  
  private void joinRoom(String room) {
    this.room = this.server.getRoom(room);
    this.room.addUser(this);
  }
  
}