package aufgabe2.server.frontend;

import java.io.IOException;
import java.util.Scanner;

import aufgabe2.server.ChatServer;
import aufgabe2.server.IChatServer;
import aufgabe2.server.chatroom.ChatRoom;


/**
 * TODO
 * @author andre
 *
 */
public class ChatServerFrontend {

	private IChatServer chatServer;
	
	private void startServer(int port) {
		try {
			this.chatServer = new ChatServer(port);
			System.out.println("Server has been created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createRoom(String roomName) {
		if (chatServer != null) {
			if (chatServer.addChatRoom(new ChatRoom(roomName))) {
				System.out.println("ChatRoom has been created");
			} else {
				System.out.println("There is already a ChatRoom with the Name " + roomName);
			}
		} else {
			System.out.println("There is no ChatServer running.");
		}
	}

	private void deleteRoom(String roomName) {
		if (chatServer != null) {
			if (chatServer.removeChatRoom(roomName)) {
				System.out.println("ChatRoom has been deleted.");
			} else {
				System.out.println("There is no ChatRoom with the Name " + roomName);
			}
		} else {
			System.out.println("There is no ChatServer running.");
		}
	}
	
	public static void main(String... args) {
		ChatServerFrontend chat = new ChatServerFrontend();
		String selection = "";
		Scanner scanner = new Scanner(System.in);
		
		while(chat.chatServer == null) {
			System.out.println("Port?");
			chat.startServer(scanner.nextInt());	
		}
		
		while(selection.equalsIgnoreCase("end")) {
			selection = scanner.next();
			if (selection.equalsIgnoreCase("create")) {
				System.out.println("Name of the ChatRoom:");
				chat.createRoom(scanner.next());
			} else if (selection.equalsIgnoreCase("delete")) {
				System.out.println("Name of the ChatRoom:");
				chat.deleteRoom(scanner.next());
			} else if (!selection.equalsIgnoreCase("end")){
				// TODO Print help
			}
		}
		// TODO ChatServer beenden
		scanner.close();
		
	}

}
