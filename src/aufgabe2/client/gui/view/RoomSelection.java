package aufgabe2.client.gui.view;

import aufgabe2.client.socket.ChatClient;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RoomSelection {
	
	private ChatClient client;
	
	private Stage primaryStage;
	
	private ComboBox<String> rooms;
	
	private Button connect;
	
	private GridPane pane;
	
	public RoomSelection() {
		this.primaryStage = new Stage();
		this.rooms = new ComboBox<>();
		this.connect = new Button("Connect");
		this.connect.setOnAction(event -> connect());
		this.pane = new GridPane();
		
		pane.add(rooms, 0, 0);
		pane.add(connect, 0, 1);
		
	}
	
	private void connect() {
		this.client.connectToChatRoom(rooms.getValue());
		ChatGUI chat = new ChatGUI();
		chat.setClient(client);
		chat.show();
		this.primaryStage.close();
		
	}
	
	public void setClient(ChatClient client) {
		this.client = client;
		rooms.setItems(FXCollections.observableArrayList(client.getChatRooms()));
	}

	public void show() {
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	

}
