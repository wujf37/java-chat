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

class LoginTask implements Runnable{
	String userId;
	String password;
	String username;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public LoginTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		username=arg[0].trim();
		password=arg[1].trim();
		server=userver;
		con=ucon;
		try{
			out = new DataOutputStream(server.getOutputStream());
		}
		catch(IOException e) {
			System.out.println("打开socket写入流失败");
		}
		ht=uht;
	}
	public void run() {
		System.out.println("用户"+username+"尝试登录");
        try
        {   
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user where username = '" + username + "'");
            if(rs.next()) 
            {   String pw = rs.getObject("password").toString().trim();
                if(pw.equals(password))
                {   
                	userId=rs.getObject("userid").toString();
                	ht.put(userId,new User(username,userId,server));
                	System.out.println("登录成功，在线用户列表：");
                	showHashData();
                	out.writeUTF("登录成功，id为:"+userId);
                }
                else {
                	System.out.println("密码错误");
                	out.writeUTF("密码错误");
                }
            }
            else{
            	System.out.println("账号不存在");
                out.writeUTF("账号不存在");
            }
            rs.close();
        	stmt.close();
        } 
        catch(SQLException s)  
        {     System.out.println("数据库操作失败: " + s.toString() +s.getErrorCode() + "  " + s.getSQLState());		
        }
        catch(IOException s) {
        	System.out.println("socket操作失败:"+s);
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