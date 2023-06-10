package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class GroupChatTask implements Runnable{
	String sourceId;
	String targetId;
	String groupmsg;
	Socket server;//此次请求所用socket
	DataOutputStream out;//为此次请求返回响应所用写出流
	Hashtable<String,User> ht;//在线列表
	Connection con=null;
	public GroupChatTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		sourceId=arg[0].trim();
		targetId=arg[1].trim();
		groupmsg=arg[2].trim();
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
		System.out.println("用户"+sourceId+"向群聊"+targetId+"发送消息："+groupmsg);
		try
        {   Statement stmt=con.createStatement();
        	System.out.println("SELECT * FROM usergroup WHERE groupid="+targetId);
        	ResultSet rs=stmt.executeQuery("SELECT * FROM usergroup WHERE groupid="+targetId);
        	Socket send;
        	while(rs.next()) {
        		String onlineuser=rs.getString("userid");
        		User u=ht.get(onlineuser);
        		if(u!=null) {
        			send=u.server;
        			synchronized(send) {
        				DataOutputStream sendout=new DataOutputStream(send.getOutputStream());
        				System.out.print("向在线用户"+onlineuser+"群发消息");
        				sendout.writeUTF("群聊:"+targetId+"###"+sourceId+"###"+groupmsg);
        			}
        		}
        	}
        	rs.close();
        	stmt.close();
        	PreparedStatement pstmt=con.prepareStatement("INSERT INTO grouprecord (userid,groupid,content,time) VALUES(?,?,?,?)");
            pstmt.setString(1,sourceId);
            pstmt.setString(2,targetId);
            pstmt.setString(3, groupmsg);
            Date date=new Date();     //获取一个Date对象
            DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //创建一个格式化日期对象
            String punchTime = simpleDateFormat.format(date);   //格式化后的时间
            pstmt.setString(4, punchTime);
            int count=pstmt.executeUpdate();
            if(count==1) {
                out.writeUTF("消息已发送");
            }else {
            	out.writeUTF("消息未能插入数据库");
            }
            pstmt.close();
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
