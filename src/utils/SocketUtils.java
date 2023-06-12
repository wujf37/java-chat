package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketUtils {
	public static String host = "localhost";
	public static Integer port = 3434;
	public static String separater = "###";
	
	public static String request(String params) {
		try (Socket friendSocket = new Socket(host, port)) {
			DataInputStream in = new DataInputStream(friendSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(friendSocket.getOutputStream());
			// out.writeUTF("获取好友列表@" + user.userid);
			out.writeUTF(params);
			String backmsg = in.readUTF();
			return backmsg;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
}
