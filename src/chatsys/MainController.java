package chatsys;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
    private ToggleGroup opt;

    @FXML
    private Pane contentPane;

    @FXML
    void chatAction(ActionEvent event) {
    	user.show();
    	contentPane.getChildren().clear();
		try {
			FlowPane currRoot = (FlowPane)FXMLLoader.load(getClass().getClassLoader().getResource("Chat.fxml"));
			contentPane.getChildren().add(currRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void friendAction(ActionEvent event) {
    	contentPane.getChildren().clear();
		try {
			FlowPane currRoot = (FlowPane)FXMLLoader.load(getClass().getClassLoader().getResource("Friend.fxml"));
			contentPane.getChildren().add(currRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void groupAction(ActionEvent event) {
    	contentPane.getChildren().clear();
		try {
			FlowPane currRoot = (FlowPane)FXMLLoader.load(getClass().getClassLoader().getResource("Group.fxml"));
			contentPane.getChildren().add(currRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
}
