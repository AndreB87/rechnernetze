import java.util.HashSet;

public class ChatRoom {

  private HashSet<User> users = new HashSet<User>();
  private String name;
  
  public ChatRoom(String name) {
    this.name = name;
  }
  
  public void addUser(User user) {
    users.add(user);
  }
  
  public void broadcast(String message) {
    for (User user : users) {
      user.getWriter().println(message);
    }
  }
}