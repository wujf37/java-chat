package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

public class RegistTask implements Runnable{
	String userId;
	String username;
	String password;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public RegistTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
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
		System.out.println("尝试注册用户："+username);
        try
        {   
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user where username = '" + username + "'");
            if(rs.next()) 
            {   
                out.writeUTF("用户名重复");
            }
            else {
            	PreparedStatement pstmt=con.prepareStatement("INSERT INTO user (username,password) VALUES(?,?)");
            	pstmt.setString(1,username);
            	pstmt.setString(2,password);
            	int count=pstmt.executeUpdate();
                if(count==1) {
                	ResultSet rs2=stmt.executeQuery("SELECT * FROM user where username='"+username+"'");
                	rs2.next();
                	out.writeUTF("注册成功，账号为:"+rs2.getInt("userid"));
                	rs2.close();
                }
            	out.writeUTF("数据库插入失败");
            	pstmt.close();
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
}