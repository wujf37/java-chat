package chatsys;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	static User user;
	static Socket client;
	public void start(Stage newStage) throws Exception {
		try {
			user=new User();
			client= new Socket();
			//各界面共享client,user,newStage变量,不能再用new赋值,否则不再是相同变量
			//除了登录，其他请求不要用client发送
			ChatController.setObject(client, user, newStage);
			FriendController.setObject(client, user, newStage);
			GroupController.setObject(client, user, newStage);
			LoginController.setObject(client, user, newStage);
			MainController.setObject(client,user,newStage);
			FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
			AnchorPane ap = (AnchorPane)loader.load();
			
			Scene scene = new Scene(ap);
			newStage.setTitle("chat聊天室");
			newStage.setScene(scene);
			newStage.setResizable(false);
			newStage.show();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
        launch(args);
    	try {
    		System.out.println("退出登录");
    		Socket logout=new Socket("localhost",3434);
    		DataOutputStream out = new DataOutputStream(logout.getOutputStream());
    		DataInputStream in = new DataInputStream(logout.getInputStream());
    		out.writeUTF("退出登录@"+user.userid);
    		String backmsg=in.readUTF();
    		System.out.println(backmsg);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

class User{
	Socket client;
	String username;
	int userid;
	public User(Socket c,String name,int id) {
		client=c;
		username=name;
		userid=id;
	}
	public void show() {
		System.out.printf(username);
		System.out.println(userid);
	}
	public void set(Socket c,String name,int id) {
		client=c;
		username=name;
		userid=id;
	}
	public User() {
	}
}