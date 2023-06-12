package chatsys;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	static Socket client;
	static User user;
	static Stage stage;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;
    
    @FXML
    void doLogin(ActionEvent event) throws IOException {
    	try {
			InetSocketAddress address = new InetSocketAddress("localhost", 3434);
			client.connect(address);
			DataInputStream in = new DataInputStream(client.getInputStream());
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			String username=usernameTextField.getText();
			String password=passwordTextField.getText().trim();
			out.writeUTF("登录@"+username+"###"+password);
			String backmsg=in.readUTF();
			if(backmsg.startsWith("登录成功")) {
				System.out.println("登陆成功");
				int userId=Integer.parseInt(backmsg.substring(backmsg.indexOf(':')+1));
				System.out.println(userId);
				user.set(client, username, userId);
				FlowPane ap = (FlowPane)FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
				Scene scene = new Scene(ap);
				stage.setTitle("chat聊天室(id=" + user.userid + "，昵称=" + user.username + ")");
				stage.setScene(scene);
				stage.setResizable(false);
				stage.show();
			}else {
				client.close();
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setContentText(backmsg);
		        alert.setHeaderText("登录失败");
		        alert.showAndWait();
			}
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
    	
    

    @FXML
    void doCancel(ActionEvent event) {
    	((Stage) usernameTextField.getScene().getWindow()).close();
    }

    @FXML
    void doReg(ActionEvent event) throws IOException {
    	AnchorPane ap = (AnchorPane)FXMLLoader.load(getClass().getClassLoader().getResource("Reg.fxml"));
    	RegController.setBackScene(passwordTextField.getScene(),stage);
		Scene scene = new Scene(ap);
		stage.setScene(scene);
    }
    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
    public LoginController() {
    }



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usernameTextField.setText("小明");
		passwordTextField.setText("123123");
	}
    
}
