package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

public class LogoutTask implements Runnable{
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	String userId;
	public LogoutTask(String[] arg,Socket userver,Hashtable<String,User> uht) {
		userId=arg[0].trim();
		server=userver;
		try{
			out = new DataOutputStream(server.getOutputStream());
		}
		catch(IOException e) {
			System.out.println("打开socket写入流失败");
		}
		ht=uht;
	}
	public void run() {
		System.out.println("用户"+userId+"退出登录");
		User user=ht.get(userId);
		try{//先关闭用户监听服务器的连接，再将键值对从哈希表中移除，最后关闭请求连接
			user.server.close();
			out.writeUTF("已退出登录");
			ht.remove(userId);
			server.close();
			System.out.println("剩余用户列表：");
			showHashData();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	public void showHashData()
	{   Enumeration em = ht.keys();
        while(em.hasMoreElements())
        {   String userid=(String)(em.nextElement());
        	System.out.println(userid+"  "+ht.get(userid).username);
        }
	}
}
