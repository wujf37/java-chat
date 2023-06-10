package chatsys;

import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FriendController {
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
	private ListView<?> listFriendView;

	@FXML
	private TextField inputTextField;

	@FXML
	void addFriendAction(ActionEvent event) {

	}

	@FXML
	void deleteFriendAction(ActionEvent event) {

	}
    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
}
