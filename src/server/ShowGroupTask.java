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

public class ShowGroupTask implements Runnable{
	String userId;
	Socket server;
	DataOutputStream out;
	Hashtable<String,User> ht;
	private Connection con=null;
	public ShowGroupTask(String[] arg,Socket userver,Connection ucon,Hashtable<String,User> uht) {
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
		System.out.println("用户"+userId+"获取群聊列表");
        try
        {   
            PreparedStatement pstmt=con.prepareStatement("SELECT * FROM usergroup WHERE userid=?");
        	pstmt.setString(1,userId);
        	ResultSet rs = pstmt.executeQuery();
        	List<String> idlist=new ArrayList<String>();
            int count=0;
        	while(rs.next()) 
            {   
            	count++;
            	idlist.add(rs.getString("groupid"));//所有群聊的外键保存为数组
            }
            String ids=String.join(",", idlist);
            rs.close();
            System.out.println(ids);
            if (idlist.size()>0) {
                rs=pstmt.executeQuery("SELECT * FROM chatgroup WHERE groupid in("+ids+")");//用外键进行查找
                String result="群聊列表:"+count;
                while(rs.next()) {
                	result+="###"+rs.getString("groupid")+','+rs.getString("name");
                }
                out.writeUTF(result);
                rs.close();
            }
            else {
            	out.writeUTF("暂无群聊");
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
