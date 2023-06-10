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

public class ExitGroupTask implements Runnable{
	String userId;
	String groupId;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public ExitGroupTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		userId=arg[0].trim();
		groupId=arg[1].trim();
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
		System.out.println("用户"+userId+"退出群聊"+groupId);
        try
        {   
            Statement stmt = con.createStatement();
            PreparedStatement pstmt=con.prepareStatement("DELETE FROM usergroup WHERE userid = ? AND groupid = ?");
            pstmt.setString(1, userId);
            pstmt.setString(2, groupId);
            int count=pstmt.executeUpdate();
            if(count==1) {
            	out.writeUTF("成功退出群聊");
            }else {
            	out.writeUTF("本就不在群聊中");
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
