package chatsys;

import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GroupController {
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
    private ListView<?> listView;

    @FXML
    private TextField inputTextField;
    
    @FXML
    private TextField newGroupNameTextField;

    @FXML
    void exitGroupAction(ActionEvent event) {
    	
    }

    @FXML
    void inviteInGroupAction(ActionEvent event) {

    }

    @FXML
    void newGroupNameAction(ActionEvent event) {

    }

    @FXML
    void createGroupAction(ActionEvent event) {

    }
    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
}
