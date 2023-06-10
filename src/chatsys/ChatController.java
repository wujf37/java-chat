package chatsys;

import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ChatController {
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
    private ToggleGroup opt;

    @FXML
    private ListView<?> listView;

    @FXML
    private TextArea chatWin;

    @FXML
    private TextField inputTextField;

    @FXML
    void toGroupAction(ActionEvent event) {

    }

    @FXML
    void toFriendAction(ActionEvent event) {

    }

    @FXML
    void sendAction(ActionEvent event) {

    }
    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
}
