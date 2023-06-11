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

public class AddFriendTask implements Runnable{
	String userId1;
	String userId2;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	Connection con=null;
	public AddFriendTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
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
		System.out.println("用户"+userId1+"尝试将用户"+userId2+"设为好友");
        try
        {   
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM friend where userid1 = " + userId1 + " AND userid2 ="+userId2);
            if(rs.next()) 
            {
                out.writeUTF("本就是好友关系");
            }
            else {
            	PreparedStatement pstmt=con.prepareStatement("INSERT INTO friend (userid1,userid2) VALUES(?,?)");
            	pstmt.setString(1,userId1);
            	pstmt.setString(2, userId2);
            	int count=pstmt.executeUpdate();
                if(count==1) {
                	out.writeUTF("添加成功");
                	User u=ht.get(userId2);
                	if(u!=null) {
            			Socket send=u.server;
            			synchronized(send) {
            				DataOutputStream sendout=new DataOutputStream(send.getOutputStream());
            				System.out.println("向在线用户"+u.userid+"告知好友事件");
            				sendout.writeUTF("好友:"+userId1+"###将你添加为好友");
            			}
            		}
                }else {
                	out.writeUTF("数据库插入失败");
                }
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
