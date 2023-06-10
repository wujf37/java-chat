package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class GetSideNewRecordTask implements Runnable{
	String starttime;
	String userId1;
	String userId2;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public GetSideNewRecordTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		starttime=arg[2].trim();
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
		System.out.println("用户"+userId1+"获取与用户"+userId2+"的新聊天记录");
        try
        {   
        	String result="";
            PreparedStatement pstmt=con.prepareStatement("SELECT * FROM siderecord WHERE ((senderid = ? AND recieverid = ?)OR(senderid = ? AND recieverid = ?)) AND time > ? ORDER BY time desc");
            pstmt.setString(1,userId1);
            pstmt.setString(2,userId2);
            pstmt.setString(3,userId2);
            pstmt.setString(4,userId1);
            pstmt.setString(5,starttime);
        	ResultSet rs = pstmt.executeQuery();
            int count=0;
        	while(rs.next()) 
            {   count++;
            	result=result+"###"+rs.getString("senderid")+","+rs.getString("time")+","+rs.getString("content");
            }
            rs.close();
            if (count>0) {
                out.writeUTF("记录："+count+result);
            }
            else {
            	out.writeUTF("暂无记录");
            	rs.close();
            }
            server.close();
        }
        catch(SQLException s)  
        {     System.out.println("数据库操作失败: " + s.toString() +s.getErrorCode() + "  " + s.getSQLState());		
        }
        catch(IOException s) {
        	System.out.println("socket操作失败:"+s);
        }
	}
}
