package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

public class DeleteFriend implements Runnable{
	String userId1;
	String userId2;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public DeleteFriend(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		userId1=arg[0].trim();
		userId2=arg[1].trim();
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
		System.out.println("用户"+userId1+"尝试将用户"+userId2+"从好友中移除");
        try
        {   
            Statement stmt = con.createStatement();
            PreparedStatement pstmt=con.prepareStatement("DELETE FROM friend WHERE userid1 = ? AND userid2 = ?");
            pstmt.setString(1, userId1);
            pstmt.setString(2, userId2);
            int count=pstmt.executeUpdate();
            if(count==1) {
            	out.writeUTF("成功移除好友");
            }else {
            	out.writeUTF("本就不是好友");
            }
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
