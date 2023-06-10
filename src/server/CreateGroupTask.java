package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class CreateGroupTask implements Runnable{
	String userId;
	String groupname;
	int groupId;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	Connection con=null;
	public CreateGroupTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		userId=arg[0].trim();
		groupname=arg[1].trim();
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
		System.out.println(userId+"尝试创建群聊："+groupname);
        try
        {   
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chatgroup where name = '" + groupname + "'");
            if(rs.next()) 
            {   
                out.writeUTF("群名称重复");
            }
            else {
            	PreparedStatement pstmt=con.prepareStatement("INSERT INTO chatgroup (name) VALUES(?)");
            	pstmt.setString(1,groupname);
            	int count=pstmt.executeUpdate();
                if(count==1) {
                	ResultSet rs2=stmt.executeQuery("SELECT * FROM chatgroup where name='"+groupname+"'");
                	rs2.next();
                	groupId=rs2.getInt("groupid");
                	out.writeUTF("建群成功，群号为:"+rs2.getInt("groupid"));
                	rs2.close();
                }else {
                	out.writeUTF("数据库插入失败");
                }
            	pstmt=con.prepareStatement("INSERT INTO usergroup (userid,groupid) VALUES (?,?)");
            	pstmt.setString(1,userId);
            	pstmt.setInt(2, groupId);
            	count =pstmt.executeUpdate();
            	if(count==1) {
            		System.out.println("成功将创群用户添加到新群聊");
            	}
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
