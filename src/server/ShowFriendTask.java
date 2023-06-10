package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ShowFriendTask implements Runnable{
	String userId;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public ShowFriendTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
		userId=arg[0].trim();
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
		System.out.println("用户"+userId+"获取好友列表");
        try
        {   
            PreparedStatement pstmt=con.prepareStatement("SELECT * FROM friend WHERE userid1=?");
            
        	pstmt.setString(1,userId);
        	ResultSet rs = pstmt.executeQuery();
        	List<String> idlist=new ArrayList<String>();
            while(rs.next()) 
            {   
            	idlist.add(rs.getString("userid2"));//所有好友的外键保存为数组
            }
            String ids=String.join(",", idlist);
            rs.close();
            System.out.println(ids);
            if (idlist.size()>0) {
                rs=pstmt.executeQuery("SELECT * FROM user WHERE userid in("+ids+")");//用外键进行查找
                String result="好友列表:";
                while(rs.next()) {
                	result+="###"+rs.getString("userid")+','+rs.getString("username");
                }
                out.writeUTF(result);
                rs.close();
            }
            else {
            	out.writeUTF("暂无好友");
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
