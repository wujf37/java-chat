package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class main {
	public static void main(String[] args) throws Exception{
		Hashtable<String,User> ht=new Hashtable();
		DB db=new DB();
		ServerSocket serverSocket = new ServerSocket(3434);
		Router router=new Router();
	  	System.out.println("Hello，服务器就绪! ");
	  	try 
	  	{	while (true) 
	  	    {	Socket server = serverSocket.accept();
				try  
				{	
					DataInputStream in = new DataInputStream(server.getInputStream());
					DataOutputStream out = new DataOutputStream(server.getOutputStream());
					String clientString=in.readUTF();
					router.Routmsg(clientString,server,db.getcon(),ht);
					//System.out.println(clientString);//debug
				}
				catch (IOException e) 
				{   server.close(); 
				}
			}
		}
	  	catch(Exception e) {
	  		System.out.print(e);
	  	}
		finally 
		{   serverSocket.close();
			db.DBClose();
		}
	}
}

class Router{
	void Routmsg(String clientString,Socket server,Connection con,Hashtable<String,User> uht) {
		int pos = clientString.indexOf("@");
		try{
			if(pos<0) {
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("请求格式出错");
				return;
			}
		    String uid = clientString.substring(0, pos);
		    String umessage = clientString.substring(pos+1);
		    String[] arg = umessage.split("###");
		    //System.out.println(uid+"参数："+umessage);//debug
		    if(uid.equals("登录")) {
		    	//System.out.println("路由到登录处理函数");//debug
		    	LoginTask lt=new LoginTask(arg,server,con,uht);
		    	Thread t=new Thread(lt);
		    	t.start();
		    	//登录@小明###123123
		    	//登录成功，id为:123
		    }
		    else if(uid.equals("退出登录")) {
		    	LogoutTask lt=new LogoutTask(arg,server,uht);
		    	Thread t=new Thread(lt);
		    	t.start();
		    	//退出登录@123
		    	//已退出登录
		    }
		    else if(uid.equals("注册")) {
		    	RegistTask rt=new RegistTask(arg,server,con,uht);
		    	Thread t=new Thread(rt);
		    	t.start();
		    	//注册@小灰###1233211234567
		    	//注册成功，账号为：126
		    }
		    else if(uid.equals("获取好友列表")) {
		    	ShowFriendTask sft=new ShowFriendTask(arg,server,con,uht);
		    	Thread t=new Thread(sft);
		    	t.start();
		    	//获取好友列表@123
		    	//好友列表:###124,小亮###125,小光###126,小晴
		    	//暂无好友
		    }
		    else if(uid.equals("获取群聊列表")) {
		    	ShowGroupTask sgt=new ShowGroupTask(arg,server,con,uht);
		    	Thread t=new Thread(sgt);
		    	t.start();
		    	//获取群聊列表@123
		    	//群聊列表:###1,宿舍群###2,刷单群###3,电诈群
		    	//暂无群聊
		    }
		    else if(uid.equals("私聊")) {
		    	PrivateChatTask pct=new PrivateChatTask(arg,server,con,uht);
		    	Thread t=new Thread(pct);
		    	t.start();
		    	//私聊@123###124###你好
		    	//消息已发送
		    }
		    else if(uid.equals("获取私聊历史")) {
		    	GetSideRecordTask gsrt=new GetSideRecordTask(arg,server,con,uht);
		    	Thread t=new Thread(gsrt);
		    	t.start();
		    	//获取私聊历史@123###124###2023-6-10 22:44:23
		    	//记录：3###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好
		    	//暂无记录
		    }
		    else if(uid.equals("获取私聊新记录")) {
		    	GetSideNewRecordTask gsnrt=new GetSideNewRecordTask(arg,server,con,uht);
		    	Thread t=new Thread(gsnrt);
		    	t.start();
		    	//获取私聊新记录@123###124###2000/1/1 00:00:00
		    	//记录：3###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好
		    	//暂无记录
		    }
		    else if(uid.equals("群聊")) {
		    	GroupChatTask gct=new GroupChatTask(arg,server,con,uht);
		    	Thread t=new Thread(gct);
		    	t.start();
		    	//群聊@123###1###你们好
		    	//------发送者-------
		    	//消息已发送
		    	//------接受者-------
		    	//群聊:1###123###你好
		    }
		    else if(uid.equals("获取群聊记录")) {
		    }
		    else if(uid.equals("退群")) {
		    	ExitGroupTask egt=new ExitGroupTask(arg,server,con,uht);
		    	Thread t=new Thread(egt);
		    	t.start();
		    	
		    }
		    else if(uid.equals("加群")) {
		    }
		    else if(uid.equals("建群")) {
		    	CreateGroupTask cgt=new CreateGroupTask(arg,server,con,uht);
		    	Thread t=new Thread(cgt);
		    	t.start();
		    	//建群@123###青龙学习小组
		    	//建群成功，群号为:4
		    	//群名称重复
		    }
		    else if(uid.equals("拉群")) {
		    }
		    else if(uid.equals("加好友")) {
		    	AddFriendTask aft=new AddFriendTask(arg,server,con,uht);
		    	Thread t=new Thread(aft);
		    	t.start();
		    	//加好友@123###124
		    	//--------主动方请求socket-------
		    	//已是好友关系
		    	//添加成功
		    	//--------被动方监听socket-------
		    	//好友:123###将你添加为好友
		    }
		    else if(uid.equals("删好友")) {
		    }
		    else if(uid.equals("获取群成员列表")) {
		    	
		    }
		    else {
		    	DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("请求格式出错");
				return;
		    }
	    }
		catch(Exception e) {
			System.out.println("请求格式出错，且未完成报错");
			return;
		}
	}
}




class User{
	public String username;
	public String userid;
	Socket server;
	public User(String name,String id,Socket userver) {
		username=name;
		userid=id;
		server=userver;
	}
}