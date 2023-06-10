package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class sender {
	public static void main(String[] args) {
		Socket client;
    	DataInputStream in;
    	DataOutputStream out=null;
    	int userId=0;
		try 
	    {   

	    	while(true) {
				client = new Socket(InetAddress.getLocalHost(), 3434);
				in = new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());
	    		
				//StringBuffer s=new StringBuffer(reader.nextLine());
	    		out.writeUTF("登录@小明###123123");
	    		System.out.println(in.readUTF());
	    		while(true) {
					Socket uclient = new Socket(InetAddress.getLocalHost(), 3434);
					DataInputStream uin = new DataInputStream(uclient.getInputStream());
					DataOutputStream uout = new DataOutputStream(uclient.getOutputStream());
					Scanner reader=new Scanner(System.in);
					StringBuffer us=new StringBuffer(reader.nextLine());
		    		System.out.println("发送请求"+us.toString());
					uout.writeUTF(us.toString());
		    		System.out.println(uin.readUTF());
		    		
		    		uin.close();
		    		uout.close();
		    		uclient.close();
		    	}
	    	}
	    } catch (IOException e){}
	    
	    try 
        {   out.writeUTF(""+userId);
	    	out.flush();
        } 
        catch (IOException e){}
	}
}
