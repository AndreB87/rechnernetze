package aufgabe2.client.gui.view;

import java.io.IOException;

import aufgabe2.client.socket.ChatClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	private TextField userName;
	
	private TextField serverAddress;
	
	private TextField port;
	
	private Button connect;
	
	private GridPane pane;
	
	private static final int GAP_SIZE = 10;
	
	@Override
	public void init() {
		this.pane = new GridPane();
		this.userName = new TextField();
		this.serverAddress = new TextField();
		this.port = new TextField();
		this.connect = new Button("Connect");
		this.connect.setOnAction(event -> connect());
		
		pane.add(new Label("User Name:"), 0, 0);
		pane.add(userName, 1, 0);
		pane.add(new Label("Server:"), 0, 1);
		pane.add(serverAddress, 1, 1);
		pane.add(new Label("Port:"), 0, 2);
		pane.add(port, 1, 2);
		pane.add(connect, 1, 3);
		pane.setHgap(GAP_SIZE);
		pane.setVgap(GAP_SIZE);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Chat");
		primaryStage.show();
	}
	
	private void connect() {
		try {
			ChatClient client = new ChatClient(userName.getText(), serverAddress.getText(), Integer.parseInt(port.getText()));
			RoomSelection rs = new RoomSelection();
			rs.setClient(client);
			rs.show();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String... args) {
		Application.launch();
	}
	
}
