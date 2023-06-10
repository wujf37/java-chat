package chatsys;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class RegController {
	static Scene backscene=null;
	static Stage stage;
	@FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void doReg(ActionEvent event) {
    	try {
    		Socket client=new Socket("localhost",3434);
			DataInputStream in = new DataInputStream(client.getInputStream());
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			String username=usernameTextField.getText();
			String password=passwordTextField.getText().trim();
			out.writeUTF("注册@"+username+"###"+password);
			String backmsg=in.readUTF();
			if(backmsg.startsWith("注册成功")) {
				System.out.println("注册成功");
				String userId=backmsg.substring(backmsg.indexOf(':')+1).trim();
				FlowPane ap = (FlowPane)FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setContentText("你的用户id为："+userId);
		        alert.setTitle("注册成功");
		        alert.showAndWait();
		        stage.setScene(backscene);
			}else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setContentText(backmsg);
		        alert.setHeaderText("注册失败");
		        alert.showAndWait();
			}
			client.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }

    @FXML
    void doCancel(ActionEvent event) {
		stage.setScene(backscene);
    }
    public static void setBackScene(Scene s,Stage st) {
    	backscene=s;
    	stage=st;
    }
}
